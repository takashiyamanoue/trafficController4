package org.yamaLab.trafficController;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.yamaLab.pukiwikiCommunicator.connector.PukiwikiJavaApplication;
import org.yamaLab.pukiwikiCommunicator.connector.SaveButtonDebugFrame;
import org.yamaLab.pukiwikiCommunicator.language.CommandReceiver;
import org.yamaLab.pukiwikiCommunicator.language.InterpreterInterface;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;

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
public class CommandTableManager extends JFrame
implements PukiwikiJavaApplication, Runnable
{
	private Thread me;
	private JLabel urlLabel;
	private JButton disConnectButton;
	private JButton clearCommandButton;
	private JScrollPane commandAreaPane;
	private JLabel resultLabel;
	private JComboBox commandIntervalCombo;
	private JComboBox returnIntervalCombo;
	private JLabel commandIntervalLabel;
	private JLabel returnIntervalLabel;
	private JLabel messageLabel;
	private JTextArea resultArea;
	private JScrollPane resultPane;
	private JScrollPane messagePane;
	private JTextArea messageArea;
	private JTextArea commandArea;
	private JLabel commandLabel;
	public JToggleButton connectButton;
	private JTextField urlTextField;
	private PacketMonitorFilter packetMonitorFilter;
	private JTabbedPane mainTabPane;
//    private JPanel mainPanel;
    private JRadioButton showDebuggerButton;
    public JCheckBox onlineCommandRefreshButton;
    private Properties setting;
    public JToggleButton startWatchingButton;
    private JButton endWatchingButton;
	private JTable commandTable;


    public CommandTableManager(){
    	super();
    }
	public CommandTableManager( JTabbedPane tp){
		if(tp==null) tp=new JTabbedPane();
		this.mainTabPane=tp;
		this.initGUI();

//		this.start();
	}
	public void setSetting(Properties x, SaveButtonDebugFrame d) {
		this.setting=x;
		this.setProperties();
		if(d==null) {
			d=new SaveButtonDebugFrame(this);
		}
		this.debugger=d;
		this.debugger.setVisible(false);		
		this.debugger.setSetting(x);
	}
	
	public void start(){
		if(me==null){
			me=new Thread(this,"PukiwikiCommunicator");
			me.start();
		}
	}

	public void stop(){
		me=null;
	}
	
//	@Override
	public String getOutput() {
		// TODO Auto-generated method stub
		return this.result;
	}
	int p2CommandTable=0;
	public void addFilterCommand(String com) {
		this.commandTable.setValueAt(p2CommandTable,p2CommandTable, 0);
		this.commandTable.setValueAt(com, p2CommandTable, 1);
		this.writeMessage("setting "+com);
		p2CommandTable++;
		com=readSpaces(com);
		if(com.startsWith("lan2wan ")){
			com=com.substring("lan2wan ".length());
			this.commandInterpreter(packetFilterLan,com);
		}
		else
		if(com.startsWith("wan2lan ")){
			com=com.substring("wan2lan ".length());
			this.commandInterpreter(packetFilterWan,com);					
		}
		else{
			this.commandInterpreter(packetMonitorFilter,com);
		}		
	}

//	@Override
	public void setInput(String x) {
		// TODO Auto-generated method stub
		if(this.packetMonitorFilter!=null){
			this.packetMonitorFilter.clear();
		}
		if(this.packetFilterLan!=null){
			this.packetFilterLan.clear();
		}
		if(this.packetFilterWan!=null){
			this.packetFilterWan.clear();
		}
		this.writeMessage("setInput("+x+")");
//		this.commandArea.setText("");
		for(int i=0;i<maxCommands;i++){
			this.commandTable.setValueAt("", i, 0);
			this.commandTable.setValueAt("", i, 1);
		}
		StringTokenizer st=new StringTokenizer(x,"\n");
		p2CommandTable=0;
		while(st.hasMoreElements()){
			String l=st.nextToken();
			if(p2CommandTable>=maxCommands){
				this.writeMessage("too many filter commands.");
				return;
			}
			if(l.startsWith("command:")){
				String com=l.substring("command:".length());
//				this.commandArea.append(com+"\n");
                this.addFilterCommand(com);
			}
			else
			if(l.startsWith("#")){
				
			}
		}
	}
	private SaveButtonDebugFrame debugger;
//	@Override
	public void setSaveButtonDebugFrame(SaveButtonDebugFrame f) {
		// TODO Auto-generated method stub
		debugger=f;
	}
	
	static public void main(String[] args){
		new CommandTableManager(null);
	}
	long lastCommandRequest;
	long lastReturnOutput;
//	@Override
	public void run() {
		lastCommandRequest=0;
		lastReturnOutput=0;
		// TODO Auto-generated method stub
		while(me!=null){
			long time=System.currentTimeMillis();
//			long commandInterval=getCommandRequestInterval();
			long returnInterval=getResultReturnInterval();
			/*
			if(time>lastCommandRequest+commandInterval){
				this.writeMessage("connectionButton");
				this.connectButtonActionPerformed(null);
				lastCommandRequest=System.currentTimeMillis();
			}
			*/
			if(returnInterval!=0L && time>lastReturnOutput+returnInterval){
				this.writeMessage("writeResult.");
				this.writeResult();
				lastReturnOutput=System.currentTimeMillis();
			}
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
				
			}
		}
		
	}
	
	private long getCommandRequestInterval(){
		long rtn,rx;
		String x=(String)(this.commandIntervalCombo.getSelectedItem());
		StringTokenizer st=new StringTokenizer(x,"-");
		String numx=st.nextToken();
		String unit=st.nextToken();
		rx=(new Long(numx)).longValue();
		if(unit.equals("sec")){
			rtn=rx*1000;
		}
		else
		if(unit.equals("min")){
			rtn=rx*60*1000;
		}
		else
		if(unit.equals("h")){
			rtn=rx*60*60*1000;
		}
		else{
			rtn=rx;
		}
//		this.messageArea.append("getCommandRequestInterval="+rtn+"\n");
		return rtn;
	}
	private long getResultReturnInterval(){
		long rtn,rx;
		/*
		String x=(String)(this.returnIntervalCombo.getSelectedItem());
		StringTokenizer st=new StringTokenizer(x,"-");
		String numx=st.nextToken();
		String unit=st.nextToken();
		rx=(new Long(numx)).longValue();
		if(unit.equals("sec")){
			rtn=rx*1000;
		}
		else
		if(unit.equals("min")){
			rtn=rx*60*1000;
		}
		else
		if(unit.equals("h")){
			rtn=rx*60*60*1000;
		}
		else{
			rtn=rx;
		}
//		this.messageArea.append("getResultReturnInterval="+rtn+"\n");
		return rtn;
		*/
		if(setting==null) return 0L;
		String ix= this.setting.getProperty("sendInterval");
		if(ix==null) return 0L;
		rtn=(new Long(ix)).longValue();
		return rtn;
	}
	private int maxCommands=20;
	private void initGUI() {
		try{
//		this.mainPanel=new JPanel();
			Container thisframe=this.getContentPane();
		thisframe.setLayout(null);
		if(this.mainTabPane!=null) {
			this.mainTabPane.add("CommandTableManager",thisframe);
		}
		else{
			getContentPane().setLayout(null);
			getContentPane().add(this);
		}
		/* */
			{
				urlLabel = new JLabel();
				thisframe.add(urlLabel);
				urlLabel.setText("manager url:");
				urlLabel.setBounds(1, 25, 105, 24);
			}
			{
				urlTextField = new JTextField();
				thisframe.add(urlTextField);
				urlTextField.setBounds(95, 21, 446, 30);
			}
			{
				connectButton = new JToggleButton();
				thisframe.add(connectButton);
				connectButton.setText("connect");
				connectButton.setBounds(541, 21, 110, 30);
				connectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
								connectButtonActionPerformed(null);								
					}
				});
			}
			{
				disConnectButton = new JButton();
				thisframe.add(disConnectButton);
				disConnectButton.setText("Disconnect");
				disConnectButton.setBounds(651, 21, 120, 30);
				disConnectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						disConnectButtonActionPerformed(evt);
					}
				});
			}
			/* */
			{
				commandLabel = new JLabel();
				thisframe.add(commandLabel);
				commandLabel.setText("command:");
				commandLabel.setBounds(1, 92, 109, 33);
			}
			{
				commandAreaPane = new JScrollPane();
				thisframe.add(commandAreaPane);
				commandAreaPane.setBounds(110, 93, 547, 82);
				{
					/*
					commandArea = new JTextArea();
					commandAreaPane.setViewportView(commandArea);
					*/
					String [] oneComLine=new String[]{"",""};
					String [][] comLines =new String[maxCommands][];
					for(int i=0;i<maxCommands;i++){
						comLines[i]=new String[]{"",""};
					}
					DefaultTableModel tableModel= new DefaultTableModel(
							comLines ,
							new String[] { "No","Command" });
					
					commandTable = new JTable();
					commandTable.setModel(tableModel);
					commandAreaPane.setViewportView(commandTable);
				}
			}
			{
				resultLabel = new JLabel();
				thisframe.add(resultLabel);
				resultLabel.setText("result:");
				resultLabel.setBounds(0, 181, 105, 29);
			}
			{
				resultPane = new JScrollPane();
				thisframe.add(resultPane);
				resultPane.setBounds(111, 181, 659, 143);
				{
					resultArea = new JTextArea();
					resultPane.setViewportView(resultArea);
//					resultArea.setPreferredSize(new java.awt.Dimension(547, 79));
				}
			}
			{
				messageLabel = new JLabel();
				thisframe.add(messageLabel);
				messageLabel.setText("message:");
				messageLabel.setBounds(2, 330, 101, 28);
			}
			{
				messagePane = new JScrollPane();
				thisframe.add(messagePane);
				messagePane.setBounds(111, 330, 659, 72);
				{
					messageArea = new JTextArea();
					messagePane.setViewportView(messageArea);
				}
			}


			{
				clearCommandButton = new JButton();
				thisframe.add(clearCommandButton);
				clearCommandButton.setText("Clear");
				clearCommandButton.setBounds(662, 145, 80, 25);
				clearCommandButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearCommandButtonActionPerformed(evt);
					}
				});
			}
			{
			   thisframe.setSize(600, 450);
			   thisframe.setPreferredSize(new java.awt.Dimension(788, 437));
			}
			this.setVisible(false);
			this.setSize(804, 454);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
