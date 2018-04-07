package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.io.BufferedReader;
import java.io.IOException;

public class InputQueue extends java.lang.Object
{
    public int length()
    {
        return totalLength;
    }

    public int totalLength;

    public char getOne()
    {
        char rtn;
        if(!readLine())  return 0;
        rtn=lineBuffer[pStart];
        pStart++;
//        System.out.println("pStart="+pStart+":"+getLineBufferString());
        return rtn;
    }
    public boolean isNumerical(char c)
    {
        if(('0'<=c) && (c<='9')) return true;
        return false;
    }
    public boolean isAlphabet(char c)
    {
        if(('a'<=c) && (c<='z')) return true;
        if(('A'<=c) && (c<='Z')) return true;
        return false;
    }
    public String rName()
    {
        String rtn="";

        if(!readLine()) return null;

        while(pStart<pEnd){
            char c=lineBuffer[pStart];
            if(!isAlphabet(c)) return null;
            rtn=""+c;
            pStart++;
//            System.out.println("pStart=p"+pStart+":"+lineBuffer.toString());
        }
        while(pStart<pEnd){
            char c=lineBuffer[pStart];
            if(!(isAlphabet(c)||isNumerical(c))) break;
            rtn=rtn+c;
            pStart++;
//            System.out.println("pStart=p"+pStart+":"+getLineBufferString());
        }
        return rtn;
    }
    public boolean isLineChanged;
    public boolean back(int p)
    {
        if(isLineChanged) return false;
        pStart=p;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());
        return true;
    }
    public int mark()
    {
        if(!readLine()) return -1;
        isLineChanged=false;
        return pStart;
    }
    public boolean rNot(String s)
    {
        int l1,l2;

        if(!readLine()) return false;

        l1=s.length();
        l2=pEnd-pStart;

        if(l1>l2) {pStart+=1; return true;}

        boolean rtn=false;

        for(int i=0;i<l1;i++){
            char c=s.charAt(i);
            if(lineBuffer[pStart+i]!=c) {
                rtn=true;
                break;
            }
        }
        if(rtn) {pStart+=1; 
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());

        return true;} else return false;

    }
    public boolean eos()
    {
         String rl;
        int length;
        boolean reRead;
        rl=null;
        if( (lineBuffer[0] != '\n') &&
            (pStart<pEnd)               )return false;
        reRead=true;
//        while(reRead){
          try{ rl=inputStream.readLine();}
          catch(IOException e){ return true;}
          catch(NullPointerException e)
          {  reRead=true;
             try{ Thread.sleep(20);}
             catch(InterruptedException e2){}
          }
          if(rl==null) return true;
//        }
        length=rl.length();
        totalLength=totalLength+length;
        rl.getChars(0,length,lineBuffer,0);
        pEnd=length;
        if(length==0) return true;
        lineBuffer[pEnd]=(char)0;
        pStart=0;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());
        return false;
    }
    public boolean rAny1()
    {

        if(!readLine()) return false;
        pStart++;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());

        return true;
    }
 /* read double quoted string constant(decoder)

          ex. "abc"
     
     the following is the encoder
 			if(c=='\''){
				sx=sx+'\\'; sx=sx+'\''; i++; }
			else
			if(c=='\"'){
				sx=sx+'\\'; sx=sx+'"'; i++; }
			else
			if(c=='\t'){
				sx=sx+'\\'; sx=sx+'t'; i++;
			}
			else
			if(c=='\\'){
				sx=sx+'\\'; sx=sx+'\\'; i++;
			}
			else
			if(c=='\b'){
				sx=sx+'\\'; sx=sx+'b'; i++;
			}
			else
			if(c=='\f'){
				sx=sx+'\\'; sx=sx+'f'; i++;
			}
			else
			if(c=='\n'){
				sx=sx+'\\'; sx=sx+"n"; i++;
			}
			else
			if(c=='\r'){
				sx=sx+'\\'; sx=sx+"r"; i++;
			}
			else
			if(c=='\t'){
				sx=sx+'\\'; sx=sx+"t"; i++;
			}
			else

			{ sx=sx+c; i++; }    
     
     
 */
    public StringBuffer rStrConst()
    {
        StringBuffer str=new StringBuffer("");
        if(!rString("\"")) return null;
        int p=pStart;
        while(lineBuffer[p]!='\"'){
            if(lineBuffer[p]=='\\')
                 { p++;
                   char c=lineBuffer[p];
				   if( c=='b') { str.append('\b'); p++;}
                   else
                   if( c=='f') { str.append('\f'); p++; }
                   else
				   if(  c=='n') { str.append('\n'); p++; }
    				else
                   if( c=='r') { str.append('\r'); p++; }
                   else
                   if( c=='t') { str.append('\t'); p++; }
                   else
                   	if( c=='\\'){ str.append('\\'); p++; }
                   	else
                   	if( c=='\"'){ str.append('"'); p++; }
                   	else
                   	if( c=='\''){ str.append('\''); p++; }
                   
                  }
            else { str.append(lineBuffer[p]); p++;     }
        }
        if(lineBuffer[p]!='\"') return null;
        p++;
        pStart=p;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());
        return str;
    }
    public int nBreak;
    public boolean isABreakSymbol(char a)
    {
        int i;
        for(i=0;i<nBreak;i++)
            if(a==breakSymbols[i]) return true;
        return false;
    }
    public char breakSymbols[]={' ',',','"','/','+','-','*','(',')','\n'};
    public int pEnd;
    public int pStart;
    public boolean readLine()
    {
         String rl;
        int length;
        boolean reRead;
        rl=null;
        if( (lineBuffer[0] != '\n') &&
            (pStart<pEnd)               )return true;
        reRead=true;
//        while(reRead){
          try{ rl=inputStream.readLine();}
          catch(IOException e){ return false;}
          catch(NullPointerException e)
          {
             return false;
//             reRead=true;
//             try{ Thread.sleep(20);}
//             catch(InterruptedException e2){}
          }
//          if(rl==null) reRead=true; else   reRead=false;
//        }
        if(rl==null) return false;
        length=rl.length();
        totalLength=totalLength+length;
        rl.getChars(0,length,lineBuffer,0);
        pEnd=length;
        if(length==0) return false;
        lineBuffer[pEnd]=(char)0;
        pStart=0;
        isLineChanged=true;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());
        return true;

     }
    public char lineBuffer[];
    public Integer rInteger()
    {
        int l1,l2,n,sign,p;
        int x;
        Integer xx;

        p=pStart;
        n=0;
        sign=1;

        if(!readLine()) return null;
        if(lineBuffer[p]=='-') {sign=-1; p++;}

        if(!('0'<=lineBuffer[p] && lineBuffer[p]<='9')) return null;
        x=(int)lineBuffer[p]-(int)'0'; p++;

        while('0'<=lineBuffer[p] && lineBuffer[p]<='9'){
            x=x*10+((int)lineBuffer[p]-(int)'0');
            p++;
        }
        x=x*sign;
        pStart=p;
//        System.out.println("pStart=p"+pStart+":"+getLineBufferString());

        xx=new Integer(x);
        return xx;
    }
    public boolean rString(String s)
    {
        int l1,l2;

        if(!readLine()) return false;

        l1=s.length();
        l2=pEnd-pStart;

        if(l1>l2) return false;

        for(int i=0;i<l1;i++){
            char c=s.charAt(i);
            if(lineBuffer[pStart+i]!=c) return false;
        }
        pStart+=l1;
//        System.out.println("pStart="+pStart+":"+getLineBufferString());

        return true;
    }
    String getLineBufferString(){
    	String x="";
    	for(int i=0;i<pEnd;i++){
    		x=x+lineBuffer[i];
    	}
    	return x;
    }
    public BufferedReader inputStream;
    public InputQueue(BufferedReader input)
    {
        lineBuffer = new char[3500];
        pStart=0;
        pEnd=0;
        inputStream=input;
 //       breakSymbols[]={' ',',','"','/','+','-','*','(',')','\n'};
        nBreak=10;
        isLineChanged=false;
        totalLength=0;
    }
    boolean skipRestFlag=false;
    public void skipRest(){
    	skipRestFlag=true;
    }
    public void printLine(){
    	System.out.println(getLineBufferString());
    }
   
}

