package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
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
public class FileFrame extends ControlledFrame implements DialogListener, FrameWithControlledButton    
{
    public boolean isDirectOperation()
    {
        return true;
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
    	this.loadFromFileButton.setText(getLclTxt("load_from_common_file"));
    	this.loadFromUserFileButton.setText(getLclTxt("load_from_user_file"));
    	this.loadFromWebButton.setText(getLclTxt("load_from_web"));
    	this.saveToCommonButton.setText(getLclTxt("*save_to_common_file"));
    	this.saveToFileButton.setText(getLclTxt("save_to_user_file"));
    	this.cancelButton.setText(getLclTxt("cancel"));
    }

    String userPath;

    String commonPath;

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
        return listener.isControlledByLocalUser();
    }

    public void sendEvent(String x)
    {
        listener.sendFileDialogMessage(x);
    }

    public DialogListener listener;

    public void setListener(DialogListener l)
    {
        listener=l;
    }

    public void exitThis()
    {
//        draw.recordMessage("exit draw");
        this.setVisible(false);
    }

    public JFileDialog fileDialog;

    public Vector dialogs;

    public Vector getDialogs()
    {
        return dialogs;
    }

    public void sendFileDialogMessage(String m)
    {
        if(this.listener==null) return;
        listener.sendFileDialogMessage(m);
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        listener.whenActionButtonPressed(d);
        this.setVisible(false); //hide();
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        this.setVisible(false); // hide();
    }

    public void action(int i)
    {
    	if(fileDialog==null) return;
    	
        fileDialog.setID(0);
        fileDialog.setListener(this);
        
       if(i==0){ // load from web button
    	  fileDialog.setSeparator("/");
          fileDialog.setDialogName("url:");
          fileDialog.promptLabel.setText("url:");
          fileDialog.titleLabel.setText("input fig from web");
          fileDialog.actionButton.setText("open");
          fileDialog.setPath("");
          fileDialog.show();
          this.hide();
       }
        else
        if(i==1){ // load from file button
          fileDialog.setDialogName("input common file name:");
          fileDialog.promptLabel.setText("input common file name:");
          fileDialog.titleLabel.setText("input fig from common file");
          fileDialog.actionButton.setText("open");
          fileDialog.setPath(this.commonPath);
//          fileDialog.setListener(this);
          fileDialog.setVisible(true);
          this.setVisible(false);
        }
        else
        if(i==2){ // load from user file button
          fileDialog.setDialogName("input user file name:");
          fileDialog.promptLabel.setText("input user file name:");
          fileDialog.titleLabel.setText("input fig from user file");
          fileDialog.actionButton.setText("open");
          fileDialog.setPath(this.userPath);
//          fileDialog.setListener(this);
          fileDialog.setVisible(true);
          this.setVisible(false);
        }
        else
        if(i==3){ // save to file button
           fileDialog.setDialogName("output user file name:");
           fileDialog.promptLabel.setText("output user file name:");
           fileDialog.titleLabel.setText("save the fig to user file");
           fileDialog.actionButton.setText("save");
           fileDialog.setPath(this.userPath);
//           fileDialog.setListener(this);
           fileDialog.setVisible(true);
           this.setVisible(false);
        }
        else
        if(i==4){ // cancel button
           this.setVisible(false);
        }
        else
        if(i==5){ // save to common button
           fileDialog.setDialogName("output common file name:");
           fileDialog.promptLabel.setText("output common file name:");
           fileDialog.titleLabel.setText("save the fig to common file");
           fileDialog.actionButton.setText("save");
           fileDialog.setPath(this.commonPath);
//           fileDialog.setListener(this);
           fileDialog.setVisible(true);
           this.setVisible(false);
        }
    }
