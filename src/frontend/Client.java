package frontend;
import java.io.*;
import java.net.*;

import shared.LoginInfo;

public class Client {
	public Socket socket;
	public ObjectInputStream in;
	public ObjectOutputStream out;
	
	public Client(String serverIp, int port) throws IOException {
		socket = new Socket(serverIp, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());

	}
	
	public void communicate() throws IOException {
		// TODO
		LoginInfo login = new LoginInfo("will", "pw");
		out.writeObject(login);
		out.close();
	}
	
	public File getFile(String path) {
		// TODO
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client("localhost", 9090);
		System.out.println("Client now Running");
		client.communicate();
	}
}
