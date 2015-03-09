package backend.model.result;

import java.net.UnknownHostException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.bson.Document;

import backend.system.MongoPersistenceUnit;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import static com.mongodb.client.model.Filters.*;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("JsonResult")
public class JsonResult extends Result {
		
	@Transient
	MongoCollection m_collection;

	public JsonResult(MongoPersistenceUnit mongopersistence)
	{
		m_collection = mongopersistence.getMongoDb().getCollection("JsonResult");
	}

	public void insert(String jsonObject)
	{
		Document insertDocument = new Document("id", id());
		insertDocument.append("storage", (Document) JSON.parse(jsonObject));
		m_collection.insertOne(insertDocument);
	}
	
	public String find(String query)
	{
		Document queryDocument = (Document) JSON.parse(query);
		return m_collection.find(and(eq("íd", id()), queryDocument)).first().toString();
	}
	
}
