package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

	private ServerSocket serverSocket;
	private Socket commSocket;

	protected boolean isStopped = false;
	protected Thread runningThread = null;

	private final ExecutorService threadPool;
	private FileHelper fileManager;
	private DatabaseHelper database;
	private EmailHelper emailService;
	private int uniqueID;

	public Server() {
		threadPool = Executors.newCachedThreadPool();
		try {
			serverSocket = new ServerSocket(9090);

		} catch (IOException e) {
		}
		System.out.println("Server now running.");
	}

	public void run(Server server) {
		try {
			for(;;) {
				threadPool.execute(new Worker(serverSocket.accept(),server));
			}
		}catch(IOException e) {
			System.out.println("Exception in server run: Shutting Down");
			threadPool.shutdown();
		}
	}
	
	int generateUniqueID() {
		return 0;
	}
	
	public static void main(String[] args){
		Server server = new Server();
		server.database = new DatabaseHelper();
		server.fileManager = new FileHelper();
		server.emailService = new EmailHelper();
		
		server.run(server);
	}
	
	void shutdown(Server server) {
		// Close all sockets
		try {
			threadPool.shutdown();
			server.commSocket.close();
			server.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

//	public void setThreadPool(ExecutorService threadPool) {
//		this.threadPool = threadPool;
//	}
	
	private synchronized boolean isStopped() {
        return this.isStopped;
    }

	public FileHelper getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileHelper fileManager) {
		this.fileManager = fileManager;
	}

	public DatabaseHelper getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseHelper database) {
		this.database = database;
	}

	public EmailHelper getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailHelper emailService) {
		this.emailService = emailService;
	}

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}

}
