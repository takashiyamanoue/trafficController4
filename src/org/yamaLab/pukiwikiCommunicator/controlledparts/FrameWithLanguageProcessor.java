package org.yamaLab.pukiwikiCommunicator.controlledparts;

import javax.swing.JFrame;

public interface FrameWithLanguageProcessor {
    JFrame getDrawFrame();
    ControlledFrame lookUp(String name);
    void resetStopFlag();
    boolean stopFlagIsOn();
    boolean traceFlagIsOn();

}
