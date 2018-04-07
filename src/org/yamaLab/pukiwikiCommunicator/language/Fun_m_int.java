package org.yamaLab.pukiwikiCommunicator.language;

public class Fun_m_int implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_int(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                int xx=x.getInt();
                return new MyInt(xx);
    }
}

