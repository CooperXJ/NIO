package com.cooper.NIOChatRoom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 9:35 下午
 */
public class Client {
    private Selector selector;
    private static int port = 9999;
    private SocketChannel socketChannel;

    public Client(){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);//只接受读事件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.readInfo();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.next();
            client.sendToServer(s);
        }
    }

    private  void sendToServer(String s) {
        try {
            socketChannel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private  void readInfo() throws IOException {
        while(selector.select()>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey sk = iterator.next();
                if(sk.isReadable()){
                   SocketChannel  channel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    channel.read(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(),0,buf.limit()).trim());
                }
                iterator.remove();
            }
        }
    }
}
