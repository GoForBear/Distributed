package Future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class futureJoin {

    private static final int SLEEP_GAP = 500;
    public static String getCurThreadName(){
        return Thread.currentThread().getName();
    }


    static class hotWaterJob implements Callable<Boolean>{
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
                Thread.sleep(2*SLEEP_GAP);
                System.out.println("茶壶洗好了");
            }catch (Exception e){
                return false;
            }
            System.out.println("洗茶壶线程结束了");
            return true;
        }
    }

    public static  void drinkTea(boolean water, boolean wash){
        if(water && wash){
            System.out.println("泡茶喝了");
        }else if(!water){
            System.out.println("水没烧开");
        }else if(!wash){
            System.out.println("没洗好茶壶");
        }
    }

    public static void main(String[] args) {
        Callable<Boolean> hJob = new hotWaterJob();
        FutureTask<Boolean> hTask = new FutureTask<>(hJob);
        Thread hThread = new Thread(hTask,"烧水Thread");

        Callable<Boolean> wJob = new washJob();
        FutureTask<Boolean> wTask = new FutureTask<>(wJob);
        Thread wThread = new Thread(wTask,"洗壶Thread");

        hThread.start();
        wThread.start();
        Thread.currentThread().setName("主线程");
        try{
            boolean water = hTask.get();
            boolean wash = wTask.get();
            drinkTea(water,wash);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("运行结束");
    }



}
