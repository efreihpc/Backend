package backend.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistenceUnit<T extends JpaRepository> {
	public boolean hasPlugin(String identifier);
	public void addPluginRepository(String identifier, T repository);
	public T repository(String pluginIdentifier);
	public T repository(Descriptor descriptor);
}
