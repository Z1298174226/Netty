package httpFileUpstream;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpFileServer {
	
	public void bind(String host,int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE,true)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpFileServerInitializerHandler());
			
		ChannelFuture f = b.bind(host,port).sync();
		System.out.println("The server is listening on the port :"+"http://10.177.235.50:"+port);
		f.channel().closeFuture().sync();
			
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		 final int DEFAULT_PORT = 8080;
		 if(args!=null&& args.length>0){
			 new HttpFileServer().bind(args[1],Integer.parseInt(args[0]));
		 }else{
		 new HttpFileServer().bind("10.177.235.50",DEFAULT_PORT);
		 }
		
	}

}
