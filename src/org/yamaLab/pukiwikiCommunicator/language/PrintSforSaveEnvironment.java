package org.yamaLab.pukiwikiCommunicator.language;


public class PrintSforSaveEnvironment extends PrintS
{
    public String strConst(String s)
    {
        int len=s.length();
        int i=0;
        String sx="";
        while(i<len){
            char c=s.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){sx=sx+'\\'; sx=sx+c; i++;
                 if(i>=len) break;
                 c=s.charAt(i); sx=sx+c; i++; }
            else
            if((int)c==10){
                sx=sx+"\\n"; i++;
                if(i>=len) break;
                c=s.charAt(i);
                if((int)c==13) i++;
                }
            else
            if((int)c==13){
                sx=sx+"\\n"; i++;
                if(i>=len) break;
                c=s.charAt(i);
                if((int)c==10) i++;}
            else
            { sx=sx+c; i++; }
//            System.out.println(sx);
        }

        // Unicode ‚ð S-JIS Code ‚É•ÏŠ·
//	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
//        String rtn=new String(sjisCode,0);
        String rtn="\""+sx+"\"";
        return rtn;

    }

    public void printString(LispObject s)
    {
        String x=((MyString)s).val;
        String w=strConst(x);
        out.append(w);
    }

	public PrintSforSaveEnvironment(ALisp lsp)
	{
		super(lsp);
	}
}