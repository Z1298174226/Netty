package aiotest;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.*;
import java.util.concurrent.*;
import java.net.*;
import java.util.*;
public class AsynchronousServerHandler implements Runnable{
     
	//private int port;
	private AsynchronousServerSocketChannel asynServer;
	private CountDownLatch latch;
	public AsynchronousServerHandler(int port){
	//	this.port=port;
		try{
			setOut();
			asynServer=AsynchronousServerSocketChannel.open();
			asynServer.bind(new InetSocketAddress(port));
			System.out.println("The time server is start in port : " + port);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	public void run(){
		latch=new CountDownLatch(1);
		doConnect();
		try{
			latch.await();
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}
	public void doConnect(){
		//public abstract <A> Future<AsynchronousSocketChannel> accept(A attachment,
        //CompletionHandler<AsynchronousSocketChannel,? super A> handler)
		asynServer.accept(this, new CompletionHandler<AsynchronousSocketChannel,
				AsynchronousServerHandler>(){
			@Override
			public void completed(AsynchronousSocketChannel socketChannel,
					AsynchronousServerHandler attachment){
				attachment.asynServer.accept(attachment, this);
				ByteBuffer buffer=ByteBuffer.allocate(1024);
				socketChannel.read(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){
					/**
					 * @Warnings!!
					 * @我在这里定义了一个AsynchronousSocketChannel，但忘记了初始化
					 * @因此运行后总会抛出一个NullPointerException
					 */
					private AsynchronousSocketChannel socketchannel;
					/**@一定要初始化啊*/
					{
						socketchannel=socketChannel;
					}
					@Override
					public void completed(Integer result,ByteBuffer readBuffer){
						readBuffer.flip();
						byte[] bytes=new byte[readBuffer.remaining()];
						readBuffer.get(bytes);
						String body;
						try{
							body=new String(bytes,"UTF-8");
							System.out.println("The time server receive order : " + body);
							String content=("QUERY TIME ORDER".equalsIgnoreCase(body))?
									new Date().toString():"BAD ORDER";
							doWrite(content);
									
						}catch(IOException ex){
							ex.printStackTrace();
						}
						
				}
					@Override
					public void failed(Throwable exc,ByteBuffer readBuffer){
						try{
							socketchannel.close();
							latch.countDown();
						}catch(IOException ex){
							ex.printStackTrace();
						}
						
					}
					public void doWrite(String response){
						if(response!=null&&response.trim().length()>0){
							byte[] bytes=response.getBytes();
							ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
							buffer.put(bytes);
							buffer.flip();
							socketchannel.write(buffer, buffer,
									new CompletionHandler<Integer,ByteBuffer>(){
								@Override
								public void completed(Integer result,ByteBuffer buffer){
									if(buffer.hasRemaining()){
										socketchannel.write(buffer, buffer, this);
									}
									
								}
								@Override
								public void failed(Throwable exc,ByteBuffer buffer){
									try{
										socketchannel.close();
										latch.countDown();
									}catch(IOException ex){
										ex.printStackTrace();
									}
								}
							});
						}
					}
				});
			}
			@Override
			public void failed(Throwable exc,AsynchronousServerHandler attachment){
				   exc.printStackTrace();
					latch.countDown();
						}
			
		});
	}
	public void setOut() throws IOException{
    	PrintStream out=new PrintStream(new FileOutputStream(".\\src\\aiotest\\out.txt"));
    	System.setOut(out);
    }
}
