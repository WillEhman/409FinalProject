package frontend;

import java.io.*;
import java.net.*;

import com.mysql.jdbc.log.Log;

import shared.LoginInfo;

public class Client implements Serializable {
	public Socket socket;
	public ObjectInputStream in;
	public ObjectOutputStream out;

	public Client(String serverIp, int port) throws IOException {
		try {
			System.out.println("Setting up client");
			socket = new Socket(serverIp, port);
			System.out.println("Setting up out socket");
			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Setting up in socket");
			in = new ObjectInputStream(socket.getInputStream());

			System.out.println("Client now Running");
		} catch (IOException e) {

		}

	}

	public void communicate(LoginInfo l) {
		try {
//			if (gui.getLoginInfo().getUsername() != null && gui.getLoginInfo().getPassword() != null) {
				System.out.println("Its not NULL");
				out.writeObject(l);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(in.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void communicate(LoginWindow l) {
//		 LoginInfo login = new LoginInfo("will", "pw");
//		LoginInfo l = new LoginInfo(null, null);
//		LoginWindow gui = new LoginWindow(l);
//		System.out.println(l.getUsername());
//		System.out.println(l.getPassword());
		try {
			if (l.getLoginInfo().getUsername() != null && l.getLoginInfo().getPassword() != null) {
				System.out.println("Its not NULL");
				out.writeObject(l.getLoginInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(in.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public File getFile(String path) {
		// TODO
		return null;
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client("10.13.166.195", 9090);
		LoginWindow l = new LoginWindow();
//		while (true) {
//			System.out.println(l.getLoginInfo().getUsername());
//			try {
//				client.communicate();
//			} catch (Exception e) {
//				e.printStackTrace();
//				break;
//			}
//		}
	}
}
