package serializetion;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author Mr.zhao
 * @date 2016.11.16
 * @version 1.0
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

    /**
     * Creates a client-side handler.
     */
    public SubReqClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	for (int i = 0; i < 1000; i++) {
	    ctx.write(subReq(i));
	}
	ctx.flush();
    }

    private SubcribeReq subReq(int i) {
	SubcribeReq req = new SubcribeReq();
	req.setAddress("西安电子科技大学南校区");
	req.setPhoneNumber("180xxxxxxxx");
	req.setProductName("Netty 权威指南");
	req.setSubReqID(i);
	req.setUserName("zhaoxudong");
	return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	System.out.println("Receive server response : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	cause.printStackTrace();
	ctx.close();
    }
}
