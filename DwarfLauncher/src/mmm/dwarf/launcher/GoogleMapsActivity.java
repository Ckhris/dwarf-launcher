package mmm.dwarf.launcher;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends MapActivity {

	private LocationManager manager;
	private List<Overlay> mapOverlays; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.main);  
		
		final MapView mapView = (MapView) findViewById(R.id.mapview);
		mapOverlays = mapView.getOverlays();

		mapView.setBuiltInZoomControls(true);

		Log.v("Chris", "IN");

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		//	Log.v("Chris", "Enabled: "+!locationManager.isProviderEnabled(LOCATION_SERVICE));
		//	if(!locationManager.isProviderEnabled(LOCATION_SERVICE)){
		/* est vrai meme quand le gps est activ� */
		//		Log.v("Chris", "Dans le IF");
		//		buildAlertMessageNoGps();
		//	}
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				Log.v("Chris", "onLocationChanged");
				afficherPoint(location.getLatitude(), location.getLongitude());
				GeoPoint g=new GeoPoint((int) (location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
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
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
		catch(Exception e){
			
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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


	private void afficherToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void buildAlertMessageNoGps() {
		Log.v("Chris", "In buildAlertMessageNoGps");
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Votre GPS semble d�sactiver, Voulez-vous l'activer?")
		.setCancelable(false)
		.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		})
		.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
