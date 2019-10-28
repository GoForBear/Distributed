package NIO;

import util.IOUtil;
import util.NIOConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOTCPServer {
    public static void getFile(String fileName) throws  Exception{
        ServerSocketChannel serverSocketChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try{
            File file = new File(fileName);
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(NIOConfig.SOCKET_SERVER_IP, NIOConfig.SOCKET_SERVER_PORT));
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(selector.select() > 0){
                System.out.println("等待。。。");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
                while(iterator.hasNext()){
                    System.out.println("循环。。。");
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isAcceptable()){
                        socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else if(selectionKey.isReadable()){
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
