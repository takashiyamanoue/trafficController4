package org.yamaLab.pukiwikiCommunicator.language;

public class Atom extends LispObject
{
	private static String kind="atom";
	public boolean isAtom(){
		return true;
	}
	public boolean isKind(String x){
		return x.equals(kind);
	}
}


