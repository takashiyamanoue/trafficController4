package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_m_bnot implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_bnot(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                return x.bnot();
    }
}
