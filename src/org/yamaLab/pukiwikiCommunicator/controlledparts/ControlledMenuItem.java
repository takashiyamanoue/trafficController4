package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.MenuComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ControlledMenuItem extends JMenuItem {

	int id;
	FrameWithControlledMenuItem frame;
	ActionListener myActionListener;
	boolean isSelected;

    Color defaultColor;

    public ActionListener getMyActionListener(){
    	return myActionListener;
    }
    
    public void click()
    {

    	click2();
        this.doClick(10);
        try{
        	Thread.sleep(10);
        }
        catch(InterruptedException e){}
 //       }
    }

    public void click2()
    {
//    	System.out.println("clicked");
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
        JPopupMenu menu=(JPopupMenu)(this.getParent());
        menu.setVisible(false);
//        if(!this.frame.isControlledByLocalUser()){

    }
    public void unFocus()
    {
        setBackground(defaultColor);
       repaint();
    }

    public void focus()
    {
        this.defaultColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

    public int getID()
    {
        return this.id;
    }

    public void setID(int x)
    {
        this.id=x;
    }

    public void setFrame(FrameWithControlledMenuItem f)
    {
        frame=f;
    }
    
	public void initGUI() {
		try {
			{
				this.addMouseListener(new MouseAdapter() {
					public void mouseExited(MouseEvent evt) {
						rootMouseExited(evt);
					}
					public void mouseEntered(MouseEvent evt) {
						rootMouseEntered(evt);
					}
					public void mouseClicked(MouseEvent evt) {
						rootMouseClicked(evt);
					}
				});
//				myActionListener=new MyActionListener();
//				this.addActionListener(myActionListener);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		this.setEnabled(false);
		
	}
	public void rootActionPerformed(ActionEvent evt){
//		System.out.println("this.actionPerformed "+evt);
//        if(frame==null) return;
//        if(frame.isControlledByLocalUser()){
//          frame.sendEvent("item.click("+this.id+")\n");
//          if(frame.isDirectOperation()){
//            frame.mouseClickedAtMenuItem(this.id);
//            this.click();
//          }
 //       }
	}
	
	public void rootMouseClicked(MouseEvent evt) {
//		System.out.println("this.mouseClicked, event=" + evt);
		//TODO add your code for this.mouseClicked
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("item.click("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseClickedAtMenuItem(this.id);
            this.click2();
          }
        }
	}
	
	public void rootMouseEntered(MouseEvent evt) {
//		System.out.println("this.mouseEntered, event=" + evt);
		//TODO add your code for this.mouseEntered
        if(frame==null) return;
        if(frame.isControlledByLocalUser()) {
          frame.sendEvent("item.enter("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseEnteredAtMenuItem(this.id);
            this.focus();
          }
        }
	}
	
	public void rootMouseExited(MouseEvent evt) {
//		System.out.println("this.mouseExited, event=" + evt);
		//TODO add your code for this.mouseExited
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("item.exit("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseExitedAtMenuItem(this.id);
            unFocus();
          }
        }
	}
    class MyActionListener implements ActionListener {
			public void actionPerformed(ActionEvent evt){
//				System.out.println(""+evt.toString());
//				rootActionPerformed(evt);
			}
	}
}
