package com.upay.upayfelmo.remit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.upay.upayfelmo.models.LoginResponse;

import java.util.HashMap;

import im.delight.android.webview.AdvancedWebView;

/**
 * Represents remit web hosting.
 */
@SuppressWarnings("unused")
public class RemitWebHost implements AdvancedWebView.Listener
{
	/**
	 * Represents parentListener.
	 */
	public interface Listener
	{
		/**
		 * Called when web page loading is started.
		 */
		void onPageStarted();

		/**
		 * Called when web page loading is finished.
		 */
		void onPageFinished();

		/**
		 * Called when error occurs during web page loading.
		 * @param errorCode Error code.
		 * @param text Error text.
		 */
		void onPageError(int errorCode, String text);

		/**
		 * Called when remit is successful.
		 * @param values Returned data.
		 */
		void onRemitSuccess(HashMap<String, String> values);

		/**
		 * Called when remit is failed.
		 * @param values Returned data.
		 */
		void onRemitFailure(HashMap<String, String> values);

		/**
		 * Called when FPX view need to be opened.
		 * @param fpxWebHost Web host that handles FPX interaction.
		 */
		void onOpenFpxView(FpxWebHost fpxWebHost);

		/**
		 * Called when FPX view need to be closed.
		 */
		void onCloseFpxView();

		/**
		 * Invite friends to share the app.
		 * @param text Text.
		 * @param url URL.
		 */
		void onInviteFriends(String text, String url);
	}


	private static final String MESSAGE_HANDLER = "message_handler";

	private Activity activity;
	private AdvancedWebView mainWebView;
	private Listener mainListener;
	private FpxWebHost fpxWebHost;

	/**
	 * Initialize a new instance of the class.
	 */
	public RemitWebHost()
	{
	}

	/**
	 * Attach the given instances.
	 * @param activity Parent activity.
	 * @param webView Web view instance.
	 * @param listener Listener.
	 */
	public void attach(Activity activity, AdvancedWebView webView, Listener listener)
	{
		this.activity = activity;
		this.mainWebView = webView;
		this.mainListener = listener;
		this.fpxWebHost = new FpxWebHost(this.mainWebView, this.mainListener);
	}

	/**
	 * Detach all instances.
	 */
	public void detach()
	{
		this.mainWebView.removeJavascriptInterface(MESSAGE_HANDLER);
		this.mainWebView.setListener((Activity)null, null);
		this.mainWebView = null;
		this.mainListener = null;
		this.activity = null;
		this.fpxWebHost = null;
	}

	/**
	 * Handle back button press.
	 * @return Returns true if handled.
	 */
	public boolean onBackPressed()
	{
		if(mainWebView != null && mainWebView.canGoBack() &&
		   !mainWebView.getUrl().contains(Constants.KEY_REMITTANCE_INVOICE) &&
		   !mainWebView.getUrl().contains(Constants.KEY_DASHBOARD_PAGE))
		{
			mainWebView.goBack();
			return true;
		}

		return false;
	}

	/**
	 * Start remit web view.
	 * @param response Login response.
	 * @param uiLanguageCode UI language code.
	 */
	public void start(LoginResponse response, String uiLanguageCode)
	{
		setWebViewSettings();
		mainWebView.setListener(activity, this);
		redirectToDashboard(response, uiLanguageCode);
	}

	@SuppressLint("AddJavascriptInterface")
	private void setWebViewSettings()
	{
		WebSettings settings = mainWebView.getSettings();
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportMultipleWindows(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		mainWebView.addJavascriptInterface(
			new WebViewJavaScriptInterface(activity, mainListener, fpxWebHost), MESSAGE_HANDLER);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			WebView.setWebContentsDebuggingEnabled(true);
		}
	}

