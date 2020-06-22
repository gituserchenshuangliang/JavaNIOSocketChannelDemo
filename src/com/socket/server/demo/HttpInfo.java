package com.socket.server.demo;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class HttpInfo {
    public static String ip = "192.168.0.102";
    public static StringBuilder sb = new StringBuilder();
    public static String requestMsg(String method,String contentType,int contentLength,String body) throws UnsupportedEncodingException {
        sb.setLength(0);
        
        sb.append(method);
        sb.append(" / HTTP/1.1\r\n");
        sb.append("Date: ");
        sb.append(new Date());
        sb.append("\r\n");
        sb.append("content-type: ");
        sb.append(contentType);
        sb.append(";charset=utf-8;\r\n");
        sb.append("content-length: ");
        sb.append(contentLength);
        sb.append("\r\n\r\n");
        sb.append(new String(body.getBytes("UTF-8")));
        
        return sb.toString();
    }
    
    public static byte[] requestByteMsg(String method,String contentType,int contentLength,String body) throws UnsupportedEncodingException {
        String msg = requestMsg( method, contentType, contentLength, body);
        return msg.getBytes();
    }
    
    public static String responseMsg(String contentType,int contentLength,String body) throws UnsupportedEncodingException {
        sb.setLength(0);
        
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Date: ");
        sb.append(new Date());
        sb.append("\r\n");
        sb.append("content-type: ");
        sb.append(contentType);
        sb.append(";charset=utf-8;\r\n");
        sb.append("content-length: ");
        sb.append(contentLength);
        sb.append("\r\n\r\n");
        sb.append(new String(body.getBytes("UTF-8")));
        
        return sb.toString();
    }
    
    public static byte[] responseByteMsg(String contentType,int contentLength,String body) throws UnsupportedEncodingException {
        String msg = responseMsg(contentType, contentLength, body);
        return msg.getBytes();
    }
}
