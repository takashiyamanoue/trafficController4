package org.yamaLab.trafficViewer;

import java.util.Objects;

import org.pcap4j.util.MacAddress;

public final class MacAddressKey {
	private final MacAddress macAddress;
	public MacAddressKey(MacAddress x) {
		macAddress=x;
	}
	
    public boolean equals(Object obj) {
    	if(obj instanceof MacAddressKey) {
    		MacAddressKey key=(MacAddressKey)obj;
    		byte[] myAddress=macAddress.getAddress();
    		byte[] keyAddress=key.macAddress.getAddress();
    		if(myAddress.length!=keyAddress.length) return false;
    		for(int i=0;i<myAddress.length;i++) {
    			if(myAddress[i]!=keyAddress[i]) return false;
    		}
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    public int hashCode() {
//    	String x=this.macAddress.toString();
    	int hashcode=macAddress.hashCode();
    	return hashcode;
    }
    
}
