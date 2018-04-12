package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import shared.Chat;
import shared.Course;
import shared.Message;
import shared.Submission;
import shared.User;
/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 10 2018
 * @version 1.0
 * 
 *          Client for client-server communication
 *
 */
public class ChatroomPage extends JPanel{
	
	/**
	 * Message to be send
	 */
	private JTextArea messages;
	
	/**
	 * User using chatroom
	 */
	private User user;
	
	/**
	 * Client being used by user
	 */
	private Client client;
	
	/**
	 * Course that chatroom is around
	 */
	private Course course;
	
	/**
	 * Chat messages vector
	 */
	private Vector<Chat> chatVector;
	
	/**
	 * Constructor for ChatroomPage
	 * @param user
	 * @param client
	 * @param course
	 */
	public ChatroomPage(User user, Client client, Course course) {
		this.user = user;
		this.client = client;
		this.course = course;
		
		/**
		 * Panel with user info and buttons
		 */
		JPanel info = new JPanel();
		JPanel buttons = new JPanel();
		messages = new JTextArea();
		messages.setEditable(false);
		JScrollPane scroll = new JScrollPane(messages);
		JTextField messageF = new JTextField(60);
		this.setLayout(new BorderLayout());
		buttons.setLayout(new FlowLayout());
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		
		/**
		 * Sends message
		 */
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Chat newChat = new Chat(course.getCourseId(), user.getFirstName() + " " + user.getLastName(), messageF.getText());
				Message<Chat> message = new Message<Chat>(newChat, "SENDCHAT");
				Message<?> receive = client.communicate(message);
				setChat((Vector<Chat>) receive.getObject());
				messageF.setText(null);
			}
		});	
		
		/**
		 * Vector of courses
		 */
		Message<Course> message = new Message<Course>(course, "CHATLIST");
		Message<?> receive = client.communicate(message);
		setChat((Vector<Chat>) receive.getObject());
		buttons.add(messageF);
		buttons.add(send);
		info.add(scroll);
		this.add("Center", info);
		this.add("South", buttons);
	}
	
	/**
	 * Sets up chat
	 * @param v
	 */
	public void setChat(Vector<Chat> v) {
		String temp = new String();
		for (int i = 0; i < v.size(); i++) {
			temp = temp + v.get(i).toString() + "\n\n";
		}
		messages.setText(temp);
	}
	
}
