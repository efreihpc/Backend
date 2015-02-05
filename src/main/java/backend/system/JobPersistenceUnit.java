package backend.system;

import backend.model.JobRepository;

public interface JobPersistenceUnit {
	JobRepository jobRepository();
}
