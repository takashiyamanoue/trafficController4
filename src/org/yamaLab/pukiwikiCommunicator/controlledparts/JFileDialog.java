/*
		A basic implementation of the JFrame class.
*/
package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

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
public class JFileDialog extends javax.swing.JFrame
                        implements EditDialog, 
                                   FrameWithControlledTextAreas, 
                                   FrameWithControlledButton,
                                   FrameWithControlledPane
{
    public boolean isDirectOperation()
    {
        if(this.listener==null) return true;
        return this.listener.isDirectOperation();
    }

    public void scrollBarValueChanged(int paneID, int barID, int v)
    {
    }

    public void scrollBarShown(int x, int y)
    {
    }

    public void scrollBarHidden(int x, int y)
    {
    }

    public void hideScrollBar(int paneID, int barID)
    {
        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
        p.hideScrollBar(barID);
    }

    public void showScrollBar(int paneID, int barID)
    {
         ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
         p.showScrollBar(barID);
    }

    public void changeScrollbarValue(int p1, int p2, int p3)
    {
    }

    public Vector panes;

    public void setPath(String p)
    {
        this.path=p;
//        this.fileNameField.setText(p);
        this.filePathField.setText(p);
    }

    public String path;

    private JFileChooser fileChooser;

    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(id));
        t.setTextAt(pos,s);
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        if(this.listener==null) return true;
        return this.listener.isControlledByLocalUser();
        // return true;
    }

    public void sendEvent(String x)
    {
        if(this.listener==null) return;
	    this.listener.sendFileDialogMessage("fdialog("+id+")."+x);
    }

    public boolean isShowingRmouse()
    {
//        return true;
         return this.listener.isShowingRmouse();
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.moveMouse(x,y);
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
    }
    
    public void exitMouseOnTheText(int id, int x, int y)
    {
        this.fileNameField.exitMouse();
    }
    
    public void enterMouseOnTheText(int id, int x, int y)
    {
        this.fileNameField.enterMouse(x,y);
    }
    
    public void mouseExitAtTheText(int id, int x, int y)
    {
    }
    
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }
    
    public String eventBlockName;

    public void pressMouseOnTheText(int id, int p,int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.pressMouse(p,x,y);
    }

    public void mousePressedAtTextArea(int id, int p,int x, int y)
    {
    }

    public void setSubName(String n)
    {
    }

    public String getSubName()
    {
        return this.promptLabel.getText();
    }

    public String getFilePath(){
    	return this.filePathField.getText();
    }

    public String getFileName(){
    	return this.fileNameField.getText();
    }
    
    public String dialogName;

    public void setDialogName(String s)
    {
        this.dialogName=s;
    }

    public Vector dialogs;

    public void setVector(Vector v)
    {
        dialogs=v;
    }

    public int id;

    public void setID(int i)
    {
        id=i;
    }

    public Vector texts;

    public void clickMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void dragMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.dragMouse(position,x,y);
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        this.listener.sendFileDialogMessage("fdialog("+id+").txt.kdn("+i+","+p+","+key+")\n");
    }

	public void keyIsPressedAtATextArea(int i, int p, int key)
	{
		// This method is derived from interface FrameWithControlledTextAreas
		// to do: code goes here
//		  this.listener.sendFileDialogMessage("fdialog("+id+").txt.kdn("+i+","+p+","+key+")\n");
	}

     public void mouseClickedAtTextArea(int i, int p,int x,int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        if(!this.isShowing()) return;
//        this.listener.sendFileDialogMessage("fdialog("+id+").txt.mdn("+i+","+p+")\n");
    }

    public void mouseDraggedAtTextArea(int i, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        this.listener.sendFileDialogMessage("fdialog("+id+").txt.mdg("+i+","+position+")\n");
    }

    public void mouseReleasedAtTextArea(int i, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        this.listener.sendFileDialogMessage("fdialog("+id+").txt.mrl("+i+","+position+")\n");
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.releaseMouse(position,x,y);
   }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.typeKey(p,key);
    }

    public void clickButton(int i)
    {
        SelectedButton t=(SelectedButton)(this.buttons.elementAt(i));
        t.click();
		if(i==this.chooserButton.getID()) return;
        this.mouseClickedAtButton(i);
    }

    public void unfocusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.unFocus();
    }

    public void focusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.focus();
    }

    public void mouseClickedAtButton(int i)
    {
//        if(!this.isShowing()) return;
        SelectedButton t=(SelectedButton)(this.buttons.elementAt(i));
        if(i==this.actionButton.getID()) {
//            this.actionButton_actionPerformed_Interaction1(null);
    		this.listener.whenActionButtonPressed(this);
            this.setVisible(false);
            return;
        }
        else
        if(i==this.cancelButton.getID()) {
            // this.cancelButton_actionPerformed_Interaction1(null);
		    this.listener.whenCancelButtonPressed(this);
		    this.setVisible(false);
		}
		else
		if(i==this.clearButton.getID()){
		    this.fileNameField.setText("");
		    repaint();
		}
		if(i==this.chooserButton.getID()){
		    if(!this.isControlledByLocalUser()) return;
		    if(this.fileChooser==null) return;
		    if(this.path!=null)
    		    this.fileChooser.setCurrentDirectory(new File(this.path));
    		this.fileChooser.setFileSelectionMode(fileChooser.FILES_AND_DIRECTORIES);
            int returnVal = this.fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = this.fileChooser.getSelectedFile();
                String fn=file.toString();
                String fx=file.getName();
                //This is where a real application would open the file.
//                this.fileNameField.setText(fn);
                this.path=file.getParent();
                this.filePathField.setText(this.path);
                this.fileNameField.setText(fx);
                this.fileNameField.ControlledTextArea_textPasted();
            }	    
		    
		}

    }

    public void mouseExitedAtButton(int i)
    {
//	    this.listener.sendFileDialogMessage("fdialog("+id+").btn.exit("+i+")\n");
//	    this.unfocusButton(i);
    }

    public void mouseEnteredAtButton(int i)
    {
//	    this.listener.sendFileDialogMessage("fdialog("+id+").btn.enter("+i+")\n");
//	    this.focusButton(i);
    }

    String separator;
    
    public void setSeparator(String s){
    	this.separator=s;
    }
    
    public String getText()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
