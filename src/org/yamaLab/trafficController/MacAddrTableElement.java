package org.yamaLab.trafficController;
import org.pcap4j.util.MacAddress;
import org.yamaLab.trafficViewer.MacAddressKey;

public class MacAddrTableElement {
//	public String macAddress;
	public MacAddressKey macAddress;
	public String ioPort;
	private boolean hasAccess=false;	
	public synchronized void setHasAccess(boolean x) {
		hasAccess=x;
	}
	public boolean hasAccess() {
		return this.hasAccess;
	}
}
