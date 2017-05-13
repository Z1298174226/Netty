package nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Administrator
 * @date 2016.11.10
 * @version 1.0
 */
public class TimeClientHandle implements Runnable {
//通过创建TimeClientHandle线程来处理异步连接和读写操作
    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
	this.host = host == null ? "localhost" : host;
	this.port = port;
	try {
	    selector = Selector.open();
	    socketChannel = SocketChannel.open();
	    socketChannel.configureBlocking(false);
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

   
    @Override
    public void run() {
	try {
	    doConnect();
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
	while (!stop) {
	    try {
		selector.select(1000);
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
	    } catch (Exception e) {
		e.printStackTrace();
		System.exit(1);
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
	 
	    SocketChannel sc = (SocketChannel) key.channel();
	    if (key.isConnectable()) {//处于连接状态，说明服务器返回ACK应答消息
		if (sc.finishConnect()) {//客户端连接成功
		    sc.register(selector, SelectionKey.OP_READ);
		    doWrite(sc);
		} else
		    System.exit(1);
	    }
	    if (key.isReadable()) {
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		int readBytes = sc.read(readBuffer);
		if (readBytes > 0) {
		    readBuffer.flip();
		    byte[] bytes = new byte[readBuffer.remaining()];
		    readBuffer.get(bytes);
		    String body = new String(bytes, "UTF-8");
		    System.out.println("Now is : " + body);
		    this.stop = true;
		} else if (readBytes < 0) {
		
		    key.cancel();
		    sc.close();
		} else
		    ; 
	    }
	}

    }

    private void doConnect() throws IOException {
	
	if (socketChannel.connect(new InetSocketAddress(host, port))) {
	    socketChannel.register(selector, SelectionKey.OP_READ);
	    doWrite(socketChannel);
	} else//如果没有直接连接成功，说明服务器没有返回TCP握手应答消息，但这并不代表连接失败
		//我们需要将SocketChannel注册Selector上，注册SelectionKey.OP_CONNECT
		//当服务器返回TCPSYN-ACK应答消息后，Selector就能轮询到这个SocketChannel处于连接状态
	    socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    private void doWrite(SocketChannel sc) throws IOException {
	byte[] req = "QUERY TIME ORDER".getBytes();
	ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
	writeBuffer.put(req);
	writeBuffer.flip();
	sc.write(writeBuffer);
	if (!writeBuffer.hasRemaining())
	    System.out.println("Send order 2 server succeed.");
    }

}
