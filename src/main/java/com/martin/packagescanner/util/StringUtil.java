package com.martin.packagescanner.util;

import java.io.File;
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
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh" <br>
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public static String getRootPath(URL url) {
        String urlPath = url.getPath();
        int pos = urlPath.indexOf('!');

        if (-1 == pos) {
            return urlPath;
        }

        return urlPath.substring(5, pos);
    }

    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * <p>
     * this method will decode the URL.
     * Syntax such as <code>file:///my%20docs/file.txt</code> will be
     * correctly decoded to <code>/my docs/file.txt</code>.
     *
     * @param url  the file URL to convert, null returns null
     * @return the equivalent <code>File</code> object, or <code>null</code>
     * @throws IllegalArgumentException if the file is incorrectly encoded
     */
    public static File toFile(URL url) {
        if (url == null) {
            return null;
        } else {
            String filename = getRootPath(url).replace('/', File.separatorChar);
            int pos =0;
            while ((pos = filename.indexOf('%', pos)) >= 0) {
                if (pos + 2 < filename.length()) {
                    String hexStr = filename.substring(pos + 1, pos + 3);
                    char ch = (char) Integer.parseInt(hexStr, 16);
                    filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
                }
            }
            return new File(filename);
        }
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
