package mmm.dwarf.launcher;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

public class GestureListener implements OnGestureListener, OnDoubleTapListener {

	PlayAreaView view;  
	public GestureListener(PlayAreaView view) {  
		this.view = view;  
	}

	@Override  
	public boolean onDoubleTap(MotionEvent e) {  
		//Log.v("TG", "onDoubleTap");  
		view.onResetLocation();  
		return true;  
	}  

	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override  
	public boolean onDown(MotionEvent e) {  
		//Log.v("TG", "onDown");
		view.onResetLocation();
		view.onMove(e.getX()-(view.nain.getWidth()/2), e.getY()-(view.nain.getHeight()/2));
		return true;  
	}
	@Override  
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, final float velocityY) {  
		//Log.v("TG", "onFling");
		velocityX=0;

		if(velocityY<0){
			view.velocityY=velocityY;
			final float distanceTimeFactor = 0.4f;  
			final float totalDx = (distanceTimeFactor * velocityX/2);  
			final float totalDy = (distanceTimeFactor * velocityY/2);
			view.setVelocity(velocityY);
			view.calculateDistance();
			view.onAnimateMove(totalDx, totalDy,  
					(long) (1000 * distanceTimeFactor));
		}
		else{
			Toast t = Toast.makeText(view.getContext(), "Hey ! Tu ne peux lancer le nain dans ce sens. Tourne toi !", 5);
			MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.too);
			mediaPlayer.start();
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override  
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
		//Log.v("TG", "onScroll");  

		view.onMove(-distanceX, -distanceY);  
		return true;  
	}  

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
