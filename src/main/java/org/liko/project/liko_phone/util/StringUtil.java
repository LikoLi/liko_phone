package org.liko.project.liko_phone.util;

import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;

/**
 * 短信工具类
 */
public class StringUtil {
    /**
     * 将PDU编码的十六进制字符串 如“4F60597DFF01” 转换成unicode "\u4F60\u597D\uFF01"
     * @param str 要转化的字符串
     * @return 转换后的十六进制字符串
     */
    public static String analyseStr(String str) {
        StringBuffer sb = new StringBuffer();
        if (!(str.length() % 4 == 0))
            return str;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 || i % 4 == 0) {
                sb.append("\\u");
            }
            sb.append(str.charAt(i));
        }
        return Unicode2GBK(sb.toString());
    }

    /**
     * 将unicode编码 "\u4F60\u597D\uFF01" 转换成中文 "你好！"
     * @param str 要转化的字符串
     * @return 转换后的中文字符串
     */
    public static String Unicode2GBK(String str) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        while (index < str.length()) {
            if (!"\\u".equals(str.substring(index, index + 2))) {
                buffer.append(str.charAt(index));
                index++;
                continue;
            }
            String charStr = "";
            charStr = str.substring(index + 2, index + 6);
            char letter = 0;
            try{letter = (char) Integer.parseInt(charStr, 16);}catch (Exception e) {}
            buffer.append(letter);
            index += 6;
        }
        return buffer.toString();
    }

    /**
     * 将中文字符串转换成Unicode
     * @param str 要转换的中文字符串
     * @return 转换后的Unicode
     */
    public static String GBK2Unicode(String str) {

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {

            char chr1 = (char) str.charAt(i);

            if (!isNeedConvert(chr1)) {

                result.append(chr1);

                continue;

            }
            try{result.append("\\u" + Integer.toHexString((int) chr1));}catch (Exception e) {}

        }

        return result.toString();

    }

    /**
     * 在中文字符串转换成Unicode方法中判断是否需要转换
     * @param para 要转化的字符
     * @return boolean
     */
    public static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);

    }

    /**
     * 使用Sms 的 SendSms()方法发送短信时,调用此方法将其短信内容转换成十六进制
     * @param msg 短信内容
     * @return 转换后的十六进制短信
     */
    public static final String encodeHex(String msg) {
        byte[] bytes = null;
        try {
            bytes = msg.getBytes("GBK");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer buff = new StringBuffer(bytes.length * 4);
        String b = "";
        char a;
        int n = 0;
        int m = 0;
        for (int i = 0; i < bytes.length; i++) {
            try{b = Integer.toHexString(bytes[i]);}catch (Exception e) {}
            if (bytes[i] > 0) {
                buff.append("00");
                buff.append(b);
                n = n + 1;
            } else {
                a = msg.charAt((i - n) / 2 + n);
                m = a;
                try{b = Integer.toHexString(m);}catch (Exception e) {}
                buff.append(b.substring(0, 4));

                i = i + 1;
            }
        }
        return buff.toString();
    }

    public static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("00");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "00" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    public static byte[] hex2byte(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    // byte类型数据，转成十六进制形式；
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex
     *            十六进制字符串
     * @return byte[]
     */
    public static byte[] hexStr2Byte(String hex) {
        if (hex == null) {
            return new byte[] {};
        }

        // 奇数位补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        for (int i = 0; i < length; i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            buffer.put(b);
        }
        return buffer.array();
    }
}