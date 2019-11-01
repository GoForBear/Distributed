package Reactor;

import util.NIOConfig;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class complicatedReactor {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    Selector[] selectors = new Selector[2];
    subReactor[] subReactors = null;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    complicatedReactor() throws  Exception{
        selectors[0]  = Selector.open();
        selectors[1]  = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(NIOConfig.SOCKET_SERVER_IP,NIOConfig.SOCKET_SERVER_PORT));
        SelectionKey selectionKey  = serverSocketChannel.register(selectors[0],SelectionKey.OP_ACCEPT);
        selectionKey.attach(new handler());
        subReactor subReactor1 = new subReactor(selectors[0]);
        subReactor subReactor2 = new subReactor(selectors[1]);
        subReactors = new subReactor[]{subReactor1,subReactor2};
    }

    private void startService(){
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();

    }


    class subReactor implements Runnable{
        private Selector selector;
        subReactor(Selector selector){
            this.selector = selector;
        }
        @Override
        public void run() {
            while(! Thread.interrupted()){
                try{
                    selector.select();
                    System.out.println("又开始了");
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();
                    while(it.hasNext()){
                        SelectionKey selectionKey = it.next();
                        dispatch(selectionKey);
                    }
                    keySet.clear();
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
    }

    class  handler implements Runnable{
        @Override
        public void run() {
            System.out.println("开始创建通道");
            try{
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(null != socketChannel){
                    System.out.println("第" + atomicInteger.get()+"个selector");
                    new ComplicatedHandler(selectors[atomicInteger.get()],socketChannel);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(atomicInteger.incrementAndGet() == selectors.length){
                atomicInteger.set(0);
            }
        }
    }

    public static void main(String[] args) throws  Exception {
        complicatedReactor complicatedReactor = new complicatedReactor();
        complicatedReactor.startService();

    }

}
