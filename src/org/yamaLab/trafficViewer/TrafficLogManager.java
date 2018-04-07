package org.yamaLab.trafficViewer;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.yamaLab.logFile.BlockedFileManager;
import org.yamaLab.trafficController.TrafficController;
import org.yamaLab.trafficController.PacketMonitorFilter;
import org.yamaLab.pukiwikiCommunicator.language.CommandReceiver;
/*
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
*/

public class TrafficLogManager implements Runnable {
	public String realstream;//?��?��?�����?��?��?��?��?��O
	public String prt;//?��?��v?��?��?��?��?��?��g?��?��R?��?��?��?��?��?��?��?��
	public int[] srcIP;//?��?��?��?��?��?��M?��?��?��?��[0]:IP?��?��A?��?��h?��?��?��?��?��?��X?��?��?��?��?��?��?��?��8bit [1]:port?��?��?��?��?��?��?��������?��r?��?��b?��?��g
	public int[] dstIP;//?��?��?��?��?��?��?��?��[0];IP?��?��A?��?��h?��?��?��?��?��?��X?��?��?��?��?��?��?��?��8bit [1]:port?��?��?��?��?��?��?��?��8bit
	public String[] IP;//[0]:?��?��?��?��?��?��?�����?��?��^?��?��?��?��?��?��v?��?��@[1]:?��?��?��?��?��?��M?��?��?��?��IP?��?��A?��?��h?��?��?��?��?��?��X?��?��@[2]:?��?��?��?��?��?��?��?��IP?��?��A?��?��h?��?��?��?��?��?��X
	public String[] detailIP;//
//	public OrgLog orgnaize;
	
	PacketMonitorFilterInterface packetFilter;
	int currentHour;
    Calendar calendar;
//	TrafficLogManager logManager;
	BlockedFileManager logFileManager;
	long firstTime=-1;
	long lastTime=0;
	long packetNumber=0;
	ParsePacketReceiver parsePacketReceiver;

	public long getFirstTime(){
		return firstTime;
	}
	public long getLatestTime(){
		return lastTime;
	}
	
