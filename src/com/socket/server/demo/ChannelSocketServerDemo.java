package com.socket.server.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * @author Cherry
 * 2020年5月20日
 */
public class ChannelSocketServerDemo {

    public static void main(String[] args) throws IOException {
        initChannel();
    }
    
    public static void initChannel() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(8080));
        ByteBuffer buffer = ByteBuffer.allocate(2000);
        ByteBuffer buffer2 = ByteBuffer.allocate(2000);
        System.out.println("server running :" + serverChannel);
        while(true) {
            SocketChannel channel = serverChannel.accept();
            if(null != channel) {
                channel.read(buffer);
                buffer.flip();
                System.out.println(new String(buffer.array()).trim());
                buffer.clear();
                
                String content = "China~World !";
                String header = "HTTP/1.0 200 OK\r\n"
                                + "Date:" + new Date() + "\r\n"
                                +"Content-type:text/html;charset=utf-8;\r\n"
                                + "Content-length:" + content.length()+"\r\n\r\n";
                buffer2.put(header.getBytes());
                buffer2.put(content.getBytes());
                buffer2.flip();
                channel.write(buffer2);
                buffer2.clear();
            }
        }
    }
}
