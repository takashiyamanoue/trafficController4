package org.yamaLab.pukiwikiCommunicator;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.yamaLab.TwitterConnector.TwitterApplication;
import org.yamaLab.TwitterConnector.TwitterController;
import org.yamaLab.TwitterConnector.Util;
import org.yamaLab.pukiwikiCommunicator.ClassWithJTable;
import org.yamaLab.pukiwikiCommunicator.connector.PukiwikiJavaApplication;
import org.yamaLab.pukiwikiCommunicator.connector.SaveButtonDebugFrame;

//import android.util.Log;
//import android.widget.EditText;

import org.yamaLab.pukiwikiCommunicator.language.CQueue;
import org.yamaLab.pukiwikiCommunicator.language.CommandReceiver;
import org.yamaLab.pukiwikiCommunicator.language.InterpreterInterface;
import org.yamaLab.trafficController.TrafficController;

//import android.util.Log;

/**
*
*/
public class PukiwikiCommunicator
implements PukiwikiJavaApplication, TwitterApplication, InterpreterInterface, Runnable
{
	private Thread me;
	public Properties setting;
//	private JTable commandTable;
	private int maxCommands;
	private CommandReceiver gui; //TrafficController
	TwitterController twitterController;
	TrafficController trafficController;
//	private ClassWithJTable commandTableClass;
	String currentPage;
	public long readCommandInterval=0; // default 10 min.
	public long sendResultInterval=0; // default 10 min.
	String pageName="";
    CQueue inqueue;
    String TAG;
//    Pi4j pi4j;
    
    private Vector<Integer> commandTableIndex;
    private Vector<String> commandTable;

	public PukiwikiCommunicator(CommandReceiver g, ClassWithJTable t){
		this.gui=g;

		this.commandTableIndex=new Vector();
		this.commandTable=new Vector();
		if(twitterController==null){
		   twitterController=new TwitterController(this);
		   this.putApplicationTable("twitter", twitterController);
		}
		//		commandTableClass=t;
		TAG=new String("MainController");
  		this.putApplicationTable("service",this);
   		/*
  		if(pi4j==null){
  		   pi4j = new Pi4j(this);   // uncomment for raspberry pi
//  		pi4j=null;  // for debug
  		   this.putApplicationTable("pi4j", pi4j);
  		}
  		*/
//  		this.putApplicationTable("twitter", twitterController);
//  		this.putApplicationTable("tcon", trafficController);
//		this.start();
	}
	public SaveButtonDebugFrame getSaveButtonDebugFrame() {
		return this.debugger;
	}
	public void setSetting(Properties s) {
		this.setting=s;
		if(this.debugger==null) {
		    this.debugger=new SaveButtonDebugFrame(this);
		     this.debugger.setVisible(false);
		}
		this.debugger.setSetting(s);	
		this.putApplicationTable("connector",debugger);

		if(this.twitterController==null) {
			this.twitterController=new TwitterController(this);
			this.putApplicationTable("twitter", twitterController);
		}
		this.twitterController.setSetting(s);
 
		String maxCommands=(String)setting.getProperty("maxCommandsStr");
		if(maxCommands!=null) {
		    this.maxCommands=Integer.parseInt((String)setting.get("maxCommandsStr"));
		}
		else {
			this.maxCommands=5000;
		}
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
		   String px="";
		   while(reports.size()>reportQueueLength){
			   reports.remove(0);
		   }
		   for(int i=0;i<reports.size();i++){
			   px=px+reports.elementAt(i);
		   }
		   return px;
	}

//	@Override
	int commandLineNo=0;
	private void setCommandLineNo(int x){
		commandLineNo=0;
	}
	private void clearCommands(){
		for(int i=0;i<maxCommands;i++){
			gui.command("wikiCommandTable setValueAt "+i+" 0", "");
			gui.command("wikiCommandTable setValueAt "+i+" 1", "");
		}
	}
	private void setSecondaryURLList(String l){
		gui.command("setSecondaryURLList", l);
	}
	Vector <String> dataLines;
	synchronized public void setInput(String x) {
		// TODO Auto-generated method stub
//		this.writeMessage("setInput("+x+")");
		this.writeMessage("reading commands from the wiki page");
		currentPage=x;
		this.pushCurrentPage(x);
		StringTokenizer st=new StringTokenizer(x,"\n");
		reading=true;
		gui.command("set readInterval", "60000");
		gui.command("set execInterval", "0");
		gui.command("set sendInterval", "0");
		while(st.hasMoreElements()){
			String l=st.nextToken();
			if(commandLineNo>=maxCommands){
				this.writeMessage("too many commands.");
				return;
			}
			if(l.startsWith("include ")){
				String urlCandidates=l.substring("include ".length());
				urlCandidates=Util.skipSpace(urlCandidates);
                readThePageFromOneOf(urlCandidates);
			}
			else
			if(l.startsWith("objectPage ")){
				String urlCandidates=l.substring("objectPage ".length());
				urlCandidates=Util.skipSpace(urlCandidates);
				StringTokenizer stx=new StringTokenizer(urlCandidates);
				String firstObjectPage=stx.nextToken();
				l=urlCandidates.substring(firstObjectPage.length());
				l=Util.skipSpace(l);
				if(l.startsWith("or ")){
				   l=l.substring("or ".length());
				}
				if(l.startsWith("OR ")){
				   l=l.substring("OR ".length());
				}
				l=Util.skipSpace(l);
	            setSecondaryURLList(l);
			}
			else
			if(l.startsWith("device ")){
				String deviceCandidates=l.substring("device ".length());
				deviceCandidates=Util.skipSpace(deviceCandidates);
				String myDeviceName=this.setting.getProperty("deviceID");
				if(myDeviceName==null){
				    StringTokenizer stxx=new StringTokenizer(deviceCandidates);
				    String firstDeviceID=stxx.nextToken();
				    if(firstDeviceID!=null){
				    	gui.command("setDeviceID", firstDeviceID);
				    }
				}
		        int pos=deviceCandidates.indexOf(myDeviceName);
		        if(pos==0){

		        }
		        else
		        if(pos>0){

		        }
			}
			else				                    /* 遶雁ｱ品ki邵ｺ�ｽｸ邵ｺ�ｽｮ陋滂ｽ､邵ｺ荵昴＆郢晄ｧｭﾎｦ郢晏ｳｨ�ｿｽ�ｽｮ隴厄ｽｸ邵ｺ蟠趣ｽｾ�ｽｼ邵ｺ�ｽｿ�ｿｽ�ｽｼ�ｿｽ*/
			if(l.startsWith("command:")){
				gui.command("wikiCommandTable setValueAt "+commandLineNo+" 0", ""+commandLineNo);
				gui.command("wikiCommandTable setValueAt "+commandLineNo+" 1", l);
				this.writeMessage("setting "+l);
				this.commandTableIndex.add(new Integer(commandLineNo));
				this.commandTable.add(l);
				commandLineNo++;
			}
			else
			if(l.startsWith("program:")){
				gui.command("wikiCommandTable setValueAt "+commandLineNo+" 0", ""+commandLineNo);
				gui.command("wikiCommandTable setValueAt "+commandLineNo+" 1", l);
				this.writeMessage("setting "+l);
				this.commandTableIndex.add(new Integer(commandLineNo));
				this.commandTable.add(l);
				commandLineNo++;
			}
			else
			if(l.startsWith("result:")||l.startsWith("control:")) {
				dataLines=new Vector();
				dataLines.add(l);
				readResult(st,dataLines);
			}
			else
			if(l.startsWith("#")){

			}
		}
		currentPage=popCurrentPage();
		long execInterval=getExecRequestInterval();
		if(pageStackIsEmpty()&& execInterval==0){
			execCommands();
			lastExec=System.currentTimeMillis();
		}
	}

	void readResult(StringTokenizer st, Vector <String> dl){
		while(st.hasMoreElements()){
			String l=st.nextToken();
			String x=Util.skipSpace(l);
			if(x.startsWith("currentDevice=")){
				x=x.substring("currentDevice=".length());
				String sc[]=new String[1];
				String rest[]=new String[1];
				boolean rtn=Util.parseStrConst(x, sc, rest);
				String deviceName=sc[0];
				x=Util.skipSpace(rest[0]);
				rtn=Util.parseKeyWord(x, ",", rest);
				x=Util.skipSpace(rest[0]);
				rtn=Util.parseKeyWord(x,"Date=", rest);
				String date=Util.skipSpace(rest[0]);
				if(thisDeviceCanActivate(deviceName,date)){
					activateThisDevice();
				}
			}
			dl.addElement(x);
		}
//		return rtn;
	}
	boolean parseInputCommand(String line){
		if(line==null){
			Log.d(TAG,"parseInputCommand(line==null)");
//			return false;
			return true;
		}
		String [] rest=new String[1];
		String cmd=Util.skipSpace(line);
		Log.d(TAG,"parseInputCommand-"+cmd);
		if(Util.parseKeyWord(cmd,"get ",rest)){
			return this.parseGetCommand(rest[0]);
		}
		else
		if(Util.parseKeyWord(cmd,"set ",rest)){
			return this.parseSetCommand(rest[0]);
		}
		else
		if(Util.parseKeyWord(cmd,"clear ",rest)){
			StringBuffer rtn=this.parseCommand(cmd);
			if((rtn.toString()).equals("ERROR")) return false;
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"program ",rest)){
			String[] names=new String[1];
			cmd=rest[0];
			boolean rtn=Util.parseName(cmd, names, rest);
			InterpreterInterface bx=this.lookUp(names[0]);
			/*
			if(bx!=null){
				
			}
			else{
			*/
				inqueue=new CQueue();
				Basic basic=new Basic("",inqueue,this);				
				this.putApplicationTable(names[0],basic);				
				/*
			}
			*/
			this.inputText="";
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"end ",rest)){
			String[] names=new String[1];
			cmd=rest[0];
			boolean rtn=Util.parseName(cmd, names, rest);
			String arg=Util.skipSpace(rest[0]);
			InterpreterInterface basic=this.lookUp(names[0]);
			basic.parseCommand("setInput "+inputText);
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"run ",rest)){
			String[] names=new String[1];
			cmd=rest[0];
			boolean rtn=Util.parseName(cmd, names, rest);
			String arg=Util.skipSpace(rest[0]);
			InterpreterInterface basic=this.lookUp(names[0]);
//			basic.parseCommand("setInput "+inputText);
			basic.parseCommand("run "+arg);
			return true;
		}
		else{
			if(applicationTable==null) {
				return false;
			}
			for(Enumeration<String> keys=applicationTable.keys();keys.hasMoreElements();){
				String key=keys.nextElement();
				if(Util.parseKeyWord(cmd,key+" ",rest)){
					cmd=rest[0];
					InterpreterInterface obj=this.lookUp(key);
					StringBuffer rtn=obj.parseCommand(cmd);
					if(rtn==null) return false;
					if((rtn.toString()).equals("ERROR")) return false;
					if(key.equals("tcon")) {
						this.putSendBuffer(rtn);
					}
					return true;
				}
			}			
		}
		return false;
	}
    char[] devKind=new char[]{'a','d'};
	boolean parseSetCommand(String x){
		String [] rest=new String[1];
		int[] intv1=new int[1];
		int[] intv2=new int[1];
		double[] dv2=new double[1];
		char[] chrtn=new char[1];
		String cmd=Util.skipSpace(x);
		Log.d(TAG,"parseSetCommand-"+cmd);
		if(Util.parseKeyWord(cmd,"out-",rest)){
			String v1=rest[0];
			if(!Util.parseChar(v1, devKind, chrtn, rest)) return false;
			char dc=chrtn[0]; // a (analog) or d (digital)
			String s2=rest[0];
			if(!Util.parseKeyWord(s2, "-", rest)) return false;
			s2=rest[0];
			if(!Util.parseInt(s2, intv1, rest)) return false;
			int port=intv1[0];
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) {
				if(!Util.parseDouble(v1, dv2, rest))
				    return false;
				else
					intv2[0]=(int)(dv2[0]);
			}
//			this.outputDevice((byte)dc,(byte)port,(byte)(intv2[0]));
//			this.sendCommandToActivity("activity set device "+dc+" "+port+" "+intv2[0], "");
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"xout-",rest)){
			String v1=rest[0];
			if(!Util.parseChar(v1, devKind, chrtn, rest)) return false;
			char dc=chrtn[0]; // a (analog) or d (digital)
			String s2=rest[0];
			if(!Util.parseKeyWord(s2, "-", rest)) return false;
			s2=rest[0];
			if(!Util.parseInt(s2, intv1, rest)) return false;
			int port=intv1[0];
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
//			this.outputDevice((byte)dc,(byte)port,(byte)(intv2[0]));
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"readInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"readCommandInterval-"+it);
			if(it>=1000*15)
			readCommandInterval=(long)it;
			gui.command("set readInterval", ""+it);
			return true;

		}
		else
		if(Util.parseKeyWord(cmd,"execInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"sendCommandInterval-"+it);
			this.sendResultInterval=(long)it;
			gui.command("set execInterval", ""+it);
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"sendInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"sendCommandInterval-"+it);
			this.sendResultInterval=(long)it;
			gui.command("set sendInterval", ""+it);
			return true;
		}

		else
		if(Util.parseKeyWord(cmd,"reportLength",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"reportLength-"+it);
			reportQueueLength=it;
			return true;
		}
		else
		if(Util.parseKeyWord(cmd, "pageName",rest)){
			String v1=Util.skipSpace(rest[0]);
			String[] name=new String[1];
			final Calendar calendar = Calendar.getInstance();
			final int year = calendar.get(Calendar.YEAR);
			final int month = calendar.get(Calendar.MONTH);
			final int day = calendar.get(Calendar.DAY_OF_MONTH);
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			currentHour=""+hour;
			currentDayOfWeek=""+calendar.get(Calendar.DAY_OF_WEEK);
			currentDay=""+day;
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseStrConst(v1, name, rest)) return false;
			String pNameX=name[0];
			Log.d(TAG,"pageNameX="+pNameX);
			pageName=pNameX.replace("<hour>",this.currentHour);
			pageName=pageName.replace("<day>", this.currentDay);
//			pName=pName.replace("<day_of_week>", this.currentDayOfWeek);
			Log.d(TAG,"pageName="+pageName);
//			this.sendCommandToActivity("connector setPageName", pageName);
//			this.parseCommand("setPageName", pageName);
			gui.command("setPageName", pageName);
			return true;

		}
		return false;
	}

	boolean parseGetCommand(String x){
		String [] rest=new String[1];
		int[] intv1=new int[1];
		int[] intv2=new int[1];
		double[] dv2=new double[1];
		char[] chrtn=new char[1];
		String cmd=Util.skipSpace(x);
		Log.d(TAG,"parseGetCommand-"+cmd);
		if(Util.parseKeyWord(cmd,"in-",rest)){
			String v1=rest[0];
			if(!Util.parseChar(v1, devKind, chrtn, rest)) return false;
			char dc=chrtn[0]; // a (analog) or d (digital)
			String s2=rest[0];
			if(!Util.parseKeyWord(s2, "-", rest)) return false;
			s2=rest[0];
			if(!Util.parseInt(s2, intv1, rest)) return false;
			int port=intv1[0];
			String rtn="ERROR";
			/*
			if(pi4j!=null){
			   rtn = pi4j.parseCommand("get-a-0.");
			}
			*/
			sendLastValue("a-0","v="+rtn);
//			this.outputDevice((byte)dc,(byte)port,(byte)(intv2[0]));
//			this.sendCommandToActivity("activity set device "+dc+" "+port+" "+intv2[0], "");
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"xout-",rest)){
			String v1=rest[0];
			if(!Util.parseChar(v1, devKind, chrtn, rest)) return false;
			char dc=chrtn[0]; // a (analog) or d (digital)
			String s2=rest[0];
			if(!Util.parseKeyWord(s2, "-", rest)) return false;
			s2=rest[0];
			if(!Util.parseInt(s2, intv1, rest)) return false;
			int port=intv1[0];
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
//			this.outputDevice((byte)dc,(byte)port,(byte)(intv2[0]));
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"readInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"readCommandInterval-"+it);
			if(it>=1000*15)
			readCommandInterval=(long)it;
			gui.command("set readInterval", ""+it);
			return true;

		}
		else
		if(Util.parseKeyWord(cmd,"execInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"sendCommandInterval-"+it);
			this.sendResultInterval=(long)it;
			gui.command("set execInterval", ""+it);
			return true;
		}
		else
		if(Util.parseKeyWord(cmd,"sendInterval",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"sendCommandInterval-"+it);
			this.sendResultInterval=(long)it;
			gui.command("set sendInterval", ""+it);
			return true;
		}

		else
		if(Util.parseKeyWord(cmd,"reportLength",rest)){
			String v1=Util.skipSpace(rest[0]);
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseInt(v1, intv2, rest)) return false;
			int it=intv2[0];
			Log.d(TAG,"reportLength-"+it);
			reportQueueLength=it;
			return true;
		}
		else
		if(Util.parseKeyWord(cmd, "pageName",rest)){
			String v1=Util.skipSpace(rest[0]);
			String[] name=new String[1];
			final Calendar calendar = Calendar.getInstance();
			final int year = calendar.get(Calendar.YEAR);
			final int month = calendar.get(Calendar.MONTH);
			final int day = calendar.get(Calendar.DAY_OF_MONTH);
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			currentHour=""+hour;
			currentDayOfWeek=""+calendar.get(Calendar.DAY_OF_WEEK);
			currentDay=""+day;
			if(!Util.parseKeyWord(v1, "=", rest)) return false;
			v1=Util.skipSpace(rest[0]);
			if(!Util.parseStrConst(v1, name, rest)) return false;
			String pNameX=name[0];
			Log.d(TAG,"pageNameX="+pNameX);
			pageName=pNameX.replace("<hour>",this.currentHour);
			pageName=pageName.replace("<day>", this.currentDay);
//			pName=pName.replace("<day_of_week>", this.currentDayOfWeek);
			Log.d(TAG,"pageName="+pageName);
//			this.sendCommandToActivity("connector setPageName", pageName);
//			this.parseCommand("setPageName", pageName);
			gui.command("setPageName", pageName);
			return true;

		}
		return false;
	}

	Vector <String> currentPageStack;
	int currentPageStackPointer=0;
	private void pushCurrentPage(String x){
		if(currentPageStack==null){
			currentPageStack=new Vector();
			currentPageStackPointer=0;
		}
		currentPageStack.add(x);
		currentPageStackPointer++;
	}
	private String popCurrentPage(){
		if(currentPageStack==null){
			return null;
		}
		if(currentPageStackPointer<=0) return null;
		String rtn=currentPageStack.elementAt(currentPageStackPointer-1);
		currentPageStack.removeElementAt(currentPageStackPointer-1);
		currentPageStackPointer--;
		return rtn;
	}
	private boolean pageStackIsEmpty(){
		if(currentPageStackPointer<=0) return true;
		else return false;
	}
	private void readThePageFromOneOf(String urlList){
		StringTokenizer st=new StringTokenizer(urlList);
		while(st.hasMoreTokens()){
		    String urlX=st.nextToken();
		    String rtn=debugger.readFromPukiwikiPageAndSetData(urlX);
		    if(rtn=="ERROR"){
		    	writeMessage("failed to read "+urlX+". try to read the next");
		    }
		    else{
		    	return;
		    }
		    String orOperator=st.nextToken();
		    if(orOperator.equals("OR")||orOperator.equals("or")||orOperator.equals("Or")){

		    }
		    else{
		    	writeMessage("strange or Operator-"+orOperator+". Failed to read.");
		    	return;
		    }
		}
	}

	private SaveButtonDebugFrame debugger;
