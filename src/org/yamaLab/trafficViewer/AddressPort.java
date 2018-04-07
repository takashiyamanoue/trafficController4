package org.yamaLab.trafficViewer;

public class AddressPort {
	public byte [] addr;
	public int port;
	public AddressPort(byte[] a,int p){
		int l=a.length;
		addr=new byte[l];
		for(int i=0;i<l;i++) addr[i]=a[i];
		port=p;
	}
	@Override
	public boolean equals(Object o){
		if(o==this)return true;
		try{
			AddressPort y=(AddressPort)o;
		    int lax=this.addr.length;
		    int lay=y.addr.length;
		    if(lax!=lay) return false;
		    for(int i=0;i<lax;i++){
			    if(this.addr[i]!=y.addr[i]) return false;
		    }
		    if(this.port!=y.port) return false;
		    return true;
		}
		catch(Exception e){
			return false;
		}
	}
	@Override
	public int hashCode(){
		int rtn=0;
		if(addr==null) return port;
		int l=addr.length;
		byte al=addr[l-1];
		byte al2=addr[l-2];
		rtn=((((((0xff & addr[0])<<8)|(0xff & al2))<<8)|(0xff & al))<<8)|(0xff & port);
		return rtn;
	}
	public String toString(){
		return SBUtil.addrPort(addr, port);
	}

}
