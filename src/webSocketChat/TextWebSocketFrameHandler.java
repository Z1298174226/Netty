package webSocketChat;

//import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 
 * @author Mr.zhao
 * 2016.12.13
 */
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
	/*
	 * If you need to broadcast a message to more than one {@link Channel}, you can
     * add the {@link Channel}s associated with the recipients and call {@link ChannelGroup#write(Object)}:
     * <pre>
     * <strong>{@link ChannelGroup} recipients =
     *         new {@link DefaultChannelGroup}({@link GlobalEventExecutor}.INSTANCE);</strong>
     * recipients.add(channelA);
     * recipients.add(channelB);
     * ..
     * <strong>recipients.write({@link Unpooled}.copiedBuffer(
     *         "Service will shut down for maintenance in 5 minutes.",
     *         {@link CharsetUtil}.UTF_8));</strong>
     * </pre>
	 */
	
	/*
	 * public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();
	 */
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception { // (1)
		Channel incoming = ctx.channel();
		//channels.writeAndFlush(new TextWebSocketFrame(msg.text()));
		for (Channel channel : channels) {
            if (channel != incoming){
                channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
            } else {
            	channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
            }
        }
	}
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
        channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
        
        channels.add(incoming);
		System.out.println("Client:"+incoming.remoteAddress() +"加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
        channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
        
		System.out.println("Client:"+incoming.remoteAddress() +"离开");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }
	    
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"在线");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"掉线");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	// (7)
			throws Exception {
    	Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"异常");
        cause.printStackTrace();
        ctx.close();
	}

}