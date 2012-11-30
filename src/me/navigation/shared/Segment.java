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

/**
 * @author Aditya
 * This class is used to initialize the segments by calling the database to get the UV values of points inside the segment object.
 */
public class Segment {

	private LatLong start_location;
	private LatLong end_location;
	private double uva=0;
	private double uvb=0;
	private int no_of_readings=0;
	
	/**
	 * @throws SQLException
	 * Initialize the segment by getting the readings for each segment
	 */
	public void initialize() throws SQLException
	{
		getReadings();
	}
	
	@Override
	public String toString() {
		
		return start_location+","+end_location+","+uva+","+uvb+","+no_of_readings;
		
	}
	
	
	/**
	 * @throws SQLException
	 * sets the UVA and UVB values for each segment by calling the database and getting 
	 * the UV values of points in each segment and then taking an average of points
	 */
	public void getReadings() throws SQLException
	{
    	final int feetVariance = 20;
		Connection con = null;
	    String url = "jdbc:mysql://localhost:3306/project";
        String username = "adityauv";
        String password = "uvnavigation";
        
        con = DriverManager.getConnection(url, username, password);
		
        // Bounding box is used to counter for the variance of error on the GPS data points
        BoundingBox x = new BoundingBox(feetVariance);
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
		return start_location;
	}


	public void setStart_point(LatLong start_point) {
		this.start_location = start_point;
	}


	public LatLong getEnd_point() {
		return end_location;
	}


	public void setEnd_point(LatLong end_point) {
		this.end_location = end_point;
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
