package aiotest;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
public class AsynchronouServerHandlerRe implements Runnable{
   private CountDownLatch latch;
   private AsynchronousServerSocketChannel serverChannel;
   public AsynchronouServerHandlerRe(int port){
	   try{
		   setOut();
	   serverChannel=AsynchronousServerSocketChannel.open();
	   serverChannel.bind(new InetSocketAddress(port));
	   System.out.println("The time server is started in :"+port);
	   }catch(IOException ex){
		   ex.printStackTrace();
	   }
   }
   public void run(){
	   latch=new CountDownLatch(1);
	   doConnected();
	   try{
		   latch.await();
	   }catch(InterruptedException ex){
		   
	   }
	   
   }
   public void doConnected(){
	   serverChannel.accept(this,new  CompletionHandler<AsynchronousSocketChannel,
				AsynchronouServerHandlerRe>(){
		   @Override
		   public void completed(AsynchronousSocketChannel socketChannel,AsynchronouServerHandlerRe attachment){
			   attachment.serverChannel.accept(attachment,this);
			   ByteBuffer readBuffer=ByteBuffer.allocate(1024);
			   socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer,ByteBuffer>(){
				  private AsynchronousSocketChannel socketchannel;
				  {
					  socketchannel=socketChannel;
				  }
				   @Override
				   public void completed(Integer result,ByteBuffer readBuffers){
					   readBuffers.flip();
					   byte[] bytes=new byte[readBuffers.remaining()];
					   readBuffers.get(bytes);
					   String body;
					   try{
						   body=new String(bytes,"UTF-8");
						   System.out.println("The server recieve the order: "+body);
						   String content=("QUERY TIME ORDER".equalsIgnoreCase(body)?
								   new Date().toString():"BAD ORDER");
						   doWrite(content);
					   }catch(IOException ex){
						   ex.printStackTrace();
					   }
					   
				   }
				   @Override
				   public void failed(Throwable exc,ByteBuffer readBuffers){
					try{
						socketchannel.close();
						latch.countDown();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				   }
				   public void doWrite(String Content){
					   if(Content!=null&&Content.trim().length()>0){
						   byte[] bytes=Content.getBytes();
						   ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
						   writeBuffer.put(bytes);
						   writeBuffer.flip();
						   socketchannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer,
								   ByteBuffer>(){
							   @Override
							   public void completed(Integer result,ByteBuffer writeBuffers){
								   if(writeBuffers.hasRemaining()){
									   socketchannel.write(writeBuffers,writeBuffers,this);
								   }
							   }
							   @Override
							   public void failed(Throwable ex,ByteBuffer writeBuffers){
								   try{
									   socketchannel.close();
									   latch.countDown();
								   }catch(Exception exc){
									   exc.printStackTrace();
								   }
							   }
						   });
					   }
					   
				   }
				   
			   });
		   }
		   @Override
		   public void failed(Throwable exc,AsynchronouServerHandlerRe attachment){
			   latch.countDown();
			   try{
				   serverChannel.close();
			   }catch(IOException ex){
			   ex.printStackTrace();
			   }
		   }
		   
	   });
   }
   public void setOut() throws IOException{
	   PrintStream out=new PrintStream(new FileOutputStream(".\\src\\aio\\outRe.txt"));
	   System.setOut(out);
   }
}
