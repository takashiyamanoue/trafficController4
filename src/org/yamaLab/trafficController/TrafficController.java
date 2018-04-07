package org.yamaLab.trafficController;
//
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/*
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.nio.JMemoryPool;
import org.jnetpcap.packet.format.FormatUtils;
*/
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.LinkLayerAddress;
import org.pcap4j.util.MacAddress;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//import org.yamaLab.TwitterConnector.Util;
import org.yamaLab.logFile.BlockedFileManager;
import org.yamaLab.pukiwikiCommunicator.PukiwikiCommunicator;
import org.yamaLab.pukiwikiCommunicator.connector.SaveButtonDebugFrame;
import org.yamaLab.pukiwikiCommunicator.language.InterpreterInterface;
import org.yamaLab.pukiwikiCommunicator.language.Util;
import org.yamaLab.pukiwikiCommunicator.language.CommandReceiver;
import org.yamaLab.trafficViewer.MacAddressKey;
//import org.yamaLab.trafficViewer.MainWatch;
import org.yamaLab.trafficViewer.MessageDialog;
import org.yamaLab.trafficViewer.ParsePacket;
import org.yamaLab.trafficViewer.ParsePacketReceiver;
import org.yamaLab.trafficViewer.SBUtil;
import org.yamaLab.trafficViewer.TrafficLogManager;

