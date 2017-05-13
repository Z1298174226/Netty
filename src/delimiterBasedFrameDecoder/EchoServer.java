package delimiterBasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.io.*;

/**
 * @author Mr.zhao
 * @date 2016.11.13
 * @version 1.0
 */
public class EchoServer {
    public void bind(int port) throws Exception {
	//构建两个线程组，一个负责客户端的接入，另一个负责向SocketChannle进行读写
    	setOut();
	//mainReactor和subReactor最终合二为一，统一为NioEventLoopGroup
	EventLoopGroup workerGroup =new NioEventLoopGroup();
	EventLoopGroup bossGroup =new NioEventLoopGroup();  /*
	SocketChannel read and write*/
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .option(ChannelOption.SO_BACKLOG, 100)
		    .handler(new LoggingHandler(LogLevel.INFO))
		    .childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch)
				throws Exception {
				//创建分隔符缓冲对象
			    ByteBuf delimiter = Unpooled.copiedBuffer("$_"
				    .getBytes());
			   
			    /*
			     * Unpooled是个帮助类，是一个final class，并且它的构造器也是私有的，
			     * 这意味的无法被别的类继承，也无法通过new运算符来创建一个Unpooled对象。
			     * Unpool类的目的就是用于创建ByteBuf对象。
			     */
			    //创建DelimiterBasedFrameDecoder对象，并加入到Channel.pipeline当中
			    ch.pipeline().addLast(
				    new DelimiterBasedFrameDecoder(1024,//表示单挑消息的最大长大
					    delimiter));
			    ch.pipeline().addLast(new StringDecoder());
			    ch.pipeline().addLast(new EchoServerHandler());
			}
		    });

	    // 缁戝畾绔彛锛屽悓姝ョ瓑寰呮垚鍔�
	    ChannelFuture f = b.bind(port).sync();

	    // 绛夊緟鏈嶅姟绔洃鍚鍙ｅ叧闂�
	    f.channel().closeFuture().sync();
	} finally {
	    // 浼橀泤閫�鍑猴紝閲婃斁绾跨▼姹犺祫婧�
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }
    public void setOut() throws IOException{
    	PrintStream out=new PrintStream(new FileOutputStream
    			(".\\src\\delimiterBasedFrameDecoder\\out.txt"));
    	System.setOut(out);
    }

    public static void main(String[] args) throws Exception {
	int port = 8082;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
	
	    }
	}
	new EchoServer().bind(port);
    }
}
