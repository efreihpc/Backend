package backend.system;

import backend.model.job.JobRepository;

public interface JobPersistenceUnit {
	JobRepository jobRepository();
}
