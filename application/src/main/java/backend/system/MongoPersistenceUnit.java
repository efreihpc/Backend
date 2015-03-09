package backend.system;

import com.mongodb.client.MongoDatabase;

public interface MongoPersistenceUnit {
	MongoDatabase getMongoDb();
}
