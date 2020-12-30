package point.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScanQr extends AppCompatActivity implements BarcodeCallback {

	private DecoratedBarcodeView barcodeView;
    Intent intentqrscan;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_scan_qr);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        intentqrscan = new Intent();

        /*
        IntentIntegrator integrator = new IntentIntegrator(ScanQr.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
		integrator.setCameraId(0);
		integrator.setBeepEnabled(false);
		integrator.setBarcodeImageEnabled(false);
		integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }else {

        }
        */
        
        setTitle("Scan QrCode");
        barcodeView = findViewById(R.id.barcodeScannerView);
        Collection<BarcodeFormat> formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(this);
        barcodeView.setStatusText("");
        barcodeView.resume();
          
    }
    /*
   
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data){
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if(result != null) {
				if(result.getContents() == null) {
					Log.e("Scan*******", "Cancelled scan");
					Intent intentqrscan = new Intent();
					setResult(Activity.RESULT_CANCELED, intentqrscan);

				}else{
					Log.e("Scan", "Scanned");
					//tv_qr_readTxt.setText(result.getContents());
					//Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
					// String info = ((TextView) v).getText().toString();
					// String address = info.substring(info.length() - 17);
					String resultqrscan = result.getContents();
					Intent intentqrscan = new Intent();
					intentqrscan.putExtra("result_qr_scan", resultqrscan);
					setResult(Activity.RESULT_OK, intentqrscan);
				}
				finish();
			}else{
				// This is important, otherwise the result will not be passed to the fragment
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
    */

    @Override
    public void barcodeResult(BarcodeResult result){
        //Intent intentqrscan = new Intent();

        if(result.getText().matches("([a-zA-Z0-9]{1,7};6([A-F0-9]{4}))")){

            String resultqrscan = result.getText();
            intentqrscan.putExtra("result_qr_scan", resultqrscan);
            intentqrscan.putExtra("check", true);
            setResult(Activity.RESULT_OK, intentqrscan);
        }

        else if (result.getText().matches("[A-F0-9]{4}")){

            String resultqrscan = result.getText();
            intentqrscan.putExtra("result_qr_scan", resultqrscan);
            intentqrscan.putExtra("check", false);////////////////////
            setResult(Activity.RESULT_OK, intentqrscan);
        }
        
        else{
			
            setResult(Activity.RESULT_CANCELED, intentqrscan);
        }
        //Toast.makeText(this, "Scanned: " + result.getResult(), Toast.LENGTH_LONG).show();
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints){

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(barcodeView != null) {
            barcodeView.pauseAndWait();
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(barcodeView != null) {
            barcodeView.pause();
        }
    }
    
    @Override
	public void onBackPressed(){
		 super.onBackPressed();
		 setResult(Activity.RESULT_CANCELED, intentqrscan);
		 finish();
	}

}
