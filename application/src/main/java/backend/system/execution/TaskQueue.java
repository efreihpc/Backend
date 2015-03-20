package backend.system.execution;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.core.task.SyncTaskExecutor;

import backend.model.descriptor.Descriptor;
import backend.model.result.Result;

public class TaskQueue extends Task {

	private SyncTaskExecutor m_executor;
	private Queue<Task> m_tasks;
	
	public TaskQueue()
	{
		m_tasks = new LinkedBlockingQueue<Task>();
		m_executor = new SyncTaskExecutor();
	}
	
	@Override
	public Descriptor descriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result result() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() {
		for(Task task: m_tasks)
		{
			m_executor.execute(task);
		}
	}
	
	public void enqueue(Task task)
	{
		m_tasks.add(task);
	}

}
