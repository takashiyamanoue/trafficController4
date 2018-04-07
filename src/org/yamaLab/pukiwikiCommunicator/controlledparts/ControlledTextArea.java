package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import java.awt.datatransfer.Clipboard;
import java.awt.event.*;

import javax.swing.TransferHandler;
import javax.swing.text.Caret;

public class ControlledTextArea extends javax.swing.JTextArea
{
	
	TransferHandler transferHandler;
	Caret caret;
	
	public void setTextAt(int pos, String s){
		String cs=this.getText();
		int len=cs.length();
		String head=cs.substring(0,pos);
        this.setText(head+s);
	}
	
    public void ControlledTextArea_textPasted()
    {
    	/*
    	   this.frame.sendEvent("txt.set("+this.id+","
    	                       +this.getStrConst()+")");        
    	*/
    	String s=this.getText();
    	int p=0;
    	int lengthAtOneTime=1200; // size of the sending text(byte) per one time
    	int len=s.length();
    	while(p<len){
    		String sx="";
			int left=len-p;
    		if(left>=lengthAtOneTime){
    			sx=s.substring(p,p+lengthAtOneTime);
				this.frame.sendEvent("txt.set("+this.id+","
						 +p+","+this.getStrConst(sx)+")\n");
    			p=p+lengthAtOneTime;
    		}
    		else{
    			sx=s.substring(p);
				this.frame.sendEvent("txt.set("+this.id+","
						 +p+","+this.getStrConst(sx)+")\n");
    			p=p+sx.length();
    		}
    	}
    }

    public String getStrConst(String s)
    {
//        String s=this.getText();
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
			if(c=='\''){
				sx=sx+'\\'; sx=sx+'\''; i++; }
			else
			if(c=='\"'){
				sx=sx+'\\'; sx=sx+'"'; i++; }
			else
			if(c=='\\'){
				sx=sx+'\\'; sx=sx+'\\'; i++;
//                if(i>=len) break;
//                c=s.charAt(i); sx=sx+c; i++; 
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
//	            if(i>=len) break;
//	            c=s.charAt(i);
//	            if((int)c==13) i++;
	        }
	        else
	        if((int)c==13){
	            sx=sx+"\\n"; i++;
//	            if(i>=len) break;
//	            c=s.charAt(i);
//	            if((int)c==10) i++;
	        }
	        else
			if(c=='\n'){
				sx=sx+'\\'; sx=sx+"n"; i++;
			}
			else
			if(c=='\r'){
				sx=sx+'\\'; sx=sx+"r"; i++;
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


    RemoteMouse rmouse;
    
    public void moveMouse(int x, int y)
    {
        rmouse.move(x,y);
         repaint();
    }

//    String fileSeparator;

    public void pressMouse(int position,int x, int y)
    {
    	this.setDragEnabled(true);
        int p=this.viewToModel(new Point(x,y));
        setCaretPosition(p);
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        this.setSelectionStart(p);
		selectionStartPoint=p;
		selectionEndPoint=p;
        repaint();
    }

    boolean crReceived;

    int selectionStartPoint;
    int selectionEndPoint;
    String tempString;

    public void releaseMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        if((this.getText()).equals("")) return;
        int p=this.viewToModel(new Point(x,y));
//        this.setSelectionEnd(position);
        this.setSelectionEnd(p);
        selectionEndPoint=p;
        if(selectionStartPoint==p) return;
        this.select(this.selectionStartPoint,this.selectionEndPoint);
        tempString=this.getSelectedText();
        this.rmouse.resetColor();
        repaint();
        
    }


    public void dragMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        int p=this.viewToModel(new Point(x,y));
        this.selectionEndPoint=p;
        int start=0;
        int end=0;        
        if(selectionEndPoint<selectionStartPoint){
        	start=this.selectionEndPoint;
        	end=this.selectionStartPoint;
        }
        else{
        	start=this.selectionStartPoint;
        	end=this.selectionEndPoint;
        }
		this.setSelectionStart(start);
		this.setSelectionEnd(end);
//		System.out.println("select:"+start+"-"+end);
        this.select(start,end);
//        String s=this.getSelectedText();
//        System.out.println("selected="+s);
        repaint();
    }