import twitter4j.Twitter;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class TrafficController extends JFrame implements
CommandReceiver, Runnable, InterpreterInterface, ParsePacketReceiver
{
	private JScrollPane interfaceTablePane;
	private JRadioButton logFileButton;
	private JButton startButton;
	public JTabbedPane mainTabPane;
	private JButton startAllButton;
	private JButton exitButton;
	private JButton dumpClearButton;
	private JButton saveSettingButton;
	private JButton ipClearButton;
	private JButton browseFileButton;
	private JTextField logFileField;
	private JRadioButton interfaceTableButton;
	static private JTable interfaceTable;
	int lanSideInterface;
	int wanSideInterface;
//	Vector <PcapIf> networkInterfaces;
	Vector <PcapNetworkInterface> networkInterfaces;
	private PacketMonitorFilter packetMonitorFilter;
	public CommandTableManager commandTableManager;
//    public VisualTrf vtraffic[][];
//    public MainWatch mainWatch;
    private JButton clearButton;
    private OneSideIO lanSideIO;
    private OneSideIO wanSideIO;
//    private ErrOut errout;
    private JPanel mainPanel;
    private JPanel tcpDumpPanel;
    private JPanel messagePanel;
	private JScrollPane tcpdump_log;
	private JScrollPane appliMessagePane;
    public JTextArea logtext;
    public JTextArea appliMessageArea;
	public Properties setting;
	String settingFileName="traffic-viewer-settings.properties";
	private JFileChooser fileChooser;
	private FirstMessage firstMessage;
	private MessageDialog messageDialog;
	private PacketFilter lan2Wan;
	private PacketFilter wan2Lan;
	private JLabel regularExpressionFieldLabel;
	private JTextField regularExpressionField;
	private JCheckBox grepCheckBox;
//	public PcapHeader hdr = new PcapHeader(JMemory.POINTER);
//	public Packet.Header hdr=new Packet.Header();
//	public JBuffer buf = new JBuffer(JMemory.POINTER);
//	public JMemoryPool jpool=new JMemoryPool();
	public Map <MacAddressKey, MacAddrTableElement> macAddrTable;
	private Thread me;
	public Hashtable <String, EachHostInformation> hostsInformation;
	
	private JTextArea tweetTextArea ;	

    PukiwikiTwitterControllGui ptGui;
    int maxRepeatingNumber=10;
    
	public TrafficController(){
		this.loadProperties();
		
		ptGui =new PukiwikiTwitterControllGui();
		ptGui.setSetting(setting);
		PukiwikiCommunicator pjc=new PukiwikiCommunicator(this,null);
		pjc.setSetting(setting);
		SaveButtonDebugFrame d=pjc.getSaveButtonDebugFrame();
		this.putApplicationTable("pjc", pjc);		
		pjc.putApplicationTable("tcon", this);
		ptGui.setPukiwikiCommunicator(pjc);
		this.putApplicationTable("ptGui",ptGui);
		
		initGUI();
		this.setVisible(true);
		try{
		this.setNetworkInterfaces();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		macAddrTable=Collections.synchronizedMap(new HashMap());
		this.commandTableManager=new CommandTableManager(mainTabPane);
		this.commandTableManager.setSetting(setting,d);
		this.packetMonitorFilter=new PacketMonitorFilter(commandTableManager);
		this.commandTableManager.setCommandReceiver("pjc",pjc);
		hostsInformation=new Hashtable();
		
//		vtraffic = new VisualTrf[256][256];
		this.setProperties();
		firstMessage =new FirstMessage(this);
//		this.setMonitorFilter(packetMonitorFilter);
		this.logManager=new TrafficLogManager();
		this.logManager.setMonitorFilter(packetMonitorFilter);
		this.logManager.setMain(this);
		this.logManager.setParsePacketReceiver(this);
	}
	private void initMainTabPane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainTabPane.add("main-tab",mainPanel);
		mainPanel.setSize(580,650);
		mainPanel.setPreferredSize(new java.awt.Dimension(712, 650));
		{
			interfaceTablePane = new JScrollPane();
			mainPanel.add(interfaceTablePane);
			interfaceTablePane.setBounds(36, 125, 698, 170);
			{
				TableModel interfaceTableModel = 
					new DefaultTableModel(
							new String[][] { 
									{ "","", "" ,"","","",""}, { "","", "" ,"","","",""},
									{ "","", "","" ,"","",""}, 
									{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
									{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
									{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
									{ "", "", "" ,"","","",""}, { "", "", "","","","","" }},
							new String[] { "lan","wan","if-name", "description", "mac address", "ip address","mask" });
				interfaceTable = new JTable();
				interfaceTablePane.setViewportView(interfaceTable);
				interfaceTable.setModel(interfaceTableModel);
				interfaceTable.setPreferredSize(new java.awt.Dimension(639, 176));
				interfaceTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						interfaceTableMouseClicked(evt);
					}
				});
			}
	    }
	    {
		    interfaceTableButton = new JRadioButton();
		    mainPanel.add(interfaceTableButton);
		    interfaceTableButton.setText("interfaceTable");
		    interfaceTableButton.setBounds(36, 98, 148, 25);
	    }
	    {
		    logFileButton = new JRadioButton();
		    mainPanel.add(logFileButton);
		    logFileButton.setText("logFile");
		    logFileButton.setBounds(36, 76, 81, 22);
	    }
	    {
		    logFileField = new JTextField();
		    mainPanel.add(logFileField);
		    logFileField.setBounds(127, 75, 252, 22);
	    }
	    {
		    browseFileButton = new JButton();
		    mainPanel.add(browseFileButton);
		    browseFileButton.setText("browse");
		    browseFileButton.setBounds(379, 73, 95, 26);
		    browseFileButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
				    browseFileButtonActionPerformed(evt);
			    }
		    });
	    }
	    {
		    startButton = new JButton();
		    mainPanel.add(startButton);
		    startButton.setText("start");
		    startButton.setBounds(41, 6, 112, 28);
		    startButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
				    startButtonActionPerformed(evt);
//				    commandTableManager.startWatchingButtonActionPerformed(null);
//					ptGui.mainController.parseCommandWithReturn("wikiStartWatching", "");

			    }
		    });
	    }
	    {
		    clearButton = new JButton();
		    mainPanel.add(clearButton);
		    clearButton.setText("clear");
		    clearButton.setBounds(220, 7, 92, 25);
		    clearButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
				    clearButtonActionPerformed(evt);
			    }
		    });
	    }
	    {
		    logPathField = new JTextField();
		    mainPanel.add(logPathField);
		    logPathField.setBounds(127, 45, 252, 26);
	    }
	    {
		    stopButton = new JButton();
		    mainPanel.add(stopButton);
		    stopButton.setText("stop");
		    stopButton.setBounds(153, 7, 67, 26);
		    stopButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
				    stopButtonActionPerformed(evt);
			    }
		    });
	    }		
	}
	private void initGUI(){
		{
			getContentPane().setLayout(null);
			{
				mainTabPane = new JTabbedPane();
				getContentPane().add(mainTabPane);
				mainTabPane.setBounds(3, 43, 770, 650);
			}
			{
				int h=12;
				
				startAllButton = new JButton();
				getContentPane().add(startAllButton);
				startAllButton.setText("StartAll");
				startAllButton.setBounds(10, h, 120, 26);
				startAllButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						startAllButtonActionPerformed(evt);
					}
				});				
				
				saveSettingButton = new JButton();
				getContentPane().add(saveSettingButton);
				saveSettingButton.setText("save setting");
				saveSettingButton.setBounds(130, h, 140, 26);
				saveSettingButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveSettingButtonActionPerformed(evt);
					}
				});				
				
				exitButton = new JButton();
				getContentPane().add(exitButton);
				exitButton.setText("exit");
				exitButton.setBounds(280, h, 66, 26);
				exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						exitButtonActionPerformed(evt);
					}
				});

				/*
				hideButton = new JButton();
				getContentPane().add(hideButton);
				hideButton.setText("hide");
				hideButton.setBounds(385, h, 66, 26);
				hideButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						hideButtonActionPerformed(evt);
					}
				});
				*/
			}
		}
	    initMainTabPane();

			{
				tcpDumpPanel = new JPanel();
				tcpDumpPanel.setLayout(null);
				mainTabPane.add("tcpdump",tcpDumpPanel);
				tcpDumpPanel.setSize(580,350);
				tcpDumpPanel.setPreferredSize(new java.awt.Dimension(558, 369));
				messagePanel = new JPanel();
				messagePanel.setLayout(null);
				mainTabPane.add("message",messagePanel);
				messagePanel.setSize(580,350);
				messagePanel.setPreferredSize(new java.awt.Dimension(558, 369));

			{
				tcpdump_log = new JScrollPane();
				tcpdump_log.setBounds(5, 40, 769, 396);
				tcpDumpPanel.add(tcpdump_log);
				{
					logtext = new JTextArea();
					tcpdump_log.setViewportView(logtext);
				}
				
			}
			{
				dumpClearButton = new JButton();
				tcpDumpPanel.add(dumpClearButton);
				tcpDumpPanel.add(getGrepCheckBox());
				tcpDumpPanel.add(getRegularExpressionField());
				tcpDumpPanel.add(getRegularExpressionFieldLabel());
				dumpClearButton.setText("clear");
				dumpClearButton.setBounds(39, 9, 82, 26);
				dumpClearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dumpClearButtonActionPerformed(evt);
					}
				});
			}
			{
			    appliMessagePane = new JScrollPane();
			    appliMessagePane.setBounds(5, 40, 686, 260);
			    messagePanel.add(appliMessagePane);
//				ip_log = new JFrame("IP?�ｽｿ�ｽｽ?�ｽｽ�ｽｽA?�ｽｿ�ｽｽ?�ｽｽ�ｽｽh?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ?�ｽｿ�ｽｽ?�ｽｽ�ｽｽX?�ｽｿ�ｽｽ?�ｽｽ�ｽｽA?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ|?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ[?�ｽｿ�ｽｽ?�ｽｽ�ｽｽg?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ?�ｽｾ荳橸ｽ･?�ｽｽ�ｽｿ?�ｽｽ�ｽｽ?�ｽｿ�ｽｽ?�ｽｽ�ｽｽ?�ｽｾ讙趣ｽ｢�ｽｺ?�ｽｿ�ｽｽ?�ｽｽ�ｽｽF");
			    {
					appliMessageArea = new JTextArea();
					appliMessagePane.setViewportView(appliMessageArea);
					appliMessageArea.setBounds(0, 0, 190, 95);
				}
			}
			{
				ipClearButton = new JButton();
				messagePanel.add(ipClearButton);
				ipClearButton.setText("clear");
				ipClearButton.setBounds(33, 9, 81, 26);
				ipClearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						ipClearButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			 ButtonGroup gr = new ButtonGroup();
		     gr.add(logFileButton);
		     gr.add(interfaceTableButton);
		     interfaceTableButton.setSelected(true);
		}
		{
			this.setSize(798, 553);
		}		
		
		{

		JPanel pukiwikiPanel=this.ptGui.getPukiwikiPanel();
		JPanel twitterPanel=this.ptGui.getTwitterPanel();
		JPanel twitterAuthPanel=this.ptGui.getTwitterAuthPanel();
		mainTabPane.addTab("Pukiwiki-Communicator", pukiwikiPanel);
		mainTabPane.addTab("twitter-Communicator", twitterPanel);
		mainTabPane.addTab("twitterAuth", twitterAuthPanel);
		
//		tweetTextArea = new JTextArea();
//		tweetScrollPane.setViewportView(tweetTextArea);
		}
		
	    networkInterfaces=new Vector();

	}
	public void setNetworkInterfaces()throws IOException {
            networkInterfaces.removeAllElements();
//			List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
            List<PcapNetworkInterface> alldevs =new ArrayList<PcapNetworkInterface>();
			StringBuilder errbuf = new StringBuilder(); // For any error msgs

			/***************************************************************************
			 * First get a list of devices on this system
			 **************************************************************************/
			/*
			int r = Pcap.findAllDevs(alldevs, errbuf);
			if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
				System.err.printf("Can't read list of devices, error is %s", errbuf
				    .toString());
				return;
			}
			*/
			try {
			alldevs=Pcaps.findAllDevs();
			}
			catch(Exception e) {
				
			}
			if(alldevs==null) {
				System.err.printf("Can't read list of devices, error is %s", errbuf
					    .toString());
					return;				
			}

			/***************************************************************************
			 * Second iterate through all the interface and get the HW addresses
			 **************************************************************************/
			int row=0;
//			for (final PcapIf i : alldevs) {
			for (final PcapNetworkInterface i:alldevs) {
//				final byte[] mac = i.getHardwareAddress();

				ArrayList<LinkLayerAddress> macs = i.getLinkLayerAddresses();
//				if (mac == null) {
				if (macs ==null) {
				 continue; // Interface doesn't have a hardware address
				}
//				for(final LinkLayerAddress mac:macs) {
//                   if(mac==null) continue;
	               String description =  
	                  (i.getDescription() != null) ? i.getDescription()  
	                      : "No description available";  
	             /*
	            List<PcapAddr> alist=i.getAddresses();
	            PcapAddr addr=null;
	            if(alist.size()>=1)
	               addr=alist.get(0);
	            */
//	               PcapAddr addr=this.getIpV4Address(i);
	               PcapAddress addr=this.getIpV4Address(i);
//	               PcapSockAddr psockaddr=null;
	               InetAddress psockaddr=null;
//	               PcapSockAddr pmaskaddr=null;
	               InetAddress pmaskaddr=null;
	               if(addr!=null){
//	            	  psockaddr=addr.getAddr();
	            	  psockaddr=addr.getAddress();
//	                  String addrx=FormatUtils.ip(psockaddr.getData());
	            	  String addrx=psockaddr.getHostAddress();
	                  interfaceTable.setValueAt(addrx,row, 5);
	                  pmaskaddr=addr.getNetmask();
//	                  String maskx=FormatUtils.ip(pmaskaddr.getData());
	                  String maskx=pmaskaddr.getHostAddress();
	                  interfaceTable.setValueAt(maskx,row, 6);
	               }
				   System.out.printf("%s=%s\n", i.getName(), (macs.get(0)).toString());
				   interfaceTable.setValueAt(i.getName(), row, 2);
				   interfaceTable.setValueAt((macs.get(0)).toString(),row,4);
				   interfaceTable.setValueAt(description, row, 3);
				   interfaceTable.setValueAt("",row,0);
				   networkInterfaces.addElement(i);
//				}
				row++;
			}
            interfaceTable.setValueAt("!", 0, 0);
            this.lanSideInterface=0;

		/**
		 * @param hardwareAddress
		 * @return
		 */
		
	}
	private static String asString(final byte[] mac) {
		final StringBuilder buf = new StringBuilder();
		for (byte b : mac) {
			if (buf.length() != 0) {
				buf.append(':');
			}
			if (b >= 0 && b < 16) {
				buf.append('0');
			}
			buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
		}

		return buf.toString();
	}
	public static void main(String arg[]){
		new TrafficController();
	}
	
	private void interfaceTableMouseClicked(MouseEvent evt) {
		System.out.println("interfaceTable.mouseClicked, event="+evt);
		//TODO add your code for interfaceTable.mouseClicked
		Point p=evt.getPoint();
		int row = interfaceTable.rowAtPoint(p);
		int column = interfaceTable.columnAtPoint(p);
		int is=interfaceTable.getRowCount();
		if(column==0){
		  for(int i=0;i<is;i++){
			 interfaceTable.setValueAt("",i,0);		
		  }
          interfaceTable.setValueAt("!", row, 0);
          this.lanSideInterface=row;
          if(this.setting!=null){
        	setting.setProperty("lanIterfaceTableNumber", ""+lanSideInterface);
          }
          this.setCurrentLanInterfaceNetworkAddr();
		}
		if(column==1){
			  for(int i=0;i<is;i++){
				 interfaceTable.setValueAt("",i,1);		
			  }
	          interfaceTable.setValueAt("!", row, 1);
	          this.wanSideInterface=row;
	          if(this.setting!=null){
	        	setting.setProperty("wanIterfaceTableNumber", ""+wanSideInterface);
	          }
	          this.setCurrentWanInterfaceNetworkAddr();
			}
	}
	
	public void startButtonActionPerformed(ActionEvent evt) {
//		System.out.println("startButton.actionPerformed, event="+evt);
		this.setBroadcastAddresses();
		if(lanSideInterface==wanSideInterface){
			if(this.messageDialog==null){
				messageDialog=new MessageDialog();
			}
			messageDialog.setMessage1("Input and output interface must be different");
			return;
		}
		//TODO add your code for startButton.actionPerformed
		/*
		Pcaps lanPcap=null;  //Pcap->Pcaps
		Pcaps wanPcap=null;   // Pcap->Pcaps
		*/
		PcapHandle lanPcap=null;
		PcapHandle wanPcap=null;
		StringBuilder errbuf = new StringBuilder(); // For any error msgs
		if(this.logFileButton.isSelected()){
			/***************************************************************************
			 * First - we open up the selected device
			 **************************************************************************/
			String logpath=this.logFileField.getText();
			if(logpath==null||logpath==""){
				logpath="test.pcap";
			}
//			lanPcap = Pcap.openOffline(logpath, errbuf);
			try {
			lanPcap = Pcaps.openOffline(logpath);
//				lanPcap=Pcaps.open
			}
			catch(Exception e) {
				
			}

			if (lanPcap == null) {
				System.out.printf("Error while opening file for capture: "
				    + errbuf.toString());
				return;
			}
		}
		else
		if(this.interfaceTableButton.isSelected()){
	        /*************************************************************************** 
	         * Second we open up the selected device 
	         **************************************************************************/  
//	        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
//	        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
//	        int timeout = 10 * 1000;           // 10 seconds in millis  
	        
	        String lanIfMac=(String)(interfaceTable.getValueAt(this.lanSideInterface, 2));
	        lanPcap =  this.getNifHandleOf(lanIfMac);
//	            Pcaps.openLive((String)(interfaceTable.getValueAt(this.lanSideInterface, 2)), snaplen, flags, timeout, errbuf);  
	        if (lanPcap == null) {  
	            System.out.printf("Error while opening device for capture: "  
	                + errbuf.toString());  
	            return;  
	        }  
	        String wanIfMac=(String)(interfaceTable.getValueAt(this.wanSideInterface, 2));
	        wanPcap =  this.getNifHandleOf(wanIfMac);  
		        if (wanPcap == null) {  
		            System.out.printf("Error while opening device for capture: "  
		                + errbuf.toString());  
		            return;  
		        }  
		}
		else{
			return;
		}
		/*
		if(errout==null)
		errout = new ErrOut(pcap);
		*/
        this.setCurrentLanInterfaceNetworkAddr();
        this.setCurrentWanInterfaceNetworkAddr();
        byte[] zeroAddr=new byte[]{0,0,0,0}; // assume ipv4 only
        if(this.equalAddr(zeroAddr, this.getCurrentWanInterfaceNetworkAddr())){
        	copyAddr(this.currentLanInterfaceNetworkAddr,this.currentWanInterfaceNetworkAddr);
        }
        else
        if(this.equalAddr(zeroAddr, this.getCurrentLanInterfaceNetworkAddr())){
        	copyAddr(this.currentWanInterfaceNetworkAddr,this.currentLanInterfaceNetworkAddr);
        	
        }
        
		String wanMac=(String)(interfaceTable.getValueAt(this.wanSideInterface,4));
		String lanMac=(String)(interfaceTable.getValueAt(this.lanSideInterface,4));
		this.wan2Lan=new PacketFilter(commandTableManager,"wan2Lan", wanMac,
				currentWanInterfaceNetworkAddr, currentWanInterfaceNetmask, this.getCurrentWanInterfaceIpAddr(),
				this.wanSideInterface);		
		this.lan2Wan=new PacketFilter(commandTableManager,"lan2Wan", lanMac,
				currentLanInterfaceNetworkAddr, currentLanInterfaceNetmask, this.getCurrentLanInterfaceIpAddr(),
				this.lanSideInterface);
		commandTableManager.setPacketFilterWan(wan2Lan);
		commandTableManager.setPacketFilterLan(lan2Wan);
        
		if(lanSideIO==null){
		   lanSideIO = new OneSideIO(this, 
			       networkInterfaces.elementAt(this.lanSideInterface),
				   lanPcap,lan2Wan, null);
		   lanSideIO.setInterfaceNo(this.lanSideInterface);
		   lanSideIO.setLogManager(this.logManager);
		   lanSideIO.setNewPcap(lanPcap);
		   lanSideIO.start();
		}
		else{
		   lanSideIO.stop();
//		   lan2Wan.stop();
		   lanSideIO.setNewPcap(lanPcap);
//		   lan2Wan.start();
		   lanSideIO.start();
		}
		if(wanSideIO==null){
		   wanSideIO = new OneSideIO(this, 
				   networkInterfaces.elementAt(this.wanSideInterface),
				   wanPcap,wan2Lan, this.getCurrentWanInterfaceIpAddr());
		   wanSideIO.setInterfaceNo(this.wanSideInterface);
		   wanSideIO.setLogManager(this.logManager);
		   wanSideIO.setNewPcap(wanPcap);
		   wan2Lan.setForwardInterface(lanSideIO);
		   wan2Lan.setAnotherSideFilter(lan2Wan);
		   wan2Lan.setPacketQueue(wanSideIO.getPacketQueue());
		   lan2Wan.setForwardInterface(wanSideIO);
		   lan2Wan.setAnotherSideFilter(wan2Lan);
		   lan2Wan.setPacketQueue(lanSideIO.getPacketQueue());
//		   lan2Wan.start();
//		   wan2Lan.start();
		   wanSideIO.start();
		}
		else{
		   wanSideIO.stop();
//		   wan2Lan.stop();
		   wanSideIO.setNewPcap(wanPcap);
//		   wan2Lan.start();
		   wanSideIO.start();
		}
		this.start();
	}
	
	public void exitButtonActionPerformed(ActionEvent evt) {
//		System.out.println("exitButton.actionPerformed, event="+evt);
		//TODO add your code for exitButton.actionPerformed
		if(commandTableManager!=null) commandTableManager.stop();
		System.exit(0);
	}
	
	public void clearButtonActionPerformed(ActionEvent evt) {
		System.out.println("clearButton.actionPerformed, event="+evt);
		//TODO add your code for clearButton.actionPerformed
//		this.mainWatch.dispose();
//		this.mainWatch=new MainWatch(this);
//		this.logout.stop();
//		this.mainWatch.clearData(); //
//		this.mainWatch.clearScreen(); //
//		this.logout.start();
	}
	
	private void dumpClearButtonActionPerformed(ActionEvent evt) {
		System.out.println("dumpClearButton.actionPerformed, event="+evt);
		//TODO add your code for dumpClearButton.actionPerformed
		this.logtext.setText("");
	}
	
	private void ipClearButtonActionPerformed(ActionEvent evt) {
		System.out.println("ipClearButton.actionPerformed, event="+evt);
		//TODO add your code for ipClearButton.actionPerformed
		this.appliMessageArea.setText("");
	}
	private void saveSettingButtonActionPerformed(ActionEvent evt) {
		System.out.println("saveSettingButton.actionPerformed, event="+evt);
		//TODO add your code for saveSettingButton.actionPerformed
		this.saveProperties();
	}
	public void saveProperties(){
       
	       try {
	           FileOutputStream saveS = new FileOutputStream(settingFileName);
	           if(setting==null){
	        	   setting=new Properties();
	           }
	           setting.store(saveS,"--- traffic-viewer settings ---");

	        } catch( Exception e){
	           System.err.println(e);
	        } 
	}
	public void loadProperties(){
	       try {
	           setting = new Properties() ;
	           FileInputStream appS = new FileInputStream( settingFileName);
	           setting.load(appS);

	        } catch( Exception e){
//	           System.err.println(e);
	        	this.saveProperties();
//	        	return;
	        } 
	}
	public void setProperties(){
		if(setting==null) return;
	        String w=setting.getProperty("logFilePath");
	        if(w!=null) this.logFilePath=w;
	        w=setting.getProperty("logFileName");
	        if(w!=null) this.logFileField.setText(w);
	        w=setting.getProperty("lanIterfaceTableNumber");
        	try{
        		int x=(new Integer(w)).intValue();
        		this.lanSideInterface=x;
        		int is=interfaceTable.getRowCount();
        		for(int i=0;i<is;i++){
        			interfaceTable.setValueAt("",i,0);			
        		}
                interfaceTable.setValueAt("!", x, 0);
        	}
        	catch(Exception e){
        		System.out.println("...");
        	}        	
	        w=setting.getProperty("wanIterfaceTableNumber");
        	try{
        		int x=(new Integer(w)).intValue();
        		this.wanSideInterface=x;
        		int is=interfaceTable.getRowCount();
        		for(int i=0;i<is;i++){
        			interfaceTable.setValueAt("",i,1);			
        		}
                interfaceTable.setValueAt("!", x, 1);
        	}
        	catch(Exception e){
        		System.out.println("...");
        	}        	
	        
	}
	
	public void writePacketMessage(String x){
		/*
		if(this.grepCheckBox.isSelected()){
			boolean b=false;
			try{
			    b = Pattern.matches(this.regularExpressionField.getText(), x);
			}
			catch(Exception e){
				this.logtext.append("\n!wrong regular expression.\n");
			}
			if(!b) return;
		}
		StringTokenizer st=new StringTokenizer(x);

		String nif="";
		String sMac="";
		String sIP="";
		String dMac="";
		String dIP="";
		if(st!=null) {
			nif=st.nextToken();
		    sMac=st.nextToken();
		    sIP=st.nextToken();
		    dMac=st.nextToken();
		    dIP=st.nextToken();
		    
		    int ix=x.indexOf(sIP);
		    x=x.substring(ix);
		    x=x.substring(sIP.length());
		    ix=x.indexOf(dIP);
		    x=x.substring(ix);
		    x=x.substring(dIP.length());
//		    this.hostsInformation.put(sMac, hinfo);
		    EachHostInformation hx=this.hostsInformation.get(sMac);
		    if(hx==null) {
			    hx=new EachHostInformation();	
			    hx.setNif(nif);			    
		    	this.hostsInformation.put(sMac, hx);
		    }
		    hx.addIP(sIP);
		    hx.addHistory(x);
		    
		    EachHostInformation hy=this.hostsInformation.get(dMac);
		    if(hy==null) {
			    hy=new EachHostInformation();	
			    hy.setNif(nif);			    
		    	this.hostsInformation.put(dMac, hy);
		    }
		    hy.addIP(dIP);
		    hy.addHistory(x);
		    
		}
		String w=this.logtext.getText();
		if(w.length()>10000)
		     w=w.substring(5000);
		w=w+x+"\n";
		try{
	        logtext.setText(w);
		}
		catch(Exception e){
			System.out.println("error @ writePackageMessage:"+e);
		}
		try{
		   JScrollBar sb=tcpdump_log.getVerticalScrollBar();
		   if(sb!=null)
		   sb.setValue(sb.getMaximum());
		}
		catch(Exception e){
			System.out.println("MainFrame.writePacketMessage, sb exception: "+e);
		}
		repaint();
		*/
	}
	public void writeApplicationMessage(String x){
//		if(commandReceiver!=null) {
//			commandReceiver.command("writeApplicationMessage", x);
//		}
		this.parseApplicationCommand("pjc", "writeApplicationMessage", x);
		String w=this.appliMessageArea.getText();
		if(w.length()>10000)
		     w=w.substring(5000);
		w=w+x+"\n";
	    appliMessageArea.setText(w);
		JScrollBar sb=this.appliMessagePane.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
		repaint();
	}
	String logFilePath="C:\\";
	private JButton hideButton;
	private JButton stopButton;
	private JTextField logPathField;
	private void browseFileButtonActionPerformed(ActionEvent evt) {
		System.out.println("browseFileButton.actionPerformed, event="+evt);
		//TODO add your code for browseFileButton.actionPerformed
	    if(this.fileChooser==null){
	    	this.fileChooser=new JFileChooser();
	    }
	    if(this.logFilePath!=null)
		    this.fileChooser.setCurrentDirectory(new File(this.logFilePath));
		this.fileChooser.setFileSelectionMode(fileChooser.FILES_AND_DIRECTORIES);
        int returnVal = this.fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.fileChooser.getSelectedFile();
            String fn=file.toString();
            String fx=file.getName();
            //This is where a real application would open the file.
//            this.fileNameField.setText(fn);
            this.logFilePath=file.getParent();
            this.logPathField.setText(this.logFilePath);
            this.logFileField.setText(fx);
            this.setting.setProperty("logFilePath", logFilePath);
            this.setting.setProperty("logFileName", fx);
        }	    

	}
	
	public void stopButtonActionPerformed(ActionEvent evt) {
		System.out.println("stopButton.actionPerformed, event="+evt);
		//TODO add your code for stopButton.actionPerformed
		
//		if(errout!=null)errout.stop();
		if(lanSideIO==null){
      
		}
		else{
			   lanSideIO.stop();
		}
		if(wanSideIO==null){

		}
		else{
			   wanSideIO.stop();
		}		
	}
	public long getLatestTime(){
		if(this.logManager==null) return 0;
		return logManager.getLatestTime();
	}
	public long getFirstTime(){
		if(this.logManager==null) return 0;
		return logManager.getFirstTime();		
	}
	/*
	public long getScrollBarEndTime(){
		return this.mainWatch.getScrollBarEndTime();
	}
	public long getScrollBarStartTime(){
		return this.mainWatch.getScrollBarStartTime();
	}
	*/
	
	private void hideButtonActionPerformed(ActionEvent evt) {
		System.out.println("hideButton.actionPerformed, event="+evt);
		//TODO add your code for hideButton.actionPerformed
		this.setVisible(false);
	}
