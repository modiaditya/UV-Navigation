import org.json.JSONException;

import me.navigation.shared.LatLong;


public class Test {

	public static void main(String args[]) throws JSONException
	{
		LatLong l = new LatLong(34.0,-118.2);
		System.out.println(l.getJson("start_location").toString());
	}
}
