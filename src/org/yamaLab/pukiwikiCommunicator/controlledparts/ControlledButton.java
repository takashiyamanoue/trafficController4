package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Font;

public class ControlledButton extends javax.swing.JButton implements SelectedButton 
{
    Color defaultColor;

    public void click()
    {
    	Color cc=this.getBackground();
        setBackground(Color.blue);
        repaint();
        try{
          Thread.sleep(80);
        }
        catch(InterruptedException e){}
//        setBackground(defaultColor);
        setBackground(cc);
        repaint();
    }

    public void unFocus()
    {
        setBackground(defaultColor);
        repaint();
    }

    public void focus()
    {
//        tmpColor=getBackground();
        this.defaultColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

    public int getID()
    {
        return this.buttonID;
    }

    public void setID(int i)
    {
        buttonID=i;
    }

    public void setFrame(FrameWithControlledButton f)
    {
        frame=f;
    }

    FrameWithControlledButton frame;
    int buttonID;

    public void controlledButton_actionPerformed(java.awt.event.ActionEvent event)
    {
//        System.out.println(this.getText()+" is selected.");
//          frame.mouseClickedAtButton(buttonID);
    }

    public void controlledButton_mouseClicked(java.awt.event.MouseEvent event)
    {
//        System.out.println(this.getText()+" is clicked");
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("btn.click("+buttonID+")\n");
          if(frame.isDirectOperation()){
            frame.mouseClickedAtButton(buttonID);
            this.click();
          }
        }
    }

    public void controlledButton_mouseEntered(java.awt.event.MouseEvent event)
    {
        if(frame==null) return;
        if(frame.isControlledByLocalUser()) {
          frame.sendEvent("btn.enter("+buttonID+")\n");
          if(frame.isDirectOperation()){
            frame.mouseEnteredAtButton(buttonID);
            this.focus();
          }
        }
    }

    public void controlledButton_mouseExited(java.awt.event.MouseEvent event)
    {
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("btn.exit("+buttonID+")\n");
          if(frame.isDirectOperation()){
            frame.mouseExitedAtButton(buttonID);
            unFocus();
          }
        }
    }


    public Color tmpColor;

	public ControlledButton()
	{

		//{{INIT_CONTROLS
		setBackground(new java.awt.Color(204,204,204));
		setForeground(java.awt.Color.black);
		setFont(new Font("Dialog", Font.BOLD, 12));
		setSize(84,30);
		//}}

		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		addActionListener(lSymAction);
		SymMouse aSymMouse = new SymMouse();
		addMouseListener(aSymMouse);

		//}}
		this.defaultColor=this.getBackground();
	}

	//{{DECLARE_CONTROLS
	//}}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton.this)
				controlledButton_mouseExited(event);
			
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton.this)
				controlledButton_mouseEntered(event);
			
		}

		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton.this)
				controlledButton_mouseClicked(event);
		}
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
		    /*
			Object object = event.getSource();
			if (object == this)
				controlledButton_actionPerformed(event);
			*/
		}
	}

/*
	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton.this)
				ControlledButton_mouseMoved(event);
		}

		public void mouseDragged(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledButton.this)
				ControlledButton_mouseDragged(event);
		}
	}
*/
}