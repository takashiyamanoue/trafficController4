package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_m_sub implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_sub(ALisp l)
    {
        lisp=l;
    
		//{{INIT_CONTROLS
		//}}
	}
    public LispObject fun(LispObject proc, LispObject argl)
    {
                MyNumber x=(MyNumber)(lisp.car(argl));
                LispObject p=lisp.cdr(argl);
                if(lisp.Null(p)) return (new MyNumber(0)).sub(x);
                MyNumber y=(MyNumber)(lisp.car(p));
                return x.sub(y);
    }
	//{{DECLARE_CONTROLS
	//}}
}

