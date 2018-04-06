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
		System.out.println("|------------------Server being created------------------|");
		threadPool = Executors.newCachedThreadPool();
		database = new DatabaseHelper();
		fileManager = new FileHelper();
		emailService = new EmailHelper();
		try {
			serverSocket = new ServerSocket(9090);

		} catch (IOException e) {
		}
		System.out.println("|-------------------Server now running-------------------|");
		
	}

	public void run(Server server) {
		try {
			for(;;) {
				threadPool.execute(new Worker(serverSocket.accept(),fileManager,database,emailService));
			}
		}catch(IOException e) {
			System.err.println("Exception in server run: Shutting Down");
			threadPool.shutdown();
		}
	}

	
	public static void main(String[] args){
		Server server = new Server();
		server.run(server);
		
	}
	
	void shutdown(Server server) {
		//TODO see if necessary
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
		//TODO see if necessary
		return threadPool;
	}

//	public void setThreadPool(ExecutorService threadPool) {
//		this.threadPool = threadPool;
//	}
	
	
	//FOR ALL FOLLOWING
	//TODO see if necessary
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
