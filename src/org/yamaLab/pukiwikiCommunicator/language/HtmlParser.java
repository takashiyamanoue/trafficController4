package org.yamaLab.pukiwikiCommunicator.language;

import java.util.Vector;
public class HtmlParser extends java.lang.Object 
{
    public String source;
    public LispObject result;

    public HtmlParser(String s,ALisp lisp)
    {
        init(lisp);
        this.htmlTokenizer=new HtmlTokenizer(s);
        this.result=parse();
    }

    public LispObject listFromStack(int i)
       /*
           0 1 ... (object(i), object(i+1), ..., object(pointerToStack-1))
       */
    {
        LispObject x=lisp.nilSymbol;
        if(i<0) return lisp.nilSymbol;
        while(this.pointerToStack>i){
            pointerToStack--;
            x=lisp.cons((LispObject)(this.objectStack.elementAt(pointerToStack)),
                         x);
         }
         return x;
    }

    public LispObject parseCloseTag(String element)
    {
            if(!(element.charAt(0)=='<')) return lisp.nilSymbol;
            
            if(!(element.charAt(1)=='/')) return lisp.nilSymbol;
            
            LexicalAnalyzer la=new LexicalAnalyzer();
            char[] breaksymbol={' ','\n','\t','\r','<','>','/'};
            la.setBreakSymbols(breaksymbol);
            la.setText(element);
            la.parseTokens();
            String dym=la.nextToken();
            dym=la.nextToken();
            String first=la.nextToken();
            if(first==null) return lisp.nilSymbol;
            LispObject x=lisp.cons(lisp.recSymbol(first),lisp.nilSymbol);
            x=lisp.cons(lisp.recSymbol("closetag"),x);
            while(la.hasMoreTokens()){
                String e=la.nextToken();
                if(e.equals(">")) break;
                LispObject y=new MyString(e);
                y=lisp.cons(lisp.recSymbol("string"),lisp.cons(y,lisp.nilSymbol));
                x=lisp.append(x,y);
            }
            return x;
      }

    public LispObject parseOpenTag(String element)
    {
            if(!(element.charAt(0)=='<')) return lisp.nilSymbol;
            if(element.charAt(1)=='?') return lisp.nilSymbol;
            if(element.charAt(1)=='!') return lisp.nilSymbol;
            if(element.charAt(1)=='/') return lisp.nilSymbol;
            if(element.charAt(1)==' ') return lisp.nilSymbol;
            if(element.charAt(1)=='=') return lisp.nilSymbol;
            if(element.endsWith("/>")) return lisp.nilSymbol;
            LexicalAnalyzer la=new LexicalAnalyzer();
            char[] breaksymbol={'!',' ','\n','\t','\r','<','>','/'};
            la.setBreakSymbols(breaksymbol);
            la.setText(element);
            la.parseTokens();
            String dym=la.nextToken();
            String first=la.nextToken();
            if(first==null) return lisp.nilSymbol;
            LispObject x=lisp.cons(lisp.recSymbol(first),lisp.nilSymbol);
            x=lisp.cons(lisp.recSymbol("tag"),x);
            while(la.hasMoreTokens()){
                String e=la.nextToken();
                if(e.equals("/")){
                	e=la.nextToken();
                	if(e.equals(">")){
                		return lisp.nilSymbol;
                	}
                	else{
                		la.backToken();
                	}
                }
                if(e.equals(">")) {
                	break;
                }
                LispObject y=new MyString(e);
                y=lisp.cons(
                		lisp.cons(lisp.recSymbol("string"),lisp.cons(y,lisp.nilSymbol)),
                		lisp.nilSymbol);
                x=lisp.append(x,y);
            }
            return x;
             
   }
   public LispObject parseXmlDeclaration(String element){
       if(!(element.charAt(0)=='<')) return lisp.nilSymbol;
       if(!(element.charAt(1)=='?')) return lisp.nilSymbol;
       element=element.substring(1);
       LexicalAnalyzer la=new LexicalAnalyzer();
       char[] breaksymbol={'!',' ','\n','\t','\r','<','>','?'};
       la.setBreakSymbols(breaksymbol);
       la.setText(element);
       la.parseTokens();
       String dym=la.nextToken();
       String first=la.nextToken();
       if(first==null) return lisp.nilSymbol;
       LispObject x=lisp.cons(lisp.recSymbol(first),lisp.nilSymbol);
       x=lisp.cons(lisp.recSymbol("xml-dcl"),x);
       while(la.hasMoreTokens()){
           String e=la.nextToken();
           if(e.equals("?")) {
        	   e=la.nextToken();
        	   if(e.equals(">")){
           		break;
        	   }
        	   else{
        		   la.backToken();
        	   }
           }
           LispObject y=new MyString(e);
           y=lisp.cons(
           		lisp.cons(lisp.recSymbol("string"),lisp.cons(y,lisp.nilSymbol)),
           		lisp.nilSymbol);
           x=lisp.append(x,y);
       }
       return x;             
   }
   public LispObject parseComment(String element){
       if(!(element.charAt(0)=='<')) return lisp.nilSymbol;
       if(!(element.charAt(1)=='!')) return lisp.nilSymbol;
       element=element.substring(1);
       LexicalAnalyzer la=new LexicalAnalyzer();
       char[] breaksymbol={'!',' ','\n','\t','\r','<','>','?'};
       la.setBreakSymbols(breaksymbol);
       la.setText(element);
       la.parseTokens();
       String dym=la.nextToken();
       String first=la.nextToken();
       if(first==null) return lisp.nilSymbol;
       LispObject x=lisp.cons(lisp.recSymbol(first),lisp.nilSymbol);
       x=lisp.cons(lisp.recSymbol("!"),x);
       while(la.hasMoreTokens()){
           String e=la.nextToken();
      	   if(e.equals(">")){
           		break;
           }
           LispObject y=new MyString(e);
           y=lisp.cons(
           		lisp.cons(lisp.recSymbol("string"),lisp.cons(y,lisp.nilSymbol)),
           		lisp.nilSymbol);
           x=lisp.append(x,y);
       }
       return x;             
   }
    
