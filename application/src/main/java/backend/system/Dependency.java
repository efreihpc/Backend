package backend.system;

import backend.model.Descriptor;
import backend.model.result.Result;

public interface Dependency <T extends Task<U>, U extends Result> {
	Descriptor<T> descriptor();
	U result();
}
