package backend.system;

public class Pair<L,R> {
	
	  private final L m_left;
	  private final R m_right;

	  public Pair(L left, R right) 
	  {
	    m_left = left;
	    m_right = right;
	  }

	  public L left() 
	  { 
		  return m_left; 
	  }
	  
	  public R right() 
	  { 
		  return m_right;
	  }
}
