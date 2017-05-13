package Interaction;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.*;

public class InteractionServeHandler extends ChannelHandlerAdapter{
	
	private static final Logger logger = Logger.getLogger(InteractionServeHandler.class.getName());
	
	@Override
	public void channelRead(ChannelHandlerContext ctx , Object msg){
		ByteBuf requery = (ByteBuf) msg;
		byte[] bytes = new byte[requery.readableBytes()];
		requery.readBytes(bytes);
		String readContent = null;
		try{
		readContent = new String(bytes,"UTF-8");
		//String content = "QUERY TIME ORDER".equalsIgnoreCase(readContent)?
		//		new Date().toString():"BAD ORDER";
		String content = new Date().toString();
		ByteBuf buffer = Unpooled.copiedBuffer(content.getBytes());
	    ctx.writeAndFlush(buffer);
		}catch(Exception ex){
			
		}
		
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		logger.warning("Unexcepted exception from downStream "+cause.getMessage());
		System.out.println(logger.toString());
		ctx.close();
		
	}

}
