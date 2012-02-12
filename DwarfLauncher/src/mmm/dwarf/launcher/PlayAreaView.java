package mmm.dwarf.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

public class PlayAreaView extends View {
	private Matrix translate;  
	public Bitmap nain;
	private GestureDetector gestures;
	private DwarfsDataSource dataSource;
	public float velocityX;
	public float velocityY;
	protected void onDraw(Canvas canvas) {  
		//Pour la boussole utilisation d'un deuxième canvas pour ne pas interférer avec le premier
		//centre de la vue
		int centerX = getMeasuredWidth()/2;
		int centerY = getMeasuredHeight()/2;

		//diamètre du cercle
		int diam = (int) Math.min(centerX/1.5, centerY/1.5);

		//dessin du cercle
		canvas.drawCircle(centerX, centerY, diam, circlePaint);

		//sauvegarde du canvas
		canvas.save();

		//rotation du canvas pour qu'il pointe vers le nord
		canvas.rotate(-northOrientation, centerX, centerY);

		//aiguilles boussole
		trianglePath.reset();
		trianglePath.moveTo(centerX, centerY-diam);
		trianglePath.lineTo(centerX-10, centerY);
		trianglePath.lineTo(centerX+10, centerY);
		//aiguille nord
		canvas.drawPath(trianglePath, northPaint);
		//aiguille sud
		canvas.rotate(180, centerX, centerY);
		canvas.drawPath(trianglePath, southPaint);

		//restauration du canvas
		canvas.restore();

		//Dessin du nain
		canvas.drawBitmap(nain, translate, null);  
		Matrix m = canvas.getMatrix();  
		//Log.d("TG", "Matrix: "+translate.toShortString());  
		//Log.d("TG", "Canvas: "+m.toShortString());  
	}

	public PlayAreaView(Context context) {
	    super(context);
	    dataSource= new DwarfsDataSource(context);
	    translate = new Matrix();  
	    gestures = new GestureDetector(context, new GestureListener(this));  
	    nain = BitmapFactory.decodeResource(getResources(), R.drawable.little_dwarf);  
	    //Nécessaire à la boussole
	    initView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestures.onTouchEvent(event);
	}

	public void onMove(float dx, float dy) {  
		translate.postTranslate(dx, dy);  
		invalidate();  
	}

	public void onResetLocation() {  
		translate.reset();  
		invalidate();  
	}

	private Matrix animateStart;  
	private Interpolator animateInterpolator;  
	private long startTime;  
	private long endTime;  
	private float totalAnimDx;  
	private float totalAnimDy;  

	public void onAnimateMove(float dx, float dy, long duration) {  
		animateStart = new Matrix(translate);  
		animateInterpolator = new OvershootInterpolator();  
		startTime = System.currentTimeMillis();  
		endTime = startTime + duration;  
		totalAnimDx = dx;  
		totalAnimDy = dy;  
		post(new Runnable() {  
			@Override  
			public void run() {  
				onAnimateStep();  
			}  
		});  
	}  

	private void onAnimateStep() {  
		long curTime = System.currentTimeMillis();  
		float percentTime = (float) (curTime - startTime)  
				/ (float) (endTime - startTime);  
		float percentDistance = animateInterpolator  
				.getInterpolation(percentTime);  
		float curDx = percentDistance * totalAnimDx;  
		float curDy = percentDistance * totalAnimDy;  
		translate.set(animateStart);  
		onMove(curDx, curDy);  

		Log.v("TG", "We're " + percentDistance + " of the way there!");  
		if (percentTime < 1.0f) {  
			post(new Runnable() {  
				@Override  
				public void run() {  
					onAnimateStep();  
				}  
			});  
		}else{
			SharedPreferences settings = this.getContext().getSharedPreferences("lance", 4);
			SharedPreferences.Editor editor = settings.edit();
			int idTouch=settings.getInt("idTouch", 0);
			calculateCoord(idTouch);
			Intent intent=new Intent(this.getContext(), GoogleMapsActivity.class);
			this.getContext().startActivity(intent);
		}
	}

	//Partie pour la boussole
	//Rotation vers la droite en degrée pour montrer le nord
	private float northOrientation=0;

	/**
	 * Retourne la rotation en degrée vers la droite pour montrer le nord
	 * @return
	 */
	public float getNorthOrientation(){
		return northOrientation;
	}

	/**
	 * setter de northOrientation
	 * @param n
	 */
	public void setNorthOrientation(float n){
		//si la direction a changé, mettre à jour
		if(n!=northOrientation){
			this.northOrientation=n;
			//Réinitialiser la vue
			this.invalidate();
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int measureWidth=measure(widthMeasureSpec);
		int measureHeight=measure(heightMeasureSpec);
		int d=Math.min(measureWidth, measureHeight);
		setMeasuredDimension(measureWidth, measureHeight);
	}

	private int measure(int measureSpec){
		int result=0;
		int specMode=MeasureSpec.getMode(measureSpec);
		int specSize=MeasureSpec.getSize(measureSpec);
		if(specMode==MeasureSpec.UNSPECIFIED){
			result=150;
		}
		else{
			result=specSize;
		}
		return specSize;
	}

	private Paint circlePaint;
	private Paint northPaint;
	private Paint southPaint;
	private Path trianglePath;

	private void initView(){
		Resources r=this.getResources();

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.compassCircle));

		northPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		northPaint.setColor(r.getColor(R.color.northPointer));

		southPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		southPaint.setColor(r.getColor(R.color.southPointer));

		trianglePath=new Path();
	}

	//Fonction de calcul des coordonnées

	private float distance=0;
	private float velocity=0;
	private float angle=0;

	public float getVelocity(){
		return velocity;
	}

	public void setVelocity(float velocity){
		this.velocity=velocity;
	}
	
	public float getAngle(){
		return angle;
	}

	public void setAngle(float angle){
		this.angle=angle;
	}
	
	public float getDistance(){
		return distance;
	}

	public void setDistance(float distance){
		this.distance=distance;
	}

	public void calculateDistance(){
		final float g = (float) 9.81;
		this.distance = (float) (((velocity*velocity)/g)*Math.sin(angle));
	}


	public void calculateCoord(double lat, double lon, int id){
		final float R = 6371;
		double latitude;
		double longitude;
		latitude=Math.asin(Math.sin(lat)*Math.cos((double) distance/R)+Math.cos(lat)*Math.sin((double)distance/R)*Math.cos(angle));
		longitude=lon+Math.atan2(Math.sin(angle)*Math.sin((double) distance/R)*Math.cos(lat), Math.cos((double) distance/R)-Math.sin(lat)*Math.sin(latitude));

	}
}
