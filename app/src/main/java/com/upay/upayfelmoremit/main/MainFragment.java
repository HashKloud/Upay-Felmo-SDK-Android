package com.upay.upayfelmoremit.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.upay.upayfelmo.models.LoginResponse;
import com.upay.upayfelmo.models.UserAuth;
import com.upay.upayfelmo.remit.RemitUserAuthenticator;
import com.upay.upayfelmoremit.R;
import com.upay.upayfelmoremit.common.BaseActivity;
import com.upay.upayfelmoremit.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

/**
 * A fragment with a Google +1 button.
 */
public class MainFragment extends BaseFragment
	implements View.OnClickListener,
			   RemitUserAuthenticator.Listener

{
	private Button launchRemitButton;
	private ProgressBar progressBar;
	private RemitUserAuthenticator userAuthenticator;

	public MainFragment()
	{
		// Required empty public constructor
	}


	@SuppressWarnings("UnnecessaryLocalVariable")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_main,
			container, false);
		return view;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		((BaseActivity)getActivity()).showAppBar(true);

		launchRemitButton = getView().findViewById(R.id.launchRemitButton);
		launchRemitButton.setOnClickListener(this);
		progressBar = getView().findViewById(R.id.progressBar);
	}

	@Override
	public void onDestroyView()
	{
		launchRemitButton = null;
		progressBar = null;
		userAuthenticator = null;

		super.onDestroyView();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onClick(View v)
	{
		int id = v.getId();

		switch(id)
		{
			case R.id.launchRemitButton:
			{
				userAuthenticator = new RemitUserAuthenticator(this, true);
				userAuthenticator.authenticate(new UserAuth("179M0iF1bkTWSO64DRT4EuVDPxnZdBbB9SRMWQAY1Qs=\\n",
					"172cf085e5d3894a", "MY"));
			}
			break;
		}
	}

	@Override
	public void onLoginBegin()
	{
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoginEnd()
	{
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onLoginError(Throwable throwable)
	{
		Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onLoginSuccess(LoginResponse response)
	{
		NavDirections action = MainFragmentDirections.actionMainToRemitWebHost(response);
		Navigation.findNavController(getView()).navigate(action);
		((BaseActivity)getActivity()).showAppBar(false);
	}
}
