package com.uf.dancemarathon;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity {

	private TouchImageView mMap;
	private ArrayList<ImageFrame> frames;
	private String ACTION_BAR_TITLE="Map";
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mMap = (TouchImageView) findViewById(R.id.map_image);
		
		initFrames();
		mMap.setOnTouchListener(new MyTouchListener());

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
		FontSetter.setFont(this, FontSetter.fontName.ALTB, customBar);
		
	}
	
	private void initFrames()
	{
		ImageFrame f1 = new ImageFrame(0.0, 0.0, 1.0, 0.5);
		
		frames = new ArrayList<ImageFrame>();
		
		frames.add(f1);
	}
	
	private void makeFrameToast()
	{
		Toast t = Toast.makeText(this, "IT WORKED!!", Toast.LENGTH_SHORT);
		t.show();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);

		return false; //return false to hide the menu
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void handleTouchesForFrames(float x, float y, float imgWidth, float imgHeight)
	{
		for(int i = 0; i < frames.size(); i++)
		{
			ImageFrame f = frames.get(i);
			
			//			if(f.isPointInFrame(x, y,imgWidth, imgHeight))
			//			makeFrameToast();
			
		}
	}
	
	private class MyTouchListener implements View.OnTouchListener{

		boolean inGesture = false;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			int ptrCount = event.getPointerCount();
			
			if(ptrCount > 1)
				inGesture = true;
			
			//If event is releasing finger off screen
			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				if(!inGesture)
				{
					float imgH = v.getHeight();
					float imgW = v.getWidth();
					float x = event.getX();
					float y = event.getY();
					
					handleTouchesForFrames(x, y, imgW, imgH);
				}
				//No longer in gesture if the only pointer left is the one which just got released
				else if(event.getPointerCount() == 1)
					inGesture = false;
			}
		
			return true;
		}
		
	}
	
	
}
