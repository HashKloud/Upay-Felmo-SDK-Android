package com.upay.upayfelmosdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.piashsarker.upayfelmosdk.R;
import com.upay.upay_felmo_lib.builder.UpayWebBuilder;
import com.upay.upay_felmo_lib.listener.UpayFelmoListener;
import com.upay.upay_felmo_lib.model.UserAuth;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements UpayFelmoListener {
    private static final String TOKEN = "b464ff4e3a2b1019ad4f3bbb625727a5cf279148c7c24a754c7ddcc9a6b3c1ae8ce9e9d58d138205955bb57535792f495645858964fd406c727b6c8d67c7e4ef ";




    private TextView txtStatus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStatus = findViewById(R.id.status);
    }



    private void setValueToTextView(HashMap<String, String> values) {
        String message ="";
        for(String key: values.keySet()){
            String value = values.get(key);
            message =   message.concat( "KEY: "+key+" VALUE: "+value+" \n");
        }
        txtStatus.setText(message);
    }

    public void paymentOnclick(View view) {
        UpayWebBuilder upayWebBuilder = new UpayWebBuilder(this);
        upayWebBuilder.sendRemittance(new UserAuth("179M0iF1bkTWSO64DRT4EuVDPxnZdBbB9SRMWQAY1Qs=\\n","172cf085e5d3894a","MY"), this,true);

    }

    @Override
    public void onSuccess(HashMap<String, String> values) {
    }

    @Override
    public void onFailure(HashMap<String, String> values) {

    }
}
