package database;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class to utilise communication between a java program and a
 * MySQL database.
 * @author Edward Hummerston - 11477172
 *
 */
public class DatabaseConnection {

	public static final String DB_URL = "jdbc:mysql://localhost/ST11477172";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "abc123";
	public static final String TABLE_NAME = "Student_marks_ITC000";

	//Constant values for the names of columns in the table
	public static final String COL_STU_ID = "studentid";
	public static final String COL_NAME = "name";
	public static final String COL_ASS_1 = "assignment1";
	public static final String COL_ASS_2 = "assignment2";
	public static final String COL_ASS_3 = "assignment3";
	public static final String COL_EXAM = "final";

	//The number of characters allowed in the name field
	public static final int NAME_CHARS = 10;

	Connection connection;
	Statement statement;

	public DatabaseConnection() throws SQLException, ClassNotFoundException{

		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Driver loaded");

		//Connect to the ST11477172 Database
		connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
		System.out.println("DB connected");

		statement = connection.createStatement();

	}

	/**
	 * Detects for a table of name {@link #TABLE_NAME}. It will then
	 * make one of the same name if there is not already one
	 *  existing or force is true.
	 * @param force Indicates whether or not to overwrite an existing
	 * table with a new one.
	 * @return Whether the new table (true) or an existing table was
	 * found and not overwritten. (false)
	 * @throws SQLException
	 */
	public boolean newTableButton(boolean force) throws SQLException
	{
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsMeta = meta.getTables(null, null, TABLE_NAME, null);
		
		//This if statement proceeds as true if there is no table
		// of the name defined by DatabaseConnection.TABLE_NAME.
		if( rsMeta.absolute(1)==false )
		{
			//No table
			System.out.println("No " + TABLE_NAME + " table detected.");
			this.createTable();
			System.out.println('\t' + TABLE_NAME + " table created.");
		}
		else
		{
			//Table found
			System.out.println(TABLE_NAME + " table exists.");
			if(force)
			{
				//This code executes after a dialog window informed the
				// user that proceeding will destroy the old table.
				statement.executeUpdate("DROP TABLE " + TABLE_NAME);
				System.out.println('\t' + TABLE_NAME + " table deleted.");
				this.createTable();
				System.out.println('\t' + TABLE_NAME + " table created.");
			}
			else return false;
			//If the user hasn't told the program to do so,
			// no new table is made in place of the old one.
		}
		return true;
	}

	/**
	 * 
	 * @param stuid A student ID (assumed to be an integer)
	 * @param name A name (will be truncated to size
	 * {@link #NAME_CHARS})
	 * @param ass1 A mark for Assignment 1
	 * @param ass2 A mark for Assignment 2
	 * @param ass3 A mark for Assignment 3
	 * @param finalExam A mark for the final exam
	 * @throws SQLException
	 */
	public void insertRecordButton(int stuid, String name, int ass1,
			int ass2, int ass3, int finalExam) throws SQLException
	{

		name = StringTools.sanitise(name);
		//StringTools.sanitise returns the supplied string
		// after truncating it to be the size determined by
		// DatabaseConnection.NAME_CHARS and removing all non-letter
		// characters.

		statement.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES ("
				+stuid + ", '" + name + "', "
				+ass1 + ", " + ass2 + ", "
				+ass3 + ", " + finalExam + ");"
				);
	}

	/**
	 * Returns a String of a general query's results.
	 * @deprecated
	 * @return A String of the query's
	 * {@link java.sql.Statement#executeQuery(String) ResultSet},
	 * separated by commas.
	 * @throws SQLException
	 */
	public String listRecords() throws SQLException
	{
		//This method outputs a general query as a string and 
		// was useful during development. It is no longer used.
		String returnString = new String();

		ResultSet rs = this.query();

		if(rs.next())
		{
			do
			{
				returnString+= rs.getInt(1);
				returnString+= ", ";
				returnString+= rs.getString(2);
				returnString+= ", ";
				returnString+= rs.getInt(3);
				returnString+= ", ";
				returnString+= rs.getInt(4);
				returnString+= ", ";
				returnString+= rs.getInt(5);
				returnString+= ", ";
				returnString+= rs.getInt(6);
				returnString+= "\n";
			}
			while(rs.next());
		}
		else
		{
			returnString+="No records found in table " + TABLE_NAME + "\n";
		}

		return returnString;
	}

