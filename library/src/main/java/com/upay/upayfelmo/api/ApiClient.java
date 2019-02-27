package com.upay.upayfelmo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
	private final Retrofit retrofit;

	/**
	 * Initialize a new instance of the class.
	 * @param baseURL Base URL.
	 */
	public ApiClient(String baseURL)
	{
		retrofit = newInstance(baseURL);
	}

	private Retrofit newInstance(String baseURL)
	{
		Gson gson = new GsonBuilder().setLenient().create();

		return new Retrofit.Builder()
			.baseUrl(baseURL)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build();
	}

	/**
	 * Create the given service.
	 * @param service Service to be created.
	 * @param <S> Service type.
	 * @return Returns service instance.
	 */
	public <S> S createService(final Class<S> service)
	{
		return retrofit.create(service);
	}
}
