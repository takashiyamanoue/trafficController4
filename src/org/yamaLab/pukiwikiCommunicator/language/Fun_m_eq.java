package org.yamaLab.pukiwikiCommunicator.language;
public class Fun_m_eq implements PrimitiveFunction
{
    public ALisp lisp;
    public Fun_m_eq(ALisp l)
    {
        lisp=l;
    
		//{{INIT_CONTROLS
		//}}
	}
    public LispObject fun(LispObject proc, LispObject argl)
    {
    	 LispObject ox=lisp.car(argl);
    	 LispObject oy=lisp.second(argl);
    	 if(ox.equals(oy)) return lisp.tSymbol;
    	 if(ox==lisp.nilSymbol) return lisp.nilSymbol;
    	 if(oy==lisp.nilSymbol) return lisp.nilSymbol;
    	 if(ox==null) return lisp.nilSymbol;
    	 if(oy==null) return lisp.nilSymbol;
         MyNumber x=(MyNumber)(ox);
         MyNumber y=(MyNumber)(oy);
         if(x.eq(y)) return lisp.tSymbol;
         else     return lisp.nilSymbol;

    }
	//{{DECLARE_CONTROLS
	//}}
}

