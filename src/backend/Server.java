package backend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 6 2018
 * @version 1.0
 * 
 *          Server for communicating with client
 *
 */
public class Server {

	/**
	 * Standard sockets
	 */
	private ServerSocket serverSocket;
	private Socket commSocket;

	/**
	 * Tells if server is running or stopped
	 */
	protected boolean isStopped = false;

	/**
	 * running thread
	 */
	protected Thread runningThread = null;

	/**
	 * pool of threads
	 */
	private final ExecutorService threadPool;

	/**
	 * helpers for server tasks
	 */
	private FileHelper fileManager;
	private DatabaseHelper database;
	private EmailHelper emailService;

	/**
	 * id of server
	 */
	private int uniqueID;

	/**
	 * Constructor for server
	 */
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

	/**
	 * Runs the server
	 * 
	 * @param server
	 */
	public void run(Server server) {
		try {
			for (;;) {
				threadPool.execute(new Worker(serverSocket.accept(), fileManager, database, emailService));
			}
		} catch (IOException e) {
			System.err.println("Exception in server run: Shutting Down");
			threadPool.shutdown();
		}
	}

	/**
	 * Run main to begin server running
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.run(server);

	}

	/**
	 * Shuts down the server
	 * 
	 * @param server
	 */
	void shutdown(Server server) {
		// TODO see if necessary
		// Close all sockets
		try {
			threadPool.shutdown();
			server.commSocket.close();
			server.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * getters and setters
	 */
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	// public void setThreadPool(ExecutorService threadPool) {
	// this.threadPool = threadPool;
	// }

	public synchronized boolean isStopped() {
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
