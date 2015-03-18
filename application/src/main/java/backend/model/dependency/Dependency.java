package backend.model.dependency;

import backend.model.Descriptor;
import backend.model.Task;
import backend.model.result.Result;

public interface Dependency <T extends Task<U>, U extends Result> {
	Descriptor<T> descriptor();
	U result();
}
