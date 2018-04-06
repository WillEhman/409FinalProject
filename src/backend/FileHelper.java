package backend;

import java.io.*;
import java.util.Scanner;

import shared.*;

public class FileHelper {
	private String path;

	public FileHelper() {
		// TODO Auto-generated constructor stub
		
	}
	
	

	byte getFileContent(String path) {
		try {
			Scanner sc = new Scanner(new FileReader(path));
			while (sc.hasNext()) {
//				String txtfile[] = sc.nextLine().split("\n");
//				preparedaddItem(new byte(Integer.parseInt(txtfile[0]), txtfile[1], Integer.parseInt(txtfile[2]),
//						Double.parseDouble(txtfile[3]), Integer.parseInt(txtfile[4])));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File " + path + " Not Found!");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
