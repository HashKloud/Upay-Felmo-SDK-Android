package com.upay.upayfelmoremit.main;

import android.os.Bundle;

import com.upay.upayfelmoremit.R;
import com.upay.upayfelmoremit.common.BaseActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		if(onFragmentSupportNavigateUp())
		{
			return true;
		}

		return NavigationUI.navigateUp(
			Navigation.findNavController(this,
				R.id.main_nav_host_fragment), (DrawerLayout)null);
	}
}
