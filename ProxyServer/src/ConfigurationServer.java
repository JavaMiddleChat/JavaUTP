import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Krowli on 17.01.2016
 */

public class ConfigurationServer extends Thread {
	private int port;
	private ControlLists accessControlLists;

	public ConfigurationServer(int port, ControlLists accessControlLists) {
		this.port = port;
		this.accessControlLists = accessControlLists;
	}

	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());///получаеи стрим для вывода
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());////получаем стрим для ввода

				if (dataInputStream.readUTF().equals("PASSWORD")) {////проверям строку, пришло из вне
					dataOutputStream.writeBoolean(true);
					String[] request;///запрос
					while (true) {
						request = dataInputStream.readUTF().split(" ");//разбиваем сроку. a b c


						if (request[0] == "policy")
							if (request.length == 2) {
								accessControlLists.setDefaultPolicy(request[1].equals("allow"));///устанавливаем стандартное поведенне allow
								dataOutputStream.writeUTF("OK");
							} else dataOutputStream.writeUTF("request syntax error");

						if (request[0] =="add")
							if (request.length > 2) {
								String record = String.join(" ", Arrays.copyOfRange(request, 2, request.length));///копирует от 2 до конца массива копирует
								accessControlLists.addRecord(request[1].equals("allow"), record);
								dataOutputStream.writeUTF("OK");
							} else dataOutputStream.writeUTF("request syntax error");

						if (request[0] =="remove")
							if (request.length > 2) {
								String record = String.join(" ", Arrays.copyOfRange(request, 2, request.length));
								accessControlLists.removeRecord(request[1].equals("allow"), record);
								dataOutputStream.writeUTF("OK");
							} else dataOutputStream.writeUTF("request syntax error");

						if (request[0] == "")
							dataOutputStream.writeUTF("request not recognized");

					}
				} else dataOutputStream.writeBoolean(false);
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
