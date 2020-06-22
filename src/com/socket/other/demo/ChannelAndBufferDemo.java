package com.socket.other.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 通道与缓存
 * @author Cherry
 * 2020年5月20日
 */
public class ChannelAndBufferDemo {

    public static void main(String[] args) throws IOException {
        //fileChannelShow();
        //bufferFlipShow();
        bufferClearShow();
    }
    
    public static void fileChannelShow() {
//      fileChannelWrite("你好，中国！","./blank.txt");
        fileChannelRead("./blank.txt");
    }
    
    public static void fileChannelRead(String path) {
        try (FileInputStream in = new FileInputStream(Paths.get(path).toFile());
            FileChannel fileChannel = in.getChannel();){
            ByteBuffer buffer = ByteBuffer.allocate(in.available());
            fileChannel.read(buffer);
            buffer.flip();
            System.out.write(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void fileChannelWrite(String txt,String path) {
        try (FileChannel channel = new FileOutputStream(Paths.get(path).toFile()).getChannel();){
            byte[] b = txt.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(b);
            //将一个channel传到另一个channel
            //channel.transferTo(position, count, target);
            //channel.transferFrom(src, position, count);
            channel.write(buffer);
            buffer.clear();
            buffer.put("中国梦！".getBytes("UTF-8"));
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void bufferInfo(ByteBuffer buffer) {
        int limit = buffer.limit();
        int position = buffer.position();
        int capacity = buffer.capacity();
        show("position:"+position+"\ncapatity:"+capacity+"\nlimit:"+limit);
    }
    
    public static void ioShow(Socket socket) throws FileNotFoundException{
        /*
         * InputStream的available()含义：返回此输入流在不受阻塞情况下能读取的字节数。
         * 网络流与文件流不同的关键就在于是否“受阻”二字，网络socket流在读取时如果没有内容read()
         * 方法是会受阻的，所以从socket初始化的输入流的available也是为零的，
         * 但文件读取时read()一般是不会受阻的，因为文件流的可用字节数 available = file.length()，
         * 而文件的内容长度在创建File对象时就已知了。
         */
            try {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                int cap = in.available();
                byte[] b = new byte[cap];
                in.read(b);
                //show(new String(b));
                String content = "中国，你好！";
                String header = "HTTP/1.0 200 OK\r\n"
                                + "Date:" + new Date() + "\r\n"
                                +"Content-type:text/html;charset=utf-8;\r\n"
                                + "Content-length:" + content.length()+"\r\n\r\n";
                
                out.write(header.getBytes());
                out.write(content.getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
    
    public static void bufferFlipShow() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put("012345".getBytes());
            bufferInfo(buffer);
            
            buffer.flip();
        
            bufferInfo(buffer);
            buffer.put((byte) 45);
            buffer.put((byte) 45);
            while(buffer.hasRemaining()) {
                show("===");
                buffer.get();
            }
            bufferInfo(buffer);
            
            buffer.flip();
            buffer.put((byte) 45);
            bufferInfo(buffer);
            while(buffer.hasRemaining()) {
                System.out.print((char)buffer.get());
            }
            bufferPrint(buffer);
    }
    
    public static void bufferClearShow() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("012388".getBytes());
        bufferInfo(buffer);
        
        buffer.flip();
        buffer.put("AAAA".getBytes());
//        buffer.put(src, offset, length)
        bufferInfo(buffer);
        
        while(buffer.hasRemaining()) {
            show(buffer.getChar(),0);
//            buffer.get(buffer, offset, length);
        }
    }
    
    public static void bufferPrint(ByteBuffer buffer) {
        System.out.println(new String(buffer.array()).trim());
    }
    
    public static void show(Object obj,Object...o) {
        if(o == null) {
            System.out.println(obj);
        }else {
            System.out.print(obj);
        }
    }
}
