package com.upay.upay_felmo_lib.Utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;


public final class AppUtils {

    public static HashMap<String, String> splitQuery(URL url) {
        HashMap<String, String> query_pairs = new HashMap<>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return query_pairs;
    }

    public static HashMap<String, String> getResponse(URL url){
        HashMap<String,String> data = splitQuery(url);
        if(data.containsKey("data")){
            String encodedString = data.get("data");
            try {
                String decodedJson = new String(Base64.decode(encodedString,Base64.DEFAULT), "UTF-8");
                return getValueFromJson(decodedJson);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  data;
    }

    private static   HashMap<String,String> getValueFromJson(String json){
        HashMap<String,String> values = new HashMap<>();
        try {
            JSONObject object = new JSONObject(json);
            Iterator<String> iter = object.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                String value = object.getString(key);
                values.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      return values;
    }
}
