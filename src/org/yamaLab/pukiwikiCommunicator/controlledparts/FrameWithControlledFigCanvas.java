package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledFigCanvas
{
    boolean isDirectOperation();

    void dragMouseOnTheFigCanvas(int id, int x, int y);

    int getTimeNow();

    boolean isControlledByLocalUser();

    void recordMessage(String x);

    boolean isShowingRmouse();

    void downKeyOnTheFigCanvas(int id, int key);

    void exitMouseOnTheFigCanvas(int id);

    void enterMouseOnTheFigCanvas(int id, int x, int y);

    void moveMouseOnTheFigCanvas(int id, int x, int y);

    void upMouseOnTheFigCanvas(int id, int x, int y);

    void downMouseOnTheFigCanvas(int id, int x, int y);

    void keyDownedAtFigCanvas(int id, int key);

    void mouseUpAtFigCanvas(int id, int x, int y);

    void sendEvent(String x);

    void mouseMovedAtFigCanvas(int id, int x, int y);

    void mouseExitedAtFigCanvas(int id);

    void mouseEnteredAtFigCanvas(int id, int x, int y);

    void mouseDraggedAtFigCanvas(int id, int x, int y);

    void mouseDownedAtFigCanvas(int id, int x, int y);


}