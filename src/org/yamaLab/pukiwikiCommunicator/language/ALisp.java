package org.yamaLab.pukiwikiCommunicator.language;

/*
An interpreter of Basic like programming language.

Lisp interpreter. of the interpreter.

  http://www.tobata.isc.kyutech.ac.jp/~yamanoue/researches/java/Basic/

  by T. Yamanoue, May.1999
  yamanoue@isc.kyutech.ac.jp
  http://www.tobata.isc.kyutech.ac.jp/~yamanoue

*/
import java.util.*;

//import android.os.Message;
//import android.widget.EditText;
public class ALisp extends java.lang.Object implements Runnable
{
    public Symbol sym_m_neg;
    public Symbol sym_setq;
    public Symbol sym_int;
    public void initFunctionDispatcher()
    {
         functionDispatcher=new Hashtable();

         Fun_car fcar=new Fun_car(this);
         functionDispatcher.put(sym_car,fcar);
         functionDispatcher.put(sym_first,fcar);
         Fun_cdr fcdr=new Fun_cdr(this);
         functionDispatcher.put(sym_cdr,fcdr);
         functionDispatcher.put(sym_rest,fcdr);
         functionDispatcher.put(sym_cons,new Fun_cons(this));
         functionDispatcher.put(sym_atom,new Fun_atom(this));
         functionDispatcher.put(sym_null,new Fun_null(this));
         functionDispatcher.put(sym_eq,  new Fun_eq(this));
         functionDispatcher.put(sym_equal,new Fun_equal(this));
         functionDispatcher.put(sym_append,new Fun_append(this));
         functionDispatcher.put(sym_reverse,new Fun_reverse(this));
         functionDispatcher.put(sym_list,  new Fun_list(this));

         functionDispatcher.put(sym_m_add, new Fun_m_add(this));
         functionDispatcher.put(sym_m_sub, new Fun_m_sub(this));
         functionDispatcher.put(sym_m_mul, new Fun_m_mul(this));
         functionDispatcher.put(sym_m_div, new Fun_m_div(this));
         functionDispatcher.put(sym_m_exp2, new Fun_m_exp2(this));
         functionDispatcher.put(sym_m_eq,  new Fun_m_eq(this));
         functionDispatcher.put(sym_m_gt,  new Fun_m_gt(this));
         functionDispatcher.put(sym_m_lt,  new Fun_m_lt(this));
         functionDispatcher.put(sym_m_ge,  new Fun_m_ge(this));
         functionDispatcher.put(sym_m_le,  new Fun_m_le(this));
         functionDispatcher.put(sym_m_ne,  new Fun_m_ne(this));
         functionDispatcher.put(sym_m_sin, new Fun_m_sin(this));
         functionDispatcher.put(sym_m_cos, new Fun_m_cos(this));
         functionDispatcher.put(sym_m_tan, new Fun_m_tan(this));
         functionDispatcher.put(sym_m_atan,new Fun_m_atan(this));
         functionDispatcher.put(sym_m_sqrt,new Fun_m_sqrt(this));
         functionDispatcher.put(sym_m_log, new Fun_m_log(this));
         functionDispatcher.put(sym_m_exp, new Fun_m_exp(this));
         functionDispatcher.put(sym_m_mod, new Fun_m_mod(this));
         functionDispatcher.put(sym_m_neg, new Fun_m_neg(this));
         functionDispatcher.put(sym_m_int, new Fun_m_int(this));
         
         functionDispatcher.put(sym_m_band, new Fun_m_band(this));
         functionDispatcher.put(sym_m_bor, new Fun_m_bor(this));
         functionDispatcher.put(sym_m_bxor, new Fun_m_bxor(this));
         functionDispatcher.put(sym_m_bnot, new Fun_m_bnot(this));
         
    }
    public Hashtable functionDispatcher;
    public void initSymbols()
    {
        sym_append=recSymbol("append");
        sym_atom=  recSymbol("atom");
        sym_car   =recSymbol("car");
        sym_cdr    =recSymbol("cdr");
        sym_cons   =recSymbol("cons");
        sym_eq     =recSymbol("eq");
        sym_equal  =recSymbol("equal");
        sym_first  =recSymbol("first");
        sym_list   =recSymbol("list");
        sym_setq   =recSymbol("setq");
        sym_m_add  =recSymbol("+");
        sym_m_band  =recSymbol("&");
        sym_m_bor  =recSymbol("|");
        sym_m_bxor  =recSymbol("bxor");
        sym_m_bnot  =recSymbol("!");        
        sym_m_atan =recSymbol("atan");
        sym_m_cos  =recSymbol("cos");
        sym_m_div  =recSymbol("/");
        sym_m_eq   =recSymbol("=");
        sym_m_exp  =recSymbol("exp");
        sym_m_exp2 =recSymbol("exp2");
        sym_m_ge   =recSymbol(">=");
        sym_m_gt   =recSymbol(">");
        sym_m_lambda=recSymbol("lambda");
        sym_m_le   =recSymbol("<=");
        sym_m_log  =recSymbol("log");
        sym_m_lt   =recSymbol("<");
        sym_m_mod  =recSymbol("mod");
        sym_m_mul  =recSymbol("*");
        sym_m_ne   =recSymbol("<>");
        sym_m_sin  =recSymbol("sin");
        sym_m_sqrt =recSymbol("sqrt");
        sym_m_sub  =recSymbol("-");
        sym_m_tan  =recSymbol("tan");
        sym_m_neg  =recSymbol("neg");
        sym_null   =recSymbol("null");
        sym_print  =recSymbol("print");
        sym_read   =recSymbol("read");
        sym_rest   =recSymbol("rest");
        sym_reverse=recSymbol("reverse");
        sym_m_int  =recSymbol("int");
    }
    public Symbol sym_m_lambda;
    public Symbol sym_m_mod;
    public Symbol sym_m_log;
    public Symbol sym_m_sqrt;
    public Symbol sym_m_exp;
    public Symbol sym_m_atan;
    public Symbol sym_m_tan;
    public Symbol sym_m_cos;
    public Symbol sym_m_sin;
    public Symbol sym_m_ne;
    public Symbol sym_m_ge;
    public Symbol sym_m_le;
    public Symbol sym_m_lt;
    public Symbol sym_m_gt;
    public Symbol sym_m_eq;
    public Symbol sym_m_exp2;
    public Symbol sym_m_div;
    public Symbol sym_m_mul;
    public Symbol sym_m_sub;
    public Symbol sym_m_add;
    public Symbol sym_m_band;
    public Symbol sym_m_bor;
    public Symbol sym_m_bxor;
    public Symbol sym_m_bnot;
    public Symbol sym_m_int;
    public Symbol sym_list;
    public Symbol sym_reverse;
    public Symbol sym_append;
    public Symbol sym_equal;
    public Symbol sym_eq;
    public Symbol sym_null;
    public Symbol sym_atom;
    public Symbol sym_cons;
    public Symbol sym_rest;
    public Symbol sym_cdr;
    public Symbol sym_first;
    public Symbol sym_car;
    public Symbol sym_read;
    public Symbol sym_print;
    public LispObject fifth(LispObject x)
    {
        return car(cdr(cdr(cdr(cdr(x)))));
    }
    public void clearEnvironment()
    {
         environment=cons(nilSymbol,nilSymbol);
   }
    public LispObject applyUserDefined(
                            LispObject proc,
                            LispObject argl,
                            LispObject env)
    {
               LispObject f;
               f=get(proc, recSymbol("lambda"));
               if(Null(f)) {
                // if proc is not a function, ...
                       f=assoc(proc,((ListCell)env).a);
                       if(Null(f)) {
                        // if proc is not associated with any name
                           plist("can not find out ",proc);
                           return nilSymbol;
                       }
                       else f=second(f);
               }
               return apply( f,argl,env);
   }

