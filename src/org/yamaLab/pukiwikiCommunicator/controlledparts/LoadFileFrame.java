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

public class LoadFileFrame extends ControlledFrame implements DialogListener, EditDialog, FrameWithControlledButton
{
    public boolean isDirectOperation()
    {
        if(this.listener==null) return true;
        return this.listener.isDirectOperation();
    }

    public void setUserPath(String p)
    {
        this.userPath=p;
    }

    public void setCommonPath(String p)
    {
        this.commonPath=p;
    }
    
    public void setWords(){
    	loadFromFileButton.setText(getLclTxt("load_from_common_file"));
		loadFromUserFileButton.setText(getLclTxt("load_from_user_file"));
		loadFromWebButton.setText(getLclTxt("load_from_web"));
		cancelButton.setText(getLclTxt("cancel"));
    }

    public String userPath;

    public String commonPath;

    public boolean isShowingRmouse()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return this.listener.isShowingRmouse();
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        if(this.listener==null) return true;
        return this.listener.isControlledByLocalUser();
    }

    public void sendEvent(String x)
    {
        this.sendFileDialogMessage(x);
    }

    public String eventBlockName;

    public Vector dialogs;

    public String subName;

    public String getSubName()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
        return subName;
    }

    public String getText()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
        return null;
    }

    public void setSubName(String n)
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
        subName=n;
    }

    public String getDialogName()
    {
        return dialogName;
    }

    public String dialogName;

    public void setDialogName(String n)
    {
        this.fileDialog.setDialogName(n);
    }

    public JFileDialog fileDialog;

	public void setSeparator(String s){
		fileDialog.setSeparator(s);
	}
    public void action(int i)
    {
        fileDialog.setID(0);
        fileDialog.setListener(this);
        if(i==0){ // load from web button
          fileDialog.promptLabel.setText("url:");
          fileDialog.titleLabel.setText("input fig from web");
          fileDialog.actionButton.setText("open");
          fileDialog.show();
       }
        else
        if(i==1){ // load from file button
          fileDialog.promptLabel.setText("input common file name:");
          fileDialog.titleLabel.setText("input fig from common file");
          fileDialog.setPath(this.commonPath);
          fileDialog.actionButton.setText("open");
          fileDialog.show();
        }
        else
        if(i==2){ // load from user file button
          fileDialog.promptLabel.setText("input user file name:");
          fileDialog.titleLabel.setText("input fig from user file");
          fileDialog.setPath(this.userPath);
          fileDialog.actionButton.setText("open");
          fileDialog.show();
        }
        else
        if(i==3){ // load from user file button
            this.hide();
        }
    }

    public DialogListener listener;

    public void setListener(DialogListener l)
    {
        this.listener=l;
    }

    public void setFileChooser(JFileChooser fc){
		if(this.fileDialog==null) return;
		this.fileDialog.setFileChooser(fc);    	
    }
    
    public Vector texts;

    public Vector buttons;

    String urlName;

    public String getUrl()
    {
        return null;
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
 		this.action(i);
    }

    public void clickMouseOnTheText(int i, int p)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void dragMouseOnTheText(int id, int position)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.focus();
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public Vector getDialogs()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return this.dialogs;
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        System.out.println("mouse clicked at load file frame");
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
 	    this.action(i);
 	
    }

    public void mouseClickedAtTextArea(int i, int p)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseDraggedAtTextArea(int id, int position)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
       this.sendFileDialogMessage("btn.enter("+i+")\n");
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        this.sendFileDialogMessage("btn.exit("+i+")\n");
    }

    public void mouseReleasedAtTextArea(int id, int position)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void releaseMouseOnTheText(int id, int position)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        if(listener==null) return;
        listener.sendFileDialogMessage("loadfd."+m);
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        SelectedButton button=(SelectedButton)(buttons.elementAt(i));
        button.unFocus();
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        if(listener==null) { fileDialog.hide(); this.hide(); return;}
        this.listener.whenActionButtonPressed(d);
        this.hide();
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        if(listener==null){fileDialog.hide(); this.hide(); return;}
        this.listener.whenCancelButtonPressed(d);
        this.hide();
    }

	public LoadFileFrame()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		setSize(204,198);
		setVisible(false);
		loadFromWebButton.setText("load from Web");
		loadFromWebButton.setActionCommand("load");
		getContentPane().add(loadFromWebButton);
		loadFromWebButton.setBackground(new java.awt.Color(204,204,204));
		loadFromWebButton.setForeground(java.awt.Color.black);
		loadFromWebButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromWebButton.setBounds(24,36,168,24);
		loadFromFileButton.setText("load from common file");
		loadFromFileButton.setActionCommand("load from file");
		getContentPane().add(loadFromFileButton);
		loadFromFileButton.setBackground(new java.awt.Color(204,204,204));
		loadFromFileButton.setForeground(java.awt.Color.black);
		loadFromFileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromFileButton.setBounds(24,60,168,24);
		JLabel1.setText("DSR/LoadFile");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(24,12,144,24);
		loadFromUserFileButton.setText("load from user file");
		loadFromUserFileButton.setActionCommand("load from user file");
		getContentPane().add(loadFromUserFileButton);
		loadFromUserFileButton.setBackground(new java.awt.Color(204,204,204));
		loadFromUserFileButton.setForeground(java.awt.Color.black);
		loadFromUserFileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromUserFileButton.setBounds(24,84,168,24);
		cancelButton.setText("cancel");
		cancelButton.setActionCommand("cancel");
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(24,108,168,24);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		loadFromWebButton.addActionListener(lSymAction);
		loadFromFileButton.addActionListener(lSymAction);
		loadFromUserFileButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//}}
		
		buttons=new Vector();
		buttons.addElement(loadFromWebButton);
		buttons.addElement(loadFromFileButton);
		buttons.addElement(loadFromUserFileButton);
		buttons.addElement(cancelButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setID(i);
		    b.setFrame(this);
		}
		
        fileDialog=new JFileDialog();
