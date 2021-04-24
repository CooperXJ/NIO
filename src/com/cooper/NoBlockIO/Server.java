package com.cooper.NoBlockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 5:00 下午
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //切换为非阻塞模式
        ssChannel.configureBlocking(false);

        ssChannel.bind(new InetSocketAddress(9999));

        //获取Selector
        Selector selector = Selector.open();

        //注册通道到选择器上,并指定"监听接受事件"
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //轮训式的获取选择器上已经准备就绪的事件
        while (selector.select()>0){
            //获取当前选择器中所有注册的选择键（已就绪的监听事件）
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            //判断具体是什么事件准备就绪
            while (it.hasNext()){
                //获取准备就绪的事件
                SelectionKey sk = it.next();

                //判断具体是什么事件准备就绪
                if(sk.isAcceptable()){
                    System.out.println("这是接受状态");
                    //若接收就绪则获取客户端的连接
                    SocketChannel acceptChannel = ssChannel.accept();

                    //切换非阻塞模式
                    acceptChannel.configureBlocking(false);

                    //将该通道注册到选择器上
                    acceptChannel.register(selector,SelectionKey.OP_READ);
                }
                else if(sk.isReadable()){
                    System.out.println("这是可读状态***********");

                    //获取当前选择器上读就绪状态的通道
                    SocketChannel channel = (SocketChannel) sk.channel();
                    //读数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    //此处不能写！=-1，因为当没有数据传输过来的时候是0
                    while ((len=channel.read(buf))>0){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }
                    //表示有人下线，也就是有人断开连接
                    if(len==-1){
                        System.out.println("有人离线。。。。"+channel.getRemoteAddress());
                        sk.cancel();
                        channel.close();
                    }
                }
                //必须要取消，否则已经完成的连接会一直存在
                it.remove();
            }
        }
    }
}
