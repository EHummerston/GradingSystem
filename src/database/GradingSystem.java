package database;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * A university grading system which maintains a number of tables in order
 * to store, retrieve and manipulate student marks. 
 * @author Edward Hummerston - 11477172
 *
 */
public class GradingSystem extends JFrame{

	private static final long serialVersionUID = 1L;
	DatabaseConnection db;
	RecordDisplay rd;

	JButton createTable = new JButton("Create Table");
	JButton insertRecord = new JButton("Insert Record");
	JButton searchTable = new JButton("Search");
	JTextField stuIDField = new JTextField(10);
	JTextField nameField = new JTextField(10);
	JTextField ass1Field = new JTextField(10);
	JTextField ass2Field = new JTextField(10);
	JTextField ass3Field = new JTextField(10);
	JTextField examField = new JTextField(10);
	private ButtonListener btnLsn;

	public static void main(String[] args)
	{
		new GradingSystem();

	}

	public GradingSystem()
	{

		setTitle("University Grading System");
		setSize(1000,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);

		setLayout(new GridLayout(2,0,5,5));

		JPanel controlPanel = new JPanel();
		JScrollPane sp = new JScrollPane(controlPanel);
		add(sp);
		controlPanel.setLayout(new GridLayout(0,2,10,10));
		
		btnLsn = new ButtonListener();
		createTable.addActionListener(btnLsn);
		insertRecord.addActionListener(btnLsn);
		searchTable.addActionListener(btnLsn);
		
		controlPanel.add(createTable);
		controlPanel.add(new JPanel());

		controlPanel.add(new JLabel("Student ID <Unique Integer>",JLabel.RIGHT));
		controlPanel.add(stuIDField);
		controlPanel.add(new JLabel("Name <String e.g. \"Edgar\">",JLabel.RIGHT));
		controlPanel.add(nameField);
		controlPanel.add(new JLabel("Assignment 1 <Integer>",JLabel.RIGHT));
		controlPanel.add(ass1Field);
		controlPanel.add(new JLabel("Assignment 2 <Integer>",JLabel.RIGHT));
		controlPanel.add(ass2Field);
		controlPanel.add(new JLabel("Assignment 3 <Integer>",JLabel.RIGHT));
		controlPanel.add(ass3Field);
		controlPanel.add(new JLabel("Final Exam <Integer>",JLabel.RIGHT));
		controlPanel.add(examField);
		
		controlPanel.add(insertRecord);
		controlPanel.add(searchTable);

		rd = new RecordDisplay();

		add( new JScrollPane(rd));

		try {
			db = new DatabaseConnection();
			db.newTableButton(false);
			rd.display(db.query());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==createTable)
			{
				try {
					if(!db.newTableButton(false))
					{
						String warningMessage = new String();
						warningMessage += DatabaseConnection.TABLE_NAME;
						warningMessage += " table already exists.\nCreating a new table will delete the old.";
						if(JOptionPane.showConfirmDialog(null, warningMessage, "", JOptionPane.OK_CANCEL_OPTION)
								==JOptionPane.OK_OPTION)
						{
							db.newTableButton(true);

							rd.display(db.query());
						}
						else
						{
							rd.display(db.query());
						}
					}

				} catch (HeadlessException | SQLException e1) {
					e1.printStackTrace();
				}
			}

			if(e.getSource()==insertRecord)
			{
				if(! (stuIDField.getText().isEmpty()||nameField.getText().isEmpty()||
						ass1Field.getText().isEmpty()||ass2Field.getText().isEmpty()||
						ass3Field.getText().isEmpty()||examField.getText().isEmpty()) )
				{
					if(StringTools.isInt(stuIDField.getText()
							+ass1Field.getText()+ass2Field.getText()
							+ass3Field.getText()+examField.getText()))
					{
						try {
							
							if(!db.query(stuIDField.getText(),
									"","","","","").next())
							{

							db.insertRecordButton(StringTools.toInt(stuIDField.getText()),
									nameField.getText(), StringTools.toInt(ass1Field.getText()),
									StringTools.toInt(ass2Field.getText()), StringTools.toInt(ass3Field.getText()),
									StringTools.toInt(examField.getText()));
							rd.display(db.query());
							}
							else
							{
								JOptionPane.showMessageDialog(null,"There is already an entry with that ID!",
										"Error",JOptionPane.ERROR_MESSAGE);
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null,"One of the integer fields isn't an integer!",
								"Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,"At least one field is empty,\nCan't add record.",
							"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
			if(e.getSource()==searchTable)
			{
					if( (StringTools.isInt(stuIDField.getText()
							+ass1Field.getText()+ass2Field.getText()
							+ass3Field.getText()+examField.getText()))
							||( stuIDField.getText().isEmpty() && ass1Field.getText().isEmpty()
									&& ass2Field.getText().isEmpty() && ass3Field.getText().isEmpty()
									&& examField.getText().isEmpty()))
					{
						try {
							rd.display(db.query(stuIDField.getText(),
									nameField.getText(),ass1Field.getText(),
									ass2Field.getText(),ass3Field.getText(),
									examField.getText()));
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null,"One of the integer fields isn't an integer!",
								"Error",JOptionPane.ERROR_MESSAGE);
					}
			}

							
							
		}
	}
}
