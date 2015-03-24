package backend.model.result;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.MapKeyClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("DictionaryResult")
public class DictionaryResult extends Result{
	
	@JsonProperty("stringStorage")
	HashMap<String, String> m_sStorage = new HashMap<String, String>();
	
	@JsonProperty("doubleStorage")
	@ElementCollection(targetClass = Double.class)
	Map<String, Double> m_dStorage = new HashMap<String, Double>();

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
	
	@JsonProperty("stringStorage")
	public HashMap<String, String> stringStorage()
	{
		return m_sStorage;
	}

	@JsonProperty("doubleStorage")
	public Map<String, Double> doubleStorage()
	{
		return m_dStorage;
	}
}
