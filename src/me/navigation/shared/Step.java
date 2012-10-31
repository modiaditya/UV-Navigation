package me.navigation.shared;

import java.net.SocketTimeoutException;

import me.navigation.server.HttpSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Step {
	
	private String googleAPIJson;
	private int distance;
	private int duration;
	private LatLong start_point;
	private LatLong end_point;
	private double uva;
	private double uvb;
	private String summary;
	private Segment[] segments;
	
	
	public void initialize() throws Exception
	{
		JsonElement jElementStep = new JsonParser().parse(this.googleAPIJson);
		JsonObject jObjectStep = jElementStep.getAsJsonObject();
		this.distance =  Integer.parseInt(jObjectStep.getAsJsonObject("distance").get("value").toString());		
		this.duration = Integer.parseInt(jObjectStep.getAsJsonObject("duration").get("value").toString());
		this.start_point = new LatLong(Double.parseDouble(jObjectStep.getAsJsonObject("start_location").get("lat").toString()), Double.parseDouble(jObjectStep.getAsJsonObject("start_location").get("lng").toString())); 
		this.end_point = new LatLong(Double.parseDouble(jObjectStep.getAsJsonObject("end_location").get("lat").toString()), Double.parseDouble(jObjectStep.getAsJsonObject("end_location").get("lng").toString()));
		this.summary = jObjectStep.get("html_instructions").toString();
		setSegments();
		//this.segments = new Segments[getNo_of_segments()];
		//this.points= new UVData[this.getNo_of_segments()];
		//this.setPoints();
	}
	
//	public int getNo_of_segments() throws SocketTimeoutException
//	{
//		String endpoint = "http://www.yournavigation.org/api/1.0/gosmore.php";
//		String parameters = "format=geojson&flat="+this.getStart_point().getLatitude()+"&flon="+this.getStart_point().getLongitude()+"&tlat="+this.getEnd_point().getLatitude()+"&tlon="+this.getEnd_point().getLongitude()+"&v=foot";
//		
//		String jsonLine = HttpSender.sendGetRequest(endpoint, parameters);
//		
//		JsonElement jElement = new JsonParser().parse(jsonLine);
//		return jElement.getAsJsonObject().getAsJsonArray("coordinates").size();
//		
//	}
	
	public void setSegments() throws Exception
	{
		String endpoint = "http://www.yournavigation.org/api/1.0/gosmore.php";
		String parameters = "format=geojson&flat="+this.getStart_point().getLatitude()+"&flon="+this.getStart_point().getLongitude()+"&tlat="+this.getEnd_point().getLatitude()+"&tlon="+this.getEnd_point().getLongitude()+"&v=foot";
		
		String jsonLine = HttpSender.sendGetRequest(endpoint, parameters);
		
		JsonElement jElement = new JsonParser().parse(jsonLine);
		JsonArray jArray = jElement.getAsJsonObject().getAsJsonArray("coordinates");
		segments =  new Segment[jArray.size()];
		String one ;
		String two ;
		String[] sp;
		LatLong p1;
		LatLong p2;
		for(int i=0;i<segments.length;i++)
		{
			segments[i] = new Segment();
			p1= new LatLong();
			one = jArray.get(i).toString();
			one = one.replace('[', ' ');
			one = one.replace(']', ' ');
			sp = one.trim().split(",");
			p1.setLatitude(Double.parseDouble(sp[1]));
			p1.setLongitude(Double.parseDouble(sp[0]));
			
			
			p2= new LatLong();
			two = jArray.get(i).toString();
			two = two.replace('[', ' ');
			two = two.replace(']', ' ');
			sp = two.trim().split(",");
			p2.setLatitude(Double.parseDouble(sp[1]));
			p2.setLongitude(Double.parseDouble(sp[0]));
			
			segments[i].setStart_point(p1);
			segments[i].setStart_point(p2);
			one ="";
			two ="";
		}
		
		
	}
	
	public String getGoogleAPIJson() {
		return googleAPIJson;
	}
	public void setGoogleAPIJson(String googleAPIJson) {
		this.googleAPIJson = googleAPIJson;
	}
	public int getDistance() {
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
	public LatLong getStart_point() {
		return start_point;
	}
	public void setStart_point(LatLong start_point) {
		this.start_point = start_point;
	}
	public LatLong getEnd_point() {
		return end_point;
	}
	public void setEnd_point(LatLong end_point) {
		this.end_point = end_point;
	}
	public double getUva() {
		return uva;
	}
	public void setUva(double uva) {
		this.uva = uva;
	}
	public double getUvb() {
		return uvb;
	}
	public void setUvb(double uvb) {
		this.uvb = uvb;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Segment[] getSegments() {
		return segments;
	}
	public void setSegments(Segment[] segments) {
		this.segments = segments;
	}
}
