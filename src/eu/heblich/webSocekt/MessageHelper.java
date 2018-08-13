package eu.heblich.webSocekt;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Set;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MessageHelper {

	public static HashMap<String, String> readToHashMap(String message){
    	HashMap<String, String> element = new HashMap<>();
    	try (JsonReader reader = new JsonReader(new StringReader(message));) {
            //JsonObject jsonMessage = reader.readObject();
    		
    		reader.beginObject();
    		while (reader.hasNext()) {
				String name = reader.nextName();
				if(reader.hasNext()){
					String contend = reader.nextString();
					element.put(name, contend);
				}else{
					element.put(name, null);
				}
			}
    		reader.endObject();
    		reader.close();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return element;
    }
	
	public static String makeFromHashMap(HashMap<String, String> map){
		 JsonWriter writer;
		 StringWriter sw = new StringWriter();
	     try {
			writer = new JsonWriter(sw);
					
			writer.beginObject(); // {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				writer.name(key).value(map.get(key));
			}
			writer.endObject(); // }
			writer.close();
					
	     } catch (IOException e) {
	    	 e.printStackTrace();
	     }
		return sw.toString();
	}
}
