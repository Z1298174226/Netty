package aiotest;
import java.util.concurrent.CountDownLatch;
//import aio.AsyncTimeClientHandler;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.*;
import java.net.*;


public class AsynchronousClientHandler implements Runnable,
CompletionHandler<Void,AsynchronousClientHandler> {
	private AsynchronousSocketChannel client;
	private String host=null;
	private int port;
	private CountDownLatch latch;
	public AsynchronousClientHandler(String host,int port){
		this.host=host;
		this.port=port;
		try{
			client=AsynchronousSocketChannel.open();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	@Override 
	public void run(){
		latch=new CountDownLatch(1);
	    client.connect(new InetSocketAddress(host,port),this,this);
	    try{
	    	latch.await();
	    }catch(InterruptedException ex){
	    	ex.printStackTrace();
	    }try{
	    	client.close();
	    }catch(IOException ex){
	    	ex.printStackTrace();
	    }
	}
	@Override
	public void completed(Void result, AsynchronousClientHandler attachment) {
		byte[] reqs="QUERY TIME ORDER".getBytes();
		ByteBuffer buffer=ByteBuffer.allocate(reqs.length);
		buffer.put(reqs);
		buffer.flip();
		client.write(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>(){
			@Override
			public void completed(Integer integer,ByteBuffer bytebuffer){
				if(bytebuffer.hasRemaining()){
					client.write(bytebuffer,bytebuffer,this);
				}
				else{
					ByteBuffer readBuffer=ByteBuffer.allocate(1024);
					client.read(readBuffer,readBuffer,new CompletionHandler<Integer,ByteBuffer>(){
						@Override
						public void completed(Integer result,ByteBuffer readBuffer){
							readBuffer.flip();
							byte[] bytes=new byte[readBuffer.remaining()];
							readBuffer.get(bytes);
							String body;
							try{
							body=new String(bytes,"UTF-8");
							System.out.println("Now is: "+body);
							latch.countDown();
							}catch(IOException ex){
								ex.printStackTrace();
							}
							
						}
						@Override
						public void failed(Throwable ex,ByteBuffer attachment){
							try{
								client.close();
								latch.countDown();
							}catch(IOException exc){
								ex.printStackTrace();
							}
							
							
						}
					});
				}
			}
			@Override
			public void failed(Throwable exc,ByteBuffer attachment){
				try{
					client.close();
					latch.countDown();
				}catch(IOException ex){
				ex.printStackTrace();
			 }
			}
		});
	}
	@Override
	public void failed(Throwable exc,AsynchronousClientHandler attachment){
		exc.printStackTrace();
		try{
			client.close();
			latch.countDown();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	
		
	}

}
