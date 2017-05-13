package niotest;

/**
 * 
 * @author zhaoxudong
 * @Date 11.11
 */
public class TimeClient {
	private final static int DEFALUT_PORT=16;
	public static void main(String[] args){
		int port;
		try{
			port=Integer.parseInt(args[0]);
		}catch(RuntimeException ex){
			port=DEFALUT_PORT;
		}
		new Thread(new ClientHandle("localhost",port)).start();
	}

}
