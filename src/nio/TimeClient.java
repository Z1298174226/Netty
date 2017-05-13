package nio;

public class TimeClient {
	private final static int DEFAULT_PORT=15;
	public static void main(String [] args){
		int port;
		try{
			port=Integer.parseInt(args[0]);
		}catch(RuntimeException ex){
			port=DEFAULT_PORT;
		}
		new Thread(new TimeClientHandle("localhost",port),"TimeClient-001").start();
	}

}
