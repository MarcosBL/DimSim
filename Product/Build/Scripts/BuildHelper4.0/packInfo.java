import java.io.*;
import java.util.*;
import java.text.*;

	public class packInfo
	{
		public static void main(String args[])
		{
			Date now = new Date();
			DateFormat df = DateFormat.getDateInstance();
			String component = "Component : " +args[0];
			String date = "Date : " + df.format(now);;
			String version = "Version : " + args[1];
			
			try
				{
					FileWriter fstream = new FileWriter("packInfo.txt");
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(component);
					out.newLine();
					out.write(date);
					out.newLine();
					out.write(version);
					out.close();
				}
			
			catch (Exception e)
				{
					System.err.println("Error in opening a file");
				}
			
			
		}
	}