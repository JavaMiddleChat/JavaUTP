import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Krowli on 17.01.2016
 */

public class ControlLists {
	 boolean defaultPolicy = true;
	 ControlList allowAccessControlList = new ControlList(true), ///разришающий и заприщающий
	 datyAccessControlList = new ControlList(false);

	public boolean isAllowed(String ip, String page) { ///Допускается
		System.out.println(ip + " " + page);

		boolean allow = defaultPolicy;//политика по умолчанию
		try {
			allow = allowAccessControlList.isAllowed(ip, page);////разрешить доступ ControlList
		} catch (Exception e) {
			try {
				allow = datyAccessControlList.isAllowed(ip, page);
			} catch (Exception ignored) {
			////Ignor me
			}
		}
		return allow;
	}

	public void addRecord(boolean type, String record) {////д. записи
		if (type)
			allowAccessControlList.addRecord(record);
		else
			datyAccessControlList.addRecord(record);

		saveControlLists();
	}

	public void removeRecord(boolean type, String record) {
		if (type)
			allowAccessControlList.removeRecord(record);
		else
			datyAccessControlList.removeRecord(record);

		saveControlLists();
	}

	public void setDefaultPolicy(boolean defaultPolicy) { ////
		this.defaultPolicy = defaultPolicy;
		saveControlLists(); // сохроняет контрольный лист
	}

	public void loadAccessControlLists() {///списки доступу
		try {
			String string;
			Scanner scanner = new Scanner(new File("Policy.txt"));///конфигурации
			ControlList currentList = allowAccessControlList;///текущий список
			setDefaultPolicy(scanner.nextLine().equals("True"));///уст. стандартна полис, если Тру то тру, если нет то фолс
			while (scanner.hasNext()) {
				string = scanner.nextLine();
				if (string.length() > 0)

					if( string =="ACCESS_CONTROL_LIST_ALLOW") currentList = allowAccessControlList;

					if( string =="ACCESS_CONTROL_LIST_DENY") currentList = datyAccessControlList;

					if( string == "") currentList.addRecord(string);


			}
			scanner.close();
		} catch (FileNotFoundException ignored) {}
	}
	public void saveControlLists() {
		try {

			FileWriter writer = new FileWriter("Policy.txt"); ///записывает в файл дальше после обработки
			writer.write((
					defaultPolicy ? "TRUE" : "FALSE") +
					"\nACCESS_CONTROL_LIST_ALLOW\n" +
					allowAccessControlList.toString() +
					"\nACCESS_CONTROL_LIST_DENY\n" +
					datyAccessControlList.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
