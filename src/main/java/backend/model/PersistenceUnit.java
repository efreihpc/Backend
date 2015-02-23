package backend.model;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistenceUnit<T extends JpaRepository> {
	public boolean hasPlugin(String identifier);
	public void addPluginRepository(String identifier, T repository, EntityManager em);
	public Pair<T, EntityManager> repository(String pluginIdentifier);
	public Pair<T, EntityManager> repository(Descriptor descriptor);
}