//	@Override
	public void setSaveButtonDebugFrame(SaveButtonDebugFrame f) {
		// TODO Auto-generated method stub
		debugger=f;
	}
/*	*/
	static public void main(String[] args){
		new PukiwikiCommunicator(null,null).setVisible(true);
	}
	long lastCommandRequest;
	long lastSendOutput;
	long lastExec;
	boolean reading;
//	@Override
	public void run() {
		lastCommandRequest=0;
		lastSendOutput=0;
		lastExec=0;
		// TODO Auto-generated method stub
		while(me!=null){
			long time=System.currentTimeMillis();
			long readInterval=getReadRequestInterval();
			long execInterval=getExecRequestInterval();
			sendResultInterval=getSendRequestInterval();
//			long returnInterval=getResultReturnInterval();
			if(time>lastCommandRequest+readInterval){
				this.writeMessage("connectionButton");
				String urlx=gui.command("getWikiUrl", "");
				this.commandTableIndex.clear();
				this.commandTable.clear();
				this.connectButtonActionPerformed(urlx);
				lastCommandRequest=System.currentTimeMillis();
			}
			if(execInterval>0 && time>lastExec+execInterval){
				execCommands();
				lastExec=System.currentTimeMillis();
			}
			/* */
			if(sendResultInterval>0 && time>lastSendOutput+sendResultInterval){
				this.writeMessage("sendResults.");
				this.sendResults();
				lastSendOutput=System.currentTimeMillis();
			}
			/* */
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
			}
		}

	}

	public String wikiUrl;
	public void connectButtonActionPerformed(String url) {
		//TODO add your code for connectButton.actionPerformed
		long timeOutTime=20000;
		if(url==null) return;
		if(url.equals("")) return;
		wikiUrl=url;
	    try{
		   Thread thread = new Thread(new Runnable(){
			  public void run(){
				setCommandLineNo(0);
				clearCommands();
				String rtn=debugger.readFromPukiwikiPageAndSetData(wikiUrl);
				if(rtn!="ERROR"){
				    setting.setProperty("managerUrl", wikiUrl);
				}
				else{
					String urlList=setting.getProperty("secondaryURLList");
					StringTokenizer st=new StringTokenizer(urlList);
					while(st.hasMoreTokens()){
					    String urlX=st.nextToken();
					    rtn=debugger.readFromPukiwikiPageAndSetData(urlX);
					    if(rtn=="ERROR"){
					    	writeMessage("failed to read "+urlX+". try to read the next");
					    }
					    else{
					    	setting.setProperty("managerUrl", urlX);
					    	return;
					    }
					    String orOperator=st.nextToken();
					    if(orOperator.equals("OR")||orOperator.equals("or")||orOperator.equals("Or")){

					    }
					    else{
					    	writeMessage("strange or Operator-"+orOperator+". Failed to read.");
					    	return;
					    }
					}

				}
		      } // run
		  } ); // new thread;
		  thread.start();
		   long endTimeMillis = System.currentTimeMillis()+timeOutTime;
		   while(thread.isAlive()){
			   if(System.currentTimeMillis()>endTimeMillis){
				   this.println(" connection time out at PukiWikiCommunicator.connectButtonActionPerformed.");
				   return; // return by the time out
			   }
			   try{
				   Thread.sleep(100);
			   }
			   catch(InterruptedException e){}
		   }
		  
	    }
	    catch(Exception e) {
	    	
	    }
	}
	private long getMilisec(String x){
		long rtn;
		StringTokenizer st=new StringTokenizer(x,"-");
		String numx=st.nextToken();
		String unit=st.nextToken();
		int dotPlace=numx.indexOf(".");
		long p1,p2;
		if(dotPlace<0){
			p1=(new Long(numx)).longValue();
			p2=0;
		}
		else{
			String p1s=numx.substring(0,dotPlace);
			String p2s=numx.substring(dotPlace+1);
			p1=(new Long(p1s)).longValue();
			p2=(new Long((p2s+"000").substring(0,3))).longValue();
		}
		if(unit.equals("sec")){
			rtn=p1*1000+p2;
		}
		else
		if(unit.equals("min")){
			rtn=(p1*1000+p2)*60;
		}
		else
		if(unit.equals("h")){
			rtn=(p1*1000+p2)*60*60;
		}
		else{
			rtn=0;
		}
//		this.messageArea.append("getCommandRequestInterval="+rtn+"\n");
		return rtn;
	}

	private long getExecRequestInterval(){
		long rtn,rx;
//		this.setting.setProperty("commandInterval", (String)(this.commandIntervalCombo.getSelectedItem()));
		String x=(String)(this.setting.getProperty("execInterval"));
//		rtn=getMilisec(x);
//	    return rtn;
		rtn=(new Long(x)).longValue();
		return rtn;

	}

	private long getReadRequestInterval(){
		long rtn,rx;
//		this.setting.setProperty("commandInterval", (String)(this.commandIntervalCombo.getSelectedItem()));
		String x=(String)(this.setting.getProperty("readInterval"));
//		rtn=getMilisec(x);
//	    return rtn;
		rtn=(new Long(x)).longValue();
		return rtn;
	}
	private long getSendRequestInterval(){
		long rtn,rx;
//		this.setting.setProperty("returnInterval", (String)(this.returnIntervalCombo.getSelectedItem()));
		String x=(String)(this.setting.getProperty("sendInterval"));
//		rtn=getMilisec(x);
//	    return rtn;
		rtn=(new Long(x)).longValue();
		return rtn;
	}
