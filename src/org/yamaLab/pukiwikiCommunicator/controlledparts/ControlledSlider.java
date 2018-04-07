package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;

public class ControlledSlider extends javax.swing.JSlider
{
    public void unFocus()
    {
        if(this.frame==null) return;
        setBackground(defaultColor);
        repaint();
    }

    Color tempColor;

    public void focus()
    {
        if(this.frame==null) return;
        tempColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

    Color defaultColor;

    public void setValueX(int v)
    {
        if(this.frame==null) return;
        this.setValue(v);
        this.repaint();
    }

    public void setID(int i)
    {
        id=i;
    }

    int id;

    public void setFrame(FrameWithControlledSlider f)
    {
        frame=f;
    }

    FrameWithControlledSlider frame;

	public ControlledSlider()
	{
	    super();

		//{{INIT_CONTROLS
		setToolTipText("Depth");
		setBackground(new java.awt.Color(204,204,204));
		setForeground(new java.awt.Color(153,153,204));
		setSize(0,0);
		//}}

		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymChange lSymChange = new SymChange();
		this.addChangeListener(lSymChange);
		//}}
		this.defaultColor=this.getBackground();
	}
	
	public ControlledSlider(int orientation, int min, int max, int value){
		super(orientation, min, max, value);
		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymChange lSymChange = new SymChange();
		this.addChangeListener(lSymChange);
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
			if (object == ControlledSlider.this)
				ControlledSlider_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledSlider.this)
				ControlledSlider_mouseEntered(event);
		}
	}

	public void ControlledSlider_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
          this.focus();
		if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sliderMouseEntered(id);
          frame.sendEvent("slid.enter("+id+")\n");
        }
	}

	public void ControlledSlider_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
//		System.out.println("mouse exited controlled slider.");
          this.unFocus();
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sliderMouseExited(id);
          frame.sendEvent("slid.exit("+id+")\n");
        }
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledSlider.this)
				ControlledSlider_stateChanged(event);
		}
	}

	public void ControlledSlider_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
        int v=this.getValue();
//        this.setValueX(v);
		if(frame==null) return;
        if(frame.isControlledByLocalUser()){
             frame.sliderStateChanged(id,v);
             frame.sendEvent("slid.state("+id+","+v+")\n");
        }

	}
}