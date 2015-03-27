package backend.model.result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.MapKeyClass;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("DictionaryResult")
public class DictionaryResult extends Result{
	
	@JsonIgnore
	HashMap<String, String> m_sStorage = new HashMap<String, String>();
	
	@JsonIgnore
	@ElementCollection(targetClass = Double.class)
	Map<String, Double> m_dStorage = new HashMap<String, Double>();
	
	public static DictionaryResult fromStringMap(HashMap<String, String> map)
	{
		DictionaryResult newResult = new DictionaryResult();
		newResult.stringStorage(map);
		return newResult;
	}

	public void value(String key, String value) {
		m_sStorage.put(key, value)	;	
	}
	
	public void value(String key, double value) {
		m_dStorage.put(key, value)	;	
	}

	public String stringValue(String key) {
		return m_sStorage.get(key);
	}
	
	public double doubleValue(String key) {
		return m_dStorage.get(key);
	}
	
	@JsonIgnore
	public HashMap<String, String> stringStorage()
	{
		return m_sStorage;
	}
	
	@JsonIgnore
	public void stringStorage(HashMap<String, String> storage)
	{
		m_sStorage = storage;
	}

	@JsonIgnore
	public Map<String, Double> doubleStorage()
	{
		return m_dStorage;
	}
	
	@JsonIgnore
	public void doubleStorage(HashMap<String, Double> storage)
	{
		m_dStorage = storage;
	}

	@Override
	public String allJson() {
		Document result = new Document();
		
		result.putAll(m_sStorage);
		result.putAll(m_dStorage);
		
		System.out.println(result);
		return result.toJson();
	}

	@Override
	public void fromJsonString(String allData) {
		ObjectMapper mapper = new ObjectMapper();
		
		try 
		{
			stringStorage((HashMap<String, String>) mapper.readValue(allData, new TypeReference<HashMap<String, String>>(){}));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
