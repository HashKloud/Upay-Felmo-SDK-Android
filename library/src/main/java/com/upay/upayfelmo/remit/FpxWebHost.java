package com.upay.upayfelmo.remit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class FpxWebHost
{
	/**
	 * Represents listener.
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
	}


	private Activity activity;
	private WebView parentWebView;
	private RemitWebHost.Listener parentListener;
	private WebView fpxWebView;
	private Listener fpxListener;
	private String redirectUrl;
	private String finalUrl;

	/**
	 * Initialize a new instance of the class.
	 * @param parentWebView Parent web view.
	 * @param listener Parent listener.
	 */
	/*package*/ FpxWebHost(WebView parentWebView, RemitWebHost.Listener listener)
	{
		this.parentWebView = parentWebView;
		this.parentListener = listener;
	}

	/**
	 * Attach the given instances.
	 * @param activity Parent activity.
	 * @param webView Web view instance.
	 * @param listener Listener.
	 */
	public void attach(Activity activity, WebView webView, Listener listener)
	{
		this.activity = activity;
		this.fpxWebView = webView;
		this.fpxListener = listener;
	}

	/**
	 * Detach all instances.
	 */
	public void detach()
	{
		this.fpxWebView.stopLoading();
		this.fpxWebView.setWebViewClient(null);
		this.fpxWebView = null;
		this.fpxListener = null;
		this.activity = null;
	}

	/**
	 * Start FPX web view.
	 */
	public void start()
	{
		setWebViewSettings();
		this.fpxWebView.loadUrl(redirectUrl);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewSettings()
	{
		if(fpxWebView == null)
		{
			return;
		}

		WebSettings settings = fpxWebView.getSettings();
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		activity.deleteDatabase("webview.db");
		activity.deleteDatabase("webviewCache.db");

		fpxWebView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				if(fpxListener != null)
				{
					fpxListener.onPageStarted();
				}

				validateFpxTransaction(url, finalUrl);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				if(fpxListener != null)
				{
					fpxListener.onPageFinished();
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				if(fpxListener != null)
				{
					fpxListener.onPageError(errorCode, description);
				}
			}

			@TargetApi(android.os.Build.VERSION_CODES.M)
			@Override
			public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr)
			{
				// Redirect to deprecated method, so you can use it in all SDK versions
				onReceivedError(view, rerr.getErrorCode(),
					rerr.getDescription().toString(), req.getUrl().toString());
			}
		});
	}

	/**
	 * Open FPX view.
	 * @param redirectUrl Redirect URL.
	 * @param finalUrl Final URL.
	 */
	/*package*/ void openFpxView(String redirectUrl, String finalUrl)
	{
		this.redirectUrl = redirectUrl;
		this.finalUrl = finalUrl;

		if(parentListener != null)
		{
			parentListener.onOpenFpxView(this);
		}
	}

	/**
	 * Close FPX view.
	 */
	/*package*/ void closeFpxView()
	{
		if(parentListener != null)
		{
			parentListener.onCloseFpxView();
		}
	}

	/**
	 * Validate FPX transaction with final URL and current URL.
	 * @param url Current URL.
	 * @param finalURL Final URL.
	 */
	private void validateFpxTransaction(String url, String finalURL)
	{
		if(!url.contains(finalURL))
		{
			return;
		}

		try
		{
			URL fpxURL = new URL(url);
			HashMap<String, String> fpxTransactionData = Utils.splitQuery(fpxURL);

			// Make a decision based on the paid status of FPX
			if(Constants.TRUE.equalsIgnoreCase(fpxTransactionData.get(Constants.KEY_BILLPLZ_PAID)))
			{
				injectDeferredObject("fireFpxSuccess(\"" + url + "\")", null);
			}
			else
			{
				injectDeferredObject("fireFpxError(\"" + url + "\")", null);
			}
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		closeFpxView();
	}

	@SuppressWarnings("SameParameterValue")
	private void injectDeferredObject(String source, String jsWrapper)
	{
		if(parentWebView != null)
		{
			Log.d("INJECT_JS_CODE", "Can't inject code into the system browser");
			return;
		}

		String scriptToInject;

		if(jsWrapper != null)
		{
			org.json.JSONArray jsonEsc = new org.json.JSONArray();
			jsonEsc.put(source);
			String jsonRepr = jsonEsc.toString();
			String jsonSourceString = jsonRepr.substring(1, jsonRepr.length() - 1);
			scriptToInject = String.format(jsWrapper, jsonSourceString);
		}
		else
		{
			scriptToInject = source;
		}

		final String finalScriptToInject = scriptToInject;

		activity.runOnUiThread(new Runnable()
		{
			@SuppressLint("NewApi")
			@Override
			public void run()
			{
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
				{
					// This action will have the side-effect of blurring the currently focused element
					parentWebView.loadUrl("javascript:" + finalScriptToInject);
				}
				else
				{
					parentWebView.evaluateJavascript(finalScriptToInject, null);
				}
			}
		});
	}
}
