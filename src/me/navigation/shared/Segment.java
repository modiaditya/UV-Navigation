package me.navigation.shared;

import me.navigation.server.HttpSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Segment {

	private LatLong start_point;
	private LatLong end_point;
	private double uva;
	private double uvb;
	private int no_of_readings;
	
	@Override
	public String toString() {
		
		return start_point+" to "+end_point;
		
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
