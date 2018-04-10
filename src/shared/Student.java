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
 *          Contains Student Class
 */

public class Student extends User implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for Student
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public Student(int id, String firstName, String lastName, String email) {
		super(id, firstName, lastName, "S", email);
	}

	/**
	 * Constructs professor from User
	 * 
	 * @param user
	 *            to build user from
	 */
	public Student(User user) {
		super(user.getId(), user.getFirstName(), user.getLastName(), "S", user.getEmailAddress());
	}

}
