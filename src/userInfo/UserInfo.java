package userInfo;

/**
 * @author Administrator
 * @date 2016.11.13
 * @version 1.0
 * 
 */
import java.io.Serializable;
/**
 * 类通过实现 java.io.Serializable 接口以启用其序列化功能。未实现此接口的类将无法使其任何状态序列化或反序列化。可序列化类的所有子类型本身都是可序列化的。序列化接口没有方法或字段，仅用于标识可序列化的语义。

要允许不可序列化类的子类型序列化，可以假定该子类型负责保存和恢复超类型的公用 (public)、受保护的 (protected) 和（如果可访问）包 (package) 字段的状态。仅在子类型扩展的类有一个可访问的无参数构造方法来初始化该类的状态时，才可以假定子类型有此职责。如果不是这种情况，则声明一个类为可序列化类是错误的。该错误将在运行时检测到。

在反序列化过程中，将使用该类的公用或受保护的无参数构造方法初始化不可序列化类的字段。可序列化的子类必须能够访问无参数构造方法。可序列化子类的字段将从该流中恢复。

当遍历一个图形时，可能会遇到不支持 Serializable 接口的对象。在此情况下，将抛出 NotSerializableException，并将标识不可序列化对象的类。

在序列化和反序列化过程中需要特殊处理的类必须使用下列准确签名来实现特殊方法：

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
    //生成了一个默认的序列号
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

