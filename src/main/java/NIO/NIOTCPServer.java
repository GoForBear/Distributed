package NIO;

import util.IOUtil;
import util.NIOConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;


/*  TCP的socket读写，由于TCP是有连接的，在服务器端需要分开，使用serverSocketChannel监控连接bind register OP_ACCEPT，
  socketChannel完成通信 register OP_READ
    但是UDP是无连接的，所以只需要DatagramChannel而已，DatagramChannel bind，register OP_READ事件，并且使用DatagramChannel来read缓冲区
 */

public class NIOTCPServer {
    public static void getFile(String fileName) throws  Exception{
        ServerSocketChannel serverSocketChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try{
            File file = new File(fileName);
            Selector selector = Selector.open();
            //服务器主要需要的是serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(NIOConfig.SOCKET_SERVER_IP, NIOConfig.SOCKET_SERVER_PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//serverSocketChannel注册“连接”事件
            while(selector.select() > 0){
                System.out.println("等待。。。");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
                while(iterator.hasNext()){
                    System.out.println("循环。。。");
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isAcceptable()){
                        System.out.println("检测连接接口");
                        //检测到“连接”事件之后，开启socketChannel进行通信
                        socketChannel = serverSocketChannel.accept();//socketChannel通过serverSocketChannel获取
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);//socketChannel注册“读”事件
                    }else if(selectionKey.isReadable()){
                        //正常的socket文件写法
                        fileOutputStream  = new FileOutputStream(file);
                        fileChannel = fileOutputStream.getChannel();
//                        socketChannel = (SocketChannel)selectionKey.channel();
                        socketChannel.read(byteBuffer);
                        int length = 0;
                        byteBuffer.flip();
                        System.out.println("服务器开始接收文件");
                        while((length  = fileChannel.write(byteBuffer)) > 0){
                            System.out.println(new String(byteBuffer.array(), 0, length));
                        }
                        fileChannel.force(true);
                        byteBuffer.clear();
                        IOUtil.close(fileOutputStream);
                        IOUtil.close(fileChannel);
                        socketChannel.shutdownOutput();
                        IOUtil.close(socketChannel);
                        System.out.println("文件下载完毕");
                    }

                }
                iterator.remove();
            }
            selector.close();
            System.out.println("to here");

        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    public static void main(String[] args) {
        try{
            getFile("D:\\333.TXT");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
