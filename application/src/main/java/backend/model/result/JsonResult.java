package backend.model.result;

import static com.mongodb.client.model.Filters.eq;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.bson.Document;

import backend.system.MongoPersistenceUnit;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mongodb.client.MongoCollection;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("JsonResult")
public class JsonResult extends Result {
		
	@Transient
	MongoCollection m_collection;
	
	public JsonResult()
	{
		super();
	}
	
	public void mongoPersistenceUnit(MongoPersistenceUnit mongopersistence)
	{
		m_collection = mongopersistence.getMongoDb().getCollection("JsonResult");
	}

	public JsonResult(MongoPersistenceUnit mongopersistence)
	{
		m_collection = mongopersistence.getMongoDb().getCollection("JsonResult");
	}

	public void insert(String jsonObject)
	{
		Document insertDocument = new Document("id", id());
		insertDocument.append("storage", Document.parse(jsonObject));
		m_collection.insertOne(insertDocument);
	}
	
	public Document find(String query)
	{
//		Document queryDocument = Document.valueOf(query);
//		return m_collection.find(and(eq("íd", id()), queryDocument)).first().toString();
		Document result = (Document) m_collection.find(eq("id", id())).first();
		if(result == null)
		{
			System.out.println("couldn't find entry for this Result id");
			return null;
		}
		return result;
	}
	
}
