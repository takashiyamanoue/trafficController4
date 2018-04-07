package org.yamaLab.pukiwikiCommunicator.language;

public class MyNumber extends Atom
{
	public static String kind="number";
	public boolean isKind(String x){
		return x.equals(kind);
	}
    public MyNumber neg()
    {
        return null;
    }
    public boolean ne(MyNumber y)
    {
        return val!=y.val;
    }
    public MyNumber mod(MyNumber y)
    {
        return null;
    }
    public MyNumber log()
    {
        return null;
    }
    public MyNumber exp()
    {
        return null;
    }
    public MyNumber sqrt()
    {
        return null;
    }
    public MyNumber atan()
    {
        return null;
    }
    public MyNumber tan()
    {
        return null;
    }
    public MyNumber cos()
    {
        return null;
    }
    public MyNumber sin()
    {
        return null;
    }
    public int getInt()
    {
        return val;
    }
    public MyNumber exp(MyNumber y)
    {
        return null;
    }
    public boolean le(MyNumber y)
    {
        return val<=y.val;
    }
    public boolean lt(MyNumber y)
    {
        return false;
    }
    public boolean ge(MyNumber y)
    {
        return val>=y.val;
    }
    public boolean gt(MyNumber y)
    {
        return val>y.val;
    }
    public boolean eq(MyNumber y)
    {
        return val==y.val;
    }
    public MyNumber div(MyNumber y)
    {
        return null;
    }
    public MyNumber mul(MyNumber y)
    {
        return null;
    }
    public MyNumber sub(MyNumber y)
    {
        return null;
    }
    public MyNumber add(MyNumber y)
    {
        return null;
    }
    public MyNumber band(MyNumber y)
    {
    	return null;
    }
    public MyNumber bor(MyNumber y)
    {
    	return null;
    }
    public MyNumber bxor(MyNumber y)
    {
    	return null;
    }
    public MyNumber bnot()
    {
    	return null;
    }
  
    public MyNumber()
    {
    }
    public MyNumber(int x)
    {
        val=x;
    }
    public int val;
}


