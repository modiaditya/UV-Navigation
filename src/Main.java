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


public class Main {

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
		
		
		boolean testBoundingBox=false;
		
		if(testBoundingBox)
		{
			// code for testing bounding box starts
			LatLong p1 = new LatLong(34.066487,-118.453407);
			//p1.setLatitude(34.064312);
			//p1.setLongitude(-118.451921);
			LatLong p2 = new LatLong(34.065705,-118.453107);
			//p2.setLatitude(34.064159);
			//p2.setLongitude(-118.452798);
			BoundingBox x = new BoundingBox(20);
			x.getBoundingBox(p1, p2);
			System.out.println(x.getMin().getLatitude()+","+x.getMin().getLongitude()+"\n"
					+x.getMax().getLatitude()+","+x.getMax().getLongitude());
			// code to test bounding box ends
			System.out.println(x.getDistance(p1, p2)+"");
			String url = "jdbc:mysql://localhost:3306/project";
	        String username = "adityauv";
	        String password = "uvnavigation";
			DatabaseOperations o = new DatabaseOperations(url, username, password);
			o.getData(p1, p2);
			

		}
		
		
		boolean testRoutes = true;
		if(testRoutes)
		{
			LatLong source = new LatLong(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			LatLong destination = new LatLong(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
			
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
				for(int k=0;k<s.length;k++)
				{
					Segment[]  seg= s[k].getSegments();
					int j=0;
					for(j=0;j<seg.length;j++)
					{
						System.out.print(seg[j].getStart_point());
						System.out.println(","+seg[j].getUva()+","+seg[j].getUvb());
					}
					System.out.println(seg[j-1].getEnd_point());
					//System.out.println("-----------");
				}
				//System.out.println("------------------------");
				
				
				//System.out.println(allRoutes[i].getJson());
				//System.out.println(allRoutes[0]);
			}
			
			JSONObject obj = new JSONObject();
			obj.put("routes", arr);
			
			
			int minRoute = getMinUVARoute(allRoutes);
			
			JSONObject minUVARoute = new JSONObject();
			minUVARoute.put("routes", allRoutes[minRoute].getJson());
			
			//System.out.println(minUVARoute);
			//System.out.println("Min UVB route is "+getMinUVBRoute(allRoutes));
			
			int minUVABRoute = getMinUVABRoute(allRoutes);
			//System.out.println("Min UVA and UVB route is "+minUVABRoute);
			
			System.out.println(obj.toString());
			
			//database related
			String url = "jdbc:mysql://localhost:3306/project";
	        String username = "adityauv";
	        String password = "uvnavigation";
			DatabaseOperations o = new DatabaseOperations(url, username, password);
			
			
			// database related ends
	//		
	//		
	//		System.out.println(allRoutes[index].getJson().toString());
	//		for(int i=0;i<allRoutes[index].getSteps().length;i++)
	//		{
	//			System.out.println("---------------------------------");
	//			System.out.println(allRoutes[index].getSteps()[i].getJson().toString());
	//			System.out.println("---------------------------------");
	//			Segment[] d = allRoutes[index].getSteps()[i].getSegments();
	//			
	//			for(int j=0;j<d.length;j++)
	//			{
	//				System.out.println(d[j]);
	//				//System.out.println(d[j].getStart_point());
	//				//o.getData(d[j].getStart_point(), d[j].getEnd_point());
	//				//System.out.println(d[j].getEnd_point());
	//			}
	//			
	//		}
		}
		
	}
}
