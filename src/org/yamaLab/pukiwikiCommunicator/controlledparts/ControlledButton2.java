package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.AbstractBorder;

public class ControlledButton2 extends javax.swing.JButton implements SelectedButton 
{
    public int getID()
    {
        return this.buttonID;
    }

    public void click()
    {
        Color tempc=this.getBackground();
        this.setBackground(Color.blue);
        try{
            Thread.sleep(80);
        }
        catch(InterruptedException e){}
        setBackground(tempc);
    }

    public void unFocus()
    {
	    if(border1!=null) setBorder(border1);
	    repaint();
    }

    public void focus()
    {
		if(border2!=null) this.setBorder(border2);
		repaint();
    }

    public void setID(int i)
    {
        buttonID=i;
    }

    int buttonID;
    AbstractBorder border2;

    public void setBorder2(AbstractBorder border)
    {
        border2=border;
    }

    public void setBorder1(AbstractBorder border)
    {
        border1=border;
	    if(border1!=null) setBorder(border1);
    }

    AbstractBorder border1;
    FrameWithControlledButton frame;

    public void setFrame(FrameWithControlledButton f)
    {
        frame=f;
        
    }

	public ControlledButton2()
	{

		//{{INIT_CONTROLS
		setBackground(new java.awt.Color(204,204,204));
		setForeground(java.awt.Color.black);
		setFont(new Font("Dialog", Font.BOLD, 12));
		setSize(82,34);
		//}}

		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		this.addActionListener(lSymAction);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		//}}
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton2.this)
				controlledButton2_actionPerformed(event);
		}
	}

	public void controlledButton2_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
//			 System.out.println("color-button:"+buttonID+" clicked");
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
             frame.sendEvent("btn.click("+buttonID+")\n");
             if(frame.isDirectOperation()){
    	    	 frame.mouseClickedAtButton(buttonID);
                 click();
             }
        }
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton2.this)
				controlledButton_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton2.this)
				controlledButton_mouseEntered(event);
		}
	}

	public void controlledButton_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		if(frame==null) return;
		if(frame.isControlledByLocalUser()){
	    	frame.sendEvent("btn.enter("+buttonID+")\n");
    	    if(frame.isDirectOperation()){
        		frame.mouseEnteredAtButton(buttonID);
		        this.focus();
		    }
		}
	}

	public void controlledButton_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		if(frame==null) return;
		if(frame.isControlledByLocalUser()){
	        frame.sendEvent("btn.exit("+buttonID+")\n");
    	    if(frame.isDirectOperation()){
        	    if(border1!=null) setBorder(border1);
	            frame.mouseExitedAtButton(buttonID);
	            repaint();
	        }
	    }
	}
}

