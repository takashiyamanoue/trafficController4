package org.yamaLab.pukiwikiCommunicator.controlledparts;

public class PcapPacket {
	private int time;
	private String head;
	private byte[] data;
	private int length;
	public PcapPacket(){
		head="";
		data=new byte[0];
		length=0;
	}
	public PcapPacket(String h, byte[] d){
		this.head=h;
		this.data=d;
		this.length=d.length;
	}
	public void setTime(int x){
		this.time=x;
	}
	public int getTime(){
		return time;
	}
	public String getHead(){
		return head;
	}
	public byte[] getData(){
		return data;
	}
	public void setHead(String h){
		this.head=h;
	}
	public void setData(byte[] d){
		this.data=d;
		this.length=d.length;
	}
	public void setData(byte[] d,int l){
		this.data=d;
		this.length=l;
	}
	public int getDataLength(){
		return this.length;
	}
	public void setDataLength(int x){
		this.length=x;
	}
	public byte[] getBytes(){
        byte[] hb=head.getBytes();
        int hl=hb.length;
        byte [] rtn=new byte[4+4+hl+4+length];
        rtn[0]=(byte)((hl>>24) & 0x000000ff);
        rtn[1]=(byte)((hl>>16) & 0x000000ff);
        rtn[2]=(byte)((hl>>8)  & 0x000000ff);
        rtn[3]=(byte)(hl & 0x000000ff);
        for(int i=0;i<hl;i++)
        	rtn[i+4]=hb[i];
        rtn[4+hl]=(byte)((length>>24) & 0x000000ff);
        rtn[4+hl+1]=(byte)((length>>16) & 0x000000ff);
        rtn[4+hl+2]=(byte)((length>>8) & 0x000000ff);
        rtn[4+hl+3]=(byte)(length & 0x000000ff);
        for(int i=0;i<length;i++){
        	rtn[4+hl+4+i]=data[i];
        }
        return rtn;
	}
	public void setFromBytes(byte[] x){
		int slen=0;
		for(int n=0;n<4;n++){
			byte c=x[n];
			slen=slen<<8;
			slen=slen | (c & 0x000000ff);
		}
		
		this.head=new String(x,4,slen);
		this.length=0;
		for(int n=0;n<4;n++){
			byte c=x[4+slen+n];
			this.length=this.length<<8;
			this.length=this.length | (c & 0x000000ff);
		}
		if(this.length==0){
			this.data=null;
		}
		else{
            this.data=new byte[this.length];
            for(int i=0;i<this.length;i++){
        	    this.data[i]=x[4+slen+4+i];
            }
		}
	}
	public void copyData(byte[] x, int l){
		byte[] a=new byte[l];
		for(int i=0;i<l;i++){
			a[i]=x[i];
		}
		this.data=a;
		this.length=l;
	}

}
