package com.eequals.spherowar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpheroWar extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spherowar_activity);
	}
	
	public void onSingleClick(View v)
	{
		Intent i = new Intent(this, WarActivity.class);
		i.putExtra(WarActivity.MODE, WarActivity.MODE_SINGLE);
		this.startActivity(i);
	}
	
	public void onMultiClick(View v)
	{
		
	}
}
