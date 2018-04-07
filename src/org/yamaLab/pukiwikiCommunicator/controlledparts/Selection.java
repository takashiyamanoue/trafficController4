package org.yamaLab.pukiwikiCommunicator.controlledparts;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class Selection extends java.awt.event.MouseAdapter
{
    public void mousePressed(MouseEvent e)
    {
        JTable table = (JTable)e.getSource();
        System.out.println(table.getSelectedRow() + " "+ table.getSelectedColumn());
    }

}