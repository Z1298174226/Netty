package aio;
/**
 * @author Mr.zhao
 * @date 2016.11.11
 * @version 1.0
 */
public class TimeClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
	int port = 8081;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		
	    }

	}
	new Thread(new AsyncTimeClientHandler("127.0.0.1", port),
		"AIO-AsyncTimeClientHandler-001").start();

    }
}
