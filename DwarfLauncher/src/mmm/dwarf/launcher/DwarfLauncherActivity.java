package mmm.dwarf.launcher;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class DwarfLauncherActivity extends Activity{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);	
		mp=MediaPlayer.create(getBaseContext(), R.raw.intro);
		mp.setLooping(true);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		mp.stop();
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
		mp.stop();
		startActivity(gmaps);

	}
	
	public void stopSound(View menuView){
		if(this.mp.isPlaying()){
			this.mp.pause();
			ImageButton button = (ImageButton) findViewById(R.id.stopSound);
			button.setImageResource(R.drawable.mug2);
		}
		else{
			this.mp.start();
			ImageButton button = (ImageButton) findViewById(R.id.stopSound);
			button.setImageResource(R.drawable.mug1);
		}
	}
	
	public void onPause(){
		super.onPause();
		this.mp.pause();
	}
	
	public void onResume(){
		super.onResume();
		try {
			this.mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mp.start();
	}
	
	@Override
	public void onBackPressed() {
	   moveTaskToBack(true);
	   return;
	}

}