/*
	String result="";
	public void writeResult(){
		System.out.println("writeResult");
		String urlx=gui.command("getWikiUrl", "");
		this.connectButtonActionPerformed(urlx);
		String x="";
		for(int i=0;i<reports.size();i++) {
			x=x+reports.elementAt(i);
		}
		this.result=x;
		this.debugger.saveText(x);
		gui.command("wikiMessage", x);
	}
*/
	public Properties getSetting(){
		return this.setting;
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
	public boolean parseCommand(String cmd, String v) {
		// TODO Auto-generated method stub
		if(cmd.startsWith("tweet ")){
			String l=cmd.substring("tweet ".length());
			String[] rest=new String[1];
			String[] param1=new String[1];
			String[] param2=new String[2];
			l=Util.skipSpace(l);
			if(!Util.parseStrConst(l, param1, rest)) return false;
			l=Util.skipSpace(rest[0]);
			if(Util.parseKeyWord(l, "when ", rest)) {
				l=Util.skipSpace(rest[0]);
				if(!Util.parseStrConst(l, param2, rest)) return false;
				tweetWhen(param1[0],param2[0]);
			}
			return true;
		}
		else
		if(cmd.startsWith("wikiDebuggerButton Clicked")){
			if(this.debugger.isVisible()){
				this.debugger.setVisible(false);
			}
			else{
				this.debugger.setVisible(true);;
			}
			return false;
		}
		else
		if(cmd.startsWith("onlineCommandRefresh")){
				this.setting.setProperty("onlineCommandRefresh", v);
				return false;
		}
		else
		if(cmd.startsWith("wikiStartWatching")){
			this.start();
			return false;
		}
		else
		if(cmd.startsWith("wikiConnectButton Clicked")){
			this.connectButtonActionPerformed(v);
			return false;
		}
		else
		if(cmd.startsWith("wikiEndWatching")){
			this.stop();
			return false;
		}
		else
		if(cmd.startsWith("twitterConnect")){
			twitterController.parseCommand("login", "");
			return false;
		}
		else
		if(cmd.startsWith("twitterTweet")){
			twitterController.parseCommand("tweet",v);
			return false;
		}
		else
		if(cmd.equals("traceCheckBox")){
			if(v.equals("true")){
				this.tracing=true;
			}
			else{
				this.tracing=false;
			}
			return false;
		}

		else
		if(cmd.startsWith("guiMessage")){
			gui.command("writeMessage",v);
			return false;
		}
		else
		if(cmd.startsWith("wikiPutSendBuffer")) {
			this.putSendBuffer(new StringBuffer(v));
		}
		else
		if(cmd.startsWith("exit")){
			return false;
		}
		return true;
	}

	public void setVisible(boolean f) {
		// TODO Auto-generated method stub

	}
	public void writeMessage(String x){
	   gui.command("writeMessage", x);
//	   System.out.println(x);
	}
	private void tweetWhen(String tw, String t){
		Calendar calendar=Calendar.getInstance();
		int cy=calendar.get(Calendar.YEAR);
		int cmon=calendar.get(Calendar.MONTH)+1;
		int cday=calendar.get(Calendar.DATE);
		int ch=calendar.get(Calendar.HOUR_OF_DAY);
		int cmin=calendar.get(Calendar.MINUTE);
		int csec=calendar.get(Calendar.SECOND);
		StringTokenizer st=new StringTokenizer(t,",");
		String gy=st.nextToken();
		String gmon=st.nextToken();
		String gday=st.nextToken();
		String gh=st.nextToken();
		String gmin=st.nextToken();
		String gsec=st.nextToken();
		if(!gy.equals("*")){ /* same year ? */
			int igy=(new Integer(gy)).intValue();
			if(igy!=cy) return;
		}
		if(!gmon.equals("*")){ /* same month ? */
			int igmon=(new Integer(gmon)).intValue();
			if(igmon!=cmon) return;
		}
		if(!gday.equals("*")){ /* same day ?*/
			int igday=(new Integer(gday)).intValue();
			if(igday!=cday) return;
		}
		if(!gh.equals("*")){ /* same hour ? */
			int igh=(new Integer(gh)).intValue();
			if(igh!=ch) return;
		}
		if(!gmin.equals("*")){ /* same minutes ? */
			int igmin=(new Integer(gmin)).intValue();
			if(igmin!=cmin) return;
		}
		if(!gsec.equals("*")){ /* same seconds ? */
			int igsec=(new Integer(gsec)).intValue();
			if(igsec!=csec) return;
		}
		twitterController.parseCommand("tweet", tw);
	}
	synchronized private void execCommands(){
//		JTable commandTable=commandTableClass.getJTable("commandTable");
//		if(commandTable==null) return;
//		int tmax=commandTable.getRowCount();
		int tmax=commandLineNo;
		boolean rtn=false;
		if(commandTable.size()==0) return;
		for(int i=0;i<tmax;i++){
//			String line=(String)commandTable.getValueAt(i,1);
			String line=commandTable.elementAt(i);
			if(line.startsWith("#")) continue;
			if(line.startsWith("command:")){
//					sendCommandToActivity("activity append input",line+"\n");
				String command=line.substring("command:".length());
				command=Util.skipSpace(command);
				rtn=parseInputCommand(command);
			}
			else
			if(line.startsWith("program:")){
//					sendCommandToActivity("activity append input",line+"\n");
				String program=line.substring("program:".length());
				rtn=addProgramLine(program);
			}
//			if(reports.size()>1) return;
			if(!rtn) return;
		}
	}
/*
	public String getOutputText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTracing() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	public StringBuffer parseCommand(String x) {
		// TODO Auto-generated method stub
		String [] rest=new String[1];
		String cmd=Util.skipSpace(x);
		Log.d(TAG,"parseInputCommand-"+cmd);
		if(Util.parseKeyWord(cmd,"println ",rest)){
			println(rest[0]);
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(cmd,"putSendBuffer ",rest)){
			putSendBuffer(new StringBuffer(rest[0]+"\n"));
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(cmd,"sendResults.",rest)){
			this.sendResults();
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(cmd,"sendResults ",rest)){
//			putSendBuffer(rest[0]+"\n");
			putSendBuffer(new StringBuffer(rest[0]+"\n"));
			this.sendResults();
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(cmd,"sendSensorNetworkValue ",rest)){
//			putSendBuffer(rest[0]+"\n");
			sendLastValue("sensorNetwork",rest[0]);
//			this.sendResults();
			return new StringBuffer("OK");
		}
		
		else
		if(Util.parseKeyWord(cmd,"set ",rest)){
			boolean rtn=this.parseSetCommand(rest[0]);
			if(rtn) return new StringBuffer("OK");
			return new StringBuffer("ERROR");
		}
		else
		if(Util.parseKeyWord(cmd,"get ",rest)){
			int [] intVal=new int[1];
			String cx=Util.skipSpace(rest[0]);
			if(Util.parseKeyWord(cx,"in-a-",rest)){
				String v2=rest[0];
				if(!Util.parseInt(v2, intVal, rest)) return new StringBuffer("ERROR");
				int dn=intVal[0];
			    if(dn>8) return new StringBuffer("ERROR");
			    if(dn<0) return new StringBuffer("ERROR");
				v2=Util.skipSpace(rest[0]);
//				return ""+this.thread.analogVals[dn];
				return new StringBuffer("0");
			}
			else
			if(Util.parseKeyWord(cx,"in-d",rest)){
//				return Util.b2h(this.thread.digitalVals);
				return new StringBuffer("0");
			}
			else return new StringBuffer("ERROR");
		}
		else
		if(Util.parseKeyWord(cmd,"clear ",rest)){
			String xx=rest[0];
			xx=Util.skipSpace(xx);
			if(Util.parseKeyWord(xx, "sendBuffer", rest)){
				reports.clear();
				return new StringBuffer("OK");
			}
			return new StringBuffer("ERROR");
		}
		else
		if(Util.parseKeyWord(cmd,"getThisPage",rest)){
			return new StringBuffer(currentPage);
		}
		else
/*
		if(Util.parseKeyWord(cmd,"getpage ",rest)){
			String page=debugger.readFromPukiwikiPageAndSetData(rest[0]);
			return page;
//			return currentPage;
		}
		else
*/
		if(Util.parseKeyWord(cmd, "guiMessage ", rest)){
			gui.command("wikiMessage",rest[0]);
			return new StringBuffer("OK");
		}
		else
		if(Util.parseKeyWord(cmd, "getCurrentDate.", rest)){
			return new StringBuffer(getCurrentDate());
		}
	    return new StringBuffer("ERROR");
//		return null;
	}

