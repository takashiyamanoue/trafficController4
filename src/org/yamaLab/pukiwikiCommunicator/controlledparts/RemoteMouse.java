package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Graphics;

public class RemoteMouse extends java.lang.Object
{
    public void resetColor()
    {
        this.color=this.defaultColor;
    }

    public Color defaultColor;

    public void setColor(Color c)
    {
        this.color=c;
    }

    public boolean getVisible()
    {
        return isVisible;
    }

    public boolean isVisible;

    public void setVisible(boolean tf)
    {
        this.isVisible=tf;
    }

    public Color colorSide;
    public boolean show;
    public int ty[];
    public int tx[];
    public Color color;
    public void move(int xx, int yy)
    {
        x=xx; y=yy;
    }
    public int y;
    public int x;
    public void paint(Graphics g)
    {
        int i;
        if(isVisible){
          Color cc=g.getColor();
          g.setColor(color);
          for(i=0;i<np;i++){
              tx[i]=fy[i]+x;
              ty[i]=fx[i]+y;
          }
          g.fillPolygon(tx,ty,np);
          g.setColor(colorSide);
          g.drawPolygon(tx,ty,np);
          g.setColor(cc);
        }
    }

    public int np;
    public RemoteMouse()
    {
        np=7;
        color=Color.red;
        defaultColor=color;
        colorSide=Color.black;
        tx=new int[np];
        ty=new int[np];
        show=true;
    }

    public static int fx[]={0,5,3,3,-3,-3,-5};
    public static int fy[]={0,5,5,10,10,5,5};
}

