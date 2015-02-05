package backend.model.SPHPC;

import java.util.HashMap;

import backend.model.Result;

public class SimpleResult implements Result<String>{
	
	HashMap<String, String> m_storage = new HashMap<String, String>();

	@Override
	public void addValue(String key, String value) {
		m_storage.put(key, value)	;	
	}
	@Override
	public String value(String key) {
		return m_storage.get(key);
	}

}
