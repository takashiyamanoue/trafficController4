package org.yamaLab.pukiwikiCommunicator.language;
import java.util.Vector;
public class LexicalAnalyzer extends java.lang.Object
{
    public void setText(String text)
    {
        this.theText=text;
    }

    public void setBreakSymbols( char[] bsym)
    {
        this.breakSymbols=bsym;
        this.nOfBreakSym=bsym.length;
    }

    public LexicalAnalyzer()
    {
        init();
    }

    public boolean isTag()
    {
        if(isTheEnd()) return false;
        if(isC('<')){
            int c=lookup2();
            if(('a'<=c && c<='z')||
               ('A'<=c && c<='Z')||
               c=='!'            ||
               c=='/') return true;
        }
        return false;
    }

    public char lookup2()
    {
        char x;
        x=theText.charAt(1);
        return x;
    }

    public int tokenIndex;

    public boolean isBreak(char c)
    {
        nOfBreakSym=breakSymbols.length;
        for(int i=0;i<nOfBreakSym;i++){
            if(c==breakSymbols[i]) return true;
        }
        return false;
    }

    public boolean isTheEnd()
    {
        if(theText==null) return true;
        if(theText.equals("")) return true;
        return false;
    }

    public boolean rNonBreak()
    {
        char c;
         if(isBreak()) return false;
        if(isTheEnd()) return false;
        while(true){
           if(this.rStringConst()){}
           else  rAny1();
           if(isBreak()) return true;
           if(isTheEnd()) return true;
        }
//        return false;
    }

    public void init()
    {
        nOfBreakSym=breakSymbols.length;
        tokens=new Vector();
        tokenIndex=0;
    }

    public void parseTokens()
    {
        aToken="";
        while(true){
            if(!parseAToken()) return;
            if(isTheEnd()) return;
        }
    }

    public boolean parseAToken()
    {
        if(isTheEnd())     { return false;}
        aToken="";
        if(  rNonBreak()   ||
             rBreak()) {tokens.addElement(aToken); return true; }
        return false;
    }

    public Vector tokens;

    public char lookup1()
    {
        char x;
        x=theText.charAt(0);
        return x;
    }
    public boolean rSpace()
    {
        while(true)
        {
            if(!rC(' ')) return true;
            if(!rC('\r')) return true;
            if(!rC('\n')) return true;
            if(!rC('\t')) return true;
        }
    }
    public LexicalAnalyzer(String text)
    {
        theText=text;
        init();
        parseTokens();
        tokenIndex=0;
        
    }
    public boolean rName()
    {
        char c;
        if(theText==null) return false;
        if(theText.equals("")) return false;

        c=theText.charAt(0);
        if(isBreak(c)) return false;
        if(!rAny1()) return false;
        c=theText.charAt(0);
        while(!isBreak(c)){
            if(!rAny1()) return false;
            c=theText.charAt(0);
        }
        return true;
    }
    public boolean rTagName()
    {
       String t,f;
       char c;
/*
       for(int i=0;i<nOfTags;i++){
            t=tags[i];
            int l=t.length();
            f=theText.substring(0,l);
            if(t.equals(f)){
                aToken=aToken+t;
                theText=theText.substring(l);
                return true;
            }
        }
*/
       c=lookup1();
       if(c<'a'||'z'<c) {
           if(c<'A'||'Z'<c ) return false;
       }
       rAny1();
       rNonBreak();
       return true;
    }
    public boolean rTag()
    {
        if(!isTag()) return false;
        boolean dmy;
        if(!rC('<')) return false;
        while(!isC('>')){
            dmy=rStringConst();
            dmy=rNot('>');
        }
        if(!rC('>')) return false;
        return true;
    }
    public boolean rAny1()
    {
        if(theText==null) return false;
        if(theText.equals("")) return false;
        aToken=aToken+theText.substring(0,1);
        theText=theText.substring(1);
        return true;
    }
    public boolean rNot(char x)
    {
        if(theText.equals("")) return false;
        if(theText==null) return false;
        if(isNot(x)){
            aToken=aToken+theText.substring(0,1);
            theText=theText.substring(1);
            return true;
        }
        return false;
    }
    public boolean isNot(char x)
    {
        if(isC(x)) return false;
        else return true;
    }
    public boolean rC(char x)
    {
        if(isTheEnd()) return false;
        if(isC(x)) {
            aToken=aToken+x;
            theText=theText.substring(1);
            return true;
        }
        else return false;
    }
    public boolean isC(char x)
    {
        if(isTheEnd()) return false;
        if(theText.charAt(0)==x) return true;
        else return false;
    }
    public boolean rBreak()
    {
        if(isTheEnd()) return false;
        if(isBreak()){
            rAny1();
            return true;
        }
        return false;

    }
    
    public String nextToken()
    {
        String rtn=(String)(tokens.elementAt(tokenIndex));
        tokenIndex++;
        return rtn;
    }
    public void backToken(){
    	tokenIndex--;
    }
    public String theText;
    public boolean hasMoreTokens()
    {
        if(tokens==null) return false;
        if(tokenIndex>tokens.size()-1) return false;
        return true;
    }
    public String aToken;
    public boolean rStringConst()
    {
        if(!rC('\"')) return false;
        if(theText.equals("")) return false;
        if(theText==null) return false;
        while(!isC('\"')){
            if(isC('\\')){
               if(!rAny1()) return false;
               if(!rAny1()) return false;
            }
            if(isC('\n')){
            	return false;
            }
            if(!rAny1()) return false;
        }
        if(!rC('\"')) return false;
        return true;

    }
    public boolean isBreak()
    {
        if(isTheEnd()) return false;
        int c=lookup1();
        nOfBreakSym=breakSymbols.length;
        for(int i=0;i<nOfBreakSym;i++){
            if(c==breakSymbols[i]) return true;
        }
        return false;
    }
    public int nOfBreakSym;
    public char breakSymbols[]={'<', '>', '(', ')', '[', ']', '{', '}',
    
                                ' ', ',', ':', ';', '.', '?',
    
                                '/', '*', '+', '-', '=', 
                                '&', '%', '\'', '$', '#',

                                '!', '#', '"', '~', '|',
                                
                                '^','@','\t','\n','\r'};
    public void comment()
    {
        /*

        input
          text  ... html format
                    ex.
                        <a href=" ... "> ... </a>


                        "<a" "href" "=" "\" ... \"" ">" ... "</a" ">"
        */
    }

}