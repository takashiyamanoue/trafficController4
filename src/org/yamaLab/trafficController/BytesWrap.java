package org.yamaLab.trafficController;

public class BytesWrap {
   byte[] bytes;
   public BytesWrap(byte[] b){
	   bytes=b;
   }
   @Override
   public boolean equals(Object o){
	   if(o==this)return true;
	   try{
	      byte[] x=(byte [])o;
	      if(bytes.length!=x.length) return false;
	      for(int i=0;i<bytes.length;i++){
		     if(bytes[i]!=x[i]) return false;
	      }
	      return true;
	   }
	   catch(Exception e){
		   return false;
	   }
   }
   @Override
   public int hashCode(){
	   if(bytes==null) return 0;
	   int l=bytes.length;
	   if(l==1) return bytes[0];
	   if(l==2) return ((0xff & bytes[0])<<8)|(0xff & bytes[1]);
	   if(l==3) return ((((0xff & bytes[0])<<8)|(0xff & bytes[1]))<<8)|(0xff & bytes[2]);
	   return ((((((0xff & bytes[0])<<8)|(0xff & bytes[1]))<<8)|(0xff & bytes[l-2]))<<8)|(0xff & bytes[l-1]);
   }
}
