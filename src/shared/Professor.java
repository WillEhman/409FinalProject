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
 *          Contains Professor Class
 */

public class Professor extends User implements Serializable {

	/**
	 * serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for Professor
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public Professor(int id, String firstName, String lastName, String email) {
		super(id, firstName, lastName, "P", email);
	}
	
	public Professor(User user) {
		super(user.getId(), user.getFirstName(), user.getLastName(), "P", user.getEmailAddress());
	}

	public Professor() {
		// TODO Auto-generated constructor stub
		super();
	}

}
