package org.yamaLab.trafficViewer;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.EthernetPacket.EthernetHeader;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IcmpV4InformationRequestPacket;
import org.pcap4j.packet.IcmpV6CommonPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6ExtDestinationOptionsPacket;
import org.pcap4j.packet.IpV6ExtHopByHopOptionsPacket;
import org.pcap4j.packet.IpV6ExtOptionsPacket;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.util.MacAddress;

/*
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.util.resolver.IpResolver;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
*/
 
public class ParsePacket {
//	static public Ip4 ip = new Ip4();
	public IpV4Packet ipv4=null;
//	static public Ethernet eth = new Ethernet();
	public IpV6Packet ipv6=null;
	
	public EthernetPacket eth=null;
//	public PcapHeader hdr = new PcapHeader(JMemory.POINTER);
//	public JBuffer buf = new JBuffer(JMemory.POINTER);
//	static public Tcp tcp = new Tcp();
	public TcpPacket tcp= null;
//    static public Udp udp = new Udp();
	public UdpPacket udp = null;
//    static public Arp arp = new Arp();
	public ArpPacket arp = null;
//    static public Icmp icmp = new Icmp();
	public IcmpV4CommonPacket icmp = null;
	public IcmpV6CommonPacket icmpV6Common = null;
	public IpV6ExtHopByHopOptionsPacket ipv6Hopbyhop=null;
	public IpV6ExtDestinationOptionsPacket ipv6Destination=null;
	
//	public final Http http = new Http();
	private String sourceMacString="";
	private String destinationMacString="";
	private String sourceIpString="";
	private String destinationIpString="";
	private String etherString="";
	public String protocol="";
	public int protocolBit=0x000;
//	public byte[] payload;
	public Packet payload;
	private String ipString="";
	private String payloadString="";
	private String l4String="";
	public long ptime=0;
	private String ptimes="";
	/**/
	public int sport;
	public int dport;
	/**/
	
	private TcpPacket.TcpHeader tcph;	
    private ArpPacket.ArpHeader arph;
	private String sha1Payload="";
	/*
	public TcpPort sport;
	public TcpPort dport;
	*/
	public int[] address = new int[14];
	public String[] states = new String[]{"","","","","",
		                                     "","","","",""};
	public boolean succeeded;
	
	public MacAddress macDestAddress=null;
	public MacAddress macSrcAddress=null;
	
	public InetAddress ipSrcAddr= null;
	public InetAddress ipDstAddr=null;
	public Inet6Address ip6SrcAddr= null;
	public Inet6Address ip6DstAddr=null;
	public int payloadLength;
	
	public String getSha1Payload() {
		if(this.sha1Payload!=null) {
			if(!this.sha1Payload.equals("")){
				return this.sha1Payload;
			}
		}
		if(payload==null) return "no-payload";
		try {
		   MessageDigest digest = MessageDigest.getInstance("SHA-1");
		   byte[] payraw=payload.getRawData();
		   byte[] sha1=digest.digest(payraw);
		   this.sha1Payload=SBUtil.byteArray2hex2(sha1);
		   return sha1Payload;
		}
		catch(Exception e) {
			System.out.println("ParsePacket.getSha1Payload error:"+e);
		}
		return "payload-error";
	}
	
	public String getTimeS() {
		if(ptimes!=null) {
			if(!ptimes.equals("")) {
				return ptimes;
			}
		}
		try{
//			ptime=packet.getCaptureHeader().timestampInMillis();
			DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
		    ptimes=""+df.format(new Date(ptime));
		    return ptimes;
		}
		catch(Exception e){
			System.out.println("ParsePacket error get timestamp: "+e);
			return "*";			
		}		
	}
	
	public String getSourceMacString() {
		if(this.sourceMacString!=null) {
		   if(!this.sourceMacString.equals("")) {
			   return this.sourceMacString;
		   }
		}
		try {
			this.sourceMacString=macSrcAddress.toString();
			return this.sourceMacString;
		}
		catch(Exception e) {
			return "*";
		}
	}
	
