package shared;

import java.io.Serializable;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains User Class
 */

public class User implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * unique id of user
	 */
	private int id;

	/**
	 * first name of user
	 */
	private String firstName;

	/**
	 * last name of user
	 */
	private String lastName;

	/**
	 * type of user. 'S' for student, 'P' for professor
	 */
	private String type;

	/**
	 * email address of the user
	 */
	private String emailAddress;

	/**
	 * Constructor for user
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param type
	 */
	public User(int id, String firstName, String lastName, String type) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
	}

	/**
	 * getters and setters
	 * 
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
