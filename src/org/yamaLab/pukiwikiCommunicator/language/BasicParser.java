package org.yamaLab.pukiwikiCommunicator.language;

/*
An interpreter of Basic like programming language.

  http://www.tobata.isc.kyutech.ac.jp/~yamanoue/researches/java/Basic/

  by T. Yamanoue, May.1999
  yamanoue@isc.kyutech.ac.jp
  http://www.tobata.isc.kyutech.ac.jp/~yamanoue

*/
import java.awt.*;

//import android.widget.EditText;

public class BasicParser extends Parser
{
    public boolean parseGraphicsPset(LispObject x)
    {
       /*
           pset (x1,y1)
           pset (x1,y1),color
           color:0-7

           is translated into

           (pset x1 y1 color)


        */
        boolean isLineTo=false;
        ListCell type=new ListCell();
        LispObject w;
        boolean dmy;
        if(!(is("pset")||is("PSET"))) return false;

            ListCell x1=new ListCell(); x1.d=lisp.nilSymbol;
            ListCell y1=new ListCell(); y1.d=lisp.nilSymbol;
            ListCell color=new ListCell(); color.d=lisp.nilSymbol;

            dmy=rB();

            if(!parseCoordinate(x1,y1)) {
                isLineTo=true;
            }
            dmy=rB();

            if(is(",")){
                dmy=rB();
                if(!parseExpression(color,type)) return false;
            }
            else
            {
                color.a=new MyInt(0);
            }

                w=lisp.cons(lisp.car(color),lisp.nilSymbol);
                w=lisp.cons(lisp.car(y1),w);
                w=lisp.cons(lisp.car(x1),w);
                w=lisp.cons(lisp.recSymbol("pset"),w);
                ((ListCell)x).a=w;
                return true;

    }
    public boolean parseCoordinate(LispObject x, LispObject y)
    {
        boolean dmy;
        dmy=rB();
        ListCell type=new ListCell();
        type.d=lisp.nilSymbol;
           if(!is("(")) {
//               putErrorMessage("( is expected at a coordinate of LINE statement.");
                  return false;
            }
            if(!parseExpression(x,type)) return false;
            if(!is(",")) {
                putErrorMessage(", is expected at LINE statement.");
                return false;
            }
            if(!parseExpression(y,type)) return false;
            if(!is(")")) {
                putErrorMessage(") is expected at a coordinate of LINE statement.");
                return false;
            }
            return true;
    }
    public void putErrorMessage(String x)
    {
        message.parseCommand("println "+x);
//        message.repaint();
    }
    public boolean parseGraphicsLine(LispObject x)
    {
       /*
           line (x1,y1)-(x2,y2)
           line (x1,y1)-(x2,y2),color
           color:0-7

           is translated into

           (line x1 y1 x2 y2 color)

           line -(x2,y2)
           line -(x2,y2),color

           is translated into
           (lineto x2 y2 color)
        */
        boolean isLineTo=false;
        ListCell type=new ListCell();
        LispObject w;
        boolean dmy;
        if(!(is("line")||is("LINE"))) return false;

            ListCell x1=new ListCell(); x1.d=lisp.nilSymbol;
            ListCell y1=new ListCell(); y1.d=lisp.nilSymbol;
            ListCell x2=new ListCell(); x2.d=lisp.nilSymbol;
            ListCell y2=new ListCell(); y2.d=lisp.nilSymbol;
            ListCell color=new ListCell(); color.d=lisp.nilSymbol;

            dmy=rB();

            if(!parseCoordinate(x1,y1)) {
                isLineTo=true;
            }
            dmy=rB();
            if(!is("-")) {
                putErrorMessage("- is expected at LINE statement.");
                return false;
            }
            dmy=rB();

            if(!parseCoordinate(x2,y2)){
                putErrorMessage("Syntax error at a coordinate of LINE statement.");
                return false;
            }
            dmy=rB();
            if(is(",")){
                dmy=rB();
                if(!parseExpression(color,type)) return false;
            }
            else
            {
                color.a=new MyInt(0);
            }
            if(!isLineTo){
                w=lisp.cons(lisp.car(color),lisp.nilSymbol);
                w=lisp.cons(lisp.car(y2),w);
                w=lisp.cons(lisp.car(x2),w);
                w=lisp.cons(lisp.car(y1),w);
                w=lisp.cons(lisp.car(x1),w);
                w=lisp.cons(lisp.recSymbol("line"),w);
                ((ListCell)x).a=w;
                return true;
            }
            else
            {
                w=lisp.cons(lisp.car(color),lisp.nilSymbol);
                w=lisp.cons(lisp.car(y2),w);
                w=lisp.cons(lisp.car(x2),w);
                w=lisp.cons(lisp.recSymbol("lineto"),w);
                ((ListCell)x).a=w;
                return true;
           }

    }
    public boolean parsePrint(LispObject x)
    {
        ListCell type=new ListCell();
        if(is("?")||is("print")||is("PRINT")){
            if(!parseExpressionList(x,type)) {
                putErrorMessage("Syntax Error at statement list of PRINT statement.");
                return false;
            }
            ((ListCell)x).a=lisp.cons(lisp.recSymbol("printl"),
                                      lisp.cdr(lisp.car(x)));
            return true;
        }
        return false;
    }
    public boolean parseStatementList(LispObject slist)
    {
        boolean dmy;
        LispObject progn=lisp.nilSymbol;
        while(true){
            ListCell x=new ListCell();
            x.d=lisp.nilSymbol;
            x.a=lisp.nilSymbol;
            if(parseStatement(x)){
                String px=lisp.print.print(x);
                if(lisp.gui.isTracing()){
                    lisp.gui.parseCommand("guiMessage "+x);
                }
                progn=lisp.nconc(progn,lisp.car(x));
                dmy=rB();
            }
            else if(is(":")||is(";")){}
            else
            {
                lisp.rplca(slist,progn);
                return true;
            }
        dmy=rB();
        dmy=is(":"); dmy=is(";");
        }
    }

