package org.yamaLab.trafficController;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
public class TrafficHistory {
	HashMap <String,Vector<String>> dhcpHash=new HashMap(); 
	int maxHistory=20;
	public void setMaxHistory(int x) {
		maxHistory=x;
	}
    public TrafficHistory() {
    	dhcpHash=new HashMap();
    }
    public void putMacDhcpRecord(String mac, String record) {
    	if(mac==null) return;
    	if(record==null) return;
    	Vector<String> xlog=dhcpHash.get(mac);
    	if(xlog==null) {
    		xlog=new Vector();
    	}
    	else {
    		if(xlog.size()>10) {
    			xlog.remove(0);
    		}
    	}
		xlog.add(record);
    	dhcpHash.put(mac, xlog);
    }
    public Vector serialize() {
    	Vector<String> rtn=new Vector();
    	for(String key:dhcpHash.keySet()) {
    		Vector<String> log=dhcpHash.get(key);
    		if(log!=null) {
    		  for(int i=0;i<log.size();i++) {
    			  String line=log.elementAt(i);
    			  rtn.add(line);
    		  }
    		}
    	}
    	return rtn;
    }
}
