package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_list implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_list(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
        return argl;
    }
}

