package org.yamaLab.trafficController;

import java.util.StringTokenizer;

import org.yamaLab.pukiwikiCommunicator.language.Util;
import org.yamaLab.trafficViewer.ParsePacket;

public class TimeAndTraffic {
//	public long time;
	public long no;
	public int nif;
	private String traffic;
	public ParsePacket parsePacket;
	public long getTime() {
		return parsePacket.ptime;
	}
	/*
	 * traffic
	wmessage="#"+packetNumber+" if="+itf+" date="+date+" "+
	         p.sourceMacString+" -> "+p.destinationMacString+
	         " prtcl="+p.protocol+" "+p.sourceIpString+" -> "+p.destinationIpString+
	         " "+p.sport+"->"+p.dport+" "+states[0];
	*/
	public String getTraffic() {
		String wmessage="date=\""+parsePacket.getTimeS()+"\", no="+no+", if="+nif+
		         ", smac=\""+parsePacket.getSourceMacString()+"\", dmac=\""+parsePacket.getDestinationMacString()+
//				p.getEtherString()+
		         "\", prtcl="+parsePacket.protocol+
				", sip=\""+parsePacket.getSourceIpString()+"\", dip=\""+parsePacket.getDestinationIpString()+
//		         p.getIpString()+
		         "\", sp="+parsePacket.sport+", dp="+parsePacket.dport+
		         ", sha1payload=\""+parsePacket.getSha1Payload()+"\", payloadLength="+parsePacket.payloadLength+
		         ", payload="+parsePacket.getPayloadString();
		return wmessage;		
	}
	
	public String getDestinationIP() {
		/*
		StringTokenizer st=new StringTokenizer(traffic,",");
		String[] rest=new String[1];
		while(st.hasMoreTokens()) {
			String tkn=st.nextToken();
			tkn=Util.skipSpace(tkn);
			if(Util.parseKeyWord(tkn, "dip=", rest)) {
				return rest[0];
			}
		}
		return "*";
		*/
		return parsePacket.getDestinationIpString();
	}
	public String getDestinationMac() {
		/*
		StringTokenizer st=new StringTokenizer(traffic,",");
		String[] rest=new String[1];
		while(st.hasMoreTokens()) {
			String tkn=st.nextToken();
			tkn=Util.skipSpace(tkn);
			if(Util.parseKeyWord(tkn, "dmac=", rest)) {
				return rest[0];
			}
		}
		return "*";
		*/
		return parsePacket.getDestinationMacString();
	}	
	
	public String getSourceIP() {
		/*
		StringTokenizer st=new StringTokenizer(traffic,",");
		String[] rest=new String[1];
		while(st.hasMoreTokens()) {
			String tkn=st.nextToken();
			tkn=Util.skipSpace(tkn);
			if(Util.parseKeyWord(tkn, "sip=", rest)) {
				return rest[0];
			}
		}
		return "*";
		*/
		return parsePacket.getSourceIpString();
	}	
	public boolean isIP() {
		if(this.parsePacket==null) return false;
		if(parsePacket.ipv4!=null) return true;
		if(parsePacket.ipv6!=null) return true;
		return false;
	}
}
