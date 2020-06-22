package com.socket.server.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Selector Channel
 * @author Cherry
 * 2020年5月22日
 */
public class NioClient {
    private Selector selector;
    private SocketChannel socket;
    private Scanner scan = new Scanner(System.in);
    public void init() throws IOException {
            selector = Selector.open();
            
            socket = SocketChannel.open();
            
            socket.configureBlocking(false);
            
            socket.connect(new InetSocketAddress("192.168.0.102",8080));
            
            socket.register(selector, SelectionKey.OP_CONNECT);
    }
    
    public void start() throws IOException {
        while(true) {
                selector.select();
                
                Set<SelectionKey> set = selector.selectedKeys();
                
                Iterator<SelectionKey> iter = set.iterator();
                
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    
                    iter.remove();
                    
                    if(key.isConnectable()) {
                        handleConnet(key);
                    }else if(key.isReadable()) {
                        handleRead(key);
                    }
                }
        }
    }
    
    
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        channel.read(buffer);
        
        show(new String(buffer.array()).trim());
    }

    private void handleConnet(SelectionKey key) throws ClosedChannelException, IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if(channel.isConnectionPending()) {
            if(channel.finishConnect()) {
                channel.configureBlocking(false);
                
                channel.register(selector, SelectionKey.OP_READ);
                
                channel.write(ByteBuffer.wrap("Message".getBytes()));
            }else {
                key.cancel();
            }
        }
    }
    
    public void show(Object obj) {
        System.out.println(obj);
    }
    
    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.init();
        client.start();
    }

}
