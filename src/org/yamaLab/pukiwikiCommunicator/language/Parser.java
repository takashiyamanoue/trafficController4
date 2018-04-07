package org.yamaLab.pukiwikiCommunicator.language;

import java.lang.*;
import java.awt.*;
//import android.widget.EditText;
public class Parser extends java.lang.Object
{
    public InterpreterInterface message;
    public Parser()
    {
    }
    public boolean parseExpressionList(LispObject x, LispObject type)
    {
        ListCell w=new ListCell();
        w.d=lisp.nilSymbol;
        if(!parseExpression(w,type)) return false;
        while(is(",")){
            ListCell y=new ListCell();
            if(!parseExpression(y,type)) return false;
            y.d=lisp.nilSymbol;
            lisp.nconc(w,y);
        }
        ((ListCell)x).a=lisp.cons(lisp.sym_list,w);
        return true;
    }
    public boolean parseParenList(LispObject x,LispObject type)
    {
        if(!is("(")) return false;
        if(!parseExpressionList(x,type)) return false;
        if(!is(")")) return false;
        return true;

    }
    public boolean leftHandSide(LispObject x, LispObject type, LispObject lt)
    {
        ListCell y=new ListCell();
        ListCell w=new ListCell();
        y.d=lisp.nilSymbol;
        LispObject prevPointer=pointer;
        if(!isName(w,type)) {
        	pointer=prevPointer;
        	return false;
        }
        if(parseParenList(y,type)){
            ((ListCell)lt).a=lisp.recSymbol("dimension");
            w.d=lisp.nilSymbol;
//            LispObject u=lisp.cons(lisp.recSymbol("quote"),w);
//            ((ListCell)x).a=lisp.cons(u,y);
            ((ListCell)x).a=lisp.cons(w.a,y);
//            ((ListCell)x).a=w.a;
            return true;
        }
        else{
            ((ListCell)x).a=w.a;
            ((ListCell)lt).a=lisp.recSymbol("var");
            return true;
        }
    }

