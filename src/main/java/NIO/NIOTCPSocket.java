package NIO;

import util.IOUtil;
import util.NIOConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


public class NIOTCPSocket {


    private Charset charset = Charset.forName("UTF-8");


    public int sendFile(String srcFileName){
        File file = new File(srcFileName);
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try{
            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(NIOConfig.SOCKET_SERVER_IP, NIOConfig.SOCKET_SERVER_PORT));
            socketChannel.configureBlocking(false);
            while(!socketChannel.finishConnect()){

            }
//            //charset.encode会返回一个参数大小的缓冲区，将文件名写入到这个缓冲区中
//            ByteBuffer writeBuffer = charset.encode(destFileName);
//            socketChannel.write(writeBuffer);
            //把文件长度写入到缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
//            byteBuffer.putLong(file.length());
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//            byteBuffer.clear();
            int length = 0;
            System.out.println("开始上传文件");
            while(((length = fileChannel.read(byteBuffer)) > 0)){
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            if(length == -1){
                IOUtil.close(fileChannel);
                IOUtil.close(fileInputStream);
                socketChannel.shutdownOutput();
                IOUtil.close(socketChannel);
            }
            System.out.println("上传文件结束");

        }catch (Exception e){
           e.printStackTrace();

        }
        return 0;
    }
}
