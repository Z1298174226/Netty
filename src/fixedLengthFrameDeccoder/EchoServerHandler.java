package fixedLengthFrameDeccoder;

import io.netty.channel.*;
public class EchoServerHandler extends ChannelHandlerAdapter {
       @Override
       public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
    	   System.out.println("Recieve client : ["+msg+"] ");
    	   
       }
       @Override
       public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
    	   cause.printStackTrace();
    	   ctx.close();
       }
}
