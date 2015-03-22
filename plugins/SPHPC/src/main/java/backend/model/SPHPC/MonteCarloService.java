package backend.model.SPHPC;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import org.bson.Document;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class MonteCarloService extends ServicePlugin<JsonResult> {
	
	public MonteCarloService()
	{
		commonName("Monto Carlo Service");
		addDependency("ad42c7ece9fd7f97a3b7daf3b55460a72c25f98a", "b80b17302e4da7adc8599b9fb302a3b48cbee13e");
	}

	@Override
	public void execute() {
		System.out.println("Preparing data for MonteCarlo Job");
		JsonResult deprep = (JsonResult) dependencies().get(0).result();
		Document stockData = deprep.find("");
		stockData = (Document) stockData.get("storage");
		stockData = (Document) stockData.get("data");
		
		
		double formerClose = 0;
		double[] logReturn = new double[stockData.size() - 1];
		
		for(int i=0;i<stockData.size();i++)
		{
			System.out.print("*");
			Document day = (Document) stockData.get(i);
			double close =  day.getDouble(4);
			
			if(i != 0)
				logReturn[i] = Math.log(formerClose/close);
			formerClose = close;
		}
		
	    double total = 0;
	    for (double element : logReturn) {
	        total += element;
	    }
	    
	    double average = total / logReturn.length;
	    double variance = getVariance(logReturn);
	    double deviation = getStdDev(logReturn);
	    double drift = average - (variance/2);
				
		MonteCarloJob job = new MonteCarloJob();
		job.setMongoPersistence(persistenceUnit());
		result(job.result());
		
		executeJob(job);
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
