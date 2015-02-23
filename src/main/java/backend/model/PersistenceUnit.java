package backend.model;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistenceUnit<T extends Describable> {
	public void addPluginRepository(JpaRepository<T, Long> repository, EntityManager em);
	public Pair<JpaRepository<T, Long>, EntityManager> pluginRepository();
	public JpaRepository<T, Long> localRepository();
	public Pair<JpaRepository<T, Long>, EntityManager> repository(Describable instance);
}
