package org.yamaLab.pukiwikiCommunicator.language;


public interface GuiWithControlFlag
{
    void resetStopFlag();

    boolean stopFlagIsOn();
    boolean traceFlagIsOn();

}
