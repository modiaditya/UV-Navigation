import java.io.ObjectInputStream.GetField;
import java.net.SocketTimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;

import me.navigation.server.DatabaseOperations;
import me.navigation.server.API_Parser;
import me.navigation.server.BoundingBox;
import me.navigation.server.HttpSender;
import me.navigation.shared.LatLong;
import me.navigation.shared.Routes;
import me.navigation.shared.Segment;
import me.navigation.shared.Step;
import me.navigation.shared.UVData;


/**
 * @author Aditya
 * This call is used to call the other classes by passing the source and destination points
 * It also returns the routes with minimum uv exposure
 * The criteria needs to be specified in the commandline arguments
 * 1 => Minimum UVA, 2=> Minimum UVB, 3 => Minimum of Avg of UVA and UVB
 * sample commandline arguments to run the code and get the route with minimum UVA exposure
 * 1 34.066454 -118.45307 34.059504 -118.44777
 *
 */
public class Main {

	
	/**
	 * @param route, array of routes
	 * @return the route number with the minimum UVA exposure
	 */
	public static int getMinUVARoute(Routes[] route)
	{
		double min=999;
		int index=-1;
		
		//sum all the values
		for(int i=0;i<route.length;i++)
		{
			if(min>route[i].getUva())
			{
				min = route[i].getUva();
				index = i;
			}
			
		}
		
		return index;
		
	}
	
	
	/**
	 * @param route, array of routes
	 * @return the route number with the minimum UVB exposure
	 */
	public static int getMinUVBRoute(Routes[] route)
	{
		double min=999;
		int index=-1;
		
		//sum all the values
		for(int i=0;i<route.length;i++)
		{
			if(min>route[i].getUvb())
			{
				min = route[i].getUvb();
				index = i;
			}
			
		}
		
		return index;
		
	}
	
	/**
	 * @param route, array of routes
	 * @return the route number with the minimum of avg of UVA and UVB exposure
	 */
	public static int getMinUVABRoute(Routes[] route)
	{
		double min=999;
		int index=-1;
		
		//sum all the values
		for(int i=0;i<route.length;i++)
		{
			if(min>route[i].getUvb())
			{
				min = (route[i].getUvb()+route[i].getUvb())/2;
				index = i;
			}
			
		}
		
		return index;
		
	}
	
	public static void main(String args[]) throws Exception
	{
		
		LatLong source;
		LatLong destination;
		
		// set source and destination depending on the arguments passed
		if(args.length==4 || Integer.parseInt(args[0]) == 0){
			source = new LatLong(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			destination = new LatLong(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
		}
		else
		{
			source = new LatLong(Double.parseDouble(args[1]), Double.parseDouble(args[2]));
			destination = new LatLong(Double.parseDouble(args[3]), Double.parseDouble(args[4]));
		}

		String endpoint = "http://maps.googleapis.com/maps/api/directions/json";

		String requestParameters = "sensor=false&mode=walking&alternatives=true&origin="+source.getLatitude()+","+source.getLongitude()+"&destination="+destination.getLatitude()+","+destination.getLongitude();
		String googleMapsResult=  HttpSender.sendGetRequest(endpoint, requestParameters);

		//System.out.println(googleMapsResult);
		Routes[] allRoutes;

		int numberOfRoutes = API_Parser.getNumberOfRoutes(googleMapsResult);
		allRoutes = new Routes[numberOfRoutes];

		JSONArray arr =  new JSONArray();

		// initializing all routes
		// print all the segments on the route
		for(int i=0;i<allRoutes.length;i++)
		{	
			allRoutes[i] = new Routes();
			allRoutes[i].setGoogleAPIJson(API_Parser.getRouteInformation(googleMapsResult, i));
			allRoutes[i].initialize();
			arr.put(i,allRoutes[i].getJson());
			Step[] s = allRoutes[i].getSteps();
		}

		JSONObject obj = new JSONObject();
		obj.put("routes", arr);


		if(args.length==4 || Integer.parseInt(args[0]) == 0)
			System.out.println(obj.toString());


		else
		{
			// return JSON with the selected criteria
			JSONObject output= new JSONObject();
			int expectedOutput = Integer.parseInt(args[0]);

			switch(expectedOutput)
			{
			case 1:
				output.put("routes", allRoutes[getMinUVARoute(allRoutes)].getJson());
				break;

			case 2:
				output.put("routes", allRoutes[getMinUVBRoute(allRoutes)].getJson());
				break;

			case 3:
				output.put("routes", allRoutes[getMinUVABRoute(allRoutes)].getJson());
				break;

			}
			System.out.println(output);

		}
	}

}
