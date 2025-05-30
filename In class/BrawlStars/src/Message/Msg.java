//package Message;
//
//import java.io.DataInputStream;
//import java.net.DatagramSocket;
//
//public interface Msg {
//    public static final int CHA_NEW_MSG = 1;
//    public static final int CHA_MOVE_MSG = 2;
//    public static final int SHOT_NEW_MSG = 3;
//    public static final int CHA_DEATH_MSG = 4;
//    public static final int SHOT_DEATH_MSG = 5;
//    public static final int CHA_EXIST_MSG = 6;
//    public static final int CHA_BLOOD_MSG = 7;
//
//    public void send(DatagramSocket ds, String IP, int UDP_Port);
//
//    public void parse(DataInputStream dis);
//}