	public String getDestinationMacString() {
		if(this.destinationMacString!=null) {
		   if(!this.destinationMacString.equals("")) {
			   return this.destinationMacString;
		   }
		}
		try {
			this.destinationMacString=macDestAddress.toString();
			return this.destinationMacString;
		}
		catch(Exception e) {
			return "*";
		}
	}
	
	public String getEtherString() {
		if(etherString!=null) {
			if(!etherString.equals("")) {
				return etherString;
			}
		}
		etherString=getSourceMacString()+" -> "+getDestinationMacString();
		return etherString;
	}
		
	public String getSourceIpString() {
		if(this.sourceIpString!=null) {
		   if(!this.sourceIpString.equals("")) {
			   return this.sourceIpString;
		   }
		}
		try {
			if(this.ipv4!=null) {
			   this.sourceIpString=ipSrcAddr.toString().substring(1);
			   return this.sourceIpString;
			}	
			else
			if(this.ipv6!=null) {
				   this.sourceIpString=ip6SrcAddr.toString().substring(1);
				   return this.sourceIpString;				
			}
			return "*";
		}
		catch(Exception e) {
			return "*";
		}
	}
		
	public String getDestinationIpString() {
		if(this.destinationIpString!=null) {
		   if(!this.destinationIpString.equals("")) {
			   return this.destinationIpString;
		   }
		}
		try {
			if(this.ipv4!=null) {
			   this.destinationIpString=ipDstAddr.toString().substring(1);
			   return this.destinationIpString;
			}
			else
			if(this.ipv6!=null) {
			    this.destinationIpString = ip6DstAddr.toString().substring(1);	
			    return this.destinationIpString;
			}
			else {
				return "*";
			}
		}
		catch(Exception e) {
			return "*";
		}
	}
	
	public String getIpString() {
		if(ipString!=null) {
			if(!ipString.equals("")) {
				return ipString;
			}
		}
		ipString=getSourceIpString()+" -> "+getDestinationIpString();
		return ipString;
	}	
	
	public String getArpString() {
		if(arp!=null) {
			if(!payloadString.equals("")) {
				return payloadString;
			}
		}
		if(arph==null) return "";
		   payloadString=
		            "oparation="+arph.getOperation().toString()+
			        ",ptype="+arph.getProtocolType().toString()+
		            ",htype="+arph.getHardwareType().toString()+
		            ",smac="+arph.getSrcHardwareAddr().toString()+
		            ",sipa="+sourceIpString+
		            ",dmac="+arph.getDstHardwareAddr().toString()+
		            ",dipa="+destinationIpString;
		states[0]=payloadString;	
		return payloadString.replaceAll(" ", "_");
	}
	public String getL4String() {
		if(l4String!=null) {
		    if(!l4String.equals("")) {
		    	return l4String;
		    }
		}
		if(udp!=null) {
           if(payload==null) {
	          l4String="udp "+sport+"->"+dport+" (no payload)";
	          return l4String;
           }
           else {
	          l4String="udp "+sport+"->"+dport+" "+SBUtil.showAsciiInBinary(payload.getRawData(),80);
	          return l4String;
           }				
		}
		else
		if(tcp!=null) {
			if(tcph==null) return "";
	        String flags="-";
	        if(tcph.getSyn()) flags=flags+"SYN-";
	        if(tcph.getAck()) flags=flags+"ACK-";
	        if(tcph.getPsh()) flags=flags+"PSH-";
	        if(tcph.getFin()) flags=flags+"FIN-";
	        if(tcph.getRst()) flags=flags+"RST-";
	        if(tcph.getUrg()) flags=flags+"URG-";			
	          l4String="tcp "+sport+"->"+dport+" "+flags+" "+getPayloadString();	
	          return l4String;
		}
		return "";
	}
	private String getUdpPayload() {
		if(udp==null) return "";
		
        if(payload==null) {
	          payloadString="(no payload)";
	          states[0]=payloadString;		    	        	  
	          return payloadString;
        }
        else {
	          payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
	          states[0]=payloadString;
	          return payloadString;
        }		
	}
	private String getTcpPayload() {
		if(tcp==null) return "";
		if(tcph==null) return "";
        String flags="-";
        if(tcph.getSyn()) flags=flags+"SYN-";
        if(tcph.getAck()) flags=flags+"ACK-";
        if(tcph.getPsh()) flags=flags+"PSH-";
        if(tcph.getFin()) flags=flags+"FIN-";
        if(tcph.getRst()) flags=flags+"RST-";
        if(tcph.getUrg()) flags=flags+"URG-";
		
        if(payload!=null)
            try {
               payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
            }
            catch(Exception e) {
	               System.out.println("ParsePacket tcp error while Making payload String:"+payload.toString()+":"+e.toString());
              }
          else
        	  payloadString="";
          states[0]=flags+" "+payloadString;
          payloadString=flags+" "+payloadString;
		return payloadString;
	}
	public String getPayloadString() {
		if(payloadString!=null) {
			if(!payloadString.equals("")) {
				return payloadString;
			}
		}
		if(arp!=null) {
			return getArpString();
		}
		if(ipv4!=null) {
			if(tcp!=null) {
				return getTcpPayload();
			}
			else
			if(udp!=null) {
				return getUdpPayload();
			}
			
		}
		else
		if(ipv6!=null) {
			if(tcp!=null) {
				return getTcpPayload();
			}
			else
			if(udp!=null) {
				return getUdpPayload();
			}
		}
		payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
		states[0]=payloadString;
        return payloadString;
	}
		
//	   sourceIpString = FormatUtils.ip(ip.source());
//	   destinationIpString = FormatUtils.ip(ip.destination());
//	InetAddress ipDstAddr =ip4h.getDstAddr();
//	destinationIpString = ip4h.getDstAddr().toString();
//	destinationIpString = ipDstAddr.toString().substring(1);
	
