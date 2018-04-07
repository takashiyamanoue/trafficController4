package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledCheckBox
{
    boolean isDirectOperation();

    boolean isControlledByLocalUser();

    void sendEvent(String x);

    void changeStateCheckBox(int i, int x);

    void unfocusCheckBox(int i);

    void focusCheckBox(int i);

    void stateChangedAtCheckBox(int i, int x);

    void mouseExitedAtCheckBox(int i);

    void mouseEnteredAtCheckBox(int i);
}