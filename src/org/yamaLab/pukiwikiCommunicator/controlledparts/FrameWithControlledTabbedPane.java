/*
 * ?øΩ?ê¨?øΩ?øΩ: 2005/04/25
 *
 * ?øΩ?øΩ?øΩÃêÔøΩ?øΩ?øΩ?øΩ?øΩ?øΩÍÇΩ?øΩR?øΩ?øΩ?øΩ?øΩ?øΩg?øΩÃë}?øΩ?øΩ?øΩ?øΩe?øΩ?øΩ?øΩv?øΩ?øΩ?øΩ[?øΩg?øΩ?øΩœçX?øΩ?øΩ?øΩÈÇΩ?øΩpukiwikiCommunicator.controlledparts> ?øΩR?øΩ[?øΩh?øΩ?øΩ?øΩ?øΩ > ?øΩR?øΩ[?øΩh?øΩ∆ÉR?øΩ?øΩ?øΩ?øΩ?øΩg
 */
package org.yamaLab.pukiwikiCommunicator.controlledparts;

import java.awt.*;

/**
 * @author yamachan
 *
 * ?øΩ?øΩ?øΩÃêÔøΩ?øΩ?øΩ?øΩ?øΩ?øΩÍÇΩ?øΩR?øΩ?øΩ?øΩ?øΩ?øΩg?øΩÃë}?øΩ?øΩ?øΩ?øΩe?øΩ?øΩ?øΩv?øΩ?øΩ?øΩ[?øΩg?øΩ?øΩœçX?øΩ?øΩ?øΩÈÇΩ?øΩ?øΩ
 * ?øΩE?øΩB?øΩ?øΩ?øΩh?øΩE > ?øΩ›íÔøΩ > Java > ?øΩR?øΩ[?øΩh?øΩ?øΩ?øΩ?øΩ > ?øΩR?øΩ[?øΩh?øΩ∆ÉR?øΩ?øΩ?øΩ?øΩ?øΩg
 */
public interface FrameWithControlledTabbedPane {

	void changeStateOnTheTabbedPane(int id, int value);

	void exitMouseOnTheTabbedPane(int id);

	void enterMouseOnTheTabbedPane(int id);

	void sendEvent(String x);

	boolean isControlledByLocalUser();

	Color getBackground();

	void stateChangedAtTabbedPane(int id, int value);

	void mouseExitedAtTabbedPane(int id);

	void mouseEnteredAtTabbedPane(int id);


	//{{DECLARE_CONTROLS
	//}}

}