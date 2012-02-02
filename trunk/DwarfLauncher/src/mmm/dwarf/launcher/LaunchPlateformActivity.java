package mmm.dwarf.launcher;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;

public class LaunchPlateformActivity extends Activity {

	private PlayAreaView image;
	
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.launch_plateform);  

		FrameLayout frame = (FrameLayout) findViewById(R.id.graphics_holder);  
		image = new PlayAreaView(this);  
		frame.addView(image);  
		
		//Regarde les capteurs disponibles et choisi le premier
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0)
			sensor = sensors.get(0);
	}  
	
	
	//utilisation de la boussole numérique du téléphone
	private final SensorEventListener sensorListener=new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			image.setNorthOrientation(event.values[SensorManager.DATA_X]);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};
	private SensorManager sensorManager;
	private Sensor sensor;
	
	protected void onResume(){
		super.onResume();
		sensorManager.registerListener(sensorListener, sensor, sensorManager.SENSOR_DELAY_NORMAL);
	}
	
	protected void onStop(){
		super.onStop();
		sensorManager.unregisterListener(sensorListener);
	}
}
