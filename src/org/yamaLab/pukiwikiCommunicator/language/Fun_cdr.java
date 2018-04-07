package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_cdr implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_cdr(ALisp l)
    {
        lisp=l;
    }
    public final LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.cdr(lisp.car(argl));
    }
}

