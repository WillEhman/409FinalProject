package shared;

import java.io.Serializable;

public class Message <M> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID =12L;
	
	private String query;
	private M object;
	
	public Message(M consObj, String consString) {
		this.object = consObj;
		this.query = consString;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String consString) {
		this.query = consString;
	}
	
	public M getObject() {
		return object;
	}

	public void setObject(M object) {
		this.object = object;
	}

	

}
