package aio;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author Mr.zhao
 * @date 2016.11.11
 * @version 1.0
 */
public class AcceptCompletionHandler implements
	CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
       //�������첽��������˴���һ��CompletionHandler���͵�Handlerʵ������accept���ճɹ���ͨ����Ϣ
    @Override
    public void completed(AsynchronousSocketChannel result,
	    AsyncTimeServerHandler attachment) {
	attachment.asynchronousServerSocketChannel.accept(attachment, this);
	//��Ȼ�Ѿ����տͻ��˳ɹ��ˣ�Ϊʲô��Ҫ�ٴε���accept������
	//������AsynchronousServerSocketChannel��accept����֮��������µĿͻ������ӣ�ϵͳ���ص����Ǵ���
	//CompletioinHandlerʵ����completed��������ʾ�µĿͻ��˽���ɹ�����Ϊһ��AsyncronousServerSocketChannel
	//���������ڶ�Ŀͻ��ˣ�������Ҫ������������accept���������������ͻ��˵���Ϣ�������γ�һ��ѭ����ÿ������һ���ͻ������ӳɹ�֮��
	//���첽�����µĿͻ�������
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
	exc.printStackTrace();
	attachment.latch.countDown();  //�����������̬��Gate�򿪣������߳�ͨ��
    }

}
