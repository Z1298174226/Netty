package delimiterBasedFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr.zhao
 * @date 2016.11.16
 * @version 1.0
 */
@Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
    	//由于DelimiterBasedFrameDecoder自动对请求消息进行了解码，后续的ChannleHandler接受到的MSg对象就是完整的消息包
	String body = (String) msg;
	System.out.println("This is " + ++counter + " times receive client : ["
		+ body + "]");
	body += "$_";
	ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
	ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	cause.printStackTrace();
	ctx.close();// 鍙戠敓寮傚父锛屽叧闂摼璺�
    }
}
