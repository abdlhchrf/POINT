package point.point;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static point.point.AppTrn.CHANNEL_ID;

public class TurnService extends Service {

	private BluetoothAdapter bluetoothAdapter = null;
	private final Handler myTimerHandler = new Handler();
	private int yrtrn =0;
    private int TurNow =0;
    private String grpname ="";
    private ArrayList<String> OldName ;
    private String[] h;
	private Notification notification;
	
    @Override
	public void onCreate(){
        super.onCreate();
		OldName = new ArrayList<>();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
		initializeBtDiscovery();
        startDiscovery();
        RenameBLT(".");
        if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
		ensureDiscoverable(true);
	}
	
    private void initializeBtDiscovery(){
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(discoveryFinishReceiver, filter);
		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(discoveryFinishReceiver, filter);
	}
	
	private void startDiscovery(){
		if (bluetoothAdapter.isDiscovering()){
			bluetoothAdapter.cancelDiscovery();
			}
			bluetoothAdapter.startDiscovery();
	}
	
	public void RenameBLT(String NewName){
	// Bluetooth Rename
	// long lTimeToGiveUpMs = System.currentTimeMillis() + 10000;
	 if (bluetoothAdapter != null){
		//String OldName = bluetoothAdapter.getName();
			if(!OldName.equals(NewName)){//equalsIgnoreCase
				//final Handler myTimerHandler = new Handler();
				//bluetoothAdapter.enable();
				myTimerHandler.postDelayed(
					new Runnable(){
						@Override
						public void run(){
						if(bluetoothAdapter.isEnabled()){
								bluetoothAdapter.setName(NewName);
								if (NewName.equals(bluetoothAdapter.getName())){
									
									//Log.i(TAG_MODULE, "Updated BT Name to " + myBTAdapter.getName());
									//bluetoothAdapter.disable();
								}
							}
						}
				},500);
			}
		}
	}
	
    //~ public boolean isMatch(String star){
		//~ String n = StringEncryption.crypt(grpname,true);
		//~ //String name = DevicesArrayAdapter.getItem(i);
		//~ //break;
		//~ return star.startsWith(n);
	//~ }
	
	private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver(){
	@Override
	public void onReceive(Context context, Intent intent){
		String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)){
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//know my turn from here
				//grpname
				//isMatch(device.getName())
				if(device.getName().startsWith(Encrypt.crypt(grpname))&&
						!OldName.contains(device.getName())){

					h = Encrypt.crypt(device.getName()).split(";4");
					//~ mList.get(pos).getText1()
					//~ +"_1"+mList.get(pos).getTrnsSize()
					RenameBLT(device.getName());
					OldName.add(device.getName());

					TurNow = Integer.parseInt(h[1]);

					//.substring(h.indexOf(";1")+2)

					if (TurNow==yrtrn){
						startYourTrnNotification("GO GO GO ! IT'S YOUR TURN");
					}else{
						startNotification();
					}
					saveTurnow();

					/*myTimerHandler.postDelayed(
						new Runnable(){
							@Override
							public void run(){
							RenameBLT("0");
						}
					},3000);*/
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
	
	private void saveTurnow(){
		SharedPreferences settings = getSharedPreferences("POINT", Context.MODE_PRIVATE);
		//~ settings.edit().clear().apply();
		//~ if (settings.getInt("TRNOW", 0)!=null)
		//~ {
			//~ settings.edit().remove("TRNOW").apply();
		//~ }
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("TRNOW", TurNow);
		editor.apply();
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        yrtrn = intent.getIntExtra("YOUR_TURN",0);
        grpname = intent.getStringExtra("GROUPE_NAME");
        startNotification();
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    
    public void startNotification(){
		Intent notificationIntent = new Intent(this, GetTurn.class);
        notificationIntent.putExtra("YOUR_TURN", yrtrn);
        notificationIntent.putExtra("THE_TURN_NOW", TurNow);
        int r = yrtrn-TurNow;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
               // Notification
         notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("POINT")
                .setContentText("remain : "+r)
                .setSmallIcon(R.drawable.set_point)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
	}
    
    public void startYourTrnNotification(String text){
			Intent onIntent = new Intent(this, GetTurn.class);
			onIntent.putExtra("YOUR_TURN", yrtrn);
			onIntent.putExtra("THE_TURN_NOW", TurNow);
        	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, onIntent, 0);
			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			//Notification
			 notification =new NotificationCompat.Builder(this, CHANNEL_ID)
				.setContentTitle("POINT")//getText(R.string.notification_title)
				.setContentText(text)//getText(R.string.notification_message)
				.setSmallIcon(R.drawable.get_point)
				.setContentIntent(pendingIntent)
				//.setTicker("IT'S YOUR TURN")//getText(R.string.ticker_text)
				.setSound(alarmSound)//sound, attributes
				.setAutoCancel(true)
				.build();
				// Notification ID cannot be 0.
				startForeground(1, notification);
				//ONGOING_NOTIFICATION_ID
				myTimerHandler.postDelayed(
						new Runnable(){
							@Override
							public void run(){
								onIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(onIntent);
								stopSelf();
							}
					},10000);
	}
	
	private void ensureDiscoverable(boolean o){
			int t =1;
			if(o){t =0;}
			
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, t);
			discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(discoverableIntent);
	}
	
    @Override
    public void onDestroy(){
        super.onDestroy();
		bluetoothAdapter.cancelDiscovery();
		this.unregisterReceiver(discoveryFinishReceiver);
		ensureDiscoverable(false);
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    
}

