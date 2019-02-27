package com.upay.upayfelmoremit.common;

import androidx.fragment.app.Fragment;

@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseFragment extends Fragment
{
	@Override
	public void onStart()
	{
		super.onStart();

		// Register for receiving back pressed event.
		if(getActivity() != null)
		{
			((BaseActivity) getActivity()).setCurrentFragment(this);
		}
	}

	@Override
	public void onStop()
	{
		// Unregister for receiving back pressed event.
		if(getActivity() != null)
		{
			((BaseActivity) getActivity()).clearCurrentFragment(this);
		}

		super.onStop();
	}

	public boolean onBackPressed()
	{
		// Default behavior is to be handled by the activity's onBackPressed() method
		return false;
	}

	public boolean onSupportNavigateUp()
	{
		// Default behavior is to be handled by the activity's onSupportNavigateUp() method
		return false;
	}

	public void onUserInteraction()
	{
	}


	/**
	 * Clear back stack.
	 * @param backStackStateName Back stack state name.
	 * @param inclusive Whether clear given state.
	 */
	public void clearBackStackImmediate(String backStackStateName, boolean inclusive)
	{
		if(getActivity() != null)
		{
			((BaseActivity) getActivity()).clearBackStackImmediate(backStackStateName, inclusive);
		}
	}

	/**
	 * Clear all back stack immediately.
	 */
	public void clearAllBackStackImmediate()
	{
		if(getActivity() != null)
		{
			((BaseActivity) getActivity()).clearAllBackStackImmediate();
		}
	}

	/**
	 * Clear all back stack.
	 */
	public void clearAllBackStack()
	{
		if(getActivity() != null)
		{
			((BaseActivity) getActivity()).clearAllBackStack();
		}
	}
}
