package lineBasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import java.io.*;
/**
 * @author Mr.zhao
 * @date 2016.11.12
 * @version 1.0
 * netty具有多种解码器来处理TCP粘包
 */
public class TimeServer {

    public void bind(int port) throws Exception {
	//配置服务端的NIO线程组
    setOut();
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    //新增了两个解码器
		    .option(ChannelOption.SO_BACKLOG, 1024)
		    .childHandler(new ChildChannelHandler());
	    // 绑定端口，同步等待成功
	    ChannelFuture f = b.bind(port).sync();

	    // 等待服务器监听端口关闭
	    f.channel().closeFuture().sync();
	} finally {
	    // 释放线程池资源
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		//新增了两个解码器
		//LineBaseFrameDecoder
	    arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
	    //StringDecoder
	    arg0.pipeline().addLast(new StringDecoder());
	    arg0.pipeline().addLast(new TimeServerHandler());
	}
    }
    public void setOut(){
    	try{
    	PrintStream out=new PrintStream(new FileOutputStream(".\\src\\lineBasedFrameDecoder\\out.txt"));
    	System.setOut(out);
    	}catch(IOException ex){
    		
    	}
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8080;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		
	    }
	}
	new TimeServer().bind(port);
    }
}
