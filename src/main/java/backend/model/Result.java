package backend.model;

public interface Result<T> {
	void addValue(String key, T value);
	T value(String key);
}
