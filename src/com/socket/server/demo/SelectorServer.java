package com.socket.server.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorServer {
    private Selector selector;
    private ServerSocketChannel server;
    
    public void init() throws IOException {
        selector = Selector.open();
        
        server = ServerSocketChannel.open();
        
        server.configureBlocking(false);
        
        
        server.socket().bind(new InetSocketAddress(HttpInfo.ip,8080));
        
        server.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("server is running at " + server);
    }
    
    public void start() throws IOException {
        while(true) {
            selector.select();
            
            Set<SelectionKey> set = selector.selectedKeys();
            
            Iterator<SelectionKey> iter = set.iterator();
            
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                
                iter.remove();
                
                if(key.isAcceptable()){
                    handleAccept(key);
                }else if(key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }
    
    private void handleRead(SelectionKey key) throws UnsupportedEncodingException, IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        //Exception in thread "main" java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        if(channel.read(buffer) != -1) {
            System.out.println(new String(buffer.array()).trim());
            
            String msg = "敲骨吸髓";
            
            channel.write(ByteBuffer.wrap(HttpInfo.responseByteMsg("text/html", msg.length(), msg)));
        }else {
            key.cancel();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel servers = (ServerSocketChannel) key.channel();
        
        SocketChannel channel = servers.accept();
       
        channel.configureBlocking(false);
        
        channel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        SelectorServer serv = new SelectorServer();
        serv.init();
        serv.start();
    }

}
