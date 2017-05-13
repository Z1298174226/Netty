package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * @author Mr.zhao
 * @date 2016.11.12
 * @version 1.0
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
	    .getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
	byte[] req = "QUERY TIME ORDER".getBytes();
	firstMessage = Unpooled.buffer(req.length);
	firstMessage.writeBytes(req);

    }
   
    /**
     * 重点关注
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	ctx.writeAndFlush(firstMessage);
    }
    //当客户端和服务器链路建立成功之后，Netty的NIO线程会调用channelActive方法，发送查询时间的指令给服务端，调用
    //ChannelHandlerContext的writeAndFlush方法将请求消息发送给服务端
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	ByteBuf buf = (ByteBuf) msg;
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);
	String body = new String(req, "UTF-8");
	System.out.println("Now is : " + body);
	//System.out.println(logger.toString());
    }
    //当服务端返回应答消息时，调用channelRead

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	
	logger.warning("Unexpected exception from downstream : "
		+ cause.getMessage());
	System.out.println(logger.toString());
	ctx.close();
    }
}
