package backend;

import java.util.concurrent.ExecutorService;

public class Server {
	
	private ExecutorService threadPool;
	private FileHelper fileManager;
	private DatabaseHelper database;
	private EmailHelper emailService;
	private int uniqueID;

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	void run() {
		
	}
	
	int generateUniqueID() {
		return 0;
	}
	
	void shutdown(){
		
	}
	
	

}
