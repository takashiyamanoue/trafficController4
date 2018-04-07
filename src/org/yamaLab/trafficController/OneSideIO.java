package org.yamaLab.trafficController;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JTextArea;

//import org.jnetpcap.packet.JMemoryPacket;
//import org.jnetpcap.packet.PcapPacket;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.LinkLayerAddress;
import org.pcap4j.util.MacAddress;

import org.yamaLab.logFile.BlockedFileManager;
import org.yamaLab.trafficViewer.ParsePacket;
import org.yamaLab.trafficViewer.TrafficLogManager;
import org.yamaLab.pukiwikiCommunicator.language.CommandReceiver;

public class OneSideIO implements Runnable, ForwardInterface
{
	PcapHandle pcap;
	Thread me=null;
	
	// tcpdump?øΩ?ΩΩ?æåÁ¢∫?øΩ?ΩΩF?øΩ?ΩΩ?æå„Éï?øΩ?ΩΩ?øΩ?ΩΩ?øΩ?ΩΩ[?øΩ?ΩΩ?øΩ?ΩΩ
//	public JScrollPane tcpdump_log;
//	private JScrollPane scroll; //tcpdump?øΩ?ΩΩ?æåÂ?∫?øΩ?ΩΩ?æçÔº?ScrollPane)
		
	String line;
	String rn ="\n";
//	LogManager logManager;
	
//	Ip4 ip = new Ip4();
	IpV4Packet ipv4=null;
//	Ethernet eth = new Ethernet();
	EthernetPacket eth= null;
	TrafficController main;
	byte [] myIpAddr;
//	PacketMonitorFilter packetFilter;
//	BlockedFileManager logFileManager;
	

	/***************************************************************************
	 * Third - we must map pcap's data-link-type to jNetPcap's protocol IDs.
	 * This is needed by the scanner so that it knows what the first header in
	 * the packet is.
	 **************************************************************************/
	int id ;
	int currentHour;
    Calendar calendar=Calendar.getInstance();
    PacketFilter forwardFilter;
//    PcapIf myIf;
    PcapNetworkInterface myIf;
//    byte[] ifMac;
    ArrayList <LinkLayerAddress> ifMac;
	public OneSideIO(TrafficController m,PcapNetworkInterface pif, PcapHandle p,PacketFilter fl, byte[] ip){
		main=m;
		myIf=pif;
		try{
// 	        ifMac = myIf.getHardwareAddress();
			ifMac = myIf.getLinkLayerAddresses();
		}
		catch(Exception e){
			ifMac=null;
		}
		forwardFilter=fl;
		forwardFilter.setReturnInterface(this);
		pcap = p;
//		id= JRegistry.mapDLTToId(pcap.datalink());
		this.myIpAddr=ip;
	}
	public void setNewPcap(PcapHandle p){
		pcap = p;
//		id= JRegistry.mapDLTToId(pcap.datalink());
	}

