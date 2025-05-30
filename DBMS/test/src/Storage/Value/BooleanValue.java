package Storage.Value;

import java.nio.ByteBuffer;

/**
 * @author zhangtianlong
 */
public class BooleanValue extends Value {

    private boolean b;

    public BooleanValue() {

    }

    public BooleanValue(boolean b) {
        this.b = b;
    }

    public boolean getBoolean() {
        return b;
    }

    public void setBoolean(boolean b) {
        this.b = b;
    }

    @Override
    public int getLength() {
        return 1 + 1;
    }

    @Override
    public byte getType() {
        return BOOLEAN;
    }

    @Override
    public int compare(Value value) {
        boolean target = ((BooleanValue) value).getBoolean();
        return (b == target) ? 0 : (b ? 1 : -1);
    }

    @Override
    public String toString() {
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }

    public Object getValue(){return b;}

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte)(b ? 1 : 0));
        return buffer.array();
    }
}
