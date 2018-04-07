package org.yamaLab.pukiwikiCommunicator.controlledparts;
public interface FrameWithControlledFocus
{
    void loseFocus();

    void gainFocus();

    void focusLost();

    void focusGained();

}