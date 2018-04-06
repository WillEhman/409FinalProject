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

	void writeFileContent(Assignment a) throws IOException {
		OutputStream output = null;
		try {
			byte[] bytesArray = a.getFileData().getBytes();
			File outputFile = new File(a.getPath());

			output = new FileOutputStream(outputFile);

			output.write(bytesArray);

			output.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error in closing the Stream");
			}
		}
	}

	Assignment readFileContent(Assignment a) throws IOException {
		Assignment local = a;
		// This will reference one line at a time
		String data = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(a.getPath());

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((data = bufferedReader.readLine()) != null) {
				local.setFileData(data);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + path + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return local;
	}
	// InputStream f = new FileInputStream(fileData);
	// for(int x = 0; x < f.available() ; x++) {
	// os.write( bWrite[x] ); // writes the bytes
	// }
	//// BufferedWriter writer = null;
	// try {
	//// File newFile = new File(path, name);
	//// writer = new BufferedWriter(new OutputStreamWriter(new
	// FileOutputStream(newFile)));
	//// byte[] byteData = fileData.getBytes();
	////// newFile.
	////
	//// writer.write(fileData);
	////
	////
	//// FileOutputStream outputStream = new FileOutputStream(fileName);
	//// byte[] strToBytes = str.getBytes();
	//// outputStream.write(strToBytes);
	// } finally {
	// writer.close();
	// }

	// void writeFileContent(Assignment a) throws IOException {
	// writeFileContent(a.getTitle(), a.getFile());
	//
	// }

	// void writeFileContent(Submission s, byte[] content) throws IOException {
	// writeFileContent(s.getTitle(),content);
	// }

	void setFilePath(Assignment a) {

	}

	void setFilePath(Submission s) {

	}

	public static void main(String args[]) {
		FileHelper fh = new FileHelper();
		Assignment test = new Assignment(1, 409, "Final Project", "test.txt", true, "Someday",
				"Don't Take me down to the paradise city where the grass is green and the girls are pretty");
		try {
			fh.writeFileContent(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test = new Assignment(1, 409, "Final Project", "test.txt", true, "Someday",null);
		try {
			fh.readFileContent(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(test.getFileData());
	}
}
