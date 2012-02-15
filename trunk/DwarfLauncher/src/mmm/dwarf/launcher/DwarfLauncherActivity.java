package mmm.dwarf.launcher;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
		mp=MediaPlayer.create(getBaseContext(), R.raw.intro);
		mp.start();
	}

	private MediaPlayer mp;
	
	public void jouer(View MenuView) {
		//Lancer le nain
		SharedPreferences settings = this.getSharedPreferences("lance", 4);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("idTouch", -1);
		editor.commit();
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
	
	public void stopSound(View menuView){
		if(this.mp.isPlaying()){
			this.mp.stop();
		}
		else{
			this.mp.start();
		}
	}
	
	

}
