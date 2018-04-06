package backend;

import java.io.*;
import java.util.Scanner;

import shared.*;

public class FileHelper {
	private String path;

	public FileHelper() {
		// TODO Auto-generated constructor stub

	}

	// byte getFileContent(String path) {
	// try {
	// Scanner sc = new Scanner(new FileReader(path));
	// while (sc.hasNext()) {
	// // String txtfile[] = sc.nextLine().split("\n");
	// // preparedaddItem(new byte(Integer.parseInt(txtfile[0]), txtfile[1],
	// // Integer.parseInt(txtfile[2]),
	// // Double.parseDouble(txtfile[3]), Integer.parseInt(txtfile[4])));
	// }
	// sc.close();
	// } catch (FileNotFoundException e) {
	// System.err.println("File " + path + " Not Found!");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }
	Message<Assignment> readFileContent(byte[] input, String query) throws IOException {
		String[] path = query.split(".SPLITTER.");
		String fileName = path[path.length - 2];
		String fileExt = path[path.length - 1];

		File newFile = new File(fileName + fileExt);
		try {
			if (!newFile.exists()) {
				newFile.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(input);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	void writeFileContent(byte[] input, String query) throws IOException {
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
		} catch (IOException e) {
			e.printStackTrace();
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;

	}


	void setFilePath(Assignment a) {

	}

	void setFilePath(Submission s) {

	}

	// FOR TESTING
	public static void main(String args[]) {
		FileHelper fh = new FileHelper();
		Assignment test = new Assignment(1, 409, "Final Project", "test.jpg", true, "Someday",
				"Take me down to the paradise city where the grass is green and the girls are pretty");
		try {
			fh.writeFileContent(test.getFileData().getBytes(), test.getFileQuery());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//test = new Assignment(1, 409, "Final Project", "test.jpg", true, "Someday", null);
		String receivedString = null;
		try {
			receivedString = new String(fh.readFileContent("test.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(receivedString);
	}
}
