package com.upay.upayfelmoremit.common;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Represents base class for activity.
 */
@SuppressLint("Registered")
@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity
{
	private BaseFragment currentFragment = null;

	@Override
	protected void onStop()
	{
		super.onStop();

        currentFragment = null;
	}

	/**
	 * Set the fragment for various event handling.
	 * @param fragment Reference of the fragment to be set.
	 */
	public void setCurrentFragment(BaseFragment fragment)
	{
		currentFragment = fragment;
	}

	/**
	 * Get the current attached fragment.
	 * @return Returns fragment if set; otherwise, returns null.
	 */
	public BaseFragment getCurrentFragment()
	{
		return currentFragment;
	}

	/**
	 * Reset current fragment reference if it is the given fragment.
	 * @param fragment Fragment to be cleared.
	 */
	public void clearCurrentFragment(BaseFragment fragment)
	{
		if(currentFragment != null && fragment == currentFragment)
		{
			currentFragment = null;
		}
	}

    @Override
    public void onBackPressed()
    {
        if(currentFragment == null || !currentFragment.onBackPressed())
        {
            super.onBackPressed();
        }
    }

    public boolean onFragmentSupportNavigateUp()
	{
		return currentFragment != null && currentFragment.onSupportNavigateUp();
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();

		if(currentFragment != null)
		{
			currentFragment.onUserInteraction();
		}
	}

    public void clearBackStackImmediate(String backStackStateName, boolean inclusive)
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(backStackStateName,
            ((inclusive) ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0));
    }

    public void clearAllBackStackImmediate()
    {
        FragmentManager fMgr = this.getSupportFragmentManager();
        int count = fMgr.getBackStackEntryCount();

        if(count > 0)
        {
            for(int i = 0; i < count; ++i)
            {
                fMgr.popBackStackImmediate();
            }
        }
    }

    public void clearAllBackStack()
    {
        FragmentManager fMgr = this.getSupportFragmentManager();
        int count = fMgr.getBackStackEntryCount();

        if(count > 0)
        {
            for(int i = 0; i < count; ++i)
            {
                fMgr.popBackStack();
            }
        }
    }

    public  void showMessage(CharSequence text)
	{
		// Get the root view set with setContentView() method
		View rootView = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);

		if(rootView != null)
		{
			Snackbar.make(rootView, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
		}
	}

	/**
	 * Whether to show or hide the app bar along with system status bar.
	 * @param show Whether to show or hide the app bar.
	 */
	public void showAppBar(boolean show)
	{
		//AndroidUtil.showSystemStatusBar(this, show);

		final ActionBar actionBar = this.getSupportActionBar();

		if(actionBar == null)
		{
			return;
		}

		if(show)
		{
			if(!actionBar.isShowing())
			{
				actionBar.show();
			}
		}
		else
		{
			if (actionBar.isShowing())
			{
				actionBar.hide();
			}
		}
	}
}
