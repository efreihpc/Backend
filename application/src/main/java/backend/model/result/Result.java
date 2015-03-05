package backend.model.result;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ro.fortsoft.pf4j.ExtensionPoint;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public abstract class Result implements ExtensionPoint {
	
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
		
//	public abstract void value(String key, T value);
//	public abstract T value(String key);
}
