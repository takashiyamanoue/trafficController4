package org.yamaLab.trafficController;

import java.util.StringTokenizer;
import java.util.Vector;

import org.pcap4j.util.MacAddress;
import org.yamaLab.trafficViewer.ParsePacket;

public class EachHostInformation {

	public final int maxHistory=1000;
	public MacAddress macAddress;
	public String nif;
	public String ioPort;
	public Vector<String> ipaddresses;
	public Vector<TimeAndTraffic> history;
	public EachHostInformation() {
		ipaddresses=new Vector();
		history=new Vector();
	}
	public void addIP(String a) {
	    int ipnum=this.ipaddresses.size();
	    boolean found=false;
	    for(int i=0;i<ipnum;i++) {
	    	String ipx=this.ipaddresses.elementAt(i);
	    	if(ipx.equals(a)) {
	    		found=true;
	    		break;
	    	}
	    }
	    if(!found) {
	    	this.ipaddresses.add(a);
	    }		
	}
	public boolean isInIPList(String a) {
	    int ipnum=this.ipaddresses.size();
	    for(int i=0;i<ipnum;i++) {
	    	String ipx=this.ipaddresses.elementAt(i);
	    	if(ipx.equals(a)) {
	    		return true;
	    	}
	    }
	    return false;
	}
	public void setNif(String h) {
		this.nif=h;
	}
	/*
	public void addHistory(String h) {
		int num=history.size();
		if(num>maxHistory)
			history.remove(0);
        long t=System.currentTimeMillis();
        TimeAndTraffic e=new TimeAndTraffic();
        e.time=t;
        e.traffic=h;
        history.add(e);
	}
	*/
	public void addHistory(long no, int nif, ParsePacket pp) {
		int num=history.size();
		if(num>maxHistory)
			history.remove(0);
         TimeAndTraffic e=new TimeAndTraffic();
        e.no=no;
        e.nif=nif;
        e.parsePacket=pp;
        history.add(e);
	}	
	public String getIPs() {
		int ipsize=this.ipaddresses.size();
		if(ipsize<=0) return "";
		int i=0;
		String rtn=this.ipaddresses.elementAt(i);
		i++;
		while(i<ipsize) {
			rtn=rtn+";";
			rtn=rtn+this.ipaddresses.elementAt(i);
			i++;
		}
		return rtn;
	}
}