    public void clickMouse(int position,int x, int y)
    {
    	Point pxy=new Point(x,y);
    	int p=this.viewToModel(pxy);
    	setCaretPosition(p);
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        selectionStartPoint=p;
        selectionEndPoint=p;
		this.setDragEnabled(true);
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
		int selectedStart=this.getSelectionStart();
		int selectedEnd=this.getSelectionEnd();
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
            int pos=this.getCaretPosition(); //  corrected by t.y, 1May2005 
            this.setCaretPosition(pos+1);     // corrected by t.y, 1May2005
            repaint();
            return;
        }
        crReceived=false;
        if(key==8){    // when  back space key
        	position++; // t,y. 2007 9/6... JDK 1.6
        	 if(selectedStart<selectedEnd){
        	 	/*
            	 pre=s.substring(0,selectedStart);
            	 post=s.substring(selectedEnd,length);
            	 */
            	 this.cut();
            	 position=selectedStart;
            	 return;
        	 }
        	 else{
                 if(length<=0) {setText(""); setCaretPosition(0); return; }
//             if(this.fileSeparator.equals("/")){ position--;}
                 position--;
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
        	 }
             setText(pre+post);
             setCaretPosition(position);
        }
        else
        if(key==127){    // when  delete key
            if(length<=0) {setText(""); setCaretPosition(0); return; }
//             if(this.fileSeparator.equals("/")){ position--;}
//             position--;
			if(selectedStart<selectedEnd){
			   /*
				pre=s.substring(0,selectedStart);
				post=s.substring(selectedEnd,length);
				*/
				this.cut();
				position=selectedStart;
				return;
			}
			if(position<0) pre="";
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
			 if(selectedStart<selectedEnd){
				pre=s.substring(0,selectedStart);
				post=s.substring(selectedEnd,length);
				position=selectedStart;
				this.cut();
			 }
			 else{
                if(position>=length) pre=s;
                else
                if(position>0) pre=s.substring(0,position); 
                else {pre=""; position=0;}
                if(position<length)
                    post=s.substring(position,length);
			 }
             setText(pre+(char)key+post);
             setCaretPosition(position+1);
        }
        repaint();
        
    }
    
    public void pressKey(int p, int code){
//    	setCaretPosition(p);
		int SS = this.getSelectionStart();
		int SN = this.getSelectionEnd();
		switch(code){
			case KeyEvent.VK_LEFT :
				if(SS != SN)
					this.setCaretPosition(SS);
				break;
			case KeyEvent.VK_RIGHT :
				if(SS != SN)
					this.setCaretPosition(SN);
				break;
				/*
			case KeyEvent.VK_BACK_SPACE	:
				new EditSelection(this, -1);
				break;
			case KeyEvent.VK_DELETE		:
				new EditSelection(this, 1);
				break;
				*/
		}
    	setCaretPosition(p);		
    }
    
    public void releaseKey(int p, int code){
//    	setCaretPosition(p);
    }

    int id;

    public void setID(int i)
    {
        id=i;
    }

    public int getID(){
    	return id;
    }
    FrameWithControlledTextAreas frame;

    public void setFrame(FrameWithControlledTextAreas f)
    {
        frame=f;
   
    }

	public ControlledTextArea()
	{
		this(50,50);
/*
		//{{INIT_CONTROLS
		setBackground(java.awt.Color.white);
		setForeground(java.awt.Color.black);
		setFont(new Font("SansSerif", Font.PLAIN, 12));
		setSize(50,50);
		//}}
		

		//{{REGISTER_LISTENERS
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		//}}
		
//		fileSeparator=System.getProperty("file.separator");
		this.rmouse=new RemoteMouse();
		rmouse.setVisible(false);
		this.transferHandler=new TransferHandler("foreground");
//		this.setEditable(false);
 * 
 */
	}
	
    Clipboard clipBoard;
	
	public ControlledTextArea(int rows, int columns){
        super(rows,columns);		
		//{{INIT_CONTROLS
		setBackground(java.awt.Color.white);
		setForeground(java.awt.Color.black);
		setFont(new Font("SansSerif", Font.PLAIN, 12));
		//}}
		

		//{{REGISTER_LISTENERS
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		//}}
		
//		fileSeparator=System.getProperty("file.separator");
		this.rmouse=new RemoteMouse();
		rmouse.setVisible(false);
//		this.setEditable(false);
		this.transferHandler=new TransferHandler("foreground");
		try{
    		this.clipBoard=getToolkit().getSystemClipboard();
		}
		catch(Exception e){
			this.clipBoard=new Clipboard("text");
		}
		this.caret=this.getCaret();
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyTyped(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_keyTyped(event);
		}

		public void keyReleased(java.awt.event.KeyEvent e) {
			Object object = e.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_keyReleased(e);
		}
		
		public void keyPressed(java.awt.event.KeyEvent e){
			Object object = e.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_keyPressed(e);
		}

	}

	synchronized void ControlledTextArea_keyTyped(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
//		System.out.println("key typed:"+event.getKeyChar());
        if(frame==null) return;
		if(this.frame.isControlledByLocalUser()){
//			this.setEditable(true);
		    int p=this.getCaretPosition();
		    int key=event.getKeyChar();
    		frame.sendEvent("txt.kty("+id+","+p+","+key+")\n");
    		if(frame.isDirectOperation())
    	        frame.keyIsTypedAtATextArea(id,p,key);
//    	    this.setEditable(false);
	    }
	}
	synchronized void ControlledTextArea_keyPressed(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
//		System.out.println("key typed:"+event.getKeyChar());
		if(frame==null) return;
		if(this.frame.isControlledByLocalUser()){
//			this.setEditable(true);
			int p=this.getCaretPosition();
			int key=event.getKeyCode();
			frame.sendEvent("txt.kps("+id+","+p+","+key+")\n");
			if(frame.isDirectOperation())
				frame.keyIsPressedAtATextArea(id,p,key);
//		    this.setEditable(false);
		}
	}
	synchronized void ControlledTextArea_keyReleased(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
//		System.out.println("key typed:"+event.getKeyChar());
		if(frame==null) return;
		if(this.frame.isControlledByLocalUser()){
//			this.setEditable(true);
			int p=this.getCaretPosition();
			int key=event.getKeyCode();
			frame.sendEvent("txt.rls("+id+","+p+","+key+")\n");
			if(frame.isDirectOperation())
				frame.keyIsReleasedAtTextArea(id,p,key);
//			this.setEditable(false);
		}
		/*
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT	:
			case KeyEvent.VK_RIGHT	:
			case KeyEvent.VK_DOWN	:
			case KeyEvent.VK_UP		:
				new EditSelection(edit_area);
		}
		*/
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseEntered(event);
		}

		public void mouseReleased(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseReleased(event);
		}

		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mousePressed(event);
		}

		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseClicked(event);
		}
	}

	synchronized void ControlledTextArea_mouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.