    public LispObject defExt(LispObject s, LispObject env)
    {
        return nilSymbol;
    }
    public LispObject applyMiscOperation(LispObject proc,
                                  LispObject argl, LispObject env)
    {
        return null;
    }
    public LispObject progn(LispObject proc, LispObject env)
    {
        LispObject rtn=nilSymbol;
//        LispObject thisEnv=cons(env,nilSymol);
        ((ListCell)env).d=cons(nilSymbol,nilSymbol);
        LispObject ps=proc;
        while(!Null(ps)){
            rtn=eval(car(ps),env);
            if(eq(second((ListCell)env),tSymbol)) return rtn;
            ps=cdr(ps);
        }
        return rtn;
    }
    public LispObject nconc(LispObject x, LispObject y)
    {
        LispObject w=x;
        if(w==null) return nilSymbol;
        if(Null(w)) return y;
        while(!atom(((ListCell)w).d)){
            w=((ListCell)w).d;
        }
        rplcd(w,y);
        return x;
    }
    public LispObject evalMiscForm(LispObject form, LispObject env)
    {
             LispObject fform=car(form);
             return null;
    }
    public LispObject applyMiscOperation(LispObject proc, LispObject argl)
    {
        return null;
    }
    public LispObject applyNumericalOperation(LispObject proc, LispObject argl)
    {
           return null;
    }
    public LispObject applyListOperation(LispObject proc,
                                         LispObject argl)
    {
            PrimitiveFunction f=(PrimitiveFunction)(functionDispatcher.get(proc));
            if(f!=null) return f.fun(proc,argl);
            if(eq(proc,sym_equal))
                return equal2(car(argl),second(argl));
/*
   The following append and reverse is
   added by Tomoki Katayama, Kyushu Institute of Technology,
   27 Feb.1998
*/
            if(eq(proc,sym_append))
                return append(car(argl),second(argl));
            if(eq(proc,sym_reverse))
                return reverse(car(argl));
//
            if(eq(proc,sym_list))
                return argl;

            return null;

    }
    
