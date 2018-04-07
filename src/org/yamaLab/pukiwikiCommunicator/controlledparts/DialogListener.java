package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.io.File;
import java.util.Vector;
public interface DialogListener
{
    boolean isDirectOperation();

    boolean isShowingRmouse();

    boolean isControlledByLocalUser();

    Vector getDialogs();

    void sendFileDialogMessage(String m);

    File getDefaultPath();

    void whenCancelButtonPressed(EditDialog d);

    void whenActionButtonPressed(EditDialog d);

}