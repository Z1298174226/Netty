package nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

/**
 * @author Administrator
 * @date 2016.11.10
 * @version 1.0
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel servChannel;

    private volatile boolean stop;         //同步
    /**
     * 初始化多路复用器，绑定监听串口
     * 
     * @param port
     */

   
    public MultiplexerTimeServer(int port) {
	try {
		setOut();
	    selector = Selector.open();
	    servChannel = ServerSocketChannel.open();
	    servChannel.configureBlocking(false);
	    servChannel.socket().bind(new InetSocketAddress(port), 1024);
	    servChannel.register(selector, SelectionKey.OP_ACCEPT);
	    System.out.println("The time server is start in port : " + port);
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public void stop() {
	this.stop = true;
    }

   
    @Override
    public void run() {
	while (!stop) {
	    try {
		selector.select(1000);  //timeout - 如果为正，则在等待某个通道准备就绪时最多阻塞 timeout 毫秒
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> it = selectedKeys.iterator();
		SelectionKey key = null;
		while (it.hasNext()) {
		    key = it.next();
		    it.remove();
		    try {
			handleInput(key);
		    } catch (Exception e) {
			if (key != null) {
			    key.cancel();
			    if (key.channel() != null)
				key.channel().close();
			}
		    }
		}
	    } catch (Throwable t) {
		t.printStackTrace();
	    }
	}

	
	if (selector != null)
	    try {
		selector.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
    }

    private void handleInput(SelectionKey key) throws IOException {

	if (key.isValid()) {
	    
	    if (key.isAcceptable()) {
		// Accept the new connection
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssc.accept();
		sc.configureBlocking(false);
		// Add the new connection to the selector
		SelectionKey key2=sc.register(selector, SelectionKey.OP_READ);
		ByteBuffer readBuffer=ByteBuffer.allocate(1024);
		key2.attach(readBuffer);
	    }
	    if (key.isReadable()) {
		// Read the data
		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer readBuffer = (ByteBuffer)key.attachment();
		int readBytes = sc.read(readBuffer);
		if (readBytes > 0) {
		    readBuffer.flip();
		    byte[] bytes = new byte[readBuffer.remaining()];
		    readBuffer.get(bytes);
		    String body = new String(bytes, "UTF-8");
		    System.out.println("The time server receive order : "
			    + body);
		    String currentTime = "QUERY TIME ORDER"
			    .equalsIgnoreCase(body) ? new java.util.Date(
			    System.currentTimeMillis()).toString()
			    : "BAD ORDER";
			    //将此 String 与另一个 String 比较，
			    //不考虑大小写。如果两个字符串的长度相同，
			    //并且其中的相应字符都相等（忽略大小写），则认为这两个字符串是相等的。
		    doWrite(sc, currentTime);
		} else if (readBytes < 0) {
		 
		    key.cancel();
		    sc.close();
		} else
		    ; 
	    }
	}
    }

    private void doWrite(SocketChannel channel, String response)
	    throws IOException {
	if (response != null && response.trim().length() > 0) {
	    byte[] bytes = response.getBytes();
	    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
	    writeBuffer.put(bytes);
	    writeBuffer.flip();
	    channel.write(writeBuffer);
	}
    }
   public void setOut(){
	   try{
		     PrintStream out=new PrintStream((new FileOutputStream(".\\src\\nio\\out.txt")));
	         System.setOut(out);
	   }catch(IOException ex){
		   
	   }
   }
}