//		selectionEndPoint=selectionStartPoint;
//		System.out.println(""+event);
		this.setEditable(true);
		this.setDragEnabled(true); 
        if(frame==null) return;
		boolean rf=this.requestFocusInWindow();
        if(this.frame.isControlledByLocalUser()){
//	    	selectionStartPoint=this.getCaretPosition();
	        int x=event.getX();
            int y=event.getY();
        	selectionStartPoint=this.viewToModel(new Point(x,y));
	    	this.setCaretPosition(selectionStartPoint);
			this.caret.moveDot(selectionStartPoint);
//	    	System.out.println("ssp="+selectionStartPoint);
//            System.out.println("x="+x+",y="+y);
    		frame.sendEvent("txt.mdn("+id+","+selectionStartPoint+","+x+","+y+")\n");
    		if(frame.isDirectOperation()){
    	        frame.mouseClickedAtTextArea(id,selectionStartPoint,x,y);
//	    tempString="";
                this.clickMouse(selectionStartPoint,x,y);
            }
        }
	}

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseMoved(event);
		}

		public void mouseDragged(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_mouseDragged(event);
		}
	}

	synchronized void ControlledTextArea_mouseDragged(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
//	   if(position==selectionEndPoint) return;
//	   selectionEndPoint=position;
//		System.out.println("drag:"+this.getDragEnabled());
//	   System.out.println("editable:"+this.isEditable());
       if(frame==null) return;
       if(this.frame.isControlledByLocalUser()){
//	       int position=this.getSelectionEnd();
    	   int position=this.getCaretPosition();
//    	   int position=this.selectionStartPoint;
           int x=event.getX();
           int y=event.getY();
           frame.sendEvent("txt.mdg("+id+","+position+","+x+","+y+")\n");
           if(frame.isDirectOperation()){
             frame.mouseDraggedAtTextArea(id,position,x,y);
             this.dragMouse(position,x,y);
           }
       }
	}

	void ControlledTextArea_mousePressed(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
//		selectionEndPoint=selectionStartPoint;
        if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
	        int x=event.getX();
            int y=event.getY();
//		    selectionStartPoint=this.getCaretPosition();
            selectionStartPoint=this.viewToModel(new Point(x,y));
    		frame.sendEvent("txt.mps("+id+","+selectionStartPoint+","+x+","+y+")\n");
    		if(frame.isDirectOperation()){
    	        frame.mousePressedAtTextArea(id,selectionStartPoint,x,y);
	            this.pressMouse(selectionStartPoint,x,y);
	        }
	    }
			 
	}

	synchronized void ControlledTextArea_mouseReleased(java.awt.event.MouseEvent event)
	{
	    if(frame==null) return;
	   if(this.frame.isControlledByLocalUser()){
	       int x=event.getX();
           int y=event.getY();
	       int position=this.getSelectionEnd();
    	   frame.sendEvent("txt.mrl("+id+","+position+","+x+","+y+")\n");
    	   if(frame.isDirectOperation()){
              frame.mouseReleasedAtTextArea(id,position,x,y);
              this.releaseMouse(position,x,y);
           }
       }
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledTextArea.this)
				ControlledTextArea_focusGained(event);
		}
	}

	void ControlledTextArea_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
	}

	void ControlledTextArea_mouseMoved(java.awt.event.MouseEvent event)
	{
	   if(frame==null) return;
       if(this.frame.isControlledByLocalUser()){
           int x=event.getX();
           int y=event.getY();
           frame.sendEvent("txt.mm("+id+","+x+","+y+")\n");
           if(frame.isDirectOperation()){
             frame.mouseMoveAtTextArea(id,x,y);
             this.moveMouse(x,y);
           }
       }
	}

	void ControlledTextArea_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
            int x=event.getX(); int y=event.getY();
            frame.sendEvent("txt.ment("+id+","+x+","+y+")\n");
            if(frame.isDirectOperation()){
                frame.mouseEnteredAtTheText(id,x,y);
	    	    this.enterMouse(x,y);
	    	}
		}
	}

	void ControlledTextArea_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledTextArea_mouseExited_Interaction1(event);
	}

	void ControlledTextArea_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
	    if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
            int x=event.getX(); int y=event.getY();
            frame.sendEvent("txt.mxit("+id+","+x+","+y+")\n");
            if(frame.isDirectOperation()){
              frame.mouseExitAtTheText(id,x,y);
              this.exitMouse();
            }
        }
	}
}