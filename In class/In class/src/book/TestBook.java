package book;

import java.util.Arrays;

public class TestBook {
    public static void main(String[] args) {
        Author[] authors = {new Author("张三", "12345@163.com", 'M'), new Author("李四", "54321@163.com", 'F')};
        Book b1 = new Book("《1》", authors, 68.00);
        Book b2 = new Book("《2》", authors, 600);
        System.out.println("b1作者："+Arrays.toString(b1.getAuthorNames()));
        System.out.println("b1作者信息：\n"+Arrays.toString(b1.getAuthors()));
        System.out.println("b1详细信息：\n"+b1.toString());
        System.out.println("b2详细信息：\n"+b2.toString());
    }
}
