package backend.model.SPHPC;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.bson.Document;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.DictionaryResult;
import backend.model.result.JsonResult;
import backend.model.result.Result;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class MonteCarloService extends ServicePlugin<JsonResult> {
	
	@Transient
	MonteCarloJob m_job;
	
	public MonteCarloService()
	{
		commonName("Monto Carlo Service");
	}
	
	@Override
	protected void configured() {
		addDependency("ad42c7ece9fd7f97a3b7daf3b55460a72c25f98a", "b80b17302e4da7adc8599b9fb302a3b48cbee13e");
	}

	@Override
	public void execute() {		
//		System.out.println("MonteCarloService> Preparing data for MonteCarlo Job");
		JsonResult deprep = (JsonResult) dependencies().get(0).result();
		Document stockDataDocument = deprep.findOne("{}");
		
		stockDataDocument = (Document) stockDataDocument.get("storage");
		ArrayList<ArrayList> stockData = (ArrayList<ArrayList>) stockDataDocument.get("data");
		
		
		double formerClose = -1;
		double[] logReturn = new double[stockData.size()];
		
		int i = 0;
		for(ArrayList subList: stockData)
		{
			double close =  (double) subList.get(4);
			
			if(i != 0)
				logReturn[i - 1] = Math.log(formerClose/close);
			
			formerClose = close;
			i++;
		}
		
	    double total = 0;
	    for (double element : logReturn) {
	        total += element;
	    }
	    
	    double average = total / logReturn.length;
	    double variance = getVariance(logReturn);
	    double deviation = getStdDev(logReturn);
	    double drift = average - (variance/2);
	    
//        System.out.println("MonteCarloService> Initializing with Values:");
//        System.out.println("\tVariance: " + variance);
//        System.out.println("\tDrift: " + drift);
//        System.out.println("\tDeviation: " + deviation);
//        System.out.println("\tAverage: " + average);
	    
	    DictionaryResult jobConfiguration = new DictionaryResult();
	    jobConfiguration.value("average", average);
	    jobConfiguration.value("variance", variance);
	    jobConfiguration.value("deviation", deviation);
	    jobConfiguration.value("drift", drift);
	    jobConfiguration.value("lastClose", (double) (stockData.get(stockData.size() - 1).get(4)));
				
//	    System.out.println("MonteCarloService> Starting Job");
		MonteCarloJob m_job = new MonteCarloJob();
		m_job.configuration(jobConfiguration);
		result(m_job.result());
		
		executeJob(m_job);
	}
	

    double getMean(double[] data)
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/data.length;
    }

    double getVariance(double[] data)
    {
        double mean = getMean(data);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/data.length;
    }

    double getStdDev(double[] data)
    {
        return Math.sqrt(getVariance(data));
    }

    public double median(double[] data) 
    {
       double[] b = new double[data.length];
       System.arraycopy(data, 0, b, 0, b.length);
       Arrays.sort(b);

       if (data.length % 2 == 0) 
       {
          return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
       } 
       else 
       {
          return b[b.length / 2];
       }
    }

}
