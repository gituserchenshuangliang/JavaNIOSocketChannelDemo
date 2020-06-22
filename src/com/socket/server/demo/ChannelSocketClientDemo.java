package com.socket.server.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Client Demo
 * @author Cherry
 * 2020年5月20日
 */
public class ChannelSocketClientDemo {

    public static void main(String[] args) {
           try(SocketChannel socket = SocketChannel.open(new InetSocketAddress("192.168.0.102", 8080));){
            ByteBuffer buffer = ByteBuffer.allocate(200);
            String content = "Hello World !";
            String header = "GET / HTTP/1.1\r\n"
                    + "Host: 192.168.0.102:8080\r\n"
                    + "Content-type: text/html;charset=utf-8;\r\n"
                    + "Content-length: " + content.length() + "\r\n\r\n";
            buffer.put(header.getBytes());
            buffer.put(content.getBytes());
            buffer.flip();
            socket.write(buffer);
            buffer.clear();
            
            ByteBuffer buffer2 = ByteBuffer.allocate(200);
            socket.read(buffer2);
            buffer2.flip();
            System.out.println(new String(buffer2.array()).trim());
            buffer2.clear();
           } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
