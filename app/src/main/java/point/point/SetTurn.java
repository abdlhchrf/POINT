package point.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetTurn extends AppCompatActivity{



    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    //private ArrayList<RecyclerViewData> recyclerViewData = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    //private String myinfo;
    private ArrayList<RecyclerViewData> mList;
    //ArrayList<String> line = new ArrayList<String>();
    TextView turnow ;
	TextView tursiz ;
	Intent intentqrscan ;

	String trnow ;
    String trnsz;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_set_turn);
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		intentqrscan = new Intent();
		
		pos = getIntent().getExtras().getInt("POS",0);

        setTitle(" Group of: "+getIntent().getExtras().getString("GROUPE_NAME","Turns"));
        turnow = findViewById(R.id.turnow);
        tursiz = findViewById(R.id.tursiz);
        
        //String turnumb = getIntent().getStringExtra("turn");
        //String staynumb = getIntent().getStringExtra("stay");

		//getIntent().getExtras().getString("TRN_NOW","-1");
        //getIntent().getExtras().getString("TRN_NXT","-1");

        trnow = String.valueOf( getIntent().getExtras().getInt("TRN_NOW",-3));
        trnsz =  String.valueOf( getIntent().getExtras().getInt("TRN_SIZ",-3));
        
        turnow.setText(trnow);
        tursiz.setText(trnsz);

        

    }

    public void addTrn(View view) {
        Intent info = new Intent(this, ScanQr.class);
        startActivityForResult(info, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.turn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
         switch (item.getItemId()){
            case R.id.one:
                intentqrscan.putExtra("RMV_TRN", "ffff");
                intentqrscan.putExtra("pos", pos);
                setResult(Activity.RESULT_OK, intentqrscan);
                finish();
				return true;
				
			case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;

            default:
                 //return super.onOptionsItemSelected(item);
                 return true;
				}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
		//IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){

                //String resultqrscan = getIntent().getStringExtra();
                String resultqrscan = data.getStringExtra("result_qr_scan");
                //result.getContents();
                
				intentqrscan.putExtra("result_qr_scan", resultqrscan);
                intentqrscan.putExtra("RMV_TRN", "");
                intentqrscan.putExtra("pos", pos);
				setResult(Activity.RESULT_OK, intentqrscan);
                finish();
            }
            else {//if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this," No turn",
                            Toast.LENGTH_SHORT).show();
            }
        }

    }
    
    @Override
	public void onBackPressed(){
		 super.onBackPressed();
		 setResult(Activity.RESULT_CANCELED, intentqrscan);
		 finish();
	}

    
}
