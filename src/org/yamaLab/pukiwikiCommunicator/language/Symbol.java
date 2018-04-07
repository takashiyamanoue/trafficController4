package org.yamaLab.pukiwikiCommunicator.language;

public class Symbol extends Atom
{
    public String name;
    public static String kind="symbol";
    public Symbol(String s)
    {
        name=s;
        hc=s.hashCode();
    }
    public Symbol(int x)
    {
        hc=x;
    }
    public boolean isKind(String x){
    	return x.equals(kind);
    }
    public int hc;
}
