package shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains Email Class
 */

public class Email implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * email sender
	 */
	private String from;

	/**
	 * email receiver(s)
	 */
	private ArrayList<String> to;

	/**
	 * subject line of email
	 */
	private String subject;

	/**
	 * contents of email
	 */
	private String content;

	/**
	 * Constructor for email
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 */
	public Email(String from, ArrayList<String> to, String subject, String content) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * getters and setters
	 */
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public ArrayList<String> getTo() {
		return to;
	}

	public void setTo(ArrayList<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
