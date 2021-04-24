package com.cooper.NIOChatRoom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author 薛进
 * @version 1.0
 * @Description
 * @date 2021/4/24 9:08 下午
 */
public class Server {
    private Selector selector;
    private ServerSocketChannel ssChannel;
    private static final int PORT = 9999;

    public Server(){
        try {
            selector = Selector.open();
            ssChannel = ServerSocketChannel.open();
            ssChannel.bind(new InetSocketAddress(PORT));
            ssChannel.configureBlocking(false);
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }

    private void listen() {
        try {
            while (selector.select()>0){
                //获取选择器中所有注册通道的就绪事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    //提取事件
                    SelectionKey sk = iterator.next();

                    //判断事件类型
                    if(sk.isAcceptable()){
                        SocketChannel accept = ssChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector,SelectionKey.OP_READ);
                    }
                    else if(sk.isReadable()){
                        //处理转发逻辑
                        readClientData(sk);
                    }
                    //移除当前事件
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 接收当前客户端的发送的信息并转发给其他客户端
     */
    private void readClientData(SelectionKey sk) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) sk.channel();
            ByteBuffer buf = ByteBuffer.allocate(1024);

            int len = 0;
            String msg = "";
            while((len = channel.read(buf))>0){//客户端如果处于离线状态的话会导致读取发生异常，以此来判断是否有人掉线
                buf.flip();
                msg += new String(buf.array(),0,buf.limit());
                buf.clear();
            }

            if(len==0){
                System.out.println("len = 0");
            }
            if(!msg.equals(""))
                //发送给其他客户端
                sendToAllClient(msg,channel);

            //此处感觉window和unix处理离线消息的机制不太一样，windows是直接报错，unix是直接返回-1，还是说nio更新了？
            if(len==-1){
                System.out.println("有人离线了...."+channel.getRemoteAddress());
                sk.cancel();//取消注册
                channel.close();//关闭通道
            }
        }catch (Exception e){
            try {

            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 推送消息给所有人
     * @param msg
     * @param sk
     */
    private void sendToAllClient(String msg, SocketChannel sk) throws IOException {
        System.out.println("服务器开始转发消息,当前线程为"+Thread.currentThread().getName());
        //遍历当前所有在线的通道并转发
        for (SelectionKey key : selector.keys()) {
            Channel channel =  key.channel();
            //忽略自己的转发  这里需要注意一下我们自己注册的服务端通道也会被注册到select中
            if(channel instanceof SocketChannel && channel!= sk){
                ByteBuffer buf = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                ((SocketChannel)channel).write(buf); //保证获取的通道是SocketChannel类型的通道
            }
        }
    }
}