	/* */
//	Queue<PcapPacket> queue = new ArrayBlockingQueue<PcapPacket>(100);  	
	Queue<Packet> queue = new ArrayBlockingQueue<Packet>(100);
//    public Queue<PcapPacket> getPacketQueue(){
	/**/
	public Queue<Packet> getPacketQueue(){
    	return queue;
    }
    /**/
	private class MyPacketListener implements PacketListener{
		public MyPacketListener() {
		}
		public void gotPacket(Packet packet) {
			try {
//			   if(!isToBeForwarded(packet)) return;
//			   queue.offer(packet); //?
			   if(forwardFilter!=null) {
				   forwardFilter.process(packet,main.macAddrTable);
			   }
			}
			catch(Exception e) {
				System.out.println("id="+id+" if="+forwardFilter.getLabel()+" OneSideIO.run.getPacket error:"+e.toString());
			}
		}
	}
	public void run(){
//		PcapPacketHandler<Queue<PcapPacket>> handler = new PcapPacketHandler<Queue<PcapPacket>>() {  
//		  public synchronized void nextPacket(PcapPacket packet, Queue<PcapPacket> queue) {  
/*
		public synchronized void nextPacket(Packet packet, Queue<Packet> queue) {
			  try{
//			  PcapPacket permanent = new PcapPacket(packet);
				  Packet permanent = new Packet(packet);
			  if(!isFromOtherIf(packet)) return;
//			  queue.offer(permanent);
			  if(forwardFilter!=null)
				  forwardFilter.process(permanent);
			  }
			  catch(Exception e){
				  System.out.println("OneSideIO.run.nextPacket error: "+e);
			  }
		  }
		} ;
		*/
		MyPacketListener mypc=new MyPacketListener();
		try{
//		 rtn=pcap.loop(-1, handler, queue);  
			pcap.loop(-1, mypc);
		}
		catch(Exception e){
			System.out.println("ioPort:"+this.forwardFilter.getLabel()+",OneSideIO.run pcap.loop error: "+e);
			pcap.close();
			System.out.println("exitting loop, please start again.");
			return;
		}
		System.out.println("exiting pcap.loop of if-"+interfaceNo);    
		pcap.close();  
	}
	/* */
    /* 
	Queue<JMemoryPacket> queue = new ArrayBlockingQueue<JMemoryPacket>(100);  	
    public Queue<JMemoryPacket> getPacketQueue(){
    	return queue;
    }
	
	public void run(){
		PcapPacketHandler<Queue<JMemoryPacket>> handler = new PcapPacketHandler<Queue<JMemoryPacket>>(){
          public void nextPacket(PcapPacket packet, Queue<JMemoryPacket> queue) {
			  byte[] jpb=new byte[2000];
			  JMemoryPacket jp =  new JMemoryPacket(jpb);
			  jp.transferFrom(packet);
			  if(!isFromOtherIf(packet)) return;

			  queue.offer(jp);
			}
		} ;
		int rtn=0; 
		try{
		 rtn=pcap.loop(-1, handler, queue);  
		}
		catch(Exception e){
			System.out.println("OneSideIO.run pcap.loop error: "+e);
			pcap.close();
			System.out.println("exitting loop, please start again.");
			return;
		}
		System.out.println("exiting pcap.loop of if-"+interfaceNo+" due to "+rtn);    
		pcap.close();  
	}
	*/
	public void start() {
		if(me==null){
			me=new Thread(this,"OneSideIO-"+this.interfaceNo);
			me.setPriority(7);
			me.start();
		}
	}
	