//	public PcapAddr getCurrentLanInterfacePcapAddr(){
	public PcapAddress getCurrentLanInterfacePcapAddr() {
//        PcapIf i=networkInterfaces.elementAt(this.lanSideInterface);
		PcapNetworkInterface i=networkInterfaces.elementAt(this.lanSideInterface);
//        List<PcapAddr> alist=i.getAddresses();
		List<PcapAddress> alist=i.getAddresses();
//        PcapAddr addr=null;
		PcapAddress addr=null;
        if(alist.size()>=1)
           addr=alist.get(0);
        return addr;

	}
	public byte[] getCurrentLanInterfaceIpAddr(){
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
//        PcapIf i=networkInterfaces.elementAt(this.lanSideInterface);
		PcapNetworkInterface i=networkInterfaces.elementAt(this.lanSideInterface);
//		PcapAddr addr=this.getIpV4Address(i);
		PcapAddress addr=this.getIpV4Address(i);
//        PcapSockAddr psockaddr=null;
		if(addr==null) return null;
		return addr.getAddress().getAddress();
 
	}
	public byte[] getCurrentWanInterfaceIpAddr(){
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
/*
 *         PcapIf i=networkInterfaces.elementAt(this.wanSideInterface);
		PcapAddr addr=this.getIpV4Address(i);
        PcapSockAddr psockaddr=null;
        */
		PcapNetworkInterface i=networkInterfaces.elementAt(this.wanSideInterface);
		PcapAddress addr=this.getIpV4Address(i);
		if(addr==null) return null;
		return addr.getAddress().getAddress();
 
	}
