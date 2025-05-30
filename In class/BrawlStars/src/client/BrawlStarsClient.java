//package client;
//
//import Message.Msg;
//import client.ui.MapFrame;
//import server.BrawlStarsServer;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.Socket;
//import java.net.DatagramSocket;
//
//
//public class BrawlStarsClient {
//    private MapFrame mp;
//    private int clientUDP_PORT;//客户端的UDP端口号
//    private String server_IP = "127.0.0.1";//服务器ip地址
//    private int serverUDP_PORT;//服务器转发客户端的UDP端口
//    private int deathUDP_PORT;//服务端监听角色死亡的UDP端口
//    private DatagramSocket ds = null;//套接字
//
//    public void setClientUDP_PORT(int clientUDP_PORT) {
//        this.clientUDP_PORT = clientUDP_PORT;
//    }
//
//    public BrawlStarsClient(MapFrame mp) {
//        this.mp = mp;
//        try {
//            this.clientUDP_PORT = getRandomPORT();
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.exit(0);//重复端口号就退出重进
//        }
//    }
//
//    //与服务器进行TCP连接
//    public void connect(String ip) {
//        server_IP = ip;
//        Socket s = null;
//        try {
//            ds = new DatagramSocket(clientUDP_PORT);
//            try {
//                s = new Socket(ip, BrawlStarsServer.TCP_PORT);
//            }catch (Exception e1){
//                e1.printStackTrace();
//            }
//            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//            dos.writeInt(clientUDP_PORT);//向服务端发射自己的UDP端口号
//            DataInputStream dis = new DataInputStream(s.getInputStream());
//            this.serverUDP_PORT = dis.readInt();//获取服务器转发客户端消息的UDP端口号
//            this.deathUDP_PORT = dis.readInt();//获取服务器监听角色死亡的UDP端口号
//            System.out.println("Client connected to " + server_IP + ":" + serverUDP_PORT);
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                if(s != null) s.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        new Thread(new UDPThread()).start();//开启客户端UDP线程
//        //CHA_New
//        //send(msg);
//    }
//
//    //客户端随机获取端口号
//    private int getRandomPORT() {
//        return (int) (Math.random() * 65535);
//    }
//
////    //发送信息到服务端
////    public void send(Msg msg){
////        msg.send(ds,server_IP,serverUDP_PORT);
////    }
//
//
//    public class  UDPThread implements Runnable{
//        byte[] buf = new byte[1024];
//        public void run() {
//            while (true) {
//                DatagramPacket dp = new DatagramPacket(buf, buf.length);
//                try {
//                    ds.receive(dp);
//                    parse(dp);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    private void parse(DatagramPacket dp) {
//        ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
//        DataInputStream dis = new DataInputStream(bais);
//        int msgType = 0;
//        try {
//            msgType = dis.readInt();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        Msg msg = null;
//        switch (msgType) {
//            case Msg.CHA_NEW_MSG:
//                msg=new
//        }
//
//    }
//}
