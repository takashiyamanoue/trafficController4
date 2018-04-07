package org.yamaLab.pukiwikiCommunicator.language;
public class SourceManager extends java.lang.Object
{
    String printTheLine(LispObject l)
    {
        String x="";
        while(!lisp.Null(l)){
            if(lisp.atom(lisp.car(l)))
               x=x+lisp.print.print(lisp.car(l));
            else
               x=x+lisp.print.print(lisp.car(lisp.cdr(lisp.car(l))));
            l=((ListCell)l).d;
        }
        x=x+"\n";
        return x;
    }
    public String printTheSource()
    {
        String x="";
        LispObject lp=sourceProgram;
        while(!lisp.Null(lp)){
            LispObject theline=lisp.car(lp);
            x=x+printTheLine(theline);
            lp=((ListCell)lp).d;
        }
        return x;
    }
    public ALisp lisp;
    public LispObject getTheProgram()
    {
        ListCell x=new ListCell();
        ListCell y;
        LispObject lp; // line pointer
        LispObject cp; // column pointer
        if(lisp.Null(sourceProgram)) return lisp.nilSymbol;
        y=x;
        lp=sourceProgram;
        while(!lisp.Null(lp)){
            cp=lisp.cdr(lisp.car(lp));
            while(!lisp.Null(cp)){
                y.a=((ListCell)cp).a;
                cp=((ListCell)cp).d;
                ListCell z=new ListCell();
                z.d=lisp.nilSymbol;
                y.d=z;
                y=z;
            }
            lp=((ListCell)lp).d;
        }
        y.a=lisp.recSymbol("end of the program");
        return x;
    }
    public LispObject pointer;
    public void renumber()
    {
    }
    public void searchLine(LispObject n)
    {
    }
    public void deleteLine(LispObject n)
    {
        LispObject lp1;
        LispObject lp2;
        MyInt l=(MyInt)(n);
        lp1=sourceProgram;
        lp2=lp1;
        if(lisp.Null(lp1)) return;
        MyInt no=(MyInt)(lisp.car(lisp.car(lp1)));
        if(l.eq(no)){
            sourceProgram=((ListCell)lp1).d;
            return;
        }
        lp2=((ListCell)lp1).d;
        while(!lisp.Null(lp2)){
            no=(MyInt)(lisp.car(lisp.car(lp2)));
            if(l.eq(no)){
                ((ListCell)lp1).d=((ListCell)lp2).d;
                return;
            }
            if(l.lt(no)) return;
            lp2=((ListCell)lp2).d;
            lp1=((ListCell)lp1).d;
        }
    }
    public void addLine(LispObject newline)
    {
        if(lisp.Null(sourceProgram)){
            ListCell x=new ListCell();
            x.d=lisp.nilSymbol;
            x.a=newline;
            sourceProgram=x;
            return;
        }
        LispObject lp=sourceProgram; // line pointer
        MyInt newnumber=(MyInt)(lisp.car(newline));
        MyInt firstnumber=(MyInt)(lisp.car(lisp.car(lp)));
        if(newnumber.lt(firstnumber)){
            ListCell y=new ListCell();
            y.d=lp;
            y.a=newline;
            sourceProgram=y;
            return;
        }
        if(newnumber.eq(firstnumber)){
            ((ListCell)sourceProgram).a=newline;
            return;
        }
        if(lisp.Null(((ListCell)lp).d)){
            ListCell y=new ListCell();
            y.d=lisp.nilSymbol;
            y.a=newline;
            ((ListCell)lp).d=y;
            return;
        }
        LispObject lp2=((ListCell)lp).d;
        MyInt secondnumber=(MyInt)(lisp.car(lisp.car(lp2)));
        while(!lisp.Null(lp2)){
            if(newnumber.eq(secondnumber)){
                ((ListCell)lp2).a=newline;
                return;
            }
            else
            if(newnumber.lt(secondnumber)){
                ListCell y=new ListCell();
                y.d=lp2;
                y.a=newline;
                ((ListCell)lp).d=y;
                return;
            }
            lp2=((ListCell)lp2).d;
            lp=((ListCell)lp).d;
        }
        ListCell y=new ListCell();
        y.d=lisp.nilSymbol;
        y.a=newline;
        ((ListCell)lp).d=y;
        return;
    }
    public LispObject sourceProgram;
    public SourceManager(ALisp l)
    {
        lisp=l;
        sourceProgram=lisp.nilSymbol;
    }
}

