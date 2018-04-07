package org.yamaLab.pukiwikiCommunicator.controlledparts;
public interface PaneWithControlledScrollBar
{
    boolean isDirectOperation();

    void sendEvent(String x);

    boolean isControlledByLocalUser();

    void scrollBarIsHidden(int barID);

    void scrollBarIsShown(int barID);

    void setScrollBarValue(int barID, int v);

    void scrollBarValueChanged(int barID, int v);

}