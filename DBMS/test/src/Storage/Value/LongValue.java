package Storage.Value;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zhangtianlong on 17/10/15.
 */
public class LongValue extends Value {

    private long i;

    public LongValue() {
    }

    public LongValue(long i) {
        this.i = i;
    }

    public long getLong() {
        return i;
    }

    public void setLong(long i) {
        this.i = i;
    }

    @Override
    public int getLength() {
        return 1 + 8;
    }

    @Override
    public byte getType() {
        return LONG;
    }

    @Override
    public int compare(Value value) {
        long target = ((LongValue) value).getLong();
        if (i > target) {
            return 1;
        } else if (i == target) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(i);
    }

    public Object getValue() {return i;}

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 大端序
        buffer.putLong(i);
        return buffer.array();
    }
}
