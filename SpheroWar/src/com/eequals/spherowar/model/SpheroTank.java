package com.eequals.spherowar.model;

import com.eequals.spherowar.Util;
import com.eequals.spherowar.controller.War;

import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.ConfigureCollisionDetectionCommand;
import orbotix.robot.base.DeviceAsyncData;
import orbotix.robot.base.DeviceMessenger;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.DeviceMessenger.AsyncDataListener;

public class SpheroTank {

	private String _id;
	private String _name;
	private War _war;
	private Robot _Robot;
	
	private final int _hp_start = 100;
	private int _hp_current;
	private int _lasthit = 0;
	private int _losses = 0;
	private int _wins = 0;
	
	CollisionDetectedAsyncData _lastimp;
	
	public SpheroTank (War war, Robot robot, String player_name) {
		_war = war;
		_id = robot.getUniqueId();
		_name = player_name;
		_Robot = robot;
		initSphero();
		initTank();
	}
	
	private void initSphero()
	{

		// Start streaming collision detection data
		//// First register a listener to process the data
		DeviceMessenger.getInstance().addAsyncDataListener(_Robot, mCollisionListener);

		//// Now send a command to enable streaming collisions
		ConfigureCollisionDetectionCommand.sendCommand(_Robot,
				ConfigureCollisionDetectionCommand.DEFAULT_DETECTION_METHOD,
				30, 30, 95, 95, 100);
		
		//Util.flashColor(_Robot, 254, 0, 0, 300, 5);
	}
	
	private void initTank()
	{
		resetHP();
	}
	
	public int getHPCurrent()
	{
		return _hp_current;
	}
	
	public int getWins()
	{
		return _wins;
	}
	
	public int getLosses()
	{
		return _losses;
	}
	
	public void resetHP()
	{
		_hp_current = _hp_start;
	}
	
	public String getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void hit(int hp)
	{
		_lasthit = hp;
		if (hp < 0)
		{
			_hp_current += hp;
			_losses += 1;
			Util.flashColor(_Robot, 254, 0, 0, 300, 5);
		} else {
			if (hp > 0) {
				_wins += 1;
				Util.flashColor(_Robot, 0, 254, 0, 300, 5);
			}
		}
	}
	
	public int getLastHit()
	{
		return _lasthit;
	}
	
	public CollisionDetectedAsyncData getLastImpactData()
	{
		return _lastimp;
	}
	
	private final AsyncDataListener mCollisionListener = new AsyncDataListener() {

		public void onDataReceived(DeviceAsyncData asyncData) {
			if (asyncData instanceof CollisionDetectedAsyncData)
			{
				
				/*
				try {
					RGBLEDOutputCommand.sendCommand(_Robot, 254, 0, 0);
					Thread.sleep(300);
					RGBLEDOutputCommand.sendCommand(_Robot, 0, 0, 0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				_lastimp =	(CollisionDetectedAsyncData) asyncData;
				_war.registerImpact(SpheroTank.this);
			}
		}
		
	};

	public void cleanupRobot()
	{
		// Assume that collision detection is configured and disable it.
		ConfigureCollisionDetectionCommand.sendCommand(_Robot, 
				ConfigureCollisionDetectionCommand.DISABLE_DETECTION_METHOD, 0, 0, 0, 0, 0);
		
		// Remove async data listener
		DeviceMessenger.getInstance().removeAsyncDataListener(_Robot, mCollisionListener);
		
		// Disconnect from the robot.
		RobotProvider.getDefaultProvider().removeAllControls();
	}

}
