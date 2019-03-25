package com.upay.upayfelmoremit.main;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.upay.upayfelmo.remit.FpxWebHost;
import com.upay.upayfelmoremit.R;
import com.upay.upayfelmoremit.common.BaseActivity;
import com.upay.upayfelmoremit.common.BaseDialogFragment;
import com.upay.upayfelmoremit.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class FpxWebHostDialogFragment extends BaseDialogFragment implements FpxWebHost.Listener
{
	private static final String FRAGMENT_TAG = "dialog_fragment_fpx";

	/**
	 * Show the fragment.
	 * @param fragment parent fragment.
	 * @param fpxWebHost FPX web host.
	 */
	@SuppressWarnings("ConstantConditions")
	public static FpxWebHostDialogFragment show(BaseFragment fragment, FpxWebHost fpxWebHost)
	{
		FragmentManager fMgr = fragment.getFragmentManager();
		FragmentTransaction ft = fMgr.beginTransaction();
		Fragment prev = fMgr.findFragmentByTag(FpxWebHostDialogFragment.FRAGMENT_TAG);

		if(prev != null)
		{
			ft.remove(prev);
		}

		ft.addToBackStack(null);

		FpxWebHostDialogFragment newFragment = new FpxWebHostDialogFragment();
		newFragment.webHost = fpxWebHost;
		newFragment.show(ft, FpxWebHostDialogFragment.FRAGMENT_TAG);
		return newFragment;
	}

	private WebView webView;
	private ProgressBar progressBar;
	private FpxWebHost webHost;

	public FpxWebHostDialogFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_fpx_web,
			container, false);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		webView = getView().findViewById(R.id.webView);
		progressBar = getView().findViewById(R.id.progressBar);

		webHost.attach(getActivity(), webView, this);
		webHost.start();
	}

	@Override
	public void onDestroyView()
	{
		webHost.detach();
		webHost = null;
		webView = null;
		progressBar = null;

		super.onDestroyView();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onStart()
	{
		super.onStart();
		Dialog dialog = getDialog();
		if(dialog != null)
		{
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setLayout(width, height);
		}
	}

	@Override
	public void onPageStarted()
	{
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageFinished()
	{
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onPageError(int errorCode, String description)
	{
		progressBar.setVisibility(View.GONE);
		dismiss();
		Toast.makeText(getContext(), errorCode + ": " + description, Toast.LENGTH_LONG).show();
	}
}
