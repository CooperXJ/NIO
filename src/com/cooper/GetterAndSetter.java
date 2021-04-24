package com.cooper;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 3:33 下午
 */
public class GetterAndSetter {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("src/com/cooper/file/1.txt", "rw");

        //获取通道
        FileChannel channel = file.getChannel();

        //分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        //分散读取
        ByteBuffer[] byteBuffers = {buf1,buf2};
        channel.read(byteBuffers);

        //切换为读模式
        for (ByteBuffer byteBuffer : byteBuffers) {
            byteBuffer.flip();
        }

        System.out.println(new String(byteBuffers[0].array(),0,byteBuffers[0].limit()));
        System.out.println("----------");
        System.out.println(new String(buf2.array(),0,buf2.limit()));

        //聚集写入
        RandomAccessFile file1 = new RandomAccessFile("src/com/cooper/file/2.txt", "rw");
        FileChannel channel1 = file1.getChannel();
        channel1.write(byteBuffers);
    }
}
