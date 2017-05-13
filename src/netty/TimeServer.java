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
	//NioEventLoopGroup��һ���߳��飬��������һ��NIO�̣߳�ר�����������¼��Ĵ���ʵ�������Ǿ���Reactor�߳���
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	//����������һ�����ڷ���˽��ܿͻ��˵����ӣ���һ�����ڽ���SocketChannel�������д
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    //ServerBootstrap����������NIO����˵ĸ��������࣬Ŀ���ǽ��ͷ���˵Ŀ����Ѷ�
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
	    //������NIO�߳�����Ϊ�������ݵ�ServerBootstrap����
	    //���Ŵ�����ChannelΪNioServerSocketChannel����Ӧ��JDKNIO����е�ServerSocketChannel
	    //��������NioServerSocketChannel��TCP����
	    //����IO�¼��Ĵ�����ChildChannelHandler����������������Reactorģʽ�е�handler�࣬��Ҫ���ڴ�������IO�¼�
	    ChannelFuture f = b.bind(port).sync();
	    //public abstract interface io.netty.channel.ChannelFuture extends
	                                    //io.netty.util.concurrent.Future {
	    // public io.netty.channel.ChannelFuture bind(int inetPort);
	    // public abstract io.netty.channel.ChannelFuture sync() 
	                                             //throws java.lang.InterruptedException;
        //�󶨶˿ڣ�ͬ���ȴ��ɹ�
	    //���֮��netty�᷵��һ��ChannelFuture
	    f.channel().closeFuture().sync();
	    // public abstract io.netty.channel.Channel channel();
	    //public abstract io.netty.channel.ChannelFuture closeFuture();
	    //�ȴ������������˿ڹر�
	} finally {
	    //�˳����ͷ��̳߳���Դ
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