//	public InterpreterInterface lookUp(String x) {
		// TODO Auto-generated method stub
//		return null;
//	}

	String saveText="";
	int uploadInterval=10;
    int uploadNumber;
    Vector<String> reports=new Vector();
    int reportQueueLength=120;

    public void putSendBuffer(StringBuffer x){
    	StringTokenizer st=new StringTokenizer(x.toString(),"\n");
    	while(st.hasMoreTokens()) {
    		String line=st.nextToken();
	   Log.d(TAG,"putSendBuffer("+line+")");
	   if(!line.endsWith("\n")) {
		   line=line+"\n";
	   }
//	   this.sendCommandToActivity("activity append output", x);
   //	mPirOutputView.append(line);
	   gui.command("writeResult", line);
	   reports.add(line);
//	   this.sendCommandToActivity("connector setMessage", "");
   //	mMessageView.setText("");
	   uploadNumber++;
    	}
   }
   String currentHour;
   String currentDayOfWeek;
   String currentDay;
   private void sendLastValue(String dname, String v){
   	final Calendar calendar = Calendar.getInstance();
	final int year = calendar.get(Calendar.YEAR);
	final int month = calendar.get(Calendar.MONTH);
	final int day = calendar.get(Calendar.DAY_OF_MONTH);
	final int hour = calendar.get(Calendar.HOUR_OF_DAY);
	final int minute = calendar.get(Calendar.MINUTE);
	final int second = calendar.get(Calendar.SECOND);
	currentHour=""+hour;
	currentDayOfWeek=""+calendar.get(Calendar.DAY_OF_WEEK);
	StringBuffer linex=new StringBuffer("device="+dname+", Date="+
			   year + "/" + (month + 1) + "/" + day + "/" + " " +
	           hour + ":" + minute + ":" + second +
	           ",  "+v+
	           ".\n");
//	           ".";
		this.putSendBuffer(linex);

   }
   String inputText="";

   public void sendResults(){
	   Log.d(TAG,"sendResults()");
//   	   this.sendCommandToActivity("activity set output", "");
	   String px="";
	   while(reports.size()>reportQueueLength){
		   reports.remove(0);
	   }
	   for(int i=0;i<reports.size();i++){
		   px=px+reports.elementAt(i);
	   }
   //	this.connectorService.saveText(px);
	   String urlx=gui.command("getCurrentUrl", "");
	   String dname=getDeviceName();
	   String control="currentDevice=\""+dname+"\",Date="+getCurrentDate();
	   px=px+control+"\n";
	   debugger.saveText(urlx,px);
	   this.uploadNumber=0;
   }
   String getDeviceName(){
	   return gui.command("getDeviceID", "");
   }
   Hashtable<String,InterpreterInterface> applicationTable;
