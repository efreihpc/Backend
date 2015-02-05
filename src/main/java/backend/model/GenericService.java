package backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance
@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
public abstract class GenericService<T> implements Service<T> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    private String name;
}