	public void stop(){
		me=null;
		System.out.println("WanSideIO loop stop");
		try{
//			this.pcap.breakloop();
			this.pcap.breakLoop();
		    this.pcap.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
    public void sendPacketPP(ParsePacket p){
    	if(p==null) return;
    	if(!p.succeeded) return;
    	if(p.packet==null) return;
//    	JMemoryPacket pp=new JMemoryPacket(p.packet);
//    	byte[] b=p.packet.getByteArray(0, 2000);
//    	byte[] b=pp.getByteArray(0, pp.getTotalSize());
//    	this.sendByte(b);
    	this.sendPcapPacket(p.packet);
	    if(logManager!=null) {
            logManager.logging(p, this.interfaceNo);
	    }
    }
    /*
    public void sendPacketJM(JMemoryPacket p, ParsePacket m){
    	if(p==null) return;
    	byte[] b= p.getByteArray(0, p.getTotalSize());
    	this.sendByte(b);
    	if(m==null) return;
    	if(!m.succeeded) return;
	    if(logManager!=null)
		      synchronized(logManager){
//		    	  m.packet=pp;
		    	  logManager.logDetail(main, m, interfaceNo);
		     }    	
    }
    */
//    public void sendPacket(PcapPacket p, ParsePacket m){
	Vector<ParsePacket> packetQueue2=new Vector();    
    public void sendPacket(Packet p, ParsePacket m) {
    	if(p==null) return;
//    	PcapPacket pp=new PcapPacket(p);
//    	JMemoryPacket pp=new JMemoryPacket(p);
//    	byte [] b=p.getByteArray(0, p.getTotalSize());
//    	this.sendByte(b);
    	this.sendPcapPacket(p);
    	if(m==null) return;
    	if(!m.succeeded) return;
	    if(logManager!=null) {
//		      synchronized(logManager){
		    if(logManager!=null) {
	            logManager.logging(m, this.interfaceNo);
		    }
		}    	
    }
    public void sendByte(byte[] b){
//    	ByteBuffer bb=ByteBuffer.wrap(b);
    	synchronized(pcap){
//    		if(this.pcap.sendPacket(bb)!=Pcap.OK){
//    	    	System.out.println("error @ sendPacket(PcapPacket), if="+interfaceNo);
//    	    }
    		try {
    			this.pcap.sendPacket(b);
    		}
    		catch(PcapNativeException e) {
    			System.out.println("PcapNativeException @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}
    		catch(NotOpenException e) {
    			System.out.println("NotOpenException @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}
    		catch(NullPointerException e) {
    			System.out.println("Send Null Packet @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}	
    	}     	
    }
//    public void sendPcapPacket(PcapPacket p){
    public void sendPcapPacket(Packet p) {
//    	synchronized(pcap){
//    		if(this.pcap.sendPacket(p)!=Pcap.OK){
//    	    	System.out.println("error @ sendPacket(PcapPacket), if="+interfaceNo);
//    	    }
    		try {
    			this.pcap.sendPacket(p);
    		}
    		catch(PcapNativeException e) {
    			System.out.println("PcapNativeException @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}
    		catch(NotOpenException e) {
    			System.out.println("NotOpenException @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}
    		catch(NullPointerException e) {
    			System.out.println("Send Null Packet @ sendPacket(PcapPacket,if="+interfaceNo+") e="+e);
    		}
//    	}     	
    }
//   public boolean isFromOtherIf(PcapPacket p){

	TrafficLogManager logManager;
	public void setLogManager(TrafficLogManager m){
		logManager=m;
	}
    int interfaceNo;
    public void setInterfaceNo(int i){
    	interfaceNo=i;
    }
	public byte[] getIPAddr(){
		return this.myIpAddr;
	}
//	@Override
	public void setIpMac(byte[] ip, byte[] mac) {
		// TODO Auto-generated method stub
		this.forwardFilter.setIpMac(ip,mac);
	}
	/*
	public void sendPacketJM(JMemoryPacket x, ParsePacket m) {
		// TODO Auto-generated method stub
		
	}
	public void sendPacket(PcapPacket x, ParsePacket m) {
		// TODO Auto-generated method stub
		
	}
*/
	/*
	public Vector<InetAddress> getBroadcastAddresses() {
		Vector<InetAddress> addresses=new Vector();
		List<PcapAddress> ll=myIf.getAddresses();
        Iterator it = ll.iterator();
        while (it.hasNext()) {
            PcapAddress address = (PcapAddress) it.next();
            //System.out.println("Found address: " + address);
            if(address == null) continue;
            InetAddress broadcast = address.getBroadcastAddress();
            if(broadcast != null) {
            	addresses.add(broadcast);
            }
        }
        return addresses;
	}
	*/
	public Vector<String> dhcpList =new Vector();
	
	public class DhcpInformation{
		public boolean dhcp;
		public String dhcpKind;
		int kind;
		byte[] userIP=new byte[4];
		byte[] userMac=new byte[6];
		byte[] serverIP=new byte[4];
		byte[] serverMac=new byte[6];
		ParsePacket parsePacket;
		public DhcpInformation(ParsePacket p) {
			if(!p.protocol.equals("udp")) {
				dhcp=false;
				return;
			}
			if(p.sport==68 || p.sport==67) {
				if(p.dport==67||p.dport==68) {
					dhcp=true;
				}
				else {
					dhcp=false;
				}
			}
			else {
				dhcp=false;
			}
			if(!dhcp) return;
			
			Packet payload=p.payload;
		   	byte[] bytePayload=payload.getRawData();
			try {
			   this.kind=(0xff & bytePayload[242]);
			}
			catch(Exception e) {
				System.out.println("DhcpInformation-Header section parse error:"+e.toString());
			}
			for(int i=0;i<6;i++) {
				userMac[i]=bytePayload[246+i];
			}				
			
			if(kind==1) {
				this.dhcpKind="discover";
			}
			else
			if(kind==2) {
				this.dhcpKind="offer";
			}
			else
			if(kind==3) {
				this.dhcpKind="request";
			}
			else
			if(kind==4) {
				this.dhcpKind="decline";
			}
			else
			if(kind==5) {
				this.dhcpKind="pack";
			}
			if(kind==6) {
				this.dhcpKind="pnak";
				
			}
		}
		public String getUserIP() {
			InetAddress ua;
			try {
			    ua=InetAddress.getByAddress(userIP);
				String uas=ua.getHostAddress();
				return uas;			    
			}
			catch(Exception e) {
				System.out.println("dhcpInformation.getUserIP error:"+e);
				return "na";
			}
		}
	}	
}
