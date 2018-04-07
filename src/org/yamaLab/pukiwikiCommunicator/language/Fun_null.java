package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_null implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_null(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.Null2(lisp.car(argl));
    }
}

