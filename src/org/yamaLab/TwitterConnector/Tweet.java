
package org.yamaLab.TwitterConnector;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Tweet
{
	static final String TAG = "Tweet";
	
	private String hashTag = null;
	private String uploadTag=null;
	private String mainTEXT = null;
	private String mDownloadHashTag;
	private TwitterController mTwitterController;
	private TwitterApplication service;
	private Properties context;
	
	public Tweet(TwitterController tc) {
		System.out.println(TAG+"Twitter");
//        setContentView(R.layout.tweet);
		mTwitterController=tc;
		service=tc.getService();
        
        //?n?b?V???^?O?????I
        uploadTag = "#up787" ;
                    	    	
		hashTag="#787-2015";
		this.service=tc.getService();
		this.context=tc.getContext();
		service.parseCommand("adk twitter set uploadHashTag", uploadTag);
		service.parseCommand("adk twitter set downloadHashTag",hashTag);
    	
	}
	
	public void setDownloadHashTag(String x){
		this.hashTag=x;
	}
	public void setUploadHashTag(String x){
		this.uploadTag=x;
	}
	
	public void tweet() throws TwitterException {
		// ?L?^?????????????t?@?C?????????????B
		System.out.println(TAG+"tweet");

		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

		confbuilder.setOAuthAccessToken(mTwitterController.ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(mTwitterController.ACCESS_TOKEN_SECRET)
		.setOAuthConsumerKey(mTwitterController.CONSUMER_KEY) 
		.setOAuthConsumerSecret(mTwitterController.CONSUMER_SECRET); 

		Twitter twitter=null;
		try{
		 twitter= new TwitterFactory(confbuilder.build()).getInstance(); 
		}
		catch(Exception e){
//			Log.d(TAG,"tweet "+e.toString());
			System.out.println(TAG+"tweet "+e.toString());
			e.printStackTrace();
			
		}

		if(twitter!=null)
		twitter.updateStatus(mainTEXT);
		
//		Toast.makeText(this, "??????????????", Toast.LENGTH_SHORT).show();
	}

	String xwork;
	String writeHash="";
    public void tweet(String x){
    	xwork=x;
		new tweetTask().execute(x);
    }
	private class tweetTask implements Runnable {
        Thread me;
        String TAG="tweetTask";
		public void execute(String x){
			mainTEXT=x;
			if(me!=null){
				me=new Thread(this,"tweetTask");
				me.start();
			}
		}
	    public void run() {
//	    	Log.d(TAG, "doInBackground - " + params[0]);
	        System.out.println(TAG+"doInBackground - "+mainTEXT);
	    	mTwitterController.setAccessingWeb(true);
	    	try{
		   	  tweet();
	    	}
	    	catch(Exception e){
	    		System.out.println(TAG+"tweetTask error:"+e.toString());
				e.printStackTrace();
	    	}
	    	mTwitterController.setAccessingWeb(false);	    	
	    	me=null;
		 }

	}
	public void getHashTweet(){
		new getHashTweetTask().execute(hashTag);
	}	
	public void getHashTweet(String x){
		hashTag=x;
		new getHashTweetTask().execute(x);
	}
	private class getHashTweetTask implements Runnable {
	     String answer;
	     String answerTime;
	     String answerWriter;
	     String TAG="getHashTweetTask";
	     String htag;
	     Thread me;
		 public void execute(String x){
				htag=x;
				if(me!=null){
					me=new Thread("tweetTask");
					me.start();
				}
		 }	     
	     public void run() {
	    	System.out.println(TAG+ "doInBackground - getHashTweetTask " + htag);
	    	mTwitterController.setAccessingWeb(true);
	    	
			// ?????t?@?C??????oauth_token??oauth_token_secret???????B
//			SharedPreferences pref = context.getSharedPreferences("Twitter_setting",context.MODE_PRIVATE);
			String oauthToken  = context.getProperty("oauth_token");
			String oauthTokenSecret = context.getProperty("oauth_token_secret");

			ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

			confbuilder.setOAuthAccessToken(oauthToken) 
			.setOAuthAccessTokenSecret(oauthTokenSecret) 
			.setOAuthConsumerKey(mTwitterController.CONSUMER_KEY) 
			.setOAuthConsumerSecret(mTwitterController.CONSUMER_SECRET); 
			Twitter twitter=null;
			try{
			 twitter= new TwitterFactory(confbuilder.build()).getInstance(); 
			}
			catch(Exception e){
				System.out.println(TAG+"tweet "+e.toString());
				e.printStackTrace();
				
			}
	    	try{
	    		Query query = new Query(htag);
//	    		query.since("2009-10-25");
//	    		query.until("2010-10-25");
	    		query.setResultType(query.RECENT);
	    		query.setCount(1);
//	    		query.setRpp(50);
	    		QueryResult result = twitter.search(query);
	    		List<twitter4j.Status> twitterSearches = result.getTweets();
	    		for (twitter4j.Status tweet : twitterSearches) {
	    		    answer=tweet.getText();
	    		    answerTime=tweet.getCreatedAt().toString();
	    		    answerWriter=tweet.getUser().getName();
	    		    service.parseCommand("activity twitter set resultMessage",
	    		    		answerTime+":"+answerWriter+":"+answer);
	    		    int pos=answer.indexOf(hashTag);
	    		    if(pos>=0){
	    		       String head=answer.substring(0,pos);
	    		       String tail=answer.substring(pos+hashTag.length());
	    		       String toArduino=head+tail;
//	    		    mResultField.append("---"+toArduino);
//	    		       service.parseCommand("adk set message",toArduino);
//	    			   service.sendCommandToService("adk command: set message "+toArduino,"");
	    		       mTwitterController.returnTweet("getHashTweet",toArduino);
	    		       
	    		    }
	    		}
	    	}
	    	catch(Exception e){
	    		System.out.println(TAG+"tweetTask error:"+e.toString());
				e.printStackTrace();
	    	}
	    	mTwitterController.setAccessingWeb(false);	    	
		   	me=null;	
		 }

	}	

}