	public boolean isMacBroadcast() {
		if(macDestAddress==null) return false;
		byte[] mb=macDestAddress.getAddress();
		for(int i=0;i<mb.length;i++) {
			int x=0xff & mb[i];
			if(x!=0xff) return false;
		}
		return true;
	}
	
	public void reParse(Packet p) {
		packet=p;
		succeeded=false;
		try{
		   reParse();
		}
		catch(Exception e){
			System.out.println("PcapPacket.reParse error: "+e);
			succeeded=false;
			return;
		}		
	}
    public void reParse(){

    	eth=null;
    	ipv4=null;
    	tcp=null;
    	udp=null;
    	arp=null;
    	icmp=null;
    	ipv6=null;
    	icmpV6Common=null;
    	ipv6Hopbyhop=null;
    	ipv6Destination=null;
		states[0]="unknown";
		sourceMacString="";
		destinationMacString="";
		sourceIpString="";
		destinationIpString="";
		etherString="";
		protocol="";
		protocolBit=0x000;
//		public byte[] payload;
		payload=null;
		ipString="";
		payloadString="";
		l4String="";
		ptime=0;
		ptimes="";
		sha1Payload="";
		tcph=null;
		arph=null;
		payloadLength=0;
		if(packet==null) return;
//			  if (packet.hasHeader(eth)) {
		if(packet.contains(EthernetPacket.class)) {
//				   sourceMacString = FormatUtils.mac(eth.source());
				eth=(EthernetPacket)packet;
				EthernetHeader eph=eth.getHeader();
				macDestAddress=eph.getDstAddr();
				macSrcAddress=eph.getSrcAddr();
//				sourceMacString=macSrcAddress.toString();
//				destinationMacString = FormatUtils.mac(eth.destination());
//				destinationMacString=macDestAddress.toString();

				address[0]= 0xff & (macSrcAddress.getAddress()[2]);
				address[1]= 0xff & (macSrcAddress.getAddress()[3]);
				address[2]= 0xff & (macSrcAddress.getAddress()[4]);
				address[3]= 0xff & (macSrcAddress.getAddress()[5]);
				address[6]= 0xff & (macDestAddress.getAddress()[2]);
				address[7]= 0xff & (macDestAddress.getAddress()[3]);
				address[8]= 0xff & (macDestAddress.getAddress()[4]);
				address[9]= 0xff & (macDestAddress.getAddress()[5]);

	    	       sport=0;
	    	       dport=0;
//				   etherString=sourceMacString+"->"+destinationMacString+" ";
		}
        if(packet.contains(ArpPacket.class)){
//		if (packet.hasHeader(ip)) {		if(packet.contains(ArpPacket.class)) {
		   try{
		       protocol ="arp";
		       arp=(ArpPacket)(packet.getPayload());
		       arph=arp.getHeader();
//		       packet.getHeader(arp);
			   address[4]= 0;
			   address[5]= 0;
			   address[10]= 0;
			   address[11]= 0;
//				sourceIpString=FormatUtils.ip(arp.spa());
			   sourceIpString=arph.getSrcProtocolAddr().toString().substring(1);
//				destinationIpString=FormatUtils.ip(arp.tpa());
			   destinationIpString=arph.getDstProtocolAddr().toString().substring(1);
				protocolBit=0x100;
		   }
		   catch(Exception e){
				System.out.println("ParsePacket arp error: "+e);
				return;				 
		   }
	    }
	    else
        if(isIpV4(packet)) {}
		else 
        if(isIpV6(packet)) {}
        else
        if(isIcmpV6Common(packet)) {}
		else {
			try{
				  protocol ="ether-N/A";
				  sport=0;
				  dport=0;
				  payload=eth.getPayload();
				  payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
				  states[0]=payloadString;
				  protocolBit=0x080;
				  if(payload!=null)
				  payloadLength=payload.length();
			}
			catch(Exception e){
				System.out.println("ParsePacket error ethernet-n/a: "+e);
				return;
			}
	   }			
		ptime=System.currentTimeMillis();
		succeeded=true;
    }
	
//	public PcapPacket packet;
    public Packet packet;
//	public JMemoryPacket packet;
    public ParsePacket() {
    }
//	public ParsePacket(PcapPacket p){
    public ParsePacket(Packet p) {
//    public ParsePacket(JMemoryPacket p){
		packet=p;
		succeeded=false;
		try{
		   reParse();
		}
		catch(Exception e){
			System.out.println("PcapPacket.reParse error: "+e);
			succeeded=false;
			return;
		}

	}
    
