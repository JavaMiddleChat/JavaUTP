import java.io.*;
import java.net.*;

/**
 * Created by Krowli on 10.01.2016
 */

public class ProxyThread extends Thread {
	private ControlLists accessControlLists;
	private Socket socket = null;
	private static final int buffersize = 1024 * 64;

	public ProxyThread(Socket socket, ControlLists accessControlLists) {///доступ ControlLists
		super("Proxy");

		this.socket = socket;

		this.accessControlLists = accessControlLists;
	}

	private String getURL() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));////метод получает на вход поток подпроцесса.
		String strLine = bufferedReader.readLine();
		 if (strLine == null) return null;
		 	else return strLine.split(" ")[1];
	}

	private InputStream getInputStreamForURL(String URL) throws IOException {
		URL url = new URL(URL);
		URLConnection urlConnect = url.openConnection();
		urlConnect.setDoInput(true);
		urlConnect.setDoOutput(false);

		return urlConnect.getInputStream();
	}

	public void run() {
		try {
			String URL = getURL();
			if (URL != null) {
				System.out.println("Request url: " + URL);
				String clientIp = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString();
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				if (accessControlLists.isAllowed(clientIp.substring(1), URL)) {

					try {
						InputStream inputStream = getInputStreamForURL(URL);
						int index;
						byte buffer[] = new byte[buffersize];
						while ((index = inputStream.read(buffer, 0, buffersize)) != -1) {
							dataOutputStream.write(buffer, 0, index);
						}
						dataOutputStream.flush();
					} catch (Exception e) {
						System.err.println(e.toString());
						dataOutputStream.writeBytes("");
					}
				} else {
					dataOutputStream.writeBytes("HTTP/1.1 403 Forbidden\r\n");
					dataOutputStream.writeBytes("403 Forbidden");
				}
			} else System.err.println("not found the URL of the request");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
