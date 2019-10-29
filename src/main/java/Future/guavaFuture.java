package Future;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class guavaFuture {
    private static final int SLEEP_GAP = 500;
    public static String getCurThreadName(){
        return Thread.currentThread().getName();
    }
    static class hotWaterJob implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try{
                System.out.println("开始烧水");
                Thread.sleep(SLEEP_GAP);
                System.out.println("水开了");
            }catch (Exception e){
                return false;
            }
            System.out.println("烧水线程结束了");
            return true;
        }
    }

    static class washJob implements Callable<Boolean>{
        @Override
        public Boolean call() {
            try{
                System.out.println("开始洗茶壶");
                Thread.sleep(SLEEP_GAP);
                System.out.println("茶壶洗好了");
            }catch (Exception e){
                return false;
            }
            System.out.println("洗茶壶线程结束了");
            return true;
        }
    }

    static class mainJob implements Runnable{
        private boolean water = false;
        private boolean wash = false;
        private int gap = SLEEP_GAP/10;
        @Override
        public void run() {
            while (true){
                try{
                    Thread.sleep(gap);
                    System.out.println("读书中。。。");
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(water && wash){
                    drinkTea(water,wash);
                }
            }
        }
        public  void drinkTea(boolean water, boolean wash){
            if(water && wash){
                System.out.println("喝茶啦");
                this.water = false;
                this.gap = SLEEP_GAP * 100;
            }else if(!water){
                System.out.println("水没烧");
            }else if(!wash){
                System.out.println("壶没洗");
            }
        }
    }

    public static void main(String[] args) {
        final mainJob mj = new mainJob();
        Thread mainThread = new Thread(mj);
        mainThread.setName("主线程");
        mainThread.start();

        Callable<Boolean> hJbo = new hotWaterJob();
        Callable<Boolean> wJob = new washJob();

        ExecutorService jpool = Executors.newFixedThreadPool(10);
        ListeningExecutorService gpool = MoreExecutors.listeningDecorator(jpool);

        ListenableFuture<Boolean> hFuture = gpool.submit(hJbo);
        Futures.addCallback(hFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    mj.water = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("烧水失败了");
            }
        });

        ListenableFuture<Boolean> wFuture = gpool.submit(wJob);
        Futures.addCallback(wFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    mj.wash = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("杯子没洗干净");
            }
        });

    }


}
