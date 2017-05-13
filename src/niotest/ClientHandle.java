package niotest;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandle implements Runnable {
	private int port;
	private String host;
	private volatile boolean stop;
	private SocketChannel socketChannel;
	private Selector selector;
	public ClientHandle(String host,int port){
		this.host=(host!=null)?host:"localhost";
		this.port=port;
		try{
			socketChannel=SocketChannel.open();
			selector=Selector.open();
			socketChannel.configureBlocking(false);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	public void stop(){
		this.stop=true;
	}
	public void run(){
		try{
			doConnet();			
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(1);			
		}
		while(!stop){
			try{
				selector.select(1000);
				Set<SelectionKey> readyKeys=selector.selectedKeys();
				Iterator<SelectionKey> iterator=readyKeys.iterator();
				SelectionKey key=null;
				while(iterator.hasNext()){
					 key=iterator.next();
					 iterator.remove();
					 try{
						 handleKey(key);
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
		if(selector!=null){
			try{
				selector.close();
			}catch(IOException ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
	public void handleKey(SelectionKey key) throws IOException{
		if(key.isValid()){
			SocketChannel socketchannel=(SocketChannel) key.channel();
		if(key.isConnectable()){
			if(socketchannel.finishConnect()){
			socketchannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketchannel);
			}else{
				System.exit(1);
			}
		}else if(key.isReadable()){
			ByteBuffer buffer =ByteBuffer.allocate(1024);
			int readBytes = socketchannel.read(buffer);
			if (readBytes > 0) {
			    buffer.flip();
			    byte[] bytes = new byte[buffer.remaining()];
			    buffer.get(bytes);//相对批量 get方法
			                        //此方法将此缓冲区的字节传输到给定的目标数组中
			    String body = new String(bytes, "UTF-8");
			    System.out.println("Now is : " + body);
			    this.stop = true;
			} else if (readBytes < 0) {		
			    key.cancel();
			    socketchannel.close();
			} else
			    ; 
		    }
		}
			
}
	public void doConnet() throws IOException{
		if(socketChannel.connect(new InetSocketAddress(host,port))){
			socketChannel.register(selector,SelectionKey.OP_READ);
			doWrite(socketChannel);
		}else{
			socketChannel.register(selector,SelectionKey.OP_CONNECT);
		}
	}
	public void doWrite(SocketChannel channel) throws IOException{
		byte[] bytes="QUERY TIME ORDER".getBytes();
		ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		channel.write(buffer);
		if(!buffer.hasRemaining()){
			System.out.println("Send Order Time Succeed");
		}
	}

}
