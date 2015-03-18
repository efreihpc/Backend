package backend.system;

import backend.model.Describable;
import backend.model.result.Result;

public interface Task <T extends Result> extends Describable{
	T result();
	void execute();
}
