package org.yamaLab.pukiwikiCommunicator.language;
import java.util.Hashtable;
public class ArrayManager extends java.lang.Object
{
    public void init()
    {
        arrayOfArrays=new Hashtable();
   }
    public LispObject get(String aname, String index)
    {
        Hashtable theArray=(Hashtable)arrayOfArrays.get(aname);
        if(theArray==null){
            return null;
        }
        LispObject rtn=(LispObject)theArray.get(index);
        return rtn;
    }
    public void put(String aname, String index, LispObject val)
    {
        Hashtable theArray=(Hashtable)arrayOfArrays.get(aname);
        if(theArray==null) {
            newArray(aname);
            theArray=(Hashtable)arrayOfArrays.get(aname);
        }
        theArray.put(index,val);
    }
    public void newArray(String n)
    {
        arrayOfArrays.put(n,new Hashtable());
    }
    public Hashtable arrayOfArrays;
    public ArrayManager()
    {
        init();
    }
}