	Thread me;
	
//	public TrafficLogManager(PacketMonitorFilterInterface f){
	public TrafficLogManager() {
//		this.packetFilter=f;
		calendar=Calendar.getInstance();
		currentHour=calendar.get(Calendar.HOUR);
		logFileManager=new BlockedFileManager("TempLog-"+currentHour);
		this.packetNumber=0;
		this.start();
	}
	CommandReceiver main;
	String wmessage;
//	public void logDetail(MainFrame m,PcapPacket packet, int id){
	public void logDetail(CommandReceiver m, ParsePacket p, int itf){
		main=m;
		int h=calendar.get(Calendar.HOUR);
		if(h!=currentHour){
			/*
			logFileManager.update();
			logFileManager=new BlockedFileManager("TempLog-"+h);
			*/
			currentHour=h;
//			main.clearButtonActionPerformed(null);
			main.command("click", "clearButton");
//			JScanner.getThreadLocal().setFrameNumber(0);  
			firstTime=-1;
			lastTime=0;
			packetNumber=0;
		}
//		long t=p.packet.getCaptureHeader().timestampInMillis();
		long t=p.ptime;
		if(this.firstTime<0) this.firstTime=t;
		if(t>this.lastTime) this.lastTime=t;
		/*
		if(logFileManager!=null){
		       this.logFileManager.putMessageAt(packet, this.packetNumber);
		       this.packetNumber++;
		}
		*/
		if(main!=null){
			/* */
//			if(main.mainWatch!=null)
//				this.main.mainWatch.setTermScrollBar();
				main.command("set", "TermScrollBar");
		   /* */
		}				

//		packet.scan(id);
		String rtn=packetFilter.exec(p);
		String match="";
		if(rtn!=null) match=rtn;
		String smac="", dmac="", sip="", dip="";
		int sport=0, dport=0;
		/*
		try{
		}
		catch(Exception e){
			return;
		}
		*/
		String date=p.getTimeS();
		/*
		*/
		
		p.getPayloadString();
		String[] states=p.states;
//		states[0]=p.getPayloadString();
		states[1]=date;
		int[] address=p.address;		

		/*
		wmessage="date=\""+date+"\", no="+packetNumber+", if="+itf+
		         ", smac=\""+p.getSourceMacString()+"\", dmac=\""+p.getDestinationMacString()+
//				p.getEtherString()+
		         "\", prtcl="+p.protocol+
				", sip=\""+p.getSourceIpString()+"\", dip=\""+p.getDestinationIpString()+
//		         p.getIpString()+
		         "\", sp="+p.sport+", dp="+p.dport+
		         ", sha1payload=\""+p.getSha1Payload()+"\", payloadLength=\""+p.payloadLength+"\", payload="+states[0];
		         */
//		main.writePacketMessage(wmessage);
//		if(p.sourceIpString==null||p.sourceIpString=="") {
//			p.sourceIpString="*";
//		}
		
		p.states[2]=p.protocol;
//		states[3]=IP[1];//?��?��?��?��?��?��?��?��?��?��?��?��M?��?��?��?��?��?��?��?��
        p.states[3]=p.getSourceIpString();
//		states[4]=IP[2];//?��?��?��?��?��?��?��?��?��?��?��?��M?��?��?��?��?��?��?��?��              
        p.states[4]=p.getDestinationIpString();
//		sport=(address[4]<<8)|(address[5]);
//		dport=(address[10]<<8)|(address[11]);
		p.states[5]=""+p.sport;
		p.states[6]=""+p.dport;
        
        p.states[7]=p.getSourceMacString();
        p.states[8]=p.getDestinationMacString();		
		
		if(p.protocol.equals("tcp")||p.protocol.equals("udp")||
				p.protocol.equals("tcpv6")||p.protocol.equals("udpv6")||
				p.protocol.equals("icmpv6")||p.protocol.equals("icmp")||
				p.protocol.equals("ip-N/A")||p.protocol.equals("ipv6Destination")||
				p.protocol.equals("ipv6-N/A")||p.protocol.equals("icmpv6hopbyhop") ) {
//		    main.command("writePacketMessage", ""+itf+" "+p.getSourceMacString()+" "+p.getSourceIpString()+" "+
//				              p.getDestinationMacString()+" "+p.getDestinationIpString()+" "+wmessage);
			parsePacketReceiver.receive("packetMessage", packetNumber, itf, p);
		}
		this.orgLog(packetNumber, t,states, address,match,p.protocolBit);		
		packetNumber++;
	}
	VisualTrf vt;
//	private MainWatch mainWatch;
	private byte[] netAddr;
	private byte[] netMask;
	
	
	private void orgLog(long fn, long lt,String[] state, int[] address, String match,int protocolBit){
/*
		if(main!=null){
			if(main.mainWatch!=null){
				mainWatch=main.mainWatch;
			}
		}
		*/
		int srcIpaunder = address[3];
		int dstIpaunder = address[9];
		int srcPortunder = address[5];
		int dstPortunder = address[11];
//		this.state = state;
//		this.address = address;
		long frameNumber=fn;

//		if(mainWatch==null) return;
//		byte[] netAddr=this.main.getCurrentLanInterfaceNetworkAddr();
//		byte[] netMask = this.main.getCurrentLanInterfaceNetmask();
		/*
		byte[] netAddr=this.main.getCurrentWanInterfaceNetworkAddr();
		byte[] netMask = this.main.getCurrentWanInterfaceNetmask();
		*/
		if(netAddr==null) {
		    String sNetAddr=this.main.command("get", "wanInterfaceNetworkAddr");
		    netAddr=SBUtil.s2byteIp4(sNetAddr);
		}
		if(netMask==null) {
	        String sNetMask=this.main.command("get", "wanInterfaceNetmask");
		    netMask=SBUtil.s2byteIp4(sNetMask);
		}
		
		byte[] destinAddr=new byte[4];
		byte[] sourceAddr=new byte[4];
		destinAddr[0]=(byte)address[6]; destinAddr[1]=(byte)address[7]; 
		destinAddr[2]=(byte)address[8]; destinAddr[3]=(byte)address[9];
		sourceAddr[0]=(byte)address[0]; sourceAddr[1]=(byte)address[1]; 
		sourceAddr[2]=(byte)address[2]; sourceAddr[3]=(byte)address[3];
		int fromTo=0;
//		System.out.print("isInTheNet s-addr="+xsaddr+" d-addr="+xdaddr+" netaddr="+xnetAddr);

		if(isInTheNet(sourceAddr,netAddr,netMask)){ // is destenation Ip address is local?
		    if(isInTheNet(destinAddr,netAddr,netMask)){ // is source Ip Address is local?
			   fromTo=4;
		    }
		    else{
		    	fromTo=1;
		    }
		}
		else{
			fromTo=2;
		}
		
		
//		System.out.println(" fromTo="+fromTo);
		
		/*
		if(checkExistSrc(srcIpaunder,srcPortunder) == true){
			vt = new VisualTrf(main,frameNumber,lt,state,address,2);
		    mainWatch.vtraffic[srcIpaunder][srcPortunder] = vt; 
			System.out.println("[  "+ srcIpaunder +"  "+srcPortunder+"] ?��?��?��?��?��?��?��?��?��?��?�����?��?��?�����?��?��?��?��?��?��?��?��B");
			
		}
		else{
			mainWatch.vtraffic[srcIpaunder][srcPortunder].coutup(frameNumber,lt,state,2);
		}
		*/
		if(fromTo==1||fromTo==4){
		  if(checkExistDst(dstIpaunder,dstPortunder) == true){
			vt =new VisualTrf(main,frameNumber,lt,state,address,fromTo,match,protocolBit);
// 		    mainWatch.vtraffic[dstIpaunder][dstPortunder] = vt;
// 		    vt.setMainWatch(mainWatch);
			System.out.println("new point [  "+ dstIpaunder +"  "+dstPortunder+"] "+state[2]);
		  }
		  else{
//			mainWatch.vtraffic[dstIpaunder][dstPortunder].coutup(frameNumber,lt,state,fromTo,match,protocolBit);
		  }
		}
		else{
		  if(checkExistDst(srcIpaunder,srcPortunder) == true){
			vt =new VisualTrf(main,frameNumber,lt,state,address,fromTo,match,protocolBit);
//		    mainWatch.vtraffic[srcIpaunder][srcPortunder] = vt;
//			vt.setMainWatch(mainWatch);
			System.out.println("new point [  "+ srcIpaunder +"  "+srcPortunder+"] "+state[2]);
		  }
		  else{
//			mainWatch.vtraffic[srcIpaunder][srcPortunder].coutup(frameNumber,lt,state,fromTo,match,protocolBit);
		  }
			
		}
			
	}
	private boolean isInTheNet(byte[] addr, byte[] netAddr, byte[] netMask){
		/*
		String xaddr=FormatUtils.ip(addr);
		String xnetAddr=FormatUtils.ip(netAddr);
		String xnetMask=FormatUtils.ip(netMask);
		System.out.println("isInTheNet addr="+xaddr+" netaddr="+xnetAddr+" netMask="+xnetMask);
		*/
		if(addr==null) return false;
		for(int i=0;i<addr.length;i++){
			byte b=(byte)(addr[i]&netMask[i]);
			if(b!=netAddr[i]) return false;
		}
		return true;
	}
	/*
	public int custom8bit(int i){
		int b1 = i;
		int under8 = 255;
		int eight = b1 & under8;
		return eight;
	}
	*/
	
