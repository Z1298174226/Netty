package niotest;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class TimeServerHandle implements Runnable{
	//private int port;
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private volatile boolean stop;
	public TimeServerHandle(int port){
	//	this.port=port;
		try{
		setOut();
		this.serverChannel=ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		this.selector=Selector.open();
		serverChannel.socket().bind(new InetSocketAddress(port),1024);
		System.out.println("Listenning the connection for the port "+port );
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(1);
		}	
	}
	public void stop(){
		this.stop=true;
	}
	public void run(){
		while(!stop){
			try{
				serverChannel.register(selector, SelectionKey.OP_ACCEPT);
				selector.select();
				Set<SelectionKey> readyKeys=selector.selectedKeys();
				Iterator<SelectionKey> iterator=readyKeys.iterator();
				SelectionKey key=null;
				while(iterator.hasNext()){
					key=iterator.next();
					iterator.remove();
					try{
						serverHandle(key);
					}catch(Exception e){
						if(key!=null){
							key.cancel();
							if(key.channel()!=null)
								key.channel().close();
						}
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);			
			}
		}
	}
	public void serverHandle(SelectionKey key) throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel serverchannel=(ServerSocketChannel) key.channel();
				SocketChannel socketchannel=serverchannel.accept();
				ByteBuffer buffer=ByteBuffer.allocate(1024);
				socketchannel.configureBlocking(false);
				SelectionKey key2=socketchannel.register(selector, SelectionKey.OP_READ
						|SelectionKey.OP_WRITE);
				key2.attach(buffer);
			}
			else if(key.isReadable()){
				SocketChannel socketchannel=(SocketChannel) key.channel();
				ByteBuffer buffer=(ByteBuffer)key.attachment();
				int readBytes=socketchannel.read(buffer);
				if(readBytes>0){
					buffer.flip();
					byte[] bytes=new byte[buffer.remaining()];
					buffer.get(bytes);
					String body=new String(bytes,"UTF-8");
					System.out.println("Recieve the time order request "+body);
					String content=("QUERY TIME ORDER".equalsIgnoreCase(body))?new Date().toString():
						"BAD ORDER";
					doWrite(socketchannel,content);
				
				}
				else if(readBytes<0){
					key.cancel();
				}
				else
					;
			}
		}	
	}
	public void doWrite(SocketChannel channel,String response) throws IOException{
		if(response!=null&&response.trim().length()>0){
			byte[] bytes=response.getBytes();
			ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			channel.write(buffer);
		}
		
	}
	public void setOut() throws IOException{
		PrintStream out=new PrintStream(new FileOutputStream(".\\src\\niotest\\out.txt"));
		System.setOut(out);
	}

}
