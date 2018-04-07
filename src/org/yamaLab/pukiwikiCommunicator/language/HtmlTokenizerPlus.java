package org.yamaLab.pukiwikiCommunicator.language;

import java.util.StringTokenizer;
import java.util.Vector;

public class HtmlTokenizerPlus extends HtmlTokenizer{
	public HtmlTokenizerPlus(String x){
		super(x);
	}
    public boolean parseAToken()
    {
        aToken="";
        if(isTheEnd())     { return false;}
        if(rTag())         {tokens.addElement(aToken); return true;}
        if(parseNonTag())  {return true;}
        return false;
    }
    public boolean parseNonTag()
    {
       if(isTag()) return false;        
        while(true){
          if(isTag()) return true;
          if(rStringConst()||
             rNonBreak2()  ||
             rBreak())       {}
          else return true;
        }
//        return true;
    }
    /*
    public boolean rNonBreak()
    {
    	Vector words=new Vector();
        char c;
        if(this.isTag()) return false;
        if(isBreak()) return false;
        if(isTheEnd()) return false;
        while(true){
           rAny1();
           if(isTag()) return true;
           if(isBreak()) return true;
           if(isTheEnd()) return true;
        }
//        return false;
    }
    */
    public boolean rNonBreak2(){
    	if(rNonBreak()){
    		StringTokenizer st=new StringTokenizer(this.aToken);
    		while(st.hasMoreTokens()){
    		    tokens.addElement(st.nextToken());
    		}
    		return true;
    	}
    	else return false;
    	
    }

}
