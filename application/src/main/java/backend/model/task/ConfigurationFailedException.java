package backend.model.task;

public class ConfigurationFailedException extends Exception {

	private String m_requirement;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfigurationFailedException(String requirement)
	{
		m_requirement = requirement;
	}
	
	public String getMessage()
	{
		return "Requirement not met: " + m_requirement;
	}

}
