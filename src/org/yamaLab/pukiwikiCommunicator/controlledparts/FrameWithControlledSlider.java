package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.Color;

public interface FrameWithControlledSlider
{
    void changeStateOnTheSlider(int id, int value);

    void exitMouseOnTheSlider(int id);

    void enterMouseOnTheSlider(int id);

    void sendEvent(String x);

    boolean isControlledByLocalUser();

    Color getBackground();

    void sliderStateChanged(int id, int value);

    void sliderMouseExited(int id);

    void sliderMouseEntered(int id);


	//{{DECLARE_CONTROLS
	//}}

}