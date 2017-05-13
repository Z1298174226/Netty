package aio;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author Mr.zhao
 * @date 2016.11.11
 * @version 1.0
 */
public class AcceptCompletionHandler implements
	CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
       //由于是异步操作，因此传递一个CompletionHandler类型的Handler实例接收accept接收成功的通道消息
    @Override
    public void completed(AsynchronousSocketChannel result,
	    AsyncTimeServerHandler attachment) {
	attachment.asynchronousServerSocketChannel.accept(attachment, this);
	//既然已经接收客户端成功了，为什么还要再次调用accept方法？
	//当调用AsynchronousServerSocketChannel的accept方法之后，如果有新的客户端连接，系统将回调我们传入
	//CompletioinHandler实例的completed方法，表示新的客户端接入成功，因为一个AsyncronousServerSocketChannel
	//可以连接众多的客户端，所以需要继续调用它的accept方法，接受其他客户端的消息，最终形成一个循环。每当接受一个客户读连接成功之后
	//再异步接受新的客户端连接
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
	exc.printStackTrace();
	attachment.latch.countDown();  //闭锁到达完成态，Gate打开，允许线程通过
    }

}
