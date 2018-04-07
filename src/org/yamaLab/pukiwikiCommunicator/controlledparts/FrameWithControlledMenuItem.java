package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledMenuItem {
    boolean isDirectOperation();

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    void clickMenuItem(int i);

    void exitMenuItem(int i);

    void enterMenuItem(int i);

    void mouseClickedAtMenuItem(int i);

    void mouseExitedAtMenuItem(int i);

    void mouseEnteredAtMenuItem(int i);

}
