package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Mr.zhao
 * @date 2016.11.12
 * @version 1.0
 */
public class TimeServer {

    public void bind(int port) throws Exception {
	
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	//public abstract interface io.netty.channel.EventLoopGroup extends io.netty.util.concurrent.EventExecutorGroup {
	  
	  // Method descriptor #5 ()Lio/netty/channel/EventLoop;
	 // public abstract io.netty.channel.EventLoop next();
	//}
	//NioEventLoopGroup是一个线程组，它包含了一组NIO线程，专门用于网络事件的处理，实际上它们就是Reactor线程组
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	//创建两个，一个用于服务端接受客户端的连接，另一个用于进行SocketChannel的网络读写
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    //ServerBootstrap类用于启动NIO服务端的辅助启动类，目的是降低服务端的开发难度
	    b.group(bossGroup, workerGroup)
	    // public io.netty.bootstrap.ServerBootstrap group
	    //(io.netty.channel.EventLoopGroup parentGroup, 
	    //io.netty.channel.EventLoopGroup childGroup);
		    .channel(NioServerSocketChannel.class)
		    // public io.netty.bootstrap.ServerBootstrap channel(java.lang.Class channelClass);
		    .option(ChannelOption.SO_BACKLOG, 1024)
		    .childHandler(new ChildChannelHandler());
	    // public io.netty.bootstrap.ServerBootstrap childHandler
	    //(io.netty.channel.ChannelHandler childHandler);
	    //将两个NIO线程组作为参数传递到ServerBootstrap当中
	    //接着创建的Channel为NioServerSocketChannel，对应于JDKNIO类库中的ServerSocketChannel
	    //接着配置NioServerSocketChannel的TCP参数
	    //最后绑定IO事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的handler类，主要用于处理网络IO事件
	    ChannelFuture f = b.bind(port).sync();
	    //public abstract interface io.netty.channel.ChannelFuture extends
	                                    //io.netty.util.concurrent.Future {
	    // public io.netty.channel.ChannelFuture bind(int inetPort);
	    // public abstract io.netty.channel.ChannelFuture sync() 
	                                             //throws java.lang.InterruptedException;
        //绑定端口，同步等待成功
	    //完成之后，netty会返回一个ChannelFuture
	    f.channel().closeFuture().sync();
	    // public abstract io.netty.channel.Channel channel();
	    //public abstract io.netty.channel.ChannelFuture closeFuture();
	    //等待服务器监听端口关闭
	} finally {
	    //退出，释放线程池资源
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }
    
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	@Override
	//protected abstract void initChannel(io.netty.channel.Channel arg0) throws java.lang.Exception;
	protected void initChannel(SocketChannel arg0) throws Exception {
	    arg0.pipeline().addLast(new TimeServerHandler());
	    //public abstract io.netty.channel.ChannelPipeline pipeline();
	    //public abstract interface io.netty.channel.ChannelPipeline extends java.lang.Iterable 
	    // public abstract io.netty.channel.ChannelPipeline addLast
	    //(java.lang.String arg0, io.netty.channel.ChannelHandler arg1);
	    
	}

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8092;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		
	    }
	}
	new TimeServer().bind(port);
    }
}


