package nettyTCP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * @author Mr.zhao
 * @date 2016.11.13
 * @version 1.0
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
	    .getLogger(TimeClientHandler.class.getName());

    private int counter;

    private byte[] req;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
	req = ("QUERY TIME ORDER" + System.getProperty("line.separator"))
		.getBytes();
    }
	/**public static String getProperty(String key)
	获取指定键指示的系统属性。
	首先，如果有安全管理器，则用该键作为其参数来调用 checkPropertyAccess 方法。结果可能导致 SecurityException。

	如果没有当前系统属性的集合，则首先用与 getProperties 方法相同的方式创建并初始化系统属性的集合。

	参数：
	key - 系统属性的名称。
	返回：
	系统属性的字符串值，如果没有带有此键的属性，则返回 null。
	
	 * @System.getProperty("line.separator")
     * @平台独立的换行符。
     
    public static final String LS = " " + System.getProperty("line.separator");
 
还有很多其它参数
os.name                Operating system name   
os.arch                Operating system architecture   
os.version            Operating system version   
file.separator            File separator ("/" on UNIX)   
path.separator            Path separator (":" on UNIX)   
line.separator            Line separator ("\n" on UNIX)   
user.name            User's account name   
user.home            User's home directory   
user.dir                User's current working directory 
*/
    

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	ByteBuf message = null;
	for (int i = 0; i < 10000; i++) {
	    message = Unpooled.buffer(req.length);
	    message.writeBytes(req);
	    ctx.writeAndFlush(message);
	    //发送即刷新
	}
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
    	ByteBuf buf=(ByteBuf) msg;
    	byte[] req=new byte[buf.readableBytes()];
    	buf.readBytes(req);
	String body = new String(req,"UTF-8");
	System.out.println("Now is : " + body + " ; the counter is : "
		+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

	logger.warning("Unexpected exception from downstream : "
		+ cause.getMessage());
	ctx.close();
    }
}
