package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledTextAreas
{
    boolean isDirectOperation();

    void setTextOnTheText(int i,int pos, String s);

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    boolean isShowingRmouse();

    void exitMouseOnTheText(int id, int x, int y);

    void enterMouseOnTheText(int id, int x, int y);

    void mouseExitAtTheText(int id, int x, int y);

    void mouseEnteredAtTheText(int id, int x, int y);

    void moveMouseOnTheText(int id, int x, int y);

    void mouseMoveAtTextArea(int id, int x, int y);

    void pressMouseOnTheText(int i, int p, int x, int y);

    void mousePressedAtTextArea(int i,int p, int x, int y);

    void releaseMouseOnTheText(int id, int position, int x, int y);

    void dragMouseOnTheText(int id, int position, int x, int y);

    void mouseReleasedAtTextArea(int id, int position, int x, int y);

    void mouseDraggedAtTextArea(int id, int position, int x, int y);

    void clickMouseOnTheText(int i, int p, int x, int y);

    void typeKey(int i, int p, int key);
    
    void pressKey(int i, int p, int code);

    void releaseKey(int i, int p, int code);

    void mouseClickedAtTextArea(int i,int p, int x, int y);

    void keyIsTypedAtATextArea(int i,int p,int key);
    
    void keyIsPressedAtATextArea(int i, int p, int key);
    
	void keyIsReleasedAtTextArea(int id, int p, int key);



}