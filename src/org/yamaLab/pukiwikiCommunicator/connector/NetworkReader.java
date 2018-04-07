package org.yamaLab.pukiwikiCommunicator.connector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


public class NetworkReader implements Runnable
{
    public NetworkReader()
    {
    }

    public String getBaseDir(String path, String dirSym)
    {
        this.getTheLastToken(path, dirSym);
        return this.baseDir;
    }

    String baseDir;

    public String getFileNameExtension(String path)
    {
        return getTheLastToken(path,".");
    }

    public String getTheLastToken(String x, String breakSymbol)
    {
    	if(x.startsWith("http://")){
    		x=x.substring("http://".length());
    		baseDir="http://";
    	}
    	else
    	if(x.startsWith("///"))
    	{
    		x=x.substring("///".length());
    		baseDir="///";
    	}
    	else
    	if(x.startsWith("//"))
    	{
    		x=x.substring("//".length());
    		baseDir="//";
    	}
    	else
    	if(x.startsWith("/"))
    	{
    		x=x.substring("/".length());
    		baseDir="/";
    	}
    	else
    	if(x.startsWith("\\\\")){
    		x=x.substring("\\\\".length());
    		baseDir="\\\\";
    	}
    	else
    	if(x.startsWith("\\")){
    		x=x.substring("\\".length());
    		baseDir="\\";
    	}
    	else{
    		baseDir="";
    	}
        String theLast="";
//        this.baseDir="";
        StringTokenizer st=new StringTokenizer(x,breakSymbol);
        theLast=st.nextToken();
        while(st.hasMoreTokens()){
            baseDir=baseDir+theLast+breakSymbol;
            theLast=st.nextToken();
        }
        return theLast;
    }

    String encodingCode;

    boolean isLoading;

    public String loadAndWait() // synchronized?
    {
        /*
        start();
        while(this.isLoading){
            try{
              Thread.sleep(20);
            }
            catch(InterruptedException e){}
        }
        */
        this.load();
        return this.outputString;
    }

    String messages;

    public void stop()
    {
        if(me!=null){
            me=null;
        }
    }

    public synchronized void start()
    {
        this.isLoading=true;
        if(me==null){
            me=new Thread(this,"network reader");
            me.start();
        }
    }

    String outputString;

    String urlString;

    Thread me;

    public NetworkReader(String url, String coding)
    {
        this.urlString=url;
        this.encodingCode=coding;
    }

    
    //synchronized
    void load()
    {
            outputString="";
            this.messages="";
            URL url=null;
            try{ url=new URL(urlString); }
            catch(MalformedURLException e)
            {  
                messages=messages+ "MalformedURLException\n";
                this.isLoading=false;
                return;
            }
            String filePath=url.getFile();
            String theTypeName="html";
            if(filePath!=null && !filePath.equals("")){
                String theFile=getTheLastToken(filePath,"/");
                if(theFile!=null && !theFile.equals("")){
                    theTypeName=getTheLastToken(theFile,".");
                }
            }
            
            int buffersize=50000;
	    	BufferedReader dinstream=null;
            String page="";
            try{
               dinstream=new BufferedReader(
                              new InputStreamReader(url.openStream(), encodingCode )
//                              new InputStreamReader(ins)
                              ,buffersize);
            }
            catch(Exception e){
                messages=messages+ ""+e;
            }
            if(dinstream==null){
            	outputString="<html><body><h1>Network Error</h1><br> Could not open the page.</body></html>";
            	return;
            }
            String line=null;
            try{
                line=dinstream.readLine();
            }
            catch(IOException e){
                messages=messages+"error at readLine.\n";
                this.isLoading=false;
                return;
            }
            catch(NullPointerException e){
                messages=messages+ "null pointer exception.\n";
                this.isLoading=false;
            }
            while(line!=null)
            {
//                if(this.me==null) return;
                this.outputString=outputString+line+"\n";
//                pageArea.setText(page);
//                pageArea.repaint();
                try{
                    line=dinstream.readLine();
                }
                catch(IOException e){
                    messages=messages+ "error at readLine.\n";
                    break;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    messages=messages+ "error at readLine, arrayIndexOutOf bounds.\n";
                    messages=messages+ "buffer size "+buffersize+"seems small.\n";
                    break;
                }
                catch(NullPointerException e){
                    messages=messages+ "error at readLine, null pointer exception.";
                    break;
                }
                catch(Exception e){
                    messages=messages+ "error at readLine, something wrong:"+e;
                    break;
                }
            }
            
  //      }
//          resultArea.repaint();
            messages=messages= "connection closed.\n";
            
            try{
               dinstream.close();
            }
            catch(IOException e) {messages=messages="close error.\n";}
//          WebPagePane.setText(page);
            this.isLoading=false;
    }

    public void run()
    {
        // This method is derived from interface java.lang.Runnable
        // to do: code goes here
        if(me!=null){            
            load();
            stop();       
        }
        this.isLoading=false;
       
    }

}
