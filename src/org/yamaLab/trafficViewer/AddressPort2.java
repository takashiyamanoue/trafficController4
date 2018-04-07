package org.yamaLab.trafficViewer;

public class AddressPort2 {
  public AddressPort x;
  public AddressPort y;
  public AddressPort2(AddressPort x1, AddressPort y1){
	  this.x=x1;
	  this.y=y1;
  }
  
  @Override
  public boolean equals(Object o){
	  if(o==this) return true;
	  try{
	     AddressPort2 a=(AddressPort2)o;
	     if(!a.x.equals(x)) return false;
	     if(!a.y.equals(y)) return false;
	     return true;
	  }
	  catch(Exception e){
		  return false;
	  }
  }
  
  @Override
  public int hashCode(){
	  return x.hashCode()*2+y.hashCode();
  }
  public String toString(){
	  return x.toString()+"-"+y.toString();
  }
}