//   @Override
   public InterpreterInterface lookUp(String x) {
	// TODO Auto-generated method stub
	   if(applicationTable==null) return null;	   
	   return applicationTable.get(x);
   }
   public void putApplicationTable(String name, InterpreterInterface obj){
	   if(applicationTable==null) {
		   applicationTable=new Hashtable();
	   }
	   applicationTable.put(name, obj);
   }
   StringBuffer outputText;
//@Override
   public StringBuffer getOutputText() {
	// TODO Auto-generated method stub
	   return outputText;
   }
   private boolean tracing=false;
//@Override
   public boolean isTracing() {
	   // TODO Auto-generated method stub
	   return tracing;
//	   return true;
   }
   private void println(String x){
	   Log.d(TAG,"println("+x+")");
//	if(showMessage) return;
		/* */
//	   if(x.length()>300){
//		   x=x.substring(0,200)+"...";
//	   }
//	   sendCommandToActivity("activity append message", x);
//		this.messageTextArea.setCaretPosition((this.messageTextArea.getText()).length());
		/* */
//	System.out.println(x);
//	   this.putSendBuffer(x);
	   gui.command("writeMessage", x);
	   System.out.println(x);
   }
   boolean debugging=false;
   public boolean isDebugging(){
	   return debugging;
   }

	boolean addProgramLine(String line){
		line=removeComment(line);
		this.inputText=inputText+line+"\n";
		return true;
	}
	String removeComment(String x){
		String rx="";
		while(x.length()>0){
		  char c=x.charAt(0);
		  if(c=='\'') return rx;
		  if(c=='\\'){
			  rx=rx+c;
			  x=x.substring(1);
			  if(x.length()<=0) return rx;
			  rx=rx+x.charAt(0);
			  x=x.substring(1);
		  }
		  if(c=='\"'){
			  rx=rx+c;
			  x=x.substring(1);
			  if(x.length()<=0) return rx;
			  c=x.charAt(0);
			  while(c!='\"'){
				  rx=rx+c;
				  x=x.substring(1);
				  if(x.length()<=0) return rx;
				  c=x.charAt(0);
				  if(c=='\\'){
					  rx=rx+c;
					  x=x.substring(1);
					  if(x.length()<=0) return rx;
					  rx=rx+x.charAt(0);
					  x=x.substring(1);
					  c=x.charAt(0);
				  }
			  }
		  }
		  rx=rx+c;
		  x=x.substring(1);
		}
		return rx;
	}
	boolean thisDeviceCanActivate(String dn, String date){
		return false;
	}
	void activateThisDevice(){

	}
	String getCurrentDate(){
		final Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int second = calendar.get(Calendar.SECOND);
		String rtn=year + "/" + (month + 1) + "/" + day + "/" + " " +
		           hour + ":" + minute + ":" + second;
		return rtn;
	}
	public void exit(){
		this.stop();
		System.exit(0);
	}

	public StringBuffer parseCommandWithReturn(String x, String y) {
		// TODO Auto-generated method stub
		boolean rtn=this.parseCommand(x,y);
		if(rtn)
			return new StringBuffer("OK");
		else
			return new StringBuffer("ERROR");
		/*
		if(x.equals("wikiStartWatching")) {
			
		}
		else
		if(x.equals("wikiEndWatching")) {
			
		}
		else
		if(x.equals("traceCheckBox")) {
			
		}
		else
		if(x.equals("wikiConnectButton Clicked")) {
			
		}
		else
		if(x.equals("wikiDebuggerButton Clicked")) {
			
		}
		else
		if(x.equals("onlineCommandRefresh")) {
			
		}
		else
		if(x.equals("twitterConnect")) {
			
		}
		else
		if(x.equals("twitterTweet")) {
			
		}		
		*/
//		return null;
	}
}


