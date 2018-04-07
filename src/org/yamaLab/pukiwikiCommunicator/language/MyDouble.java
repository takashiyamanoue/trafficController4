package org.yamaLab.pukiwikiCommunicator.language;

public class MyDouble extends MyInt
{
	public static String kind="mydouble";
	public boolean isKind(String x){
		return x.equals(kind);
	}
    public MyNumber neg()
    {
        return new MyDouble(-val);
    }
    public boolean ne(MyNumber y)
    {/*
        if(y.getClass().getName().equals("MyInt")){
            return val!=(double)(((MyInt)y).val);
        }
        if(y.getClass().getName().equals("MyDouble")){
        */
    	if(y.isKind("myint")){
    		return val!=(double)(((MyInt)y).val);
    	}
    	if(y.isKind("mydouble")){
            return val!=((MyDouble)y).val;
        }
        return false;
    }
    public MyNumber mod(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return new MyDouble(val%((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyDouble(val%((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber log()
    {
        return new MyDouble(Math.log(val));
    }
    public MyNumber sqrt()
    {
        return new MyDouble(Math.sqrt(val));
    }
    public MyNumber exp()
    {
        return new MyDouble(Math.exp(val));
    }
    public MyNumber atan()
    {
            return new MyDouble(Math.atan(val));
    }
    public MyNumber tan()
    {
         return new MyDouble(Math.tan(val));
    }
    public MyNumber cos()
    {
            return new MyDouble(Math.cos(val));
    }
    public MyNumber sin()
    {
            return new MyDouble(Math.sin(val));
    }
    public int getInt()
    {
        return (int)val;
    }
    public boolean ge(MyNumber y)
    {
//         if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return val>=(double)(((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return val>=((MyDouble)y).val;
        }
        return false;
   }
    public boolean gt(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	  if(y.isKind("myint")){
            return val>(double)(((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	  if(y.isKind("mydouble")){
            return val>((MyDouble)y).val;
        }
        return false;
    }
    public boolean lt(MyNumber y)
    {
        if(y.isKind("myint")){
            return val<(double)(((MyInt)y).val);
        }
    	if(y.isKind("mydouble")){
            return val<((MyDouble)y).val;
        }
        return false;
    }
    public boolean eq(MyNumber y)
    {
        if(y.isKind("myint")){
            return val==(double)(((MyInt)y).val);
        }
        if(y.isKind("mydouble")){
            return val==((MyDouble)y).val;
        }
        return false;
    }
    public boolean le(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return val<=(double)(((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return val<=((MyDouble)y).val;
        }
        return false;
    }
    public MyNumber exp(MyNumber y)
    {
        int i;
        int xi;
        double xd,yd;
//         if(y.getClass().getName().equals("MyInt")){
        if(y.isKind("myint")){
            int yy=((MyInt)y).val;
            if(yy==0) return new MyDouble(1.0);
            if(yy>0){
                xd=1.0;
                for(i=0;i<yy;i++) xd=xd*val;
                return new MyDouble(xd);
            }
            if(yy<0){
                xd=1.0;
                for(i=0;i<-yy;i++) xd=xd/val;
                return new MyDouble(xd);
            }
            return null;
        }
//        if(y.getClass().getName().equals("MyDouble")){
        if(y.isKind("mydouble")){
            yd=((MyDouble)y).val;
            if(yd==0.0) new MyDouble(1.0);
            if(val<=0.0) return null;
            double r=exp((double)val,yd);
            return new MyDouble(r);
        }
        return null;
    }
    public MyNumber div(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return new MyDouble(val/((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyDouble(val/((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber mul(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return new MyDouble(val*((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyDouble(val*((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber sub(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return new MyDouble(val-((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyDouble(val-((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber add(MyNumber y)
    {
//        if(y.getClass().getName().equals("MyInt")){
    	if(y.isKind("myint")){
            return new MyDouble(val+((MyInt)y).val);
        }
//        if(y.getClass().getName().equals("MyDouble")){
    	if(y.isKind("mydouble")){
            return new MyDouble(val+((MyDouble)y).val);
        }
        return null;
    }
    public MyDouble(double d)
    {
        val=d;
    }
    public double val;
}


