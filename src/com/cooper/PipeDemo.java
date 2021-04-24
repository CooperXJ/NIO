package com.cooper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 7:29 下午
 */
public class PipeDemo {
    /**
     * pipe 适用于两个线程之间的管道，一个读线程往管道里放东西，一个写线程往管道里取东西
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //获取管道
        Pipe pipe = Pipe.open();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("Hello pipe".getBytes(StandardCharsets.UTF_8));

        //往管道中写入数据
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buf.flip();
        sinkChannel.write(buf);

        //读取管道中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        ByteBuffer buf1 = ByteBuffer.allocate(1024);
        sourceChannel.read(buf1);
        buf1.flip();
        System.out.println(new String(buf1.array(),0, buf1.limit()));

        sinkChannel.close();
        sourceChannel.close();
    }
}
