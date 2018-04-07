package org.yamaLab.TwitterConnector;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
//import org.yamalab.android.twitter2neomatrixex1.twitterconnector.Tweet;
import java.util.StringTokenizer;

import org.yamaLab.pukiwikiCommunicator.language.InterpreterInterface;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterController  implements InterpreterInterface
{
	static final String TAG = "TwitterController ";
		
	public String CONSUMER_KEY = ""; // dev.twitter.com �ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ
	public String CONSUMER_SECRET = ""; // dev.twitter.com �ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ
	public String CALLBACK_URL = "http://www.yama-lab.org" ; // "myapp://oauth";
	public String ACCESS_TOKEN="";
	public String ACCESS_TOKEN_SECRET="";
	private String nechatterStatus;
	private TwitterApplication service;
//	private Hashtable<String, String> context;
	private Properties context;
	public RequestToken requestToken = null;
	public Twitter twitter = null;
	private Tweet mTweet = null;
	private TwitterLoginController mTwitterLoginController=null;
//	public OAuthAuthorization twitterOauth;
	private boolean accessingWeb=false;
    /** Called when the activity is first created. */
    
    public TwitterController(TwitterApplication svs) {
//    	context=ct;
        service=svs;
		System.out.println(TAG+"TwitterController");
//        mTwitterController=activity.mTwitterController;
        //�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ
//	    SharedPreferences pref = context.getSharedPreferences("Twitter_setting", context.MODE_PRIVATE);
	    //�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽK�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽv�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ�ｿｽ�ｽｿ�ｽｽ

		mTwitterLoginController = new TwitterLoginController(this);
    }
    public void setSetting(Properties x) {
    	this.context=x;
		nechatterStatus  = (String)context.get("status");
        mTweet = new Tweet(this);
        CONSUMER_KEY =context.getProperty("oauth.consumerKey");
        CONSUMER_SECRET =context.getProperty("oauth.consumerSecret");
        ACCESS_TOKEN =context.getProperty("oauth.accessToken");
        ACCESS_TOKEN_SECRET =context.getProperty("oauth.accessTokenSecret");
        CALLBACK_URL =context.getProperty("oauth.callbackUrl");
        if(CALLBACK_URL==null){
        	CALLBACK_URL="http://www.yama-lab.org/";
        	context.put("oauth.callbackUrl", CALLBACK_URL);
        }
    }
	
    final private boolean isConnected(String nechatterStatus){
		if(nechatterStatus != null && nechatterStatus.equals("available")){
			return true;
		}else{
			return false;
		}
	}
    
    public void disconnectTwitter(){		
    	context.put("status", null);
        
        //finish();
	}
	public boolean parseCommand(String x, String v){
		String subcmd=Util.skipSpace(x);
		String [] rest=new String[1];
		String [] match=new String[1];
	   	if(this.service==null) return false;
	   	if(this.mTweet==null) return false;
//		System.out.println(TAG+"parseCommand("+x+","+v+")");
	   	service.parseCommand("guiMessage",x+" "+v);
		int [] intv = new int[1];
		String tweetMessage="";
		if(v==null) return false;
		if(Util.parseKeyWord(subcmd,"tweet",rest)){
		   	String subsub=Util.skipSpace(rest[0]);
//		    SharedPreferences pref = context.getSharedPreferences("Twitter_setting", context.MODE_PRIVATE);
			Status status=null;
			Calendar calendar=Calendar.getInstance();
			int cy=calendar.get(Calendar.YEAR);
			int cmon=calendar.get(Calendar.MONTH)+1;
			int cday=calendar.get(Calendar.DATE);
			int ch=calendar.get(Calendar.HOUR_OF_DAY);
			int cmin=calendar.get(Calendar.MINUTE);
			int csec=calendar.get(Calendar.SECOND);
			int dp=v.indexOf("<day>");
			if(dp>0){
                v=v.replace("<day>", ""+cday);				
			}
			int dh=v.indexOf("<hour>");
			if(dh>0){
                v=v.replace("<hour>", ""+ch);				
			}
			int dm=v.indexOf("<min>");
			if(dm>0){
                v=v.replace("<min>", ""+cmin);				
			}
			
			if(twitter==null){
//				putMessage("error... twitter is not connected.");
				service.parseCommand("guiMessage", "twitter error... twitter is not connected.");
				return true;
			}
			try{
			   status=twitter.updateStatus(v);
			}
			catch(Exception e){
//				putMessage("error when tweet, "+e.getMessage());
				String wstatus="null";
				if(status!=null) wstatus=status.toString();
				service.parseCommand("guiMessage", "twitter error when tweet, "+e.getMessage()+" status="+wstatus);
				return true;
			}
		   	return true;
		}
		else
		if(Util.parseKeyWord(subcmd,"getNewTweet",rest)){
			nechatterStatus  = (String)context.get("status");
			if(accessingWeb) return true;
			accessingWeb=true;
		   	if(isConnected(nechatterStatus)){
//		   		if(tweetMessage==null) return false;
		   	    mTweet.getHashTweet();
		   	}
		   	else{
		   		/* */
//					new connectTwitterTask().execute("");
		   		/* */
		   	}
		   	return true;
		}
		else
		if(Util.parseKeyWord(subcmd,"set ",rest)){
		   	String subsub=Util.skipSpace(rest[0]);
		   	if(subsub.equals("uploadHashTag")){
		   		mTweet.setUploadHashTag(v);
		   		service.parseCommand("guiMessage", "twitter set uploadHashTag:"+ v);
		   		return true;
		   	}
		   	else
		   	if(subsub.equals("downloadHashTag")){
		   		service.parseCommand("guiMessage","twitter set downloadHashTag"+v);
		   		mTweet.setDownloadHashTag(v);
		   		return true;
		   	}
		   	else
		   	if(subsub.equals("OAuth")){
		   		StringTokenizer st=new StringTokenizer(v);
		   		String oAuthToken=st.nextToken();
		   		String oAuthVerifier=st.nextToken();
		   		mTwitterLoginController.startOAuthTask(oAuthToken,oAuthVerifier);

		   		return true;
		   	}
		   	else
		   	if(subsub.equals("accessingweb")){
		   		if(v.equals("true")){
		   			
		   		}
		   		else
		   		if(v.equals("false")){
		   			this.setAccessingWeb(false);		   			
		   		}

		   		return true;
		   	}
		   	
		   	return false;
		}
		else
		if(subcmd.equals("login")){
			loginTwitter();
		}
		return false;
	}
	private void loginTwitter(){
        try{
        // 郢ｧ�ｽ｢郢ｧ�ｽｯ郢ｧ�ｽｻ郢ｧ�ｽｹ郢晏現�ｿｽ�ｽｼ郢ｧ�ｽｯ郢晢ｽｳ邵ｺ�ｽｮ髫ｪ�ｽｭ陞ｳ�ｿｽ
        AccessToken token = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
         
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setOAuthAccessToken(token);
        User user = twitter.verifyCredentials();
        nechatterStatus="available";
        // 髯ｦ�ｽｨ驕会ｽｺ邵ｺ蜉ｱ窶ｻ邵ｺ�ｽｿ郢ｧ荵晢ｿｽ�ｿｽ
        List<Status> list_status = twitter.getHomeTimeline();
        System.out.println("髢ｾ�ｽｪ陋ｻ�ｿｽ邵ｺ�ｽｮ陷ｷ讎顔√�ｿｽ�ｽｼ�ｿｽ" + user.getScreenName());
        service.parseCommand("guiMessage","twitter login/髢ｾ�ｽｪ陋ｻ�ｿｽ邵ｺ�ｽｮ陷ｷ讎顔√�ｿｽ�ｽｼ�ｿｽ" + user.getScreenName());
        System.out.println("隶弱ｊ�ｽｦ竏夲ｿｽ�ｿｽ邵ｲ�ｿｽ邵ｲ�ｿｽ�ｿｽ�ｽｼ�ｿｽ" + user.getDescription());
        service.parseCommand("guiMessage","twitter login/隶弱ｊ�ｽｦ竏夲ｿｽ�ｿｽ邵ｲ�ｿｽ邵ｲ�ｿｽ�ｿｽ�ｽｼ�ｿｽ" + user.getDescription());
//        for (Status status : list_status) {
//            System.out.println("郢晢ｿｽ郢ｧ�ｽ､郢晢ｽｼ郢晁肩�ｽｼ�ｿｽ" + status.getText());
//        	service.parseCommand("guiMessage","郢晢ｿｽ郢ｧ�ｽ､郢晢ｽｼ郢晁肩�ｽｼ�ｿｽ" + status.getText());        
//          }
        }
        catch(Exception e){
        	System.out.println("twitter error:"+e.toString());
            service.parseCommand("guiMessage","twitter error�ｿｽ�ｽｼ�ｿｽ" + e.toString());
        	nechatterStatus=null;
        }
        context.setProperty("status", nechatterStatus);
		
	}
	public void setAccessingWeb(boolean x){
		accessingWeb=x;
	}
	public TwitterApplication getService(){
		return service;
	}
	public Properties getContext(){
		return context;
	}

	public StringBuffer getOutputText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTracing() {
		// TODO Auto-generated method stub
		return false;
	}

	public StringBuffer parseCommand(String x) {
		// TODO Auto-generated method stub
		StringBuffer errorX=new StringBuffer("ERROR");
		String subcmd=Util.skipSpace(x);
		String [] rest=new String[1];
		String [] match=new String[1];
		String[] param1=new String[1];
		String[] param2=new String[2];		
	   	if(this.service==null) return errorX;
	   	if(this.mTweet==null) return errorX;
//		System.out.println(TAG+"parseCommand("+x+","+v+")");
//	   	service.parseCommand("guiMessage",x);
		int [] intv = new int[1];
		String tweetMessage="";
		if(Util.parseKeyWord(subcmd,"tweet ",rest)){
			String l=subcmd.substring("tweet ".length());
			l=Util.skipSpace(l);
			if(!Util.parseStrConst(l, param1, rest)) return errorX;
			l=Util.skipSpace(rest[0]);
			if(Util.parseKeyWord(l, "when ", rest)) {
				l=Util.skipSpace(rest[0]);
				if(!Util.parseStrConst(l, param2, rest)) return errorX;
				tweetWhen(param1[0],param2[0]);
			}
			else{
				this.parseCommand("tweet", param1[0]);				
			}
			return new StringBuffer("OK");

		}
		else
		if(Util.parseKeyWord(subcmd,"getNewTweet ",rest)){
			nechatterStatus  = (String)context.get("status");
			if(accessingWeb) return errorX;
			accessingWeb=true;
		   	if(isConnected(nechatterStatus)){
//		   		if(tweetMessage==null) return false;
		   	    mTweet.getHashTweet();
		   	}
		   	else{
		   		/* */
//					new connectTwitterTask().execute("");
		   		return errorX;
		   		/* */
		   	}
		   	String rtn=null;
		   	for(int count=0;count<300; count++){
		   		rtn=this.getReturnTweet("getHashTweet");
		   		if(rtn!=null){
		   			return new StringBuffer(rtn);
		   		}
		   		try{
     		   		Thread.sleep(100);
		   		}
		   		catch(InterruptedException e){
		   			
		   		}
		   	}
		   	return errorX;
		}
		else
		if(Util.parseKeyWord(subcmd,"set ",rest)){
		   	String subsub=Util.skipSpace(rest[0]);
		   	if(Util.parseKeyWord(subsub,"uploadHashTag ",rest)){
		   		String l=Util.skipSpace(rest[0]);
				if(!Util.parseStrConst(l, param1, rest)) return errorX;
				String v=rest[0];
		   		mTweet.setUploadHashTag(v);
		   		service.parseCommand("guiMessage", "twitter set uploadHashTag:"+ v);
		   		return new StringBuffer("OK");
		   	}
		   	else
		   	if(Util.parseKeyWord(subsub,"downloadHashTag ",rest)){
		   		String l=Util.skipSpace(rest[0]);
				if(!Util.parseStrConst(l, param1, rest)) return errorX;
				String v=rest[0];
		   		service.parseCommand("guiMessage","twitter set downloadHashTag"+v);
		   		mTweet.setDownloadHashTag(v);
		   		return new StringBuffer("OK");
		   	}
		   	return errorX;
		}
		else
		if(subcmd.equals("login")){
			loginTwitter();
			return new StringBuffer("OK");
		}
		return errorX;
		
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
		this.parseCommand("tweet", tw);
	}	

	public InterpreterInterface lookUp(String x) {
		// TODO Auto-generated method stub
		return null;
	}
	Hashtable<String,String> returnTable;
	public void returnTweet(String key, String val){
		if(returnTable==null){
			returnTable=new Hashtable();
		}
		returnTable.put(key, val);
	}
	public String getReturnTweet(String key){
		if(returnTable==null) return null;
		String x=returnTable.get(key);
		returnTable.remove(key);
		return x;
	}

	public StringBuffer parseCommandWithReturn(String x, String y) {
		// TODO Auto-generated method stub
		return null;
	}

}
