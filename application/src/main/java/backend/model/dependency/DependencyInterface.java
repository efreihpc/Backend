package backend.model.dependency;

import backend.model.result.Result;
import backend.system.execution.Task;

public interface DependencyInterface <T extends Task<U>, U extends Result> {
	void task(T task);
	boolean satisfied();
	
	U result();
}
