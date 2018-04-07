
package org.yamaLab.TwitterConnector;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;

public class TwitterLoginController {
	static final String TAG = "TwitterLoginController";

    Properties context;
//    WebView webView;
    TwitterController mTwitterController;
    Twitter mTwitter;
   RequestToken requestToken;
	TwitterLoginController(TwitterController tc) {
		System.out.println(TAG+"TwitterLoginController");
        context=tc.getContext();
        mTwitterController=tc;
//		webView = (WebView)findViewById(R.id.twitterlogin);
    }
	/*
	public void loadUrl(String x){
//		mTwitterController.setAccessingWeb(true);
		WebSettings webSettings = webView.getSettings();
		//?????????????[?U?[???????T?C???C???????B?????s??????
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if(url != null && url.startsWith(mTwitterController.CALLBACK_URL )){
					String[] urlParameters = url.split("\\?")[1].split("&");

					String oauthToken = "";
					String oauthVerifier = "";

					if(urlParameters[0].startsWith("oauth_token")){
						oauthToken = urlParameters[0].split("=")[1];
					}else if(urlParameters[1].startsWith("oauth_token")){
						oauthToken = urlParameters[1].split("=")[1];
					}

					if(urlParameters[0].startsWith("oauth_verifier")){
						oauthVerifier = urlParameters[0].split("=")[1];
					}else if(urlParameters[1].startsWith("oauth_verifier")){
						oauthVerifier = urlParameters[1].split("=")[1];
					}

					new setOAuthTask().execute(oauthToken,oauthVerifier);
				}
			}
		});


//		new loadUrlTask().execute(x);
	}
	*/
	/*
	@Override
	protected void onAccesssoryAttached() {
		// TODO Auto-generated method stub
		
	}
	*/

	private class setOAuthTask implements Runnable {
//		 @Override
		Thread me;
		String token;
		String verifier;
		public void execute(String t, String v){
			token=t;
			verifier=v;
			if(me==null){
			   me=new Thread(this,"setOAuthTask");
			   me.start();
			}
		}
	     public void run() {
	    	System.out.println(TAG+ "connectTwitterTask.doInBackground - " );
	    	try{
                setOAuth(token,verifier);
	    	}
	    	catch(Exception e){
	    		System.out.println(TAG+"tweetTask error:"+e.toString());
				e.printStackTrace();
				me=null;
			}
			 mTwitterController.setAccessingWeb(false);
			 me=null;
	    }
	}
			
	/* */
    protected void setOAuth(String oauthToken, String oauthVerifier) {
		System.out.println(TAG+"setOAuth token="+oauthToken+" verifier="+oauthVerifier);

        mTwitter=mTwitterController.twitter;
//		mTwitter=mTwitterController.twitterOauth;
        requestToken=mTwitterController.requestToken;
			AccessToken accessToken = null;
	        if(mTwitter==null) {
	        	System.out.println(TAG+"setOAuth activity.twitter==null");
	        	return;
	        }

			try {
				accessToken = mTwitter.getOAuthAccessToken(
						requestToken, oauthVerifier);
/*
		        SharedPreferences pref=context.getSharedPreferences(
		        		  "Twitter_setting",context.MODE_PRIVATE);

		        SharedPreferences.Editor editor=pref.edit();
		        editor.putString("oauth_token",accessToken.getToken());
		        editor.putString("oauth_token_secret",accessToken.getTokenSecret());
		        editor.putString("status","available");

		        editor.commit();
	*/	        
//		        context.showTabContents(R.id.main_tweet_label);
		        //finish();
			} catch (TwitterException e) {
	    		System.out.println(TAG+"setOAuth error:"+e.toString());
				e.printStackTrace();
			}
    }
    public void startOAuthTask(String oAuthToken, String oAuthVerifier){
		setOAuthTask sot=new setOAuthTask();
		sot.execute(oAuthToken,oAuthVerifier);
    }
}

