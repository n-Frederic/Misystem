package Storage.Page;

import Storage.BPlusTree.BpTree;
import Storage.Value.*;
import Table.Field;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Page {
    public static final int PAGE_SIZE = 8 * 1024; // 8KB

    private int pageId;    // page 的 id

    boolean isLeaf;

    boolean isRoot;

    Page parent;

    int PageId;


    final int maxLength = 5;


    // 以上是内部，叶子公共属性

    Page previous;                                            ArrayList<Page> children;

    Page next;                                                ArrayList<Value> entries;

    ArrayList<Tuple> tuples;                                   // 以上是内部节点的属性



    // 以上是叶子节点的属性


    public Page(int pageId) {
        this.pageId = pageId;
        this.tuples = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

    public Page() {
        this.tuples = new ArrayList<>();
    }

    public Page(boolean isLeaf) {
        this.isLeaf = isLeaf;
        tuples = new ArrayList<>();
        if (!isLeaf) {
            children = new ArrayList<>();
            entries = new ArrayList<>();
        }
    }

    public Page(boolean isLeaf, boolean isRoot) {
        this(isLeaf);
        this.isRoot = isRoot;
    }






    public Page getParent() {
        return parent;
    }

    public void setParent(Page parent) {
        this.parent = parent;
    }

    public Page getNext() {
        return next;
    }

    /**
     * 得到行数组
     * @return 行数组
     */
    public List<Tuple> getTuples() {
        return tuples;
    }

    // 判断当前页是否是叶子页
    public boolean isLeaf() {
        return isLeaf;
    }

    // 获取子节点的页号
    public int getChildPageId(Page currentPage, int index) {
        // 如果当前页是叶子节点，没有子节点
        if (currentPage.isLeaf()) {
            throw new UnsupportedOperationException("叶子节点没有子节点");
        }

        // 如果当前是非叶子节点，从当前页的子节点列表中获取对应的子节点页号
        Page childPage = currentPage.getChildren().get(index);

        // 返回该子节点的页号
        return childPage.getPageId();
    }

    // 获取当前页的页号
    public int getPageId() {
        return pageId;
    }

    // 获取当前页的子节点列表
    public List<Page> getChildren() {
        return children;
    }

    // 设置子节点列表
    public void setChildren(ArrayList<Page> children) {
        this.children = children;
    }

    // 设置当前页的页号
    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public ArrayList<Value> getEntries() {
        return entries;
    }



    /**
     * 将某一行插入page
     * @param tuple 待插入的行
     * @return 插入是否成功
     */
    public boolean insertTuple(Tuple tuple) throws IOException {
        if (isFull(tuple)) return false;
        for(int i = 0; i < tuples.size(); i++) {
            if(tuples.get(i).compare(tuple) > 0) {
                tuples.add(i,tuple);
                return true;
            }
        }
        tuples.add(tuple);
        return true;
    }

    /**
     * 在page的tuples里获取tuple
     * @param tuple tuple
     * @return 一行信息
     */
    public Tuple get(Tuple tuple) {
        for (Tuple t : tuples) {
            if (t.compare(tuple) == 0) {
                return tuple;
            }
        }
        return null;
    }



    /**
     * 通过调用预估大小函数，判断page是否还能插入行数组
     * @param candidate 待插入的行
     * @return 是否能插入
     */
    public boolean isFull(Tuple candidate) throws IOException {
        int used = estimateCurrentSize();
        int added = candidate.toBytes().length;
        return used + added + 100 > PAGE_SIZE; // +100 留点空余防溢出
    }

    /**
     * 估计当前页的大小
     * @return 总的字节数
     */
    private int estimateCurrentSize() throws IOException {
        int total = 16;
        for (Tuple t : tuples) {
            total += t.toBytes().length;
        }
        return total;
    }






    /**
     * 叶子页的结构如下:
     * 页id（int 4b）
     * 是否是叶子节点 （bool, 1B)
     * 是否是根节点 （bool, 1B)
     * 父亲的页id （int, 4B)
     * 前一个叶子页的id （int, 4B)
     * 后一个叶子页的id (int, 4B)
     * 页的行数 （int 4b）
     * 真实数据开始的偏移量 begin (4b)
     * 真实数据结束的偏移量 end  (4b)
     * 每一行的开始的偏移量 (4b)
     * 实际数据
     * @return 字节数组
     */
    public byte[] leafToBytes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(PAGE_SIZE);
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(pageId);    // pageID
        dataOut.writeBoolean(isLeaf);
        dataOut.writeBoolean(isRoot);
        dataOut.writeInt(parent.PageId);
        dataOut.writeInt(previous.PageId);
        dataOut.writeInt(next.PageId);
        dataOut.writeInt(tuples.size());     // 行数
        dataOut.writeInt(0);      // 真实数据开始位置的偏移量，占位
        dataOut.writeInt(0);   // 真实数据结束位置的偏移量，占位

        int begin; // 真实数据开始位置的偏移量
        int end; // 真实数据结束位置的偏移量

        // 实时维护偏移量
        int currentOffset = 36;

        ArrayList<byte[]> info = new ArrayList<>();

        // 写入偏移量表
        for(Tuple tuple : tuples) {
            dataOut.writeInt(currentOffset);
            byte[] bytes = tuple.toBytes();
            info.add(bytes);
            currentOffset += 4;
        }

        // 此时偏移量在真实数据开始的位置
        begin = currentOffset;

        // 写入实际数据
        for (byte[] b : info) {
            dataOut.write(b);
            currentOffset += b.length;
        }

        // 此时偏移量在真实数据结束的位置
        end = currentOffset;

        byte[] bytes = out.toByteArray();
        // 写 begin 到 bytes[4] ~ bytes[7]
        bytes[29] = (byte) ((begin >> 24) & 0xFF);
        bytes[30] = (byte) ((begin >> 16) & 0xFF);
        bytes[31] = (byte) ((begin >> 8) & 0xFF);
        bytes[32] = (byte) (begin & 0xFF);

        // 写 end 到 bytes[8] ~ bytes[11]
        bytes[33]  = (byte) ((end >> 24) & 0xFF);
        bytes[34]  = (byte) ((end >> 16) & 0xFF);
        bytes[35] = (byte) ((end >> 8) & 0xFF);
        bytes[36] = (byte) (end & 0xFF);

        return bytes;
    }

    /**
     * 叶子页的结构如下:
     * 页id（int 4b）
     * 是否是叶子节点 （bool, 1B)
     * 是否是根节点 （bool, 1B)
     * 父亲的页id （int, 4B)
     * 前一个叶子页的id （int, 4B)
     * 后一个叶子页的id (int, 4B)
     * 页的行数 （int 4b）
     * 真实数据开始的偏移量 begin (4b)
     * 真实数据结束的偏移量 end  (4b)
     * 每一行的开始的偏移量 (4b)
     * 实际数据
     * @return 字节数组
     */
    public static Page leafFromBytes(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        int pageId = in.readInt();
        boolean isLeaf = in.readBoolean();
        boolean isRoot = in.readBoolean();
        int parentId = in.readInt();
        int previousId = in.readInt();
        int nextId = in.readInt();
        int rowCount = in.readInt();
        int begin = ((bytes[4] & 0xFF) << 24) |
                ((bytes[5] & 0xFF) << 16) |
                ((bytes[6] & 0xFF) << 8) |
                (bytes[7] & 0xFF);

        int end = ((bytes[8] & 0xFF) << 24) |
                ((bytes[9] & 0xFF) << 16) |
                ((bytes[10] & 0xFF) << 8) |
                (bytes[11] & 0xFF);

        ArrayList<Integer> offsets = new ArrayList<>(rowCount);
        for(int i = 0; i < rowCount; i++) {
            offsets.add(in.readInt());
        }

        Page page = new Page(pageId);
        for (int i = 0; i < rowCount - 1; i++) {
            int rowStart = offsets.get(i);
            int rowEnd = (i + 1 < rowCount) ? offsets.get(i + 1) : end;
            byte[] rowData = Arrays.copyOfRange(bytes, rowStart, rowEnd);
            Tuple tuple = Tuple.fromBytes(rowData);
            page.tuples.add(tuple);
        }

        page.isLeaf = isLeaf;
        page.isRoot = isRoot;
        page.parent = new Page(parentId);
        page.previous = new Page(previousId);
        page.next = new Page(nextId);

        return page;
    }




    /**
     * 第零页的结构
     * 根页的页码 ( int 4B )
     * 目前的最高页码 （ int 4B ）
     * 中间节点允许的最多key数量 （ int 4B ）
     * 列的当前数量 （int 4B)
     * 列名 （最多100个字段，每列是 int（列名长度） + n 字节（UTF-8 字节串）)
     * 列类型 （ int 4B*100 , 最多允许400B)
     */

    public byte[] ZeroToBytes(ArrayList<Field> fields) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(PAGE_SIZE);
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(1);
        dataOut.writeInt(1);
        dataOut.writeInt(5);

        int size = fields.size();
        dataOut.writeInt(size);

        // 写入列名（以每个列名长度 + UTF-8 字符串形式写入）
        int fieldCount = Math.min(size, 100);
        for (int i = 0; i < fieldCount; i++) {
            String name = fields.get(i).getName();
            byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);

            // 写入列名长度 + 内容
            dataOut.writeInt(nameBytes.length);
            dataOut.write(nameBytes);
        }

        // 如果字段数不足 100 个，补空列名（写入0长度）
        for (int i = fieldCount; i < 100; i++) {
            dataOut.writeInt(0);
        }
        // 写入字段类型（int，4B * n，最多允许400B)
        for (int i = 0; i < fieldCount; i++) {
            String fieldType = fields.get(i).getType();  // 获取字段类型（String 类型）
            int fieldTypeInt = mapFieldTypeToInt(fieldType);  // 将字段类型转换为数字
            dataOut.writeInt(fieldTypeInt);  // 写入字段类型
        }

        // 填充剩余的空间（如果字段数小于 100，填充 0）
        for (int i = fieldCount; i < 100; i++) {
            dataOut.writeInt(0);  // 填充 0
        }

        return out.toByteArray();
    }

    private int mapFieldTypeToInt(String fieldType) {
        return switch (fieldType.toUpperCase()) {
            case "STRING" -> 1;
            case "INT" -> 2;
            case "LONG" -> 3;
            case "BOOLEAN" -> 4;
            case "NULL" -> 0;
            default -> -1;  // 未知类型
        };
    }




    /**
     * 非叶子页的结构
     * 页码 （ int 4B ）
     * 是否是叶子节点 （bool, 1B)
     * 是否是根节点 （bool, 1B)
     * 父亲的页id （int, 4B)
     * 目前孩子有几个 （int, 4B)
     * 记录的索引的类型 （目前为主键）（int, 4B)
     * 孩子页的行中主键的最小值序列 (value 不确认多长  *5）
     * 孩子页的页码 （int 4B*5 ）
     */
    public byte[] InnerToBytes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(PAGE_SIZE);
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(pageId);    // pageID
        dataOut.writeBoolean(isLeaf);
        dataOut.writeBoolean(isRoot);
        dataOut.writeInt(parent.PageId);
        dataOut.writeInt(children.size());
        dataOut.writeInt(entries.getFirst().getType());

        for(Value value : entries) {
            dataOut.write(value.toBytes());
        }

        for(Page page : children) {
            dataOut.writeInt(page.PageId);
        }

        return out.toByteArray();
    }

    /**
     * 非叶子页的结构
     * 页码 （ int 4B ）
     * 是否是叶子节点 （int 4B)
     * 是否是根节点 （int, 4B)
     * 父亲的页id （int, 4B)
     * 记录的索引的类型 （目前为主键）（int, 4B)
     * 孩子页的行中主键的最小值序列 （ 不确定长度 * 5 ）
     * 孩子页的页码 （int 4B*5 ）
     */
    public static Page InnerFromBytes(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        int pageId = in.readInt();
        Page page = new Page(pageId);

        boolean isLeaf = in.readBoolean();
        boolean isRoot = in.readBoolean();
        int parentId = in.readInt();
        int size = in.readInt();
        int type = in.readInt();

        switch(type) {
            case 1:
                for(int i = 0; i < size; i++) {
                    StringBuilder sb = new StringBuilder();
                    byte b;
                    while ((b = in.readByte()) != 0) {
                        sb.append((char) b);
                    }
                    page.entries.add(new StringValue(sb.toString()));
                }
                break;
            case 2:
                for(int i = 0; i < size; i++) {
                    page.entries.add(new IntValue(in.readInt()));
                }
                break;
            case 3:
                for(int i = 0; i < size; i++) {
                    page.entries.add(new LongValue(in.readLong()));
                }
                break;
            case 4:
                for(int i = 0; i < size; i++) {
                    page.entries.add(new BooleanValue(in.readBoolean()));
                }
                break;
        }

        for(int i = 0; i < size; i++) {
            page.children.add(new Page(in.readInt()));
        }


        page.isLeaf = isLeaf;
        page.isRoot = isRoot;
        page.parent = new Page(parentId);

        return page;
    }





    /**
     * 在树中插入一个节点
     * @param key 待插入的行
     * @param tree B+树
     */

    public void insert(Tuple key, BpTree tree) {
        if (isLeaf) {
            if (!isLeafToSplit()) {
//                System.out.println("直接插入叶节点");
                insertInLeaf(key.getPrimaryV());
            } else {
//                System.out.println("插入叶节点,且叶节点分裂");
                //需要分裂为左右两个节点
                Page left = new Page(true);
                Page right = new Page(true);
                if (previous != null) {
                    left.previous = previous;
                    previous.next = left;
                } else {
                    tree.setHead(left);
                }
                if (next != null) {
                    right.next = next;
                    next.previous = right;
                }
                left.next = right;
                right.previous = left;
                // for GC
                previous = null;
                next = null;
                // 插入后再分裂
                insertInLeaf(key.getPrimaryV());

                int leftSize = getUpper(entries.size(), 2);
                int rightSize = entries.size() - leftSize;
//                System.out.printf("leaf key left:%d  right:%d\n", leftSize, rightSize);
                // 左右节点拷贝
                for (int i = 0; i < leftSize; i++) {
                    left.entries.add(entries.get(i));
                }
                for (int i = 0; i < rightSize; i++) {
                    right.entries.add(entries.get(leftSize + i));
                }
                // 不是根节点
                if (!isRoot) {
                    // 调整父子节点关系
                    // 寻找当前节点在父节点的位置
                    System.out.println("parent children is null:" + (parent.children == null));

                    int index = parent.children.indexOf(this);
//                    System.out.println("parent children size:" + parent.children.size());
//                    System.out.println("index:" + index);

                    // 删除当前指针
                    parent.children.remove(this);
                    left.setParent(parent);
                    right.setParent(parent);
                    // 将分裂后节点的指针添加到父节点
                    parent.children.add(index, left);
                    parent.children.add(index + 1, right);
                    // for GC
                    entries = null;
                    children = null;

                    // 父节点[非叶子节点]中插入关键字，是右边的第一位
                    parent.insertInParent(right.entries.getFirst());
                    System.out.println("父节点插入key");
                    parent.updateNode(tree);
                    // for GC
                    parent = null;
                } else {
                    // 是根节点
                    System.out.println("生成新的根节点");
                    isRoot = false;
                    Page rootPage = new Page(false, true);
                    tree.setRoot(rootPage);
                    left.parent = rootPage;
                    right.parent = rootPage;
                    rootPage.children.add(left);
                    rootPage.children.add(right);
                    // for GC
                    entries = null;
                    children = null;
                    // 根节点插入关键字
                    rootPage.insertInParent(right.entries.getFirst());
                }
            }
        } else {
            // 如果不是叶子节点,沿着指针向下搜索
            if (isRoot) {
                System.out.println("根节点,向下搜索");
            }
            if (key.getPrimaryV().compare(entries.getFirst()) < 0) {
                System.out.println("中间节点,向下搜索");
                children.getFirst().insert(key, tree);
            } else if (key.getPrimaryV().compare(entries.getLast()) >= 0) {
                System.out.println("中间节点,向下搜索");
                children.getLast().insert(key, tree);
            } else {
                // 遍历比较
                System.out.println("中间节点,向下搜索");
                int left = 0;
                int right = entries.size() - 1;
                Value keyValue = key.getPrimaryV();

                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (keyValue.compare(entries.get(mid)) < 0) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                }
                children.get(left).insert(key, tree);
            }
        }
    }

    public boolean remove(Tuple key, BpTree tree) {
        boolean isFound = false;
        if (isLeaf) {
            // 如果是叶子节点
            if (!contains(key)) {
                // 不包含关键字
                return false;
            }
            // 是 叶子节点 且是 根节点,直接删除
            if (isRoot) {
                if (removeInLeaf(key)) {
                    isFound = true;
                }
            } else {
                if (canRemoveDirectInLeaf()) {
                    // 可以在叶节点中直接删除
                    if (removeInLeaf(key)) {
                        isFound = true;
                    }
                } else {
                    // 如果当前关键字不够,并且前节点有足够的关键字,从前节点借
                    if (leafCanBorrow(previous)) {
                        if (removeInLeaf(key)) {
                            borrowLeafPrevious();
                            isFound = true;
                        }
                    } else if (leafCanBorrow(next)) {
                        if (removeInLeaf(key)) {
                            borrowLeafNext();
                            isFound = true;
                        }
                        // 从后兄弟节点借
                    } else {
                        // 合并叶子节点, 先合并后删除
                        Page tmpParent = this.parent;
                        if (leafCanMerge(previous)) {
                            // 和前叶子节点合并
                            mergeToPreLeaf(this.previous, this);
                            if (previous.removeInLeaf(key)) {
                                isFound = true;
                            }
                            // 删除在父节点中的key
                            int parentKeyIdx = getMiddleKeyIdxInParent(this);
                            parent.entries.remove(parentKeyIdx);
                            // 删除在父节点中的指针
                            parent.children.remove(this);
                            // for GC
                            parent = null;
                            entries = null;
                            // 更新 叶节点链表
                            if (this.next != null) {
                                Page tmp = this;
                                tmp.previous.next = tmp.next;
                                tmp.next.previous = tmp.previous;
                                tmp.previous = null;
                                tmp.next = null;
                            } else {
                                this.previous.next = null;
                                this.previous = null;
                            }
                        } else if (leafCanMerge(next)) {
                            // 和后叶子节点合并
                            mergeToPreLeaf(this, this.next);
                            if (removeInLeaf(key)) {
                                isFound = true;
                            }
                            // 删除在父节点中的key
                            int parentKeyIdx = getMiddleKeyIdxInParent(this.next);
                            parent.entries.remove(parentKeyIdx);
                            // 删除在父节点中的指针
                            parent.children.remove(this.next);
                            // for GC
                            next.parent = null;
                            next.entries = null;
                            // 更新 叶节点链表
                            if (this.next.next != null) {
                                Page tmp = this.next;
                                this.next = tmp.next;
                                tmp.next.previous = this;
                                tmp.previous = null;
                                tmp.next = null;
                            } else {
                                this.next.previous = null;
                                this.next = null;
                            }
                        }
                        tmpParent.updateRemove(tree);
                    }
                }
            }
        } else {
            // 非叶子节点,继续向下搜索
            if (key.getPrimaryV().compare(entries.getFirst()) < 0) {
                if (children.getFirst().remove(key, tree)) {
                    isFound = true;
                }
            } else if (key.getPrimaryV().compare(entries.getLast()) >= 0) {
                if (children.getLast().remove(key, tree)) {
                    isFound = true;
                }
            } else {
                for (int i = 0; i < (entries.size() - 1); i++) {
                    if (key.getPrimaryV().compare(entries.get(i)) >= 0 && key.getPrimaryV().compare(entries.get(i + 1)) < 0) {
                        if (children.get(i + 1).remove(key, tree)) {
                            isFound = true;
                            break;
                        }
                    }
                }
            }
        }
        return isFound;
    }

    /**
     * 中间节点删除后的更新操作
     */
    private void updateRemove(BpTree tree) {
        int half = getUpper(maxLength, 2);
        if (children.size() < half || children.size() < 2) {
            if (isRoot) {
                if (children.size() >= 2) {
                    return;
                } else {
                    // 如果根节点只有一个指针,则删除 根节点,让其孩子节点作为根节点
                    Page rootPage = children.getFirst();
                    tree.setRoot(rootPage);
                    rootPage.isRoot = true;
                    rootPage.parent = null;
                    // for GC
                    this.entries = null;
                    this.children = null;
                }
            } else {
                // 计算前后兄弟节点
                int curIdx = parent.children.indexOf(this);
                int preIdx = curIdx - 1;
                int nextIdx = curIdx + 1;
                Page preNode = null;
                Page nextNode = null;
                if (preIdx >= 0) {
                    preNode = parent.children.get(preIdx);
                }
                if (nextIdx < parent.children.size()) {
                    nextNode = parent.children.get(nextIdx);
                }
                if (middleNodeCanBorrow(preNode)) {
                    // 从前节点借
                    borrowMiddleNodePrevious(preNode);
                } else if (middleNodeCanBorrow(nextNode)) {
                    // 从后继节点借
                    borrowMiddleNodeNext(nextNode);
                } else {
                    // 和兄弟节点合并
                    Page tmpParent = this.parent;
                    if (middleNodeCanMerge(preNode)) {
                        // 与前节点合并
                        mergeToPreMiddleNode(preNode, this);
                        int parentKeyIdx = getMiddleKeyIdxInParent(this);
                        this.parent.entries.remove(parentKeyIdx);
                        this.parent.children.remove(parentKeyIdx + 1);
                        // for GC
                        this.parent = null;
                        this.entries = null;
                        this.children = null;
                    } else if (middleNodeCanMerge(nextNode)) {
                        mergeToPreMiddleNode(this, nextNode);
                        int parentKeyIdx = getMiddleKeyIdxInParent(nextNode);
                        this.parent.entries.remove(parentKeyIdx);
                        this.parent.children.remove(parentKeyIdx + 1);
                        // for GC
                        nextNode.parent = null;
                        nextNode.entries = null;
                        nextNode.children = null;
                    }
                    tmpParent.updateRemove(tree);
                }
            }
        }
    }

    /**
     * 从前兄弟节点中借
     */
    private void borrowMiddleNodePrevious(Page preNode) {
        /**
         *        20
         * 3  7        30
         * ---------------------
         *        7
         *    3        20   30
         */
        int parentKeyIdx = getMiddleKeyIdxInParent(this);
        // 父节点中下沉的 key
        Value downKey = parent.entries.get(parentKeyIdx);
        this.entries.add(0, downKey);
        // 从父节点中删除 key
        parent.entries.remove(parentKeyIdx);

        int preSize = preNode.entries.size();
        // 前节点中提升到父节点的key
        Value upKey = preNode.entries.get(preSize - 1);
        parent.entries.add(parentKeyIdx, upKey);
        // 删除提升节点
        preNode.entries.remove(preSize - 1);
        // 前节点的最后一个指针后移到当前节点
        int preChildSize = preNode.children.size();
        Page borrowPoint = preNode.children.get(preChildSize - 1);
        this.children.add(0 , borrowPoint);
        preNode.children.remove(preChildSize - 1);
        borrowPoint.parent = this;
    }

    /**
     * 从后继兄弟节点中借
     */
    private void borrowMiddleNodeNext(Page nextPage) {
        /**
         *        20
         *   7        30   40
         * ---------------------
         *            30
         *   7   20        40
         */
        int parentKeyIdx = getMiddleKeyIdxInParent(nextPage);
        Value downKey = parent.entries.get(parentKeyIdx);
        this.entries.add(downKey);
        this.parent.entries.remove(parentKeyIdx);

        Value upKey = nextPage.entries.getFirst();
        this.parent.entries.add(parentKeyIdx, upKey);
        nextPage.entries.removeFirst();
        // 后继节点的第一个指针移到当前节点最后面
        Page borrowPoint = nextPage.children.getFirst();
        this.children.add(borrowPoint);
        nextPage.children.removeFirst();
    }

    /**
     * 将后一个节点中的关键字 合并到 前节点中
     */
    private void mergeToPreLeaf(Page first, Page sec) {
        first.entries.addAll(sec.entries);
    }

    /**
     * 将后一个中间节点的关键字和指针复制到 前一个中间节点中
     */
    private void mergeToPreMiddleNode(Page first, Page sec) {
        int parentKeyIdx = getMiddleKeyIdxInParent(sec);
        // 将父节点关键字下沉
        first.entries.add(first.parent.entries.get(parentKeyIdx));

        for (int i = 0; i < sec.entries.size(); i++) {
            first.entries.add(sec.entries.get(i));
        }
        // sec的指针复制
        for (int i = 0; i < sec.children.size(); i++) {
            // 变更父亲节点
            sec.children.get(i).parent = first;
            first.children.add(sec.children.get(i));
        }
    }

    /**
     * 从前兄弟叶子节点 借
     */
    private void borrowLeafPrevious() {
        int size = previous.entries.size();
        Value borrowKey = previous.entries.get(size - 1);
        previous.entries.remove(size - 1);
        entries.add(0, borrowKey);
        // 获取 当前节点指针 和 前节点指针 之间的 关键字 在父节点中的位置
        int parentEntryIdx = getMiddleKeyIdxInParent(this);
        parent.entries.remove(parentEntryIdx);
        parent.entries.add(parentEntryIdx, borrowKey);
    }

    /**
     * 从后兄弟叶子节点 借
     */
    private void borrowLeafNext() {
        Value borrowKey = next.entries.getFirst();
        next.entries.removeFirst();
        entries.add(borrowKey);
        // 获取 当前节点指针 和 后节点指针 之间的 关键字 在父节点中的位置
        int parentEntryIdx = getMiddleKeyIdxInParent(this.next);
        parent.entries.remove(parentEntryIdx);
        // 将后叶子节点 第1个关键字(从0开始计数)提升到父节点中
        // 由于前面已经删除第0个关键字,所以这里添加的还是0
        parent.entries.add(parentEntryIdx, next.entries.getFirst());
    }

    /**
     * 获取 当前节点指针 和 前节点指针 之间的 关键字 在父节点中的位置
     */
    private int getMiddleKeyIdxInParent(Page node) {
        int index = parent.children.indexOf(node);
        return index - 1;
    }

    /**
     * 兄弟叶节点是否能够借出
     */
    private boolean leafCanBorrow(Page node) {
        if (node != null) {
            int min = getUpper(maxLength - 1, 2);
            return node.entries.size() > min && node.parent == parent;
        }
        return false;
    }

    /**
     * 兄弟中间节点是否可以借
     */
    private boolean middleNodeCanBorrow(Page node) {
        if (node != null) {
            int min = getUpper(maxLength, 2);
            return node.children.size() > min && node.parent == parent;
        }
        return false;
    }

    /**
     * 叶子节点是否可以合并
     */
    private boolean leafCanMerge(Page node) {
        if (node != null) {
            return (entries.size() + node.entries.size() - 1) <= (maxLength - 1)
                    && parent == node.parent;
        }
        return false;
    }

    /**
     * 中间节点是否可以合并
     */
    private boolean middleNodeCanMerge(Page node) {
        if (node != null) {
            return (entries.size() + node.entries.size() + 1) <= (maxLength - 1)
                    && parent == node.parent;
        }
        return false;
    }


    /**
     * 上取整
     */
    private int getUpper(int x, int y) {
        if (y == 2) {
            int remainder = x & 1;
            if (remainder == 0) {
                return x >> 1;
            } else {
                return (x >> 1) + 1;
            }
        } else {
            int remainder = x % y;
            if (remainder == 0) {
                return x / y;
            } else {
                return x / y + 1;
            }
        }
    }

    /**
     * 关键字是否可以直接在叶节点中删除
     */
    private boolean canRemoveDirectInLeaf() {
        if (isLeaf) {
            int maxKey = maxLength - 1;
            int remainder = maxKey % 2;
            int half;
            if (remainder == 0) {
                half = maxKey / 2;
            } else {
                half = maxKey / 2 + 1;
            }
            if ((entries.size() - 1) < half) {
                return false;
            } else {
                return true;
            }
        } else {
            throw new UnsupportedOperationException("it isn't leaf node.");
        }
    }

    /**
     * 直接在叶子节点中删除,不改变树结构
     */
    private boolean removeInLeaf(Tuple key) {
        int index = -1;
        boolean isFound = false;
        for (int i = 0; i < entries.size(); i++) {
            if (key.getPrimaryV().compare(entries.get(i)) == 0) {
                index  = i;
                isFound = true;
                break;
            }
        }
        if (index != -1) {
            entries.remove(index);
        }
        return isFound;
    }

    /**
     * 判断当前节点是否包含关键字
     */
    private boolean contains(Tuple key) {
        for (Value value: entries) {
            if (key.getPrimaryV().compare(value) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 非叶节点插入关键字后,检查是否需要分裂
     */
    private void updateNode(BpTree tree) {
        // 需要分裂
        if (isNodeToSplit()) {
            System.out.println("非叶节点插入关键字后,需要分裂");
            Page left = new Page(false);
            Page right = new Page(false);

            int pLeftSize = getUpper(children.size(), 2);
            int pRightSize = children.size() - pLeftSize;   //fix bug

            // 提升到父节点的关键字
            Value keyToParent = entries.get(pLeftSize - 1);

            System.out.printf("middle node p left:%d  right:%d\n", pLeftSize, pRightSize);
            // 复制左边的关键字
            for (int i = 0; i < (pLeftSize - 1); i++) {
                left.entries.add(entries.get(i));
            }
            // 复制左边的指针
            for (int i = 0; i < pLeftSize; i++) {
                left.children.add(children.get(i));
                left.children.get(i).setParent(left);
            }

            // 复制右边关键字,右边的第一个关键字提升到父节点
            for (int i = 0;  i < (pRightSize - 1); i++) {
                right.entries.add(entries.get(pLeftSize + i));
            }
            // 复制右边的指针
            for (int i = 0; i < pRightSize; i++) {
                right.children.add(children.get(pLeftSize + i));
                right.children.get(i).setParent(right);     // fix index bug
            }

            if (!isRoot) {
                System.out.println("current is root:" + false);
                System.out.println("非叶节点的父节点插入key");
                int index = parent.children.indexOf(this);
                parent.children.remove(index);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(index, left);
                parent.children.add(index + 1, right);
                // 插入关键字
//                parent.insertInParent(keyToParent);
                parent.entries.add(index, keyToParent);
                parent.updateNode(tree);
                entries.clear();
                children.clear();
                entries = null;
                children = null;
                parent = null;
            } else {
                // 是根节点
                System.out.println("current is root:" + true);
                System.out.println("parent null:" + (parent == null));
                isRoot = false;
                Page rootPage = new Page(false, true);
                tree.setRoot(rootPage);
                left.parent = rootPage;
                right.parent = rootPage;
                rootPage.children.add(left);
                rootPage.children.add(right);
                children.clear();
                entries.clear();
                children = null;
                entries = null;
                // 插入关键字
//                rootPage.insertInParent(keyToParent);
                rootPage.entries.add(keyToParent);
            }
        }
    }

    /**
     * 叶子节点是否需要分裂,
     * 用于插入前进行判断
     */
    private boolean isLeafToSplit() {
        if (isLeaf) {
            if (entries.size() >= (maxLength - 1)) {
                return true;
            } else return false;
        } else {
            throw new UnsupportedOperationException("the node is not leaf.");
        }
    }

    /**
     * 中间节点是否需要分裂,
     * 已经插入指针和关键字
     */
    private boolean isNodeToSplit() {
        // 由于是先插入关键字,所以不需要[=]
        if (isLeaf) {
            throw new UnsupportedOperationException("error access to leaf");
        }
        if (children.size() > maxLength) {
            return true;
        }
        return false;
    }


    /**
     * 插入到当前叶子节点中,不分裂
     */
    private void insertInLeaf(Value key) {
        if (!isLeaf) {
            throw new UnsupportedOperationException("can't insert into middle node.");
        }

        // 插入entry的适当位置，保持有序
        for(int i = 0; i < entries.size(); i++) {
            if(entries.get(i).compare(key) > 0) {
                entries.add(i,key);
                break;
            }
        }
    }

    /**
     * 插入到非叶子节点中,不分裂
     */
    private void insertInParent(Value key){
        if (isLeaf) {
            throw new UnsupportedOperationException("can't insert into leaf node.");
        }

        for(int i = 0; i < entries.size(); i++) {
            if(entries.get(i).compare(key) > 0) {
                entries.add(i,key);
                break;
            }
        }
    }


    /**
     * 验证节点是否满足 point数 = key数 + 1
     */
    private boolean checkKeyPointRelation() {
        if (!isLeaf) {
            if ((entries.size() + 1) == children.size()) {
                return true;
            } else {
                System.out.println("不满足 point数 = key数 + 1");
                return false;
            }
        }
        return true;
    }

    /**
     * 检查关键字是否有序
     */
    private boolean keyIsOrder() {
        for (int i = 0; i < (entries.size() - 1); i++) {
            if (entries.get(i).compare(entries.get(i + 1)) > 0) {
                System.out.println("节点关键字 不 有序");
                return false;
            }
        }
        return true;
    }

    /**
     * 验证节点是否符合B+树 定义
     */
    boolean validate() {
        if (checkKeyPointRelation()) {
            // 检查关键字是否有序
            if (keyIsOrder()) {
                if (isLeaf) {
                    if (isRoot) {
                        // 是页节点 且是 根节点
                        return true;
                    } else {
                        // 是叶子节点 不是 根节点
                        if (entries.size() < getUpper(maxLength - 1, 2) || entries.size() > (maxLength - 1)) {
                            System.out.println("叶节点key数 不合法");
                            return false;
                        }
                        if (parent == null) {
                            System.out.println("叶子节点的父节点的指针为空");
                            return false;
                        }
                    }
                    return true;
                } else {
                    // 非叶子节点
                    // 先检查指针数是否符合
                    if (isRoot) {
                        if (children.size() < 2) {
                            System.out.printf("根节点指针数 不合法, children:%d\n", children.size());
                            return false;
                        }
                    } else {
                        if (children.size() < getUpper(maxLength, 2) || children.size() > maxLength) {
                            System.out.printf("非叶节点指针数 不合法, children:%d\n", children.size());
                            System.out.printf("entry:%d\n", entries.size());
                            return false;
                        }
                        for (Page node : children) {
                            if (node.parent == null) {
                                System.out.println("中间节点的父指针为空");
                                return false;
                            }
                        }
                    }
                    for (Page node : children) {
                        if (node.validate()) {
                            // 子节点符合B+树定义
                            int pIdx = children.indexOf(node);
                            Value minChildKey = node.entries.getFirst();
                            Value maxChildKey = node.entries.getLast();
                            if (pIdx == 0) {
                                // 第一个指针
                                boolean isValid = maxChildKey.compare(entries.getFirst()) < 0;
                                if (!isValid) {
                                    System.out.println("子节点与父节点不满足大小关系");
                                    return false;
                                }
                            } else if (pIdx == (children.size() - 1)) {
                                // 最后一个指针
                                boolean isValid = minChildKey.compare(entries.getLast()) >= 0;
                                if (!isValid) {
                                    System.out.println("子节点与父节点不满足大小关系");
                                    return false;
                                }
                            } else {
                                Value preKey = entries.get(pIdx - 1);
                                Value nextKey = entries.get(pIdx);
                                boolean isValid = minChildKey.compare(preKey) >= 0
                                        && maxChildKey.compare(nextKey) < 0;
                                if (!isValid) {
                                    System.out.println("子节点与父节点不满足大小关系");
                                    return false;
                                }
                            }
                        } else {
                            // 子节点违反B+树定义
                            System.out.println("子节点违反B+树定义");
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                // 关键字不有序
                return false;
            }
        } else {
            return false;
        }
    }
}
