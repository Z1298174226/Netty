package niotest;

public class TimeServer {
	private final static int DEFAULT_PORT=16;
	public static void main(String[] args){
		int port;
		try{
			port=Integer.parseInt(args[0]);
		}catch(RuntimeException ex){
			port=DEFAULT_PORT;
		}
		
		new Thread(new TimeServerHandle(port)).start();
	}

}
