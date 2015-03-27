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
import backend.model.job.JobPlugin;
import backend.model.result.JsonResult;
import backend.model.result.Result;
import backend.system.MongoPersistenceUnit;

@Extension
@Entity
@Inheritance
public class QuandlDataRetrievalJob extends JobPlugin<JsonResult> {

	@Transient
	HttpClient m_httpClient;
	
	public QuandlDataRetrievalJob()
	{
		m_httpClient = HttpClientBuilder.create().build();
		result(new JsonResult());
	}
	
	@Override
	public void execute() {		
		Result configuration = configuration();
		String stockId = configuration.stringValue("stockId");
		String authToken = configuration.stringValue("OauthToken");
		HttpGet request = new HttpGet("http://www.quandl.com/api/v1/datasets/" + stockId + ".json?sort_order=asc?auth_token=" + authToken);
		
		try 
		{
			HttpResponse response = m_httpClient.execute(request);
			result().insert(EntityUtils.toString(response.getEntity()));
			System.out.println("QuandlDataRetrievalJob: Added information to result!");
		} 
		catch (ParseException | IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void configured() {
		// TODO Auto-generated method stub
		
	}

}
