package com.ofcoder.util;

/**
 * @author far.liu
 */
public class StringUtil {
    public static boolean isNotEmpty(CharSequence content) {
        return content != null && content.length() > 0;
    }

    public static boolean isEmpty(CharSequence content) {
        return !isNotEmpty(content);
    }

    public static boolean startWith(String content, String prefix) {
        if (isEmpty(content) || isEmpty(prefix)) {
            return false;
        }
        return content.startsWith(prefix);
    }
}
