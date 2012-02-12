package mmm.dwarf.launcher;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class DwarfOverlayItem extends OverlayItem {
	private long id;

	public DwarfOverlayItem(GeoPoint point, String title, String snippet, long id) {
		super(point, title, snippet);
		this.id=id;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
