package fixedLengthFrameDecoderTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
public class EchoServer {
	public void bind(int port) throws Exception{
		/*
		 * 创建两个线程组
		 */
		/*
		 * 用于客户端的连接
		 */
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		/*
		 * 用于向SocketChannel进行读写操作
		 */
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		try{
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					ch.pipeline().addLast(new FixedLengthFrameDecoder(11));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new EchoServerHandler());
				}
			});
			
			ChannelFuture f=b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			
		}
	}
	
	public static void main(String[] args){
		int port = 9092;
		try{
			if(args!=null && args.length>0){
				port = Integer.parseInt(args[0]);
				
			}
			
			new EchoServer().bind(port);
				
			}catch(Exception ex){
				
		}
		
	}

}
