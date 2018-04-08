package test;


import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
//import javax.activation.*;

public class EmailTest {
	public static void main(String [] args) {
		
		String YOUR_ADDRESS = "stevesjob69@gmail.com";
		String YOUR_PASSWORD = "wowzers409";
		InternetAddress RECIPIENT_ADDRESS = null;
		try {
			RECIPIENT_ADDRESS = new InternetAddress("zoochegg@gmail.com");
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
				return new PasswordAuthentication(YOUR_ADDRESS, YOUR_PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(YOUR_ADDRESS));
			message.addRecipient(Message.RecipientType.TO, RECIPIENT_ADDRESS);
			message.setSubject("Your Message Subject");
			message.setText("Your Message Content");
			Transport.send(message); // Send the Email Message
			} catch (MessagingException e) {
			e.printStackTrace();
			}
	}
}
