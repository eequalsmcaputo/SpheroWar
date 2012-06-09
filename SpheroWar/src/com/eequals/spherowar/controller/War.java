package com.eequals.spherowar.controller;


import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import orbotix.multiplayer.LocalMultiplayerClient;
import orbotix.multiplayer.LocalMultiplayerClient.OnGameDataReceivedListener;
import orbotix.multiplayer.RemotePlayer;
import orbotix.robot.base.CollisionDetectedAsyncData;

import android.content.Context;

import com.eequals.spherowar.model.Impact.ImpactResult;
import com.eequals.spherowar.model.SpheroTank;
import com.eequals.spherowar.model.Impact;
import com.eequals.spherowar.Util;

public class War {

	private Impact _lastimpact;
	private Context _context;
	private LocalMultiplayerClient _mp;
	private SpheroTank _st;
	
	public static final double FRONT_LEFT = 45.0d;
	public static final double FRONT_RIGHT = 45.0d;
	public static final int IMPACT_THRESHOLD = 100;
	
	public War(Context context, String robot_id, String player_name) {
		_context = context;
		_mp = new LocalMultiplayerClient(_context);
		_mp.open();
		_mp.setOnGameDataReceivedListener(_GameDataListener);
		
		_st = new SpheroTank(this, robot_id, player_name);
		
	}
	
	private OnGameDataReceivedListener _GameDataListener = new OnGameDataReceivedListener () {

		@Override
		public void onGameDataReceived(Context context, JSONObject data,
				RemotePlayer other) {
			// TODO Auto-generated method stub
			
			String imptype;
			try {
				imptype = data.getString(Util.IMPTYPE);
				
				if (imptype == Util.IMPTYPE_IMPACT)
				{
					registerOtherImpact(data);
				} else if (imptype == Util.IMPTYPE_RESULT)
				{
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	};

	public void registerImpact(SpheroTank st)
	{
		boolean isImpact = false;
		
		if (_lastimpact == null)
		{
			isImpact = true;
		} else {
			if (War.isOldImpact(_lastimpact)) {
				isImpact = true;
			}
		}
		
		if (isImpact)
		{
			_lastimpact = new Impact(st);
			sendImpact();
		}

	}
	
	private void sendImpact()
	{
		JSONObject impact = Util.getImpactJSON(_lastimpact.getST().getLastImpactData(), 
				_lastimpact.getST().getId());
		_mp.sendGameDataToAll(impact);
	}
	
	public void registerOtherImpact(JSONObject other)
	{
		String otherid;
		try {
			otherid = other.getString(Util.IMP_ID);
			if (otherid != _st.getId())
			{
				if (_lastimpact.isImpact(other))
				{
					
										
					resetImpact();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendResult(JSONObject result)
	{
		
	}
	
	private void resolveImpactResult(JSONObject result)
	{
		
		
		resetImpact();
	}
	
	private void resetImpact()
	{
		_lastimpact = null;
	}
	
	private void recordImpactResult(ImpactResult result)
	{
		
	}
	
	public static boolean isInFront(double loc)
	{
		return ((-45.0d < loc) && (loc < 45.0d));
	}
	
	public static boolean isValidImpact(long impactTime1, long impactTime2)
	{
		return (Math.abs(impactTime2 - impactTime1) < IMPACT_THRESHOLD);
	}
	
	public static boolean isOldImpact(Impact impact) 
	{
		return (Math.abs(System.currentTimeMillis() - impact.getImpactTimestamp()) > 
			IMPACT_THRESHOLD);
	}
}