    boolean isIpV4(Packet packet) {
	   if(!packet.contains(IpV4Packet.class)) 
		   return false;
		try{
//			   packet.getHeader(ip);
			ipv4=(IpV4Packet)(packet.getPayload());
			IpV4Packet.IpV4Header ip4h=ipv4.getHeader();
			ipSrcAddr= ip4h.getSrcAddr();

			ipDstAddr =ip4h.getDstAddr();

			address[0]=0xff & (ipSrcAddr.getAddress()[0]);
			address[1]=0xff & (ipSrcAddr.getAddress()[1]);
			address[2]=0xff & (ipSrcAddr.getAddress()[2]);
			address[3]=0xff & (ipSrcAddr.getAddress()[3]);
			address[6]=0xff & (ipDstAddr.getAddress()[0]);
			address[7]=0xff & (ipDstAddr.getAddress()[1]);
			address[8]=0xff & (ipDstAddr.getAddress()[2]);
			address[9]=0xff & (ipDstAddr.getAddress()[3]);

//			    ipString=sourceIpString+"->"+destinationIpString+" ";
		  }
		  catch(Exception e){
			  System.out.println("ParsePacket error ip: "+e);
			  return false;
		  }
//		if(packet.hasHeader(tcp)){
		  if(ipv4==null) 
			  return false;
		  if(ipv4.contains(TcpPacket.class)) {
			  try {
//	    	          packet.getHeader(tcp);
	            	tcp=(TcpPacket)(ipv4.getPayload());
	            	tcph=tcp.getHeader();
	    	          try{
//		    	            payload=tcp.getPayload();
	    	        	  payload=tcp.getPayload();
	    	        	  if(payload!=null)  payloadLength=payload.length();	    	        	  
		    	      }
		    	      catch(Exception e){
//		    		        payload=new byte[]{'e','r','r','o','r'};
		    		        System.out.println("ParsePacket get tcpPayload error: "+e);
		    		        return true;
		    	      }
	    	          protocol="tcp";
	    	          protocolBit=0x001;

	    	          sport=tcph.getSrcPort().valueAsInt();
	    	          dport=tcph.getDstPort().valueAsInt();
	    	          address[4]=0xff & (sport>>8);
	    	          address[5]=0xff & (sport);
	    	          address[10]=0xff & (dport>>8);
	    	          address[11]=0xff & dport;
                      return true;
	            }
	            catch(Exception e){
	            	System.out.println("ParsePacket tcp error: "+e);
	            	return false;
	            }
	    }
	    else
//	    if(packet.hasHeader(udp)){
	    if(ipv4.contains(UdpPacket.class)) {
		    if(ipv4.contains(IcmpV4CommonPacket.class)) {
	  			try{
//	  				  packet.getHeader(icmp);
	  				icmp = (IcmpV4CommonPacket)(ipv4.getPayload());
					  protocol ="icmp";
					  protocolBit=0x004;
					  address[4]= 0;
					  address[5]= 0;
					  address[10]= 0;
					  address[11]= 0;
//					  String icmpString=icmp.checksumDescription();
					  String icmpString=icmp.getHeader().getCode().toString();
					  try {
					     payload=icmp.getPayload();
					     if(payload!=null)
	    	             payloadLength=payload.length();					     
					  }
					  catch(Exception e) {
			  				System.out.println("ParsePacket error udp-icmpv4-getpayload:"+e);
			  				return false;							  
					  }
					  if(payload!=null) {
					     payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
					     states[0]=icmpString+" "+payloadString;
					     payloadString=states[0];
					  }
					  else {
						  states[0]=icmpString+" (icmpv4- no payload)";
						  payloadString=states[0];
					  }
					  return true;
				 }
	  			catch(Exception e){
	  				System.out.println("ParsePacket icmpv4-udp:"+e);
	  				return false;
	  			}
		   }
	       else {
	            try{
	            	udp=(UdpPacket)(ipv4.getPayload());
//	    	          packet.getHeader(udp);
	            	UdpPacket.UdpHeader udph=udp.getHeader();
	    	          protocol="udp";
	    	          protocolBit=0x002;
	    	          /*
	    	          sport=udp.source();
	    	          dport=udp.destination();
	    	          */
	    	          sport=udph.getSrcPort().valueAsInt();
	    	          dport=udph.getDstPort().valueAsInt();
	    	    
	  				  address[4]= 0xff & (sport>>8);
					  address[5]= 0xff & sport;
					  address[10]= 0xff & dport>>8;
					  address[11]= 0xff & dport;
					  /**/
					    try{
				            payload=udp.getPayload();
				            if(payload!=null)
		                      payloadLength=payload.length();				            
				        }
				        catch(Exception e){
					          System.out.println("ParsePacket getUdpPayload error:"+e);
//					          payload=new byte[]{'e','r','r','o','r','-','g','e','t','P','a','y','l','o','a','d'};
					          return false;
				        }	    	          
	    	          return true;
	            }
	            catch(Exception e){
	            	System.out.println("ParsePacket udp error: "+e);
	            	return false;
	            }
	       }
	    }
	    else
//	  	if(packet.hasHeader(icmp)){
	    if(ipv4.contains(IcmpV4CommonPacket.class)) {
	  			try{
//	  				  packet.getHeader(icmp);
	  				icmp = (IcmpV4CommonPacket)(ipv4.getPayload());
					  protocol ="icmp";
					  protocolBit=0x004;
					  address[4]= 0;
					  address[5]= 0;
					  address[10]= 0;
					  address[11]= 0;
//					  String icmpString=icmp.checksumDescription();
					  String icmpString=icmp.getHeader().getCode().toString();
					  try {
					     payload=icmp.getPayload();
					     if(payload!=null)
	                      payloadLength=payload.length();					     
					  }
					  catch(Exception e) {
			  				System.out.println("ParsePacket error icmpv4-getpayload:"+e);
			  				return false;						  
					  }
					  if(payload!=null) {
					     payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
					     states[0]=icmpString+" "+payloadString;
					  }
					  else {
						  states[0]=icmpString+" (icmpv4- no payload)";
					  }
					  return true;
				 }
	  			catch(Exception e){
	  				System.out.println("ParsePacket error icmpv4:"+e);
	  				return false;
	  			}
		}
		else{
				try{
					  protocol ="ip-N/A";
					  protocolBit=0x008;
					  sport=0;
					  dport=0;
					  payload=ipv4.getPayload();
					  if(payload!=null)
					  payloadLength=payload.length();
					  payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
					  states[0]=payloadString;
					  return true;
				}
				catch(Exception e){
					System.out.println("ParsePacket error ip-n/a: "+e);
					return false;
				}
		}			
    }
    
