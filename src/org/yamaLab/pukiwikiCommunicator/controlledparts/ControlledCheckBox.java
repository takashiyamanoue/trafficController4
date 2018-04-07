package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Font;

public class ControlledCheckBox extends javax.swing.JCheckBox
{
    public void changeState(int x)
    {
        this.setSelected(x==1);
    }

    public void unFocus()
    {
        setBackground(defaultColor);
        repaint();
    }

    public void setID(int x)
    {
        this.boxID=x;
    }

    public void setFrame(FrameWithControlledCheckBox f)
    {
        frame=f;
    }

    FrameWithControlledCheckBox frame;
    Color defaultColor;
    int boxID;
    Color tmpColor;

    public void focus()
    {
        tmpColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

	public ControlledCheckBox()
	{

		//{{INIT_CONTROLS
		setBackground(new java.awt.Color(204,204,204));
		setForeground(java.awt.Color.black);
		setFont(new Font("Dialog", Font.BOLD, 12));
		setSize(50,20);
		//}}

		//{{REGISTER_LISTENERS
		SymItem lSymItem = new SymItem();
		this.addItemListener(lSymItem);
		SymChange lSymChange = new SymChange();
		this.addChangeListener(lSymChange);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		//}}
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledCheckBox.this)
				ControlledCheckBox_itemStateChanged(event);
		}
	}

	void ControlledCheckBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		ControlledCheckBox_itemStateChanged_Interaction1(event);
	}

	void ControlledCheckBox_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
			// ControlledCheckBox Show the ControlledCheckBox
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledCheckBox.this)
				ControlledCheckBox_stateChanged(event);
		}
	}

	void ControlledCheckBox_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
			 
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
            int s=0;
            if(this.isSelected()) s=1; else s=0;
          frame.sendEvent("cbx.state("+boxID+","+s+")\n");
          if(frame.isDirectOperation())
            frame.stateChangedAtCheckBox(boxID,s);
        }
//		ControlledCheckBox_stateChanged_Interaction1(event);
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
	}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledCheckBox.this)
				ControlledCheckBox_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledCheckBox.this)
				ControlledCheckBox_mouseEntered(event);
		}
	}

	void ControlledCheckBox_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
        if(frame==null) return;
        if(frame.isControlledByLocalUser()) {
          frame.sendEvent("cbx.enter("+boxID+")\n");
          if(frame.isDirectOperation()){
            frame.mouseEnteredAtCheckBox(boxID);
            this.focus();
          }
        }
	}

	void ControlledCheckBox_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("cbx.exit("+boxID+")\n");
          if(frame.isDirectOperation()){
            frame.mouseExitedAtCheckBox(boxID);
            unFocus();
          }
        }
    }
			 
}
