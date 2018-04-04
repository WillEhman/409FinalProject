package shared;

import java.io.Serializable;

public class Professor extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public Professor(int id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}

}