//			this.setTitle("PukiwikiCommunicator");
	}
	private void debuggerButtonActionPerformed(ActionEvent evt){
		if(showDebuggerButton.isSelected()){
			this.debugger.setVisible(true);
		}
		else{
			this.debugger.setVisible(false);
		}
	}
	public void connectButtonActionPerformed(ActionEvent evt) {
		System.out.println("connectButton.actionPerformed, event="+evt);
		//TODO add your code for connectButton.actionPerformed
		(new Thread(){
			public void run(){
		      if(connectButton.isSelected()){
			         if(onlineCommandRefreshButton.isSelected()){
				            debugger.readFromPukiwikiPageAndSetData(urlTextField.getText());
				            repaint();
				            setting.setProperty("managerUrl", urlTextField.getText());
				         }
		      }
		      else{
		    	  if(!onlineCommandRefreshButton.isSelected()){
		    		  
		    	  }
		      }
			}
		}).start();


	}
	/*
	 * command interpreter
	 *  get ip=<ip addr>
	 */

	public String readSpaces(String x){
		while(x.startsWith(" ")){
			x=x.substring(" ".length());
		}
		return x;
	}
	private boolean isNumber(char x){
		if('0'<=x && x<='9') return true;
		return false;
	}
	private boolean isLetter(char x, char y){
		if(x==y) return true;
		return false;
	}
	public String getIpV4AddrFromHead(String x, String[] rest){
		String a[]=new String[4];
		a[0]="0"; a[1]="0"; a[2]="0"; a[3]="0";
		String w="";
		String[] restx=new String [1];
		String xx=readSpaces(x);
		String ns=parseNumbers(xx,restx);
		int i=0;
		if(ns.equals("")){
			restx[0]=x;
			return "";
		}
		a[i]=ns;
		xx=restx[0];
		i++;
		while(true){
			String px=parseString(".",xx,restx);
			if(px.equals("")) break;
			xx=restx[0];
			String n=parseNumbers(xx,restx);
			if(n.equals("")) {
				n=parseString("*",xx,restx);
				if(n.equals("")){
					break;
				}
			}
			xx=restx[0];
			a[i]=n;
			i++;
		}

		rest[0]=xx;
		return a[0]+"."+a[1]+"."+a[2]+"."+a[3];
	}
	public String getNumberFromHead(String x, String[] rest){
		String a[]=new String[4];
		String w="";
		String xx=readSpaces(x);
		while(true){
		  if(xx.equals("")) break;
		  if(xx==null) break;
		  if(isNumber(xx.charAt(0))){
			w=w+xx.charAt(0);
			xx=xx.substring(1);
		  }
		  else{
			  break;
		  }
		}
		rest[0]=xx;
		return w;
	}
	public String getStringConst(String w){
		String rtn="";
		int len=w.length();
		if(len>80) return null;
		int i=0;
		char c='\0';
		if(!w.startsWith("\"")) return null;
		c=w.charAt(i);
		while(c!='\"'){
			if(i>len) return null;
			if(c=='\\'){
				rtn=rtn+c;
				i++;
				c=w.charAt(i);
				rtn=rtn+c;
			}
			rtn=rtn+c;
			i++;
			c=w.charAt(i);
		}
		return rtn;
	}
	public String parseNumbers(String x,String[] w){
		String rtn="";
		while(true){
			if(x==null) break;
			if(x.equals("")) break;
			char c=x.charAt(0);
			if(isNumber(c)){
			   rtn=rtn+c;
			   x=x.substring(1);
			}
			else{
				break;
			}
		}
		w[0]=x;
		return rtn;
		
	}
	public String parseString(String c,String x, String[] w){
		String rtn="";
		if(x.startsWith(c)){
			w[0]=x.substring(c.length());
			rtn=c;
		}
		return rtn;
	}
	public boolean subCommandInterpreter(FilterInterface f, String command, String subCommand){
		String y=readSpaces(subCommand);
		if(y.startsWith("ip=")){
			y=y.substring("ip=".length());
			String w=readSpaces(y);
			String [] rest=new String[1];
			String ipa=getIpV4AddrFromHead(w,rest);
			if(ipa==null){
				ipa="0.0.0.0";
			}
			String[] args=new String[3];
			args[0]=ipa;
			w=readSpaces(rest[0]);
			if(w.equals("")){
			}
			else
			if(w.startsWith("to ")){
				w=w.substring("to ".length());
				w=readSpaces(w);
				String ipb=getIpV4AddrFromHead(w,rest);
				if(ipb==null){
					ipb="0.0.0.0";
				}
				args[1]=ipb;
				w=readSpaces(rest[0]);
				if(w.startsWith(":")){
					w=w.substring(":".length());
					w=readSpaces(w);
					String port=getNumberFromHead(w,rest);
					if(port==null){
						port="0";
					}
					args[2]=port;
				}
				else{
				}
			}
			if(f!=null){
			   f.addFilter(command+" ip=",args);
			}
			return true;
		}
		if(y.startsWith("sip=")){
			y=y.substring("sip=".length());
			String w=readSpaces(y);
			String [] rest=new String[1];
			String ipa=getIpV4AddrFromHead(w,rest);
			if(ipa==null){
				ipa="0.0.0.0";
			}
			String[] args=new String[3];
			args[0]=ipa;
			w=readSpaces(rest[0]);
			if(w.equals("")){
			}
			else
			if(w.startsWith("to ")){
				w=w.substring("to ".length());
				w=readSpaces(w);
				String ipb=getIpV4AddrFromHead(w,rest);
				if(ipb==null){
					ipb="0.0.0.0";
				}
				args[1]=ipb;
				w=readSpaces(rest[0]);
				if(w.startsWith(":")){
					w=w.substring(":".length());
					w=readSpaces(w);
					String port=getNumberFromHead(w,rest);
					if(port==null){
						port="0";
					}
					args[2]=port;
				}
				else{
				}
			}
			if(f!=null){
			   f.addFilter(command+" sip=",args);
			}
			return true;
		}
		if(y.startsWith("includes ")){
			y=y.substring("includes ".length());
			String w=readSpaces(y);
			StringTokenizer st=new StringTokenizer(w," ");
			String sc=st.nextToken();
			if(sc==null) return false;
			String[] args=new String[1];
			args[0]=sc;
			if(f!=null){
			   f.addFilter(command+" includes ",args);
			}
			return true;
		}
		if(y.startsWith("startsWith ")){
			y=y.substring("startsWith ".length());
			String w=readSpaces(y);
			StringTokenizer st=new StringTokenizer(w," ");
			String sc=st.nextToken();
			if(sc==null) return false;
			String[] args=new String[1];
			args[0]=sc;
			if(f!=null){
			   f.addFilter(command+" startsWith ",args);
			}
			return true;
		}
		return false;        
	}
	public boolean commandInterpreter(FilterInterface f, String x){
		String y=readSpaces(x);
		if(y.startsWith("get ")){
			y=y.substring("get ".length());
			y=readSpaces(y);
			return this.subCommandInterpreter(f,"get",y);
		}
		if(y.startsWith("drop ")){
			y=y.substring("drop ".length());
			y=readSpaces(y);
			return this.subCommandInterpreter(f, "drop", y);
		}	
		if(y.startsWith("return-syn-ack ")){
			y=y.substring("return-syn-ack ".length());
			y=readSpaces(y);
			return this.subCommandInterpreter(f, "return-syn-ack", y);
		}	
		if(y.startsWith("forward ")){
			y=y.substring("forward ".length());
			y=readSpaces(y);
			return this.subCommandInterpreter(f, "forward", y);			
		}
		if(y.startsWith("dns-intercept ")){
			y=y.substring("forward ".length());
			y=readSpaces(y);
			return this.subCommandInterpreter(f, "dns-intercept", y);			
		}
		return false;
	}
	public void setMonitorPacketFilter(PacketMonitorFilter f){
		this.packetMonitorFilter=f;
	}
	PacketFilter packetFilterWan;
	PacketFilter packetFilterLan;
	public void setPacketFilterWan(PacketFilter f){
		packetFilterWan=f;
	}
	public void setPacketFilterLan(PacketFilter f){
		packetFilterLan=f;
	}
	String result="";
	Vector <String> resultVector;
	public void writeResult(){
		System.out.println("writeResult");
		String x="";
		if(this.packetMonitorFilter==null) return;
		this.resultVector=this.packetMonitorFilter.getResults();
		if(this.resultVector!=null) {
		   for(int i=0;i<resultVector.size();i++){
			   String w=resultVector.elementAt(i);
			   x=x+w;
//			x=x+"\n";
			   this.parseCommandWithReturn("pjc", "wikiPutSendBuffer",w);
		   }
		}
		if(this.packetFilterLan==null) return;
		this.resultVector=this.packetFilterLan.getResults();
		if(this.resultVector!=null) {
		   for(int i=0;i<resultVector.size();i++){
			   String w=resultVector.elementAt(i);
			   x=x+w;
//			x=x+"\n";
			   this.parseCommandWithReturn("pjc", "wikiPutSendBuffer",w);
		   }
		}
		if(this.packetFilterWan==null) return;
		this.resultVector=this.packetFilterWan.getResults();
		if(this.resultVector!=null) {
		   for(int i=0;i<resultVector.size();i++){
			   String w=resultVector.elementAt(i);
			   x=x+w;
//			x=x+"\n";
			   this.parseCommandWithReturn("pjc", "wikiPutSendBuffer",w);
		   }
		}
		
		this.result=x;
		this.resultArea.setText(x);
		this.resultArea.repaint();
		/* */
//		if(this.onlineCommandRefreshButton.isSelected())
		/*
		if(debugger!=null && x!=null) {
			if(!x.equals(""))
		   this.debugger.saveText(x);
		}
		*/
		/*   */
		this.repaint();
	}
	
	public void onlineCommandRefreshButtonActionPerformed(ActionEvent e){
		this.setting.setProperty("onlineCommandRefresh", ""+(this.onlineCommandRefreshButton.isSelected()));
	}
	
	public void writeMessage(String x){
		String w=this.messageArea.getText();
		if(w.length()>10000)
			w=w.substring(5000);
		w=w+x+"\n";
		this.messageArea.setText(w);
		JScrollBar sb=messagePane.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
	}
	public void setProperties(){
		if(this.setting==null)return;
		String w=this.setting.getProperty("managerUrl");
		if(w!=null)
		   this.urlTextField.setText(w);
		w=this.setting.getProperty("onlineCommandRefresh");
		if(w!=null){
			if(this.onlineCommandRefreshButton==null) return;
			if(w.equals("true"))
		       this.onlineCommandRefreshButton.setSelected(true);
			else
				this.onlineCommandRefreshButton.setSelected(false);
		}
		w=this.setting.getProperty("commandInterval");
		if(this.commandIntervalCombo==null) return;
		if(w!=null)
			this.commandIntervalCombo.setSelectedItem(w);
		w=this.setting.getProperty("returnInterval");
		if(this.returnIntervalCombo==null) return;
		if(w!=null)
			this.returnIntervalCombo.setSelectedItem(w);
		
	}
	public Properties getSetting(){
		return this.setting;
	}
	
	public void startWatchingButtonActionPerformed(ActionEvent evt) {
//		System.out.println("startWatchButton.actionPerformed, event="+evt);
		//TODO add your code for startWatchButton.actionPerformed
		this.start();
	}
	
	public void endWatchingButtonActionPerformed(ActionEvent evt) {
//		System.out.println("endWatchingButton.actionPerformed, event="+evt);
		//TODO add your code for endWatchingButton.actionPerformed
		this.stop();
	}
	
	private void commandIntervalComboActionPerformed(ActionEvent evt) {
//		System.out.println("commandIntervalCombo.actionPerformed, event="+evt);
		//TODO add your code for commandIntervalCombo.actionPerformed
		this.setting.setProperty("commandInterval", (String)(this.commandIntervalCombo.getSelectedItem()));
	}
	
	private void returnIntervalComboActionPerformed(ActionEvent evt) {
//		System.out.println("returnIntervalCombo.actionPerformed, event="+evt);
		//TODO add your code for returnIntervalCombo.actionPerformed
		this.setting.setProperty("returnInterval", (String)(this.returnIntervalCombo.getSelectedItem()));
	}
	
	public void clearCommandButtonActionPerformed(ActionEvent evt) {
//		System.out.println("clearCommandButton.actionPerformed, event="+evt);
		//TODO add your code for clearCommandButton.actionPerformed
		if(this.packetMonitorFilter!=null){
			this.packetMonitorFilter.clear();
		}
		if(this.packetFilterLan!=null){
			this.packetFilterLan.clear();
		}
		if(this.packetFilterWan!=null){
			this.packetFilterWan.clear();
		}		
		for(int i=0;i<maxCommands;i++){
			this.commandTable.setValueAt("", i, 0);
			this.commandTable.setValueAt("", i, 1);
		}
		this.p2CommandTable=0;
	}
	
	private void disConnectButtonActionPerformed(ActionEvent evt) {
//		System.out.println("disConnectButton.actionPerformed, event="+evt);
		//TODO add your code for disConnectButton.actionPerformed
		this.connectButton.setSelected(false);
	}
//	@Override
	public void error(String x) {
		// TODO Auto-generated method stub
		if(x.startsWith("format-error")){
			this.writeMessage("pukiwiki-page:"+x+"\n");
			this.stop();
			return;
		}
	}
	Hashtable<String,InterpreterInterface> interpreters;
	/*
	public String parseCommand(String x);
	public String parseCommandWithReturn(String x, String y);
	public InterpreterInterface lookUp(String x);	
	*/
	public void setCommandReceiver(String name,  InterpreterInterface x) {
		if(interpreters==null) {
			interpreters=new Hashtable();
		}
		interpreters.put(name, x);
	}
	private StringBuffer parseCommand(String name, String x) {
		InterpreterInterface ix=interpreters.get(name);
		if(ix!=null) {
			return ix.parseCommand(x);
		}
		return null;
	}
	private StringBuffer parseCommandWithReturn(String name, String x, String y) {
		InterpreterInterface ix=interpreters.get(name);
		if(ix!=null) {
			return ix.parseCommandWithReturn(x,y);
		}
		return null;
	}	
}
