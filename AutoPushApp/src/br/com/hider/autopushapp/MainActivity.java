package br.com.hider.autopushapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener, OnClickListener {

	private static final String TAG = "br.com.hider.autopushapp.INFO";
	
	private SensorManager mSensorManager;
	
	private Sensor mProximity;
	private Sensor mLight;
	private Sensor mGravity;
	
	private int counter;

	private TextView out;
	private TextView info;
	private Button bResetCounter;
	private Button bPlus;
	private Button bMinus;

	@Override
	protected void onResume() 
	{
		
		super.onResume();
		
		mSensorManager.registerListener((SensorEventListener) this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener((SensorEventListener) this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener((SensorEventListener) this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		mSensorManager.unregisterListener((SensorEventListener) this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_main);
		
		
		this.out = (TextView)this.findViewById(R.id.mainactivity_text_out);
		this.info = (TextView)this.findViewById(R.id.info);
		
		
		this.bResetCounter = (Button)this.findViewById(R.id.reset);
		this.bPlus = (Button) this.findViewById(R.id.plus);
		this.bMinus = (Button) this.findViewById(R.id.minus);
		
		
		this.bResetCounter.setOnClickListener(this);
		this.bPlus.setOnClickListener(this);
		this.bMinus.setOnClickListener(this);
		
		
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		
		
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		updateCounter();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		this.getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		//Fazer nada.
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if(Sensor.TYPE_PROXIMITY == event.sensor.getType()){
			
			Log.i(TAG, event.sensor.getName()+" - "+event.values[0]);
			
			float value = event.values[0];
			
			if(value == 0){
				
				counter++;
				
				updateCounter();
			}
			
		}
		
		if(Sensor.TYPE_LIGHT == event.sensor.getType()){
			
			Log.i(TAG, event.sensor.getName()+" - "+event.values[0]);
			
			float value = event.values[0];
			
			info.setText("Lum: "+value);
			
		}	

	}

	private void updateCounter() {
		
		String text= ""+counter+"";
		
		out.setText(text);
		
	}

	@Override
	public void onClick(View arg0) {
		
		if(arg0.getId() == R.id.plus){
			this.counter++;
		}
		
		if(arg0.getId() == R.id.minus){
			if(this.counter > 0){
				this.counter--;
			}
		}
		
		if(arg0.getId() == R.id.reset){
			this.counter = 0;
		}
		
		updateCounter();
	}

}
