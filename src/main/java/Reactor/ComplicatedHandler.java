package Reactor;

import util.IOUtil;
import util.NIOConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplicatedHandler implements Runnable {
     private  Selector selector;
     private SocketChannel socketChannel;
     private  SelectionKey selectionKey;
     static ExecutorService pool = Executors.newFixedThreadPool(4);
     ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
     ComplicatedHandler(Selector selector, SocketChannel socketChannel) throws  Exception{
         this.selector = selector;
         this.socketChannel = socketChannel;
         if(socketChannel.isOpen() && socketChannel != null){
             System.out.println("通道开了");
         }else{
             System.out.println("通道关了");
         }
         socketChannel.configureBlocking(false);
         selector.wakeup();//这里需要唤醒，不知道为什么这里的selector会被阻塞掉
         selectionKey = socketChannel.register(selector,0);
         selectionKey.attach(this);
         selectionKey.interestOps(SelectionKey.OP_READ);//这个就是直接给selectionkey一个定值，等于是说无论什么通道都监控read状态
         selector.wakeup();
    }

    @Override
    public void run() {
         pool.execute(new AsyncTask());
    }

    public synchronized  void asyncRun(){
        try{
            if(!selectionKey.isValid()){ //这里加上判断，如果任何一个异步程序关掉了通道的话，判断selectionKey的合法性，如果不合法说明关掉了，那么就return掉
                return;
            }
            if(selectionKey.isWritable()){
                System.out.println("文件写");
                socketChannel.write(byteBuffer);
                byteBuffer.flip();
                FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\333.TXT"));
                FileChannel fileChannel = fileOutputStream.getChannel();
                int length = 0;
                while((length = fileChannel.write(byteBuffer))> 0){
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
                byteBuffer.clear();
                fileChannel.force(true);
                IOUtil.close(fileChannel);
                IOUtil.close(fileOutputStream);
                IOUtil.close(socketChannel);//这里关了会导致线程池的其他线程的selectionKey无效，就会报错，如果不关的话，这边的通道会一直在使用，所以需要在前边判断
                System.out.println("关了");

            }else if(selectionKey.isReadable()){
                System.out.println("文件读");
                int length = 0;
                //这里要注意不要用-1。。。filechannle和socketChannel还是不一样的
                while((length = socketChannel.read(byteBuffer)) > 0){
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
                byteBuffer.flip();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }

        }catch (Exception e){
            e.printStackTrace();
            selectionKey.cancel();
        }
    }

    class AsyncTask implements  Runnable{
        @Override
        public void run() {
            ComplicatedHandler.this.asyncRun();
        }
    }

}
