package com.martin.packagescanner.util;

import java.net.URL;

/**
 * @author chen.tengfei <br>
 * @version 1.0<br>
 * @CreateDate 2017/10/16 <br>
 */
public class StringUtil {
    private StringUtil() {}

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public static String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * "cn.fh.lightning" -> "cn/fh/lightning"
     * @param string <br>
     * @return <br>
     */
    public static String dotToSplash(String string) {
        return string.replaceAll("\\.", "/");
    }

    /**
     * "cn/fh/lightning" -> "cn.fh.lightning"
     * @param string <br>
     * @return <br>
     */
    public static String splashToDot(String string) {
        return string.replaceAll("/", ".");
    }

    /**
     * "Apple.class" -> "Apple"
     */
    public static String trimExtension(String string) {
        int pos = string.lastIndexOf('.');
        if (-1 != pos) {
            return string.substring(0, pos);
        }

        return string;
    }
}
