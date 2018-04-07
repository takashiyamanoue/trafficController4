package org.yamaLab.pukiwikiCommunicator.language;

public class Fun_m_band implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_band(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                MyNumber y;
                LispObject p=lisp.cdr(argl);
                while(!lisp.Null(p)){
                   y=(MyNumber)(lisp.car(p));
                   p=lisp.cdr(p);
                   x=x.band(y);
                }
                return x;
    }
}
