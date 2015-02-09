package backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Result<T> {
	
	@Id
	private int m_uid;
	
	public void uid(int uid)
	{
		m_uid = uid;
	}
	
	public int uid()
	{
		return m_uid;
	}
		
	public abstract void value(String key, T value);
	public abstract T value(String key);
}
