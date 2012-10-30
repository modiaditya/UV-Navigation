package me.navigation.server;

import me.navigation.shared.LatLong;


/**
 * @author Aditya
 * This class is responsible to get the bounbing box when we pass it two points
 * An object of this class should be created 
 */
public class BoundingBox {

	private LatLong min= new LatLong();
	private LatLong max= new LatLong();
	private final double DEGREE_VARIANCE_PER_FEET = 0.0000027;
	private double feet = 100;
	private double degreeVariance = feet * DEGREE_VARIANCE_PER_FEET;
	BoundingBox()
	{
		this.setDegreeVariance(feet * DEGREE_VARIANCE_PER_FEET);
		
	}
	
	public BoundingBox(double feet)
	{
		this.setFeet(feet);
		this.setDegreeVariance(feet * DEGREE_VARIANCE_PER_FEET);
	}
	
	public void getBoundingBox(LatLong p1, LatLong p2)
	{

		
		this.min.setLatitude(Math.min(p1.getLatitude(), p2.getLatitude())-this.getDegreeVariance());
		this.min.setLongitude(Math.min(p1.getLongitude(), p2.getLongitude())-this.getDegreeVariance());
		this.max.setLatitude(Math.max(p1.getLatitude(), p2.getLatitude())+this.getDegreeVariance());
		this.max.setLongitude(Math.max(p1.getLongitude(), p2.getLongitude())+this.getDegreeVariance());
		
	}
	
	public double getDistance(LatLong p1, LatLong p2)
	{
		long R = 6379; // km
		double dLat = Math.toRadians(p2.getLatitude()-p1.getLatitude());
		double dLon = Math.toRadians(p2.getLongitude()-p2.getLongitude());
		double lat1 = Math.toRadians(p1.getLatitude());
		double lat2 = Math.toRadians(p2.getLatitude());

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return  (R * c);
	}
	public LatLong getMin() {
		return min;
	}
	public void setMin(LatLong min) {
		this.min = min;
	}
	public double getDegreeVariance() {
		return degreeVariance;
	}
	public void setDegreeVariance(double degreeVariance) {
		this.degreeVariance = degreeVariance;
	}
	public double getFeet() {
		return feet;
	}
	public void setFeet(double feet) {
		this.feet = feet;
	}
	public LatLong getMax() {
		return max;
	}
	public void setMax(LatLong max) {
		this.max = max;
	}
	public double getDEGREE_VARIANCE_PER_FEET() {
		return DEGREE_VARIANCE_PER_FEET;
	}
	
	public static void main(String args[])
	{
		
		
	}
}
