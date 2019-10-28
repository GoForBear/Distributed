package Reactor;



import util.NIOConfig;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class simpleReactor implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    simpleReactor() throws Exception{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(NIOConfig.SOCKET_SERVER_IP,NIOConfig.SOCKET_SERVER_PORT));
        SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        selectionKey.attach(new handler());
    }
    @Override
    public void run() {
        System.out.println("启动线程");
        while(! Thread.interrupted()){
            try{
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        dispatch(selectionKey);
                    }
                    iterator.remove();


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey selectionKey){
        Runnable runnable = (Runnable) selectionKey.attachment();
        if(null != runnable){
            runnable.run();
        }
    }

    class  handler implements Runnable{
        @Override
        public void run() {
            System.out.println("开始创建通道");
            try{
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(null != socketChannel){
                    new simpleReactorHandler(selector,socketChannel);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(new simpleReactor()).start();
    }
}
