package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_append implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_append(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.append(lisp.car(argl),lisp.second(argl));
    }
}

