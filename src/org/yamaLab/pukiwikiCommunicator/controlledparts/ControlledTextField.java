package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JScrollBar;

public class ControlledTextField extends javax.swing.JTextField
{
    public void ControlledTextField_textPasted()
    {
    	   this.frame.sendEvent("fld.set("+this.id+","
    	                       +this.getStrConst()+")");        
    }

    FrameWithControlledTextField frame;
    JScrollBar horizontalScrollBar;
    JScrollBar virticalScrollBar;
    RemoteMouse rmouse;
    String fileSeparator;
    boolean crReceived;
    int selectionStartPoint;
    int selectionEndPoint;
    String tempString;
    int id;

    public String getStrConst()
    {
        String s=this.getText();
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){
            	sx=sx+'\\'; sx=sx+c; i++;
//                 if(i>=len) break;
//                 c=s.charAt(i); sx=sx+c; i++; 
            }
			else
		    if(c=='\b'){
				sx=sx+'\\'; sx=sx+'b'; i++;
			}
			else
			if(c=='\f'){
				sx=sx+'\\'; sx=sx+'f'; i++;
			}
            else
            if((int)c==10){
                sx=sx+"\\n"; i++;
//                if(i>=len) break;
//                c=s.charAt(i);
 //               if((int)c==13) i++;
            }
            else
            if((int)c==13){
                sx=sx+"\\n"; i++;
//                if(i>=len) break;
//                c=s.charAt(i);
//                if((int)c==10) i++;
            }
			else
			if(c=='\t'){
				sx=sx+'\\'; sx=sx+"t"; i++;
			}
            else
            { sx=sx+c; i++; }
//            System.out.println(sx);
        }

        // Unicode ?¿½?¿½ S-JIS Code ?¿½É•ÏŠï¿½
//	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
//        String rtn=new String(sjisCode,0);
        String rtn="\""+sx+"\"";

        return rtn;
    }
        
    public void exitMouse()
    {
        this.rmouse.setVisible(false);
        this.repaint();
    }

    public void enterMouse(int x, int y)
    {
        if(this.frame.isShowingRmouse()) this.rmouse.setVisible(true);
        else this.rmouse.setVisible(false);
        this.rmouse.resetColor();
        rmouse.move(x,y);
        this.repaint();
   }

    public void paint(Graphics g)
    {
        super.paint(g);
        rmouse.paint(g);
    }
    
    public void moveMouse(int x, int y)
    {
        rmouse.move(x,y);
         repaint();
    }

    public void pressMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        setCaretPosition(position);
        this.setSelectionStart(position);
		selectionStartPoint=position;
		selectionEndPoint=position;
//	    tempString="";
        repaint();
    }
    
    public void releaseMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        if((this.getText()).equals("")) return;
        this.setSelectionEnd(position);
        selectionEndPoint=position;
        if(selectionStartPoint==position) return;
        this.select(this.selectionStartPoint,this.selectionEndPoint);
        tempString=this.getSelectedText();
//        System.out.println("mouse released at carat "+position);
        this.rmouse.resetColor();
        repaint();
        
    }

    public void dragMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        this.setSelectionEnd(position);
        selectionEndPoint=position;
        this.select(this.selectionStartPoint,this.selectionEndPoint);
        repaint();
    }

    public void clickMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        setCaretPosition(position);
        this.setSelectionStart(position);
		selectionStartPoint=position;
		selectionEndPoint=position;
