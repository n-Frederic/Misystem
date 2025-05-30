//package Message;
//
//import client.*;
//import client.BrawlStarsClient;
//import client.ui.Character;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
//
//public class CHA_NEW_MSG implements Msg {
//    private int msgType = Msg.CHA_NEW_MSG;
//    private Character character;
//    private BrawlStarsClient bsc;
//
//    public CHA_NEW_MSG(Character character, BrawlStarsClient bsc) {
//        this.character = character;
//        this.bsc = bsc;
//    }
//
//    public void send(DatagramSocket ds, String IP, int UDP_Port) {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(88);
//        DataOutputStream dos = new DataOutputStream(bos);
//        try {
//            dos.writeInt(msgType);
//            dos.writeInt(character.getX());
//            dos.writeInt(character.getY());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] buf = bos.toByteArray();
//        try {
//            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, UDP_Port));
//            ds.send(dp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void parse(DataInputStream dis) {
//        try {
//            int id = dis.readInt();
//            if(id == this.bsc.) {}
//        }
//    }
//}
