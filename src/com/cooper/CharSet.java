package com.cooper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 3:44 下午
 */
public class CharSet {
    public static void main(String[] args) throws CharacterCodingException {
        Charset gbk = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder charsetEncoder = gbk.newEncoder();

        //获取解码器
        CharsetDecoder charsetDecoder = gbk.newDecoder();

        CharBuffer buf = CharBuffer.allocate(1024);
        buf.put("Hello world");
        //因为编码需要对其进行读取操作，所以需要flip
        buf.flip();

        //编码
        ByteBuffer encode = charsetEncoder.encode(buf);
        for (int i = 0; i < 11; i++) {
            System.out.println(encode.get(i));
        }

        //解码
        System.out.println(charsetDecoder.decode(encode));
    }
}
