package org.yamaLab.trafficViewer;

public interface ParsePacketReceiver {
    public void receive(String command, ParsePacket parsePacket);
    public void receive(String command, long no, int nic, ParsePacket parsePacket);
    
}
