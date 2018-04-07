package org.yamaLab.pukiwikiCommunicator.controlledparts;
public interface FrameWithControlledButton
{
    boolean isDirectOperation();

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    void clickButton(int i);

    void unfocusButton(int i);

    void focusButton(int i);

    void mouseClickedAtButton(int i);

    void mouseExitedAtButton(int i);

    void mouseEnteredAtButton(int i);

}