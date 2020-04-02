package com.ofcoder.util;

import com.ofcoder.exception.GeneratorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author far.liu
 */
public class PropertyUtil {
    private static Properties properties = new Properties();

    private static final String PROPERTIES_RESOURCE = "application.yml";

    static {
        InputStream resource = null;
        try {
            resource = PropertyUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_RESOURCE);
            properties.load(resource);
        } catch (IOException e) {
            throw new GeneratorException(e.getMessage(), e);
        } finally {
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static class Param {
        public static String PREFIX = "generator.";

        public static class Enc {
            public static String PREFIX = Param.PREFIX + "enc.db.";
            public static String ENCRYPT_KEY = PREFIX + "aeskey";
            public static String ENCRYPT_E = PREFIX + "aese";
        }

        public static class Assist {
            public static String PREFIX = Param.PREFIX + "assist.";
            public static String WHERE = PREFIX + "where";
        }

    }
}
