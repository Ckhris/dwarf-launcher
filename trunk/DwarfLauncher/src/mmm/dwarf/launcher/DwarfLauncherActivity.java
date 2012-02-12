package mmm.dwarf.launcher;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class DwarfLauncherActivity extends Activity{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);	
		
	    //CrŽation d'une instance de ma classe LivresBDD
        DwarfsDataSource dwarfDS = new DwarfsDataSource(this);
        //CrŽation d'un livre
        Dwarf dwarf = new Dwarf(48.13002, -1.64911);
        //On ouvre la base de donnŽes pour Žcrire dedans
        dwarfDS.open();
        //On ins�re le livre que l'on vient de crŽer
        Long success = dwarfDS.createDwarf(dwarf);
        dwarfDS.close();
		
	}

	public void jouer(View MenuView) {
		//Lancer le nain
		Intent intent=new Intent(DwarfLauncherActivity.this, LaunchPlateformActivity.class);
		startActivity(intent);

	}
	public void detailsgame(View MenuView) {
		//On instancie notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.details, null);
 
        //Création de l'AlertDialog
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
 
        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);
 
        //On donne un titre à l'AlertDialog
        adb.setTitle("Détails de l'application");
        adb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				adb.setCancelable(isFinishing());
			}
		});
 
      adb.show();
		
	}
	public void afficherMap(View MenuView) {
		//Lancement de la google maps
		Intent gmaps = new Intent(DwarfLauncherActivity.this, GoogleMapsActivity.class);
		startActivity(gmaps);

	}
	
	

}
