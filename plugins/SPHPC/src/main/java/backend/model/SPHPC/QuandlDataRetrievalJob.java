package backend.model.SPHPC;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import ro.fortsoft.pf4j.Extension;
import backend.model.job.JobEntity;
import backend.model.result.JsonResult;
import backend.system.MongoPersistenceUnit;

@Extension
@Entity
@Inheritance
public class QuandlDataRetrievalJob extends JobEntity<JsonResult> {

	@Transient
	HttpClient m_httpClient;
	
	@Transient
	MongoPersistenceUnit m_mongoPersistence;
	
	public QuandlDataRetrievalJob()
	{
		m_httpClient = HttpClientBuilder.create().build();
	}
	
	public void setMongoPersistence(MongoPersistenceUnit mongoPersistence)
	{
		m_mongoPersistence = mongoPersistence;
		result(new JsonResult(m_mongoPersistence));
	}
	
	@Override
	protected void execute() {	
		if(m_mongoPersistence == null)
		{
			System.out.println("QuandlDataRetrievalJob: No Mongopersistence Set!");
		}
		
		HttpGet request = new HttpGet("http://www.quandl.com/api/v1/datasets/GOOG/SWX_VW.json");
		
		try 
		{
			HttpResponse response = m_httpClient.execute(request);
			result().insert(EntityUtils.toString(response.getEntity()));
		} 
		catch (ParseException | IOException e) 
		{
			e.printStackTrace();
		}
	}

}
