package backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class Result<T> {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
	
	public void id(long id)
	{
		m_id = id;
	}
	
	public long id()
	{
		return m_id;
	}
		
	public abstract void value(String key, T value);
	public abstract T value(String key);
}