    public boolean parseAssign(LispObject x)
    {
        boolean dmy;
        ListCell lt=new ListCell();
        ListCell left=new ListCell();
        ListCell right=new ListCell();
        ListCell type=new ListCell();
        LispObject prevPointer=pointer;
        if(!leftHandSide(left,type,lt)) {
        	pointer=prevPointer;
        	return false;
        }
        dmy=rB();
        if(!is("=")) {
        	pointer=prevPointer;
        	return false;
//            ((ListCell)x).a= ((ListCell)left).a;
//        	return true;
        }
        if(!parseExpression(right,type)) {
        	pointer=prevPointer;
        	return false;
        }
        if(lisp.eq(lt.a,lisp.recSymbol("dimension"))){
            right.d=lisp.nilSymbol;
            lisp.nconc(((ListCell)left).a,right);
            ((ListCell)x).a=lisp.cons(lisp.recSymbol("aput"),
                                       ((ListCell)left).a);
            return true;
        }
        if(lisp.eq(lt.a,lisp.recSymbol("var"))){
//            LispObject quoted=consUnaryOpr("quote",left.a);
            ((ListCell)x).a=consBinaryOpr(lisp.sym_setq,left.a,right.a);
            return true;
        }
        pointer=prevPointer;
        return false;
    }
    public LispObject parseLine()
    {
        ListCell x=new ListCell();
        if(parsePrint(x)) return x.a;
        if(parseAssign(x)) return x.a;
        return null;
    }
    public LispObject parseLine(LispObject l)
    {
        line=l;
        pointer=line;
        return parseLine();
    }
    public Parser(ALisp l,InterpreterInterface m)
    {
        line=null;
        pointer=null;
        lisp=l;
        message=m;
    }
    public void dmy()
    {
    }
    public boolean rB()
    {
        if(!is(" ")) return false;
        while(is(" ")){
            dmy();
        }
        return true;
    }
    public LispObject consUnaryOpr(String opr, LispObject x)
    {
        LispObject u,v;
        v=lisp.cons(x,lisp.nilSymbol);
        u=lisp.cons(lisp.recSymbol(opr),v);
        return u;
    }
    public boolean parsePrint(LispObject x)
    {
        ListCell type=new ListCell();
        if(is("?")||is("print")||is("PRINT")){
            if(!parseExpression(x,type)) return false;
            ((ListCell)x).a=consUnaryOpr("print",((ListCell)x).a);
            return true;
        }
        return false;
    }
    public LispObject consBinaryOpr(LispObject opr,LispObject x, LispObject y)
    {
        LispObject u,v,w;
        w=lisp.cons(y,lisp.nilSymbol);
        v=lisp.cons(x,w);
        u=lisp.cons(opr,v);
        return u;
    }
    public boolean isNumber(LispObject x,LispObject type)
    {
        if(lisp.Null(pointer)) return false;
        LispObject s=((ListCell)pointer).a;
        if(s.isKind("myint")){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
        if(s.isKind("mydouble")){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
//        if(s.getClass().getName().equals("MyString")){
        if(s.isKind("mystring")){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("string");
            pointer=((ListCell)pointer).d;
            return true;
        }
//        if(s.getClass().getName().equals("MyNumber")){
        if(s.isKind("mynumber")){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
        return false;
    }
    public boolean isName(LispObject x, LispObject type)
    {
        if(lisp.Null(pointer)) return false;
        LispObject s=((ListCell)pointer).a;
//        if(!s.getClass().getName().equals("ListCell")) return false;
        if(s.isAtom()) return false;
//        if(isReservedNR()) return false;
        if(!lisp.eq(lisp.car(s),lisp.recSymbol("name"))) return false;
        ((ListCell)x).a=lisp.car(lisp.cdr(s));
        ((ListCell)type).a=lisp.recSymbol("number");
        pointer=((ListCell)pointer).d;
        return true;
    }
    public boolean isReservedNR()
    {
        if(lisp.Null(pointer)) return false;
        Symbol sym=(Symbol)(((ListCell)pointer).a);
        String str=(String)(lisp.symbolTable.get(sym));
        if(isReserved(str)) return true;
        return false;
    }
    public String symbol2str(Symbol x)
    {

        String y;
        y=((Symbol)(lisp.symbolTable.get(
             new Integer(x.hc)))).name;
        return y;
    }
    public ALisp lisp;
    public boolean is(String x)
    {
        String y;
        if(lisp.Null(pointer)) return false;
        LispObject o=((ListCell)pointer).a;
//        if(!(o.getClass().getName().equals("Symbol"))) return false;
        if(!o.isKind("symbol")) return false;
        y=symbol2str((Symbol)o);
        if(!y.equals(x)) return false;
        pointer=((ListCell)pointer).d;
        return true;
    }
    public boolean parseElement(LispObject x,LispObject type)
    {
        boolean dmy;
        char sign=' ';
        LispObject w,v;
        dmy=rB();
        if(is("(")){
            if(!parseExpression(x,type)) return false;
            if(is(")")) return true;
            return false;
        }
        if(is("-")) {sign='-'; dmy=rB();}
        if(is("!")) {sign='!'; dmy=rB();}        
        if(isNumber(x,type)) {
            if(sign=='-'){
                w=lisp.cons(lisp.car(x),lisp.nilSymbol);
                w=lisp.cons(lisp.recSymbol("neg"),w);
                ((ListCell)x).a=w;
            }
            if(sign=='!'){
                w=lisp.cons(lisp.car(x),lisp.nilSymbol);
                w=lisp.cons(lisp.recSymbol("bnot"),w);
                ((ListCell)x).a=w;
            }
            
            return true;
        }
//        if(isReservedNR()) return false;
        if(isName(x,type)){
            ListCell y=new ListCell();
            y.d=lisp.nilSymbol;
            if(parseParenList(y,type)){
                ListCell u=new ListCell();
                u.d=lisp.nilSymbol;
                u.a=((ListCell)x).a;
//                LispObject f=lisp.get(((ListCell)x).a,
//                                      lisp.recSymbol("lambda"));
                /*
                if(lisp.Null(f)){ // case of array reference
//                   w=lisp.cons(lisp.recSymbol("quote"), u);
//                   v=lisp.cons(w,y)
                     v=lisp.cons(((ListCell)x).a,y);
                   ((ListCell)x).a=lisp.cons(lisp.recSymbol("aget"),v);
                }
                else{ // case of function call
                   LispObject argl=lisp.cdr(lisp.car(y));
                   ((ListCell)x).a=lisp.cons(((ListCell)x).a,argl);
                }
                */
                LispObject argl=lisp.cdr(lisp.car(y));
                ((ListCell)x).a=lisp.cons(((ListCell)x).a,argl);


            }
            if(sign=='-'){
              w=lisp.cons(lisp.car(x),lisp.nilSymbol);
              w=lisp.cons(lisp.recSymbol("neg"),w);
              ((ListCell)x).a=w;
            }
            if(sign=='!'){
                w=lisp.cons(lisp.car(x),lisp.nilSymbol);
                w=lisp.cons(lisp.recSymbol("bnot"),w);
                ((ListCell)x).a=w;
            }
            
            return true;
        }
        return false;
    }
    public boolean parseFactor(LispObject x, LispObject type)
    {
        ListCell y=new ListCell();
        if(!parseElement(x,type)) return false;
        do{
            if(is("^")){
                if(parseElement(y,type)){
                   ((ListCell)x).a=consBinaryOpr(lisp.sym_m_exp2,
                       ((ListCell)x).a,((ListCell)y).a);
                }
            }
            else return true;
        } while(true);
    }
    public boolean parseTerm(LispObject x, LispObject type)
    {
        ListCell y=new ListCell();
        boolean dmy;        
        if(!parseFactor(x,type)) return false;
        dmy=rB();        
        do{
            if(is("*")){
                if(parseFactor(y,type)){
                  ((ListCell)x).a=consBinaryOpr(lisp.sym_m_mul,
                     ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            else if(is("/")){
                if(parseFactor(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_div,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();                
            }
            else if(is("&")){
                if(parseFactor(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_band,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
                
            }
            
            else return true;
        } while(true);
    }
    public LispObject x;
    public boolean parseExpression(LispObject x,LispObject type)
    {
        ListCell y=new ListCell();
        boolean dmy;
        if(!parseTerm(x,type)) return false;
        dmy=rB();
        do{
          //  dmy=rB();
            if(is("+")){
                if(parseTerm(y,type)){
                   ((ListCell)x).a=consBinaryOpr(lisp.sym_m_add,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            else if(is("-")){
                if(parseTerm(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_sub,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            else if(is("|")){
                if(parseTerm(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_bor,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            
            else return true;
        } while(true);
    }
    public int sizeOfReserveSymbols;
    public boolean isReserved(String s)
    {
        int i;
        for(i=0;i<sizeOfReserveSymbols;i++){
            if(s.equals(reserveSymbols[i])) return true;
        }
        return false;
    }
    public String reserveSymbols[];
    public void setReserveSymbols(String[] s, int l)
    {
        reserveSymbols=s;
        sizeOfReserveSymbols=l;
    }
    public void comment()
    {
        /*
        This Class is for parse a program.

        line is the input.
        pointer is the pointer to the line.
        */
    }
    public LispObject pointer;
    public LispObject line;
    public Parser(LispObject s,ALisp l)
    {
        line=s;
        pointer=line;
        lisp=l;
    }
}


