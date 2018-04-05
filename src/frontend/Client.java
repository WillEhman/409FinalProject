package frontend;

import java.io.*;
import java.net.*;

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

	public void communicate(){
		// TODO
		LoginInfo login = new LoginInfo("will", "pw");

		try {
			out.writeObject(login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(in.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public File getFile(String path) {
		// TODO
		return null;
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client("localhost", 9090);
		while(true) {
		try {
		client.communicate();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			break;
		}
		}
	}
}
