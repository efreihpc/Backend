package backend.model.result;

import java.util.HashMap;

import javax.persistence.Entity;

import backend.model.Result;

@Entity
public class SimpleResult extends Result<String>{
	
	HashMap<String, String> m_storage = new HashMap<String, String>();

	@Override
	public void value(String key, String value) {
		m_storage.put(key, value)	;	
	}
	@Override
	public String value(String key) {
		return m_storage.get(key);
	}

}
