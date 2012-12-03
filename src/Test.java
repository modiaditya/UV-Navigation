import java.sql.SQLException;

import org.json.JSONException;

import me.navigation.server.DatabaseOperations;
import me.navigation.shared.LatLong;


public class Test {

	public static void main(String args[]) 
	{
		//   
		LatLong l1 = new LatLong(34.059379,-118.448048);
		LatLong l2 = new LatLong(34.059966,-118.446933);
		
		DatabaseOperations s = new DatabaseOperations();
		try{
		s.getData(l1,l2);
		}
		catch(Exception e)
		{
			System.out.println("There is a database error");
			System.out.println(e.getMessage());
		}
	}
}
