package mmm.dwarf.launcher;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DwarfLauncherActivity extends Activity{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);	
		
	    //Création d'une instance de ma classe LivresBDD
        DwarfsDataSource dwarfDS = new DwarfsDataSource(this);
        //Création d'un livre
        Dwarf dwarf = new Dwarf(48.13002, -1.64911);
        //On ouvre la base de données pour écrire dedans
        dwarfDS.open();
        //On insère le livre que l'on vient de créer
        Long success = dwarfDS.createDwarf(dwarf);
        dwarfDS.close();
		
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
