package org.yamaLab.pukiwikiCommunicator.controlledparts;

public interface FrameWithControlledPane
{
    boolean isDirectOperation();

    void sendEvent(String x);

    boolean isControlledByLocalUser();

    void changeScrollbarValue(int paneID, int barID, int value);

    void showScrollBar(int paneID, int barID);

    void hideScrollBar(int paneID, int barID);

    void scrollBarHidden(int paneID, int barId);

    void scrollBarShown(int paneID, int barID);

    void scrollBarValueChanged(int paneID, int barID, int v);

}