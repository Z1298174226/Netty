package niotest;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class ServerHandler implements Runnable{
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private volatile boolean stop;
	public ServerHandler(int port){
		try{
		serverChannel=ServerSocketChannel.open();
		selector= Selector.open();
		serverChannel.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(false);
		System.out.println("The server is open on the port: "+port);
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		
	}
	
	@Override
	public void run(){
		while(! stop){
			try{
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			selector.select();
			Set<SelectionKey> selectionKeys =selector.selectedKeys();
			Iterator<SelectionKey> iterator =selectionKeys.iterator();
			SelectionKey key= null;
			while(iterator.hasNext()){
				key= iterator.next();
				iterator.remove();
				try{
					serverHandler(key);
				}catch(Exception ex){
					if(key!= null){
						key.cancel();
					}
					if(key.channel() !=null){
						key.channel().close();
					}
					
				}
			}
			}catch(Exception ex){
				ex.printStackTrace();
				System.exit(1);			
			}
		}
	}
		public void serverHandler(SelectionKey key) throws IOException{
			if(key.isValid()){
				if(key.isAcceptable()){
					ServerSocketChannel serverSocketChannel =(ServerSocketChannel) key.channel();
					SocketChannel socketChannel= serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					SelectionKey key2=socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					ByteBuffer byteBuffer= ByteBuffer.allocate(1024);
					key2.attach(byteBuffer);					
				}
				if(key.isReadable()){
					SocketChannel socketChannel =(SocketChannel) key.channel();
					ByteBuffer readBuffer= (ByteBuffer) key.attachment();
					int byteInt=socketChannel.read(readBuffer);
					if(byteInt>0){
						readBuffer.flip();
						byte[] bytes= new byte[readBuffer.remaining()];
						readBuffer.get(bytes);
						String body;
						try{
							body=new String(bytes,"UTF-8");
							System.out.println("Recieve the order: "+body);
							String content= ("QUERY TIME ORDER".equalsIgnoreCase(body))?
									new Date().toString():"BAD ORDER";
							doWrite(socketChannel,content);
						}catch(IOException ex){
							if(key !=null){
								key.cancel();
							}
							if(key.channel()!=null){
								key.channel().close();
							}
							
						}
						
						
					}
					else if(byteInt<0){
						key.cancel();
					}
					
				}
			}
		}
		public void doWrite(SocketChannel socketChannel,String content) throws IOException{
			if(content!=null&&content.trim().length()>0){
				ByteBuffer writeBuffer= ByteBuffer.allocate(1024);
				byte[] bytes=content.getBytes();
				writeBuffer.put(bytes);
				writeBuffer.flip();
				socketChannel.write(writeBuffer);
				
			}
		}
	
	

}
