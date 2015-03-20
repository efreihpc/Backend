package backend.system.execution;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import backend.model.job.Job;

public class ThreadPoolExecutor implements Executor{
	
	private TaskExecutor m_taskExecutor;
	private ApplicationContext m_context;
	
	public ThreadPoolExecutor(String beanName)
	{
		m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
	    m_taskExecutor = (ThreadPoolTaskExecutor) m_context.getBean(beanName);
	}

	@Override
	public void execute(Task task) {
		m_taskExecutor.execute(task);
	}

}
