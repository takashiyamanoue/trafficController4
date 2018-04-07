package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.util.Vector;
import javax.swing.JScrollBar;
import java.awt.*;

public class ControlledScrollPane extends javax.swing.JScrollPane implements PaneWithControlledScrollBar   
{
	public ControlledScrollBar hscrollbar;
	public ControlledScrollBar vscrollbar;
	public Vector scrollBars;
	int id;
	FrameWithControlledPane frame;

    public boolean isDirectOperation()
    {
        if(frame==null) return true;
        return frame.isDirectOperation();
    }

    public void sendEvent(String x)
    {
        if(frame==null) return;
        String s="pane("+this.id+")."+x;
        frame.sendEvent(s);
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        if(frame==null) return true;
        return frame.isControlledByLocalUser();
    }

    public void setID(int i)
    {
        id=i;
    }

    public void setFrame(FrameWithControlledPane f)
    {
        frame=f;
    }

    public void hideScrollBar(int barID)
    {
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
//        bar.hide();
        bar.setVisible(false);
    }

    public void showScrollBar(int barID)
    {
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
        bar.setVisible(true);
    }

    public void scrollBarIsHidden(int barID)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        if(frame==null) return;
        frame.scrollBarHidden(id,barID);
    }

    public void scrollBarIsShown(int barID)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        if(frame==null) return;
        frame.scrollBarShown(id,barID);
    }

    public void scrollBarValueChanged(int barID, int v)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        if(frame==null) return;
        frame.scrollBarValueChanged(id,barID,v);
    }

    public void setScrollBarValue(int barID, int v)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
        bar.setValue(v);
    }

    public JScrollBar createVerticalScrollBar()
    {
//        return new ScrollBar(JScrollBar.VERTICAL);
//       return new ControlledScrollBar(JScrollBar.VERTICAL);
          if(this.vscrollbar==null){
              if(this.scrollBars==null){
        		 vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		         hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		         scrollBars=new Vector();
		         scrollBars.addElement(vscrollbar);
		         scrollBars.addElement(hscrollbar);
		         int numberOfScrollBars=scrollBars.size();	
		         for(int i=0;i<numberOfScrollBars;i++){
		            ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
		            bar.setID(i);
		            bar.setPane(this);
		         }
              }
          }
         return this.vscrollbar;
    }

    public JScrollBar createHorizontalScrollBar()
    {
//        return new ScrollBar(JScrollBar.HORIZONTAL);
//        return new ControlledScrollBar(JScrollBar.HORIZONTAL);
          if(this.hscrollbar==null){
              if(this.scrollBars==null){
        		 vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		         hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		         scrollBars=new Vector();
		         scrollBars.addElement(vscrollbar);
		         scrollBars.addElement(hscrollbar);
		         int numberOfScrollBars=scrollBars.size();	
		         for(int i=0;i<numberOfScrollBars;i++){
		            ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
		            bar.setID(i);
		            bar.setPane(this);
		         }
              }
          }
          return this.hscrollbar;
    }

	public ControlledScrollPane()
	{
        super();
		//{{INIT_CONTROLS
		setSize(0,0);
		//}}

		//{{REGISTER_LISTENERS
		SymPropertyChange lSymPropertyChange = new SymPropertyChange();
		this.addPropertyChangeListener(lSymPropertyChange);
		//}}
		
		if(scrollBars==null){
  		  vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		  hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		  scrollBars=new Vector();
		  scrollBars.addElement(vscrollbar);
		  scrollBars.addElement(hscrollbar);
		  int numberOfScrollBars=scrollBars.size();	
		  for(int i=0;i<numberOfScrollBars;i++){
		      ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
		      bar.setID(i);
		      bar.setPane(this);
		  }
		}

	}

    public ControlledScrollPane(Component c){
    	super(c);

		SymPropertyChange lSymPropertyChange = new SymPropertyChange();
		this.addPropertyChangeListener(lSymPropertyChange);
		
		if(scrollBars==null){
		  vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		  hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		  scrollBars=new Vector();
		  scrollBars.addElement(vscrollbar);
		  scrollBars.addElement(hscrollbar);
		  int numberOfScrollBars=scrollBars.size();	
		  for(int i=0;i<numberOfScrollBars;i++){
			  ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
			  bar.setID(i);
			  bar.setPane(this);
		  }
		}
   }

	//{{DECLARE_CONTROLS
	//}}


	class SymPropertyChange implements java.beans.PropertyChangeListener
	{
		public void propertyChange(java.beans.PropertyChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledScrollPane.this && event.getPropertyName().equalsIgnoreCase("verticalScrollBar"))
				ControlledScrollPane_propertyChangeVerticalScrollBar(event);
			if (object == ControlledScrollPane.this && event.getPropertyName().equalsIgnoreCase("horizontalScrollBar"))
				ControlledScrollPane_propertyChangeHorizontalScrollBar(event);
		}
	}

	void ControlledScrollPane_propertyChangeHorizontalScrollBar(java.beans.PropertyChangeEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollPane_propertyChangeHorizontalScrollBar_Interaction1(event);
	}

	void ControlledScrollPane_propertyChangeHorizontalScrollBar_Interaction1(java.beans.PropertyChangeEvent event)
	{
			this.setHorizontalScrollBar(this.getHorizontalScrollBar());
	}

	void ControlledScrollPane_propertyChangeVerticalScrollBar(java.beans.PropertyChangeEvent event)
	{
		// to do: code goes here.
			 
		ControlledScrollPane_propertyChangeVerticalScrollBar_Interaction1(event);
	}

	void ControlledScrollPane_propertyChangeVerticalScrollBar_Interaction1(java.beans.PropertyChangeEvent event)
	{
			this.setVerticalScrollBar(this.getVerticalScrollBar());
	}
}