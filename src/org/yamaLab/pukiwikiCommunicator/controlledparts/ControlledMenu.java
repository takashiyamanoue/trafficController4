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

import javax.swing.JMenu;
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
public class ControlledMenu extends JMenu {

	int id;
	FrameWithControlledMenu frame;
    Color defaultColor;
    JPopupMenu popupMenu;
    boolean popupMenuIsVisible;
    boolean isThisMenuSelected;
    int menuEnterCount;  // 0:out 1:enter 2:popupmenu open
    ControlledMenuItem mainMenu;

    public void click()
    {
    	/*
//        this.repaint();
    	Color cc=this.getBackground();
        setBackground(Color.blue);
        repaint();
        try{
          Thread.sleep(50);
        }
        catch(InterruptedException e){}
//        setBackground(defaultColor);
        setBackground(cc);
        this.doClick(10);
        */
    }

    public void unFocus()
    {
     	if(this.popupMenu.isShowing()){
     		return;
    	}
     	this.popupMenu.setVisible(false);
       setBackground(defaultColor);
       repaint();
    }

    public void focus()
    {
    	if(this.popupMenu.isShowing()){
    		this.popupMenu.setVisible(false);
    		return;
    	}
       	this.setPopupMenuVisible(true);    		
        this.defaultColor=getBackground();
        setBackground(Color.white);
        repaint();
    }

    public void setID(int x)
    {
        this.id=x;
    }

    public void setFrame(FrameWithControlledMenu f)
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
//				this.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent evt){
//						System.out.println(""+evt.toString());
//					}
//				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.popupMenu=this.getPopupMenu();
		popupMenu.addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent evt){
				popupMenuExited(evt);
			}
			public void mouseEntered(MouseEvent evt){
				popupMenuEntered(evt);
			}
		});
		this.menuEnterCount=0;
//		this.mainMenu=(ControlledMenu)this.getParent();
	}
	
	public void popupMenuExited(MouseEvent evt){
//		System.out.println("popupMenuExited");
//		this.menuEnterCount--;
//		this.isThisMenuSelected=false;
//		if(isThisMenuSelected)
//		this.popupMenu.setVisible(false);
//		this.isThisMenuSelected=false;
	}
	public void popupMenuEntered(MouseEvent evt){
//		System.out.println("popupMenuEntered");
//		this.menuEnterCount++;
//		this.popupMenu.setVisible(true);
//		this.isThisMenuSelected=true;
//		this.popupMenu.setVisible(true);
	}
	
	public void rootActionPerformed(ActionEvent evt){
//		System.out.println("this.rootActionPerformed "+evt);
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("menu.click("+this.id+")\n");
          if(frame.isDirectOperation()){
//            frame.mouseClickedAtMenu(this.id);
//            this.click();
          }
        }		
	}
	
	public void rootMouseClicked(MouseEvent evt) {
		/*
//		System.out.println("this.mouseClicked, event=" + evt);
		//TODO add your code for this.mouseClicked
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("menu.click("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseClickedAtMenu(this.id);
//            this.click();
          }
        }
        */
	}
	
	public void rootMouseEntered(MouseEvent evt) {
//		System.out.println("this.mouseEntered, event=" + evt);
		//TODO add your code for this.mouseEntered
        if(frame==null) return;
        if(frame.isControlledByLocalUser()) {
          frame.sendEvent("menu.enter("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseEnteredAtMenu(this.id);
            this.focus();
          }
        }
	}
	
	public void rootMouseExited(MouseEvent evt) {
//		System.out.println("this.mouseExited, event=" + evt);
		//TODO add your code for this.mouseExited
        if(frame==null) return;
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("menu.exit("+this.id+")\n");
          if(frame.isDirectOperation()){
            frame.mouseExitedAtMenu(this.id);
            unFocus();
          }
        }
	}
	public void menuSelectionChanged(boolean isIncluded){
//		if(isIncluded) 
//		this.setPopupMenuVisible(false);
	}

}
