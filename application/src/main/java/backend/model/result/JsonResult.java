package backend.model.result;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Entity
@Inheritance                                                                                                                                                 
@JsonTypeName("JsonResult")
public class JsonResult extends Result {
	
	ApplicationContext m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
	DB m_db;
	DBCollection m_collection;

	JsonResult()
	{
		MongoDbFactory factory = (MongoDbFactory) m_context.getBean("mongoDbFactory");
		m_db = factory.getDb();
		m_collection = m_db.getCollection("Result");
	}

	public void insert(String jsonObject)
	{
		DBObject dbObject = (DBObject) JSON.parse(jsonObject);
		m_collection.insert(dbObject);
	}
	
	public void find(String jsonQuery)
	{
		DBObject dbObject = (DBObject) JSON.parse(jsonQuery);
		m_collection.find(dbObject);
	}
	
}
