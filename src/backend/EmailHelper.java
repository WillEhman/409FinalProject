package backend;

import shared.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 8 2018
 * @version 1.0
 * 
 *          EmailHelper for communication with the email servers
 *
 */
public class EmailHelper {

	/**
	 * Default constructor
	 */
	public EmailHelper() {
	}

	/**
	 * Sends an email
	 * 
	 * @param sender
	 * @param senderPw
	 * @param recipient
	 * @param subject
	 * @param content
	 * @return if email succeeded or failed to send
	 */
	public String SendEmail(final String sender, String senderPw, String recipient, String subject, String content) {
		String response = "Success";
		InternetAddress RECIPIENT_ADDRESS = null;
		try {
			RECIPIENT_ADDRESS = new InternetAddress(recipient);
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true"); // Using TLS
		properties.put("mail.smtp.auth", "true"); // Authenticate
		properties.put("mail.smtp.host", "smtp.gmail.com"); // Using Gmail Account
		properties.put("mail.smtp.port", "587"); // TLS uses port 587

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, senderPw);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, RECIPIENT_ADDRESS);
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message); // Send the Email Message
			response = "Success";
		} catch (MessagingException e) {
			e.printStackTrace();
			response = "Fail";

		}
		return response;
	}

	/**
	 * For testing EmailHelper
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String sender1 = "stevesjob69@gmail.com";
		String senderPw1 = "wowzers409";
		String recipient1 = "zoochegg@gmail.com";
		String subject1 = "holy dicks, batman!";
		String content1 = "chickens";
		EmailHelper se = new EmailHelper();
		se.SendEmail(sender1, senderPw1, recipient1, subject1, content1);
	}

}
