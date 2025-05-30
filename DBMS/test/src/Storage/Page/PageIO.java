package Storage.Page;

import Storage.Value.Value;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class PageIO {
    private RandomAccessFile file;
    private static final String DIRECTORY = "TestData/DatabaseManager";
    public static final int PAGE_SIZE = 8 * 1024; // 8KB

    public void setHighestPageId(int highestPageId) {
        this.highestPageId = highestPageId;
    }

    private int highestPageId; // 当前最高页码

    public PageIO(String filePath) throws IOException {
        this.file = new RandomAccessFile(filePath, "rw");
        this.highestPageId = 0;
//        if (file.length() == 0) {
//            // 文件为空，初始化根页和最高页码
//            writeRootPageStructure(new Schema(new ArrayList<>()));
//            this.highestPageId = 1; // 初始化最高页码为1
//        } else {
//            // 读取当前最高页码（前8字节保存了根页和最高页码）
//            file.seek(4);
//            file.readInt();
//            this.highestPageId = file.readInt(); // 读取最高页码
//        }
    }

    /**
     * 获取当前最高页码
     */
    public int getHighestPageId() {
        return highestPageId;
    }

    /**
     * 更新当前最高页码
     */
    public void updateHighestPageId() throws IOException {
        file.seek(4);  // 定位到保存最高页码的位置（第8个字节）
        file.writeInt(highestPageId);  // 更新文件中的最高页码
    }


    public Page readPage(int pageId) throws IOException {
        file.seek((long) pageId * Page.PAGE_SIZE);
        byte[] data = new byte[Page.PAGE_SIZE];
        file.readFully(data);
        return Page.leafFromBytes(data);
    }

    public void writePage(int pageId, Page page) throws IOException {
        byte[] data = page.leafToBytes();
        file.seek(pageId * Page.PAGE_SIZE);
        file.write(data);
    }

    public int getRootPageId() throws IOException {
        file.seek(0); // file header
        return file.readInt();  // 假设前4字节是 rootPageId
    }

    public void setRootPageId(int rootId) throws IOException {
        file.seek(0);
        file.writeInt(rootId);
    }

    public int allocateNewPage() throws IOException {
        highestPageId++;
        updateHighestPageId();
        long length = file.length();
        int newPageId = (int)(length / Page.PAGE_SIZE);
        file.setLength(length + Page.PAGE_SIZE);
        return newPageId;
    }

    // 根据主键和表名找到页号
    public int findPageNum(Tuple tuple, String tbname) throws IOException {
        int pageNum = -1; // 初始假设未找到页号
        String filename = tbname + ".ibd"; // 获取表的文件名（.ibd文件）
        Value value = tuple.getPrimaryV(); // 获取主键值

        // 从文件中获取第0页，作为根节点
        Page rootPage = readPage(0);

        // 递归查找页号
        pageNum = findPageNumHelper(rootPage, value, filename);

        return pageNum;
    }

    // 递归方法，根据给定的值遍历树结构，找到对应的页号
    private int findPageNumHelper(Page currentPage, Value keyValue, String filename) throws IOException {
        // 获取当前页中的所有tuples
        List<Tuple> tuples = currentPage.getTuples();

        // 如果当前页是叶子页，直接返回
        if (currentPage.isLeaf()) {
            for (Tuple tuple : tuples) {
                if (tuple.getPrimaryV().compare(keyValue) == 0) {
                    return currentPage.getPageId();
                }
            }
        } else {
            // 如果当前是非叶子节点，读取该页中每个子节点的主键最小值
            for (int i = 0; i < tuples.size(); i++) {
                // 假设每个tuple的主键最小值是第一个字段
                Value minKey = tuples.get(i).getPrimaryV();
                if (keyValue.compare(minKey) <= 0) {
                    // 找到符合条件的子节点，递归查找
                    int childPageId = getChildPageId(currentPage, i);
                    Page childPage = readPage(childPageId);
                    return findPageNumHelper(childPage, keyValue, filename);
                }
            }
        }
        return -1; // 如果没找到
    }

    // 从当前页获取子节点的页号
    private int getChildPageId(Page currentPage, int index) {
        // 返回当前页中第 index 个子节点的页号
        // 这里假设我们有一个可以获取子节点页号的机制
        // 在实际的B+树中，可能是通过某种结构存储在页面上
        return currentPage.getChildren().get(index).getPageId(); // 假设我们有子节点
    }

    public void close() {
        try {
            if (file != null) {
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("关闭文件失败: " + e.getMessage());
        }
    }
}


