package serializetion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Mr.zhao
 * @date 2016.11.16
 * @version 1.0
 */
public class SubReqServer{
	public void bind(int port) throws Exception{
		EventLoopGroup bossGroup =new NioEventLoopGroup();
		EventLoopGroup workGroup =new NioEventLoopGroup();
		try{
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch){
					ch.pipeline().addLast(new ObjectDecoder(
							1024*1024,ClassResolvers.weakCachingConcurrentResolver
							(this.getClass().getClassLoader())));
					//pipeline().addLast(new ObjectEncoder());
					//新增一个ObjectEncoder，它可以在消息放送的时候自动将实现Serializable的POJO对象进行编码
					//因此用户无需亲自对对象进行手工序列化
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new SubReqServerHandler());
					
				}
			});
			/**
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch){
					ch.pipeline().addLast(new ObjectDecoder(
							1024*1024,ClassResolvers
							.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new SubReqServerHandler());
				}
			});
			*/
			ChannelFuture f=b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

/**
 public class SubReqServer {
    public void bind(int port) throws Exception {
	// 配置服务端NIO线程组
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .option(ChannelOption.SO_BACKLOG, 100)
		    .handler(new LoggingHandler(LogLevel.INFO))
	        .childHandler(new ChannelInitializer<SocketChannel>() {    	
			@Override
			public void initChannel(SocketChannel ch) {
			    ch.pipeline()
				    .addLast(
					    new ObjectDecoder(
						    1024 * 1024,
						    ClassResolvers
							    .weakCachingConcurrentResolver(this
								    .getClass()
								    .getClassLoader())));
			    ch.pipeline().addLast(new ObjectEncoder());
			    ch.pipeline().addLast(new SubReqServerHandler());
			}
		    });

	   
	    ChannelFuture f = b.bind(port).sync();

	
	    f.channel().closeFuture().sync();
	} finally {
	   
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

*/
    public static void main(String[] args) throws Exception {
	int port = 8083;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
	
	    }
	}
	setOut();
	new SubReqServer().bind(port);
    }
    public static  void  setOut() throws IOException{
    	PrintStream out=new PrintStream(new FileOutputStream(".\\src\\serializetion\\out.txt"));
    	System.setOut(out);
    }
}
