package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_m_exp2 implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_exp2(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                MyNumber y=(MyNumber)(lisp.second(argl));
                return x.exp(y);

    }
}

