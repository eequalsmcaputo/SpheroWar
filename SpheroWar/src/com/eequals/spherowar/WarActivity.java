package com.eequals.spherowar;

import com.eequals.spherowar.controller.War;

import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.widgets.ControllerActivity;
import orbotix.robot.widgets.calibration.CalibrationView;
import orbotix.robot.widgets.joystick.JoystickView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WarActivity extends ControllerActivity {
	
	private static final int STARTUP_ACTIVITY_RESULTS = 0;
	public static final String MODE = "mode";
	public static final String MODE_SINGLE = "single";
	public static final String MODE_MULTI = "multi";
	private War _war;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.war_activity);
		
        //Add the JoystickView as a Controller
        addController((JoystickView)findViewById(R.id.joystick));

        //Add the CalibrationView as a Controller
        addController((CalibrationView)findViewById(R.id.calibration));
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == STARTUP_ACTIVITY_RESULTS
				&& resultCode == Activity.RESULT_OK) {
			// Get a reference to the connected Sphero
			final String robot_id = data.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
			final Robot robot;
			
			if (robot_id != null && !robot_id.equals("")) {
				robot = RobotProvider.getDefaultProvider().findRobot(robot_id);
				setRobot(robot);
				_war = new War(this, robot, robot.getBluetoothName());
				
				
				
				Intent i = getIntent();
				
				if (i.getStringExtra(MODE) == MODE_SINGLE)
				{
					
				}
			}
			

		}
	}

}
