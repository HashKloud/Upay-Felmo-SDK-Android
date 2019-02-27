package com.upay.upayfelmo.models;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable
{

	@SerializedName("data")
	@Expose
	private Data data;

	public LoginResponse(Data data)
	{
		this.data = data;
	}

	public Data getData()
	{
		return data;
	}

	public void setData(Data data)
	{
		this.data = data;
	}

	public class Data implements Serializable
	{
		@SerializedName("client")
		@Expose
		private String client;

		@SerializedName("redirect_url")
		@Expose
		private String reDirectUrl;

		@SerializedName("signature")
		@Expose
		private String signature;

		public Data(String signature, String client, String reDirectUrl)
		{
			this.client = client;
			this.reDirectUrl = reDirectUrl;
			this.signature = signature;
		}

		public String getSignature()
		{
			return signature;
		}

		public void setSignature(String signature)
		{
			this.signature = signature;
		}

		public String getClient()
		{
			return client;
		}

		public void setClient(String client)
		{
			this.client = client;
		}

		public String getReDirectUrl()
		{
			/** Decoding Base64 data into string**/
			return new String(Base64.decode(reDirectUrl, Base64.DEFAULT));
		}

		public void setReDirectUrl(String reDirectUrl)
		{
			this.reDirectUrl = reDirectUrl;
		}
	}
}
