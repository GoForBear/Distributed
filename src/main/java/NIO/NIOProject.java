package NIO;

import util.IOUtil;
import util.NIOConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOProject {


    public static int copyFile(String srcFileName, String dstFileName){
        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel inFileChannel  = null;
        FileChannel outFileChannel = null;

        try{
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(dstFile);
            inFileChannel = fileInputStream.getChannel();
            outFileChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
            int length = -1;
            //这里的-1代表文件末尾，因为不能保证一次read完，所以用循环，如果返回-1代表到了文件末尾
            while((length = inFileChannel.read(byteBuffer))!= -1){
                byteBuffer.flip();//写变成读

                int outLength = 0;
                //这里的0代表write的数据长度，因为不能保证一次write完，所以用循环，如果返回0代表这次write了0字节，也就是write完了
                while((outLength = outFileChannel.write(byteBuffer)) != 0){

                }

                byteBuffer.clear();//读变成写
            }
            outFileChannel.force(true);//保证通道的数据一定是写入磁盘的
            IOUtil.close(outFileChannel);
            IOUtil.close(inFileChannel);
            IOUtil.close(fileInputStream);
            IOUtil.close(fileOutputStream);

        }catch (Exception e){
            System.out.println("文件复制出现异常：" + e);
            IOUtil.close(outFileChannel);
            IOUtil.close(inFileChannel);
            IOUtil.close(fileInputStream);
            IOUtil.close(fileOutputStream);
            return 1;
        }


        return 0;
    }

}
