package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_m_lt implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_lt(ALisp l)
    {
        lisp=l;
    }
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                MyNumber y=(MyNumber)(lisp.second(argl));
                if(x.lt(y)) return lisp.tSymbol;
                else     return lisp.nilSymbol;

    }
}

