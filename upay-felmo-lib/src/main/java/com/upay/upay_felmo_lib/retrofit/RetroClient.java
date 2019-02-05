package com.upay.upay_felmo_lib.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upay.upay_felmo_lib.builder.UpayWebBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static Retrofit getRetrofitInstance() {

        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(UpayWebBuilder.getRequestURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

}
