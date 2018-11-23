package club.usql.mybatis.generator.type;

import java.io.Serializable;

/**
 * @author far.liu
 */
public class AESString implements Serializable, Comparable<String>, CharSequence {
    private static final long serialVersionUID = -1L;
    private String value;

    public AESString(String value) {
        this.value = value;
    }

    public AESString() {

    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public int compareTo(String o) {
        return value.compareTo(o);
    }

    @Override
    public String toString() {
        return value;
    }
}