    public LispObject equal2(LispObject x, LispObject y)
    {
        if(equal(x,y)) return tSymbol;
        else        return nilSymbol;
    }
    public void stop()
    {
        if(me!=null){ me.stop(); me=null;}
    }
    public void start()
    {
        if(me==null){me=new Thread(this); me.start();}
    }
    public void run()
    {
        while(me!=null){
            if(inqueue!=null){
              while(!inqueue.isEmpty()){
               LispObject s=read.read(inqueue);
               if(s!=null){
                 LispObject r=preEval(s,environment);
          //   LispObject r=eval(s,environment);
                 String o=print.print(r);
//                 printArea.append(o+"\n");
                 printMessage(o+"\n");
               }
//               printArea.repaint();
             }
            }
            try{ me.sleep(100);}
            catch(InterruptedException e){System.out.println(e);}
        }
//        stop();
    }
    public void rplcd(LispObject x, LispObject y)
    {
        if(atom(x)) { 
        	//printArea.append("rplcd failed\n");
        	printMessage("rplcd failed\n");
        	return;
        	}
        ((ListCell)x).d=y;
        return;

   }
    public void rplca(LispObject x, LispObject y)
    {
        if(atom(x)) { 
//        	printArea.append("rplca failed\n"); 
        	
        	return;
        }
        ((ListCell)x).a=y;
        return;

    }
    public LispObject setf(LispObject form, LispObject val)
    {
        return setf(form,val,environment);
    }
    public LispObject setf(LispObject form, LispObject val, LispObject env)
    {
        ((ListCell)env).a=setfx(form,val,((ListCell)env).a);
        return form;
    }
/*
   The following "append" and "reverse" is
   added by Tomoki Katayama, Kyushu Institute of Technology,
   27 Feb.1998
*/
    public LispObject append(LispObject x,LispObject y){
        if (Null(x))
                return y;
        else{
                return cons(car(x), append(cdr(x),y));
        }
    }
    public LispObject reverse(LispObject x)
        {
        if(Null(x)){
                return nilSymbol;
                }
        else if(Null(cdr(x))){
                return cons(car(x),nilSymbol);
        }
        else {
                return append(reverse(cdr(x)),cons(car(x),nilSymbol));
        }
    }

    public InterpreterInterface gui;
    public LispObject atom2(LispObject s)
    {
        if(atom(s)) return tSymbol;
        else        return nilSymbol;
    }
    public LispObject evalcond(LispObject cond, LispObject env)
    {
        while(true){
            if(Null(cond)) return nilSymbol;
            LispObject pair=car(cond);
            LispObject px=eval(car(pair),env);
           if(!Null(px)) return eval(second(pair),env);
           cond=cdr(cond);
        }
    }
    public boolean isDefun(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("defun"))) return true;
        return false;
    }

    public LispObject caseOfDefun(LispObject f, LispObject env)
    {

        LispObject fn=cons(recSymbol("get"),
                      cons(
                        cons(recSymbol("quote"),
                        cons(car(f),nilSymbol)),
                      cons(
                        cons(recSymbol("quote"),
                        cons(recSymbol("lambda"),nilSymbol)),
                      nilSymbol)));
        LispObject val=cons(recSymbol("lambda"),
                       cdr(f));

        LispObject x=setf(fn,val);
        return environment;
    }