//        fileDialog.setBounds(this.getInsets().left+100, this.getInsets().right+100,
//                             400,100);                       
        this.dialogs=new Vector();
        dialogs.addElement(fileDialog);
        fileDialog.setID(0);
        fileDialog.setVector(dialogs);
      
	}

	public LoadFileFrame(String sTitle)
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
		(new LoadFileFrame()).setVisible(true);
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
	ControlledButton loadFromWebButton = new ControlledButton();
	ControlledButton loadFromFileButton = new ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	ControlledButton loadFromUserFileButton = new ControlledButton();
	ControlledButton cancelButton = new ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == loadFromWebButton)
				loadFromWebButton_actionPerformed(event);
			else if (object == loadFromFileButton)
				loadFromFileButton_actionPerformed(event);
			else if (object == loadFromUserFileButton)
				loadFromUserFileButton_actionPerformed(event);
			else if (object == cancelButton)
				cancelButton_actionPerformed(event);
			
		}
	}

	void loadFromWebButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadFromWebButton_actionPerformed_Interaction1(event);
	}

	void loadFromWebButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			loadFromWebButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void loadFromFileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadFromFileButton_actionPerformed_Interaction1(event);
	}

	void loadFromFileButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			loadFromFileButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void loadFromUserFileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadFromUserFileButton_actionPerformed_Interaction1(event);
	}

	void loadFromUserFileButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			loadFromUserFileButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void cancelButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		cancelButton_actionPerformed_Interaction1(event);
	}

	void cancelButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// cancelButton Show the ControlledButton
			cancelButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == LoadFileFrame.this)
				LoadFileFrame_windowClosing(event);
		}
	}

	void LoadFileFrame_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		LoadFileFrame_windowClosing_Interaction1(event);
	}

	void LoadFileFrame_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		int i=4; // cancel button
        this.sendFileDialogMessage("btn.click("+i+")\n");
 	    this.action(i);
	}

	public String getFilePath() {
		// TODO ?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½ê‚½?¿½?¿½?¿½\?¿½b?¿½h?¿½E?¿½X?¿½^?¿½u
		return null;
	}

	public String getFileName() {
		// TODO ?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½?¿½ê‚½?¿½?¿½?¿½\?¿½b?¿½h?¿½E?¿½X?¿½^?¿½u
		return null;
	}
}