	public boolean checkExistSrc(int i,int p){
		boolean b;
		/*
		if(mainWatch==null)return false;
		if (mainWatch.vtraffic[i][p] == null){
			b = true;
			return b ;
		}
		else {
			b = false;
			return b;
		}
		*/
		return false;
	}
	public boolean checkExistDst(int i,int p){
		boolean b;
		/*
		if(mainWatch==null) return false;
		if (mainWatch.vtraffic[i][p]== null){
			b =true;
			return b;
		}
		else{
			b = false;
			return b;
		}
		*/
		return false;
	}
	public void updateLogFileManager(){
		this.logFileManager.update();
	}
	/*
	public void setMainWatch(MainWatch mw) {
		this.mainWatch=mw;
	}
	*/
	public void setMain(CommandReceiver m) {
		this.main=m;
	}
	public void setMonitorFilter(PacketMonitorFilterInterface f) {
		this.packetFilter=f;
	}
	public void setParsePacketReceiver(ParsePacketReceiver ppr) {
		parsePacketReceiver= ppr;
	}
	
	class ParsePacketWithInterfaceNo{
		public ParsePacket pp;
		public int interfaceNo;
	}
	Vector<ParsePacketWithInterfaceNo> packetQueue=new Vector();		
	public void logging(ParsePacket pp, int nif) {
		ParsePacketWithInterfaceNo ppn=new ParsePacketWithInterfaceNo();
		ppn.pp=pp;
		ppn.interfaceNo=nif;
		packetQueue.add(ppn);
	}
	public void run() {
		// TODO Auto-generated method stub
		while(me!=null) {
  	    	if(packetQueue.size()>0) {
  	    	   ParsePacketWithInterfaceNo x=packetQueue.remove(0);
  	    	   if(x!=null)
  		          logDetail(main, x.pp, x.interfaceNo);
  	    	}
  	    	try {
  	    		Thread.sleep((long)2);
  	    	}
  	    	catch(Exception e) {
  	    		System.out.println("TrafficLogManager.run.sleep exception:"+e);
  	    	}
		}
		
	}
	public void start() {
		if(me==null) {
			me=new Thread(this,"TrafficLogManager");
			me.start();
		}
	}
	public void stop() {
		me=null;
	}
}

