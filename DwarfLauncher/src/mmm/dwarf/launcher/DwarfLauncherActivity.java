package mmm.dwarf.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.maps.MapActivity;

public class DwarfLauncherActivity extends MapActivity{


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);	
	}

	public void jouer(View MenuView) {

		//Lancer le nain
		Intent intent=new Intent(DwarfLauncherActivity.this, LaunchPlateformActivity.class);
		startActivity(intent);

	}

	public void afficherMap(View MenuView) {
		//Lancement de la google maps
		Intent gmaps = new Intent(DwarfLauncherActivity.this, GoogleMapsActivity.class);
		startActivity(gmaps);

	}

}
