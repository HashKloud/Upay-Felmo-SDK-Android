package com.upay.upayfelmoremit.common;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Represents base dialog fragment.
 */
@SuppressWarnings("unused")
public class BaseDialogFragment extends DialogFragment
{
	/**
	 * Dismiss the fragment and its dialog.  If the fragment was added to the
	 * back stack, all back stack state up to and including this entry will
	 * be popped.  Otherwise, a new transaction will be committed to remove
	 * the fragment.
	 */
	@Override
	public void dismiss()
	{
		// NOTE: Sometimes, it gets null for unknown reason and creates crash.
		if(getFragmentManager() == null)
		{
			return;
		}

		super.dismiss();
	}

	/**
	 * Version of {@link #dismiss()} that uses
	 * {@link FragmentTransaction#commitAllowingStateLoss()
	 * FragmentTransaction.commitAllowingStateLoss()}. See linked
	 * documentation for further details.
	 */
	@Override
	public void dismissAllowingStateLoss()
	{
		// NOTE: Sometimes, it gets null for unknown reason and creates crash.
		if(getFragmentManager() == null)
		{
			return;
		}

		super.dismissAllowingStateLoss();
	}
}
