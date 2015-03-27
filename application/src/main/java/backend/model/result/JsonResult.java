package backend.model.result;

import static com.mongodb.client.model.Filters.eq;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.bson.Document;

import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("JsonResult")
public class JsonResult extends Result {
		
	@Transient
	MongoCollection m_collection;
	
	public JsonResult()
	{
	}

	public void insert(String jsonObject)
	{
		lazyInitDb();
		Document insertDocument = new Document("id", id());
		insertDocument.append("storage", Document.parse(jsonObject));
		m_collection.insertOne(insertDocument);
	}
	
	public Document findOne(String query, String selection)
	{
		return find(query, selection).first();
	}
	
	public Document findOne(String query)
	{
		return(findOne(query, "{}"));
	}
	
	public FindIterable<Document> find(String query, String selection)
	{
		lazyInitDb();
//		Document queryDocument = Document.valueOf(query);
//		return m_collection.find(and(eq("íd", id()), queryDocument)).first().toString();
		FindIterable<Document> result = m_collection.find(eq("id", id()));
		if(result == null)
		{
			System.out.println("couldn't find entry for this Result id");
			return null;
		}
		return result;
	}
	
	public FindIterable<Document> find(String query)
	{
		return(find(query, "{}"));
	}
	
	public void value(String key, String value) {
		insert("{" + key + ":" + value + "}");	
	}
	
	public void value(String key, double value) {
		insert("{" + key + ":" + value + "}");	
	}

	public String stringValue(String key) {
		return findOne("{}", "{" + key + ":1}").getString(key);
	}
	
	public double doubleValue(String key) {
		return findOne("{}", "{" + key + ":1}").getDouble(key);
	}

	@Override
	public String allJson() {
		String allData = "";
		
		MongoCursor<Document> cursor = find("{}").iterator();
		try {
		    while (cursor.hasNext()) {
		        allData += cursor.next().toJson();
		    }
		} finally {
		    cursor.close();
		}

		return allData;
	}
	
	private void lazyInitDb()
	{
		if(m_collection == null)
		{
			GlobalPersistenceUnit globalPersistence = GlobalState.get("GlobalPersistenceUnit");
			m_collection = globalPersistence.getMongoDb().getCollection("JsonResult");
		}
	}

	@Override
	public void fromJsonString(String allData) {
		insert(allData);
	}
	
}
