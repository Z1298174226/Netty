package lineBasedFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author lilinfeng
 * @date 2014骞�2鏈�14鏃�
 * @version 1.0
 */
public class TimeClient {

    public void connect(int port, String host) throws Exception {
	// 配置客户端NIO线程组
	EventLoopGroup group = new NioEventLoopGroup();
	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch)
				throws Exception {
				//同样直接新增两个解码器
			    ch.pipeline().addLast(
				    new LineBasedFrameDecoder(1024));
			    ch.pipeline().addLast(new StringDecoder());
			    ch.pipeline().addLast(new TimeClientHandler());
			}
		    });

	    // 发起异步连接操作
	    ChannelFuture f = b.connect(host, port).sync();

	    // 等待客户链路关闭
	    f.channel().closeFuture().sync();
	} finally {
	   
	    group.shutdownGracefully();
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
		// 閲囩敤榛樿鍊�
	    }
	}
	new TimeClient().connect(port, "127.0.0.1");
    }
}
