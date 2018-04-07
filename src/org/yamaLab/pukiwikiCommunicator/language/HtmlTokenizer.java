package org.yamaLab.pukiwikiCommunicator.language;
public class HtmlTokenizer extends LexicalAnalyzer  
{
    public HtmlTokenizer()
    {
    }


    public boolean rNonBreak()
    {
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

    public boolean parseNonTag()
    {
       if(isTag()) return false;        
        while(true){
          if(isTag()) return true;
          if(rStringConst()||
             rNonBreak()   ||
             rBreak())       {}
          else return true;
        }
//        return true;
    }

    public void init()
    {
        tags=new String[]{"applet","APPLET","a","A",  // 4
              "brockquote","BROCKQUOTE", "body","BODY","br","BR","b","B",//8,12
              "center","CENTER",  //2,14
              "dt","DT", "d","D",//4,18
              "frame","FRAME","form","FORM", "font","FONT",//6,24
              "head","HEAD","html","HTML","hr","HR","h","H", //8,32
              "img","IMG", //2,34
              "li","LI", //2,36
              "mailto","MAILTO","meta","META", //4,40
              "ol","OL", //2,42
              "pre","PRE","p","P", //4,46
              "t","T", //2,48
              "ul","UL", //2,50
              "title","TITLE", //2,52
              "!"}; //1,53;
        super.init();
        nOfTags=tags.length;
    }

    public boolean parseAToken()
    {
        aToken="";
        if(isTheEnd())     { return false;}
        if(rTag())         {tokens.addElement(aToken); return true;}
        if(parseNonTag())  {tokens.addElement(aToken); return true;}
        return false;
    }
    public HtmlTokenizer(String html)
    {
        theText=html;
        char[] bs={'<','>','/'};
        this.setBreakSymbols(bs);
        init();
        parseTokens();
        tokenIndex=0;
        
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
    public int nOfTags;
    public String tags[];
    /*
    ={"applet","APPLET","a","A",  // 4
              "brockquote","BROCKQUOTE", "body","BODY","br","BR","b","B",//8,12
              "center","CENTER",  //2,14
              "dt","DT", "d","D",//4,18
              "frame","FRAME","form","FORM", "font","FONT",//6,24
              "head","HEAD","html","HTML","hr","HR","h","H", //8,32
              "img","IMG", //2,34
              "li","LI", //2,36
              "mailto","MAILTO","meta","META", //4,40
              "ol","OL", //2,42
              "pre","PRE","p","P", //4,46
              "t","T", //2,48
              "ul","UL", //2,50
              "title","TITLE", //2,52
              "!"}; //1,53;
  */    public void comment()
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

