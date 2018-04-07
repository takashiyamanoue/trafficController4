/*
 * ?øΩ?ê¨?øΩ?øΩ: 2005/04/25
 *
 * ?øΩ?øΩ?øΩÃêÔøΩ?øΩ?øΩ?øΩ?øΩ?øΩÍÇΩ?øΩR?øΩ?øΩ?øΩ?øΩ?øΩg?øΩÃë}?øΩ?øΩ?øΩ?øΩe?øΩ?øΩ?øΩv?øΩ?øΩ?øΩ[?øΩg?øΩ?øΩœçX?øΩ?øΩ?øΩÈÇΩ?øΩpukiwikiCommunicator.controlledparts> ?øΩR?øΩ[?øΩh?øΩ?øΩ?øΩ?øΩ > ?øΩR?øΩ[?øΩh?øΩ∆ÉR?øΩ?øΩ?øΩ?øΩ?øΩg
 */
package org.yamaLab.pukiwikiCommunicator.controlledparts;

import javax.swing.JTabbedPane;
import java.awt.Color;

/**
 * @author yamachan
 *
 * ?øΩ?øΩ?øΩÃêÔøΩ?øΩ?øΩ?øΩ?øΩ?øΩÍÇΩ?øΩR?øΩ?øΩ?øΩ?øΩ?øΩg?øΩÃë}?øΩ?øΩ?øΩ?øΩe?øΩ?øΩ?øΩv?øΩ?øΩ?øΩ[?øΩg?øΩ?øΩœçX?øΩ?øΩ?øΩÈÇΩ?øΩ?øΩ
 * ?øΩE?øΩB?øΩ?øΩ?øΩh?øΩE > ?øΩ›íÔøΩ > Java > ?øΩR?øΩ[?øΩh?øΩ?øΩ?øΩ?øΩ > ?øΩR?øΩ[?øΩh?øΩ∆ÉR?øΩ?øΩ?øΩ?øΩ?øΩg
 */
public class ControlledTabbedPane extends JTabbedPane {

	Color tempColor;
	Color defaultColor;
	int id;
	FrameWithControlledTabbedPane frame;

	public void unFocus()
	{
		if(this.frame==null) return;
		setBackground(defaultColor);
		repaint();
	}

	public void focus()
	{
		if(this.frame==null) return;
		defaultColor=getBackground();
		setBackground(Color.white);
		repaint();
	}

	public void setSelectedIndexX(int v)
	{
		if(this.frame==null) return;
		this.setSelectedIndex(v);
		this.repaint();
	}

	public void setID(int i)
	{
		id=i;
	}

	public void setFrame(FrameWithControlledTabbedPane f)
	{
		frame=f;
	}


	public ControlledTabbedPane()
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
	
	//{{DECLARE_CONTROLS
	//}}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTabbedPane.this)
				ControlledTabbedPane_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTabbedPane.this)
				ControlledTabbedPane_mouseEntered(event);
		}
	}

	public void ControlledTabbedPane_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		  this.focus();
		if(frame==null) return;
		if(frame.isControlledByLocalUser()){
		  frame.mouseEnteredAtTabbedPane(id);
		  frame.sendEvent("tbpane.enter("+id+")\n");
		}
	}

	public void ControlledTabbedPane_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
//		System.out.println("mouse exited controlled slider.");
		  this.unFocus();
		if(frame==null) return;
		if(frame.isControlledByLocalUser()){
		  frame.mouseExitedAtTabbedPane(id);
		  frame.sendEvent("tbpane.exit("+id+")\n");
		}
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTabbedPane.this)
				ControlledTabbedPane_stateChanged(event);
		}
	}

	public void ControlledTabbedPane_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		int v=this.getSelectedIndex();
//		  this.setValueX(v);
		if(frame==null) return;
		if(frame.isControlledByLocalUser()){
			 frame.stateChangedAtTabbedPane(id,v);
			 frame.sendEvent("tbpane.state("+id+","+v+")\n");
		}

	}
}