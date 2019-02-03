package com.upay.upay_felmo_lib.retrofit;

import com.upay.upay_felmo_lib.model.LoginResponse;


import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/v3/third_party/process_login.json")
    Call<LoginResponse> authUser(@Header("client") String client , @Header("Access-Country") String country, @Header("signature") String signature);
}
