package Database;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class DatabaseManager {

    private static String currentDatabase;
    private static final String BASE_DIR = "../TestData/DatabaseManager";
    public static String getCurrentDatabase(){
        return currentDatabase;
    }

    public static void createDataBase(String dbName) {
        currentDatabase = dbName;
        File folder =  new File("../TestData/DatabaseManager/" + dbName);

        if(!folder.exists()) {
            if(folder.mkdirs()) {
                System.out.println("CreateDatabase Successfully : " + folder.getAbsolutePath());
            } else {
                System.out.println("CreateDatabase failed.");
            }
        } else {
            System.out.println("The database has already existed : " + folder.getAbsolutePath());
        }
    }

    public static List<String> listDatabases() {
        Path base = Paths.get(BASE_DIR);
        if (!Files.isDirectory(base)) {
            return List.of();  // 根目录不存在或不是目录，返回空列表
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
            return StreamSupport.stream(stream.spliterator(), false)
                    .filter(Files::isDirectory)           // 只保留目录
                    .map(Path::getFileName)               // 取最后一段路径
                    .map(Path::toString)                  // 转为 String
                    .sorted()                             // 按字母排序（可选）
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("列出数据库失败", e);
        }
    }

    public static boolean useDatabase(String dbName){
        File folder =  new File("../TestData/DatabaseManager/" + dbName);
        if(!folder.exists()){
            return false;
        } else {
            currentDatabase = dbName;
            return true;
        }
    }

    public static List<String> showDatabases(){
        File folder =  new File("../TestData/DatabaseManager");
        List<String> databases = new ArrayList<>();

        if(!folder.exists()||!folder.isDirectory()){
            System.out.println("not exist.");
        }
        File[] files = folder.listFiles();
        if(files!=null){
            for(File file:files){
                if(file.isDirectory()){
                    databases.add(file.getName());
                }
            }
        }
        return databases;
    }

    public static void dropDatabase(String dbName,int userLevel){
        if(userLevel==1){return;}
        // 原来是

        File folder = new File("../TestData/DatabaseManager/" + dbName);

        if(!folder.exists()||!folder.isDirectory()){
            System.out.println("not exist");
            return;
        }
        if(deleteDir(folder)){
            System.out.println("Database "+dbName+" deleted successfully.");
        }else{
            System.out.println("Failed to delete database "+ dbName);
        }
    }

    // 递归调用，删除文件夹里的文件
    private static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for(File f:files){
                    if(!deleteDir(f)){
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }
}
