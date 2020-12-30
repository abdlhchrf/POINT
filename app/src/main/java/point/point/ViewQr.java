package point.point;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class ViewQr extends AppCompatActivity{

    BluetoothAdapter bluetoothAdapter;
	ImageView imageView;

	//Thread thread;
	public final static int QRcodeWidth = 350 ;
	Bitmap bitmap ;
	//TextView tv_qr_readTxt;
	Intent intentqrscan = new Intent();
    String[] h ;
    String myadres="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_qr);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");
        imageView = findViewById(R.id.imageView);
        
		//if(!editText.getText().toString().isEmpty()){
		//EditTextValue = editText.getText().toString();

		//bluetoothAdapter = getIntent().getStringExtra("blt");
		//setResult(Activity.RESULT_CANCELED, intentqrscan);

		myadres = getIntent().getStringExtra("qrView");
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		try{
			BitMatrix bitMatrix = multiFormatWriter.encode(myadres, BarcodeFormat.QR_CODE,1080,1080);
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
			imageView.setImageBitmap(bitmap);
		}catch (WriterException e){
			
			e.printStackTrace();
		}

		//~ final Handler myTimerHandler = new Handler();
		//~ myTimerHandler.postDelayed(
		//~ new Runnable(){
		//~ @Override
		//~ public void run()
		//~ {
		//~ finish();
		//~ }
		//~ },500);

		if (getIntent().getBooleanExtra("getrn",false)){
			setTitle("Starting...");
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			initializeBtDiscovery();
			startDiscovery();
		}else{
			setTitle("Checking...");
			SetresultOk();
			intentqrscan.putExtra("view", false);
		}
    }
/*
    Bitmap TextToImageEncode(String Value) throws WriterException{
        BitMatrix bitMatrix;
        try{
           bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.QR_CODE
                ,QRcodeWidth, QRcodeWidth, null );

        }catch (IllegalArgumentException Illegalargumentexception){
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        
        for (int y = 0; y < bitMatrixHeight; y++){
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++){
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    */
    private final BroadcastReceiver discovery = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               
                if (device.getName().startsWith(Encrypt.crypt(myadres))){
                    //~ adres+"/1/"+mList.get(pos).getText1()+"/2/"+mList.get(pos)
                    //~ .getTurNow()+"/3/"+mList.get(pos).getNxtTrn();;;;;;;
                    //~ adres = data.getExtras().getString("result_qr_scan")
                    //~ .substring(data.getExtras().getString("result_qr_scan").indexOf("/*/")+3,b.length());;;;;
                    //~ int pos = foundInMainArrayAdapter(data.getExtras().getString("result_qr_scan")
                    //~ .substring(0, data.getExtras().getString("result_qr_scan").indexOf("/*/")-1));;;;;
                    //~ myinfo.copyValueOf(b, 10, 6);
                    //~ char[] b = device.getName().toCharArray();

                    //~ grpname = h.substring(h.indexOf("_1")+2,h.indexOf("_2")-1);
                    //~ turNow = Integer.parseInt(h.substring(h.indexOf("_2")+2,h.indexOf("_3")-1));
                    //~ yrtrn = Integer.parseInt(h.substring(h.indexOf("_3")+2));

                    //~ Integer.parseInt(myinfo.copyValueOf(b, 6, 4));
                    //~ .replace("-","")  09-00-00-00-00-00

                    h = Encrypt.crypt(device.getName()).split(";5");
                    intentqrscan.putExtra("grpname", h[1]);
                    intentqrscan.putExtra("turNow", Integer.parseInt(h[2]));
                    intentqrscan.putExtra("yrtrn", Integer.parseInt(h[3]));
                    //finishActivity(1);
                    bluetoothAdapter.cancelDiscovery();
                    unregisterReceiver(discovery);
                    SetresultOk();
                    intentqrscan.putExtra("view", true);
                    finish();
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //~ setProgressBarIndeterminateVisibility(false);
                //~ setTitle(R.string.select_device);
                //~ if (newDevicesArrayAdapter.getCount() == 0) {
                //~ String noDevices = getResources().getText(
                //~ R.string.none_found).toString();
                //~ newDevicesArrayAdapter.add(noDevices);
                //~ }
                startDiscovery();
            }

        }  
    };

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
    
    private void initializeBtDiscovery(){
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discovery, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discovery, filter);
    }
     
    private void startDiscovery(){
        if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        Toast.makeText(this,"start Discovery",Toast.LENGTH_SHORT).show();
    }

    public void SetresultOk(){
        setResult(Activity.RESULT_OK, intentqrscan);
    }

    @Override
	public void onDestroy(){
		super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(discovery);
    }
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		setResult(Activity.RESULT_CANCELED, intentqrscan);
		finish();
	}
    
}




