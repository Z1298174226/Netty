package delimiterBasedFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author Mr.zhao
 * @date 2016.11.13
 * @version 1.0
 */
public class EchoClient {

    public void connect(int port, String host) throws Exception {
	// 閰嶇疆瀹㈡埛绔疦IO绾跨▼缁�
	EventLoopGroup group = new NioEventLoopGroup();
	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch)
				throws Exception {
			    ByteBuf delimiter = Unpooled.copiedBuffer("$_"
				    .getBytes());
			    ch.pipeline().addLast(
				    new DelimiterBasedFrameDecoder(1024,
					    delimiter));
			    ch.pipeline().addLast(new StringDecoder());
			    ch.pipeline().addLast(new EchoClientHandler());
			}
		    });

	    // 鍙戣捣寮傛杩炴帴鎿嶄綔
	    ChannelFuture f = b.connect(host, port).sync();

	    // 褰撲唬瀹㈡埛绔摼璺叧闂�
	    f.channel().closeFuture().sync();
	} finally {
	    // 浼橀泤閫�鍑猴紝閲婃斁NIO绾跨▼缁�
	    group.shutdownGracefully();
	}
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8081;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 閲囩敤榛樿鍊�
	    }
	}
	new EchoClient().connect(port, "127.0.0.1");
    }
}

