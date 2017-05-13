package aio;
import java.io.IOException;

/**
 * @author Mr.zhao
 * @date 2016.11.11
 * @version 1.0
 */
public class TimeServer {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
	int port = 8081;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		
	    }
	}
	AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
	new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}

