package backend.model.dependency;

import backend.model.Task;
import backend.model.result.Result;

public interface DependencyInterface <T extends Task<U>, U extends Result> {
	void task(T task);
	boolean satisfied();
	
	U result();
}
