package org.yamaLab.pukiwikiCommunicator.language;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Util {
	/*
	static void centerAround(int x, int y, Drawable d) {
		int w = d.getIntrinsicWidth();
		int h = d.getIntrinsicHeight();
		int left = x - w / 2;
		int top = y - h / 2;
		int right = left + w;
		int bottom = top + h;
		d.setBounds(left, top, right, bottom);
	}
	*/
	static char[] i2c={'0','1','2','3','4','5','6','7',
			    '8','9','a','b','c','d','e','f'};
	public static String b2h(byte x){
		int h1=(x >> 4)& 0x0f;
		int h2=x & 0x0f;
		return "0x0"+i2c[h1]+i2c[h2];
	}
	public static String i2h(int x){
		String hex="";
		char c=i2c[x&0x0f];
		hex=""+c+hex;
		x=x>>4;
		for(int i=0;i<7;i++) {   // 4bit * 8 = 32 bit
			c=i2c[x&0x0f];
			hex=""+c+hex;
			x=x>>4;
		}
		return "0x0"+hex;
	}
	public static boolean isEOL(String x){
		if(x==null) return true;
		if(x.equals("")) return true;
		if(x.charAt(0)=='\0'||
		   x.charAt(0)=='\n'||
		   x.charAt(0)=='\r') return true;
		return false;
	}
	public static boolean parseEquation(String x, String[] left, String[] right){
		String[] rest=new String[1];
		String[] strc=new String[1];
		String l="";
		String r="";
		x=Util.skipSpace(x);
		if(Util.parseStrConst(x,strc,rest)){
			return false;
		}
		StringTokenizer st=new StringTokenizer(x,"=");
		if(st.hasMoreTokens())
		   l=st.nextToken();
		else
		   return false;
		if(st.hasMoreTokens())
			r=st.nextToken();
		else
			return false;
		l=Util.skipSpace(l);
		l=Util.deleteLastSpace(l);
		r=Util.skipSpace(r);
		r=Util.deleteLastSpace(r);
		left[0]=l;
		right[0]=r;
		return true;
	}
	public static boolean parseName(String x,String[] namertn,String[] rest){
		if(x.length()<=0) return false;
		StringBuffer xb=new StringBuffer(x);
		char c=xb.charAt(0);
		StringBuffer ix=new StringBuffer("");
		if('a'<=c && c<='z' || 'A'<=c && c<='Z'|| c=='_'){
			ix.append(c);
		}
		else{
			return false;
		}
		xb.deleteCharAt(0);
		if(xb.length()<=0) return false;
		c=xb.charAt(0);
		while('0'<=c && c<='9'||'a'<=c && c<='z' || 'A'<=c && c<='Z'|| c=='_'){
			ix.append(c);
			xb.deleteCharAt(0);
			if(xb.length()<=0) break;
			c=xb.charAt(0);
		}
		if(ix.length()>0){
		   namertn[0]=ix.toString();
		   rest[0]=xb.toString();
		   return true;
		}
		return false;

	}
	public static String skipSpace(String x){
		int is=0;
		if(x.length()<=is) return "";
		while(x.charAt(is)==' ') {
			is++;
			if(x.length()<=is){
				return "";
			}
		}
		return x.substring(is);
	}
	public static String deleteLastSpace(String x){
		try{
//		  int len=x.length();
		  int len=x.length();
		  int il=len-1;
		  while(x.charAt(il)==' '){
			  il--;
		  }
		  return x.substring(0,il+1);
		}
		catch(Exception e){
			return x;
		}
	}
	public static boolean parseKeyWord(String x, String key, String [] rest){
		if(x.startsWith(key)){
			rest[0]=x.substring(key.length());
			return true;
		}
		return false;
	}
	public static boolean isHexChar(char x){
		if('0'<=x && x<='9') return true;
		if('a'<=x && x<='f') return true;
		if('A'<=x && x<='F') return true;
		return false;
	}
	public static boolean parseHex(String x, String[] hex, String [] rest){
		StringBuffer rx=new StringBuffer("");
		if(x.length()<3) return false;
		StringBuffer xb=new StringBuffer(x);
		char c=xb.charAt(0);
		if(c!='0') return false;
		rx.append(c);
		xb.deleteCharAt(0);
		c=xb.charAt(0);
		if(c!='x') return false;
		rx.append(c);
		xb.deleteCharAt(0);
		c=xb.charAt(0);
		if(!isHexChar(c)) return false;
		rx.append(c);
		xb.deleteCharAt(0);
		c=xb.charAt(0);		
		while(isHexChar(c)){
			rx.append(c);
			xb.deleteCharAt(0);
			if(xb.length()<=0) break;
			c=xb.charAt(0);					
		}
		hex[0]=rx.toString();
		rest[0]=xb.toString();
		return true;
	}
	public static boolean parseInt(String x, int[] intrtn, String [] rest){
		int sign=1;
		StringBuffer ix=new StringBuffer("");
		StringBuffer xb= new StringBuffer(Util.skipSpace(x));
		char c=xb.charAt(0);
		if(c=='-'){
			sign=-1;
			xb.deleteCharAt(0);
			xb=new StringBuffer(Util.skipSpace(xb.toString()));
			c=xb.charAt(0);
		}
		while('0'<=c && c<='9'){
			ix.append(c);
			xb.deleteCharAt(0);
			if(xb.length()<=0) break;
			c=xb.charAt(0);
		}
		if(xb.length()>1) {
			char d=xb.charAt(1);
			if(('0'<=d && d<='9') && c=='.'){
			    return false;
			}
		}
		if(ix.length()>0){
		   int ixx=(new Integer(ix.toString())).intValue();
		   intrtn[0]=sign*ixx;
		   rest[0]=xb.toString();
		   return true;
		}
		return false;
	}
	public static boolean parseDouble(String x, double[] drtn, String [] rest){
		int sign=1;
		x=Util.skipSpace(x);
		StringBuffer xb=new StringBuffer(x);
		StringBuffer ix=new StringBuffer("");
		char c=xb.charAt(0);
		if(c=='-'){
			sign=-1;
			xb.deleteCharAt(0);
			xb=new StringBuffer(Util.skipSpace(xb.toString()));
			c=xb.charAt(0);
		}
		while('0'<=c && c<='9'){
			ix.append(c);
			xb.deleteCharAt(0);
			if(xb.length()<=0) break;
			c=xb.charAt(0);
		}
		if(xb.length()<=0){
			return false;
		}
		if(c!='.'){
			return false;
		}
		int ixx=0;
		if(ix.length()>0){
		   ixx=(new Integer(ix.toString())).intValue();
		}
		xb.deleteCharAt(0);
		c=xb.charAt(0);
        double rx=0.1;
        double r=0.0+ixx;
        while('0'<=c && c<='9'){
            int p;
            p=0;
            try{p=Integer.parseInt(""+c);}
            catch(NumberFormatException e){}
            r=r+p*rx;
            rx=rx*0.1;
            xb.deleteCharAt(0);
			if(xb.length()<=0) break;
            c=xb.charAt(0);
        }
        drtn[0]=sign*r;
        rest[0]=xb.toString();
		return true;
	}
	public static boolean parseStrConst(String x,String [] sconst, String [] rest){
		StringBuffer xconst=new StringBuffer("");
		StringBuffer xb=new StringBuffer(x);
		if(xb.charAt(0)=='\"'){
			xb.deleteCharAt(0);
			while(!(xb.charAt(0)=='\"')){
				if(xb.length()<1) {
					rest[0]="";
					sconst[0]="";
					return false;
				}
				if(xb.charAt(0)=='\\'){
						xconst.append(x.charAt(0));
						xconst.append(x.charAt(1));
						xb.deleteCharAt(0);
						xb.deleteCharAt(0);
				}
				else{
					xconst.append(xb.charAt(0));
					xb.deleteCharAt(0);
				}
			}
			xb.deleteCharAt(0);
			sconst[0]=xconst.toString();
			rest[0]=xb.toString();
			return true;
		}
		return false;
	}	
	public static boolean isDate(String x){
		String xconst="";
		SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy/MM/dd"); 
		SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss"); 
		
		try{
		  Date date = dateFormat2.parse(x);
		  return true;
		}
		catch(Exception e){
		}
		try{
		  Date date = dateFormat1.parse(x);
		  return true;
		}
		catch(Exception e){
		}		
		return false;
	}		
	public static long date2l(String x){
		String xconst="";
		SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy/MM/dd"); 
		SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss"); 
		SimpleDateFormat dateFormat3=new SimpleDateFormat("HH:mm:ss");

		try{
		  Date date = dateFormat2.parse(x);
		  return date.getTime();
		}
		catch(Exception e){
		}
		try{
			  Date date = dateFormat1.parse(x);
			  return date.getTime();
			}
			catch(Exception e){
			}		
		try{
			  Date date = dateFormat3.parse(x);
			  return date.getTime();
			}
			catch(Exception e){
			}		
		return 0;
	}		
	public static String l2date(long x){
		String xconst="";
		SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy/MM/dd"); 
		SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss"); 
		long lx=x;
		Date d=new Date(lx);
		String dx=dateFormat2.format(d);
		return dx;
	}		
	public static boolean parseChar(String x, char[] chars, char[] charrtn, String [] rest){
		if(x==null) return false;
		if(x.length()<=0) return false;
		char c=x.charAt(0);
		for(int i=0;i<chars.length;i++){
			char d=chars[i];
			if(c==d) break;
			if(i==chars.length-1){
				return false;
			}
		}
		charrtn[0]=c;
		x=x.substring(1);
		rest[0]=x;
		return true;
	}
	public static String getUrlWithoutParameters(String url){
		int i=url.indexOf("?");
		if(i<0) return url;
		String rtn=url.substring(0,i);
		return rtn;
	}
	public static String getBetween(String x, String l, String r){
//		System.out.println("x="+x);
//		this.println("l="+l);
//		this.println("r="+r);
		int i=0;
		while(i<=0){
			i=x.indexOf(l,i);
    		if(i<0) return null;
    		if(i==0) break;
	    	if(isInStringConst(x,i)){
		    	i=i+l.length();
		    }
		}
		
		i=i+l.length();
		int j=i;
		while(j<=i){
		    j=x.indexOf(r,j);
		    if(j<0) return null;
		    if(isInStringConst(x,j)){
		    	j=j+r.length();
		    }
		}
		String rtn="";
		try{
			rtn=x.substring(i,j);
		}
		catch(Exception e){
			return null;
		}
//		System.out.println("rtn="+rtn);
		return rtn;
	}
	public static boolean isInStringConst(String x, int p){
		
		int px=0;
		int py=0;
		boolean isIn=false;
		while(px<x.length()){
			if(px>p) return false;
			char cx=x.charAt(px);
			char cy=0;
			py=px+1;
			if(cx=='"'){
				isIn=true;
				while(py<x.length()){
					cy=x.charAt(py);
					if(cy=='"'){
						if(px<p && p<py)
							return true;
						else{
							isIn=false;
							px=py;
							break;
						}
					}
					if(cy=='\\'){
						py=py+1;
					}
					py=py+1;
				}
				if(isIn)
					return true;
			}
			if(cx=='\\'){
				px=px+1;
			}
			px=px+1;
		}
		return false;
	}
	public static int sh2i(String x) {
		// TODO Auto-generated method stub
		int rtn=0;
		if(x.startsWith("0x")){
			x=x.substring("0x".length());
		}
		try{
		   rtn=Integer.parseInt(x,16);
		}
		catch(Exception e){
			System.out.println("decode error at sh2i "+e);
		}
		return rtn;
	}
}
