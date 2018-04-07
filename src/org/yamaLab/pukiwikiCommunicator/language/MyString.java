package org.yamaLab.pukiwikiCommunicator.language;

public class MyString extends MyNumber
{
	public static String kind="mystring";
	public boolean isKind(String x){
		return x.equals(kind);
	}
    public boolean eq(MyNumber y)
    {
    	/*
        if(y.getClass().getName().equals("MyInt")){
            return false;
        }
        if(y.getClass().getName().equals("MyDouble")){
            return false;
        }
        if(y.getClass().getName().equals("MyString")){
        */
    	if(y.isKind("mystring")){
            return val.equals(((MyString)y).val);
        }
       return false;
    }
    public boolean ne(MyNumber y)
    {
        return !eq(y);
    }

    public MyNumber add(MyNumber y)
    {
//            if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            String r=val+((MyInt)y).val;
            return new MyString(r);
        }
    	if(y.isKind("mylong")){
            String r=val+((MyLong)y).val;
            return new MyString(r);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyString(val+((MyDouble)y).val);
        }
//        if(y.getClass().getName().equals("MyString")){
    	if(y.isKind("mystring")){
            return new MyString(val+((MyString)y).val);
        }
        return null;
}
    public MyString(String x)
    {
        val=x;
    }
    public String val;
}


