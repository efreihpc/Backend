package backend.system;

import backend.model.result.Result;

public interface Configurable {

    Result configuration();
    public void configuration(Result configuration);

}
