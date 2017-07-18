package database;

/**
 * A collection of static methods to be used with Strings to assist in the
 * brevity of the program.
 * @author Edward Hummerston - 11477172
 *
 */
public class StringTools {

	/**
	 * Used to truncate a String to fit it within the
	 * {@link DatabaseConnection #COL_NAME}, it is therefore to be of length
	 * {@link DatabaseConnection #NAME_CHARS} or less. Additionally, all
	 * non-letter characters are replaced with white-space characters.
	 * @param in The String to be 'sanitised'.
	 * @return The sanitised String
	 */
	public static String sanitise(String in)
	{
		if(in.length()>DatabaseConnection.NAME_CHARS)
		{
			in = in.substring(0, DatabaseConnection.NAME_CHARS);
		}
		char[] charray = in.toCharArray();

		for(int i = 0; i < charray.length; i++)
		{
			if(! ((charray[i]>='a'&&charray[i]<='z')||
					(charray[i]>='A'&&charray[i]<='Z') ))
			{
				charray[i] = ' ';
			}
		}

		String out = String.valueOf(charray);

		return out;
	}

	/**
	 * If the String has any non-numerical characters or is empty, this will
	 * return false. This means that any decimal places or negative signs
	 * will return false, so the String can only be an unsigned integer to
	 * return true. 
	 * @param in The String which is to be checked for integer-likeness.
	 * @return Is the String an integer?
	 */
	public static boolean isInt(String in)
	{
		if (in == null)
		{
			return false;
		}
		int length = in.length();
		if (length == 0)
		{
			return false;
		}
		for (int i = 0; i < length; i++)
		{
			char c = in.charAt(i);
			if (c < '0' || c > '9')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * If {@link #isInt(String)}, returns false, this method will return 0.
	 * If {@code isInt} returns true, it is a very simple calculation to
	 * determine the integer value that the String is depicting. 
	 * @param in A String to be converted.
	 * @return The value which the String depicted.
	 */
	public static int toInt(String in)
	{
		if (!isInt(in))
		{
			return 0;
		}
		int out = 0;
		for (int i = 0; i < in.length(); i++)
		{
			char c = in.charAt(in.length()-(i+1));
			out += Math.pow(10, i)*(c-48);
		}
		return out;
	}

}
