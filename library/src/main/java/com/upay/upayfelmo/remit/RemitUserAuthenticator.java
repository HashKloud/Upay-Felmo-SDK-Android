package com.upay.upayfelmo.remit;

import com.upay.upayfelmo.api.ApiClient;
import com.upay.upayfelmo.api.ApiService;
import com.upay.upayfelmo.models.LoginResponse;
import com.upay.upayfelmo.models.UserAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents remit user authenticator.
 */
@SuppressWarnings({"NullableProblems", "unused"})
public class RemitUserAuthenticator
{
	private Listener listener;
	private ApiClient apiClient;

	/**
	 * Initialize a new instance of the class.
	 * @param listener Listener for receiving result.
	 * @param useSandboxMode Whether to use sandbox mode.
	 */
	public RemitUserAuthenticator(Listener listener, boolean useSandboxMode)
	{
		this.listener = listener;
		this.apiClient = new ApiClient(getBaseURL(useSandboxMode));
	}

	/**
	 * Dispose.
	 */
	public void dispose()
	{
		listener = null;
		apiClient = null;
	}

	/**
	 * Authenticate user.
	 * @param userAuth User information.
	 */
	public void authenticate(UserAuth userAuth)
	{
		if(listener != null)
		{
			listener.onLoginBegin();
		}

		ApiService apiService = apiClient.createService(ApiService.class);

		Call<LoginResponse> loginCall = apiService.authUser(
			userAuth.getClientKey(),
			userAuth.getCountryCode(),
			userAuth.getSignature());

		loginCall.enqueue(new Callback<LoginResponse>()
		{
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
			{
				if(listener != null)
				{
					listener.onLoginEnd();
					listener.onLoginSuccess(response.body());
				}
			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t)
			{
				if(listener != null)
				{
					listener.onLoginEnd();
					listener.onLoginError(t);
				}
			}
		});
	}

	private String getBaseURL(boolean useSandboxMode)
	{
		return useSandboxMode ? Constants.UAT_URL : Constants.PRODUCTION_URL;
	}


	/**
	 * Represents listener.
	 */
	public interface Listener
	{
		/**
		 * Called when login process begins.
		 */
		void onLoginBegin();

		/**
		 * Called when login process ends.
		 */
		void onLoginEnd();

		/**
		 * Called when login error occurs.
		 * @param throwable Cause of the error.
		 */
		void onLoginError(Throwable throwable);

		/**
		 * Called when login is successful.
		 * @param response Login response.
		 */
		void onLoginSuccess(LoginResponse response);
	}
}
