package fixedLengthFrameDecoderTest;

import io.netty.channel.*;

public class EchoServerHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		System.out.println(msg);
	}
	/*
	 * (non-Javadoc)
	 * ChannelHandlerContextһ����������Ϣ����
	 * @see io.netty.channel.ChannelHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}

}
