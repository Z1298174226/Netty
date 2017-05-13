package Interaction;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class InteractionServe {
	public  void bind(int port ) throws Exception{
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, work)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch){
					ch.pipeline().addLast(new InteractionServeHandler());				
				}
			});
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
			
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();			
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		int port = 9091;
		if(args!=null && args.length>0){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception ex){
				
			}
			
		}
		new InteractionServe().bind(port);
	}
}
