package com.cooper.blockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 4:11 下午
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //打开服务端通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //获取本地写入的通道
        FileChannel outChannel = FileChannel.open(Paths.get("src/com/cooper/file/3.txt"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
        //绑定端口
        ssChannel.bind(new InetSocketAddress(9999));

        SocketChannel sChannel = ssChannel.accept();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (sChannel.read(buf)!=-1){
            //read操作将数据put到ByteBuffer中，然后获取此处write需要读取buf，所以需要先flip操作
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        //发送反馈
        buf.put("发送成功...".getBytes(StandardCharsets.UTF_8));
        buf.flip();
        sChannel.write(buf);

        //记得关闭管道
        outChannel.close();
        sChannel.close();
        ssChannel.close();
    }
}
