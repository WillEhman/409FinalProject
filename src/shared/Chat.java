package shared;

import java.io.Serializable;

public class Chat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int coursenum;

	private String sender;

	private String message;

	public Chat(int coursenum, String sender, String message) {
		super();
		this.coursenum = coursenum;
		this.sender = sender;
		this.message = message;
	}

	public int getCoursenum() {
		return coursenum;
	}

	public void setCoursenum(int coursenum) {
		this.coursenum = coursenum;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		String s;
		s = sender + ": " + message;
		// for (int i = 0; i<s.length();i++) {
		// s.charAt(i) = '\n';
		// }
		return s;
	}

}
