# SQL Grading System

Created as part of the *Programming in Java 2* course at Charles Sturt University

## Functionality

Upon initialising this program, a GUI is initialised in a window and a connection to the database found at `jdbc:mysql://localhost/ST11477172` is established with the username `root` and the password `abc123`. If any of this information is incorrect, the program will fail, though this information can be found as modifiable variables in the class elements of the `database.DatabaseConnection` class.

The program creates a `Student_marks_ITC000` table (or one has been detected and a new one has not been made). The “Create Table” button will attempt to make a new table of the same button but, on discovering the existence of one already, will prompt the user to confirm the deletion of the current table for the sake of a new one. The text fields on the right can have data entered into them, and then a record with that data can be inserted into the table.

The program will alert the user and not add a record if any of the fields are empty, if any of the integer fields do not contain integers, or if there already exists an entry with the specified student ID. At program initialisation, table creation and record insertion, the text area at the bottom of the window displays the results of a general query with no search parameters (therefore all the records). They are sorted in order of Student ID ascending. The final column “Final Score” is not stored in database, but is instead calculated from the data of the preceding four columns, corresponding to the weightings defined in the assignment question. Search terms can be specified with entries in the text fields.

The program will then retrieve and display only records whose fields match the values within the text fields. A search with all entries blank will therefore display the same general search as any action which changes the state of the data (insertion of table creation) and if no records match the input data, no entries will be displayed.