    public LispObject parseSingleTag(String element)
    {
            if(!(element.charAt(0)=='<')) return lisp.nilSymbol;
            if(element.charAt(1)=='/') return lisp.nilSymbol;
            if(element.charAt(1)==' ') return lisp.nilSymbol;
            if(element.charAt(1)=='=') return lisp.nilSymbol;
            LexicalAnalyzer la=new LexicalAnalyzer();
            char[] breaksymbol={'!',' ','\n','\t','\r','<','>','/'};
            la.setBreakSymbols(breaksymbol);
            la.setText(element);
            la.parseTokens();
            String dym=la.nextToken();
            String first=la.nextToken();
            if(first==null) return lisp.nilSymbol;
            LispObject x=lisp.cons(lisp.recSymbol(first),lisp.nilSymbol);
            x=lisp.cons(lisp.recSymbol("stag"),x);
            while(la.hasMoreTokens()){
                String e=la.nextToken();
                if(e.equals("/")) {
                	e=la.nextToken();
                	if(e.equals(">")){
                		break;
                	}
                	else{
                		la.backToken();
                	}
                }
                LispObject y=new MyString(e);
                y=lisp.cons(
                		lisp.cons(lisp.recSymbol("string"),lisp.cons(y,lisp.nilSymbol)),
                		lisp.nilSymbol);
                x=lisp.append(x,y);
            }
            return x;             
    }
    
    public boolean isCloseTag(LispObject e)
    {
        if(lisp.equal(lisp.car(e),lisp.recSymbol("closetag"))) return true;
        else return false;
    }

    public LispObject parseElement(String element)
    {
        LispObject  x=parseOpenTag(element);
        if(!lisp.Null(x)) return x;
        else{
          x=parseXmlDeclaration(element);
          if(!lisp.Null(x)) return x;
          else{
        	 x=parseComment(element);
        	 if(!lisp.Null(x)) return x;
             else{
               x=parseSingleTag(element);
               if(!lisp.Null(x)) return x;
               else{
            	  x=parseCloseTag(element);
            	  if(!lisp.Null(x)) return x;
            	  else{
                     x=new MyString(element);
                     x=lisp.cons(x,lisp.nilSymbol);
                     LispObject rtn=lisp.cons(lisp.recSymbol("string"),x);
                     return rtn;
            	  }
               }
             }
          }
        }
    }

    public boolean isPairTag(LispObject a, LispObject b)
    {
//    	System.out.println("isPairTag");
//    	lisp.plist2("a=",a);
//    	lisp.plist2("b=",b);
       LispObject tagname=lisp.car(lisp.cdr(a));
       LispObject tagclose=lisp.car(lisp.cdr(b));
       boolean rtn=lisp.equal(tagname,tagclose);
       return rtn;
       
    }

    public LispObject reduceStack(LispObject e)
    {
        LispObject x=lisp.nilSymbol;
        int p=pointerToStack;
        while(p>0){
            p--;
            LispObject w=(LispObject)(this.objectStack.elementAt(p));
            if(lisp.atom(lisp.car(w))){
              if(this.isPairTag(w, e)){
                  x=listFromStack(p);
//                  lisp.plist2("reduce ",x);
                   this.pointerToStack=p;
                  return x;
              }
            }
        }
        if(p<=0){
        	return null;
        	/*
        	x=listFromStack(p);
        	this.pointerToStack=0;
        	*/
        }
        /*
        if(p==0){
        	x=listFromStack(p);
        	this.pointerToStack=0;
        	
        }
        */
         return x;
    }

