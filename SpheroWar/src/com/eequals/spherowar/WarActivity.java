package com.eequals.spherowar;

import com.eequals.spherowar.controller.War;

import orbotix.robot.widgets.ControllerActivity;
import orbotix.robot.widgets.calibration.CalibrationView;
import orbotix.robot.widgets.joystick.JoystickView;


import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class WarActivity extends ControllerActivity {
	
	private static final int STARTUP_ACTIVITY_RESULTS = 0;
	public String currentMode = "";
	public static final String MODE = "mode";
	public static final String MODE_SINGLE = "single";
	public static final String MODE_MULTI = "multi";
	private War _war;
	private JoystickView mJoystick;
	private CalibrationView mCalibration;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.war_activity);
		
		mJoystick = (JoystickView)findViewById(R.id.joystick);
		mCalibration = (CalibrationView)findViewById(R.id.calibration);
		
        addController(mJoystick);
        addController(mCalibration);
        
        //Add the JoystickView as a Controller
		//mJoystick = (JoystickView)findViewById(R.id.joystick);
        //mCalibration = (CalibrationView)findViewById(R.id.calibration);
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = new Intent(this, StartupActivity.class);
		startActivityForResult(intent, STARTUP_ACTIVITY_RESULTS);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		_war.cleanup();
	}
	
	/*
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		
		mJoystick.interpretMotionEvent(ev);
		mCalibration.interpretMotionEvent(ev);
		
		return super.dispatchTouchEvent(ev);
	}
	*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == STARTUP_ACTIVITY_RESULTS
				&& resultCode == Activity.RESULT_OK) {
			// Get a reference to the connected Sphero
			
			try
			{
				final String robot_id = data.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
				final Robot robot;
				
				if (robot_id != null && !robot_id.equals("")) {
					robot = RobotProvider.getDefaultProvider().findRobot(robot_id);
					setRobot(robot);
					mJoystick.setSpeed(1);
					mJoystick.enable();
					
					/*
					mJoystick.setRobot(robot);
					mCalibration.setRobot(robot);
					*/
					
					Intent i = getIntent();
					currentMode = i.getStringExtra(MODE);
					
					_war = new War(this, robot, robot.getBluetoothName(), currentMode);
					
					if (currentMode == MODE_MULTI)
					{
						//TODO: call multiplayer lobby
					}
				}
			} catch (Exception e) 
			{
				Log.e("SW_STARTUP", e.getMessage());
			}

		}
	}

}