    boolean isIpV6(Packet packet) {
	    if(!packet.contains(IpV6Packet.class))
		   return false;
		try{
//			   packet.getHeader(ip);
			ipv6=(IpV6Packet)(packet.getPayload());
			IpV6Packet.IpV6Header ip6h=ipv6.getHeader();
			ip6SrcAddr= ip6h.getSrcAddr();
//			   sourceIpString = FormatUtils.ip(ip.source());
//			sourceIpString=ip6SrcAddr.toString().substring(1);
//			   destinationIpString = FormatUtils.ip(ip.destination());
			ip6DstAddr =ip6h.getDstAddr();

			address[0]=0xff & (ip6SrcAddr.getAddress()[0]);
			address[1]=0xff & (ip6SrcAddr.getAddress()[1]);
			address[2]=0xff & (ip6SrcAddr.getAddress()[14]);
			address[3]=0xff & (ip6SrcAddr.getAddress()[15]);
			address[6]=0xff & (ip6DstAddr.getAddress()[0]);
			address[7]=0xff & (ip6DstAddr.getAddress()[1]);
			address[8]=0xff & (ip6DstAddr.getAddress()[14]);
			address[9]=0xff & (ip6DstAddr.getAddress()[15]);

//			ipString=sourceIpString+"->"+destinationIpString+" ";
			    
		}
		catch(Exception e){
			System.out.println("ParsePacket error ipv6: "+e);
			return false;
		}
//		if(packet.hasHeader(tcp)){
		if(ipv6==null)
			return false;
		if(ipv6.contains(TcpPacket.class)) {
			try {
//	    	          packet.getHeader(tcp);
	            	tcp=(TcpPacket)(ipv6.getPayload());
	            	tcph=tcp.getHeader();
	    	          try{
//		    	            payload=tcp.getPayload();
	    	        	  payload=tcp.getPayload();
	    	        	  if(payload!=null)
	                      payloadLength=payload.length();					     	    	        	  
		    	      }
		    	      catch(Exception e){
//		    		        payload=new byte[]{'e','r','r','o','r'};
		    		        System.out.println("ParsePacket get tcpPayload error: "+e);
		    		        return false;
		    	      }
	    	          protocol="tcp";
	    	          protocolBit=0x010;

	    	          sport=tcph.getSrcPort().valueAsInt();
	    	          dport=tcph.getDstPort().valueAsInt();
	    	          address[4]=0xff & (sport>>8);
	    	          address[5]=0xff & (sport);
	    	          address[10]=0xff & (dport>>8);
	    	          address[11]=0xff & dport;
	    	          String flags="-";
	    	          if(tcph.getSyn()) flags=flags+"SYN-";
	    	          if(tcph.getAck()) flags=flags+"ACK-";
	    	          if(tcph.getPsh()) flags=flags+"PSH-";
	    	          if(tcph.getFin()) flags=flags+"FIN-";
	    	          if(tcph.getRst()) flags=flags+"RST-";
	    	          if(tcph.getUrg()) flags=flags+"URG-";

                      return true;
	            }
	            catch(Exception e){
	            	System.out.println("ParsePacket tcp error: "+e);
	            	return false;
	            }
	    }
		else
		if(ipv6.contains(IpV6ExtOptionsPacket.class)) {
			
//		  	if(packet.hasHeader(icmp)){
				if(ipv6.contains(IpV6ExtHopByHopOptionsPacket.class)) {
		  			try{
//		  				  packet.getHeader(icmp);
		  				ipv6Hopbyhop = (IpV6ExtHopByHopOptionsPacket)(ipv6.getPayload());
						  protocol ="icmpv6hopbyhop";
						  protocolBit=0x040;
						  address[4]= 0;
						  address[5]= 0;
						  address[10]= 0;
						  address[11]= 0;
//						  String icmpString=icmp.checksumDescription();
//						  String icmpString=ipv6Hopbyhop.getHeader().toString();
						  payload=ipv6Hopbyhop.getPayload();
						  try {
							     payload=icmp.getPayload();
							     if(payload!=null)
			                      payloadLength=payload.length();					     							     
						  }
						  catch(Exception e) {
						  }
						  if(payload!=null) {							  
						     payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
						     states[0]=payloadString;
						  }
						  else {
							  states[0]="(icmpv6-no-payload)";
							  payloadString=states[0];
						  }
						  return true;
		  			}
		  			catch(Exception e){
		  				System.out.println("ParsePacket error IpVtExtHopByHop:"+e);
		  				return false;
		  			}
			    }		
				else
					if(ipv6.contains(IpV6ExtDestinationOptionsPacket.class)) {
			  			try{
//			  				  packet.getHeader(icmp);
			  				ipv6Destination = (IpV6ExtDestinationOptionsPacket)(ipv6.getPayload());
							  protocol ="ipv6Destination";
							  protocolBit=0x040;
							  address[4]= 0;
							  address[5]= 0;
							  address[10]= 0;
							  address[11]= 0;
//							  String icmpString=icmp.checksumDescription();
							  String icmpString=ipv6Destination.getHeader().toString();
							  try {
							     payload=ipv6Destination.getPayload();
							     if(payload!=null)
			                      payloadLength=payload.length();					     							     
							  }
							  catch(Exception e) {
							  }
							  if(payload!=null) {
							     payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
							     states[0]=icmpString+" "+payloadString;
							  }
							  else {
								  states[0]=icmpString+" (ipv6Destination- no payload)";
							  }
							  payloadString=states[0];
							  return true;
			  			}
			  			catch(Exception e){
			  				System.out.println("ParsePacket error IpVtExtDestination:"+e);
			  				return false;
			  			}
				    }
					else {
						try{
							  protocol ="ipv6-extOption-N/A";
							  protocolBit=0x040;
							  sport=0;
							  dport=0;
							  payload=ipv6.getPayload();
							  if(payload!=null)
		                      payloadLength=payload.length();					     							  
							  payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
							  states[0]=payloadString;
							  return true;
						}
						catch(Exception e){
							System.out.println("ParsePacket error ipv6-extOption-n/a: "+e);
							return false;
						}
						
					}
				}
	    else
//	    if(packet.hasHeader(udp)){
	    if(ipv6.contains(UdpPacket.class)) {
	            try{
	            	udp=(UdpPacket)(ipv6.getPayload());
//	    	          packet.getHeader(udp);
	            	UdpPacket.UdpHeader udph=udp.getHeader();
	    	          protocol="udp";
	    	          protocolBit=0x020;
	    	          /*
	    	          sport=udp.source();
	    	          dport=udp.destination();
	    	          */
	    	          sport=udph.getSrcPort().valueAsInt();
	    	          dport=udph.getDstPort().valueAsInt();
	    	    
	  				  address[4]= 0xff & (sport>>8);
					  address[5]= 0xff & sport;
					  address[10]= 0xff & dport>>8;
					  address[11]= 0xff & dport;
					  /**/
	    	          
	    	          try{
	    	              payload=udp.getPayload();
	    	              if(payload!=null)
	                      payloadLength=payload.length();					     	    	              
	    	          }
	    	          catch(Exception e){
	    		          System.out.println("ParsePacket getUdpPayload error:"+e);
//	    		          payload=new byte[]{'e','r','r','o','r','-','g','e','t','P','a','y','l','o','a','d'};
	    		          return false;
	    	          }
	    	          return true;
	              }
	              catch(Exception e){
	            	  System.out.println("ParsePacket ipv6 udp error: "+e);
	            	  return false;
	              }
	    }

			else{
				try{
					  protocol ="ipv6-N/A";
					  protocolBit=0x080;
					  sport=0;
					  dport=0;
					  payload=ipv6.getPayload();
					  if(payload!=null)
                      payloadLength=payload.length();					     				  
					  payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
					  states[0]=payloadString;
					  return true;
				}
				catch(Exception e){
					System.out.println("ParsePacket error ipv6-n/a: "+e);
					return false;
				}
		   }
    }
    
