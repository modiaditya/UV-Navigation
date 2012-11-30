package me.navigation.shared;

import java.net.SocketTimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.navigation.server.API_Parser;
import me.navigation.server.HttpSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Aditya
 * This class is used to intialize the route information.
 * It gets data from Google Maps and the also calls the steps to be initialized.
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
	private String polylines;
	
	/**
	 * 
	 * Initializes all the class variables in the function
	 * @throws Exception 
	 * 
	 */
	public void initialize() throws Exception
	{
		JsonElement jElement = new JsonParser().parse(googleAPIJson);
		JsonObject jObject = jElement.getAsJsonObject();
		summary = jObject.get("summary").toString().toString().replace("\"","");
		polylines = jObject.getAsJsonObject("overview_polyline").get("points").toString().replace("\"","");
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
			// initializes the steps
			steps[i].initialize();
		}
		
		// after all the steps are initialized, this method is called to set the UV values
		setUVValues();
		
	}
	
	/**
	 * get the UV data of each step and then take a weighted average to set the UV exposure on each route.
	 * 
	 */
	private void setUVValues() {
		
		//taking weighted average
		double numeratorUVA = 0;
		double denominatorUVA = 0;
		double numeratorUVB = 0;
		double denominatorUVB = 0;
		
		// get the weighted average of the steps to find the UV across a specific route
		for(int i=0;i<steps.length;i++)
		{
			if(steps[i].getUva()>0)
			{
				numeratorUVA+= steps[i].getDistance()*steps[i].getUva();
				denominatorUVA += steps[i].getDistance();
			}
			
			if(steps[i].getUvb()>0)
			{
				numeratorUVB+=steps[i].getDistance()*steps[i].getUvb();
				denominatorUVB += steps[i].getDistance();
			}
			
		}
		
		uva = numeratorUVA/denominatorUVA;
		uvb = numeratorUVB/denominatorUVB;
		
	}

	
	/**
	 * @return JSONObject, all the route information is encoded.
	 * @throws JSONException
	 */
	public JSONObject getJson() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("distance", this.distance);
		//JSONObject duration = new JSONObject();
		obj.put("duration", this.duration);
		//JSONObject start_location = new JSONObject();
		obj.put("start_location", this.start_location.getJson());
		//JSONObject end_location = new JSONObject();
		obj.put("end_location",this.end_location.getJson());
		obj.put("uv",this.getUVJson());
		//JSONObject summary = new JSONObject();
		obj.put("summary",this.summary);
		obj.put("polylines", this.polylines);
		//JSONObject[] stepsJson = new JSONObject[steps.length]; 
		JSONArray arr = new JSONArray();
		
		for(int i =0 ;i < steps.length;i++)
		{
			arr.put(i,steps[i].getJson());
			
		}
		obj.put("steps", arr);
		
		return obj;	
	}

	
	
	public  JSONObject getUVJson() throws JSONException
	{
		JSONObject uv = new JSONObject();
		uv.put("uva", this.uva);
		uv.put("uvb", this.uvb);
		return uv;
		
		
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

	public String getPolylines() {
		return polylines;
	}

	public void setPolylines(String polylines) {
		this.polylines = polylines;
	}

}
