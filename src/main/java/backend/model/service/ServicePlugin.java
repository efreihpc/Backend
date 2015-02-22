package backend.model.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import backend.model.result.Result;

@Entity
@Inheritance
public abstract class ServicePlugin<T extends Result> extends ServiceEntity<T> {
	public ServicePlugin()
	{
		super();
		
		try
		{
			MessageDigest messageDigest;
			messageDigest = MessageDigest.getInstance("SHA");	
			String identifier = "local";
			identifier = String.format("%040x", new BigInteger(1, messageDigest.digest()));
			
			descriptor().pluginIdentifier(identifier);
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}
}
