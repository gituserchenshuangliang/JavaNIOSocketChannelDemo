package com.socket.server.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.socket.newio.demo.IPEnum;

/**
 * 文件服务器
 * @author Cherry
 * 2020年5月7日
 */
public class JavaHTTPFileServer {
    private final static Logger logger = Logger.getLogger(JavaHTTPFileServer.class.getName());
    public static void main(String[] args) {
       new JavaHTTPFileServer().start();
    }
    
    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try(ServerSocket server = new ServerSocket(8080,0,IPEnum.IP2.address());){
            logger.info("service is running at " + server);
            while(true) {
                Socket socket = server.accept();
                pool.submit(() -> {
                    requestAndResponse(socket, new File("E:/ServiceDemo"));
                });
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<File> showFiles(File rootDir) throws IOException {
        ArrayList<File> array = new ArrayList<File>(20);
        if(rootDir.isDirectory()) {
            File[] fs = rootDir.listFiles();
            for (File f2 : fs) {
                array.add(f2.getCanonicalFile());
            }
        }else {
            array.add(rootDir);
        }
        return array;
    }
    
    public void requestAndResponse(Socket socket,File rootDir) {
        try {
            OutputStream os = new BufferedOutputStream(socket.getOutputStream());
            InputStream ins = new BufferedInputStream(socket.getInputStream());
            Reader in = new InputStreamReader(ins, "UTF-8");
            
            StringBuilder request = new StringBuilder(80);
            while(true) {
                int c = in.read();
                if(c=='\r' || c == '\n' || c == -1) {
                    break;
                }
                request.append((char)c);
            }
            String get  = request.toString();
            logger.info(socket.getRemoteSocketAddress() + " " + get);
            
            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String fileName = tokens[1];
            
            String version = "";
            if(tokens.length > 2) {
                version = tokens[2];
            }
            
            fileName = rootDir.getAbsolutePath() + fileName;
            File file = new File(fileName);
            
            if(method.contentEquals("GET")) {
                request.setLength(0);
                StringBuilder content = request;
                
                if(file.exists() && file.isDirectory()) {
                    ArrayList<File> array = showFiles(file);
                    for (File f : array) {
                        String fileFina = f.getAbsolutePath().substring(rootDir.getAbsolutePath().length());
                        content.append("<html><body>");
                        content.append("<li><a href = \'" + fileFina + "\'>");
                        content.append(fileFina);
                        content.append("</a></li></body></html>");
                    }
                    
                    byte[] data = content.toString().getBytes("UTF-8");
                    
                    if(version.startsWith("HTTP/")) {
                        sendHeaderResult(os,"text/html",data.length,data);
                    }
                    logger.info("Send File：" + file);
                }else if(file.exists() && file.isFile()){
                    String mimeType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                    
                    byte[] data = Files.readAllBytes(Paths.get(fileName));
                    
                    if(version.startsWith("HTTP/")) {
                        sendHeaderResult(os,mimeType,data.length,data);
                    }
                    logger.info("Send File：" + fileName);
                }else {
                    redirector(os,"/");
                }
            }
            
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
    
    private void sendHeaderResult(OutputStream out, String contentType,int length,byte[] data) {
        StringBuilder header = new StringBuilder();
        header.append("HTTP/1.0 200 OK\r\n");
        header.append("Date: " + new Date() + "\r\n");
        header.append("Server: HTTPServer 3.0\r\n");
        header.append("Content-length: " + length + "\r\n");
        header.append("Conent-type: " + contentType + ";charset=utf-8\r\n\r\n");
        try {
            out.write(header.toString().getBytes("UTF-8"));
            out.write(data);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            logger.log(Level.WARNING,"链接断开",e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    //重定向
    public void redirector(OutputStream out,String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 302 FOUND\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Server: Redirector 1.1\r\n");
        sb.append("Content-type: text/html\r\n");
        sb.append("Location: " +url+ "\r\n\r\n");
        String header = sb.toString();
        out.write(header.getBytes("UTF-8"));
        out.flush();
    }
}
