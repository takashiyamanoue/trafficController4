package org.yamaLab.pukiwikiCommunicator.controlledparts;
public interface EditDialog
{
    String getSubName();

    void setSubName(String n);

    void setDialogName(String name);

    void setListener(DialogListener l);

    String getText();

    String getDialogName();
    
    String getFilePath();
    
    String getFileName();

}