//	public PcapAddr getIpV4Address(PcapIf iface){
//	public InetAddress getIpV4Address(PcapNetworkInterface iface) {
	public PcapAddress getIpV4Address(PcapNetworkInterface iface) {
//        List<PcapAddr> alist=iface.getAddresses();
		List<PcapAddress> alist=iface.getAddresses();
//        PcapAddr addr=null;
	    PcapAddress addr=null;
//        PcapSockAddr sockaddr=null;
//		InetSocketAddress sockaddr=null;
        System.out.println("address-number="+alist.size());
        if(alist.size()>=1)
        	for(int j=0;j<alist.size();j++){
                addr=alist.get(j);
//                sockaddr=addr.getAddr();
//                InetAddress iaddr=addr.getAddress();
                Class cx=addr.getClass();
                if(cx.getName()=="org.pcap4j.core.PcapIpV4Address") {
                	return addr;
                }
                /*
                if(sockaddr.getFamily()==PcapSockAddr.AF_INET){
                	return addr;
                }
                */
        	}
        return null;
	}
	byte[] currentLanInterfaceNetworkAddr;
	byte[] currentLanInterfaceNetmask;
	private void setCurrentLanInterfaceNetworkAddr(){
		byte[] addr=getCurrentLanInterfaceIpAddr();
		if(addr==null){
			// assume ipv4 only
			addr=new byte[4];
			addr[0]=0;
			addr[1]=0;
			addr[2]=0;
			addr[3]=0;
		}
		int size=addr.length;
		currentLanInterfaceNetworkAddr=new byte[size];
//        PcapIf ifc=networkInterfaces.elementAt(this.lanSideInterface);
		PcapNetworkInterface ifc=networkInterfaces.elementAt(this.lanSideInterface);
//		PcapAddr paddr=this.getIpV4Address(ifc);
		PcapAddress paddr=this.getIpV4Address(ifc);
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
//        PcapSockAddr pmaskaddr=null;
		InetAddress pmaskaddr=null;
		currentLanInterfaceNetmask=null;
        if(paddr!=null){
            pmaskaddr=paddr.getNetmask();
            currentLanInterfaceNetmask=pmaskaddr.getAddress();
		    for(int i=0;i<size;i++){
			   currentLanInterfaceNetworkAddr[i]=
				  (byte)(0xff & ((0xff & addr[i])&( 0xff & currentLanInterfaceNetmask[i])));
		   }	
        }
        else{
        	currentLanInterfaceNetmask=new byte[size];
        	for(int i=0;i<size;i++) currentLanInterfaceNetmask[i]=0x00;
        }
	}
	public byte[] getCurrentLanInterfaceNetworkAddr(){
		return currentLanInterfaceNetworkAddr;
	}
	public byte[] getCurrentLanInterfaceNetmask(){
		return currentLanInterfaceNetmask;
	}
	
	byte[] currentWanInterfaceNetworkAddr;
	byte[] currentWanInterfaceNetmask;
	private void setCurrentWanInterfaceNetworkAddr(){
		byte[] addr=getCurrentWanInterfaceIpAddr();
		if(addr==null){
			// assume ipv4 only
			addr=new byte[4];
			addr[0]=0;
			addr[1]=0;
			addr[2]=0;
			addr[3]=0;
		}
		int size=addr.length;
		currentWanInterfaceNetworkAddr=new byte[size];
		/*
        PcapIf ifc=networkInterfaces.elementAt(this.wanSideInterface);
		PcapAddr paddr=this.getIpV4Address(ifc);
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
        PcapSockAddr pmaskaddr=null;
        */
		PcapNetworkInterface ifc=networkInterfaces.elementAt(this.wanSideInterface);
		currentWanInterfaceNetmask=null;
		PcapAddress paddr=this.getIpV4Address(ifc);
//		addr=paddr.getAddress().getAddress();
		InetAddress pmaskaddr=null;
        if(paddr!=null){
            pmaskaddr=paddr.getNetmask();
            currentWanInterfaceNetmask=pmaskaddr.getAddress();
		    for(int i=0;i<size;i++){
			   currentWanInterfaceNetworkAddr[i]=
				(byte)(0xff & ((0xff & addr[i])&( 0xff & currentWanInterfaceNetmask[i])));
		    }	
        }
        else{
        	currentWanInterfaceNetmask=new byte[size];
        	for(int i=0;i<size;i++) currentWanInterfaceNetmask[i]=0x00;        	
        }

	}
	public byte[] getCurrentWanInterfaceNetworkAddr(){
		return currentWanInterfaceNetworkAddr;
	}
	public byte[] getCurrentWanInterfaceNetmask(){
		return currentWanInterfaceNetmask;
	}
	public boolean equalAddr(byte[] a1, byte[] a2){
		int l1=a1.length;
		int l2=a2.length;
		if(l1!=l2) return false;
		for(int i=0;i<l1;i++){
			byte c1=a1[i];
			byte c2=a2[i];
			if(c1!=c2) return false;
		}
		return true;
	}
	public void copyAddr(byte[] a1, byte[] a2){
		int l1=a1.length;
		int l2=a2.length;
		if(l2>=l1) {
		   for(int i=0;i<l1;i++){
			byte c1=a1[i];
			a2[i]=c1;
		   }
		}
		else{
			   for(int i=0;i<l2;i++){
					byte c1=a1[i];
					a2[i]=c1;
				   }
			
		}
	}
	PacketMonitorFilter monitorFilter;
	TrafficLogManager logManager;
	public void setMonitorFilter(PacketMonitorFilter f){
		monitorFilter=f;
		/*
		if(this.mainWatch!=null) {
			mainWatch.setMonitorFilter(f);
		}
		*/
	}
	
	private JCheckBox getGrepCheckBox() {
		if(grepCheckBox == null) {
			grepCheckBox = new JCheckBox();
			grepCheckBox.setText("grep");
			grepCheckBox.setBounds(148, 10, 68, 23);
		}
		return grepCheckBox;
	}
	
	private JTextField getRegularExpressionField() {
		if(regularExpressionField == null) {
			regularExpressionField = new JTextField();
			regularExpressionField.setBounds(216, 9, 221, 26);
		}
		return regularExpressionField;
	}
	
	private JLabel getRegularExpressionFieldLabel() {
		if(regularExpressionFieldLabel == null) {
			regularExpressionFieldLabel = new JLabel();
			regularExpressionFieldLabel.setText("(regular expression)");
			regularExpressionFieldLabel.setBounds(443, 12, 141, 19);
		}
		return regularExpressionFieldLabel;
	}
	private InetAddress getNetMask(PcapNetworkInterface ni, InetAddress a) {
		List<PcapAddress> list=ni.getAddresses();
		for(int i=0;i<list.size();i++) {
			PcapAddress ax=list.get(i);
			InetAddress axa=ax.getAddress();
			if(a.equals(axa)) {
				InetAddress axm=ax.getNetmask();
				return axm;
			}
		}
		return null;
	}
	private PcapHandle getNifHandleOf(String x) {
        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
//        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
        int timeout = 5 * 1000;           // 5 seconds in millis  
		
		int row=0;
//		for (final PcapIf i : alldevs) {
//		for (final PcapNetworkInterface i:networkInerfaces) {
		for(int i=0;i<networkInterfaces.size();i++) {
			PcapNetworkInterface nif=networkInterfaces.elementAt(i);
			
//			final byte[] mac = i.getHardwareAddress();
//			ArrayList<LinkLayerAddress> macs = i.getLinkLayerAddresses();
			ArrayList<LinkLayerAddress> macs=nif.getLinkLayerAddresses();
//			if (mac == null) {
			if (macs ==null) {
				continue; // Interface doesn't have a hardware address
			}
            String description =  
                (nif.getDescription() != null) ? nif.getDescription()  
                    : "No description available";  
             /*
            List<PcapAddr> alist=i.getAddresses();
            PcapAddr addr=null;
            if(alist.size()>=1)
               addr=alist.get(0);
            */
//            PcapAddr addr=this.getIpV4Address(i);
            PcapAddress addr=this.getIpV4Address(nif);
//            PcapSockAddr psockaddr=null;
            InetAddress psockaddr=null;
//            PcapSockAddr pmaskaddr=null;
            InetAddress pmaskaddr=null;
            if(addr!=null){
//            	psockaddr=addr.getAddr();
            	psockaddr=addr.getAddress();
//                String addrx=FormatUtils.ip(psockaddr.getData());
            	String addrx=psockaddr.getHostAddress();
//                interfaceTable.setValueAt(addrx,row, 5);
                pmaskaddr=addr.getNetmask();
//                String maskx=FormatUtils.ip(pmaskaddr.getData());
                String maskx=pmaskaddr.getHostAddress();
//                interfaceTable.setValueAt(maskx,row, 6);
            }
//			System.out.printf("%s=%s\n", nif.getName(), (macs.get(0)).toString());
//			interfaceTable.setValueAt(nif.getName(), row, 2);
            if(x.equals(nif.getName())) {
            	try {
                	final PcapHandle handle
                     = nif.openLive(snaplen, PromiscuousMode.PROMISCUOUS, timeout);
                	return handle;
            	}
            	catch(Exception e) {
            		return null;
            	}
            }
//			interfaceTable.setValueAt((macs.get(0)).toString(),row,4);
//			interfaceTable.setValueAt(description, row, 3);
//			interfaceTable.setValueAt("",row,0);
//			networkInterfaces.addElement(nif);
			row++;
		}
	
		return null;
	}
	public void run() {
		// TODO Auto-generated method stub
		while(me!=null) {
			updateMacAddrTable();
			try {
			me.sleep((long)300000);
			}
			catch(InterruptedException e) {
				System.out.println("interrupted exception at mainFrame.run");
			}
		}
		
	}
	public void start() {
		if(me==null) {
			me=new Thread(this,"mainFrame");
			me.start();
		}
	}
	public void stop() {
		me=null;
	}

	private int maxCommands=100;	
    
    String TWITTER_CONSUMER_KEY    = "蜿門ｾ励＠縺溘さ繝ｼ繝峨ｒ蜈･蜉�";
    String TWITTER_CONSUMER_SECRET = "蜿門ｾ励＠縺溘さ繝ｼ繝峨ｒ蜈･蜉�";
     
    String TWITTER_ACCESS_TOKEN        = "蜿門ｾ励＠縺溘さ繝ｼ繝峨ｒ蜈･蜉�";
    String TWITTER_ACCESS_TOKEN_SECRET = "蜿門ｾ励＠縺溘さ繝ｼ繝峨ｒ蜈･蜉�";