	/**
	 * This function is for a general search with no conditions. It is used
	 * after a record is added and at initialisation.
	 * @return A ResultSet object from the search of the query.
	 * @throws SQLException
	 */
	public ResultSet query() throws SQLException
	{
		System.out.println(queryString());
		return statement.executeQuery(queryString()
				+" ORDER BY "+COL_STU_ID+" ASC;");
	}

	/**
	 * Each string defines search terms for a query to be sent to the database.
	 * An empty results in no search conditions for that column.
	 * @param stuid A student ID (assumed to be an integer)
	 * @param name A name (will be truncated to size
	 * {@link #NAME_CHARS})
	 * @param ass1 A mark for Assignment 1
	 * @param ass2 A mark for Assignment 2
	 * @param ass3 A mark for Assignment 3
	 * @param finalExam A mark for the final exam
	 * @return The ResultSet returned by the database.
	 * @throws SQLException
	 */
	public ResultSet query(String stuID, String name, String ass1,
			String ass2, String ass3, String exam) throws SQLException
	{
		String queryStr = new String();
		queryStr += queryString();

		name = StringTools.sanitise(name);

		if(! (stuID.isEmpty()||name.isEmpty()||ass1.isEmpty())
				||ass2.isEmpty()||ass3.isEmpty()||exam.isEmpty() )
		{
			queryStr+= " WHERE";
			boolean first = true;
			if(!stuID.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_STU_ID;
				queryStr+= " = ";
				queryStr+= stuID;
			}
			if(!name.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_NAME;
				queryStr+= " = '";
				queryStr+= name;
				queryStr+= "'";
			}
			if(!ass1.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_ASS_1;
				queryStr+= " = ";
				queryStr+= ass1;
			}
			if(!ass2.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_ASS_2;
				queryStr+= " = ";
				queryStr+= ass2;
			}
			if(!ass3.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_ASS_3;
				queryStr+= " = ";
				queryStr+= ass3;
			}
			if(!exam.isEmpty())
			{
				if(first)
				{
					first = false;
				}
				else
				{
					queryStr+="&&";
				}
				queryStr+=" ";
				queryStr+= COL_EXAM;
				queryStr+= " = ";
				queryStr+= exam;
			}
			queryStr +=" ORDER BY "+COL_STU_ID+" ASC;";
		}
		System.out.println(queryStr);
		return statement.executeQuery(queryStr);
	}

	/**
	 * Composes the beginning of a query to send to the connected database
	 * which will select every column with a more formal name and adds the
	 * "Final Mark" column, computed by converting the four assessment marks
	 * into their appropriate weightings.
	 * @return A string which is far too long to be typed out more than once.
	 */
	private String queryString()
	{
		String out = new String();
		out+= "SELECT ";
		out+= COL_STU_ID;
		out+= " as \"Student ID\", ";
		out+= COL_NAME;
		out+= " as \"Name\", ";
		out+= COL_ASS_1;
		out+= " as \"Assignment 1\", ";
		out+= COL_ASS_2;
		out+= " as \"Assignment 2\", ";
		out+= COL_ASS_3;
		out+= " as \"Assignment 3\", ";
		out+= COL_EXAM;
		out+= " as \"Final Exam\", ";
		out+= "( 1 * ";
		out+= COL_ASS_1;
		out+= "+ 2 * ";
		out+= COL_ASS_2;
		out+= "+ 2 * ";
		out+= COL_ASS_3;
		out+= "+ 5 * ";
		out+= COL_EXAM;
		out+= ") / 10";
		out+= " as \"Final Score\" FROM ";
		out+= TABLE_NAME;

		return out;
	}

	/**
	 * The function which composes and executes the SQL command to create the 
	 * {@link #TABLE_NAME} table with its 6 fields.
	 * @throws SQLException
	 */
	private void createTable() throws SQLException
	{
		statement.executeUpdate("CREATE TABLE " + TABLE_NAME +" ("
				+COL_STU_ID+" integer, "
				+COL_NAME+" char("+NAME_CHARS+"), "
				+COL_ASS_1+" integer, "
				+COL_ASS_2+" integer, "
				+COL_ASS_3+" integer, "
				+COL_EXAM+" integer, "
				+"PRIMARY KEY ("+COL_STU_ID+"));"
				);
	}

	/**
	 * This function closes the {@link java.sql.Statement Statement} and
	 * {@link java.sql.Connection Connection} objects.
	 * @throws SQLException
	 */
	public void close() throws SQLException
	{
		statement.close();
		connection.close();
	}

}
