package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

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

public class ChatroomPage extends JPanel{
	
	private JTextArea messages;
	private User user;
	private Client client;
	private Course course;
	private Vector<Chat> chatVector;
	
	public ChatroomPage(User user, Client client, Course course) {
		this.user = user;
		this.client = client;
		this.course = course;
		JPanel info = new JPanel();
		JPanel buttons = new JPanel();
		messages = new JTextArea();
		JScrollPane scroll = new JScrollPane(messages);
		JTextField messageF = new JTextField(5);
		this.setLayout(new BorderLayout());
		info.setLayout(new FlowLayout());
		buttons.setLayout(new FlowLayout());
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Chat newChat = new Chat(course.getCourseId(), user.getFirstName() + " " + user.getLastName(), messageF.getText());
				Message<Chat> message = new Message<Chat>(newChat, "SENDCHAT");
				Message<?> receive = client.communicate(message);
				setChat((Vector<Chat>) receive.getObject());
			}
		});	
		Message<Course> message = new Message<Course>(course, "CHATLIST");
		Message<?> receive = client.communicate(message);
		setChat((Vector<Chat>) receive.getObject());
		buttons.add(messageF);
		buttons.add(send);
		info.add(scroll);
		this.add("Center", info);
		this.add("South", buttons);
	}
	
	public void setChat(Vector<Chat> v) {
		String temp = new String();
		for (int i = 0; i < v.size(); i++) {
			temp = temp + v.get(i).toString() + "\n\n";
		}
		messages.setText(temp);
	}
	
}
