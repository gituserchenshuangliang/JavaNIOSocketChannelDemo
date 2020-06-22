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

/**
 * Selector ServerSocketChannel
 * @author Cherry
 * 2020年5月22日
 */
public class NioServer {
    private Selector selector;
    private ServerSocketChannel server;
    
    public void init() throws IOException {
            selector = Selector.open();
            
            server = ServerSocketChannel.open();
            
            server.configureBlocking(false);
            
            server.socket().bind(new InetSocketAddress(HttpInfo.ip,8081));
            
            show("server running at " + server);
            
            server.register(selector, SelectionKey.OP_ACCEPT);
    }
    
    public void start() throws IOException {
        while(true) {
                selector.select();
                
                Set<SelectionKey> set = selector.selectedKeys();
                
                Iterator<SelectionKey> iter = set.iterator();
                
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    
                    iter.remove();
                    
                    if(key.isAcceptable()) {
                        handleAccept(key);
                    }else if(key.isReadable()) {
                        handleRead(key);
                    }
                }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        if(channel.read(buffer) != -1) {
            show(new String(buffer.array()).trim());
            
            String msg = "a leaf cover your eyes !";
            
            channel.write(ByteBuffer.wrap(HttpInfo.responseByteMsg("text/html", msg.length(), msg)));
        }else {
            key.cancel();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
            ServerSocketChannel serv = (ServerSocketChannel) key.channel();
        
            SocketChannel channel = serv.accept();
            
            channel.configureBlocking(false);
            
            channel.register(selector,SelectionKey.OP_READ);
    }
    
    public void show(Object obj) {
        System.out.println(obj);
    }
    
    public static void main(String[] args) throws IOException {
        NioServer nio = new NioServer();
        nio.init();
        nio.start();
    }

}
