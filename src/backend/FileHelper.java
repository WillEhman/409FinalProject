package backend;

import java.io.*;
import shared.*;

public class FileHelper {
	private String path;

	public FileHelper() {
		// TODO Auto-generated constructor stub
		
	}
	
	

	byte getFileContent(String path) {
		return 0;
	}
	
	void writeFileContent(String name, byte[] data) throws IOException {
		BufferedWriter writer = null;
		try {
			File newFile = new File(path, name);
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
			for (int c : data) {
				writer.write(c);
			}
		} finally {
			writer.close();
		}
		

	}

	void writeFileContent(Assignment a, byte[] content) throws IOException {
		writeFileContent(a.getTitle(),content);

	}

	void writeFileContent(Submission s, byte[] content) throws IOException {
		writeFileContent(s.getTitle(),content);
	}

	void setFilePath(Assignment a) {

	}

	void setFilePath(Submission s) {

	}
}
