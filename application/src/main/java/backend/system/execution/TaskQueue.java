package backend.system.execution;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.springframework.core.task.SyncTaskExecutor;

import reactor.event.Event;
import reactor.function.Consumer;
import backend.model.descriptor.Descriptor;
import backend.model.result.Result;

@Entity
@Inheritance
public class TaskQueue extends Task implements Consumer<Event<Long>>{

	@Transient
	private SyncTaskExecutor m_executor;
	@Transient
	private Queue<Task> m_tasks;
	@Transient
	private Task m_currentTask;
	
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
	public void execute() 
	{
		if(m_currentTask != null)
		{
			System.out.println("TaskQueue: cannot execute Queue, allready running");
			return;
		}
		m_currentTask = m_tasks.poll();
		
		if(m_currentTask != null)
			m_executor.execute(m_currentTask);
	}
	
	protected void nextTask()
	{
		if(m_currentTask == null)
		{
			System.out.println("TaskQueue: trying to execute next job though there is no current Job");
			return;
		}
		m_currentTask = m_tasks.poll();
		
		if(m_currentTask != null)
			m_executor.execute(m_currentTask);
	}
	
	public void enqueue(Task task)
	{
		m_tasks.add(task);
	}

	@Override
	public void accept(Event<Long> ev) {
		System.out.println("Task finished: " + ev.getData());
		if(ev.getData() == m_currentTask.id())
			nextTask();
	}
}
