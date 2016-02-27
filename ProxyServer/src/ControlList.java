import java.util.*;

/**
 * Created by Krowli on 17.01.2016
 */

public class ControlList {
	boolean type;
	Map<String, List<String>> accessControlList = new HashMap<>();//// Мап - объкт с ключом и значением. Хеш только стринги использовать.

	public ControlList(boolean type) {
		this.type = type;
	}/// Контроль листа, динай или эллоу

	public boolean isAllowed(String ip, String page) throws Exception {
		List<String> pages = accessControlList.get(ip);
		if (pages == null)
				throw new Exception();
		boolean allowed = pages.contains(page);///содержит страницу
		if (!type)///Лист заприщаюший инвертировать значение
			allowed = !allowed;
		return allowed;
	}
///фильтрацыя клиента
	public void addRecord(String records) { ///Запись
		String[] elements = records.split(" ");
		List<String> listHost = accessControlList.get(elements[0]);
		if (listHost == null) {
			listHost = new ArrayList<>();
			accessControlList.put(elements[0], listHost);
		}
		if (elements.length > 1) listHost.addAll(Arrays.asList(elements).subList(1, elements.length));//Получаем массив как лист, выдиляем под лист начиная с 1 элемента
	}

	public void removeRecord(String record) { //// удаляем
		String[] elements = record.split(" ");
		if (elements.length == 1) accessControlList.remove(elements[0]);
		else {
			List<String> pages = accessControlList.get(elements[0]);
			if (pages != null)
				for (int index = 1; index < elements.length; index++) {
				pages.remove(elements[index]);
			}
		}
	}

	public String toString() {

		String result = "";

		for (Map.Entry<String, List<String>> entry : accessControlList.entrySet())//элемент списка
		{
			result = result + entry.getKey() +
					" " + String.join(" ", entry.getValue()) + "\n";///////Возвращает ключ, соответствующий этой записи.
		}
		return result;
	}
}
