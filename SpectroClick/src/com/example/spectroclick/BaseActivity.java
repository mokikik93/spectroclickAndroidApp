package com.example.spectroclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	TextView tvStatus;
	TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvResult = (TextView) findViewById(R.id.tvResult);

        Button scanBtn = (Button) findViewById(R.id.btnScan);
        Log.d("BaseActivity", "For Testing");
        
        //for testing Git

        //in some trigger function e.g. button press within your code you should add:
        scanBtn.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
		        try {
			        Intent intent = new Intent(
			        "com.google.zxing.client.android.SCAN");
			        intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
			        startActivityForResult(intent, 0);
		        } catch (Exception e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			        Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();
		        }
		
		    }
        });
    }
    
public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == 0) {
    	if (resultCode == RESULT_OK) {
		    	tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
		    	tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
	    	} else if (resultCode == RESULT_CANCELED) {
		    	tvStatus.setText("Press a button to start a scan.");
		    	tvResult.setText("Scan cancelled.");
	    	}
    	}
	}
}
