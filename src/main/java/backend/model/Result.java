package backend.model;

public interface Result<T> {
	void value(String key, T value);
	T value(String key);
}
