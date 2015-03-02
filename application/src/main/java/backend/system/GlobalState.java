package backend.system;

import java.util.HashMap;

public class GlobalState {
	
	static HashMap<String, Object> m_container = new HashMap<String, Object>();
	
	public static <T> T get(String key)
	{
		return (T) m_container.get(key);
	}
	
	public static void set(String key, Object value)
	{
		m_container.put(key, value);
	}
}
