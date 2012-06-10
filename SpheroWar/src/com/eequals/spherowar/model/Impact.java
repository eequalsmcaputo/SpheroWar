package com.eequals.spherowar.model;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.eequals.spherowar.Util;
import com.eequals.spherowar.controller.War;

import orbotix.robot.base.CollisionDetectedAsyncData.CollisionPower;
import orbotix.robot.sensor.Acceleration;

public class Impact {

	private SpheroTank _st;
	private long _impacttime;
	
	public Impact(SpheroTank st)
	{
		_st = st;
		_impacttime = st.getLastImpactData().getImpactTimeStamp();
	}
	
	public SpheroTank getST()
	{
		return _st;
	}
	
	public void hitST(ImpactResult result)
	{
		_st.hit(result.getHit(_st.getId()));
	}
	
	public long getImpactTimestamp()
	{
		return _impacttime;
	}
	
	public boolean isImpact(JSONObject other)
	{	
		try {
			long otherimpacttime = other.getLong(Util.IMP_TIMESTAMP);
			
			if (War.isValidImpact(_impacttime, otherimpacttime))
			{
				return true;
			} else {
				return false;
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public ImpactResult getImpactResult(JSONObject other)
	{
		
		try {
			Acceleration acc1 = _st.getLastImpactData().getImpactAcceleration();
			
			double x2 = other.getDouble(Util.IMP_ACCEL_X);
			double y2 = other.getDouble(Util.IMP_ACCEL_Y);
			
			double loc1 = Util.getImpactLocation(acc1.x, acc1.y);
			double loc2 = Util.getImpactLocation(x2, y2);
			
			ImpactResult result = 
					new ImpactResult(_st.getId(), _st.getLastImpactData().getImpactTimeStamp(),
					other.getString(Util.IMP_ID), other.getLong(Util.IMP_TIMESTAMP));
			
			
			
			return result;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static class ImpactResult {
		
		private HashMap<String, Integer> res = new HashMap<String, Integer>();
		private long _timestamp;
		
		public ImpactResult (String id1, long timestamp1, 
				String id2, long timestamp2) {
			
			res.put(id1, 0);
			res.put(id2, 0);
			
			_timestamp = (timestamp1 + timestamp2) / 2;
		}
		
		public ImpactResult(JSONObject result)
		{
			try {
				res.put(result.getString(Util.IMPRES_ID1), result.getInt(Util.IMPRES_HIT1));
				res.put(result.getString(Util.IMPRES_ID2), result.getInt(Util.IMPRES_HIT2));
				_timestamp = result.getLong(Util.IMPRES_TIMESTAMP);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getID1()
		{
			return (String) res.keySet().toArray()[0];
		}
		
		public String getID2()
		{
			return (String) res.keySet().toArray()[1];
		}
		
		public int getHit(String id)
		{
			return (int) res.get(id);
		}
		
		public void setHit(String id, int hit)
		{
			res.put(id, hit);
		}
		
		public long getTimestamp()
		{
			return _timestamp;
		}
		
	}
	
}
