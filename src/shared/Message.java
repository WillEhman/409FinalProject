package shared;

import java.io.Serializable;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains Message Class
 */

public class Message<M> implements Serializable {

	/**
	 * Contains Serial ID
	 */
	private static final long serialVersionUID = 12L;

	/**
	 * String with info on task to be completed
	 */
	private String query;

	/**
	 * object passed
	 */
	private M object;

	/**
	 * Constructor for Message
	 * 
	 * @param consObj
	 * @param consString
	 */
	public Message(M consObj, String consString) {
		this.object = consObj;
		this.query = consString;
	}

	/**
	 * getters and setters
	 * 
	 */
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
