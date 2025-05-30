package Storage.BPlusTree;

import Storage.Page.Page;
import Storage.Page.PageIO;
import Storage.Page.Tuple;
import Table.Schema;

import java.io.IOException;

/**
 * B+树
 * @author zhangtianlong
 */
public class BpTree {

    Page head;
    Page root;
    private String filePath;
    private PageIO pageIO;
    private int rootPageId;
    private Schema schema;

    public BpTree() {

    }

    public BpTree(String filePath) throws IOException {
        this.filePath = filePath;
        this.pageIO = new PageIO(filePath);
        this.rootPageId = pageIO.getRootPageId();
    }

    public Page getHead() {
        return head;
    }

    public void setHead(Page head) {
        this.head = head;
    }

    public Page getRoot() {
        return root;
    }

    public void setRoot(Page root) {
        this.root = root;
    }

    public boolean insert(Tuple tuple) throws IOException {
        if(!root.isFull(tuple)) {
            root.insert(tuple,this);
            return true;
        }
        return false;
    }

    public boolean remove(Tuple tuple) {
        return root.remove(tuple, this);
    }
}
