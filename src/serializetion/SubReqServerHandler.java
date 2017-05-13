package serializetion;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

//import com.phei.netty.codec.pojo.SubscribeReq;
//import com.phei.netty.codec.pojo.SubscribeResp;

/**
 * @author Mr.zhao
 * @date 2016.11.16
 * @version 1.0
 */
@Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	SubcribeReq req = (SubcribeReq) msg;
	if ("zhaoxudong".equalsIgnoreCase(req.getUserName())) {
	    System.out.println("Service accept client subscrib req : ["
		    + req.toString() + "]");
	    ctx.write(resp(req.getSubReqID()));
	}
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
    	ctx.flush();
    	
    }

    private SubscribeResq resp(int subReqID) {
	SubscribeResq resp = new SubscribeResq();
	resp.setSubReqID(subReqID);
	resp.setRespCode(0);
	resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
	return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	cause.printStackTrace();
	ctx.close();// 鍙戠敓寮傚父锛屽叧闂摼璺�
    }
   
}