//    Properties configuration = new Properties();       
    Twitter twitter;
	private Vector<String> putMessageQueue=new Vector();
	private Vector<String> putResultQueue=new Vector();	
	private Vector<String> putTableQueue=new Vector();
	private JTextField auth1ID;
	private JPasswordField password1Field;
	private JLabel auth2Label;
	private JTextField auth2ID;
	private JLabel auth2PassLabel;
	private JPasswordField password2Field;	
	private boolean isError;
	
	public void updateMacAddrTable() {
		if(macAddrTable==null) return;
//		Enumeration<String> keys=macAddrTable.keys();
//		if(keys==null) return;
//		while(keys.hasMoreElements()) {
		for(MacAddressKey x:macAddrTable.keySet()) {
//		   String x=keys.nextElement();
		   MacAddrTableElement y=macAddrTable.get(x);
		   if(y.hasAccess()) {
			   y.setHasAccess(false);
		   }
		   else {
			   macAddrTable.remove(x);
		   }
		}
	}
	public String command(String x, String v) {
		// TODO Auto-generated method stub
		if(x.equals("click")) {
			if(v.equals("clearButton")) {
				this.clearButtonActionPerformed(null);;
			}
		}
		if(x.startsWith("set ")){
			String x2=x.substring("set ".length());
			if(x2.equals("readInterval")){
				/*
				this.readIntervalField.setText(v);
				this.reflectProperties();
				this.saveProperties();
				*/
				return this.parseApplicationCommand("ptGui", x, v).toString();
			}
			else
			if(x2.equals("execInterval")){
				return this.parseApplicationCommand("ptGui", x, v).toString();
			}
			else
			if(x2.equals("sendInterval")){
				return this.parseApplicationCommand("ptGui", x, v).toString();
			}
		}
		else
		if(x.equals("set")) {
			if(v.equals("TermScrollBar")) {
//				this.mainWatch.setTermScrollBar();
			}
		}
		else
		if(x.equals("writePacketMessage")) {
			this.writePacketMessage(v);
		}
		else
		if(x.equals("writeApplicationMessage")) {
			this.writeApplicationMessage(v);
		}
		else
		if(x.equals("get")) {
			if(v.equals("lanInterfaceNetworkAddr")) {
				String rtn=SBUtil.bytes2sip(this.getCurrentLanInterfaceNetworkAddr());
				return rtn;
			}
			else
			if(v.equals("lanInterfaceNetmask")) {
				String rtn=SBUtil.bytes2sip(this.getCurrentLanInterfaceNetmask());
				return rtn;
			}
			if(v.equals("wanInterfaceNetworkAddr")) {
				String rtn=SBUtil.bytes2sip(this.getCurrentWanInterfaceNetworkAddr());
				return rtn;
			}
			else
			if(v.equals("wanInterfaceNetmask")) {
				String rtn=SBUtil.bytes2sip(this.getCurrentWanInterfaceNetmask());
				return rtn;
			}
		}
		// TODO Auto-generated method stub
//		this.putMessage(x+"-"+v);
		if(x.equals("setDeviceID")){
//			this.deviceIDField.setText(v);
//			this.ptGui.command("set deviceIDField",v);
			return this.parseApplicationCommand("ptGui",x,v).toString();
		}
		else
		if(x.equals("writeResult")){
//			this.writeResult(v);
//			this.ptGui.command("writeResult", v);
			return this.parseApplicationCommand("ptGui", x, v).toString();
		}		
		else
		if(x.equals("writeMessage")){
//			this.writeMessage(v);
			return this.parseApplicationCommand("ptGui", x, v).toString();
		}
		else
		if(x.equals("getWikiUrl")){
//			String rtn=this.wikiUrlTextField.getText();
			String rtn=this.parseApplicationCommand("ptGui",x, v).toString();
			return rtn;
		}
		else
		if(x.equals("wikiStartWatching")) {
			String rtn=this.parseApplicationCommand("ptGui", x, v).toString();
			return rtn;
		}
		else
		if(x.startsWith("wikiCommandTable setValueAt ")){
			String rtn=this.parseApplicationCommand("ptGui", x, v).toString();
			/*

            */
		}
		else
		if(x.equals("getDeviceID")){
//			String rtn=this.deviceIDField.getText();
			return this.parseApplicationCommand("ptGui", x, v).toString();
		}
		else
		if(x.equals("getCurrentUrl")){
//			String rtn=this.wikiUrlTextField.getText();
			return this.parseApplicationCommand("ptGui", x, v).toString();
		}
		else	
		if(x.equals("setSecondaryURLList")){
//			this.wikiSecondaryUrlTextField.setText(v);
//            this.setting.put("secondaryURLList", v);
			return this.parseApplicationCommand("ptGui", x, v).toString();
		}
		else
		if(x.equals("setPageName")){
			return this.parseApplicationCommand("ptGui",x,v).toString();
			/*
			String url=""+this.wikiUrlTextField.getText();
			StringTokenizer st=new StringTokenizer(url,"?");
			if(!st.hasMoreElements()) return "ERROR";
			String baseUrl=st.nextToken();
			if(!st.hasMoreElements()) return "ERROR";
			String oldPageName=st.nextToken();
			String newPageUrl=baseUrl+"?"+v;
			putMessage("parseCommand-setPageName-"+url+" to "+newPageUrl);
			this.wikiUrlTextField.setText(""+newPageUrl);
			this.reflectProperties();
			return "OK";
			*/
		}
		else
        {
		   return null;
		}
		return "";
	}
	public PacketMonitorFilter getPacketMonitorFilter() {
		return this.packetMonitorFilter;
	}
	public StringBuffer getOutputText() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isTracing() {
		// TODO Auto-generated method stub
		return false;
	}
	private void clearAll() {
		commandTableManager.clearCommandButtonActionPerformed(null);
	}
	private void addFilterCommand(String x) {
		if(this.commandTableManager!=null) {
			commandTableManager.addFilterCommand(x);
		}
	}
	public StringBuffer parseCommand(String x) {
		StringBuffer rtn=new StringBuffer("");
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String subcmd=Util.skipSpace(x);
		String [] rest=new String[1];
		String [] match=new String[1];
		String[] param1=new String[1];
		String[] param2=new String[2];	
		long time=System.currentTimeMillis();		
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
	    String timex=""+df.format(new Date(time));		
		
//		System.out.println(TAG+"parseCommand("+x+","+v+")");
//	   	service.parseCommand("guiMessage",x);
		int [] intv = new int[1];
		String tweetMessage="";
		if(Util.parseKeyWord(subcmd,"clear ",rest)){
			String l=rest[0]; //subcmd.substring("clear ".length());
			l=Util.skipSpace(l);
			if(!Util.parseKeyWord(l,"all.",rest)) {
				return new StringBuffer("ERROR");
			}
			this.clearAll(); // 
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(subcmd,"add ",rest)){
			String l=Util.skipSpace(rest[0]);
			if(!Util.parseStrConst(l, param1, rest)) return new StringBuffer("ERROR");
			l=Util.skipSpace(rest[0]);
			this.addFilterCommand(param1[0]); //*
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(subcmd, "set ", rest)) {
			String l=rest[0]; 
			l=Util.skipSpace(l);
			if(Util.parseKeyWord(l, "repeating ", rest)) {
				l=rest[0]; 
				l=Util.skipSpace(l);
				if(Util.parseKeyWord(l, "number=", rest)) {
					l=rest[0];
					int[] num=new int[1];
					l=Util.skipSpace(l);
					if(Util.parseInt(l, num, rest)) {
						this.maxRepeatingNumber=num[0];
						return new StringBuffer("OK");						
					}
				}
			}		
			return new StringBuffer("ERROR");
//			return new StringBuffer("OK");			
		}
		else
		if(Util.parseKeyWord(subcmd,"get ",rest)) {
			String l=rest[0]; 
			l=Util.skipSpace(l);
			if(Util.parseKeyWord(l, "hosts.", rest)) {
				return this.getHostsInThisLan();
//				return new StringBuffer("OK");
			}
			else
			if(Util.parseKeyWord(l, "repeating ", rest)) {
				l=rest[0]; 
				l=Util.skipSpace(l);
				BooleanFunctionInterface bfi=null;
				if(Util.parseKeyWord(l, "all ", rest)) {
					l=rest[0]; 
					l=Util.skipSpace(l);
					bfi=nf;
				}
				else
				if(Util.parseKeyWord(l, "unicast ", rest)) {
					l=rest[0]; 
					l=Util.skipSpace(l);
					bfi=uf;
				}
				else
				if(Util.parseKeyWord(l, "multicast ", rest)) {
					l=rest[0]; 
					l=Util.skipSpace(l);
					bfi=mf;
				}
				else
				if(Util.parseKeyWord(l, "broadcast ", rest)) {
					l=rest[0]; 
					l=Util.skipSpace(l);
					bfi=bf;
				}
				
				if(Util.parseKeyWord(l, "over ", rest)) {
					l=rest[0]; 
					l=Util.skipSpace(l);
					int[] num=new int[1];
					if(Util.parseInt(l, num, rest)) {
						return this.getRepeatingTrafficOver(num[0],bfi);
//						if(rtn==null) return new StringBuffer("ERROR");
					}
				}
			}
			else
			if(Util.parseKeyWord(l, "domain-list", rest)) {
			    return getDomainList();
//				return "OK";
			}			
			else
			if(Util.parseKeyWord(l, "dhcp-list", rest)) {
				return this.getDhcpList();
//				return "OK";
			}			
			else
			if(Util.parseKeyWord(l, "arp-list", rest)) {
				return this.getArpList();
//				return "OK";
			}	
			else
			if(Util.parseKeyWord(l, "ip=", rest)) {
				String line=Util.skipSpace(rest[0]);
				String [] strc=new String[1];
				Util.parseStrConst(line, strc, rest);
				return this.getIpList(strc[0]);
//				return "OK";
			}						
			else
			if(Util.parseKeyWord(l, "mac=", rest)) {
				String macPattern=rest[0];
				return this.getMacList(macPattern);
//				return "OK";
			}												
		}
	    StringBuffer line=new StringBuffer( "\""+timex+"\", error, command=\""+subcmd+"\"");
//		parseApplicationCommand("pjc","wikiPutSendBuffer",line);		
//		return "ERROR";
	    return line;

	}
	BroadcastFilter bf=new BroadcastFilter();
	MulticastFilter mf=new MulticastFilter();
	UnicastFilter uf=new UnicastFilter();
	NoFilter nf=new NoFilter();
	
	class MulticastFilter implements BooleanFunctionInterface{
		public boolean isTrue(String x) {
			return isMulticast(x);
		}
		boolean isMulticast(String x) {
			if(x.equals("*")) return false;
			InetAddress ip=null;
			try {
				ip=InetAddress.getByName(x);
				if(ip.isMulticastAddress()) {
					return true;
				}
			}
			catch(Exception e) {
				System.out.println("TrafficController.isMulticast error:"+e);
			}
			return false;
		}		
	}
	class BroadcastFilter implements BooleanFunctionInterface{
		public boolean isTrue(String x) {
			return isBroadcast(x);
		}
		boolean isBroadcast(String x) {
			if(lanSideIO.forwardFilter.isABroadCastIP(x)) return true;
			if(wanSideIO.forwardFilter.isABroadCastIP(x)) return true;
			return false;
		}
	}	
	class UnicastFilter implements BooleanFunctionInterface{
		public boolean isTrue(String x) {
			return isUnicast(x);
		}
		boolean isUnicast(String x) {
			InetAddress ip=null;
			if(bf.isTrue(x)) return false;
			if(mf.isTrue(x)) return false;
            return true;
		}		
	}		
	class NoFilter implements BooleanFunctionInterface{
		public boolean isTrue(String x) {
			return true;
		}
	}			
	public InterpreterInterface lookUp(String x) {
		// TODO Auto-generated method stub
		return null;
	}
	Hashtable<String,InterpreterInterface> applicationTable=new Hashtable();
	//   @Override
	private void putApplicationTable(String name, InterpreterInterface obj){
		   applicationTable.put(name, obj);
	}
	public StringBuffer parseCommandWithReturn(String x, String y) {
		// TODO Auto-generated method stub
		return null;
	}
	private StringBuffer parseApplicationCommand(String appliName, String x, String y) {
		if(applicationTable==null) return null;
		InterpreterInterface appli=applicationTable.get(appliName);
		if(appli==null) return null;
		return appli.parseCommandWithReturn(x,y);
	}
	private StringBuffer getHostsInThisLan() {
		StringBuffer rtn=new StringBuffer("");
		if(this.macHostTable==null) return null;
		DateFormat df=new SimpleDateFormat("yyy/MM/dd HH:mm:ss Z");
		for(String key: this.macHostTable.keySet() ) {
			if(key==null) return rtn;
			EachHostInformation e=this.macHostTable.get(key);
			if(e!=null) {
				String date=df.format(new Date());
//			   parseApplicationCommand("pjc", "wikiPutSendBuffer","cmd=get hosts, date=\""+date+"\", nif="+e.nif+", mac=\""+key+"\", ip=\""+e.getIPs()+"\".\n");
			    StringBuffer line=new StringBuffer("cmd=get hosts, date=\""+date+"\", nif="+e.nif+", mac=\""+key+"\", ip=\""+e.getIPs()+"\".\n");
			    rtn=rtn.append(line);
			}
		}
		return rtn;
	}
	private StringBuffer getDomainList() {
		StringBuffer rtn=new StringBuffer("");
		if(wan2Lan==null) {
			System.out.println("Error... no wan2Lan");
			return new StringBuffer("ERROR");
		}
		if(wan2Lan.domainNameList==null) {
			System.out.println("Error ... no wan2Lan.domainNameList");
			return new StringBuffer("ERROR");
		}
        Map<String,String> domainList=this.wan2Lan.domainNameList;
        if(domainList==null) return null;
        for(String key: domainList.keySet()) {
     	   if(key!=null) {
     		   String aipserver=domainList.get(key);
     		   StringTokenizer st=new StringTokenizer(aipserver);
     		   String aip=st.nextToken();
     		   String server=st.nextToken();
     		   String qhost=st.nextToken();
     		   int timePosition=aip.length()+1+server.length()+1+qhost.length()+1;
     		   String time=aipserver.substring(timePosition);
     		   StringBuffer dnsLine=new StringBuffer("cmd=get domain-list, date="+time+", qhost=\""+qhost+"\", name="+key+", ip="+aip+", server="+server+".\n");
//     		   parseApplicationCommand("pjc","wikiPutSendBuffer",dnsLine);
     		   rtn=rtn.append(dnsLine);
     	   }
        }
        domainList=this.lan2Wan.domainNameList;
        if(domainList==null) return rtn;
        for(String key: domainList.keySet()) {
     	   if(key!=null) {
     		   String aipserver=domainList.get(key);
     		   StringTokenizer st=new StringTokenizer(aipserver);
     		   String aip=st.nextToken();
     		   String server=st.nextToken();
     		   String qhost=st.nextToken();     		   
     		   int timePosition=aip.length()+1+server.length()+1+qhost.length()+1;
     		   String time=aipserver.substring(timePosition);
     		   StringBuffer dnsLine=new StringBuffer("cmd=get domain-list, date="+time+", qhost="+qhost+", name="+key+", ip="+aip+", server="+server+".\n");
//     		   parseApplicationCommand("pjc","wikiPutSendBuffer",dnsLine);
     		   rtn=rtn.append(dnsLine);
     	   }
        }	
        return rtn;
	}
	private StringBuffer writeDhcpList(TrafficHistory dhcpH) {
		StringBuffer rtn=new StringBuffer("");
        if(dhcpH==null) return null;
        Vector<String> dhcpList=dhcpH.serialize();
        for(int i=0;i<dhcpList.size();i++) {
     		   String dhcpRecord=dhcpList.elementAt(i);
     		   if(dhcpRecord==null) continue;
     		   String [] rest=new String[1];
     		   String [] timea=new String[1];
     		   dhcpRecord=Util.skipSpace(dhcpRecord);
     		   if(!Util.parseStrConst(dhcpRecord, timea, rest)) continue;
     		   String time=timea[0];
     		   dhcpRecord=rest[0];
      		   dhcpRecord=Util.skipSpace(dhcpRecord);     		   
     		   StringTokenizer st=new StringTokenizer(dhcpRecord);
     		   String nif=st.nextToken();
     		   String smac=st.nextToken();
     		   String sip=st.nextToken();
     		   String dmac=st.nextToken();
     		   String dip=st.nextToken();
               String uip=st.nextToken();
               String rip=st.nextToken();
               String srip=st.nextToken();
               String gip=st.nextToken();
               String umac=st.nextToken();
               String option=st.nextToken();
     		   StringBuffer dhcpLine=new StringBuffer("cmd=get dhcp-list, date=\""+time+
     				   "\", nif="+nif+
     				   ", smac="+smac+", sip="+sip+", dmac="+dmac+", dip="+dip+
     				   ", uip="+uip+", rip="+rip+", srip="+srip+", gip="+gip+
     				   ", umac="+umac+", option="+option+
     				   ".\n");
//     		   parseApplicationCommand("pjc","wikiPutSendBuffer",dhcpLine);
     		   rtn=rtn.append(dhcpLine);
        }
		return rtn;
	}
	private StringBuffer getDhcpList() {
		StringBuffer rtn=writeDhcpList(this.wan2Lan.dhcpHistory);
		if(rtn==null) return null;
		rtn=rtn.append(writeDhcpList(this.lan2Wan.dhcpHistory));
		return rtn;
	}	
	private StringBuffer writeArpList(TrafficHistory arpH) {
		StringBuffer rtn=new StringBuffer("");
        if(arpH==null) return null;
        Vector<String> arpList=arpH.serialize();
        for(int i=0;i<arpList.size();i++) {
     		   String arpRecord=arpList.elementAt(i);
     		   if(arpRecord==null) continue;
     		   String [] rest=new String[1];
     		   String [] timea=new String[1];
     		   arpRecord=Util.skipSpace(arpRecord);
     		   if(!Util.parseStrConst(arpRecord, timea, rest)) continue;
     		   String time=timea[0];
     		   arpRecord=rest[0];
      		   arpRecord=Util.skipSpace(arpRecord);     		   
     		   StringTokenizer st=new StringTokenizer(arpRecord);
     		   String nif=st.nextToken();
     		   String smac=st.nextToken();
     		   String sip=st.nextToken();
     		   String dmac=st.nextToken();
     		   String dip=st.nextToken();
               String arpx=st.nextToken();
     		   StringBuffer arpLine=new StringBuffer("cmd=get arp-list, date=\""+time+
     				   "\", nif="+nif+
     				   ", smac="+smac+", sip="+sip+", dmac="+dmac+", dip="+dip+
     				   ", arp="+arpx+
     				   ".\n");
//     		   parseApplicationCommand("pjc","wikiPutSendBuffer",arpLine);
     		   rtn=rtn.append(arpLine);
        }
		return rtn;
	}
	private StringBuffer getArpList() {
		StringBuffer rtn;
		rtn=writeArpList(this.wan2Lan.arpHistory);
		if(rtn==null) return rtn;
		rtn.append(writeArpList(this.lan2Wan.arpHistory));
		return rtn;
	}	

	private StringBuffer getRepeatingTrafficOver(int x, BooleanFunctionInterface f) {
		if(hostsInformation==null) return null;
		StringBuffer rtn=new StringBuffer("");
		if(x<10000) x=10000;
		try {
		for(String key: this.hostsInformation.keySet() ) {
			EachHostInformation e=this.hostsInformation.get(key);
			// listing the repeating sending packet to the same destination hosts
			if(e==null) continue; 
			int historyNumber=e.history.size();
//			System.out.print("getRepeating...key="+key);			
//			System.out.println(" hn="+historyNumber);
			TimeAndTraffic traffic1=e.history.elementAt(0);
			if(!traffic1.isIP()) continue;
			String destIP1=traffic1.parsePacket.getDestinationIpString();
			if(!f.isTrue(destIP1)) continue;
			TimeAndTraffic last=e.history.elementAt(historyNumber-1);
			long t1=traffic1.getTime();
			long t2=last.getTime();
//			System.out.println("t2-t1="+(t2-t1));
			if(t2-t1<x) continue;		
			if(historyNumber>=2) {
//			    parseApplicationCommand("pjc","wikiPutSendBuffer","cmd=get repeating, "+traffic1.getTraffic()+".\n");				
				StringBuffer line=new StringBuffer("cmd=get repeating, "+traffic1.getTraffic()+".\n");
				rtn.append(line);
			}
			else {
				continue;
			}			
			int n=maxRepeatingNumber;
			if(n>historyNumber) n=historyNumber;
			for(int i=1;i<n;i++) {
				int ix=historyNumber-n+i;
				TimeAndTraffic traffic2=e.history.elementAt(ix);
//				parseApplicationCommand("pjc","wikiPutSendBuffer"," cmd=get repeating, "+traffic2.getTraffic()+".\n");				
				StringBuffer line=new StringBuffer(" cmd=get repeating, "+traffic2.getTraffic()+".\n");
				rtn.append(line);
			}     
		}
		return rtn;
		}
		catch(Exception e) {
			System.out.println("TrafficController.getRepeatingTrafficOver error:"+e);
			return null;
		}
	}	
	boolean isBroadcastMac(String x) {
        if(x.equals("ff:ff:ff:ff:ff:ff")) return true;
		return false;
	}
	String deleteDoubleQuotations(String x) {
		String rtn="";
		String xx=Util.skipSpace(x);
		try {
			char c=xx.charAt(0);
			if(c=='"') {
				xx=xx.substring(1);
			}
		  while(xx.length()>0) {
			c=xx.charAt(0);
			if(c=='"') {
				  return rtn;
			}
			if(c=='\\'){
				rtn=rtn+c;
				xx=xx.substring(1);
				if(x.length()>0) {
					c=xx.charAt(0);
					rtn=rtn+c;
					xx=xx.substring(1);
				}
		    }
			else {
			    rtn=rtn+c;
    			xx=xx.substring(1);
			}
		  }
		}
		catch(Exception e) {
			System.out.println("TrafficController.deleteDoubleQuotation x="+x+" rtn="+rtn+" e:"+e);
		}
		return rtn;
	}
	Vector<InetAddress> bcastAddresses;	
	public void setBroadcastAddresses() {
		if(bcastAddresses==null) {
	      try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
               Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
               for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            	   if(bcastAddresses==null) {
            		   bcastAddresses=new Vector();
            	   }
                   bcastAddresses.add(inetAddress);
               }
            }
	      }
	      catch(Exception e) {
	    	 System.out.println("TrafficController.setBroadcastAddresses() e:"+e);
	      }
		}
    }
    private void startAllButtonActionPerformed(ActionEvent evt) {
    	System.out.println("startButton.actionPerformed, event="+evt);
    	//TODO add your code for startButton.actionPerformed
    	this.startButtonActionPerformed(null);
		this.command("wikiStartWatching", "");
    }

    HashMap <String, EachHostInformation> macHostTable;
	@Override
	public void receive(String command, long no, int nic, ParsePacket parsePacket) {
		// TODO Auto-generated method stub
		if(parsePacket==null) return;
		ParsePacket pp=parsePacket.clone();
		if(pp==null) return;

		String nif="";
		String sMac="";
		String sIP="";
		String dMac="";
		String dIP="";
			nif=""+nic;
		    sMac=pp.getSourceMacString();
		    sIP=pp.getSourceIpString();
		    dMac=pp.getDestinationMacString();
		    dIP=pp.getDestinationIpString();
		    String key="";
		    if(pp.ipv4!=null) {
		    	key=sIP+"-"+dIP;
		    }
		    else
		    if(pp.ipv6!=null) {
		    	key=sIP+"-"+dIP;
		    }
		    else {
		    	key=sMac+"-"+dMac;
		    }
		    EachHostInformation hx=this.hostsInformation.get(key);
		    if(hx==null) {
			    hx=new EachHostInformation();	
			    hx.setNif(nif);			    
		    	this.hostsInformation.put(key, hx);
		    }
		    hx.addIP(sIP);
		    hx.addHistory(no,nic,pp);
		    
		    if(macHostTable==null) {
		    	macHostTable=new HashMap();
		    }
		    EachHostInformation mh=this.macHostTable.get(sMac);
		    if(mh==null) {
			    mh=new EachHostInformation();	
			    mh.setNif(nif);			    
		    	this.macHostTable.put(sMac, hx);
		    }
		    mh.addIP(sIP);
		    mh.addHistory(no,nic,pp);		    
		    /*
		    EachHostInformation hy=this.hostsInformation.get(dMac);
		    if(hy==null) {
			    hy=new EachHostInformation();	
			    hy.setNif(nif);			    
		    	this.hostsInformation.put(dMac, hy);
		    }
		    hy.addIP(dIP);
		    hy.addHistory(no,nic,parsePacket);
		    */
		    
		String w=this.logtext.getText();
		if(w.length()>10000)
		     w=w.substring(5000);
		String x=pp.getTimeS()+" "+no+" nif="+nif+" smac="+sMac+" dmac="+dMac+
				    " sip="+sIP+" dip="+dIP+" protocol="+pp.protocol+
				    " payloadLength="+pp.payloadLength+
				    " payload="+pp.getPayloadString();
		if(this.grepCheckBox.isSelected()){
			boolean b=false;
			try{
			    b = Pattern.matches(this.regularExpressionField.getText(), x);
			}
			catch(Exception e){
				this.logtext.append("\n!wrong regular expression.\n");
			}
			if(!b) return;
		}		
		w=w+x+"\n";

		
		try{
	        logtext.setText(w);
		}
		catch(Exception e){
			System.out.println("error @ writePackageMessage:"+e);
		}
		try{
		   JScrollBar sb=tcpdump_log.getVerticalScrollBar();
		   if(sb!=null)
		   sb.setValue(sb.getMaximum());
		}
		catch(Exception e){
			System.out.println("MainFrame.writePacketMessage, sb exception: "+e);
		}
		repaint();		
	}	
	
	public StringBuffer getIpList(String pattern) {
		StringBuffer rtn=new StringBuffer("");
		for(String key: this.hostsInformation.keySet() ) {
			EachHostInformation e=this.hostsInformation.get(key);
			// listing the repeating sending packet to the same destination hosts
			if(e!=null) {
				HashMap<String,TimeAndTraffic> previousHosts= new HashMap();				
			   int historyNumber=e.history.size();
			   for(int i=0;i<historyNumber-1;i++) {
				  TimeAndTraffic traffic1=e.history.elementAt(i);
				  String destIP1=traffic1.parsePacket.getDestinationIpString();
				  if(isMatchIpV4Address(destIP1, pattern)) {
//					  parseApplicationCommand("pjc","wikiPutSendBuffer"," cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  StringBuffer line=new StringBuffer(" cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  rtn=rtn.append(line);
				  }
				  String sourceIP1=traffic1.parsePacket.getSourceIpString();
				  if(isMatchIpV4Address(sourceIP1,pattern)) {
//				     parseApplicationCommand("pjc","wikiPutSendBuffer"," cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  StringBuffer line=new StringBuffer(" cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  rtn=rtn.append(line);
				  }
			   }
			     
		    }
		}		
		return rtn;
	}
	public StringBuffer getMacList(String pattern) {
		StringBuffer rtn=new StringBuffer("");
		for(String key: this.hostsInformation.keySet() ) {
			EachHostInformation e=this.hostsInformation.get(key);
			// listing the repeating sending packet to the same destination hosts
			if(e!=null) {
				HashMap<String,TimeAndTraffic> previousHosts= new HashMap();				
			   int historyNumber=e.history.size();
			   for(int i=0;i<historyNumber-1;i++) {
				  TimeAndTraffic traffic1=e.history.elementAt(i);
				  String destMac1=traffic1.parsePacket.getDestinationMacString();
				  if(isMatchMacAddress(destMac1, pattern)) {
//					  parseApplicationCommand("pjc","wikiPutSendBuffer"," cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  StringBuffer line=new StringBuffer(" cmd=get Mac=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  rtn=rtn.append(line);
				  }
				  String sourceMac1=traffic1.parsePacket.getSourceMacString();
				  if(isMatchMacAddress(sourceMac1,pattern)) {
//				     parseApplicationCommand("pjc","wikiPutSendBuffer"," cmd=get IP=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  StringBuffer line=new StringBuffer(" cmd=get Mac=\""+pattern+"\", "+traffic1.getTraffic()+".\n");
					  rtn=rtn.append(line);
				  }
			   }
			     
		    }
		}		
		return rtn;
	}
    public boolean isMatchIpV4Address(String x, String y){
//		String ax[]=new String[4];
//		String ay[]=new String[4];
		StringTokenizer stx=new StringTokenizer(x,".");
		if(stx==null) return false;
		StringTokenizer sty=new StringTokenizer(y,".");
		if(sty==null) return false;
		for(int i=0;stx.hasMoreElements();i++){
			String ax="",ay="";
			try{
			   ax=stx.nextToken();
			   ay=sty.nextToken();
			}
			catch(Exception e){
				return false;
			}
			if(!(ax.equals(ay))){
				if((!ax.equals("*")) && (!ay.equals("*"))){
					return false;
				}
			}
		}
		return true;
    }
    public boolean isMatchMacAddress(String x, String y){
//		String ax[]=new String[4];
//		String ay[]=new String[4];
		StringTokenizer stx=new StringTokenizer(x,":");
		if(stx==null) return false;
		StringTokenizer sty=new StringTokenizer(y,":");
		if(sty==null) return false;
		for(int i=0;stx.hasMoreElements();i++){
			String ax="",ay="";
			try{
			   ax=stx.nextToken();
			   ay=sty.nextToken();
			}
			catch(Exception e){
				return false;
			}
			if(!(ax.equals(ay))){
				if((!ax.equals("*")) && (!ay.equals("*"))){
					return false;
				}
			}
		}
		return true;
    }    
	@Override
	public void receive(String command, ParsePacket parsePacket) {
		// TODO Auto-generated method stub
		
	}	
}


