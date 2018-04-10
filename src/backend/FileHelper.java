package backend;

import java.io.*;
//import java.util.Scanner;

import shared.*;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 *
 */
public class FileHelper {
	// private String path;

	public FileHelper() {
		// TODO Auto-generated constructor stub

	}

	// Should contain path in query in form CREATEFILE.SPLITTER.TEST.SPLITTER.txt
	// Should contain data in object in form byte[]
	boolean writeFileContent(byte[] input, String query) throws IOException {
		String[] path = query.split(".SPLITTER.");
		String fileName = path[path.length - 2];
		String fileExt = path[path.length - 1];
		File newFile = new File(fileName + "." + fileExt);
		try {
			if (!newFile.exists()) {
				newFile.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(input);
			bos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	byte[] readFileContent(String path) throws IOException {
		File selectedFile = new File(path);
		long length = selectedFile.length();
		byte[] content = new byte[(int) length];

		try {
			FileInputStream fis = new FileInputStream(selectedFile);
			BufferedInputStream bos = new BufferedInputStream(fis);
			bos.read(content, 0, (int) length);
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}
		return content;
	}

	// void setFilePath(Assignment a) {
	//
	// }
	//
	// void setFilePath(Submission s) {
	//
	// }

	// FOR TESTING
	public static void main(String args[]) {
		FileHelper fh = new FileHelper();
		Assignment test = new Assignment(1, 409, "Final Project", "test.jpg", true, "Someday",
				"Take me down to the paradise city where the grass is green and the girls are pretty".getBytes());
		try {
			fh.writeFileContent(test.getBytes(), test.getFileQuery());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// test = new Assignment(1, 409, "Final Project", "test.jpg", true, "Someday",
		// null);
		String receivedString = null;
		try {
			receivedString = new String(fh.readFileContent("453SWE.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(receivedString);
	}
}
