package backend.model.SPHPC;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class FinanceDataRetrievalService extends ServicePlugin<JsonResult> {
	
	@Transient
	HttpClient m_httpClient;

	public FinanceDataRetrievalService()
	{
		super();
		commonName("Finance Data Retrieval Service");
		m_httpClient = new DefaultHttpClient();
	}
	
	@Override
	public void execute() {
		result(new JsonResult(persistenceUnit()));
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
