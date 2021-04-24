package com.cooper.blockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 4:21 下午
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9999));
        FileChannel inChannel = FileChannel.open(Paths.get("src/com/cooper/file/1.txt"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (inChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        //如果不加上这句话，服务端会一直处于等待接受数据的状态  这里需要关闭发送请求shutdownOutput
        sChannel.shutdownOutput();

        //接受服务端的反馈
        while (sChannel.read(buf)!=-1){
            buf.flip();
            System.out.println(new String(buf.array(),0,buf.limit()));
            buf.clear();
        }

        inChannel.close();
        sChannel.close();
    }
}
