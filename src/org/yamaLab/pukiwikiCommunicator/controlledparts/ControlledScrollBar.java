package org.yamaLab.pukiwikiCommunicator.controlledparts;

public class ControlledScrollBar extends javax.swing.JScrollBar
{
	int id;
	PaneWithControlledScrollBar pane;

    public void setValue(int v)
    {
        try{
          super.setValue(v);
        }
        catch(Exception e){
        }
    }

    public void setID(int i)
    {
        id=i;
    }

    public void setPane(PaneWithControlledScrollBar p)
    {
        pane=p;
    }

    public int getID()
    {
        return this.id;
    }
    public void registerListeners()
    {
		SymAdjustment lSymAdjustment = new SymAdjustment();
		this.addAdjustmentListener(lSymAdjustment);
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		SymPropertyChange lSymPropertyChange = new SymPropertyChange();
		this.addPropertyChangeListener(lSymPropertyChange);
    }

    public ControlledScrollBar(int orientation)
    {
        super(orientation);
//        this();
        registerListeners();
    }

	public ControlledScrollBar()
	{

		//{{INIT_CONTROLS
		setSize(430,270);
		//}}

		//{{REGISTER_LISTENERS
		SymAdjustment lSymAdjustment = new SymAdjustment();
		this.addAdjustmentListener(lSymAdjustment);
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		SymPropertyChange lSymPropertyChange = new SymPropertyChange();
		this.addPropertyChangeListener(lSymPropertyChange);
		//}}
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymAdjustment implements java.awt.event.AdjustmentListener
	{
		public void adjustmentValueChanged(java.awt.event.AdjustmentEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledScrollBar.this)
				ControlledScrollBar_adjustmentValueChanged(event);
		}
	}

	void ControlledScrollBar_adjustmentValueChanged(java.awt.event.AdjustmentEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollBar_adjustmentValueChanged_Interaction1(event);
	}

	void ControlledScrollBar_adjustmentValueChanged_Interaction1(java.awt.event.AdjustmentEvent event)
	{
        if(this.pane==null) return;
        if(this.pane.isControlledByLocalUser()){
//         sendEvent("sb.value("+paneID+","+barID+","+v+")\n");
           pane.sendEvent("sbv("+id+","+this.getValue()+")\n");
           if(this.pane.isDirectOperation())
              pane.scrollBarValueChanged(id,this.getValue());
        }
	}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentShown(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledScrollBar.this)
				ControlledScrollBar_componentShown(event);
		}

		public void componentHidden(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledScrollBar.this)
				ControlledScrollBar_componentHidden(event);
		}
	}

	void ControlledScrollBar_componentHidden(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollBar_componentHidden_Interaction1(event);
	}

	void ControlledScrollBar_componentHidden_Interaction1(java.awt.event.ComponentEvent event)
	{
        if(this.pane==null) return;
        if(this.pane.isControlledByLocalUser()){
           pane.sendEvent("sbh("+id+")\n");
           if(this.pane.isDirectOperation()){
             pane.scrollBarIsHidden(id);
             this.setVisible(false);
           }
        }
	}

	void ControlledScrollBar_componentShown(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollBar_componentShown_Interaction1(event);
	}

	void ControlledScrollBar_componentShown_Interaction1(java.awt.event.ComponentEvent event)
	{
        if(this.pane==null) return;
        if(this.pane.isControlledByLocalUser()){
           pane.sendEvent("sbs("+id+")\n");
           if(this.pane.isDirectOperation()){
             pane.scrollBarIsShown(id);
             this.setVisible(true);
           }
        }
	}

	class SymPropertyChange implements java.beans.PropertyChangeListener
	{
		public void propertyChange(java.beans.PropertyChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledScrollBar.this && event.getPropertyName().equalsIgnoreCase("value"))
				ControlledScrollBar_propertyChangeValue(event);
		}
	}

	void ControlledScrollBar_propertyChangeValue(java.beans.PropertyChangeEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollBar_propertyChangeValue_Interaction1(event);
	}

	void ControlledScrollBar_propertyChangeValue_Interaction1(java.beans.PropertyChangeEvent event)
	{
	    /*
        if(this.pane==null) return;
        if(this.pane.isControlledByLocalUser()){
//         sendEvent("sb.value("+paneID+","+barID+","+v+")\n");
           pane.sendEvent("sbv("+id+","+this.getValue()+")\n");
           pane.scrollBarValueChanged(id,this.getValue());
        }
        */
	}
}