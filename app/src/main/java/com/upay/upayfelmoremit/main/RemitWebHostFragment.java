package com.upay.upayfelmoremit.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.upay.upayfelmo.models.LoginResponse;
import com.upay.upayfelmo.remit.Constants;
import com.upay.upayfelmo.remit.FpxWebHost;
import com.upay.upayfelmo.remit.RemitWebHost;
import com.upay.upayfelmoremit.R;
import com.upay.upayfelmoremit.common.BaseFragment;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import im.delight.android.webview.AdvancedWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemitWebHostFragment extends BaseFragment
	implements RemitWebHost.Listener
{
	private AdvancedWebView webView;
	private ProgressBar progressBar;
	private RemitWebHost webHost;
	private FpxWebHostDialogFragment fpxWebHostDialogFragment;

	public RemitWebHostFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_remit_web,
			container, false);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		LoginResponse response = (LoginResponse)RemitWebHostFragmentArgs.fromBundle(
			getArguments()).getUserInfo();

		webView = getView().findViewById(R.id.webView);
		progressBar = getView().findViewById(R.id.progressBar);

		webHost = new RemitWebHost();
		webHost.attach(getActivity(), webView, this);
		webHost.start(response, Constants.KEY_LANGUAGE_ENGLISH);
	}

	@Override
	public void onDestroyView()
	{
		webHost.detach();
		webHost = null;
		webView = null;
		progressBar = null;
		fpxWebHostDialogFragment = null;

		super.onDestroyView();
	}

	@Override
	public boolean onBackPressed()
	{
		return webHost.onBackPressed();
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
	public void onPageError(int errorCode, String text)
	{
		Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRemitSuccess(HashMap<String, String> values)
	{
		handleData("success", values);
	}

	@Override
	public void onRemitFailure(HashMap<String, String> values)
	{
		handleData("failure", values);
	}

	@Override
	public void onOpenFpxView(FpxWebHost fpxWebHost)
	{
		fpxWebHostDialogFragment = FpxWebHostDialogFragment.show(this, fpxWebHost);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onInviteFriends(String text, String url)
	{
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, url);
		getContext().startActivity(Intent.createChooser(shareIntent, text));
	}

	@Override
	public void onCloseFpxView()
	{
		if(fpxWebHostDialogFragment != null)
		{
			fpxWebHostDialogFragment.dismiss();
		}

		fpxWebHostDialogFragment = null;
	}

	@SuppressWarnings({"ConstantConditions", "unused"})
	private void handleData(String status, HashMap<String, String> values)
	{
		if(values == null)
		{
			return;
		}

		String message = "";

		for(String key : values.keySet())
		{
			String value = values.get(key);
			message = message.concat("KEY: " + key + " VALUE: " + value + " \n");
		}

		Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
	}
}
