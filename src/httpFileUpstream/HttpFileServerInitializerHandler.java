package httpFileUpstream;

import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
public class HttpFileServerInitializerHandler 
                extends ChannelInitializer<SocketChannel>{
	@Override
	public void initChannel(SocketChannel ch) throws Exception{
		ch.pipeline().addLast(new HttpRequestDecoder());
		ch.pipeline().addLast(new HttpResponseEncoder());
		ch.pipeline().addLast(new HttpContentCompressor());
		ch.pipeline().addLast(new HttpFileHandler());
		ch.pipeline().addLast(new ChunkedWriteHandler());
		ch.pipeline().addLast(new HttpObjectAggregator(1048576));
	
	}

}
