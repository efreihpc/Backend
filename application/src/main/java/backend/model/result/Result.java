package backend.model.result;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import ro.fortsoft.pf4j.ExtensionPoint;

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
		
	public abstract void value(String key, String value);
	public abstract String stringValue(String key);
	public abstract void value(String key, double value);
	public abstract double doubleValue(String key);
	
	@JsonProperty("storage")
	public abstract String allJson();
	
	@JsonProperty("storage")
	public abstract void fromJsonString(String allData);
}
