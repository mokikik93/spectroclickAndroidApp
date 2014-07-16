package com.example.spectroclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main Activity
 * @author Danny Byun
 *
 */
public class BaseActivity extends Activity {
	
	TextView tvStatus;//for test purpose
	JSONObject js;//JSON object of QR code
	public final static String RAW_RESULT = "com.example.spectroclick.RAW_RESULT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_base);
	    tvStatus = (TextView) findViewById(R.id.tvStatus);
	}
	/**
	* brings up Camera to front screen and tries to scan for QR code
	* @param view default view object
	*/
	public void toggleScan(View view){
		try {
	        Intent intent = new Intent(
	        "com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "tv_CODE_MODE,PRODUCT_MODE");
	        startActivityForResult(intent, 0);
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();
	    }
	}
	/**
	 * when the camera reads a QR code, it calls the main parser
	 * @param requestCode request code for the scanning library to rather scan QRcode or barcode 
	 * @param resultCode result code given from the scanning library
	 * @param intent intent given from the scanning library
	 */ 
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
	    if (requestCode == 0) {
	    	if (resultCode == RESULT_OK) {
			    	try {
						parseQR(intent.getStringExtra("SCAN_RESULT"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
		    	} else if (resultCode == RESULT_CANCELED) {
			    	tvStatus.setText("Scan cancelled.");
		    	}
	    	}
	}
	/**
	 * main parser for parsing acquired QR code
	 * @param s resulting string from scanning the QR code
	 */
	public void parseQR(String s) throws JSONException{
		js = new JSONObject(s);
		if(validateQRCode(js)){
			String raw = s;
			tvStatus.setText("successful");
			startResultActivity(raw);
			return;
		}
		tvStatus.setText("QRCode was not a valid SpectroClick's QRCode");
	}
	/**
	 * Starts ResultActivity for displaying the result(insturctions)
	 * @param result instructions
	 */
	public void startResultActivity(String result){
		Intent intent = new Intent(this,ResultActivity.class);
		intent.putExtra(RAW_RESULT, result);
		super.onResume();
		startActivity(intent);
	}
	/**
	 * self-explanatory
	 * @param js JSONObject to be validate
	 * @return true if the QRCode is valid SpectroClick's QRCode, otherwise false
	 */
	private boolean validateQRCode(JSONObject js){
		try{
			js.getString("conf");
			return true;
		} catch (JSONException e) {
			Log.w("QRCode","This QRCode is not valid");
			return false;
		}
	}
}


