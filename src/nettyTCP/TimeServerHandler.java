 package nettyTCP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr.zhao
 * @date 2016.11.12
 * @version 1.0
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
	private int counter;
	//ChannelHandlerAdapter对于网络事件进行读写操作

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	ByteBuf buf = (ByteBuf) msg;
	//ByteBuf类似于ByteBuffer
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);//对比ByteBuffer.get()
	String body = new String(req, "UTF-8").
			substring(0, req.length-System.getProperty("line.separator").length());
	System.out.println("The time server receive order : " + body+"; The counter is: "+ ++counter);
	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
		System.currentTimeMillis()).toString() : "BAD ORDER";
		currentTime=currentTime+System.getProperty("line.separator");
	ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	ctx.write(resp);
	//为了防止频繁地唤醒Slector进行消息发送，netty的write方法并不直接将消息写入SocketChannel中
	//调用write方法只是把待发送的消息放到发送缓冲数组当中，再通过flush方法，将发送缓冲去的消息全部写到SocketChannel当中
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	ctx.close();
    }
}
