package com.upay.upayfelmo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents user authentication information model.
 */
@SuppressWarnings("unused")
public class UserAuth
{
	@SerializedName("signature")
	@Expose
	private String signature;

	@SerializedName("client")
	@Expose
	private String clientKey;

	@SerializedName("Access-Country")
	@Expose
	private String countryCode;

	/**
	 * Initialize a new instance of the class.
	 * @param signature Signature.
	 * @param clientKey Client key.
	 * @param countryCode Target country code.
	 */
	public UserAuth(String signature, String clientKey, String countryCode)
	{
		this.signature = signature;
		this.clientKey = clientKey;
		this.countryCode = countryCode;
	}

	/**
	 * Get signature.
	 * @return Returns signature.
	 */
	public String getSignature()
	{
		return signature;
	}

	/**
	 * Set signature.
	 * @param signature Signature to be set.
	 */
	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	/**
	 * Get target country code.
	 * @return Returns country code.
	 */
	public String getCountryCode()
	{
		return countryCode;
	}

	/**
	 * Set country code.
	 * @param countryCode Country code to be set.
	 */
	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	/**
	 * Get client key.
	 * @return Returns client key.
	 */
	public String getClientKey()
	{
		return clientKey;
	}

	/**
	 * Set client key.
	 * @param clientKey Client key to be set.
	 */
	public void setClientKey(String clientKey)
	{
		this.clientKey = clientKey;
	}
}