    public boolean parseFor(LispObject x)
    {

        /*
        for <vi>=<ei> to <ee> step <step> <slist> next <vx>

        is translated into

        (for <vi> <ei> <step> (progn <slist>))

        */

        boolean dmy;
        LispObject w;

        if(!(is("for")||is("FOR"))) return false;

        dmy=rB();
        ListCell vi=new ListCell(); vi.d=lisp.nilSymbol;
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        if(!isName(vi,type)) {
            putErrorMessage("Syntax Error at the control variable of For statement.");
            return false;
        }
        dmy=rB();

        if(!is("=")) {
            putErrorMessage("= is expected at the initial value of For statement.");
            return false;
        }

        ListCell ei=new ListCell(); ei.d=lisp.nilSymbol;
        if(!parseExpression(ei,type)) {
            putErrorMessage("Syntax Error at initial value of For statement.");
            return false;
        }

        if(!(is("to")||is("TO"))) {
            putErrorMessage("TO is expected at between initial and end value of For statement.");
            return false;
        }

        dmy=rB();
        ListCell ee=new ListCell(); ee.d=lisp.nilSymbol;

        if(!parseExpression(ee,type)) {
            putErrorMessage("Syntax Error at end value of For statement.");
            return false;
        }

        dmy=rB();

        ListCell step=new ListCell(); step.d=lisp.nilSymbol;
        lisp.rplca(step,new MyInt(1));

        if((is("step")||is("STEP"))) {

            if(!parseExpression(step,type)) {
                putErrorMessage("Syntax Error at step value of FOR statement.");
                return false;
            }

        }

        ListCell slist=new ListCell(); slist.d=lisp.nilSymbol;

        if(!parseStatementList(slist)) {
            putErrorMessage("Syntax Error at statement list between FOR and NEXT loop.");
            return false;
        }

        LispObject sl=lisp.cons(lisp.recSymbol("progn"), slist.a);
        dmy=rB();
        if(!(is("next")||is("NEXT"))) {
            putErrorMessage("NEXT is expected at the FOR loop.");
            return false;
        }

        dmy=rB();
        ListCell vx=new ListCell(); vx.d=lisp.nilSymbol;
        if(!isName(vx,type)) {
            putErrorMessage("Syntax Error at control value after NEXT of the FOR loop.");
            return false;
        }

        w=lisp.cons(sl,lisp.nilSymbol);
        w=lisp.cons(lisp.car(step),w);
        w=lisp.cons(lisp.car(ee),w);
        w=lisp.cons(lisp.car(ei),w);
        w=lisp.cons(lisp.car(vi),w);
        w=lisp.cons(lisp.recSymbol("for"),w);
        ((ListCell)x).a=w;
        return true;
    }
    public boolean parseExpression(LispObject x,LispObject type)
    {

        if( !parseIf2(x)){
              if(!parseBlock(x)){
                 if(!super.parseExpression(x,type)) return false;
              }
        }
        return true;
    }
    public boolean parseDefDim(LispObject x)
    {
        boolean dmy;
        LispObject w;
        dmy=rB();
        if(!(is("dim")||is("DIM"))) return false;
        dmy=rB();
        ListCell name=new ListCell(); name.d=lisp.nilSymbol;
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        if(!isName(name,type)) {
            putErrorMessage("Syntax Error at DIM statement.");
            return false;
        }

        w=lisp.cons(((ListCell)name).a,lisp.nilSymbol);
        w=lisp.cons(lisp.recSymbol("defdim"),w);
        ((ListCell)x).a=w;

        if(is("("))   {
//            putErrorMessage("Syntax Error at DIM statement.");
           dmy=rB();
           ListCell varl=new ListCell(); varl.d=lisp.nilSymbol; varl.a=lisp.nilSymbol;
           if(!parseVarList(varl)) {
               putErrorMessage("Syntax Error at DIM statement.");
               return false;
           }
           dmy=rB();
           if(!is(")")) {
               putErrorMessage("Syntax Error at DIM statement.");
               return false;
           }
        }
        dmy=rB();
        while(is(",")) {
           dmy=rB();
           ListCell y=new ListCell();
           y.d=lisp.nilSymbol;
           name=new ListCell(); name.d=lisp.nilSymbol;
           type=new ListCell(); type.d=lisp.nilSymbol;
           if(!isName(name,type)) {
               putErrorMessage("Syntax Error at DIM statement.");
               return false;
           }

           w=lisp.cons(((ListCell)name).a,lisp.nilSymbol);
           w=lisp.cons(lisp.recSymbol("defdim"),w);
           ((ListCell)y).a=w;

           if(is("("))   {
//            putErrorMessage("Syntax Error at DIM statement.");
              dmy=rB();
              ListCell varl=new ListCell(); varl.d=lisp.nilSymbol; varl.a=lisp.nilSymbol;
              if(!parseVarList(varl)) {
                  putErrorMessage("Syntax Error at DIM statement.");
                  return false;
              }
              dmy=rB();
              if(!is(")")) {
                  putErrorMessage("Syntax Error at DIM statement.");
                  return false;
              }
           }
           lisp.nconc(x,y);
           dmy=rB();           
        }        
        return true;

    }
    public LispObject nameTable;
    public boolean parseIf2(LispObject x)
    {
        boolean dmy;
        LispObject w;
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        if(!(is("if")||is("IF"))) return false;
        dmy=rB();
        ListCell rlop=new ListCell(); rlop.d=lisp.nilSymbol;
        if(!parseRelational(rlop)) {
            putErrorMessage("Syntax Error at relational operation of IF statement.");
            return false;
        }
        dmy=rB();
        if(!(is("then")||is("THEN"))) {
            putErrorMessage("THEN is expected at IF statement.");
            return false;
        }
        dmy=rB();
        ListCell s1= new ListCell(); s1.d=lisp.nilSymbol;
        if(!parseExpression(s1,type)) {
            putErrorMessage("Syntax Error at the expression after THEN of IF statement.");
            return false;
        }
        dmy=rB();
        if(!(is("else")||is("ELSE"))) {
            w=lisp.cons(lisp.tSymbol,lisp.nilSymbol);
            w=lisp.cons(lisp.car(s1),w);
            w=lisp.cons(lisp.car(rlop),w);
            w=lisp.cons(lisp.recSymbol("if"),w);
            ((ListCell)x).a=w;
//            ((ListCell)x).d=w;
            return true;
        }
        dmy=rB();
        ListCell s2=new ListCell(); s2.d=lisp.nilSymbol;
        if(!parseExpression(s2,type)) {
            putErrorMessage("Syntax Error at the expression after ELSE of IF statement.");
            return false;
        }
        w=lisp.cons(lisp.car(s2),lisp.nilSymbol);
        w=lisp.cons(lisp.car(s1),w);
        w=lisp.cons(lisp.car(rlop),w);
        w=lisp.cons(lisp.recSymbol("if"),w);
        ((ListCell)x).a=w;
//        ((ListCell)x).d=w;
        return true;
    }
    public BasicParser()
    {
    }
    public BasicParser(ABasic basic, InterpreterInterface iif){
    	super(basic,iif);
    }
    public boolean parseBlock(LispObject block)
    {
        boolean dmy;
        LispObject progn=lisp.nilSymbol;
        ListCell x=new ListCell();
        ((ListCell)x).a=lisp.nilSymbol; ((ListCell)x).d=lisp.nilSymbol;
        if(is("{")) {
           dmy=rB();
           if(!parseStatementList(x)) {
               putErrorMessage("Syntax error at statement list of Block, {...}");
               return false;
           }
           dmy=rB();
           if(!is("}")) {
              putErrorMessage("} is expected at the end of Block, {...}");
              return false;
           }
        }
        else
        if(is("[")) {
           dmy=rB();
           if(!parseStatementList(x)) {
               putErrorMessage("Syntax error at statement list of Block, [...]");
               return false;
           }
           dmy=rB();
           if(!is("]")) {
               putErrorMessage("] is expected at the end of Block, [...]");
               return false;
           }
        }
        else return false;
        ((ListCell)block).a=lisp.cons(
                   lisp.recSymbol("progn"),lisp.car(x));
        return true;
    }
    public boolean parseRelOp(LispObject x)
    {
        boolean dmy;
        dmy=rB();
        if(is("=")){
            if(is("=")){}
            ((ListCell)x).a=lisp.sym_m_eq;
            return true;
        }
        if(is("<")){
            if(is("=")){ ((ListCell)x).a=lisp.sym_m_le; return true;}
            if(is(">")){ ((ListCell)x).a=lisp.sym_m_ne; return true;}
            ((ListCell)x).a=lisp.sym_m_lt;
            return true;
        }
        if(is(">")){
            if(is("=")){ ((ListCell)x).a=lisp.sym_m_ge; return true;}
            ((ListCell)x).a=lisp.sym_m_gt;
            return true;
        }
        return false;
    }
    public boolean parseRelational(LispObject x)
    {
        ListCell e1=new ListCell(); e1.d=lisp.nilSymbol;
        ListCell e2=new ListCell(); e2.d=lisp.nilSymbol;
        ListCell rlop=new ListCell(); rlop.d=lisp.nilSymbol;
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        if(!parseExpression(e1,type)) return false;
        if(!parseRelOp(rlop)) return false;
        if(!parseExpression(e2,type)) return false;
        LispObject w=lisp.cons(e1.a,e2);
        ((ListCell)x).a=lisp.cons(rlop.a,w);
        return true;
    }
    public boolean parseIf(LispObject x)
    {
        boolean dmy;
        LispObject w;
        if(!(is("if")||is("IF"))) return false;
        dmy=rB();
        ListCell rlop=new ListCell(); rlop.d=lisp.nilSymbol;
        if(!parseRelational(rlop))
        { putErrorMessage("Syntax Error at relational operation of IF statement.");
          return false;}
        dmy=rB();
        if(!(is("then")||is("THEN")))
        { putErrorMessage("THEN is expected at IF statement.");return false;}
        dmy=rB();
        ListCell s1= new ListCell(); s1.d=lisp.nilSymbol;
        if(!parseStatement(s1))
        { putErrorMessage("Syntax Error at the statement after THEN of IF statement.");
          return false;}
        dmy=rB();
        if(!(is("else")||is("ELSE"))) {
            w=lisp.cons(lisp.tSymbol,lisp.nilSymbol);
            w=lisp.cons(lisp.car(lisp.car(s1)),w);
            w=lisp.cons(((ListCell)rlop).a,w);
            w=lisp.cons(lisp.recSymbol("if"),w);
            ((ListCell)x).a=w;
            return true;
        }
        dmy=rB();
        ListCell s2=new ListCell(); s2.d=lisp.nilSymbol;
        if(!parseStatement(s2))
        {   putErrorMessage("Syntax Error at statement after ELSE of IF statement.");
            return false;}
        w=lisp.cons(lisp.car(lisp.car(s2)),lisp.nilSymbol);
        w=lisp.cons(lisp.car(lisp.car(s1)),w);
        w=lisp.cons(((ListCell)rlop).a,w);
        w=lisp.cons(lisp.recSymbol("if"),w);
        ((ListCell)x).a=w;
        return true;
    }
    public boolean parseReturn(LispObject x, LispObject type)
    {
        boolean dmy;
        LispObject rtn;
        if(!(is("return")||is("RETURN"))) return false;
        dmy=rB();
        if(parseExpression(x,type)) {
            rtn=lisp.cons(lisp.recSymbol("return"),
                lisp.cons(lisp.car(x),lisp.nilSymbol));
        }
        else{
            rtn=lisp.cons(lisp.recSymbol("return"),
                lisp.cons(lisp.tSymbol,lisp.nilSymbol));
        }
        lisp.rplca(x,rtn);
        return true;
    }
    public boolean parseStatement(LispObject x)
    {
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        ListCell y=new ListCell(); y.d=lisp.nilSymbol;
                 y.a=lisp.nilSymbol;
        if(parseReturn(y,type)) {((ListCell)x).a=y;return true;}
        if(parseIf(y))          {((ListCell)x).a=y;return true;}
        if(parseFor(y))         {((ListCell)x).a=y;return true;}
        if(parsePrint(y))       {((ListCell)x).a=y;return true;}
        if(parseBlock(y))       {((ListCell)x).a=y;return true;}
        if(parseGraphicsLine(y))        {((ListCell)x).a=y;return true;}
        if(parseGraphicsPset(y))        {((ListCell)x).a=y;return true;}
        if(parseAssign(y)) {((ListCell)x).a=y;return true;}
        if(parseExpression(y,type))  {((ListCell)x).a=y; return true;}
        return false;
    }
    public boolean parseVarList(LispObject w)
    {
//        ListCell w=new ListCell();
//        w.d=lisp.nilSymbol;
        ListCell type=new ListCell();
        if(!isName(w,type)) return false;
        while(is(",")){
            ListCell y=new ListCell(); y.d=lisp.nilSymbol;
            if(!isName(y,type)) return false;
            lisp.nconc(w,y);
        }
        return true;
    }
    public boolean parseArgl(LispObject argl)
    {
        return false;
    }
    public boolean parseDefun(LispObject x)
    {
        boolean dmy;
        dmy=rB();
        if(!(is("def")||is("DEF"))) return false;
        dmy=rB();
        ListCell name=new ListCell(); name.d=lisp.nilSymbol;
        ListCell type=new ListCell(); type.d=lisp.nilSymbol;
        if(!isName(name,type)) {
            putErrorMessage("Syntax Error at function name of the DEF statement.");
            return false;
        }
        if(!is("(")) {
            putErrorMessage("arg list of ( is expected at the DEF statement.");
            return false;
        }
        dmy=rB();
        ListCell varl=new ListCell(); varl.d=lisp.nilSymbol; varl.a=lisp.nilSymbol;
        if(!parseVarList(varl)) {};
        dmy=rB();
        if(!is(")")) {
            putErrorMessage("arg list of ) is expected at the DEF statement.");
            return false;
        }
        dmy=rB();
        if(!is("=")) {
            putErrorMessage("= is expected at the DEF statement.");
            return false;
        }
        dmy=rB();
        ListCell e=new ListCell(); e.d=lisp.nilSymbol; e.a=lisp.nilSymbol;
//        if( !parseIf2(e)){
//              if(!parseBlock(e)){
                 if(!parseExpression(e,type)) {
                    putErrorMessage("Syntax error at the expression of right hand side of DEF statement.");
                    return false;
                 }
//              }
//        }
        /*
        LispObject nameattr=lisp.cons(((ListCell)name).a,
                              cons(lisp.recSymbol("function"),
                              lisp.nilSymbol));
        nameTable=lisp.cons(nameattr,nameTable);
        */
        LispObject w=lisp.cons(varl,e);
        w=lisp.cons(((ListCell)name).a,w);
        w=lisp.cons(lisp.recSymbol("defun"),w);
        ((ListCell)x).a=w;
        return true;

    }
    public boolean parseBasic2(LispObject flist)
    {
        LispObject progn=lisp.nilSymbol;
        while(true){
        ListCell x=new ListCell();
            x.d=lisp.nilSymbol;
            x.a=lisp.nilSymbol;
            if(parseDefun(x)||parseDefDim(x)){

                if(lisp.Null(((ListCell)flist).a)){
                    ((ListCell)flist).a=x.a;
                }
                else{
                    lisp.nconc(flist,x);
                }
               /*
                String str=lisp.print.print(x.a);
                lisp.printArea.appendText(str);
                LispObject rtn=lisp.preEval(x.a,lisp.environment);
                str=lisp.print.print(rtn);
                lisp.printArea.appendText(str);
               */
            }
            else
            if(parseStatement(x)){
                progn=lisp.nconc(progn,x.a);
            }
            else
            if(is(":")||is(";")){}
            else {
                if(lisp.Null(progn)) return true;
                progn=lisp.cons(lisp.recSymbol("progn"),
                         progn);

                if(lisp.Null(((ListCell)flist).a)){
                    ((ListCell)flist).a=progn;
                }

                else{
                    LispObject w=lisp.cons(progn,lisp.nilSymbol);
                    lisp.nconc(flist,w);
                }

                return true;
            }
        }
    }
    public LispObject parseBasic(LispObject source)
    {
        line=source;
        pointer=line;
        ListCell functions=new ListCell();
        functions.d=lisp.nilSymbol;
        functions.a=lisp.nilSymbol;
        if(parseBasic2(functions)) return functions;
        return lisp.nilSymbol;
    }

}

