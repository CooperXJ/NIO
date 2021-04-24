package com.cooper.NoBlockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 4:55 下午
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9999));

        //切换成非阻塞模式
        sChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            buf.put(str.getBytes(StandardCharsets.UTF_8));
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        sChannel.close();
    }
}
