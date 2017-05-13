package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr.zhao
 * @date 2016.11.12
 * @version 1.0
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
	//ChannelHandlerAdapter���������¼����ж�д����

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	ByteBuf buf = (ByteBuf) msg;
	//ByteBuf������ByteBuffer
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);//�Ա�ByteBuffer.get()
	String body = new String(req, "UTF-8");
	System.out.println("The time server receive order : " + body);
	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
		System.currentTimeMillis()).toString() : "BAD ORDER";
	ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	ctx.write(resp);
	//Ϊ�˷�ֹƵ���ػ���Slector������Ϣ���ͣ�netty��write��������ֱ�ӽ���Ϣд��SocketChannel��
	//����write����ֻ�ǰѴ����͵���Ϣ�ŵ����ͻ������鵱�У���ͨ��flush�����������ͻ���ȥ����Ϣȫ��д��SocketChannel����
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	ctx.close();
    }
}
