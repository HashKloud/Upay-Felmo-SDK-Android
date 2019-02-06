package com.upay.upay_felmo_lib.builder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.piashsarker.www.easy_utils_lib.Utils;
import com.upay.upay_felmo_lib.Utils.Constant;
import com.upay.upay_felmo_lib.activity.UpayFelmoWebActivity;
import com.upay.upay_felmo_lib.listener.UpayFelmoListener;
import com.upay.upay_felmo_lib.model.LoginResponse;
import com.upay.upay_felmo_lib.model.UserAuth;
import com.upay.upay_felmo_lib.retrofit.ApiService;
import com.upay.upay_felmo_lib.retrofit.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpayFelmoWebBuilder {
    private static UpayFelmoListener listener;
    private Context context;
    private static String requestURL;

    /**
     * Constructor
     *
     * @param  context
     **/
    public UpayFelmoWebBuilder(Context context) {
        this.context = context;
    }


    private final  void authenticateUser(UserAuth userAuth, final UpayFelmoListener upayListener){

        if(Utils.isNetworkAvailable(context)){
            Utils.showProgressDialog(context);
            ApiService apiService = RetroClient.getApiService() ;
            Call<LoginResponse> call = apiService.authUser(userAuth.getClientKey(), userAuth.getCountryCode(), userAuth.getSingnature());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Utils.hideProgressDialog();
                    if(response.body() != null){
                        setUpayListener(upayListener);
                        startWebViewActivity(response.body());
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Utils.hideProgressDialog();
                    Log.d("Login",t.getMessage());
                    Toast.makeText(context, "Something went wrong, Try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(context, "Sorry,No internet connection.", Toast.LENGTH_SHORT).show();
        }


    }

    public void sendRemittance(UserAuth userAuth, UpayFelmoListener upayFelmoListener, boolean sandBoxMode){
        setRequestURL(sandBoxMode);
        authenticateUser(userAuth, upayFelmoListener);
    }

    private void startWebViewActivity(LoginResponse userInfo) {
        if (listener != null) {
            Intent intent = new Intent(context, UpayFelmoWebActivity.class);
            intent.putExtra("userInfo", userInfo);
            context.startActivity(intent);
        }
    }


    private void setUpayListener(UpayFelmoListener upayListener) {
        listener = upayListener;
    }

    public static UpayFelmoListener getUpayListener() {
        return listener;
    }

    private void setRequestURL(boolean sanboxMode) {
        if (sanboxMode) {
            requestURL = Constant.UAT_URL;
        } else {
            requestURL = Constant.PRODUCTION_URL;
        }
    }

    public static String getRequestURL() {
        return requestURL;
    }

}
