package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JTextArea;

/**
 * A JTextArea which is not editable and can be sent a ResultSet object
 * to be displayed in a table-like form.
 * @author Edward Hummerston - 11477172
 *
 */
public class RecordDisplay extends JTextArea{

	private static final long serialVersionUID = 1L;

	public RecordDisplay()
	{
		this.setTabSize(DatabaseConnection.NAME_CHARS + 2);
		this.setEditable(false);
	}
	
	/**
	 * Uses the column names of a ResultSet and its contents to
	 * {@link javax.swing.JTextArea #append(String) append} the data in a
	 * reasonably readable format.
	 * @param rs The ResultSet to be displayed.
	 * @throws SQLException
	 */
	public void display (ResultSet rs) throws SQLException
	{
		this.setText(null);
		
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i = 0; i < 7; i++)
		{
			this.append((rsmd.getColumnLabel(i+1)));
			this.append("\t");
		}
		this.append("\n");

		while(rs.next())
		{
			for(int i = 0; i < 7; i++)
			{
				this.append(rs.getString(i+1));
				this.append("\t");
			}
			this.append("\n");
		}
		rs.close();
	}

}
