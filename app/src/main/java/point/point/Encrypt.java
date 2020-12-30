package point.point;

public  class Encrypt{
	//public static void main (String[] args) { }

    public static String crypt(String msg){
		
        String emsg ="";
        String stre =" !#$%&()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		String strd =stre;
		
		
        for (int i = 0; i <msg.length(); i++){

            int m = strd.indexOf(msg.charAt(i))+1;
            int n =(strd.length()/2)+((strd.length()/2)-m);
            emsg += strd.charAt(n);
            
            strd = pool(strd);
			strd = flip(strd);
			
			}
        return emsg;
		}
    
    
    
    public static String flip(String str){
		
			String x="";
			for (int h = 0; h < str.length()/2; h++){
					x+=str.charAt(h);
					x+=str.charAt(str.length()/2+h);
			}
			
			return x;
		}
	
	
	
	
	public static String pool(String str){
		
			String x="";
			for (int l = 1; l < str.length(); l++){
				x+=str.charAt(l);
			}
			x+=str.charAt(0);
			
			return x;
		}
	
}

