import java.sql.SQLException;

import org.json.JSONException;

import me.navigation.server.DatabaseOperations;
import me.navigation.shared.LatLong;


public class Test {

	public static void main(String args[]) throws JSONException, SQLException
	{
		//   
		LatLong l1 = new LatLong(34.059379,-118.448048);
		LatLong l2 = new LatLong(34.059966,-118.446933);
		
		DatabaseOperations s = new DatabaseOperations();
		s.getData(l1,l2);
		
	}
}
