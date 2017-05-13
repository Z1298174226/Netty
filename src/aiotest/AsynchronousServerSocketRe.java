package aiotest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class AsynchronousServerSocketRe implements Runnable {
	
	AsynchronousServerSocketChannel serverChannel;
	CountDownLatch latch;
	public AsynchronousServerSocketRe(int port){
		try{
		serverChannel = AsynchronousServerSocketChannel.open();
		serverChannel.bind(new InetSocketAddress(port));
		System.out.println("Started Server at the port of " + port);
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		latch = new CountDownLatch(1);
		doConnected();
		try{
			latch.await();
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		
	}

	private void doConnected() {
		serverChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel,AsynchronousServerSocketRe>(){

			@Override
			public void completed(AsynchronousSocketChannel socketChannels, AsynchronousServerSocketRe attachment) {
				attachment.serverChannel.accept(attachment, this);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socketChannels.read(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){
                    
					AsynchronousSocketChannel socketChannel;
					{
						socketChannel = socketChannels;
					}
					@Override
					public void completed(Integer result, ByteBuffer attachment) {
						byte[] bytes = new byte[attachment.remaining()];
						attachment.flip();
						attachment.get(bytes);
						String body;
						try{
							body = new String(bytes,"UTF-8");
							String content = (body.equalsIgnoreCase("QUERY TIME ORDER")?
									new Date().toString():"BAD QUERY!");
							doWtite(content);
							
						}catch(IOException e){
							
						}
						
					}

					private void doWtite(String content) {
						if(content!=null&&content.trim().length()!=0){
						ByteBuffer buffer = ByteBuffer.allocate(content.length());
						byte[] bytes = content.getBytes();
						buffer.put(bytes);
						buffer.flip();
						socketChannel.write(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){

							@Override
							public void completed(Integer result, ByteBuffer attachment) {
								if(attachment.hasArray()){
									socketChannel.write(attachment,attachment,this);
								}
								
							}

							@Override
							public void failed(Throwable exc, ByteBuffer attachment) {
								try{
									latch.countDown();
									socketChannel.close();
								}catch(Exception e){
									
								}
								
							}
							
						});
						}
						
					}

					@Override
					public void failed(Throwable exc, ByteBuffer attachment) {
						try{
							latch.countDown();
							socketChannel.close();
						}catch(Exception e){
							
						}
						
					}
					
				});
				
				
			}

			@Override
			public void failed(Throwable arg0, AsynchronousServerSocketRe arg1) {
				try{
					latch.countDown();
					serverChannel.close();
				}catch(Exception e){
					
				}
				
			}
			
		});
		
	}
	
	public static void main(String[] args){
		int port = 8066;
		new Thread(new AsynchronousServerSocketRe(8066)).start();
	}

}