//    public JTerm jterm;
    public LispObject bindVars(LispObject vars, LispObject vals)
    {
        return bindVars(vars,vals,nilSymbol);
    }
    public LispObject bindVars(LispObject vars, LispObject vals, LispObject env)
    {
        LispObject alist=((ListCell)env).a;
        while(true){
            if(Null(vars)) return alist;
            if(Null(vals)) return alist;
            alist=cons(cons(car(vars),
                       cons(car(vals),
                            nilSymbol)),alist);
            vars=cdr(vars); vals=cdr(vals);
        }
    }
    public LispObject get(LispObject sym, LispObject attr, LispObject env)
    {
         LispObject getf=cons(recSymbol("get"),
                        cons(sym,
                        cons(attr, nilSymbol)));
         LispObject w=assoc(getf,env);
         if(Null(w)) return nilSymbol;
         else return second(w);
    }
    public LispObject get(LispObject sym, LispObject attr)
    {
        return get(sym,attr,((ListCell)environment).a);
    }
    public boolean isSetf(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("setf"))) return true;
        return false;
    }
    public synchronized LispObject preEval(LispObject s, LispObject env)
    {
        LispObject rtn;
        if(isSetf(s)) {
        	try{
                environment=caseOfSetf(cdr(s),env);
                return second(car(environment));
        	}
        	catch(Exception e){
        		gui.parseCommand("error at isSetf, println "+e.toString());
        		plist("s=",s);
        		Thread.dumpStack();        		
        		return nilSymbol;
        	}
        }
        else
        if(isDefun(s)){
        	try{
            environment=caseOfDefun(cdr(s),env);
            return second(s);
        	}
        	catch(Exception e){
        		gui.parseCommand("error at isDeFun, println "+e.toString());
        		plist("s=",s);
        		Thread.dumpStack();        		
        		return nilSymbol;        		
        	}
        }
        else
        {
        	try{
            rtn=defExt(s,env);
            if(!Null(rtn)) return rtn;
            else  return eval(s,env);
        	}
        	catch(Exception e){
        		gui.parseCommand("error at defEXt, println "+e.toString());
        		plist("s=",s);
        		Thread.dumpStack();        		
        		return nilSymbol;
        	}
        }
    }
    public LispObject caseOfSetf(LispObject form, LispObject env)
    {
        while(true){
          if(Null(form)) return env;
          else {
            env =setfx(car(form),
                       eval(second(form),env),env);
            form= cdr(cdr(form));
          }
        }
    }
    public boolean equal(LispObject x, LispObject y)
    {
        if(atom(x)) return eq(x,y);
        if(atom(y)) return false;
        if(equal(car(x),car(y)))
           return equal(cdr(x),cdr(y));
        else return false;
    }
    public LispObject setfx(LispObject form, LispObject value, LispObject env)
    {
        LispObject key=null;
        if(symbolp(form)){
//            return cons(cons(form,cons(value,nilSymbol)),env);
              key=form;
        }
        else
        if(eq(car(form),recSymbol("get"))){
            LispObject fa=eval(second(form),env);
            LispObject sa=eval(third(form),env);
            key=cons(car(form),cons(fa,cons(sa,nilSymbol)));
//            return cons(cons(key,cons(value,nilSymbol)),env);
        }
        LispObject obj=assoc(key,env);
        if(!Null(obj)) {rplca(cdr(obj),value); return env;}
        else return cons(cons(key,cons(value,nilSymbol)),env);
    }
    public LispObject eq2(LispObject x, LispObject y)
    {
    	if(x==y) return tSymbol;
    	if(x==null) return nilSymbol;
    	if(y==null) return nilSymbol;
        if(eq(x,y)) return tSymbol;
        else        return nilSymbol;
    }
    public void plist(String s, LispObject x)
    {

          String a=""+s;
          String o=print.print(x);
          if(o.equals("&")){
        	  System.out.println("break");
          }

          gui.parseCommand("println "+a+o); 
   }

    public boolean symbolp(LispObject s)
    {
//        if(s.getClass().getName().equals("ListCell"))
    	if(!s.isAtom())
             return false;
//        if(s.getClass().getName().equals("Symbol"))
    	if(s.isKind("symbol"))
             return true;
        else return false;
   }
    public LispObject Null2(LispObject s)
    {
        if(Null(s)) return tSymbol;
        else return nilSymbol;
    }
    public LispObject apply(LispObject proc,
                            LispObject argl,
                            LispObject env)
    {
        LispObject f=null;
        LispObject rtn=null;
        if(gui.isTracing()){
           plist("apply-",proc);
           plist("argl-",argl);
        }
        if(symbolp(proc)){
            // apply list operation
//            LispObject rtn=applyListOperation(proc,argl);
//            if(rtn!=null) return rtn;
              PrimitiveFunction fx=(PrimitiveFunction)(functionDispatcher.get(proc));
              if(fx!=null) return fx.fun(proc,argl);

            // apply numerical operation
//            rtn=applyNumericalOperation(proc,argl);
//            if(rtn!=null) return rtn;

            // apply misc operation
            rtn=applyMiscOperation(proc,argl);
            if(rtn!=null) return rtn;

            if(eq(proc,recSymbol("print"))){
                LispObject p=argl;
                while(!Null(p)){
                    String o=print.print(car(p));
//                    printArea.append(o+"\n");
//                    Message m=new Message();
//                    m.obj=o+"\n";
                    gui.parseCommand("println "+o);
//                    printArea.repaint();
                    p=cdr(p);
                }
                return car(argl);
            }
            if(eq(proc,recSymbol("read"))){
                return read.read(inqueue);
            }
            else{
     //
     //       if proc is user defined name, ...
     //
               return applyUserDefined(proc,argl,env);
           }
        }
        else{
            LispObject newEnv=bindVars(second(proc),
                                       argl,env);
            rtn=nilSymbol;
            LispObject ps=cdr(cdr(proc));
            rtn=progn(ps,cons(newEnv,nilSymbol));
            return rtn;
        }
    }
    public LispObject evalArgl(LispObject argl, LispObject env)
    {
        if(Null(argl)) return nilSymbol;
        LispObject y=evalArgl(cdr(argl),env);
        LispObject x=eval(car(argl),env);
        return cons(x,y);
    }
    public LispObject fourth(LispObject x)
    {
        return car(cdr(cdr(cdr(x))));
    }
    public LispObject third(LispObject x)
    {
        return car(cdr(cdr(x)));
    }
    public LispObject assoc(LispObject key, LispObject alist)
    {
        LispObject l=alist;
        while(true){
          if(Null(l)) return nilSymbol;
          if(equal(key,car(car(l)))) return car(l);
          l=cdr(l);
        }
    }
    public LispObject second(LispObject x)
    {
        return car(cdr(x));
    }
    public LispObject eval(LispObject form, LispObject env)
    {
        LispObject rtn;
        if(gui.isTracing())
                 plist("eval..",form);
        if(atom(form)){
            if(numberp(form)) rtn= form;
            else
            if(eq(tSymbol,form)) rtn= tSymbol;
            else
            if(eq(nilSymbol,form)) rtn= nilSymbol;
            else{
               LispObject w=assoc(form,((ListCell)env).a);
               if(Null(w)){
                 plist("can not find out ",form);
                 return nilSymbol;
               }
               //
               if(!atom(second(w))){
                  rtn=nilSymbol;
                  if(eq(recSymbol("dimension"),car(second(w)))){
                     rtn= form;
                  }
               }
               else
               //
               rtn= second(w);
            }
        }
        else{
            LispObject fform=car(form);
            if(eq(fform,recSymbol("quote")))
                 rtn= second(form);
             else
            if(eq(fform,recSymbol("if"))) {
               if(!Null( eval(second(form),env)))
                     rtn=  eval(third(form), env);
               else  rtn=  eval(fourth(form),env);
            }
            else
            if(eq(fform,recSymbol("cond")))
               rtn= evalcond(cdr(form),env);
            else
            if(eq(fform,recSymbol("get")))
               rtn= get(eval(second(form),env),
                          eval(third(form), env), env);
            else
            if(eq(fform,recSymbol("apply")))
               rtn= apply( eval(second(form),env),
                           eval(third(form),env),env);
            else
            if(eq(fform,recSymbol("setq"))){
               rtn= setf( second(form),
                             eval(third(form),env),env);
            }
            else
            if(eq(fform,recSymbol("progn"))){
                rtn= progn(cdr(form),env);
            }
            else
            if(eq(fform,recSymbol("return"))){
                rtn=eval(second(form),env);
                ((ListCell)(((ListCell)env).d)).a=tSymbol;
            }
            else{
                rtn=evalMiscForm(form,env);
                if(rtn!=null) return rtn;
                else rtn= apply(fform,
                            evalArgl(cdr(form),env),env);
            }
        }
        if(gui.isTracing())
           plist("eval return ...",rtn);
        return rtn;

    }
    public void evals(CQueue iq)
    {
        inqueue=iq;
   }
