package Storage.Value;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NullValue extends Value{
    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public int compare(Value value) {
        return -1;
    }

    public Object getValue() {return null;}

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 大端序
        buffer.put((byte)0);
        return buffer.array();
    }
}
