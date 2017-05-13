package aio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.io.*;

/**
 * @author Administrator
 * @date 2016.11.11
 * @version 1.0
 */
public class AsyncTimeServerHandler implements Runnable {
	
  
    //private int port;
    CountDownLatch latch;       //����һ��ͬ�������ࡪ������
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;   //һ���첽ͨ��
    //��д�첽ͨ�����������أ�������IO���֮ǰ�ͻ����
    public AsyncTimeServerHandler(int port) {
	//this.port = port;
	try {
		setOut();
	    asynchronousServerSocketChannel = AsynchronousServerSocketChannel
		    .open();
	    asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
	    System.out.println("The time server is start in port : " + port);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

	latch = new CountDownLatch(1);  //д�������ֵΪ1
	doAccept();
	try {
	    latch.await();  //Gate�رգ������߳��ڴ���������ֹ���������˳�
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public void doAccept() {
	asynchronousServerSocketChannel.accept(this,
		new AcceptCompletionHandler());
    }
    
    public void setOut() throws IOException{
    	PrintStream out=new PrintStream(new FileOutputStream(".\\src\\aio\\out.txt"));
    	System.setOut(out);
    }

}
