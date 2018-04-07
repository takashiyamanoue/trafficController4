package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledMenu {
    boolean isDirectOperation();

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    void enterMenu(int i);

    void exitMenu(int i);

    void clickMenu(int i);

    void mouseClickedAtMenu(int i);

    void mouseExitedAtMenu(int i);

    void mouseEnteredAtMenu(int i);

}
