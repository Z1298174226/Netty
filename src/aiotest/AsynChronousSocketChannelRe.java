package aiotest;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class AsynChronousSocketChannelRe implements Runnable,CompletionHandler<Void,
AsynChronousSocketChannelRe> {
	private AsynchronousSocketChannel socketChannel;
	private CountDownLatch latch;
	private String host=null;
	private int port ;
	public AsynChronousSocketChannelRe(String host,int port){
		this.host=host;
		this.port=port;
		try{
		socketChannel=AsynchronousSocketChannel.open();
		}catch(IOException ex){
			
		}
	}
	public void run(){
		socketChannel.connect(new InetSocketAddress(host,port),this,this);
		latch=new CountDownLatch(1);
		try{
			latch.await();
		}catch(InterruptedException ex){
			
		}try{
			socketChannel.close();
		}catch(IOException ex){
			
		}
	}
	@Override
	public void completed(Void result,AsynChronousSocketChannelRe attachment){
		byte[] bytes="QUERY TIME ORDER".getBytes();
		ByteBuffer readBuffer=ByteBuffer.allocate(bytes.length);
		readBuffer.put(bytes);
		readBuffer.flip();
		socketChannel.write(readBuffer, readBuffer, new CompletionHandler<Integer,ByteBuffer>(){
			@Override
			public void completed(Integer result,ByteBuffer readBuffers){
				if(readBuffers.hasRemaining()){
					socketChannel.write(readBuffers, readBuffers,this);
				}
				else{
					ByteBuffer buffer=ByteBuffer.allocate(1024);
					socketChannel.read(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){
						@Override
						public void completed(Integer result,ByteBuffer readbuffer){
							readbuffer.flip();
							byte[] bytes=new byte[readbuffer.remaining()];
							readbuffer.get(bytes);
							String body;
							try{
								body=new String(bytes,"UTF-8");
								System.out.println("Now is :"+body);
								latch.countDown();
							}catch(IOException ex){
								
							}
						}
						@Override
						public void failed(Throwable ex,ByteBuffer readbuffer){
							try{
								socketChannel.close();
								latch.countDown();
							}catch(IOException exc){
								
							}
						}
					});
					
			}
			}
			@Override
			public void failed(Throwable exc,ByteBuffer readBuffers){
				try{
					socketChannel.close();
					latch.countDown();
				}catch(IOException ex){
					
				}
			}
		});
		
	}
	@Override
	public void failed(Throwable ex,AsynChronousSocketChannelRe attachment){
		try{
		latch.countDown();
		socketChannel.close();
	}catch(IOException exc){
		
	}

}
}
