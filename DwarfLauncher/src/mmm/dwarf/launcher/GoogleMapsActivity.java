package mmm.dwarf.launcher;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends MapActivity {

	private DwarfsDataSource ds;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private double latitude;
	private double longitude;
	private List<Overlay> mapOverlays;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.main);  
		final MapView mapView = (MapView) findViewById(R.id.mapview);
		mapOverlays = mapView.getOverlays();

		mapView.setBuiltInZoomControls(true);

		//Activation du wifi sans autorisation -> Pas top
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
		
		// Acquire a reference to the system Location Manager
		//LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
//		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
//		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		longitude = location.getLongitude();
//		latitude = location.getLatitude();
		ds = new DwarfsDataSource(this);
		ds.open();
		List<Dwarf> dwarfs = ds.getAllDwarfs();
		ds.close();
		for (Dwarf dwarf : dwarfs) {
			afficherPoint(dwarf.getLatitude(), dwarf.getLongitude(),dwarf.getId());
		}
		
		
		
//		// Register the listener with the Location Manager to receive location updates
//		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			latitude = lastLocation.getLatitude();
//			longitude = lastLocation.getLongitude();
//		}
//		else{
//			//Boite de dialogie pour activer le GPS
//			Log.v("testDialogue", "j'envoie la sauce");
//			buildAlertMessageNoGps();
//		}		
		
		SharedPreferences settings= getSharedPreferences("lance", 4);
		if(settings.getInt("lanceX", 0)!=0){
			int lanceX= settings.getInt("lanceX", 0);
			int lanceY= settings.getInt("lanceY", 0);
			mapView.getController().setZoom(21);
			GeoPoint geo=mapView.getProjection().fromPixels(lanceX,lanceY);
			Drawable drawable = this.getResources().getDrawable(R.drawable.little_dwarf);
			HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
			DwarfOverlayItem overlayitem = new DwarfOverlayItem(geo, "Hola, Mundo!", "I'm in Mexico City!",1);
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
			mapView.getController().setZoom(16);
			mapView.getController().animateTo(geo);
			settings.edit().clear();
		}
	}



	protected void afficherPoint(double latitude, double longitude,long id) {
		latitude = latitude* 1E6;
		longitude = longitude* 1E6;
		Log.v("Chris", "Afficher Point : "+latitude+" / "+longitude);
		GeoPoint gp = new GeoPoint((int) latitude, (int) longitude);
		Drawable drawable = this.getResources().getDrawable(R.drawable.little_dwarf);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		DwarfOverlayItem overlayitem = new DwarfOverlayItem(gp, "Hola, Mundo!", "I'm in Mexico City!",id);
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}

	public void buildAlertMessageNoGps(){
			//Boite de dialogie pour activer le GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Votre GPS n'est pas activé, voulez-vous l'activer ?")
			.setCancelable(false)
			.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				}
			})
			.setNegativeButton("Non", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
