package com.eequals.spherowar;

import java.util.Calendar;
import java.util.Date;

import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.eequals.spherowar.model.Impact.ImpactResult;

public class Util {
	
	public static final String IMPTYPE = "impact_type";
	
	public static final String IMPTYPE_IMPACT = "impact";
	public static final String IMP_ID = "id";
	public static final String IMP_TIMESTAMP = "timestamp";
	public static final String IMP_ACCEL_X = "accel_x";
	public static final String IMP_ACCEL_Y = "accel_y";
	public static final String IMP_ACCEL_Z = "accel_z";
	public static final String IMP_SPEED = "speed";
	public static final String IMP_PWR_X = "pwr_x";
	public static final String IMP_PWR_Y = "pwr_y";

	public static final String IMPTYPE_RESULT = "result";
	public static final String IMPRES_ID1 = "id1";
	public static final String IMPRES_ID2 = "id2";
	public static final String IMPRES_TIMESTAMP = "timestamp";
	public static final String IMPRES_HIT1 = "hit1";
	public static final String IMPRES_HIT2 = "hit2";
	
	public static JSONObject getImpactJSON(CollisionDetectedAsyncData data, String id)
	{
		JSONObject impact = new JSONObject();
		
		try {
			impact.put(IMP_ID, id);
			impact.put(IMP_TIMESTAMP, data.getImpactTimeStamp());

			impact.put(IMP_ACCEL_X, data.getImpactAcceleration().x);
			impact.put(IMP_ACCEL_Y, data.getImpactAcceleration().y);
			impact.put(IMP_ACCEL_Z, data.getImpactAcceleration().z);
			
			impact.put(IMP_SPEED, data.getImpactSpeed());
			
			impact.put(IMP_PWR_X, data.getImpactPower().x);
			impact.put(IMP_PWR_Y, data.getImpactPower().y);
			
			return impact;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject getImpactResultJSON(ImpactResult result)
	{
		JSONObject resultj = new JSONObject();
		
		try {
			resultj.put(IMPRES_ID1, result.getID1());
			resultj.put(IMPRES_ID2, result.getID2());
			resultj.put(IMPRES_TIMESTAMP, result.getTimestamp());
			resultj.put(IMPRES_HIT1, result.getHit(result.getID1()));
			resultj.put(IMPRES_HIT2, result.getHit(result.getID2()));
			return resultj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void flashColor(Robot robot, int red, int green, int blue, 
			long durationMillis, int count)
	{
		ColorFlasher flasher = new ColorFlasher(robot, red, green, blue, durationMillis, count);
		flasher.execute();
	}
	
	
	public static long dateToLong(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTimeInMillis();
	}
	
	public static double getImpactLocation(double x, double y)
	{
		return Math.toDegrees(Math.atan2(y, x));
	}
	
	private static class ColorFlasher extends AsyncTask {
		
		private Robot _robot;
		private int _red;
		private int _green;
		private int _blue;
		private long _durationMillis;
		private int _count;
		
		public ColorFlasher(Robot robot, int red, int green, int blue, 
				long durationMillis, int count)
		{
			_robot = robot;
			_red = red;
			_green = green;
			_blue = blue;
			_durationMillis = durationMillis;
			_count = count;
		}
		
		@Override
		protected Object doInBackground(Object... params) {

			int i = 1;
			
			do
			{
				try {
					RGBLEDOutputCommand.sendCommand(_robot, _red, _green, _blue);
					Thread.sleep(_durationMillis);
					RGBLEDOutputCommand.sendCommand(_robot, 0, 0, 0);
					Thread.sleep(_durationMillis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				i++;
			} while(i <= _count);
			
			return null;
		}
		
	}
	
}
