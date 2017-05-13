package userInfo;

/**
 * @author Administrator
 * @date 2016.11.13
 * @version 1.0
 * 
 */
import java.io.Serializable;
/**
 * ��ͨ��ʵ�� java.io.Serializable �ӿ������������л����ܡ�δʵ�ִ˽ӿڵ��ཫ�޷�ʹ���κ�״̬���л������л��������л�������������ͱ����ǿ����л��ġ����л��ӿ�û�з������ֶΣ������ڱ�ʶ�����л������塣

Ҫ���������л�������������л������Լٶ��������͸��𱣴�ͻָ������͵Ĺ��� (public)���ܱ����� (protected) �ͣ�����ɷ��ʣ��� (package) �ֶε�״̬��������������չ������һ���ɷ��ʵ��޲������췽������ʼ�������״̬ʱ���ſ��Լٶ��������д�ְ������������������������һ����Ϊ�����л����Ǵ���ġ��ô���������ʱ��⵽��

�ڷ����л������У���ʹ�ø���Ĺ��û��ܱ������޲������췽����ʼ���������л�����ֶΡ������л�����������ܹ������޲������췽���������л�������ֶν��Ӹ����лָ���

������һ��ͼ��ʱ�����ܻ�������֧�� Serializable �ӿڵĶ����ڴ�����£����׳� NotSerializableException��������ʶ�������л�������ࡣ

�����л��ͷ����л���������Ҫ���⴦��������ʹ������׼ȷǩ����ʵ�����ⷽ����

 private void writeObject(java.io.ObjectOutputStream out)
     throws IOException
 private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException;
 private void readObjectNoData() 
     throws ObjectStreamException;
 
 */
import java.nio.ByteBuffer;
public class UserInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //������һ��Ĭ�ϵ����к�
    private String userName;

    private int userID;

    public UserInfo buildUserName(String userName) {
	this.userName = userName;
	return this;
    }

    public UserInfo buildUserID(int userID) {
	this.userID = userID;
	return this;
    }

    /**
     * @return the userName
     */
    public final String getUserName() {
	return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public final void setUserName(String userName) {
	this.userName = userName;
    }

    /**
     * @return the userID
     */
    public final int getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            the userID to set
     */
    public final void setUserID(int userID) {
	this.userID = userID;
    }

    public byte[] codeC() {
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	byte[] value = this.userName.getBytes();
	buffer.putInt(value.length);
	buffer.put(value);
	buffer.putInt(this.userID);
	buffer.flip();
	value = null;
	byte[] result = new byte[buffer.remaining()];
	buffer.get(result);
	return result;
    }

    public byte[] codeC(ByteBuffer buffer) {
	buffer.clear();
	byte[] value = this.userName.getBytes();
	buffer.putInt(value.length);
	buffer.put(value);
	buffer.putInt(this.userID);
	buffer.flip();
	value = null;
	byte[] result = new byte[buffer.remaining()];
	buffer.get(result);
	return result;
    }
}