	private void redirectToDashboard(LoginResponse response, String uiLanguageCode)
	{
		String reDirectUrlStr = response.getData().getReDirectUrl();

		if(reDirectUrlStr != null && Utils.isValidURL(reDirectUrlStr))
		{
			String finalUrlStr = String.format("%s?%s=%s&&%s=%s&&%s=%s",
				reDirectUrlStr,
				Constants.KEY_SIGNATURE,
				response.getData().getSignature(),
				Constants.KEY_CLIENT,
				response.getData().getClient(),
				Constants.KEY_LANGUAGE,
				uiLanguageCode);
			mainWebView.loadUrl(finalUrlStr);
		}
		else if(mainListener != null)
		{
			mainListener.onPageError(-1, "Something went wrong, Try again later.");
		}
	}


	//========================================================
	// AdvancedWebView.Listener implementation
	//========================================================

	@Override
	public void onPageStarted(String url, Bitmap favicon)
	{
		if(mainListener != null)
		{
			mainListener.onPageStarted();
		}
	}

	@Override
	public void onPageFinished(String url)
	{
		if(mainListener != null)
		{
			mainListener.onPageFinished();
		}
	}

	@Override
	public void onPageError(int errorCode, String description, String failingUrl)
	{
		if(mainListener != null)
		{
			mainListener.onPageError(errorCode, description);
		}
	}

	@Override
	public void onDownloadRequested(String url, String suggestedFilename,
									String mimeType, long contentLength,
									String contentDisposition, String userAgent)
	{
		// Do nothing
	}

	@Override
	public void onExternalPageRequest(String url)
	{
		// Do nothing
	}


	//========================================================
	// WebView JavaScript Interface
	//========================================================

	/**
	 * Represents WebView interface for JavaScript.
	 */
	private static class WebViewJavaScriptInterface
	{
		private Activity activity;
		private Listener parentListener;
		private FpxWebHost fpxWebHost;

		/**
		 * Initialize a new instance of the class.
		 * @param activity Parent activity.
		 * @param parentListener Parent listener.
		 * @param fpxWebHost FPX web host instance.
		 */
		WebViewJavaScriptInterface(Activity activity, Listener parentListener, FpxWebHost fpxWebHost)
		{
			this.activity = activity;
			this.parentListener = parentListener;
			this.fpxWebHost = fpxWebHost;
		}

		/*
		 * This method can be called from Android. @JavascriptInterface
		 * required after SDK version 17.
		 */

		/**
		 * When user selecting fpx in pwa, we will open a new webview
		 * @param redirectUrl Redirect URL.
		 * @param finalUrl Final URL.
		 */
		@JavascriptInterface
		public void openFpxWebView(final String redirectUrl, final String finalUrl)
		{
			if(fpxWebHost == null)
			{
				return;
			}

			activity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if(fpxWebHost != null)
					{
						fpxWebHost.openFpxView(redirectUrl, finalUrl);
					}
				}
			});
		}

		/**
		 * when a remittance transaction success in
		 * pwa this method will be triggered
		 * @param value Value to be received.
		 */
		@JavascriptInterface
		public void remittanceSuccess(String value)
		{
			if(parentListener == null)
			{
				return;
			}

			final HashMap<String, String> hashMap = Utils.getResponse(value);

			activity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if(parentListener != null)
					{
						parentListener.onRemitSuccess(hashMap);
					}
				}
			});
		}

		/**
		 * When a remittance transaction error in pwa this method will be triggered
		 * @param value Value to be received.
		 */
		@JavascriptInterface
		public void remittanceError(String value)
		{
			if(parentListener == null)
			{
				return;
			}

			final HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("error", value);

			activity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if(parentListener != null)
					{
						parentListener.onRemitFailure(hashMap);
					}
				}
			});
		}

		/**
		 * The event will be triggered for social sharing when
		 * selecting invite friends.
		 * @param text Text to be shown.
		 * @param url URL to be shown.
		 */
		@JavascriptInterface
		public void socialSharing(final String text, final String url)
		{
			if(parentListener == null)
			{
				return;
			}

			activity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if(parentListener != null)
					{
						parentListener.onInviteFriends(text, url);
					}
				}
			});
		}
	}
}
