package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;

public class ControlledComboBox extends javax.swing.JComboBox
{
    public void hidePopup()
    {
        super.hidePopup();
        System.out.println("combo box popup is hidden");
    }

    public void showPopup()
    {
        super.showPopup();
        System.out.println("combo box popuped");
    }

    public void installModel()
    {
//        setModel(new javax.swing.JComboBox.DefaultComboBoxModel());
       
    }

    public void installListeners()
    {
		SymAction lSymAction = new SymAction();
		addActionListener(lSymAction);
		SymMouse aSymMouse = new SymMouse();
		addMouseListener(aSymMouse);
    }

    public void setSelectedItem(Object anObject)
    {
        super.setSelectedItem(anObject);
//        System.out.println("an item is selected");
    }

    public void controlledComboBox_componentShown(java.awt.event.ComponentEvent event)
    {
        System.out.println("component shown");
    }

    public void controlledComboBox_vetoableChange(java.beans.PropertyChangeEvent event)
    {
    }

    public void controlledComboBox_itemStateChanged(java.awt.event.ItemEvent event)
    {
    }

    public Color tmpColor;

    public void controlledComboBox_mouseExited(java.awt.event.MouseEvent event)
    {
        setBackground(tmpColor);
        repaint();
    }

    public void controlledComboBox_mouseEntered(java.awt.event.MouseEvent event)
    {
        tmpColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

    public void controlledComboBox_mouseClicked(java.awt.event.MouseEvent event)
    {
 //       System.out.println("mouse is clicked");
    }

    public void controlledComboBox_actionPerformed(java.awt.event.ActionEvent event)
    {
//        super.actionPerformed(event);
        System.out.println("action performed");
    }

	public ControlledComboBox()
	{
//        super();
//        setModel(new DefaultComboBoxModel());
//        installAncestorListener();
//        updateUI();

		//{{INIT_CONTROLS
		setSize(0,0);
		//}}

		//{{REGISTER_LISTENERS
//		SymFocus aSymFocus = new SymFocus();
//		this.addFocusListener(aSymFocus);
//		SymMouse aSymMouse = new SymMouse();
//		this.addMouseListener(aSymMouse);
		//}}
		installModel();
		installListeners();
	}

	//{{DECLARE_CONTROLS
	//}}
	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledComboBox.this)
				controlledComboBox_mouseExited(event);
			
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledComboBox.this)
				controlledComboBox_mouseEntered(event);
			
		}

		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledComboBox.this)
				controlledComboBox_mouseClicked(event);
		}
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == this)
				controlledComboBox_actionPerformed(event);
			
		}
	}
	
	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == this)
				controlledComboBox_itemStateChanged(event);
		}
	}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentShown(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == this)
				controlledComboBox_componentShown(event);
		}
	}

	class SymVetoableChange implements java.beans.VetoableChangeListener
	{
		public void vetoableChange(java.beans.PropertyChangeEvent event)
			throws java.beans.PropertyVetoException
		{
			Object object = event.getSource();
			if (object == this)
				controlledComboBox_vetoableChange(event);
		}
	}



	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledComboBox.this)
				ControlledComboBox_focusLost(event);
		}

		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledComboBox.this)
				ControlledComboBox_focusGained(event);
		}
	}

	public void ControlledComboBox_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
		tmpColor=this.getBackground();
		this.setBackground(Color.white);
		repaint();
			 
	}

	public void ControlledComboBox_focusLost(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
		this.setBackground(tmpColor);
		repaint();
			 
	}
}