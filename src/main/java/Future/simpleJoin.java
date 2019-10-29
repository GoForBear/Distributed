package Future;

public class simpleJoin {
    private static final int SLEEP_GAP = 500;
    public static String getCurThreadName(){
        return Thread.currentThread().getName();
    }


    static class hotWaterThread extends  Thread{
        public hotWaterThread(){
            super("hot water");
        }

        @Override
        public void run() {
            try{
                System.out.println("begin hot water");
                Thread.sleep(SLEEP_GAP);
                System.out.println("end hot water");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    static class washThread extends  Thread{
        private static final int SLEEP_GAP = 500;
        public washThread(){
            super("wash");
        }

        @Override
        public void run() {
            try{
                System.out.println("begin wash");
                Thread.sleep(SLEEP_GAP);
                System.out.println("end  wash");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread hThread = new hotWaterThread();
        Thread wThread = new washThread();
        hThread.start();
        wThread.start();
        try{
            hThread.join();
            wThread.join();
            Thread.currentThread().setName("主线程");
            System.out.println("to here");

        }catch (Exception e){
            System.out.println(getCurThreadName() + "发生异常");
        }
        System.out.println("over");

    }

}
