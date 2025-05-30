import Storage.Page.PageIO;
import Table.Field;
import Table.TableManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        ArrayList<Field> fields = new ArrayList<>();

        fields.add(new Field("id", "INT"));
        fields.add(new Field("name", "STRING"));
        fields.add(new Field("age", "INT"));

        TableManager.CreateTable("test6",fields);

        RandomAccessFile raf = new RandomAccessFile("TestData/DatabaseManager/test6.idb","rw");

        raf.seek(0);

        PageIO pio = new PageIO("TestData/DatabaseManager/test6.idb");
        pio.setHighestPageId(2);
        pio.updateHighestPageId();
        /**
         * 第零页的结构
         * 根页的页码 ( int 4B )
         * 目前的最高页码 （ int 4B ）
         * 中间节点允许的最多key数量 （ int 4B ）
         * 列的当前数量 （int 4B)
         * 列名 （最多100个字段，每列是 int（列名长度） + n 字节（UTF-8 字节串）)
         * 列类型 （ int 4B*100 , 最多允许400B)
         */
        byte[] bytes = Files.readAllBytes(Paths.get("TestData/DatabaseManager/test6.idb"));
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        System.out.println("根页页码： " + in.readInt());
        System.out.println("最高页码： " + in.readInt());
        System.out.println("最多key： " + in.readInt());
        int size = in.readInt();
        System.out.println("列的数量： " + size);

        for(int i = 0; i < size; i++) {
            int len = in.readInt();
            StringBuilder sb = new StringBuilder();
            byte b;
            for (int j = 0; j < len; j++) {
                b = in.readByte();
                sb.append((char) b);
            }
            System.out.println("列名： " + sb.toString());
        }

    }
}