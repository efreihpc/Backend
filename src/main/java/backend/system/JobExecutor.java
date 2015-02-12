package backend.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import backend.model.job.Job;

public class JobExecutor {
	
	private TaskExecutor m_taskExecutor;
	private ApplicationContext m_context;
	
	public JobExecutor()
	{
		m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
	    m_taskExecutor = (ThreadPoolTaskExecutor) m_context.getBean("taskExecutor");
	}
	
	public void execute(Job job)
	{
		m_taskExecutor.execute(job);
	}

}
