package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_cons implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_cons(ALisp l)
    {
        lisp=l;
    }
    public final LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.cons(lisp.car(argl),lisp.second(argl));
    }
}

