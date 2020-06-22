package com.socket.newio.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public enum IPEnum implements InetAddrInterface {
    IP1{
        @Override
        public InetAddress address() {
            try {
                return InetAddress.getByName("192.168.0.102");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return null;
        }
    },
    IP2{

        @Override
        public InetAddress address() {
            try {
                return InetAddress.getByName("192.168.144.1");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return null;
        }
        
    };
}
interface InetAddrInterface {
    InetAddress address();
}