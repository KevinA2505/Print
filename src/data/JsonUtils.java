package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
 * Reciclaje
 */
public class JsonUtils<T> { // generic class

	private String filePath; // JSON path for process.

	/*
	 * add jars in the path.
	 */
	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	/*
	 * constructor.
	 */
	public JsonUtils(String filePath) {
		this.filePath = filePath;
	}

	public void save(T t) throws StreamReadException, DatabindException, IOException {
		/*
		 * Obtain the class using the T object.
		 */
		List<T> list = getElements((Class<T>) t.getClass());
		list.add(t);
		/*
		 * Drop the list. write.
		 */
		mapper.writeValue(new File(this.filePath), list);
	}

	public void delete(T elementToDelete) throws IOException {
		List<T> list = getElements((Class<T>) elementToDelete.getClass());

		list.removeIf(item -> item.equals(elementToDelete));

		mapper.writeValue(new File(this.filePath), list);
	}

	/*
	 * this method is for update take the entire object and change by the new.
	 */
	public void update(String key, String value, T newObject) throws IOException {
		File jsonFile = new File(filePath);
		// read the json as an array, add a cast.
		ArrayNode jsonArray = (ArrayNode) mapper.readTree(jsonFile);

		/*
		 * reference
		 */
		boolean updated = false;

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonNode jsonNode = jsonArray.get(i);

			if (jsonNode.has(key) && jsonNode.get(key).asText().equals(value)) {
				jsonArray.set(i, mapper.valueToTree(newObject));
				updated = true;
				break;
			}
			if (updated) {
				mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonArray);
				System.out.println("Updated object with " + key + " = " + newObject);
			} else {
				System.out.println("No object found with " + key + " = " + newObject);
			}
		}

	}

	/*
	 * Class parameter, is necessary because the Json need to know what Object is
	 * working. add Throws for the exceptions: StreamReadException,
	 * DatabindException, IOException. Learn this method.
	 */
	public List<T> getElements(Class<T> temp) throws StreamReadException, DatabindException, IOException {
		File file = new File(filePath);

		if (!file.exists()) {
			return new ArrayList<T>();
		}

		/*
		 * return with the object temp.
		 */
		return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, temp));

	}

}
