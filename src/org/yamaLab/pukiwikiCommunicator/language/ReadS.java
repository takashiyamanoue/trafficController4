package org.yamaLab.pukiwikiCommunicator.language;
public class ReadS extends java.lang.Object
{
    public boolean readStringConst()
    {
        int dmy;
        String buff="";
        if(!rCh('"')) return false;
        char c=(char)(in.prevRead1());
        while(true){
            if(c=='\\') {
                dmy=in.get();
                c=(char)(in.prevRead1());
                dmy=in.get();
                buff=buff+c;
                c=(char)(in.prevRead1());
            }
            else if(c=='"'){
                dmy=in.get();
                break;
            }
            else{
              buff=buff+c;
              dmy=in.get();
              c=(char)(in.prevRead1());
            }
        }
        x=new MyString(buff);
        return true;

    }

    public void dmy()
    {
    }
    public ReadS()
    {
    }
    public boolean rNumOpr()
    {
        clearName();
        if(rCh('<')) {
            if(rCh('=')||rCh('>')){
                x=lisp.recSymbol(name);
                return true;
            }
            x=lisp.recSymbol(name);
            return true;
        }
        if(rCh('>')){
            if(rCh('=')){
               x=lisp.recSymbol(name);
               return true;
            }
            x=lisp.recSymbol(name);
            return true;
        }
        if(rCh('=')||
           rCh('+')||
           rCh('-')||
           rCh('*')||
           rCh('/')){
               x=lisp.recSymbol(name);
               return true;
        }
        else return false;
   }
    public LispObject read(CQueue i)
    {
        in=i;
        return read();
    }
    public boolean readQuote()
    {
        while(rB());
        if(!rCh('\'')) return false;
        if(!readS()) return false;
        ListCell l2= new ListCell();
        l2.a=x; l2.d=lisp.nilSymbol;
        ListCell l1= new ListCell();
        l1.a=lisp.recSymbol("quote");
        l1.d=l2;
        x=l1;
        return true;
    }
    public ALisp lisp;
    public boolean readList()
    {
        ListCell l,w1,w2;
        while(rB());
        if(!rCh('(')) return false;
        if(!readS()) return false;
        l=new ListCell();
        l.a=x; l.d=lisp.nilSymbol; w1=l; w2=l;
        while(readS()){
            w2=new ListCell();
            w2.a=x; w2.d=lisp.nilSymbol;
            w1.d=w2; w1=w2;
        }
        if(!rCh(')')) return false;
        x=l;
        return true;
    }
    public boolean readS()
    {
        while(rB())
         { try{Thread.sleep(10);} catch(InterruptedException e){}} ;
        if(readAtom())  return true;
        if(readQuote()) return true;
        if(readList())  return true;
        return false;
    }
    public boolean readNumber()
    {
        int s,n;
        double r,rx;
        s=1; n=0;
        if(rCh('-')) s=-1;

        int x1=in.prevRead1();  // hex?
        int x2=in.prevRead2();  // hex?
        if(x1==(int)'0' &&  x2==(int)'x') {  //hex?
         	in.rNext();
        	in.rNext();
        	clearName();
            if(!rHex()) return false;
            while(rHex());
            n=hexStr2i(name);
            if(!rCh('.')){
                x=new MyInt(s*n);
                return true;
             }            
        }
        else{
           clearName();
           if(!rNum()) return false;
           while(rNum());
           try{ n=Integer.parseInt(name);}
           catch(NumberFormatException e){}
           if(!rCh('.')){
              x=new MyInt(s*n);
              return true;
           }
           r=(double)n;
           rx=0.1;
           clearName();
           while(rNum()){
              int p;
              p=0;
              try{p=Integer.parseInt(name);}
              catch(NumberFormatException e){}
              r=r+p*rx;
              rx=rx*0.1;
              clearName();
           }
           x=new MyDouble(s*r);
           return true;
        }
        return false;
    }
    public boolean rB()
    {
        int x=in.prevRead1();
        if(x==(int)' ') {
            in.rNext();
            return true;
        }
        if(x==(int)'\n'){
            in.rNext();
            return true;
        }
        if(x==13){
            in.rNext();
            return true;
        }
        return false;
    }
    public void clearName()
    {
        name="";
    }
    public boolean rSymbol()
    {
        clearName();
        if(rNumOpr()) return true;
        if(!rA()) return false;
        while(rA()||rNum()||rCh('-')){
            dmy();
        }
        if(name.equals("line")){
        	System.out.println(name);
        }
        x=lisp.recSymbol(name);
        return true;

    }
    public boolean rNum()
    {
         int x=in.prevRead1();
        if((int)'0'<=x && x<=(int)'9') {
            conc(x); in.rNext();
            return true;
        }
        return false;
    }
    public boolean rHex()
    {
         int x=in.prevRead1();
        if((int)'0'<=x && x<=(int)'9') {
            conc(x); in.rNext();
            return true;
        }
        if((int)'A'<=x && x<=(int)'F') {
            conc(x); in.rNext();
            return true;
        }
        if((int)'a'<=x && x<=(int)'f') {
            conc(x); in.rNext();
            return true;
        }
        
        return false;
    }
    public int hexStr2i(String x){
    	int rtn=0;
    	int p=0;
    	while(p<x.length()){
    		char c=x.charAt(p);
    		if((int)'0'<=c && c<=(int)'9'){
    			rtn=rtn*16+((int)c-'0');
    		}
    		if((int)'a'<=c && c<=(int)'f'){
    			rtn=rtn*16+((int)c-'a'+10);
    		}
    		if((int)'A'<=c && c<=(int)'F'){
    			rtn=rtn*16+((int)c-'A'+10);
    		}
    		p++;
    	}
    	return rtn;
    }
    public boolean rCh(char c)
    {
        int x=in.prevRead1();
        if(x==(int)c) {
            conc(x); in.rNext();
            return true;
        }
        return false;
    }
    public CQueue in;
    public void conc(int x)
    {
        name=name+(char)x;
    }
    public String name;
    public boolean rA()
    {
        int x=in.prevRead1();
        if((int)'a'<=x && x<=(int)'z') {
            conc(x); in.rNext();
            return true;
        }
        if((int)'A'<=x && x<=(int)'Z') {
            conc(x); in.rNext(); return true;
        }
        return false;
    }
    public boolean readAtom()
    {
        if(rSymbol()||readStringConst()||readNumber()) return true;
        return false;
    }
    public LispObject read()
    {
        boolean rtn=readS();
//        while(rB())
//         try{Thread.sleep(10);} catch(InterruptedException e){};
        if(rtn) return x;
        else return null;

    }
    public String inLine;
    public void init()
    {
        x=null;
        inLine="";
    }
    public ReadS(CQueue cq, ALisp lsp)
    {
        init();
        lisp=lsp;
        in=cq;
    }
    public LispObject x;
}

