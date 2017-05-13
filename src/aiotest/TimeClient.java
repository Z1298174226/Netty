package aiotest;

public class TimeClient {
	private final static int DEFAULT_PORT=4001;
	public static void main(String[] args){
		int port;
		try{
			port=Integer.parseInt(args[0]);
		}catch(RuntimeException ex){
			port=DEFAULT_PORT;
		}
		new Thread(new AsynChronousSocketChannelRe("localhost",port)).start();
		
	}

}
