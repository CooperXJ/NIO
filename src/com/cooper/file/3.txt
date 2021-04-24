package com.cooper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
	    String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("--------allocate------------");
        //存放数据
        buf.put(str.getBytes(StandardCharsets.UTF_8));
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------flip------------");
        //切换成读模式  limit变为了position的位置
        buf.flip();
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------read------------");
        //读取数据
        byte[] buffer = new byte[buf.limit()];
        buf.get(buffer,0,buffer.length-2);
        System.out.println(new String(buffer,0,buffer.length-2));
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------mark------------");
        //mark，标记当前position的位置
        buf.mark();
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------read------------");
        //读取数据
        buf.get(buffer,buffer.length-2,2);
        System.out.println(new String(buffer,buffer.length-2,2));
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------reset------------");
        //重新回到之前的position
        buf.reset();
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------rewind------------");
        //rewind  重读
        buf.rewind();
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());

        System.out.println("--------clear------------");
        //clear  清空缓冲区，但是缓冲区的数据依旧存在,处于"遗忘"状态  其实就是pos和limit置为了0，但是数据依旧是存在于缓冲区中的
        buf.clear();
        System.out.println("position:" +buf.position()+" limit:"+buf.limit()+"  capacity:"+buf.capacity());
        System.out.println((char) buf.get());
    }
}