//        String separator=""+System.getProperty("file.separator");
        String x=this.filePathField.getText()+this.separator+
                 this.fileNameField.getText();
        return x;
    }

    public String getDialogName()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
//        return this.promptLabel.getText();
        return this.dialogName;
    }

    public DialogListener listener;

    public void setListener(DialogListener l)
    {
        this.listener=l;
    }

	public JFileDialog()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(633,120);
		setVisible(false);
		actionButton.setText("action");
		actionButton.setActionCommand("action");
		actionButton.setSelected(true);
		getContentPane().add(actionButton);
		actionButton.setBackground(new java.awt.Color(204,204,204));
		actionButton.setForeground(java.awt.Color.black);
		actionButton.setFont(new Font("Dialog", Font.BOLD, 12));
		actionButton.setBounds(252,84,108,24);
		promptLabel.setText("prompt");
		getContentPane().add(promptLabel);
		promptLabel.setBackground(new java.awt.Color(204,204,204));
		promptLabel.setForeground(new java.awt.Color(102,102,153));
		promptLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		promptLabel.setBounds(12,48,120,24);
		titleLabel.setText("title of this dialog");
		getContentPane().add(titleLabel);
		titleLabel.setBackground(new java.awt.Color(204,204,204));
		titleLabel.setForeground(new java.awt.Color(102,102,153));
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		titleLabel.setBounds(72,12,168,24);
		cancelButton.setText("cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.setSelected(true);
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(360,84,132,24);
		clearButton.setText("clear");
		clearButton.setActionCommand("clear");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(144,84,108,24);
		chooserButton.setText("chooser");
		chooserButton.setActionCommand("chooser");
		chooserButton.setToolTipText("open file chooser");
		getContentPane().add(chooserButton);
		chooserButton.setBackground(new java.awt.Color(204,204,204));
		chooserButton.setForeground(java.awt.Color.black);
		chooserButton.setFont(new Font("Dialog", Font.BOLD, 12));
		chooserButton.setBounds(492,84,132,24);
		controlledScrollPane1.setOpaque(true);
		getContentPane().add(controlledScrollPane1);
		controlledScrollPane1.setBounds(360,36,264,48);
		fileNameField.setSelectionColor(new java.awt.Color(204,204,255));
		fileNameField.setSelectedTextColor(java.awt.Color.black);
		fileNameField.setCaretColor(java.awt.Color.black);
		fileNameField.setDisabledTextColor(new java.awt.Color(153,153,153));
		controlledScrollPane1.getViewport().add(fileNameField);
		fileNameField.setBackground(java.awt.Color.lightGray);
		fileNameField.setForeground(java.awt.Color.black);
		fileNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		fileNameField.setBounds(0,0,260,44);
		controlledScrollPane2.setOpaque(true);
		getContentPane().add(controlledScrollPane2);
		controlledScrollPane2.setBounds(144,36,216,48);
		controlledScrollPane2.getViewport().add(filePathField);
		filePathField.setBackground(java.awt.Color.white);
		filePathField.setForeground(java.awt.Color.black);
		filePathField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		filePathField.setBounds(0,0,212,44);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		actionButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		clearButton.addActionListener(lSymAction);
		chooserButton.addActionListener(lSymAction);
		//}}
		
		buttons=new Vector();
		buttons.addElement(this.actionButton);
		buttons.addElement(this.cancelButton);
		buttons.addElement(this.clearButton);
		buttons.addElement(this.chooserButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
		    b.setFrame(this);
//    		b.addActionListener(lSymAction);		    
		    b.setID(i);
		}
		
		texts=new Vector();
		texts.addElement(this.fileNameField);
		fileNameField.setFrame(this);
		fileNameField.setID(0);
		
		this.panes=new Vector();
        panes.addElement(this.controlledScrollPane1);
        for(int i=0;i<1;i++){
            ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
            p.setFrame(this);
            p.setID(i);
        }
		
//		this.fileChooser= new JFileChooser();
        /*
        try{
    		this.fileChooser= new JFileChooser();
        }
        catch(Exception e){
        	System.out.println(e.toString());
        	this.fileChooser=null;
        }
        this.setFileChooser(this.fileChooser);
       */
	}
	
	public void setFileChooser(JFileChooser fc){
		this.fileChooser=fc;
	}

	public JFileDialog(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
	}

	static public void main(String args[])
	{
		(new JFileDialog()).setVisible(true);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	ControlledButton actionButton = new ControlledButton();
	javax.swing.JLabel promptLabel = new javax.swing.JLabel();
	javax.swing.JLabel titleLabel = new javax.swing.JLabel();
	ControlledButton cancelButton = new ControlledButton();
	public ControlledButton clearButton = new ControlledButton();
	ControlledButton chooserButton = new ControlledButton();
	ControlledScrollPane controlledScrollPane1 = new ControlledScrollPane();
	ControlledTextArea fileNameField = new ControlledTextArea();
	ControlledScrollPane controlledScrollPane2 = new ControlledScrollPane();
	ControlledTextArea filePathField = new ControlledTextArea();
	//}}

	//{{DECLARE_MENUS
	//}}

    Vector buttons= new Vector();

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == actionButton)
				actionButton_actionPerformed(event);
			else if (object == cancelButton)
				cancelButton_actionPerformed(event);
			else if (object == clearButton)
				clearButton_actionPerformed(event);
			else if (object == chooserButton)
				chooserButton_actionPerformed(event);
		}
	}

	void actionButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		actionButton_actionPerformed_Interaction1(event);
	}

	void actionButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	void cancelButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		cancelButton_actionPerformed_Interaction1(event);
	}

	void cancelButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    /*
		if(!this.isShowing()) return;
		if(this.listener.isControlledByLocalUser()){
		    this.listener.whenCancelButtonPressed(this);
		}
		*/
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosed(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == JFileDialog.this)
				JFileDialog_windowClosed(event);
		}

		public void windowDeactivated(java.awt.event.WindowEvent event)
		{
		}
	}

	void JFileDialog_windowClosed(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		JFileDialog_windowClosed_Interaction1(event);
	}

	void JFileDialog_windowClosed_Interaction1(java.awt.event.WindowEvent event)
	{
	    
		try {
			this.setVisible(false);
		} catch (java.lang.Exception e) {
		}
		
	}

	void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    /*
		try {
			// clearButton Show the ControlledButton
//			clearButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
		*/
	}

	void chooserButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		chooserButton_actionPerformed_Interaction1(event);
	}

	void chooserButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			chooserBotton.show();
		} catch (java.lang.Exception e) {
		}
	}
	/* (?¿½?¿½ Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsReleasedAtTextArea(int, int)
	 */
	public void keyIsReleasedAtTextArea(int id, int p, int code) {
		// TODO ?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½ê‚½?¿½?¿½?¿½\?¿½b?¿½h?¿½E?¿½X?¿½^?¿½u

	}

	/* (?¿½?¿½ Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#releaseKey(int, int)
	 */
	public void releaseKey(int i, int p, int code) {
		// TODO ?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½ê‚½?¿½?¿½?¿½\?¿½b?¿½h?¿½E?¿½X?¿½^?¿½u
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.releaseKey(p, code);

	}
	public void pressKey(int i, int p, int code){
	}

}