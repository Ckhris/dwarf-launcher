package mmm.dwarf.launcher;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends com.google.android.maps.ItemizedOverlay {

	private ArrayList<DwarfOverlayItem> mOverlays = new ArrayList<DwarfOverlayItem>();
	private Context mContext;

	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public void addOverlay(DwarfOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected DwarfOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		/*OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();*/
		LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		DwarfOverlayItem dwarf=mOverlays.get(index);
		GeoPoint g=dwarf.getPoint();
		float latitude = (float) (g.getLatitudeE6()/1E6);
		float longitude = (float) (g.getLongitudeE6()/1E6);

		Location loc= new Location(Context.LOCATION_SERVICE);
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		
		//if(location.distanceTo(loc)<50){

			SharedPreferences settings = mContext.getSharedPreferences("lance", 4);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong("idTouch", dwarf.getId());
			editor.commit();
			Intent intent=new Intent(mContext, LaunchPlateformActivity.class);
			mContext.startActivity(intent);
		//}else{
			Toast toast = Toast.makeText(mContext, "Nain trop loin! Rapproche toi feignant!", Toast.LENGTH_LONG);
			toast.show();
		//}
		return true;
	}
}
