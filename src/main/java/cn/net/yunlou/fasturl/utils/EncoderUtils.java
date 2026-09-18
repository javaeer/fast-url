/*
 *
 *     Copyright (c) 2019 - forever, javaeer All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the smartcloudx.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * Author: javaeer (javaeer@aliyun.com)
 *
 */
package cn.net.yunlou.fasturl.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.bitcoinj.core.Base58;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p>
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * </p>
 *
 * @Author javaeer(javaeer @ aliyun.com)
 * @Date 2018/11/27 13:54
 * @Version 1.0
 */
public class EncoderUtils {
    private static final String DEFAULT_URL_ENCODING = "UTF-8";
    private static final int[] BASE62_INDEX = new int[128];
    private static final String BASE62 = "AaBbCcDdEeFf01234GgHhIiJjKkLl56789MmNnOoPpQqRrSsTtUuVvWwXxYyZz";

    static {
        for (int i = 0; i < BASE62.length(); i++) {
            BASE62_INDEX[BASE62.charAt(i)] = i;
        }
    }

    /**
     * Hex编码.
     *
     * @param input 输入字节数组
     * @return 编码后的字符串
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * Hex解码.
     *
     * @param input 输入字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalArgumentException("解码Hex字符串时出错: " + e.getMessage(), e);
        }
    }

    /**
     * Base58编码.
     *
     * @param input 输入字节数组
     * @return 编码后的字符串
     */
    public static String encodeBase58(byte[] input) {
        return Base58.encode(input);
    }

    /**
     * Base58解码.
     *
     * @param input 输入字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeBase58(String input) {
        return Base58.decode(input);
    }

    /**
     * Base64编码.
     *
     * @param input 输入字节数组
     * @return 编码后的字符串
     */
    public static String encodeBase64(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    /**
     * Base64编码.
     *
     * @param input 输入字符串
     * @return 编码后的字符串
     */
    public static String encodeBase64(String input) {
        try {
            return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("不支持的编码格式: " + DEFAULT_URL_ENCODING, e);
        }
    }

    /**
     * Base64解码.
     *
     * @param input 输入字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input.getBytes());
    }

    /**
     * Base64解码.
     *
     * @param input 输入字符串
     * @return 解码后的字符串
     */
    public static String decodeBase64String(String input) {
        try {
            return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("不支持的编码格式: " + DEFAULT_URL_ENCODING, e);
        }
    }

    /**
     * Base62编码.
     * @param input 输入长整型
     * @return 编码后的字符串
     */
    public static String encodeBase62(long input) {
        if (input == 0) {
            return "0";
        }

        StringBuilder encoded = new StringBuilder();
        while (input > 0) {
            int index = (int) (input % 62);
            encoded.append(BASE62.charAt(index));
            input /= 62;
        }

        return encoded.reverse().toString();
    }

    /**
     * Base62解码.
     * @param input 输入字符串
     * @return 解码后的长整型
     * @throws IllegalArgumentException 如果输入包含非Base62字符
     */
    public static long decodeBase62(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入不能为空");
        }

        long decoded = 0;
        for (int i = 0; i < input.length(); i++) {
            int index = BASE62_INDEX[input.charAt(i)];
            if (index == -1) {
                throw new IllegalArgumentException("输入包含非Base62字符: " + input);
            }
            decoded += Math.pow(62, input.length() - i - 1) * index;
        }

        return decoded;
    }


    /**
     * Html 转码.
     *
     * @param html 输入HTML字符串
     * @return 转码后的字符串
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html 解码.
     *
     * @param htmlEscaped 输入HTML转码字符串
     * @return 解码后的字符串
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Xml 转码.
     *
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml10(xml);
    }

    /**
     * Xml 解码.
     *
     * @param xmlEscaped 输入XML转码字符串
     * @return 解码后的字符串
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     *
     * @param part 输入字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("不支持的编码格式: " + DEFAULT_URL_ENCODING, e);
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     *
     * @param part 输入字符串
     * @return 解码后的字符串
     */
    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("不支持的编码格式: " + DEFAULT_URL_ENCODING, e);
        }
    }


    public static void main(String[] args) {
        System.out.println(encodeBase62(1234567890111L));
        System.out.println(decodeBase62("g7"));
    }

}
