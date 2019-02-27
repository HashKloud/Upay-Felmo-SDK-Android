package com.upay.upayfelmo.remit;

import android.util.Base64;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;

/* package */ class Utils
{
	public static boolean isValidURL(String urlStr)
	{
		return Patterns.WEB_URL.matcher(urlStr).matches();
	}

	public static HashMap<String, String> splitQuery(URL url)
	{
		HashMap<String, String> query_pairs = new HashMap<>();
		String query = url.getQuery();
		String[] pairs = query.split("&");
		for(String pair : pairs)
		{
			int idx = pair.indexOf("=");
			try
			{
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			}
			catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		return query_pairs;
	}

	public static HashMap<String, String> getResponse(String value)
	{
		HashMap<String, String> data = new HashMap<>();

		try
		{
			String decodedJson = new String(Base64.decode(value, Base64.DEFAULT), "UTF-8");
			return getValueFromJson(decodedJson);
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static HashMap<String, String> getValueFromJson(String json)
	{
		HashMap<String, String> values = new HashMap<>();
		try
		{
			JSONObject object = new JSONObject(json);
			Iterator<String> iter = object.keys();
			while(iter.hasNext())
			{
				String key = iter.next();
				String value = object.getString(key);
				values.put(key, value);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return values;
	}
}
