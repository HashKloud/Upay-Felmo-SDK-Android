package com.upay.upay_felmo_lib.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.piashsarker.upay_felmo_lib.R;
import com.piashsarker.www.easy_utils_lib.Utils;
import com.upay.upay_felmo_lib.Utils.AppUtils;
import com.upay.upay_felmo_lib.Utils.Constant;
import com.upay.upay_felmo_lib.builder.UpayFelmoWebBuilder;
import com.upay.upay_felmo_lib.model.LoginResponse;

import java.net.MalformedURLException;
import java.net.URL;

import im.delight.android.webview.AdvancedWebView;


public class UpayFelmoWebActivity extends Activity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upay_felmo_web);
        if(getIntent().getSerializableExtra("userInfo") != null){
            LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("userInfo");
            openWebView(loginResponse);
        }



    }



    private void openWebView(LoginResponse loginResponse) {
        mWebView = findViewById(R.id.webview);
        mWebView.setListener(this, this);
        WebSettings settings = mWebView.getSettings();
        settings.setDatabaseEnabled(true);
        // Enable DOM storage
        settings.setDomStorageEnabled(true);
        if(loginResponse.getData().getReDirectUrl() != null && Utils.isValidURL(loginResponse.getData().getReDirectUrl())){
            String params = "signature="+loginResponse.getData().getSignature()+"&&"+"client="+loginResponse.getData().getClient();
            mWebView.loadUrl("https://59b5ec41.ngrok.io/#/my-remittance"+"?"+params);
            Log.d("Params", params);
        }else{
            Toast.makeText(this, "Something Went Wrong, Please try again later.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Utils.showProgressDialog(this);
        if (url.contains(Constant.SUCCESS_URL)) {
            if (UpayFelmoWebBuilder.getUpayListener() != null) {
                //  UpayFelmoWebBuilder.getUpayListener().onSuccess(processSuccessValues(url));
                try {
                    URL successURL = new URL(url);
                    UpayFelmoWebBuilder.getUpayListener().onSuccess(AppUtils.getResponse(successURL));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            Utils.hideProgressDialog();
            finish();
        }
        if (url.contains(Constant.CANCEL_URL)) {
            if (UpayFelmoWebBuilder.getUpayListener() != null) {
                try {
                    URL cancelURL = new URL(url);
                    UpayFelmoWebBuilder.getUpayListener().onFailure(AppUtils.splitQuery(cancelURL));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Utils.hideProgressDialog();
                finish();
            }
        }
    }

    @Override
    public void onPageFinished(String url) {
        Utils.hideProgressDialog();

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Utils.hideProgressDialog();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

}