//    public EditText printArea;
    public String readArea;
    public void init(String rarea, CQueue iq,InterpreterInterface g)
    {
         me=null;
         inqueue=iq;
        symbolTable=new Hashtable();
        nilSymbol  = recSymbol("nil");
        environment=cons(nilSymbol,nilSymbol);
        tSymbol    = recSymbol("true");
        initSymbols();
        initFunctionDispatcher();
    //    inqueue=iq;
    //    outqueue=oq;
        readArea=rarea;
        read=new ReadS(inqueue,this);
        print=new PrintS(this);
        gui=g;
    }
    public ALisp()
    {
    }
    public boolean eq(LispObject x, LispObject y)
    {
        if(x==y) return true;
        if(!atom(x)) return false;
        if(!atom(y)) return false;
        if(numberp(x)){
            if(numberp(y))
               return ((MyNumber)x).eq((MyNumber)y);}
        if(numberp(y)) return false;
        return((Symbol)x).hc == ((Symbol)y).hc;
    }
    public Thread me;
    public Symbol tSymbol;
    public Symbol nilSymbol;
    public PrintS print;
    public ReadS read;
    public CQueue inqueue;
    public ALisp(String in, CQueue iq, InterpreterInterface g)
    {
        init(in,iq,g);
    }
    public boolean numberp(LispObject s)
    {
//        if(s.getClass().getName().equals("ListCell"))
    	if(!s.isAtom())
             return false;
//        if(s.getClass().getName().equals("MyNumber"))
    	if(s.isKind("mynumber"))
             return true;
//        if(s.getClass().getName().equals("MyInt"))
    	if(s.isKind("myint"))
            return true;
//        if(s.getClass().getName().equals("MyDouble"))
    	if(s.isKind("mydouble"))
             return true;
//        if(s.getClass().getName().equals("MyString"))
    	if(s.isKind("mystring"))
             return true;
        else return false;
    }
    public LispObject cons(LispObject x, LispObject y)
    {
        ListCell z=new ListCell();
        z.a=x; z.d=y;
        return z;
    }
    public boolean Null(LispObject s)
    {
        if(!atom(s)) return false;
        if(s==nilSymbol) return true;
        if(eq(nilSymbol,s)) return true;
        return false;
    }
    public boolean atom(LispObject s)
    {
    	/*
        if(s.getClass().getName().equals("ListCell"))
           return false;
        else return true;
        */
    	return s.isAtom();
    }
    public LispObject cdr(LispObject s)
    {
//        if(s.getClass().getName().equals("ListCell"))
    	if(!s.isAtom())
            return ((ListCell)s).d;
        else{  System.out.println("error");}
        return null;
    }
    public LispObject car(LispObject s)
    {
//        if(s.getClass().getName().equals("ListCell"))
    	if(!s.isAtom())
            return ((ListCell)s).a;
        else{  System.out.println("error");}
        return null;
   }
    public Symbol recSymbol(String s)
    {
        int hc=s.hashCode();
        Integer key=new Integer(hc);
        Symbol rtn=(Symbol)(symbolTable.get(key));
        if(rtn==null){
            rtn=new Symbol(s); symbolTable.put(key,rtn);
        }
        return rtn;
    }
    public LispObject environment;
    public Hashtable symbolTable;
    public void output(String x){
        gui.parseCommand("println "+x);
    }
    public void printMessage(String x){
        gui.parseCommand("guiMessage "+x);
    }
    
}





