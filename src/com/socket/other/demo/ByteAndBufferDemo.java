package com.socket.other.demo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Byte Ande Buffer
 * @author Cherry
 * 2020年5月20日
 */
public class ByteAndBufferDemo {

    public static void main(String[] args) throws FileNotFoundException {
        //charToByte();
        //charShow();
        //byteAndChar();
    }
    
    public static void charToByte() throws FileNotFoundException {
        String letter = "0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvw";
        byte[] s = letter.getBytes();
        OutputStream out = new FileOutputStream("charToByte.txt");
        System.setOut(new PrintStream(out));
        System.out.println("------- 字符       ->    字节对照表  -------");
        int i = 0;
        for (byte b : s) {
            i++;
            System.out.print((char)b + " -> " + b + "\t\t");
            if(i%5 ==0) {
                System.out.println("\n");
            }
        }
    }
    
    public static void charShow() {
        byte[] rotation = new byte[95*2];
        for(byte i = ' ' ; i <= '~'; i++) {
            rotation[i - ' '] = i;
            rotation[i + 95 - ' '] = i;
        }
        System.out.println(new String(rotation));
    }
    
    @SuppressWarnings("resource")
    public static void byteAndChar() {
        Scanner scan = null;
        while(true) {
            show("---------字节字符转换----------");
            show("1.字节转字符    2.字符转字节    3.退出");
            show("按编号选择：");
            scan = new Scanner(System.in);
            int t = 0;
            try {
                t = scan.nextInt();
                if(t >4 || t < 1) {
                    show("请输入选项中的数字！\n");
                    continue;
                }
                
                switch (t) {
                case 1:
                    do {
                        scan = new Scanner(System.in);
                        show("-----------字节转字符-----------");
                        show("输入字节数字(0~119)：");
                        show("结果："+(char)(scan.nextByte())+"\n");
                        show("-----------主菜单：0 \t继续：1~9------------");
                        show("选择：");
                        try {
                            t = scan.nextInt();
                        }catch (InputMismatchException e) {
                            show("请输入整数位数字！\n");
                        }
                        if(t == 0) {
                            break;
                        }
                    }while(true);
                    continue;
                case 2:
                    do {
                        scan = new Scanner(System.in);
                        show("-----------字符转字节-----------");
                        show("输入单个字符：");
                        String chars = scan.nextLine();
                        if(chars.length() == 1) {
                            show("结果："+(chars.getBytes()[0])+"\n");
                            show("-----------主菜单：0 \t继续：1~9------------");
                            show("选择：");
                        }else {
                            show("请输入单个字符！");
                            continue;
                        }
                        
                        try {
                            t = scan.nextInt();
                        }catch (InputMismatchException e) {
                            show("请输入整数位数字！\n");
                        }
                        if(t == 0) {
                            break;
                        }
                    }while(true);
                    continue;
                case 3:
                    show("退出.......");
                    System.exit(1000);
                    break;
                default:
                    show("没有此选项！\n");
                    continue;
                }
            }catch (InputMismatchException e) {
                show("请输入整数位数字！");
            }
        }
    }
    
    public static void show(Object obj) {
        System.out.println(obj);
    }
}
