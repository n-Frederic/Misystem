//package server;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.ByteArrayInputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BrawlStarsServer extends JFrame {
//    public static final int TCP_PORT = 8080;//TCP端口号
//    public static final int UDP_PORT = 5678;//转发客户端数据的UDP端口号
//    public static final int Death_PORT = 5679;//接收客户端角色死亡的端口号
//    private List<Client> clients = new ArrayList<Client>();//客户端集合
//    private Image offScreenImage;//服务器画布
//    private Graphics offScreenGraphics;
//    private static final int SCREEN_WIDTH = 800;
//    private static final int SCREEN_HEIGHT = 600;
//
//    public static void main(String[] args) {
//        BrawlStarsServer server = new BrawlStarsServer();
//        server.launchFrame();
//        server.start();
//    }
//
//    public void start() {
//        new Thread(new UDPThread()).start();
//        new Thread(new deathThreadUDPThread()).start();
//        ServerSocket ss = null;
//        try {
//            ss = new ServerSocket(TCP_PORT);//监听客户端连接
//            System.out.println("Listening on port... " + TCP_PORT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        while (true) {
//            Socket s = null;
//            try {
//                s = ss.accept();//给客户端分配专属TCP套接字
//                System.out.println("Accepted connection from " + s.getRemoteSocketAddress());
//                DataInputStream in = new DataInputStream(s.getInputStream());
//                DataOutputStream out = new DataOutputStream(s.getOutputStream());
//                int UDP_PORT = in.readInt();//记录客户端UDP端口
//                Client client = new Client(s.getInetAddress().getHostAddress(), UDP_PORT);
//                clients.add(client);//添加到客户端容器
//
//                out.writeInt(BrawlStarsServer.UDP_PORT);//通知客户端自己的UDP端口号
//                out.writeInt(BrawlStarsServer.Death_PORT);//通知客户端监听死亡的UDP端口号
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (s != null) s.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private class UDPThread implements Runnable {
//        byte[] buf = new byte[1024];
//
//        public void run() {
//            DatagramSocket ds = null;
//            try {
//                ds = new DatagramSocket(UDP_PORT);//绑定到指定的UDP端口
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
//
//            while (null != ds) {
//                DatagramPacket dp = new DatagramPacket(buf, buf.length);//创建一个数据包用于接收数据
//                try {
//                    ds.receive(dp);
//                    for (Client client : clients) {
//                        dp.setSocketAddress(new InetSocketAddress(client.IP, client.UDP_PORT));
//                        ds.send(dp);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private class deathThreadUDPThread implements Runnable {
//        byte[] buf = new byte[1024];
//        public void run() {
//            DatagramSocket ds = null;
//            try {
//                ds = new DatagramSocket(Death_PORT);
//            }catch (SocketException e){
//                e.printStackTrace();
//            }
//            while (null != ds) {
//                DatagramPacket dp = new DatagramPacket(buf, buf.length);
//                ByteArrayInputStream bais = null;
//                DataInputStream dis = null;
//                try {
//                    ds.receive(dp);
//                    bais = new ByteArrayInputStream(buf,0,dp.getLength());
//                    dis = new DataInputStream(bais);
//                    int deadUDPPort = dis.readInt();
//                    //遍历集合，如果角色死亡就从集合中移除
//                    for (int i = 0; i < clients.size(); i++) {
//                        Client client = clients.get(i);
//                        if(client.UDP_PORT==deadUDPPort){
//                            clients.remove(client);
//                        }
//                    }
//                }catch (IOException e){
//                    e.printStackTrace();
//                }finally {
//                    if (null != ds) {
//                        try {
//                            dis.close();
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                    if (null != bais) {
//                        try {
//                            bais.close();
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
//    //服务器ui
//    @Override
//    public void paint(Graphics g) {
//        g.drawString("Brawl Stars", 30, 50);
//        int y = 80;
//        for(Client client : clients){
//            g.drawString("ip: "+client.IP,30,y);
//            y += 30;
//        }
//    }
//    @Override
//    //双缓冲绘制到屏幕
//    public void update(Graphics g) {
//        if(offScreenImage == null){
//            offScreenImage = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
//        }
//        offScreenGraphics = offScreenImage.getGraphics();
//        Color c = offScreenGraphics.getColor();
//        offScreenGraphics.setColor(Color.ORANGE);
//        offScreenGraphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
//        offScreenGraphics.setColor(c);
//        paint(offScreenGraphics);
//        g.drawImage(offScreenImage, 0, 0, null);
//    }
//
//    public void launchFrame(){
//        this.setLocation(200, 100);
//        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
//        this.setTitle("Brawl Stars");
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setResizable(false);
//        this.setBackground(Color.ORANGE);
//        this.setVisible(true);
//        new Thread(new UDPThread()).start();
//    }
//
//    //重画线程
//    class PaintThread implements Runnable {
//        public void run() {
//            while(true){
//                repaint();
//                try {
//                    Thread.sleep(50);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
