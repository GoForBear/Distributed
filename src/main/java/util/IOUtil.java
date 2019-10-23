package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class IOUtil {

    public static void close(FileChannel fileChannel){
        if( null != fileChannel && fileChannel.isOpen()){
            try{
                fileChannel.close();
            }catch (Exception e){
                System.out.println("关闭通道异常" + e);
            }
        }
    }


    //这里有个问题，如何判断输出流和后边的输入流是已经关闭的？？？
    public static void close(FileOutputStream fileOutputStream){
        if( null != fileOutputStream ){
            try{
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println("关闭输出流异常" + e);
            }
        }
    }

    public static void close(FileInputStream fileInputStream){
        if( null != fileInputStream ){
            try{
                fileInputStream.close();
            }catch (Exception e){
                System.out.println("关闭输入流异常" + e);
            }
        }
    }
}
