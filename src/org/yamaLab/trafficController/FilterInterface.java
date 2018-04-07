package org.yamaLab.trafficController;

import java.util.Vector;

public interface FilterInterface {
    public void addFilter(String cmd, String[] args);
    public void clear();
    public Vector<String> getResults();
    public void setReturnInterface(ForwardInterface f);
}