//	    tempString="";
        repaint();
        try{
            Thread.sleep(80);
        }
        catch(InterruptedException e){}
        this.rmouse.resetColor();
        repaint();
    }

    public void typeKey(int position, int key)
    {
        String s=getText();
        String pre="";
        String post="";
        int length=s.length();
//        System.out.println("s="+s+" len="+length+" position="+position+" key="+key);
        if(key==(int)('\r')||key==(int)('\n')){     // return, enter, new-line, CR, LF
//            crReceived=true;
            if(position>length-1){
                pre=s;
                post="";
            }
            else{
                pre=s.substring(0,position-1);
                post=s.substring(position-1,length);
            }
//            setText(pre+(char)key+post);
            setText(pre+"\n"+post);
            repaint();
            return;
        }
        crReceived=false;
        if(key==8){    // when  back space key
             if(length<=0) {setText(""); setCaretPosition(0); return; }
             if(this.fileSeparator.equals("/")){ position--;}
//             position--;
             if(position<1) pre="";
             else  pre=s.substring(0,position);
             if(pre.length()<=0) post=s.substring(1,length);
             else 
             if(position+1>length){
                post="";
                pre=s.substring(0,length-1);
                position=length-1;
             }
             else
               post=s.substring(position+1,length);
             setText(pre+post);
             setCaretPosition(position);
        }
        else{
             if(position>=length) pre=s;
             else
             if(position>0) pre=s.substring(0,position); 
             else {pre=""; position=0;}
             if(position<length)
                 post=s.substring(position,length);
             setText(pre+(char)key+post);
             setCaretPosition(position+1);
        }
        repaint();
        
    }

    public void setID(int i)
    {
        id=i;
    }

    public void setFrame(FrameWithControlledTextField f)
    {
        frame=f;
   
    }

	public ControlledTextField()
	{

		//{{INIT_CONTROLS
		setSelectionColor(new java.awt.Color(204,204,255));
		setSelectedTextColor(java.awt.Color.black);
		setCaretColor(java.awt.Color.black);
		setDisabledTextColor(new java.awt.Color(153,153,153));
		setBackground(java.awt.Color.lightGray);
		setForeground(java.awt.Color.black);
		setFont(new Font("SansSerif", Font.PLAIN, 12));
		setSize(0,0);
		//}}

		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		SymAction lSymAction = new SymAction();
		this.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		//}}
		
		fileSeparator=System.getProperty("file.separator");
		this.rmouse=new RemoteMouse();
		rmouse.setVisible(false);
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseReleased(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mouseReleased(event);
		}

		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mousePressed(event);
		}

		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mouseEntered(event);
		}
	}

	public void ControlledTextField_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		this.setBackground(Color.white);
			 
		ControlledTextField_mouseEntered_Interaction1(event);
	}

	public void ControlledTextField_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		this.setBackground(Color.lightGray);	 
			 
		ControlledTextField_mouseExited_Interaction1(event);
	}

	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyTyped(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_keyTyped(event);
		}
	}

	public void ControlledTextField_keyTyped(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
        if(frame==null) return;
		if(this.frame.isControlledByLocalUser()){
		    int p=this.getCaretPosition();
		    int key=event.getKeyChar();
    		frame.sendEvent("fld.kdn("+id+","+p+","+key+")\n");
    		frame.keyIsTypedAtATextArea(id, p, key);
	    }
//		System.out.println("keydown:"+event.getKeyChar());
			 
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_actionPerformed(event);
		}
	}

	public void ControlledTextField_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_actionPerformed_Interaction1(event);
	}

	public void ControlledTextField_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
        if(frame==null) return;
		if(this.frame.isControlledByLocalUser()){
    		frame.sendEvent("fld.etr("+id+")\n");
    		frame.enterPressed(id);
	    }
	}

	void ControlledTextField_mouseEntered_Interaction1(java.awt.event.MouseEvent event)
	{
		if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
            int x=event.getX(); int y=event.getY();
            frame.sendEvent("fld.ment("+id+","+x+","+y+")\n");
            frame.mouseEnteredAtTheText(id,x,y);
		    this.enterMouse(x,y);
		}
	}

	void ControlledTextField_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
	    if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
            int x=event.getX(); int y=event.getY();
            frame.sendEvent("fld.mxit("+id+","+x+","+y+")\n");
            frame.mouseExitAtTheText(id,x,y);
            this.exitMouse();
        }
	}

	public void ControlledTextField_mousePressed(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_mousePressed_Interaction1(event);
	}

	void ControlledTextField_mousePressed_Interaction1(java.awt.event.MouseEvent event)
	{
        if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
	    	selectionStartPoint=this.getCaretPosition();
	        int x=event.getX();
            int y=event.getY();
    		frame.sendEvent("fld.mdn("+id+","+selectionStartPoint+","+x+","+y+")\n");
	        frame.mouseClickedAtTextArea(id,selectionStartPoint,x,y);
//	    tempString="";
            this.clickMouse(selectionStartPoint,x,y);
        }
	}

	public void ControlledTextField_mouseReleased(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_mouseReleased_Interaction1(event);
	}

	void ControlledTextField_mouseReleased_Interaction1(java.awt.event.MouseEvent event)
	{
	    if(frame==null) return;
	   if(this.frame.isControlledByLocalUser()){
	       int x=event.getX();
           int y=event.getY();
	       int position=this.getSelectionEnd();
    	   frame.sendEvent("fld.mrl("+id+","+position+","+x+","+y+")\n");
           frame.mouseReleasedAtTextArea(id,position,x,y);
           this.releaseMouse(position,x,y);
       }
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_focusLost(event);
		}

		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_focusGained(event);
		}
	}

	public void ControlledTextField_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_focusGained_Interaction1(event);
	}

	void ControlledTextField_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
	}

	public void ControlledTextField_focusLost(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_focusLost_Interaction1(event);
	}

	void ControlledTextField_focusLost_Interaction1(java.awt.event.FocusEvent event)
	{
	}

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mouseMoved(event);
		}

		public void mouseDragged(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextField.this)
				ControlledTextField_mouseDragged(event);
		}
	}

	public void ControlledTextField_mouseDragged(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_mouseDragged_Interaction1(event);
	}

	void ControlledTextField_mouseDragged_Interaction1(java.awt.event.MouseEvent event)
	{
       if(frame==null) return;
       if(this.frame.isControlledByLocalUser()){
	       int position=this.getSelectionEnd();
           int x=event.getX();
           int y=event.getY();
           frame.sendEvent("fld.mdg("+id+","+position+","+x+","+y+")\n");
           frame.mouseDraggedAtTextArea(id,position,x,y);
           this.dragMouse(position,x,y);
       }
	}

	void ControlledTextField_mouseMoved(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextField_mouseMoved_Interaction1(event);
	}

	void ControlledTextField_mouseMoved_Interaction1(java.awt.event.MouseEvent event)
	{
	    if(frame==null) return;
       if(this.frame.isControlledByLocalUser()){
           int x=event.getX();
           int y=event.getY();
           frame.sendEvent("fld.mm("+id+","+x+","+y+")\n");
           frame.mouseMoveAtTextArea(id,x,y);
           this.moveMouse(x,y);
       }
	}
}