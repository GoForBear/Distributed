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




public class simpleReactorHandler implements Runnable {
    private Selector selector;
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    private static  final  int RECIEVING = 0, SENDING = 1;
    private int state = RECIEVING;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConfig.capacity);
    simpleReactorHandler(Selector selector, SocketChannel socketChannel) throws Exception{
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector,0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);//这个就是直接给selectionkey一个定值，等于是说无论什么通道都监控read状态
        System.out.println("让我康康");
        selector.wakeup();
    }
    @Override
    public void run() {
        try{
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
                selectionKey.interestOps(SelectionKey.OP_READ);
                state =   RECIEVING ;
                IOUtil.close(socketChannel);

            }else if(selectionKey.isReadable()){
                System.out.println("文件读");
                int length = 0;
                //这里要注意不要用-1。。。filechannle和socketChannel还是不一样的
                while((length = socketChannel.read(byteBuffer)) > 0){
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
                byteBuffer.flip();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