/*
    public void setDraw(DrawFrame d)
    {
        draw=d;
    }
*/
//    public DrawFrame draw;

    public Vector buttons;

	public FileFrame()
	{
		//{{INIT_CONTROLS
		setTitle("A Simple Frame");
		getContentPane().setLayout(null);
		this.setSize(230, 232);
		setVisible(false);
		loadFromWebButton.setText("load from Web");
		loadFromWebButton.setActionCommand("load");
		getContentPane().add(loadFromWebButton);
		loadFromWebButton.setBackground(new java.awt.Color(204,204,204));
		loadFromWebButton.setForeground(java.awt.Color.black);
		loadFromWebButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromWebButton.setBounds(12, 36, 200, 24);
		loadFromFileButton.setText("load from common file");
		loadFromFileButton.setActionCommand("load from file");
		getContentPane().add(loadFromFileButton);
		loadFromFileButton.setBackground(new java.awt.Color(204,204,204));
		loadFromFileButton.setForeground(java.awt.Color.black);
		loadFromFileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromFileButton.setBounds(12,60,200,24);
		saveToFileButton.setText("save to user file");
		saveToFileButton.setActionCommand("save");
		getContentPane().add(saveToFileButton);
		saveToFileButton.setBackground(new java.awt.Color(204,204,204));
		saveToFileButton.setForeground(java.awt.Color.black);
		saveToFileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		saveToFileButton.setBounds(12,132,200,24);
		JLabel1.setText("DSR/Draw/File");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(24,12,144,24);
		loadFromUserFileButton.setText("load from user file");
		loadFromUserFileButton.setActionCommand("load from user file");
		getContentPane().add(loadFromUserFileButton);
		loadFromUserFileButton.setBackground(new java.awt.Color(204,204,204));
		loadFromUserFileButton.setForeground(java.awt.Color.black);
		loadFromUserFileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadFromUserFileButton.setBounds(12,108,200,24);
		cancelButton.setText("cancel");
		cancelButton.setActionCommand("cancel");
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(12,156,200,24);
		saveToCommonButton.setText("*save to common file");
		saveToCommonButton.setActionCommand("load from file");
		getContentPane().add(saveToCommonButton);
		saveToCommonButton.setBackground(new java.awt.Color(204,204,204));
		saveToCommonButton.setForeground(java.awt.Color.black);
		saveToCommonButton.setFont(new Font("Dialog", Font.BOLD, 12));
		saveToCommonButton.setBounds(12,84,200,24);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		loadFromWebButton.addActionListener(lSymAction);
		loadFromFileButton.addActionListener(lSymAction);
		saveToFileButton.addActionListener(lSymAction);
		loadFromUserFileButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		saveToCommonButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
		
		buttons=new Vector();
		buttons.addElement(loadFromWebButton);
		buttons.addElement(loadFromFileButton);
		buttons.addElement(loadFromUserFileButton);
		buttons.addElement(saveToFileButton);
		buttons.addElement(cancelButton);
		buttons.addElement(saveToCommonButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setID(i);
		    b.setFrame(this);
		}
		try{
           fileDialog=new JFileDialog();
           fileDialog.setListener(this);
           this.dialogs=new Vector();
           dialogs.addElement(fileDialog);
           fileDialog.setID(0);
           fileDialog.setVector(dialogs);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
//        fileDialog.setBounds(this.getInsets().left+100, this.getInsets().right+100,
//                             400,100);
                             
//        String separator=""+System.getProperty("file.separator");
//        fileDialog.setSeparator(separator);
	}
	
	public void setFileChooser(JFileChooser fc){
		if(this.fileDialog==null) return;
		this.fileDialog.setFileChooser(fc);
	}
	
	public void setSeparator(String s){
		if(this.fileDialog==null) return;
		fileDialog.setSeparator(s);
	}

	public FileFrame(String title)
	{
		this();
		setTitle(title);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
	super.setVisible(b);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == FileFrame.this)
			FileFrame_WindowClosing(event);
		}
	}

	void FileFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		hide();		 // dispose of the Frame.
			 
		FileFrame_windowClosing_Interaction1(event);
	}
	//{{DECLARE_CONTROLS
	ControlledButton loadFromWebButton = new ControlledButton();
	ControlledButton loadFromFileButton = new ControlledButton();
	ControlledButton saveToFileButton = new ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	ControlledButton loadFromUserFileButton = new ControlledButton();
	ControlledButton cancelButton = new ControlledButton();
	ControlledButton saveToCommonButton = new ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
 		this.action(i);
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
          button.focus();
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        System.out.println("mouse clicked at filet frame");
//        this.sendFileDialogMessage("btn.click("+i+")\n");
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        this.action(i);
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        this.sendFileDialogMessage("btn.enter("+i+")\n");
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        this.sendFileDialogMessage("btn.exit("+i+")\n");
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//        button.controlledButton_mouseExited(null);
        button.unFocus();
    }


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == loadFromWebButton)
				loadFromWebButton_actionPerformed(event);
			else if (object == loadFromFileButton)
				loadFromFileButton_actionPerformed(event);
			else if (object == saveToFileButton)
				saveToFileButton_actionPerformed(event);
			else if (object == loadFromUserFileButton)
				loadFromUserFileButton_actionPerformed(event);
			else if (object == cancelButton)
				cancelButton_actionPerformed(event);
			else if (object == saveToCommonButton)
				saveToCommonButton_actionPerformed(event);
		}
	}

	void loadFromWebButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadFromWebButton_actionPerformed_Interaction2(event);
	}

	void loadFromWebButton_actionPerformed_Interaction2(java.awt.event.ActionEvent event)
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

	void saveToFileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		saveToFileButton_actionPerformed_Interaction2(event);
	}

	void saveToFileButton_actionPerformed_Interaction2(java.awt.event.ActionEvent event)
	{
		try {
//			saveToFileButton.show();
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

	void FileFrame_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
	}

	void cancelButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		cancelButton_actionPerformed_Interaction1(event);
	}

	void cancelButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	void saveToCommonButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		saveToCommonButton_actionPerformed_Interaction1(event);
	}

	void saveToCommonButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			saveToCommonButton.show();
		} catch (java.lang.Exception e) {
		}
	}
}