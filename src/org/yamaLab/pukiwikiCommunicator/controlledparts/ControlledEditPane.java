package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class ControlledEditPane extends javax.swing.JEditorPane
{
    public // synchronized 
    void repaint()
    {
            super.repaint();
//            notifyAll();
    }


    String fileSeparator;

    public void exitMouse()
    {
		this.rmouse.setVisible(false);
		this.repaint();
    }

    public void enterMouse(int x, int y)
    {
        if(this.frame.isShowingRmouse()) this.rmouse.setVisible(true);
        else this.rmouse.setVisible(false);
        moveMouse(x,y);
        this.repaint();        
    }

    boolean isCapturing;

    public void init()
    {
        
		this.rmouse=new RemoteMouse();
		rmouse.setVisible(false);
		int width=this.getWidth();
		int height=this.getHeight();
		offScreenImage=createImage(width,height);
                    //Create the hidden plane's image            
        if(offScreenImage==null){
            this.addNotify();
            offScreenImage=createImage(width,height);
        }
                    
        offScreenGraphics=offScreenImage.getGraphics();
                    //Initialize the hidden plane.
                    
    }

    Graphics offScreenGraphics;
    Image offScreenImage;
    Image image;

    public boolean imageUpdate(Image image, int info, int x, int y, int width, int height)
    {
        this.image=image;
        System.out.println("html image updated, info:"+
           info+" x="+x+" y="+y+" width="+width+" height="+height);
        if((info & ABORT)!=0){
            System.out.println("aobrted.");
            return false;
        }
        if((info & ALLBITS)!=0){
            System.out.println("all bits available.");
            return false;
        }
        return true;
    }

    public Image getImage()
    {
        /*
           BufferedImage image =
                new BufferedImage(100,100,
                    BufferedImage.TYPE_INT_ARGB);
           Graphics2D g2d = image.createGraphics();
 // ?¿½?¿½?¿½?¿½?¿½?¿½Ç‰ï¿½
           g2d.setClip(0, 0, image.getWidth(this), image.getHeight(this));
 //          g.translate(100,100);
 //          t.draw((Graphics2D)g);
           t.draw(g2d);
           g.drawImage(image,100,100,this);
       
*/
        if(offScreenGraphics!=null){
           offScreenGraphics.setColor(getBackground());
            // Set the hidden plane's background color.
           offScreenGraphics.fillRect(0,0,this.getSize().width, this.getSize().height);
            // Set the hidden plane's size.
           offScreenGraphics.setColor(getForeground());
            // Set the hidden plane's foreground.
//           super.paint(offScreenGraphics);
           this.isCapturing=true;
           this.repaint();
           this.isCapturing=false;
            // super.paint(g);
/*       
           if(this.rmouseIsShown && this.rmouseIsActivated) 
                 this.rmouse.paint(offScreenGraphics);
            //     this.rmouse.paint(g);
        
           g.drawImage(offScreenImage,0,0,this);
             // Show the hidden plane.
 */       
           image=offScreenImage;
        }
        return image;
    }

    public void registerListeners()
    {
        /*
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		*/
		SymHyperlink lSymHyperlink = new SymHyperlink();
		this.addHyperlinkListener(lSymHyperlink);
		
		this.rmouse=new RemoteMouse();
		
		offScreenImage=createImage(this.getSize().width, this.getSize().height);
                    //Create the hidden plane's image
        offScreenGraphics=offScreenImage.getGraphics();
                    //Initialize the hidden plane.
		
    }

    public void moveMouse(int x, int y)
    {
         rmouse.move(x,y);
         repaint();
    }

    public void setVisibleRemoteMouse(boolean f)
    {
        this.rmouse.setVisible(f);
    }

    public void paint(Graphics g)
    {
       if(this.isCapturing){
            super.paint(this.offScreenGraphics);
       }
       else
            super.paint(g);
       rmouse.paint(g);
/*
if(this.rmouseIsShown && this.rmouseIsActivated)
            if(rmouse!=null)
                this.rmouse.paint(g);      
*/        
    }

    RemoteMouse rmouse;

    String tempString;
    int selectionStartPoint;
    int selectionEndPoint;
    int id;
    FrameWithControlledEditPane frame;
    boolean crReceived;

    public void typeKey(int position, int key)
    {
        String s=getText();
        String pre="";
        String post="";
        int length=s.length();
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
        /*
        if(position>0) pre=s.substring(0,position); else pre="";
        if(position<length) post=s.substring(position,length);
        if(key==8){    // when  back space key
            if(post.length()>0) post=s.substring(position+1,s.length());
            setText(pre+post);
        }
        else{
            setText(pre+(char)key+post);
        }
        setCaretPosition(position);
        repaint();
        */
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

    public void setFrame(FrameWithControlledEditPane f)
    {
        frame=f;
   
    }

    public void releaseMouse(int position,int x, int y)
    {
        rmouse.move(x,y);
        String txt="";
        try{
            txt=this.getText();
            if(txt.equals("")) return;
            this.setSelectionEnd(position);
            selectionEndPoint=position;
            if(selectionStartPoint==position) return;
            this.select(this.selectionStartPoint,this.selectionEndPoint);
            tempString=this.getSelectedText();
        }
        catch(Exception e){}
//        System.out.println("mouse released at carat "+position);
        this.rmouse.resetColor();
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

    public void dragMouse(int position, int x, int y)
    {
        rmouse.move(x,y);
        this.rmouse.setColor(Color.blue);
        this.setSelectionEnd(position);
        selectionEndPoint=position;
        if(selectionEndPoint>selectionStartPoint)
        this.select(this.selectionStartPoint,this.selectionEndPoint);
        else
        	this.select(this.selectionEndPoint,this.selectionStartPoint);
        repaint();
    }

     public void clickMouse(int position, int x, int y)
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

	public ControlledEditPane()
	{

		//{{INIT_CONTROLS
		setSize(436,276);
		//}}

		//{{REGISTER_LISTENERS
		/*
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		SymHyperlink lSymHyperlink = new SymHyperlink();
		this.addHyperlinkListener(lSymHyperlink);
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		SymPropertyChange lSymPropertyChange = new SymPropertyChange();
		this.addPropertyChangeListener(lSymPropertyChange);
		*/
		//}}
		
	    this.addHyperlinkListener(new HyperlinkListener() {
	        public void hyperlinkUpdate(HyperlinkEvent evt) {
	          // Ignore hyperlink events if the frame is busy
	    		ControlledEditPane_hyperlinkUpdate_Interaction1(evt);
	        }
	      });

	    this.addKeyListener(new KeyListener() {
			public void keyTyped(java.awt.event.KeyEvent event)
			{
				Object object = event.getSource();
				if (object == ControlledEditPane.this)
					ControlledEditPane_keyTyped(event);
			}

			public void keyReleased(java.awt.event.KeyEvent event)
			{
				Object object = event.getSource();
				if (object == ControlledEditPane.this)
					ControlledEditPane_keyReleased(event);
			}

			public void keyPressed(java.awt.event.KeyEvent event)
			{
				Object object = event.getSource();
				if (object == ControlledEditPane.this)
					ControlledEditPane_keyPressed(event);
			}	    	
	      });

	      		
		isCapturing=false;
		fileSeparator=System.getProperty("file.separator");
		
	}

	//{{DECLARE_CONTROLS
	//}}

/*
	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyTyped(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_keyTyped(event);
		}

		public void keyReleased(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_keyReleased(event);
		}

		public void keyPressed(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_keyPressed(event);
		}
	}
*/
	void ControlledEditPane_keyPressed(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_keyPressed_Interaction1(event);
	}

	void ControlledEditPane_keyPressed_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
			// ControlledEditPane Show the ControlledEditPane
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void ControlledEditPane_keyReleased(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_keyReleased_Interaction1(event);
	}

	void ControlledEditPane_keyReleased_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
			// ControlledEditPane Show the ControlledEditPane
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void ControlledEditPane_keyTyped(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_keyTyped_Interaction1(event);
	}

	void ControlledEditPane_keyTyped_Interaction1(java.awt.event.KeyEvent event)
	{
		int key=event.getKeyChar();
        if(frame==null) return;
        if(this.frame.isControlledByLocalUser()){
    		int p=this.getCaretPosition();
	    	frame.sendEvent("etxt.kdn("+id+","+p+","+key+")\n");
	        frame.keyIsTypedAtTheEPane(id,p,key);
	    }
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseReleased(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseReleased(event);
		}

		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mousePressed(event);
		}

		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseEntered(event);
		}

		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseClicked(event);
		}
	}

	void ControlledEditPane_mouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseClicked_Interaction1(event);
	}

	void ControlledEditPane_mouseClicked_Interaction1(java.awt.event.MouseEvent event)
	{
        if(this.frame.isControlledByLocalUser()){
	    	selectionStartPoint=this.getCaretPosition();
	        int x=event.getX();
            int y=event.getY();
    		frame.sendEvent("etxt.mdn("+id+","+selectionStartPoint+","+x+","+y+")\n");
	        frame.mouseClickedAtTheEPane(id,selectionStartPoint,x,y);
//	    tempString="";
            this.clickMouse(selectionStartPoint,x,y);
        }
	}

	void ControlledEditPane_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseEntered_Interaction1(event);
	}

	void ControlledEditPane_mouseEntered_Interaction1(java.awt.event.MouseEvent event)
	{
        int x=event.getX(); int y=event.getY();
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("etxt.ment("+id+","+x+","+y+")\n");
            frame.mouseEnteredAtTheEPane(id,x,y);
            this.enterMouse(x,y);
        }
	}

	void ControlledEditPane_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseExited_Interaction1(event);
	}

	void ControlledEditPane_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
        int x=event.getX(); int y=event.getY();
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("etxt.mxit("+id+","+x+","+y+")\n");
            frame.mouseExitAtTheEPane(id,x,y);
            this.exitMouse();
        }
	}

	void ControlledEditPane_mousePressed(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mousePressed_Interaction1(event);
	}

	void ControlledEditPane_mousePressed_Interaction1(java.awt.event.MouseEvent event)
	{
        if(this.frame.isControlledByLocalUser()){
	        int x=event.getX();
            int y=event.getY();
		    selectionStartPoint=this.getCaretPosition();
    		frame.sendEvent("etxt.mps("+id+","+selectionStartPoint+","+x+","+y+")\n");
	        frame.mousePressedAtTheEPane(id,selectionStartPoint,x,y);
	        this.pressMouse(selectionStartPoint,x,y);
	    }
	}

	void ControlledEditPane_mouseReleased(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseReleased_Interaction1(event);
	}

	void ControlledEditPane_mouseReleased_Interaction1(java.awt.event.MouseEvent event)
	{
	   if(this.frame.isControlledByLocalUser()){
	       int x=event.getX();
           int y=event.getY();
	       int position=this.getSelectionEnd();
    	   frame.sendEvent("etxt.mrl("+id+","+position+","+x+","+y+")\n");
           frame.mouseReleasedAtTheEPane(id,position,x,y);
           this.releaseMouse(position,x,y);
       }
	}

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseMoved(event);
		}

		public void mouseDragged(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_mouseDragged(event);
		}
	}

	void ControlledEditPane_mouseDragged(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseDragged_Interaction1(event);
	}

	void ControlledEditPane_mouseDragged_Interaction1(java.awt.event.MouseEvent event)
	{
       if(this.frame.isControlledByLocalUser()){
//	       int position=this.getSelectionEnd();
    	   int position=this.getCaretPosition();
           int x=event.getX();
           int y=event.getY();
           frame.sendEvent("etxt.mdg("+id+","+position+","+x+","+y+")\n");
           frame.mouseDraggedAtTheEPane(id,position,x,y);
           this.dragMouse(position,x,y);
       }
	}

	void ControlledEditPane_mouseMoved(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_mouseMoved_Interaction1(event);
	}

	void ControlledEditPane_mouseMoved_Interaction1(java.awt.event.MouseEvent event)
	{
       int x=event.getX();
       int y=event.getY();
       if(this.frame.isControlledByLocalUser()){
           frame.sendEvent("etxt.mm("+id+","+x+","+y+")\n");
           frame.mouseMovedAtTheEPane(id,x,y);
           this.moveMouse(x,y);
       }
	}

	class SymHyperlink implements javax.swing.event.HyperlinkListener
	{
		public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_hyperlinkUpdate(event);
		}
	}

	void ControlledEditPane_hyperlinkUpdate(javax.swing.event.HyperlinkEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_hyperlinkUpdate_Interaction1(event);
	}

	void ControlledEditPane_hyperlinkUpdate_Interaction1(javax.swing.event.HyperlinkEvent event)
	{
	    if(this.frame==null) return;
        if(this.frame.isControlledByLocalUser()){
        	if(event.getSource()!=this) return;
            HyperlinkEvent.EventType etype= event.getEventType();
            if(etype==HyperlinkEvent.EventType.ACTIVATED) {
               URL theURL=event.getURL();
               String urlName=theURL.toString();
//	    	frame.sendEvent("etxt.kdn("+id+","+p+","+key+")\n");
    		   this.frame.hyperLinkUpdate(id,urlName);
               this.frame.sendEvent("etxt.hpl("+id+",\""+urlName+"\")\n");
               return;
//	        this.typeKey(p,key);
            }
            else{
//            	System.out.println(event.toString());
            }
	    }
	}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentShown(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_componentShown(event);
		}
	}

	void ControlledEditPane_componentShown(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		ControlledEditPane_componentShown_Interaction1(event);
	}

	void ControlledEditPane_componentShown_Interaction1(java.awt.event.ComponentEvent event)
	{
//	    System.out.println("the component is shown");
	}

	class SymPropertyChange implements java.beans.PropertyChangeListener
	{
		public void propertyChange(java.beans.PropertyChangeEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledEditPane.this)
				ControlledEditPane_propertyChange(event);
		}
	}

	void ControlledEditPane_propertyChange(java.beans.PropertyChangeEvent event)
	{
		// to do: code goes here.
		String prop=event.getPropertyName();
//	    System.out.println("property changed:"+event.getPropertyName());
	    if(prop.equals("page")){
//	        System.out.println("property changed:"+prop);
            this.frame.pageLoadingDone(this.id);
	    }
	}
}