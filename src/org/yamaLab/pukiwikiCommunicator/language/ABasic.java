package org.yamaLab.pukiwikiCommunicator.language;

/*
An interpreter of Basic like programming language.

  http://www.tobata.isc.kyutech.ac.jp/~yamanoue/researches/java/Basic/

  by T. Yamanoue, May.1999
  yamanoue@isc.kyutech.ac.jp
  http://www.tobata.isc.kyutech.ac.jp/~yamanoue

*/
import java.awt.*;
import java.util.*;
//import android.widget.*;
public class ABasic extends ALisp
{
    public void clearEnvironment()
    {
        super.clearEnvironment();
        arrays.init();
    }
    public LispObject printLine(LispObject x)
    {
        LispObject s=x;
        String o;
        while(!atom(s)){
            o=print.print(car(s));
//            printArea.append(o);
            output(o);
            s=((ListCell)s).d;
            if(!atom(s)) //printArea.append(",");
            	output(",");
        }
        if(!Null(s)){
//            printArea.append(".");
        	output(".");
            o=print.print(s);
//            printArea.append(o);
            output(o);
        }
//        printArea.append("\n");
        output("\n");
//        printArea.repaint();
        return x;
    }
    public LispObject evalFor(LispObject form, LispObject env)
    {
        LispObject rtn;

        LispObject variable;
        LispObject initial;
        LispObject end;
        LispObject step;
        LispObject slist;
        MyNumber vx;
        variable=second(form);
        initial =third(form);
        end     =fourth(form);
        step    =car(cdr(cdr(cdr(cdr(form)))));
        slist   =car(cdr(cdr(cdr(cdr(cdr(form))))));

        // set initial value
        rtn=setf(variable,eval(initial,env),env);

        LispObject stepx=eval(step,env);
        if(!numberp(stepx)) return nilSymbol;

        LispObject endx=eval(end,env);
        if(!numberp(endx)) return nilSymbol;

        if(((MyNumber)stepx).gt(new MyInt(0))){
            while(true){
              vx=((MyNumber)(eval(variable,env)));
              if(vx.gt((MyNumber)endx)) return tSymbol;
              rtn=eval(slist,env);
              vx=((MyNumber)(eval(variable,env)));
              rtn=setf(variable,vx.add((MyNumber)stepx));
            }
        }
        else{
            while(true){
              vx=((MyNumber)(eval(variable,env)));
              if(vx.lt((MyNumber)endx)) return tSymbol;
              rtn=eval(slist,env);
              vx=((MyNumber)(eval(variable,env)));
              rtn=setf(variable,vx.add((MyNumber)stepx));
            }
        }

    }
    public LispObject applyUserDefined(LispObject proc,
                            LispObject argl,
                            LispObject env)
    {
               LispObject f;
               f=get(proc, recSymbol("lambda"));
               if(Null(f)) {
                   // if proc is not a function, ...
                       f=assoc(proc,((ListCell)env).a);
                       if(Null(f)) {
                        // if proc is not associated with any name ...
                           plist("can not find out ",proc);
                           return nilSymbol;
                       }
                       else if(eq(recSymbol("dimension"),
                                   car(second(f))))
                           {
                                // if proc is dimension name, ...
                                LispObject getdimarg=
                                     cons(proc,
                                     cons(argl,nilSymbol));
                              return apply(recSymbol("aget"),getdimarg,env);
                           }
                       else f=second(f);
//                    }
               }
               return apply( f,argl,env);
    }
    public LispObject caseOfDefDim(LispObject f, LispObject env)
    {
        LispObject x=setf(car(f),cons(recSymbol("dimension"),
                                 cons(car(f),nilSymbol)));
        return environment;
    }
     public boolean isDefDim(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("defdim"))) return true;
        return false;
    }
    public LispObject defExt(LispObject s, LispObject env)
    {
        if(isDefDim(s)){
            environment=caseOfDefDim(cdr(s),env);
            return second(s);
        }
        return nilSymbol;
    }
    public synchronized void evalList(LispObject x)
    {
		LispObject s=null;
		LispObject r=null;    	
    	try{
            while(!Null(x)){
               s=car(x);
               r=preEval(s,environment); //eval the S expression
               x=cdr(x);
            }
//        printArea.append("OK\n");
            printMessage("OK\n");
//        printArea.requestFocus();
    	}
    	catch(Exception e){
    		gui.parseCommand("println "+e.toString());
    		plist("s=",s);
    		Thread.dumpStack();
    	}
    }
    public BasicParser basicparser;
    public void parseCommands(LispObject x)
    {
        String str;
//        str=print.print(x);
//        printArea.appendText(str);
        if(x==null) return;
        if(Null(x)) return;
        if(numberp(car(x))){
            if(Null(cdr(x))) sourceManager.deleteLine(car(x));
            else sourceManager.addLine(x);
            return;
        }
        if(eq(car(x),recSymbol("list"))||
           eq(car(x),recSymbol("LIST"))){
              str=sourceManager.printTheSource();
//              printArea.append(str);
              printMessage(str);
              return;
        }
        if(eq(car(x),recSymbol("run"))||
           eq(car(x),recSymbol("RUN"))){
              LispObject o=sourceManager.getTheProgram();
//              str=print.print(o);
//              printArea.appendText(str);
              LispObject p=basicparser.parseBasic(o);
              str=print.print(p);
//              printArea.append(str);
//              printMessage(str);
              evalList(p);
//              printArea.repaint();
              return;
        }
        else {
//            LispObject t=parser.parseLine(x);
//            LispObject r=preEval(t,environment); //eval the S expression
            LispObject t=basicparser.parseBasic(x);
//            str=print.print(t);
//            printArea.appendText(str);
            evalList(t);
//            printArea.repaint();
        }
    }
    public SourceManager sourceManager;
    public LispObject sourceProgram;
    public void comment()
    {
        /*
        ABasic class

        commands:
            '?'<expression>
           | 'print' <expression>
           | 'PRINT' <expression>    ... print

            <var>'='<expression>
           |<var>'('<expressionlist>')''='<expression> ... assign

            <linenumber> <statement> ... store program
            <linenumber>             ... delete the statement

            <def>   ... define the function

            'list'  ... listing the program
            'run'   ... compiling and run the program


        no goto statement.
        ignore line numbers while compiling and running.

        <def>::= 'def' <fun>'('<argl>')'=<block>

        this is translated into

             (defun <fun> (<argl>) <block>)

        <dim>::= 'dim' <name>['('<numlist>')']

        this is translated into

             (defdim <name>)

        <if>::= 'if' <loe> 'then' <block> 'else' <block> 'endif'

        this is translated into

              (if <loe> <block> <block>)


        <if>::= 'if' <loe> 'then' <block> 'endif'

        this is translated into

              (if <loe> <blocl> t)


        <for>::= 'for' <var>'='<expression> 'to' <expression>
                          <block>
                 'next' <var>

        this is translated into

              (for  <var> <expression> <expression> 1 <block>)

        <for>::= 'for' <var>'='<expression> 'to' <expression> 'step'
                                  <expression>
                          <block>
                  'next' <var>

        this is translated into

              (for <var> <expression> <expression> <expression>
                          <block>)

        <block>::=<statement>|<statementList>
        <statementList>::='['<statement>{(';'|<lf>)<statement>}']'

        this is translated into

              (progn <statement> ... <statement>)

        <statement>=<if>|<for>|<print>|<return>|<assign>

        <return>::='return' <expression>

        this is translated into
              (return <expression>)

        <return>::='return'

        this is translated into
              (return t)

        <gosub>::='gosub' <fname>'('<expressionlist>')'

        this is translated into
              (<fname> <expressionlist>)

        sourceprogram=
        (
         (<lineNumber0> <list of tokens>)
         (<lineNumber1> <list of tokens>)
            ...
         (<lineNumbern-1> <list of tokens>)
        )

        programInS=

        environment (in super)
        = association list

        */
    }
    public LispObject evalMiscForm(LispObject form, LispObject env)
    {
            LispObject fform=car(form);
            if(eq(fform,recSymbol("for"))){
                return evalFor(form,env);
            }
            return null;
    }
    public LispObject applyMiscOperation(LispObject proc,LispObject argl)
    {

        /*
          array access operations

          assign a <value> to the element of the <array>, indexed by <index>:
          (aput <array> <index> <value>)
          <array>: symbol
          <index>: (i1 i2 ... in)
          <value>: LispObject

        */

            if(eq(proc,recSymbol("aput"))){
                String aname=print.print(car(argl));
                String index=print.print(second(argl));
                LispObject val=third(argl);
                arrays.put(aname,index,val);
                return val;
            }
        /*
          array access operations

          get a <value> from the element of the <array>, indexed by <index>:
          (aget <array> <index>)
          <array>: symbol
          <index>: (i1 i2 ... in)

        */
             if(eq(proc,recSymbol("aget"))){
            	 try{
                    String aname=print.print(car(argl));
                    String index=print.print(second(argl));
                    LispObject val= arrays.get(aname,index);
                    if(val==null) return nilSymbol;
                    return val;
            	 }
            	 catch(Exception e){
            		 plist("aget error...argl=", argl);
            		return nilSymbol; 
            	 }
            }

        /*
        */
             if(eq(proc,recSymbol("printl"))){
                return printLine(argl);
             }
             if(eq(proc,recSymbol("line"))){
                LispObject x=cons(proc,argl);
//                gui.graphicArea.add(x);
//                gui.graphicArea.repaint();
                return x;

             }
             if(eq(proc,recSymbol("lineto"))){
                LispObject x=cons(proc,argl);
//                gui.graphicArea.add(x);
//                gui.graphicsArea.repaint();
                return x;

             }
              if(eq(proc,recSymbol("pset"))){
                LispObject x=cons(proc,argl);
//                gui.graphicArea.add(x);
//                gui.graphicsArea.repaint();
                return x;

             }
      return null;
    }
    public ArrayManager arrays;
    public String reservedWords[]={"print", "PRINT",
                                    "list",  "LIST",
                                    "run",   "RUN",
                                    "if",    "IF",
                                    "then",  "THEN",
                                    "else",  "ELSE",
                                    "for",   "FOR",
                                    "to",    "TO",
                                    "step",  "STEP",
                                    "next",  "NEXT",
                                    "def",   "DEF",
                                    "return","RETURN",
                                    "rtn",   "RTN",
                                    "gosub", "GOSUB",
                                    "input", "INPUT",
                                    "dim",   "DIM",
                                    "line",  "LINE",
                                    "pset",  "PSET",
                                    "circle","CIRCLE",
                                    "true",  "TRUE",
                                    "nil",   "NIL",
                                    "pset",  "PSET"};
