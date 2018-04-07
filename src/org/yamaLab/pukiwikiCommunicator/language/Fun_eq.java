package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_eq implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_eq(ALisp l)
    {
        lisp=l;
    }
    public final LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.eq2(lisp.car(argl),lisp.second(argl));
    }
}

