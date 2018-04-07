package org.yamaLab.trafficViewer;


import java.util.StringTokenizer;

public class SBUtil {

	public static boolean isInNetwork(byte[] h, byte[] na, byte[] mask){
		if(h==null) return false;
		if(na==null) return false;
		if(mask==null) return false;
		for(int i=0;i<h.length;i++){
			byte bi= (byte)(h[i] & mask[i]);
			if(bi!=na[i]) return false;
		}
		return true;
	}
	public static boolean isSameAddress(byte[] x, byte[] y){
		if(x==null) return false;
		if(y==null) return false;
		for(int i=0;i<x.length;i++){
			if(x[i]!=y[i]) return false;
		}
		return true;
	}
	public static byte[] smac2byte(String xmac){
		if(xmac==null) return null;
		byte[] count=new byte[16];
		byte[] rtn;
		int c=0;
		StringTokenizer st=new StringTokenizer(xmac,".:");
		while(st.hasMoreTokens()){
			String hx=st.nextToken();
			count[c]=(byte) ((Character.digit(hx.charAt(0), 16) << 4) 
                    + Character.digit(hx.charAt(1), 16)); 

			c++;
		}
		rtn=new byte[c];
		for(int i=0;i<c;i++) rtn[i]=count[i];
		return rtn;
	}
	public static byte[] getAddressPartOfAP(String ap){
		byte[] rtn=new byte[4];
		StringTokenizer st=new StringTokenizer(ap,".:");
		for(int i=0;i<4;i++){
			int x=(new Integer(st.nextToken())).intValue();
			rtn[i]=(byte)(0xff & x);
		}
		return rtn;
	}
	
	public static int getPortPartOfAP(String ap){
		StringTokenizer st=new StringTokenizer(ap,":");
		String dmy=st.nextToken();
		String pt=st.nextToken();
		int x=(new Integer(pt)).intValue();
		return x;
	}
	public static boolean isMatchIpV4Address(String x, String y){
//		String ax[]=new String[4];
//		String ay[]=new String[4];
		if(x==null) return false;
		if(y==null) return false;
		StringTokenizer stx=new StringTokenizer(x,".");
		if(stx==null) return false;
		StringTokenizer sty=new StringTokenizer(y,".");
		if(sty==null) return false;
		for(int i=0;stx.hasMoreElements();i++){
			String ax="",ay="";
			try{
			   ax=stx.nextToken();
			   ay=sty.nextToken();
			}
			catch(Exception e){
				return false;
			}
			if(!(ax.equals(ay))){
				if((!ax.equals("*")) && (!ay.equals("*"))){
					return false;
				}
			}
		}
		return true;
	}
	public static boolean isMatchMacAddress(String x, String y){
//		String ax[]=new String[4];
//		String ay[]=new String[4];
		if(x==null) return false;
		if(y==null) return false;
		StringTokenizer stx=new StringTokenizer(x,":");
		if(stx==null) return false;
		StringTokenizer sty=new StringTokenizer(y,":");
		if(sty==null) return false;
		for(int i=0;stx.hasMoreElements();i++){
			String ax="",ay="";
			try{
			   ax=stx.nextToken();
			   ay=sty.nextToken();
			}
			catch(Exception e){
				return false;
			}
			if(!(ax.equals(ay))){
				if((!ax.equals("*")) && (!ay.equals("*"))){
					return false;
				}
			}
		}
		return true;
	}
	public static String bytes2smac(byte[] x){
		byte ax=0;
		ax=x[0];
		String c=byte2hex(ax);
		String rtn=""+c;
		int len=x.length;
		for(int i=1;i<len;i++){
			ax=x[i];
			c=byte2hex(ax);
			rtn=rtn+":"+c;
		}
		return rtn;
	}

	public static String byte2hex(byte x){
		char ch, cl;
		String rtn;
		int hx=(x & 0x00f0)>>4;
		int lx=(x & 0x000f);
		if(hx>9) ch=(char)((byte)('a')+(hx-10)); else ch=(char)((byte)('0')+hx);
		rtn=""+ch;
		if(lx>9) ch=(char)((byte)('a')+(lx-10)); else ch=(char)((byte)('0')+lx);
		rtn=rtn+ch;
		return rtn;
	}
	public static String byteArray2hex(byte[] x) {
		int l=x.length;
		String rtn="";
		if(l<=0) return "";
		rtn=byte2hex(x[0]);
		for(int i=1;i<l;i++) {
		    rtn=rtn+" "+byte2hex(x[i]);
			if((i%32)==0) rtn=rtn+"\n";
		}
		return rtn;
	}
	public static String byteArray2hex2(byte[] x) {
		int l=x.length;
		String rtn="";
		if(l<=0) return "";
		rtn=byte2hex(x[0]);
		for(int i=1;i<l;i++) {
		    rtn=rtn+byte2hex(x[i]);
		}
		return rtn;
	}	
	public static String bytes2sip(byte[] x){
		int ax=0;
		ax=x[0];
		if(ax<0)ax=256+ax;
		String rtn=""+ax;
		int len=x.length;
		for(int i=1;i<len;i++){
			ax=x[i];
			if(ax<0) ax=256+ax;
			rtn=rtn+"."+ax;
		}
		return rtn;
	}
	static char[] asciis=new char[]{
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j',
			'k','l','m','n','o','p','q','r','s','t',
			'u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J',
			'K','L','M','N','O','P','Q','R','S','T',
			'U','V','W','X','Y','Z',
			' ','!','"','#','$','%','&','\'','(',')',
			'=','-','+','*','/','~','^','|','{','}',
			'[',']','<','>','?',';',':','.',',','@'};
	public static String showAsciiInBinary(byte[] b, int len){
		String rtn="";
		int lb=b.length;
		if(lb<len) len=lb;
		for(int i=0;i<len;i++){
			char x=(char)b[i];
            if(isInChars(x,asciis)) rtn=rtn+x;
            else rtn=rtn+'.';			
		}
		rtn=rtn+"...";
		return rtn;
	}
	public static String showAsciiInBinary(byte[] b){
		String rtn="";
		int lb=b.length;
		for(int i=0;i<lb;i++){
			char x=(char)b[i];
            if(isInChars(x,asciis)) rtn=rtn+x;
            else rtn=rtn+'.';			
            if(i%32==0)rtn=rtn+"\n";
		}
		return rtn;
	}
	
	public static boolean isInChars(char x, char[] y){
		for(int i=0;i<y.length;i++){
			if(x==y[i]) return true;
		}
		return false;
	}
	public static byte[] s2byteIp4(String x){
		 String a="";
		 byte b[] = new byte[4]; // ipv4
		 StringTokenizer st=new StringTokenizer(x,".");
		 for(int i=0;i<4;i++){
			  a=st.nextToken();
			  if(!(a.equals("*"))){
				 int ax=(new Integer(a)).intValue();
				 b[i]=(byte)(0x00ff & ax);
			  }
		 }
	     return b;
	}
	public static String addrPort(byte[] a, int p){
	    String as=bytes2sip(a);
		as=as+":";
		as=as+p;
		return as;
	}

}