//    public Parser parser;
    public String breakSymbols[]=
          {" ", "+", "-", "*", "/", "=", "(", ")",
           ",", ".", ":", "?", "^", ";", "$", "#",
           "%", "&", "\"","<", ">", "'", "!", "@",
           "|", "[", "]", "{", "}"};
    public ABasic()
    {
    }
    public ABasic(String in, CQueue iq,InterpreterInterface g)
    {
        init(in,iq,g);
    }


    public void init(String rarea, CQueue iq, InterpreterInterface g)
    {
        super.init(rarea,iq,g);

        read=(ReadS)(new ReadLine(inqueue,this));
        ((ReadLine)read).setBreak(breakSymbols,29);
        ((ReadLine)read).setReserve(reservedWords,44);
        print=new PrintS(this);
        gui=g;
//        parser=new Parser(this,gui);
//        parser.setReserveSymbols(reserveSymbols,2);
        arrays=new ArrayManager();
        sourceManager=new SourceManager(this);
        basicparser=new BasicParser(this,gui);
    }
    public void run()
    {
        String o;
        while(me!=null){

            if(inqueue!=null){
//                printArea.appendText("OK\n");
              while(!inqueue.isEmpty()){
               LispObject s=((ReadLine)read).readLine(inqueue); //input the line
               if(s!=null){
                   parseCommands(s);
               }

//               printArea.repaint();

             }

            }

            try{ me.sleep(50);}
            catch(InterruptedException e){System.out.println(e);}
        }

    }
}


