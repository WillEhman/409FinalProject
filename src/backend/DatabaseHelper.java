package backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class DatabaseHelper {

	PreparedStatement statement;
	Connection connection;
	String sql;
	public String databaseName = "school_master", studentsTable = "students", professorsTable = "professors",
			coursesTable = "courses";
	public String connectionInfo = "jdbc:mysql://localhost:3306/school_master", login = "student", password = "student";

	public DatabaseHelper() {
		try {
			// If this throws an error, make sure you have added the mySQL connector JAR to
			// the project
			Class.forName("com.mysql.jdbc.Driver");

			// If this fails make sure your connectionInfo and login/password are correct
			connection = DriverManager.getConnection(connectionInfo, login, password);
			System.out.println("Connected to: " + connectionInfo + "\n");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ~~~~~~~~~~~~~~FOR TESTING CURRENTLY~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Use the connection to create a new database in MySQL.
	 */
	public void preparedcreateDB() {
		try {
			String query = "CREATE DATABASE " + databaseName;
			statement = connection.prepareStatement(query);
			System.out.println("Created Database " + databaseName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a default set of data table of tools inside the database
	 */
	
	public void preparedcreateTable() {
		String sql = "CREATE TABLE " + tableName + "(" + "ID INT(4) NOT NULL, " + "TOOLNAME VARCHAR(20) NOT NULL, "
				+ "QUANTITY INT(4) NOT NULL, " + "PRICE DOUBLE(5,2) NOT NULL, " + "SUPPLIERID INT(4) NOT NULL, "
				+ "PRIMARY KEY ( id ))";
		try {
			statement = connection.prepareStatement(sql);
			statement.executeUpdate(sql);
			System.out.println("Created Table " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a specific Table
	 */
	public void preparedcreateTable(String tableName) {
		String sql = "CREATE TABLE " + tableName + "(" + "ID INT(4) NOT NULL, " + "TOOLNAME VARCHAR(20) NOT NULL, "
				+ "QUANTITY INT(4) NOT NULL, " + "PRICE DOUBLE(5,2) NOT NULL, " + "SUPPLIERID INT(4) NOT NULL, "
				+ "PRIMARY KEY ( id ))";
		try {
			statement = connection.prepareStatement(sql);
			statement.executeUpdate(sql);
			System.out.println("Created Table " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedremoveTable() {
		String sql = "DROP TABLE " + tableName;
		try {
			statement = connection.prepareStatement(sql);
			statement.executeUpdate(sql);
			System.out.println("Removed Table " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Fills the data table with all the tools from the text file 'items.txt' if
	// found
	public void fillTable() {
		try {
			Scanner sc = new Scanner(new FileReader(dataFile));
			while (sc.hasNext()) {
				String toolInfo[] = sc.nextLine().split(";");
				preparedaddItem(new Tool(Integer.parseInt(toolInfo[0]), toolInfo[1], Integer.parseInt(toolInfo[2]),
						Double.parseDouble(toolInfo[3]), Integer.parseInt(toolInfo[4])));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File " + dataFile + " Not Found!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a tool to the database table
	 * 
	 * @param tool
	 *            tool to be added
	 */

	public void preparedaddItem(Tool tool) {
		String sql = "INSERT INTO ToolTable VALUES ( ?, ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);

			// statement.setString(1,tableName);
			statement.setInt(1, tool.getID());
			statement.setString(2, tool.getName());
			statement.setInt(3, tool.getQuantity());
			statement.setDouble(4, tool.getPrice());
			statement.setInt(5, tool.getSupplierID());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Selects and searches for a tool id
	 * 
	 * @param toolID
	 *            to be searched for
	 * @return tool or null if no tool
	 */
	public Tool preparedsearchTool(int toolID) {
		String sql = "SELECT * FROM ToolTable WHERE ID= ?";
		ResultSet tool;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, toolID);
			tool = statement.executeQuery();
			if (tool.next()) {
				return new Tool(tool.getInt("ID"), tool.getString("TOOLNAME"), tool.getInt("QUANTITY"),
						tool.getDouble("PRICE"), tool.getInt("SUPPLIERID"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * prints all items in database to console
	 */
	public void preparedprintTable() {
		try {
			String sql = "SELECT * FROM " + tableName;
			statement = connection.prepareStatement(sql);
			ResultSet tools = statement.executeQuery(sql);
			System.out.println("Tools:");
			while (tools.next()) {
				System.out.println(tools.getInt("ID") + " " + tools.getString("TOOLNAME") + " "
						+ tools.getInt("QUANTITY") + " " + tools.getDouble("PRICE") + " " + tools.getInt("SUPPLIERID"));
			}
			tools.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		InventoryManager inventory = new InventoryManager();

		// You should comment this line out once the first database is created (either
		// here or in MySQL workbench)
		// inventory.createDB();

		inventory.preparedcreateTable();

		System.out.println("\nFilling the table with tools");
		inventory.fillTable();

		System.out.println("Reading all tools from the table:");
		inventory.preparedprintTable();

		System.out.println("\nSearching table for tool 1002: should return 'Grommets'");
		int toolID = 1002;
		Tool searchResult = inventory.preparedsearchTool(toolID);
		if (searchResult == null)
			System.out.println("Search Failed to find a tool matching ID: " + toolID);
		else
			System.out.println("Search Result: " + searchResult.toString());

		System.out.println("\nSearching table for tool 9000: should fail to find a tool");
		toolID = 9000;
		searchResult = inventory.preparedsearchTool(toolID);
		if (searchResult == null)
			System.out.println("Search Failed to find a tool matching ID: " + toolID);
		else
			System.out.println("Search Result: " + searchResult.toString());

		System.out.println("\nTrying to remove the table");
		inventory.preparedremoveTable();

		try {
			inventory.statement.close();
			inventory.jdbc_connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nThe program is finished running");
		}
	}

}
