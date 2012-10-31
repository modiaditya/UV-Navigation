package me.navigation.shared;

import java.net.SocketTimeoutException;

import me.navigation.server.API_Parser;
import me.navigation.server.HttpSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Aditya
 *
 */
public class Routes {


	private String googleAPIJson;
	private int distance;
	private int duration;
	private LatLong start_location;
	private LatLong end_location;
	private double uva;
	private double uvb;
	private String summary;
	private Step[] steps;
	
	/**
	 * 
	 * Initializes all the class variables in the function
	 * @throws SocketTimeoutException 
	 * 
	 */
	public void initialize() throws Exception
	{
		JsonElement jElement = new JsonParser().parse(googleAPIJson);
		JsonObject jObject = jElement.getAsJsonObject();
		summary = jObject.get("summary").toString();
		JsonObject legs = jObject.getAsJsonArray("legs").get(0).getAsJsonObject();
		distance =  Integer.parseInt(legs.getAsJsonObject("distance").get("value").toString());		
		duration = Integer.parseInt(legs.getAsJsonObject("duration").get("value").toString());
		start_location = new LatLong(Double.parseDouble(legs.getAsJsonObject("start_location").get("lat").toString()), Double.parseDouble(legs.getAsJsonObject("start_location").get("lng").toString())); 
		end_location = new LatLong(Double.parseDouble(legs.getAsJsonObject("end_location").get("lat").toString()), Double.parseDouble(legs.getAsJsonObject("end_location").get("lng").toString()));
		//initialize step information
		steps = new Step[legs.getAsJsonArray("steps").size()];
		
		for(int i=0;i<steps.length;i++)
		{
			steps[i]= new Step();
			steps[i].setGoogleAPIJson(API_Parser.getStepInformation(googleAPIJson, i));
			steps[i].initialize();
		}
	}
	
	
	
	/**
	 * Set the number of segments for each step by calling the yournavigation API
	 * @throws SocketTimeoutException
	 *
	 */
	
	
	/**
	 * This function is used to set all the coordinates between two points 
	 * @throws Exception
	 *
	 */
//	public void setPoints() throws Exception
//	{
//		String endpoint = "http://www.yournavigation.org/api/1.0/gosmore.php";
//		String parameters = "format=geojson&flat="+this.getStart_location().getLatitude()+"&flon="+this.getStart_location().getLongitude()+"&tlat="+this.getEnd_location().getLatitude()+"&tlon="+this.getEnd_location().getLongitude()+"&v=foot";
//		
//		String jsonLine = HttpSender.sendGetRequest(endpoint, parameters);
//		
//		JsonElement jElement = new JsonParser().parse(jsonLine);
//		JsonArray jArray = jElement.getAsJsonObject().getAsJsonArray("coordinates");
//		this.points = new UVData[jArray.size()];
//		String one ;
//		String[] sp;
//		LatLong j;
//		for(int i=0;i<jArray.size();i++)
//		{
//			points[i] = new UVData();
//			j= new LatLong();
//			one = jArray.get(i).toString();
//			one = one.replace('[', ' ');
//			one = one.replace(']', ' ');
//			sp = one.trim().split(",");
//			j.setLatitude(Double.parseDouble(sp[1]));
//			j.setLongitude(Double.parseDouble(sp[0]));
//			points[i].setPoint(j);
//			one ="";
//		}
//		
//		
//	}
	public String toString()
	{
		return distance+","+duration+","+start_location+","+end_location+","+uva+","+uvb+","+summary;
	}
	
	public String getGoogleAPIJson() {
		return googleAPIJson;
	}


	public void setGoogleAPIJson(String googleAPIJson) {
		this.googleAPIJson = googleAPIJson;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public LatLong getStart_location() {
		return start_location;
	}


	public void setStart_location(LatLong start_location) {
		this.start_location = start_location;
	}


	public LatLong getEnd_location() {
		return end_location;
	}


	public void setEnd_location(LatLong end_location) {
		this.end_location = end_location;
	}


	public double getUva() {
		return uva;
	}


	public void setUva(double uva) {
		this.uva = uva;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Step[] getSteps() {
		return steps;
	}


	public void setSteps(Step[] steps) {
		this.steps = steps;
	}


	public double getUvb() {
		return uvb;
	}

	public void setUvb(double uvb) {
		this.uvb = uvb;
	}

}
