package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_equal implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_equal(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
        return lisp.equal2(lisp.car(argl),lisp.second(argl));
    }
}


