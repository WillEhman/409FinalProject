package frontend;
import java.io.*;
import java.net.*;

public class Client {
	public Socket socket;
	public ObjectInputStream in;
	public ObjectOutputStream out;
	
	public Client(String serverIp, int port) throws IOException {
		socket = new Socket(serverIp, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}
	
	public void communicate() {
		// TODO
	}
	
	public File getFile(String path) {
		// TODO
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client("localHost", 9090);
		client.communicate();
	}
}
