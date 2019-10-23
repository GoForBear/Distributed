package NIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOTCPSocket {

    private static String SOCKET_SERVER_IP = "127.0.0.1";
    private static int SOCKET_SERVER_PORT = 8080;


    public int sendFile(String srcFileName){
        File file = new File(srcFileName);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try{
            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1",80));




        }catch (Exception e){

        }
        return 0;
    }
}
