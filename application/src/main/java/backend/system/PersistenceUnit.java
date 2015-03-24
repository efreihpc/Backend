package backend.system;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.model.descriptor.Describable;

public interface PersistenceUnit<T extends Describable> {
	public void addPluginRepository(JpaRepository<T, Long> repository, EntityManager em);
	public <U extends JpaRepository<T, Long>>void registerPluginRepository(ClassLoader loader, Class<U> repositoryType);
	public EntityManager pluginEntityManager();
	public JpaRepository<T, Long> pluginRepository();
	public JpaRepository<T, Long> localRepository();
}
