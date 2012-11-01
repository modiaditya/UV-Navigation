package me.navigation.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import me.navigation.server.BoundingBox;
import me.navigation.server.DatabaseOperations;
import me.navigation.server.HttpSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Segment {

	private LatLong start_point;
	private LatLong end_point;
	private double uva=0;
	private double uvb=0;
	private int no_of_readings=0;
	
	public void initialize() throws SQLException
	{
		getReadings();
		//DatabaseOperations o = new DatabaseOperations();
		//o.getData(start_point, end_point);
		
	}
	
	@Override
	public String toString() {
		
		return start_point+","+end_point+","+uva+","+uvb+","+no_of_readings;
		
	}
	
//	public JsonObject getJson()
//	{
//		//JSONObject obj = new JSONObject();
//		//JsonObject lat = new JsonObject();
//		//JsonObject lng = new JsonObject();
//		//lat.add("Lat", start_point.getLatitude());
//		//lng.add("Lng",start_point.getLongitude());
//		
//		
//		
//	}
	
	public void getReadings() throws SQLException
	{
		Connection con = null;
	    String url = "jdbc:mysql://localhost:3306/project";
        String username = "adityauv";
        String password = "uvnavigation";
        
        con = DriverManager.getConnection(url, username, password);
    	BoundingBox x = new BoundingBox(20);
    	x.getBoundingBox(this.getStart_point(), this.getEnd_point());
    	LatLong min = x.getMin();
    	LatLong max = x.getMax();
    	
    	String sql = "select distinct Latitude, Longitude, UVA1, UVA2, UVB1, UVB2 " +
    				"from uvReadings where Time > '2012-10-22' " +
    				"and UVB1 > 7.289 and UVA1 > 9.719 and Latitude > ?  and Longitude > ? " +
    				"and Latitude < ? and Longitude < ?";
    	PreparedStatement p = con.prepareStatement(sql);
    	p.setDouble(1, min.getLatitude());
    	p.setDouble(2, min.getLongitude());
    	p.setDouble(3,max.getLatitude());
    	p.setDouble(4, max.getLongitude());
    	ResultSet rs = p.executeQuery();
    	while(rs.next())
    	{
    		no_of_readings++;
    		this.uva += rs.getDouble("UVA1");
    		this.uvb += rs.getDouble("UVB1");
    	}
    	if(no_of_readings>0)
    	{
    		this.uva/=no_of_readings;
    		this.uvb/=no_of_readings;
    	}
    	rs.close();
    	con.close();
        
        
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


	public int getNo_of_readings() {
		return no_of_readings;
	}


	public void setNo_of_readings(int no_of_readings) {
		this.no_of_readings = no_of_readings;
	}


	
}
