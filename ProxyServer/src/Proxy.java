import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Croley on 10.01.2016
 */

public class Proxy {
	public static void main(String[] args) {
		int port ;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		else
		port =  8080;

		try {
			ControlLists accessControlLists = new ControlLists();
			accessControlLists.loadAccessControlLists();
			new ConfigurationServer(port + 1, accessControlLists).start();
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server started on port: " + port);

			while (true) {
				ProxyThread proxyThread = new ProxyThread(serverSocket.accept(), accessControlLists);
				proxyThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
