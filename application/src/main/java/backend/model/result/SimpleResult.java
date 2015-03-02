package backend.model.result;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("SPHPCSimpleResult")
public class SimpleResult extends Result<String>{
	
	@JsonProperty("storage")
	HashMap<String, String> m_storage = new HashMap<String, String>();

	@Override
	public void value(String key, String value) {
		m_storage.put(key, value)	;	
	}
	@Override
	public String value(String key) {
		return m_storage.get(key);
	}
	
	@JsonProperty("storage")
	public HashMap<String, String> storage()
	{
		return m_storage;
	}

}
