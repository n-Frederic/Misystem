import java.io.*;

public class TestLineNumbering {
    public static void main(String[] args) {

        //声明文件实例，初始化为命令行参数接收的名字
        File file = new File(args[0] + ".txt");
        int lineNumber = 0;
        String line;

        try {
            //创建输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //创建输出流
            PrintWriter pw = new PrintWriter(file);
            //按行读取输入内容，打印带行号的内容到文件
            while ((line = br.readLine()) != null) {
                lineNumber++;
                pw.println(lineNumber + "\t" + line);
            }
            //关闭流
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
