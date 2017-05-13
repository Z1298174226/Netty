package lineBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * @author lilinfeng
 * @date 2014骞�2鏈�14鏃�
 * @version 1.0
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
	    .getLogger(TimeClientHandler.class.getName());

    private int counter;

    private byte[] req;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
    	//所发送的消息以换行符结束
	req = ("QUERY TIME ORDER" + System.getProperty("line.separator"))
		.getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	ByteBuf message = null;
	for (int i = 0; i < 100000; i++) {
	    message = Unpooled.buffer(req.length);
	    message.writeBytes(req);
	    ctx.writeAndFlush(message);
	}
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	String body = (String) msg;
	//此时的msg已经是解码成字符串之后的应答消息
	System.out.println("Now is : " + body + " ; the counter is : "
		+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	// 閲婃斁璧勬簮
	logger.warning("Unexpected exception from downstream : "
		+ cause.getMessage());
	ctx.close();
    }
}
