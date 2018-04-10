package shared;

public class Chat {

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

}