    boolean isIcmpV6Common(Packet packet) {
//	  	if(packet.hasHeader(icmp)){
	    if(!packet.contains(IcmpV6CommonPacket.class)) 
		return false;
	  			try{
//	  				  packet.getHeader(icmp);
	  				icmpV6Common = (IcmpV6CommonPacket)(ipv6.getPayload());
					  protocol ="icmpv6";
					  protocolBit=0x040;
					  address[4]= 0;
					  address[5]= 0;
					  address[10]= 0;
					  address[11]= 0;
//					  String icmpString=icmp.checksumDescription();
					  String icmpString=icmpV6Common.getHeader().getCode().toString();
					  try {
						     payload=icmpV6Common.getPayload();
						     if(payload!=null) {
						     payloadLength=payload.length();
						     }
						     else {
						    	 payloadLength=0;
						     }
					  }
					  catch(Exception e) {
					  }
					  if(payload!=null) {
					     payloadString=SBUtil.showAsciiInBinary(payload.getRawData(),80);
					     states[0]=icmpString+" "+payloadString;
					     payloadString=icmpString+" "+payloadString;
					  }
					  else {
						  states[0]=icmpString+" (icmpv6- no payload)";
						  payloadString=states[0];
					  }
					  return true;
	  			}
	  			catch(Exception e){
	  				System.out.println("ParsePacket error icmpv6CommonPacket:"+e);
	  				return false;
	  			}
	}
    public ParsePacket clone() {
    	long time=this.ptime;
    	ParsePacket ppc=new ParsePacket(this.packet);  
    	ppc.ptime=time;
    	return ppc;
    	
    	/*
    	try {
    		if(packet!=null) ppc.packet=this.packet.getBuilder().build();
    	if(eth!=null) ppc.eth=this.eth.getBuilder().build();
    	if(ipv4!=null)
    	ppc.ipv4=this.ipv4.getBuilder().build();
    	if(tcp!=null)
    	ppc.tcp=this.tcp.getBuilder().build();
    	if(udp!=null)
    	ppc.udp=this.udp.getBuilder().build();
    	if(arp!=null)
    	ppc.arp=this.arp.getBuilder().build();
    	if(icmp!=null)
    	ppc.icmp=this.icmp.getBuilder().build();
    	if(ipv6!=null)
    	ppc.ipv6=this.ipv6.getBuilder().build();
		if(this.payload!=null)
		ppc.payload=this.payload.getBuilder().build();
		ppc.ipString=this.ipString;
		ppc.payloadString=this.payloadString;
		ppc.l4String=this.l4String;
		ppc.ptime=this.ptime;
		ppc.ptimes=this.ptimes;
		ppc.sha1Payload=this.sha1Payload;
		if(ppc.tcp!=null)ppc.tcph=tcp.getHeader();
		if(ppc.arp!=null)ppc.arph=arp.getHeader();
		ppc.payloadLength=this.payloadLength;    
		ppc.dport=this.dport;
		ppc.sport=this.sport;  
		ppc.states[0]=this.states[0];
		ppc.sourceMacString=this.sourceMacString;
		ppc.destinationMacString=this.destinationMacString;
		ppc.sourceIpString=this.sourceIpString;
		ppc.destinationIpString=this.destinationIpString;
		ppc.etherString=this.etherString;
		ppc.protocol=this.protocol;
		ppc.protocolBit=this.protocolBit;
//		public byte[] payload;
		
    	}
    	catch(Exception e) {
    		System.out.println("ParsePacket.clone p1 error:"+e);
//    		Thread.dumpStack();
    		return this;
    		
    	}
    	try {
    	if(icmpV6Common!=null)
    	ppc.icmpV6Common=this.icmpV6Common.getBuilder().build();
    	if(ipv6Hopbyhop!=null)
    	ppc.ipv6Hopbyhop=this.ipv6Hopbyhop.getBuilder().build();
    	if(ipv6Destination!=null)
    	ppc.ipv6Destination=this.ipv6Destination.getBuilder().build();

    	return ppc;
    	}
    	catch(Exception e) {
    		System.out.println("ParsePacket.clone p2 error:"+e);
//    		Thread.dumpStack();
    		return this;
    	}
//    	return null;
 * 
 */
    }
      
}