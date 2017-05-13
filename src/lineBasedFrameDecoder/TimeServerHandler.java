package lineBasedFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr.zhao
 * @date 2016.11.13
 * @version 1.0
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	String body = (String) msg;
	//接收到的msg就是删除回车换行符后的请求信息
	/**
	 * ByteBuf buf=(ByteBuf) mas;
	 * byte[] req=new byte[buf.readableBytes()];
	 * buf.readBytes(req);
	 * String body=new String(req,"UTF-8"）.
	 * substring(0,req.length-System.gerProperty("line.separator").length());
	 */
	System.out.println("The time server receive order : " + body
		+ " ; the counter is : " + ++counter);
	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
		System.currentTimeMillis()).toString() : "BAD ORDER";
	currentTime = currentTime + System.getProperty("line.separator");
	ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	ctx.close();
    }
}
