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
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends MapActivity {

	private LocationListener locationListener;
	private LocationManager locationManager;
	private List<Overlay> mapOverlays; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.main);  
		final MapView mapView = (MapView) findViewById(R.id.mapview);
		mapOverlays = mapView.getOverlays();

		mapView.setBuiltInZoomControls(true);

		//Activation du wifi -> A tester
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
		
		
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				Log.v("Chris", "onLocationChanged");
				afficherPoint(location.getLatitude(), location.getLongitude());
				GeoPoint g=new GeoPoint((int) (location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
				mapView.getController().setZoom(16);
				mapView.getController().animateTo(g);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.v("Chris", "onStatusChanged");
			}

			public void onProviderEnabled(String provider) {
				Log.v("Chris", "onProviderEnabled");
			}

			public void onProviderDisabled(String provider) {
				Log.v("Chris", "onProviderDisabled");
			}
		};

		// Register the listener with the Location Manager to receive location updates
		
		try{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
		catch(Exception e){
			
		}
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Log.v("Chris", "bp: "+bestProvider);
		
		SharedPreferences settings= getSharedPreferences("lance", 4);
		if(settings.getInt("lanceX", 0)!=0){
			int lanceX= settings.getInt("lanceX", 0);
			int lanceY= settings.getInt("lanceY", 0);
			mapView.getController().setZoom(21);
			GeoPoint geo=mapView.getProjection().fromPixels(lanceX,lanceY);
			Drawable drawable = this.getResources().getDrawable(R.drawable.little_dwarf);
			HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
			OverlayItem overlayitem = new OverlayItem(geo, "Hola, Mundo!", "I'm in Mexico City!");
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
			mapView.getController().setZoom(16);
			mapView.getController().animateTo(geo);
			settings.edit().clear();
		}
	}



	protected void afficherPoint(double latitude, double longitude) {
		latitude = latitude* 1E6;
		longitude = longitude* 1E6;
		Log.v("Chris", "Afficher Point : "+latitude+" / "+longitude);
		GeoPoint gp = new GeoPoint((int) latitude, (int) longitude);
		Drawable drawable = this.getResources().getDrawable(R.drawable.little_dwarf);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		OverlayItem overlayitem = new OverlayItem(gp, "Hola, Mundo!", "I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}

	public void onResume(){
		super.onResume();
		
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
		else{
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
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
