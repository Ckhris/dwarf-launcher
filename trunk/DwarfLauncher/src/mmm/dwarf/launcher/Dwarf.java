package mmm.dwarf.launcher;

public class Dwarf {
	private long id;
	private double latitude;
	private double longitude;

	public Dwarf(){}
	public Dwarf(double lat, double lng){
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return latitude+", "+longitude;
	}
}
