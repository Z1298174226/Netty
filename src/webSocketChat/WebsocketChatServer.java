package webSocketChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
/**
 * Websocket 
 * 
 * @author Mr.zhao
 * @date 2016.12.13
 */
public class WebsocketChatServer {

    private int port;

    public WebsocketChatServer(int port) {
        this.port = port;
    }
     /*
      * 在我们的应用中，当 URL 请求以“/ws”结束时，我们才升级协议为WebSocket。否则，服务器将使用基本的
              HTTP/S。一旦升级连接将使用的WebSocket 传输所有数据。
      */
    public void run() throws Exception {
       
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	ServerBootstrap b = new ServerBootstrap();
      //      ServerBootstrap b = new ServerBootstrap(); // (2)
        	b.group(bossGroup, workerGroup)
        	.channel(NioServerSocketChannel.class)
        	.handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new WebsocketChatServerInitializer())  //(4)
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            
    		//System.out.println("WebsocketChatServer 监听端口" + port);
    		
           
            ChannelFuture f = b.bind("192.168.43.44",port).sync(); // (7)
            System.out.println("WebsocketChatServer 监听端口" + "http://192.168.43.44:"
        		    +port);
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            
    		System.out.println("WebsocketChatServer 关闭");
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8088;
        }
        new WebsocketChatServer(port).run();

    }
}