package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.Extension;

import com.jimmoores.quandl.MetaDataRequest;
import com.jimmoores.quandl.MetaDataResult;
import com.jimmoores.quandl.QuandlSession;

import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class FinanceDataRetrievalService extends ServicePlugin<JsonResult> {

	public FinanceDataRetrievalService()
	{
		super();
		commonName("Finance Data Retrieval Service");
	}
	
	@Override
	public void execute() {
		QuandlSession session = QuandlSession.create();
		MetaDataResult metaData = session.getMetaData(MetaDataRequest.of("GOOG/SWX_VW"));
		
		result(new JsonResult(persistenceUnit()));
		result().insert(metaData.toPrettyPrintedString());
	}

}
