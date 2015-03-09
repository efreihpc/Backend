package backend.model.SPHPC;

import com.jimmoores.quandl.MetaDataRequest;
import com.jimmoores.quandl.MetaDataResult;
import com.jimmoores.quandl.QuandlSession;

import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

public class FinanceDataRetrievalService extends ServicePlugin<JsonResult> {

	@Override
	public void execute() {
		QuandlSession session = QuandlSession.create();
		MetaDataResult metaData = session.getMetaData(MetaDataRequest.of("GOOG/SWX_VW"));
		
		result(new JsonResult(persistenceUnit()));
		result().insert(metaData.toPrettyPrintedString());
	}

}
