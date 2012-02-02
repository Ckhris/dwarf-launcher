package mmm.dwarf.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class DwarfLauncherActivity extends MapActivity{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Lancement de la google maps
		Intent gmaps = new Intent(DwarfLauncherActivity.this, GoogleMapsActivity.class);
		startActivity(gmaps);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}