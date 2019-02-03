package com.upay.upay_felmo_lib.listener;

import java.util.HashMap;

public  interface UpayFelmoListener {
    void onSuccess(HashMap<String, String> values);
    void onFailure(HashMap<String, String> values);
}
