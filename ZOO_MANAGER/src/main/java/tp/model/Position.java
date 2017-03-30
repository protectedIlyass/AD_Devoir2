package tp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Position {

	private double latitude,longitude;
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Position() {	}

	public boolean equals(Object o){
		boolean result = false;
		if (o instanceof Position){
			Position otherPosition = (Position)o; 
			result = otherPosition.latitude == this.latitude && 
					 otherPosition.longitude == this.longitude;
		}
		return result;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public boolean near (Position P){
		
		Double lat = P.getLatitude();
		Double lng = P.getLongitude();
		
		Double myLat= this.getLatitude();
		Double myLng= this.getLongitude();
		
		Double diffLat;
		Double diffLng;
		
		
		if(lat.compareTo(myLat)<0)
			diffLat= myLat-lat;
		else
			diffLat= lat-myLat;
		
		
		if(lng.compareTo(myLng)<0)
			diffLng= myLng-lng;
		else
			diffLng= lng-myLng;
		
		if(diffLat <1 && diffLng <1){
			return true;
		}
		
		return false;
		
	}
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(latitude).append(", ").append(longitude).append(")");
		return buffer.toString();
	}
	
}
