package point.point;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class GetTurn extends AppCompatActivity{

    public TextView texyourtur;
    public TextView texturnumb;
    public TextView textstaynumb;

    String yrtrn ;
    String TurNow ;
    String stay ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_get_turn);
        
        texyourtur= findViewById(R.id.texyourtur);
        texturnumb= findViewById(R.id.texturnumb);
        textstaynumb= findViewById(R.id.textstaynumb);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Get Turn");
		
        //turnumb = getIntent().getIntExtra("TURN",-1);
		//String address = data.getExtras().getString(information.DEVICE_ADDRESS);
		
        //String turnumb = getIntent().getStringExtra("turn");
        //String staynumb = getIntent().getStringExtra("stay");
        
        yrtrn = getIntent().getExtras().getString("YOUR_TURN","-1");
        TurNow = getIntent().getExtras().getString("THE_TURN_NOW","-1");
        
        stay = String.valueOf(getIntent().getIntExtra("YOUR_TURN",-1)-getIntent()
                .getIntExtra("THE_TURN_NOW",-1));
                
        texyourtur.setText(yrtrn);
        texturnumb.setText(TurNow);
        textstaynumb.setText(stay);
        
        if (TurNow.equals(yrtrn)){
            setTitle("IT'S YOUR TURN NOW");
        }
        
       // final Handler myTimerHandler = new Handler();
        
        //~ myTimerHandler.postDelayed(
                //~ new Runnable(){
                    //~ @Override
                    //~ public void run()
                    //~ {
                        //~ finish();
                    //~ }
                //~ },5000);
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
	public void onBackPressed(){
		 super.onBackPressed();
		     finish();
	}
	
}
