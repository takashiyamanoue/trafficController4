package org.yamaLab.pukiwikiCommunicator.language;

class ListCell extends LispObject
{
    public LispObject d;
    public LispObject a;
    public boolean isAtom(){
    	return false;
    }
}
