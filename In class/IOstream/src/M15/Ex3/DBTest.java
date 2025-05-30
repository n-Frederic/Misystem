package M15.Ex3;

import java.io.*;
import java.util.*;

public class DBTest {

    static final int NAME_LEN = 10;
    static final int QUANTITY_LEN = 4;

    public static void main(String[] args) throws IOException {

        ArrayList<Product> products = new ArrayList<>();
        RandomAccessFile rf = null;
        Scanner scanner = new Scanner(System.in);

        products.add(new Product("p1", 4));
        products.add(new Product("p2", 3));
        products.add(new Product("p3", 2));
        products.add(new Product("p4", 1));

        try {
            rf = new RandomAccessFile("DataBase", "rw");
            rf.setLength(0);
            for (Product product : products) {
                writeProduct(rf, product);

            }

            while (true) {
                // 显示菜单
                System.out.println("---------- Menu ----------");
                System.out.println("0. display all products");
                System.out.println("1. modify a product");
                System.out.println("2. add a product");
                System.out.println("3. quit");
                System.out.print("Enter your choice: ");

                // 读取用户输入
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                // 使用 switch-case 来根据用户输入执行不同操作
                switch (choice) {
                    case 0:
                        displayAllProducts(rf);
                        break;

                    case 1:
                        System.out.print("Please enter the index of the record you want to modify: ");
                        Scanner scanner2 = new Scanner(System.in);
                        int index2 = scanner2.nextInt();
                          updateProduct(rf, index2);
                        break;
                    case 2:
                        addNewRecord(rf);
                        break;


                    case 3:
                        System.out.println("quit the program...");
                        return;

                    default:
                        // 无效输入
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                rf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取姓名
    private static String readName(RandomAccessFile randomAccessFile) throws IOException {
        StringBuilder nameBuilder = new StringBuilder(NAME_LEN);
        for (int j = 0; j < NAME_LEN; j++) {
            nameBuilder.append((char)randomAccessFile.readByte());
        }

        return nameBuilder.toString();
    }

    //获取产品
    private static Product readProduct(RandomAccessFile randomAccessFile) throws IOException {
        String name = readName(randomAccessFile);
        int quantity = randomAccessFile.readInt();
        return new Product(name, quantity);
    }

    //写入产品
    private static void writeProduct(RandomAccessFile randomAccessFile, Product product) throws IOException {
        String name = product.getName();

        randomAccessFile.writeBytes(name);
        for (int i = 0; i < NAME_LEN - name.length(); i++) {
            randomAccessFile.writeByte(' ');
        }
        randomAccessFile.writeInt(product.getQuantity());
    }

    private static void addNewRecord(RandomAccessFile randomAccessFile) {
        Product product = inputProduct();

        try {
            randomAccessFile.seek(randomAccessFile.length());
            writeProduct(randomAccessFile, product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Product inputProduct() {
        System.out.print("Please enter the name: ");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        System.out.print("Please enter the Quantity: ");
        System.out.println();
        int quantity = scanner.nextInt();

        return new Product(name, quantity);
    }
    //更新一个产品
    public static void updateProduct(RandomAccessFile rf, long index) throws IOException {
        Product product = inputProduct();
        try {
            rf.seek(index * (NAME_LEN + QUANTITY_LEN));
            writeProduct(rf, product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //展示所有产品
    public static void displayAllProducts(RandomAccessFile randomAccessFile) throws IOException {
        String name;
        int quantity;

        int recordLen = (int) randomAccessFile.length() / (NAME_LEN + QUANTITY_LEN);
        System.out.println(randomAccessFile.length());

        for (int i = 0; i < recordLen; i++) {
            randomAccessFile.seek(i * (NAME_LEN + QUANTITY_LEN));

            Product product = readProduct(randomAccessFile);
            System.out.println(product);
        }

    }
}
