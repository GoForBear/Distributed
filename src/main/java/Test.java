import NIO.NIOProject;
import NIO.NIOTCPSocket;

public class Test {
    public static void main(String[] args) {
        String srcFileName = "D:\\TEST.TXT";
        String dstFileName ="D:\\333.TXT";
//        int result  = NIOProject.copyFile(srcFileName,dstFileName);
//        System.out.println(result);
        NIOTCPSocket niotcpSocket = new NIOTCPSocket();
        niotcpSocket.sendFile(srcFileName);
    }
}
