package backend.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import backend.model.serviceprovider.GenericServiceProvider;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Descriptor<T> {
	private Class<T> m_classDescriptor;
	
	@JsonProperty("commonName")
	private String m_commonName;
	
	@JsonProperty("identifier")
	private String m_identifier;
	
	@JsonProperty("pluginIdentifier")
	private boolean m_pluginIdentifier;	
	
	public Descriptor()
	{}
	
	public Descriptor(Class<T> clazz)
	{
		m_classDescriptor = clazz;
		try
		{
			String identifier = m_classDescriptor.getCanonicalName();
			MessageDigest messageDigest;
			messageDigest = MessageDigest.getInstance("SHA");
			messageDigest.update(identifier.getBytes());
			
			identifier = String.format("%040x", new BigInteger(1, messageDigest.digest()));
			identifier(identifier);

			pluginIdentifier(false);
			
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}
	
	public Class<T> classDescriptor()
	{
		return m_classDescriptor;
	}	
	
	@JsonProperty("commonName")
	public void commonName(String name)
	{
		m_commonName = name;
	}
	
	@JsonProperty("commonName")
	public String commonName()
	{
		return m_commonName;
	}
	
	@JsonProperty("pluginIdentifier")
	public void pluginIdentifier(boolean identifier)
	{
		m_pluginIdentifier = identifier;
	}
	
	@JsonProperty("pluginIdentifier")
	public boolean pluginIdentifier()
	{
		return m_pluginIdentifier;
	}
	
	@JsonProperty("identifier")
	public void identifier(String name)
	{
		m_identifier = name;
	}
	
	@JsonProperty("identifier")
	public String identifier()
	{
		return m_identifier;
	}
}
