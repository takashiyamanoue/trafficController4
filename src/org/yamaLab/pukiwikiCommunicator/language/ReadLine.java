package org.yamaLab.pukiwikiCommunicator.language;
public class ReadLine extends ReadS
{
    public boolean readCommentLine()
    {
        if(!(rCh('\'')||
           rCh('#'))) return false;

           // this line is a comment
            while(!isEOL()){
                if(in.isEmpty()) break;
                int dmy=in.get();
            }
            boolean b=readEOL();
            x=lisp.nilSymbol;
            return true;
    }
    public boolean readStringConst()
    {
        int dmy;
        String buff="";
        if(!rCh('"')) return false;
        char c=(char)(in.prevRead1());
        int cx=(int)c;
        while(true){
//            if(c=='\\'||c=='¥') {
        	if(c=='\\') {
                dmy=in.get();
                c=(char)(in.prevRead1());
                buff=buff+c;
                dmy=in.get();
                c=(char)(in.prevRead1());                
                cx=(int)c;
            }
            else if(c=='"'){
                dmy=in.get();
                break;
            }
            else if(c=='\n'){
            	return false;
            }
            else if(c=='\0'){
            	return false;
            }
            else if(c=='\r'){
            	return false;
            }
            else if((int)c==65535){
            	return false;
            }
            else{
              buff=buff+c;
              dmy=in.get();
              c=(char)(in.prevRead1());
              cx=(int)c;
            }
        }
        x=new MyString(buff);
        return true;

    }
    public synchronized LispObject readProgram(CQueue i)
    {
        in=i;
        LispObject rtn=lisp.nilSymbol;
        while(!in.isEmpty()){
           if(readCommentLine()) {}
           else{
             while(isEOL()){
                 if(in.isEmpty()) break;
                 readEOL();
             }
             LispObject line=readLine();
             if(lisp.Null(line)) return rtn;
             if(lisp.gui.isTracing()){
                  lisp.plist("",line);
             }
             rtn=lisp.nconc(rtn,line);
           }
        }
        return rtn;
    }
    public int sizeOfReservedWord;
    public int sizeOfBreakSymbols;
    public ReadLine()
    {
    }
    public synchronized LispObject readLine(CQueue i)
    {
        in=i;
        if(readCommentLine()) return lisp.nilSymbol;
        else return readLine();
    }
    public boolean readEOL()
    {
        if(in.isEmpty()) return true;
        while(isEOL()) {
            if(in.isEmpty()) return true;
            int c=in.get();
        }
        return true;
    }
    public boolean rSymbol()
    {
        clearName();
//        if(rNumOpr()) return true;
        if(!rA()) return false;
        while(rA()||rNum())
        { dmy(); }
        if(isReserved(name)) x=lisp.recSymbol(name);
        else{
            LispObject u,v;
            u=lisp.recSymbol(name);
            v=lisp.cons(u,lisp.nilSymbol);
            x=lisp.cons(lisp.recSymbol("name"),v);
        }
        return true;
   }
    public boolean isEOL()
    {
        if(in.isEmpty()) return true;
        int c=in.prevRead1();
        if((char)c=='\n') return true;
        if((char)c=='\r') return true;
        return false;
    }
    public boolean isReserved(String s)
    {
        int i;
        for(i=0;i<sizeOfReservedWord;i++){
            if(s.equals(reservedWord[i])) return true;
        }
        return false;
    }
    public boolean isBreak(String s)
    {
        int i;
        for(i=0;i<sizeOfBreakSymbols;i++){
            if(s.equals(breakSymbols[i])) return true;
        }
        return false;
    }
    public boolean getNextToken()
    {
        int c;
        if(isEOL()) return false;
        if(readStringConst()) return true;
        c=in.prevRead1();
        String cw=""+(char)c;
        if(isBreak(cw)) {
            in.rNext();
            x=lisp.recSymbol(cw);
            return true;
        }
        if(readNumber()) return true;
        if(rSymbol()) return true;
        return false;
    }
    public void setBreak(String[] b,int s)
    {
        breakSymbols=b;
        sizeOfBreakSymbols=s;
    }
    public String breakSymbols[];
    public void setReserve(String[] r,int l)
    {
        reservedWord=r;
        sizeOfReservedWord=l;
    }
    public String reservedWord[];
    public synchronized LispObject readLine()
    {
//        LispObject x=null;
        ListCell l,w1,w2;
        if(!getNextToken()) return lisp.nilSymbol;
        l=new ListCell();
        l.a=x; l.d=lisp.nilSymbol; w1=l; w2=l;
        while(getNextToken()){
            // make an atom from s
            // add (s) to the x's tail.
//            if(!getNextToken()) return lisp.nilSymbol;
            w2=new ListCell();
            w2.a=x;  w2.d=lisp.nilSymbol;
            w1.d=w2; w1=w2;
        }
        if(!readEOL()) return lisp.nilSymbol;
        return l;
    }
    public ReadLine(CQueue iq, ALisp lsp)
   {
        init();
        lisp=lsp;
        in=iq;
    }
}

