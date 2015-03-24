package backend.system.execution;

import backend.model.task.Task;


public interface Executor {
	void execute(Task task);
}
