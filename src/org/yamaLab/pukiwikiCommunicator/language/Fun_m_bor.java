package org.yamaLab.pukiwikiCommunicator.language;

public class Fun_m_bor implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_bor(ALisp l)
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
                   x=x.bor(y);
                }
                return x;
    }
}