    public void shiftStack(LispObject e)
    {
    	if(this.pointerToStack>0){
    	   LispObject top=(LispObject)(objectStack.elementAt(pointerToStack-1));
    	   if(isString(e)&&isString(top)){
    		   e=concatinate(e,top);
    		   pointerToStack--;
    	   }
    	}
    	if(objectStack.size()>this.pointerToStack){
            objectStack.setElementAt(e,this.pointerToStack);
    	}
    	else{
    	    objectStack.addElement(e);
    	}
        this.pointerToStack++;
    }

    public boolean isString(LispObject x){
    	if(lisp.atom(x)) return false;
    	LispObject tag=lisp.car(x);
    	if(lisp.eq(tag,lisp.recSymbol("string"))) return true;
    	return false;
    }
    public LispObject concatinate(LispObject x, LispObject y){
    	if(!isString(x)) return null;
    	if(!isString(y)) return null;
    	LispObject wxs=lisp.car(lisp.cdr(x));
    	LispObject wys=lisp.car(lisp.cdr(y));
    	String xs=((MyString)wxs).toString();
    	String ys=((MyString)wys).toString();
    	xs=xs+ys;
    	LispObject rtn=new MyString(xs);
    	rtn=lisp.cons(rtn,lisp.nilSymbol);
    	rtn=lisp.cons(lisp.recSymbol("string"),rtn);
    	return rtn;
    }
    
    public int pointerToStack;

    public void init(ALisp lisp)
    {
//        this.lisp=new ALisp();
        this.lisp=lisp;
        this.tagNameStack=new Vector();
        this.objectStack=new Vector();
        this.pointerToStack=0;
    }

    public ALisp lisp;

    public Vector objectStack;

    public Vector tagNameStack;

    public LispObject object;

    public LispObject parse()
    {
//        LispObject rtn=lisp.nilSymbol;
//        LispObject firstObj=lisp.nilSymbol;
        LispObject e=lisp.nilSymbol;
        while(htmlTokenizer.hasMoreTokens()){
            e=parseElement(htmlTokenizer.nextToken());
//            lisp.plist2("e---",e);
            if(!lisp.atom(e)){
            	LispObject w=lisp.cdr(e);
            	if(!lisp.atom(w)){
            		w=lisp.car(w);
            		if(lisp.eq(lisp.recSymbol("w:WordDocument"),w)){
            			System.out.println("...");
            		}
            	}
            }
            if(isCloseTag(e)){
                e=reduceStack(e);
            }
            if(e!=null)
            shiftStack(e);
        }
        if(pointerToStack>0){
    	   e=listFromStack(0);
    	   shiftStack(e);
    	   this.pointerToStack=0;
        }
        LispObject rtn=(LispObject)(this.objectStack.elementAt(0));
//        lisp.plist2("parse..",rtn);
        return rtn;
    }
        

    public void comment()
    {
        /*
        
        parse() translates the given html into an S expression.
        
        example.
        
        <html>
        <title>title </title>
        <body>
        <h1> H1 String </h1>
        <hr>
        normal string
        <li> item 1
        <ul>
           <li> item 1-1
        </ul>
        <li> item 2
        <ol> 
            <li> <img src="img1"> num 2-1
            <li> <a href="url">num 2-2</a>
        </ol>
        </body>
        </html>
        
        
        is translated into
        
        ((tag html)
           ((tag title)  (string "title"))
           ((tag body)
               ((tag h1) (string "H1 String"))
               (tag hr)
               (string "normal string")
               (tag li) 
                   (string "item 1")
                   ((tag ul)
                       ((tag li) (string "item 1-1")))
               (tag li)
                   (string "item 2")
                   ((tag ol)
                       (tag li) 
                          (tag img src="img1")
                          (string "num 2-1")
                       (tag li)
                          ((tag a href="url1")
                             (string "num 2-2"))
                   )         
            )
         )
        
         
         Lexical rule (process by HtmlTokenizer)
           1. HTML      ::= ELEMNT*
           2. ELEMENT   ::= TAG + CLOSE_TAG + String
           3. TAG       ::= <tagname[ PARAMETER] (> + />)
           4. CLOSE_TAG ::= </tagname>
           5. String    ::= &<specialterm> + else<anotherstring>
           
         Translation rule (process by HtmlParser)
           0. ELEMENT*
                   -> (ELEMENT ... ELEMENT)
         
           1. <tagname  p1 ... pn>
                   -> (tag tagname p1 ... pn)
           2. <tagname  p1 ... pn> ELEMENT* </tagname>
                   -> ((tag tagname p1 ... pn)
                        Translate(ELEMENT*)
                      )
           3. String
                   -> (string "string")
           
        
        
        */
    }

    public LispObject getResult()
    {
        return this.result;
    }

    public void setHtmlText(String text)
    {
        htmlTokenizer=new HtmlTokenizer(text);
    }

    public HtmlParser()
    {
    }

    public HtmlTokenizer htmlTokenizer;

}