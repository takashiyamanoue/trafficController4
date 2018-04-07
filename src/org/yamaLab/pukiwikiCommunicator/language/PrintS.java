package org.yamaLab.pukiwikiCommunicator.language;

import java.lang.*;
import java.util.*;
public class PrintS extends java.lang.Object
{
    public void printString(LispObject s)
    {
//        out=out+((MyString)s).val;
    	out.append(((MyString)s).val);
    }
    public String print(LispObject s)
    {
        out=new StringBuffer(""); printS(s); return out.toString();
    }
    public void pCh(char c)
    {
        out.append(c);
    }
    public void printList(LispObject s)
    {
        pCh('(');
        while(!lisp.atom(s)){
            printS(((ListCell)s).a);
            s=((ListCell)s).d;
            pCh(' ');
        }
        if(!lisp.Null(s)){
            pCh('.');
            printAtom(s);
        }
        pCh(')'); pCh('\n');
    }
    public void printNumber(LispObject s)
    {
//        if(s.getClass().getName().equals("MyInt")){
    	if(s.isKind("myint")){
            printInt(s); return;
        }
//        if(s.getClass().getName().equals("MyDouble")){
    	if(s.isKind("mydouble")){
            printDouble(s); return;
        }
//        if(s.getClass().getName().equals("MyString")){
    	if(s.isKind("mystring")){
            printString(s); return;
        }
        out.append(((MyNumber)s).val);
    }
    public void printInt(LispObject s)
    {
        out.append(((MyInt)s).val);
    }
    public void printDouble(LispObject s)
    {
        out.append(((MyDouble)s).val);
    }
    public void printSymbol(LispObject s)
    {
        out.append(((Symbol)(lisp.symbolTable.get(
             new Integer(((Symbol)s).hc)))).name);
    }
    public void printAtom(LispObject s)
    {
        if(lisp.numberp(s)) printNumber(s);
        else
        printSymbol(s);
    }
    public ALisp lisp;
    public StringBuffer out;
    public void printS(LispObject s)
    {
        if(lisp.atom(s)) printAtom(s);
        else printList(s);
    }
    public Hashtable symbolTable;
    public PrintS(ALisp lsp)
    {
    	out=new StringBuffer("");
        lisp=lsp;
    }
}


