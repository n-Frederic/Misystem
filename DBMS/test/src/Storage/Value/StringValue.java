package Storage.Value;

import java.nio.charset.StandardCharsets;

/**
 * Created by zhangtianlong on 17/10/15.
 */
public class StringValue extends Value {

    private String s;

    public StringValue() {
    }

    public StringValue(String s) {
        this.s = s;
    }

    public String getString() {
        return s;
    }

    public void setString(String s) {
        this.s = s;
    }

    // [type][length][data]
    @Override
    public int getLength() {
        return 1 + 4 + s.length();
    }

    @Override
    public byte getType() {
        return STRING;
    }

    @Override
    public int compare(Value value) {
        String target = ((StringValue) value).getString();
        int result = s.compareTo(target);
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public Object getValue() {return s;}

    @Override
    public byte[] toBytes() {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bytes.length+1];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        result[bytes.length] = 0;
        return result;
    }
}
