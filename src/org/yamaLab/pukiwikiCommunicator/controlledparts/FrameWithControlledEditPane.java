package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledEditPane
{
    void pageLoadingDone(int i);

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    boolean isShowingRmouse();

    void updateHyperLink(int id, String url);

    void hyperLinkUpdate(int id, String link);

    void mouseExitAtTheEPane(int id, int x, int y);

    void mouseEnteredAtTheEPane(int id, int x, int y);

    void exitMouseOnTheEPane(int id, int x, int y);

    void enterMouseOnTheEPane(int id, int x, int y);

    void moveMouseOnTheEPane(int id,int x, int y);

    void mouseMovedAtTheEPane(int i, int x, int y);

    void pressMouseOnTheEPane(int i, int p, int x, int y);

    void mousePressedAtTheEPane(int i,int p, int x, int y);

    void releaseMouseOnTheEPane(int id, int position, int x, int y);

    void dragMouseOnTheEPane(int id, int position, int x, int y);

    void mouseReleasedAtTheEPane(int id, int position,int x, int y);

    void mouseDraggedAtTheEPane(int id, int position, int x, int y);

    void clickMouseOnTheEPane(int i, int p, int x, int y);

    void typeKeyAtTheEPane(int i, int p, int key);

    void mouseClickedAtTheEPane(int i,int p, int x, int y);

    void keyIsTypedAtTheEPane(int i,int p,int key);


}