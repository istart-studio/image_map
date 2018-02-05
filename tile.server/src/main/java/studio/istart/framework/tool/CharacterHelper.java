package studio.istart.framework.tool;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Se7en
 * @date 2016/5/23
 * Desc:字符帮助类
 */
@Log4j2
public class CharacterHelper {

    public static class MD5 {

        public static String encode(String input) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(input.getBytes());
            return byteToHexString(b);
        }

        // 用来将字节转换成 16 进制表示的字符
        private final static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        /**
         * 把byte[]数组转换成十六进制字符串表示形式
         *
         * @param tmp 要转换的byte[]
         * @return 十六进制字符串表示形式
         */
        private static String byteToHexString(byte[] tmp) {
            String s;
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串
            return s;
        }
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param oldCharset 原编码
     * @return UTF-8的编码结果集
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset)
        throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes(oldCharset);
            //用新的字符编码生成字符串
            return new String(bs, Charsets.UTF_8);
        }
        return null;
    }


    public static int convertToInt(String value) {
        try {
            if (!Strings.isNullOrEmpty(value)) {
                return Integer.parseInt(value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    public static float convertToFloat(String value) {
        try {
            if (!Strings.isNullOrEmpty(value)) {
                return Float.parseFloat(value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
