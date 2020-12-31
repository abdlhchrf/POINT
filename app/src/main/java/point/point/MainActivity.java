package point.point;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity{

    private BluetoothAdapter bluetoothAdapter=null ;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
	//private ArrayList<RecyclerViewData> recyclerViewData = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
	//private String myinfo;
	private ArrayList<RecyclerViewData> mList;
	private final Handler myTimerHandler = new Handler();

	private Button buttonInsert;
	private Button buttonRemove;
	private EditText editTextInsert;
	private TextView textView;
	//~ private EditText editTextRemove;
    
	private MenuItem one;
	private MenuItem two;
	private MenuItem three;
    
    int yrtrn = 0;
    int turNow = -2;
    String grpname="";
    String myadres="";


	@SuppressLint("HardwareIds")
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonInsert = findViewById(R.id.button_insert);
        buttonInsert.setVisibility(View.GONE);
        
        editTextInsert = findViewById(R.id.edittext_insert);
		editTextInsert.setVisibility(View.GONE);
		
		buttonRemove = findViewById(R.id.button_remove);
		buttonRemove.setVisibility(View.GONE);

		textView= findViewById(R.id.textView);
		textView.setVisibility(View.GONE);

		createExampleList();
		buildRecyclerView();
        
		if(isMyServiceRunning()){
			SharedPreferences settings = getSharedPreferences("POINT", Context.MODE_PRIVATE);
			//settings.edit().clear().apply();
			turNow=settings.getInt("TRNOW",-1);
			yrtrn=settings.getInt("YRTRN",-1);

			adapter.notifyItemChanged(0);
			//~ settings.edit().remove("TRNOW").apply();
			turNow =getIntent().getIntExtra("GO",0);
			if (turNow==yrtrn){
				bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				stopTrnService();
				Intent onIntent = new Intent(this, GetTurn.class);
				startActivity(onIntent);
				//setTitle("NOW IT'S YOUR TURN");
				//bluetoothAdapter.disable();
				//stopForeground(STOP_FOREGROUND_DETACH);
			}
		}

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//Deviceslist =new ArrayList<String>();
		myadres= bluetoothAdapter.getAddress().replaceAll("-","").substring(10);
		//09-00-00-00-00-00
		
		//setButtons();
		/*
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL));
        //~ Intent info = new Intent(this, ScanQr.class);
        //~ startActivityForResult(info, 2);
        MainChat = (ListView) findViewById(R.id.MainChat);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.message);
        MainChat.setAdapter(chatArrayAdapter);
        MainChat.setOnItemClickListener(mDeviceClickListener);
        setTitle("scanning....");  
        lsvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lsvChat.setAdapter(chatArrayAdapter);
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged(){
                super.onChanged();
                lsvChat.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        */
    }
    ///////////////////////////////////////////////////
    
	private void saveData(){
			SharedPreferences settings = getSharedPreferences("POINT", Context.MODE_PRIVATE);
			//settings.edit().clear().apply();
			if (settings.contains("PNT")){
				
				 settings.edit().remove("PNT").apply();
			}
			SharedPreferences.Editor editor = settings.edit();
			//Gson gson = new Gson();
			//String json = gson.toJson(mExampleList);
			String save = mList.toString();
			save = save.substring(1,save.length()-1);
			save = save.replace(", ", ";3");
			
			//String save = String.join("/+", mList);
			editor.putString("PNT", save);
			editor.putInt("YRTRN", yrtrn);
			editor.apply();
	}

	private void loadData(){
		try {
		SharedPreferences sharedPreferences = getSharedPreferences("POINT", MODE_PRIVATE);
		String save = sharedPreferences.getString("PNT", "aaa");
		yrtrn=sharedPreferences.getInt("YRTRN",-1);

		//If the key is not available neither are the values!!
			if (sharedPreferences.contains("PNT")){
				//Type type = new TypeToken<ArrayList<ExampleItem>>(){}.getType();
				//mExampleList = gson.fromJson(json, type);
				//ArrayList<String> test = new ArrayList<>();

				String[] str = save.split(";3");
				//ArrayList<String> ViewData = new ArrayList<>(Arrays.asList(str));
				
				String[] stri;
				String[] stor;
				for(String s: str){
						stri = s.split(";2");
						if (str.length == 5){
							stor = str[0].split(";1");
							mList.add(mList.size(),new RecyclerViewData(Integer.parseInt(str[1]),str[2],
									str[3],Integer.parseInt(str[4]),stor));
					}
				}
			}
		}
		catch (Exception ex) {
            Toast(" loadData Error");

		}
	}
    
    private boolean isMyServiceRunning(){
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			if (TurnService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

    public void setNameButton(){
		// buttonInsert = findViewById(R.id.button_insert);
		// buttonRemove = findViewById(R.id.button_remove);
		// editTextInsert = findViewById(R.id.edittext_insert);
		// editTextRemove = findViewById(R.id.edittext_remove);
		
		buttonInsert.setVisibility(View.VISIBLE);
		editTextInsert.setVisibility(View.VISIBLE);
		//textView.setVisibility(View.VISIBLE);
		
		buttonInsert.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){

				//textView.setVisibility(View.GONE);
				buttonInsert.setVisibility(View.GONE);
				editTextInsert.setVisibility(View.GONE);
				return true;
			}
		});

	buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
				//int position = Integer.parseInt(editTextInsert.getText().toString());
				//insertItem(position);
				grpname = editTextInsert.getText().toString();
				if (foundInMainRecyclerView(grpname)==-1&&grpname.matches("([a-zA-Z0-9]{1,7})")){
					// !grpname.equals("GET Point")
					insertItem(mList.size(),R.drawable.set_point, grpname,
							" the turn now for :" );
					editTextInsert.getText().clear();
					//ensureDiscoverable();
				}else{
					Toast("enter a new name : a-zA-Z0-9 ");
				}
				buttonInsert.setVisibility(View.GONE);
				editTextInsert.setVisibility(View.GONE);
				//textView.setVisibility(View.GONE);
			}
        });
    }

	public void Toast(String text){
		Toast.makeText(this,text
				,Toast.LENGTH_SHORT).show();
	}

	public void createExampleList(){
		mList = new ArrayList<>();
		loadData();
		//~ mList.add(new RecyclerViewData(R.drawable.ic_delete, "Line 1", "Line 2"));
		//~ mList.add(new RecyclerViewData(R.drawable.ic_audio, "Line 3", "Line 4"));
		//~ mList.add(new RecyclerViewData(R.drawable.ic_sun, "Line 5", "Line 6"));
	}
	
	public void buildRecyclerView(){
			// set up the RecyclerView
			layoutManager = new LinearLayoutManager(this);
			recyclerView = findViewById(R.id.recyclerView);
			recyclerView.setLayoutManager(layoutManager);
			recyclerView.setHasFixedSize(true);
			adapter = new RecyclerViewAdapter(mList);
			recyclerView.setAdapter(adapter);
			
			adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
				
			@Override
			public void onItemClick(int position){
				seeGetOrSetTrn(mList.get(position).getText1().equals("GET Point"),position);

				//changeItem(position, "Clicked");
			}
			
			@Override
            public void onDeleteClick(int position){
				//buttonRemove = findViewById(R.id.button_remove);
				buttonRemove.setVisibility(View.VISIBLE);
				textView.setVisibility(View.VISIBLE);
				buttonRemove.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						buttonRemove.setVisibility(View.GONE);
						textView.setVisibility(View.GONE);
						removeItem(position);
						return true;
					}
				});

				buttonRemove.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v){
						textView.setVisibility(View.GONE);
						buttonRemove.setVisibility(View.GONE);
					}
				});
				/*
				myTimerHandler.postDelayed(
				new Runnable(){
					@Override
					public void run(){
						buttonRemove.setVisibility(View.GONE);
					}
				},2000);*/
            }
		});
	}

	public void insertItem(int position, int image, String text, String subtext){
        mList.add(position, new RecyclerViewData(image, text, subtext));
        adapter.notifyItemInserted(position);
    }
    
    public void removeItem(int position){
        mList.remove(position);
        adapter.notifyItemRemoved(position);
        if (mList.size()==0)
		{
			one.setEnabled(false);
			two.setEnabled(true);
			three.setEnabled(true);
		}
    }

    public void changeItem(int position, String txt){
        mList.get(position).changeSubText(txt);
        adapter.notifyItemChanged(position);
    }
    
	public void startTrnService(){//View v
        Intent serviceIntent = new Intent(this, TurnService.class);
        serviceIntent.putExtra("YOUR_TURN", yrtrn);
        serviceIntent.putExtra("GROUPE_NAME", grpname);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    
    public void stopTrnService(){//View v
        Intent serviceIntent = new Intent(this, TurnService.class);
		stopService(serviceIntent);
    }
    
	public void seeGetOrSetTrn(boolean t,int p){

		Intent intenti;
		if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
		if(t){

			SharedPreferences sharedPreferences = getSharedPreferences("POINT", MODE_PRIVATE);
			turNow = sharedPreferences.getInt("TRNOW",turNow);
			intenti = new Intent(this, GetTurn.class);
			intenti.putExtra("YOUR_TURN", yrtrn);
			intenti.putExtra("THE_TURN_NOW", turNow);
			startActivity(intenti);

		}else{

			intenti = new Intent(this, SetTurn.class);
			intenti.putExtra("GROUPE_NAME", mList.get(p).getText1());
			intenti.putExtra("TRN_NOW", mList.get(p).getTurNow());
			intenti.putExtra("TRN_SIZ", mList.get(p).getTurSiz());
			intenti.putStringArrayListExtra("TRN_LST",mList.get(p).getTrnLst());
			intenti.putExtra("POS",p);
			startActivityForResult(intenti, 2);
			//if (mList.get(p).getTurSiz()!=0)
			ensureDiscoverable();

		}
		//startActivity(intent);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
			case 1:
                if (resultCode == Activity.RESULT_OK){

					if (data.getExtras().getBoolean("view",false)){
					grpname = data.getExtras().getString("grpname","");
					turNow = data.getExtras().getInt("turNow",0);
					yrtrn = data.getExtras().getInt("yrtrn",0);
					insertItem(0,R.drawable.get_point, "GET Point",
							"your turn number is :"+yrtrn);
					menuSetEnabled(true);

					//ensureDiscoverable();
					if (turNow==yrtrn){
						Intent onIntent = new Intent(this, GetTurn.class);
						startActivity(onIntent);
					}else{
						startTrnService();
					}
					//Intent intentqrscan = new Intent();
					//intentqrscan.putExtra("result_qr_scan", resultqrscan);
					//setResult(Activity.RESULT_OK, intentqrscan);
					//String resultqrscan = data.getStringExtra("result_qr_scan");
					//result.getContents();
					}

				}
                else{
					Toast("View not complete");
				}
                break;
			case 2:
                if(resultCode == Activity.RESULT_OK ){

					if (data.getExtras().getString("RMV_TRN").equals("ffff")){
						int pos = data.getExtras().getInt("pos",0);
						if (mList.get(pos).getTurSiz()!=0){
							if (removeTrn(pos,"ZZZZ")){
								Toast("one turn skiped");
							}
						}
					}

					else {
						String adres= data.getExtras().getString("result_qr_scan");
						int pos = data.getExtras().getInt("pos");
						if (!mList.get(pos).checkpnt(adres,true))
						{
							mList.get(pos).addTrn(adres);
							setTrn(pos,adres);
						}else
							Toast("No duplicate , please add new one");
					}
                }else{
					Toast("No turn added");
				}
                break;
			case 3:
                if (resultCode == Activity.RESULT_OK &&
					data.getExtras().getBoolean("check",false)){

					String[] adres = data.getExtras().getString("result_qr_scan").split(";6");
					if (foundInMainRecyclerView(adres[1])!=-1){
						int pos = foundInMainRecyclerView(adres[1]);
						if (removeTrn(pos,adres[1])){
							 nextTrn(pos);
						}
					}

				}else{
                    Toast("No turn removed");
				}break;
        }
    }

	public boolean removeTrn(int position, String adrss){
		if (mList.get(position).checkpnt(adrss,false) || adrss.equals("ZZZZ")){
			mList.get(position).rmvTrn();
			//adapter.notifyItemChanged(position);
			nextTrn( position);
			return true;
		}
		else{
			Toast("No turn removed");
			return false;
		}
	}
	
	private void nextTrn(int pos){
			String nxtrn = Encrypt.crypt(mList.get(pos).getText1()+";4"
				+mList.get(pos).getTurNow());
			RenameBLT(nxtrn);
			//mList.get(pos).changeSubText(""+text);
			changeItem(pos," the turn now for :" + mList.get(pos).getTrnNowName());

			/*myTimerHandler.postDelayed(
                new Runnable(){
                    @Override
                    public void run(){
                        RenameBLT("0");
                    }
                },2000);*/
	}
	
	private void setTrn(int pos,String adress){
		String setrn = Encrypt.crypt(adress+";5"+mList.get(pos).getText1()
						+";5"+mList.get(pos).getTurNow()+";5"+mList.get(pos).getNxtTrn());
		RenameBLT(setrn);
				/*myTimerHandler.postDelayed(
                new Runnable(){
                    @Override
                    public void run(){
                        RenameBLT("0");
                    }
                },2000);*/
	}
	
	public int foundInMainRecyclerView(String text){
			if (mList.size()!=0){
				for (int i = 0; i < mList.size(); i++){
					if (mList.get(i).getText1().equals(text)){
					return i;
					}
				}
			}
			return -1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		one = menu.getItem(0);
		two =  menu.getItem(1);
		three =  menu.getItem(2);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
    // if (isFinalized){
         //menu.getItem(1).setEnabled(false);
			myTimerHandler.postDelayed(
				new Runnable(){
					@Override
					public void run()
					{
						if (mList.size()==0)
						{
								menu.findItem(R.id.one).setEnabled(true);
								menu.findItem(R.id.two).setEnabled(true);
								menu.findItem(R.id.three).setEnabled(false);
						}
						else if (mList.get(0).getText1().equals("GET turn"))
						{
								menu.findItem(R.id.one).setEnabled(false);
								menu.findItem(R.id.two).setEnabled(false);
								menu.findItem(R.id.three).setEnabled(true);
						}
						else
						{
								menu.findItem(R.id.one).setEnabled(false);
								menu.findItem(R.id.two).setEnabled(true);
								menu.findItem(R.id.three).setEnabled(true);
						}	
					}
				},300);
			
					//~ invalidateOptionsMenu()
				    //~ MenuItem item = menu.findItem(R.id.menu_my_item);
					//~ You can also use something like:
					//~ menu.findItem(R.id.example_foobar).setEnabled(false);
				//~ }
				return true;
 	}
 	
 	public void menuSetEnabled(boolean b){
		if (b)
		{
			one.setEnabled(false);
			two.setEnabled(false);
			three.setEnabled(true);
		}else
		{
			one.setEnabled(false);
			two.setEnabled(true);
			three.setEnabled(true);
		}
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent serverIntent;
        switch (item.getItemId()){
            case R.id.one:

                if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
				serverIntent = new Intent(this, ViewQr.class);
				serverIntent.putExtra("qrView", myadres);
				serverIntent.putExtra("getrn",true);
				startActivityForResult(serverIntent, 1);
				return true;
				/*
                // serverIntent = new Intent(this, ViewQr.class);
				// startActivityForResult(serverIntent, 1);
				// bluetoothAdapter.cancelDiscovery();
				//String info = ((TextView) v).getText().toString();
                String info = "";
				// String address = info.substring(info.length() - 17);
                Intent intent = new Intent();
                intent.putExtra(DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                if (!bluetoothAdapter.isEnabled()){
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
                }
                return true;*/

            case R.id.two:
				setNameButton();
				//GroupTurns.add(mList.size(),new Turns(-1,"NAME OF GROUP"));
				menuSetEnabled(false);
				return true;
				
			case R.id.three:
					if (mList.get(0).getText1().equals("GET turn")){
						serverIntent = new Intent(this, ViewQr.class);
						serverIntent.putExtra("qrView",grpname+";6"+myadres);
						serverIntent.putExtra("getrn",false);
						
						startActivity(serverIntent);
					}
					else{
						serverIntent = new Intent(this, ScanQr.class);
						
						startActivityForResult(serverIntent, 3);
						ensureDiscoverable();
					}
				return true;
        }
			return true;
	}

	public void RenameBLT(String NewName){
	// Bluetooth Rename
	// long lTimeToGiveUp_ms = System.currentTimeMillis() + 10000;
		if (bluetoothAdapter != null){
		String OldName = bluetoothAdapter.getName();
			if(!OldName.equalsIgnoreCase(NewName))
			{
				//final Handler myTimerHandler = new Handler();
				//bluetoothAdapter.enable();
				myTimerHandler.postDelayed(
					new Runnable(){
					@Override
					public void run()
					{
						if(bluetoothAdapter.isEnabled()){
							bluetoothAdapter.setName(NewName);
							if (NewName.equals(bluetoothAdapter.getName()))
							{
								Toast("bluetooth name updated to :"+bluetoothAdapter.getName());
								//Log.i(TAG_MODULE, "Updated BT Name to " + myBTAdapter.getName());
								//bluetoothAdapter.disable();
							}
						}
					}
				},500);
			}
		}
	}
	
	
	private void ensureDiscoverable(){
			/*if (!bluetoothAdapter.isEnabled()){
							Intent enableIntent = new Intent(
									BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			}*/
			if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
			// SCAN_MODE_CONNECTABLE
			//if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
			//}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onStop(){
		super.onStop();
		saveData();
		if (!isMyServiceRunning()){
			bluetoothAdapter.cancelDiscovery();
		}
	}
/*
	@Override
	public void onStart(){
	  super.onStart();
	}

	@Override
	public synchronized void onResume(){
		super.onResume();
	}

	@Override
	public synchronized void onPause(){
		super.onPause();
	}
*/
}


