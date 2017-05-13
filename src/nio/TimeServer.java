package nio;

public class TimeServer {
	private final static int DEFAULT_PORT=15;
	public static void main(String[] args){
		int port;
			try{
				port=Integer.parseInt(args[0]);
			}catch(RuntimeException e){
				port=DEFAULT_PORT;
			}
		
		MultiplexerTimeServer timeServer=new MultiplexerTimeServer(port);
		new Thread(timeServer,"NIO-MultiplexerTimServer-001").start();
	}

}
