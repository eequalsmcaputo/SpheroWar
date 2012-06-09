package com.eequals.spherowar.model;

import com.eequals.spherowar.controller.War;

import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.ConfigureCollisionDetectionCommand;
import orbotix.robot.base.DeviceAsyncData;
import orbotix.robot.base.DeviceMessenger;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
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
	
	public SpheroTank (War war, String robot_id, String player_name) {
		_war = war;
		_id = robot_id;
		_name = player_name;
		initSphero();
		initTank();
	}
	
	private void initSphero()
	{
	
		if (_id != null && !_id.equals("")) {
			_Robot = RobotProvider.getDefaultProvider().findRobot(_id);
		}

		// Start streaming collision detection data
		//// First register a listener to process the data
		DeviceMessenger.getInstance().addAsyncDataListener(_Robot, mCollisionListener);

		//// Now send a command to enable streaming collisions
		ConfigureCollisionDetectionCommand.sendCommand(_Robot,
				ConfigureCollisionDetectionCommand.DEFAULT_DETECTION_METHOD,
				45, 45, 100, 100, 100);
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
	
	public void Hit(int hp)
	{
		_lasthit = hp;
		if (hp < 0)
		{
			_hp_current += hp;
			_losses += 1;
		} else {
			if (hp > 0) {
				_wins += 1;
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
			if (asyncData instanceof CollisionDetectedAsyncData) {
				_lastimp =	(CollisionDetectedAsyncData) asyncData;
				_war.registerImpact(SpheroTank.this);
			}
		}
		
	};


}
