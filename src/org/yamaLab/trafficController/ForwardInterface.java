package org.yamaLab.trafficController;

import org.pcap4j.packet.Packet;
import org.yamaLab.trafficViewer.ParsePacket;

/*
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.PcapPacket;
*/

public interface ForwardInterface {
    public void sendPacketPP(ParsePacket x);
//    public void sendPacketJM(JMemoryPacket x, ParsePacket m);
//    public void sendPacket(PcapPacket x, ParsePacket m);
    public void sendPacket(Packet x, ParsePacket m);
    public byte[] getIPAddr();
    public void setIpMac(byte[] ip, byte[] mac);
}
