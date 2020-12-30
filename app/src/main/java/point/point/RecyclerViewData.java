package point.point;

import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerViewData{
    
    private String name;
    private String mText2;
    private int mImageResource;
    private int turnow;
    
	private ArrayList<String> adrs;
	
    public RecyclerViewData(int imageResource, String name, String text2){
        this.mImageResource = imageResource;
        this.name = name;
        this.mText2 = text2;
        this.turnow = 0;
        adrs = new ArrayList<>();
    }
	
	public RecyclerViewData(int imageResource, String name, String text2, int turnow, String[] str){
        this.mImageResource = imageResource;
        this.name = name;
        this.mText2 = text2;
        this.turnow = turnow;

        if (str[0].equals("")&&str.length==1){
            adrs = new ArrayList<>();
        }else{
            adrs = new ArrayList<>(Arrays.asList(str));
        }
    }
	
	public boolean checkpnt (String adress, boolean t){
		
        if (adrs.size()!=0&&!t){
            return adrs.get(0).equals(adress);
        }
        else return adrs.contains(adress);
    }
	
	public int getTurSiz(){
		return adrs.size();
    }
	
    public int getTurNow(){
		return turnow;
    }

    public void addTrn(String adr){
		adrs.add(adr);
	}


	
	public void rmvTrn(){
        if (adrs.size()!=0){
            adrs.remove(0);
            turnow = turnow + 1;
        }
    }
    
    public String getTrnNowName(){
        if (adrs.size()!=0){
        return adrs.get(0);
        }
        return "";
    }

    public ArrayList<String> getTrnLst() {
        return adrs;
    }

    public int getNxtTrn(){
		return adrs.size()+turnow;
    }
    
    public int  getImageResource(){
        return mImageResource ;
    }
    
    public void changeText1(String text){
        name = text;
    }

    public void changeSubText(String text){
        mText2 = text;
    }
    
    public String getText1(){
        return name;
    }
    
    public String getText2(){
        return mText2;
    }
    
	//@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
	public String toString(){
        //~ String ADRS = null;
        //~ if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //~ ADRS = String.join("/-", adrs);
        //~ }
        String A = adrs.toString();
		 //if (adrs.size()==0){
         //   A="aaa;1ttth;1ky;1juuu";
		 //}else {
            A = A.substring(1,A.length()-1);
            A = A.replace(", ", ";1");
        //}
        return A+";2"+mImageResource+""+";2"+name +";2"+ mText2+";2"+turnow+"";
	}
}
