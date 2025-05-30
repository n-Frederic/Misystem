package Storage.Page;

import Table.Table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PageManager {
    private static final int MAX_PAGES = 1000;  // 最大页数
    private static ArrayList<Page> pages;           // 页的集合
    private RandomAccessFile diskFile;          // 磁盘文件模拟存储
    private Set<Page> modifiedPages;            // 记录需要写回磁盘的页面


    public PageManager(String diskFileName) throws IOException {
        // 先初始化列表长度，全用null填满，防止set越界
        pages = new ArrayList<>(Collections.nCopies(MAX_PAGES,null));
        diskFile = new RandomAccessFile(diskFileName, "rw");
        modifiedPages = new HashSet<>();
    }

    // 获取页
    public Page getPage(int pageId) throws IOException {
        if(pageId==0){
            return getZeroPage();
        }

        Page page = pages.get(pageId);
        if(page!=null){
            return page;
        }

        // 如果页在内存中没有，则从磁盘加载
        byte[] pageData = loadPageFromDisk(pageId);
        page = Page.leafFromBytes(pageData);
        pages.set(pageId, page);
        return page;
    }

    public static ArrayList<Page> getPages() {
        return pages;
    }

    private Page getZeroPage() throws IOException{
        Page schemaPage = pages.get(0);
        if (schemaPage!=null) {
            return schemaPage;
        }

        byte[] pageData = loadPageFromDisk(0);
        schemaPage = Page.leafFromBytes(pageData);
        pages.set(0, schemaPage);
        return schemaPage;
    }

    // 创建新页
    public Page createPage(Table table) throws IOException {
        for (int i = 1; i < MAX_PAGES; i++) {
            if (pages.get(i) == null) {
                Page newPage = new Page(i);
                pages.set(i, newPage);
                return newPage;
            }
        }
        throw new RuntimeException("页面数已达最大值");
    }

    // 将页数据保存到磁盘
    public void savePageToDisk(Page page) throws IOException {
        byte[] pageData = page.leafToBytes();
        diskFile.seek(page.getPageId() * Page.PAGE_SIZE);
        diskFile.write(pageData);
        modifiedPages.remove(page);  // 保存到磁盘后，移除改动记录
    }

    // 从磁盘读取页
    private byte[] loadPageFromDisk(int pageId) throws IOException {
        diskFile.seek(pageId * Page.PAGE_SIZE);
        byte[] pageData = new byte[Page.PAGE_SIZE];
        diskFile.read(pageData);
        return pageData;
    }

    // 刷新所有已修改的页面到磁盘
    public void flushModifiedPages() throws IOException {
        for (Page page : modifiedPages) {
            savePageToDisk(page);
        }
        modifiedPages.clear();
    }

    // 标记一个页面已被修改，需要写回磁盘
    public void markPageModified(Page page) {
        modifiedPages.add(page);
    }

    // 关闭磁盘文件
    public void close() throws IOException {
        flushModifiedPages();
        diskFile.close();
    }
}

