package org.yamaLab.pukiwikiCommunicator.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JTextArea;

import org.yamaLab.pukiwikiCommunicator.controlledparts.FrameWithLanguageProcessor;

public class LispAndFile extends ALisp
{
//    public void init(JTextArea rarea, JTextArea parea,CQueue iq, FrameWithLanguageProcessor g)
	public void init(JTextArea rarea, JTextArea parea, CQueue iq, InterpreterInterface g)
    {
         me=null;
         inqueue=iq;
        symbolTable=new Hashtable();
        nilSymbol  = recSymbol("nil");
        environment=cons(nilSymbol,nilSymbol);
        tSymbol    = recSymbol("true");
        initSymbols();
        initFunctionDispatcher();
    //    inqueue=iq;
    //    outqueue=oq;
//        readArea=rarea;
        readArea=rarea.getText();
//        printArea=parea;

        read=new ReadS(inqueue,this);
        print=new PrintSforSaveEnvironment(this);
        gui=g;
    }

    public CQueue cq;

    public JTextArea outText;

    public JTextArea inText;

    public void saveEnv(String path)
    {
        this.print.print(this.environment);
        
        FileOutputStream fouts=null;

        try{ fouts= new FileOutputStream(new File(path));}
        catch(Exception e){
            System.out.println(e.toString());
            Thread.dumpStack();
            return;
        }

        String outx=this.outText.getText();
//        byte[] buff=new byte[outx.length()];
        byte[] buff=null;
          buff=outx.getBytes();
        try{
            fouts.write(buff);
            fouts.flush();
            fouts.close();
        }
        catch(Exception e){ 
            System.out.println(e.toString());
            Thread.dumpStack();
            return;
        }
   }

    public void readEnv(String path)
    {
         BufferedReader inputStream=null;

        File f=new File(path);
        try{
            inputStream=//new DataInputStream(url.openStream());
                    new BufferedReader(
                          new InputStreamReader(
                              new FileInputStream(f)
                          ));
        }
        catch(Exception e){
            System.out.println(e.toString());
            Thread.dumpStack();
            return;
       }

       this.inText.setText("");
       StringBuffer x=new StringBuffer("");
       String w=null;
       do{
          w=null;
          try{
            w=inputStream.readLine();
          }
          catch(java.io.IOException e){
            System.out.println(e.toString());
            Thread.dumpStack();
            return;
          }
          if(w!=null){
             x.append(w+"\n");
          }
       } while(w!=null);
       this.inText.setText(x.toString());
       this.cq.putString(this.inText.getText());
       this.environment=this.read.read(cq);
    }

    public void readEnvFromUrl(String urlName)
    {
		   URL url=null;
	       BufferedReader inputStream=null;
		   InputStream instream=null;
//		CQueue inqueue;
		   int rl=0;
		   int xl=0;
		   try{
    		   url=new URL(urlName);
    	   }
    	   catch(MalformedURLException e){
    		   System.out.println(e.toString());
//               printArea.appendText("URL Format Error\n");
    	   }
           try{
               instream=url.openStream();
           }
           catch(IOException e){
//               printArea.appendText("URL Open Error\n");
        	   System.out.println(e.toString());
           }
//        inqueue=new CQueue();
           try{
               inputStream=//new DataInputStream(url.openStream());
                       new BufferedReader(new InputStreamReader(instream));
           }
           catch(Exception e){
               System.out.println(e.toString());
               Thread.dumpStack();
               return;
          }
           
           StringBuffer x=new StringBuffer("");
           String w=null;
           do{
              w=null;
              try{
                w=inputStream.readLine();
              }
              catch(java.io.IOException e){
                System.out.println(e.toString());
                Thread.dumpStack();
                return;
              }
              if(w!=null){
                 x.append(w+"\n");
              }
           } while(w!=null);
           this.inText.setText(x.toString());
           this.cq.putString(this.inText.getText());
           this.environment=this.read.read(cq);           
           
    }
    
    public LispAndFile()
    {
        this.inText=new JTextArea();
        this.outText=new JTextArea();
        this.cq=new CQueue();
        this.init(inText,outText,cq,null);
    }

}