package shared;

import java.io.Serializable;

public class Student extends User implements Serializable{

	private static final long serialVersionUID = 1L;

	public Student(int id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}

}
