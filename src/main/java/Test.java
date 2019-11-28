import NIO.NIOProject;
import NIO.NIOTCPSocket;

import java.nio.channels.SelectionKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {




    public static int getTimeDifference(String firstDay, String secondDay, String dateFormat) {
        if (dateFormat == null || dateFormat.trim().length() == 0) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        DateFormat df = new SimpleDateFormat(dateFormat);

        try {
            Date d1 = df.parse(firstDay);
            Date d2 = df.parse(secondDay);
            long diff = d1.getTime() - d2.getTime();
            double days = diff / (1000.0 * 60 * 60 * 24);
            System.out.println(days);

            return (int)Math.abs(days)+1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {
//        String srcFileName = "D:\\TEST.TXT";
//        String dstFileName ="D:\\333.TXT";
//        int result  = NIOProject.copyFile(srcFileName,dstFileName);
//        System.out.println(result);
//        NIOTCPSocket niotcpSocket = new NIOTCPSocket();
//        niotcpSocket.sendFile(srcFileName);
//        System.out.println(SelectionKey.OP_ACCEPT);
//        System.out.println(Math.ceil(150000/(24.0*60*60)));
        DateFormat sdf = new SimpleDateFormat( "yyyyMMdd");
        try{
//            Date d1 = sdf.parse("20191124235958".substring(0,8));
//            Date d2 = sdf.parse("20191117130641".substring(0,8));
//            long diff = d1.getTime() - d2.getTime();
//            System.out.println(diff);
//            double days = diff / (1000.0 * 60 * 60 * 24);
//            System.out.println((int)Math.abs(days)+1);
//            System.out.println("333");
//            System.out.println((int)Math.abs(Math.ceil(days)));
//            SimpleDateFormat sdf = getYear2MinuteSDF();
//        System.out.println(this.getClass());
//            DateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");

//            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
//            calendar.set(Calendar.HOUR_OF_DAY, 23);
//            calendar.set(Calendar.MINUTE, 59);
//            calendar.set(Calendar.SECOND, 59);
//            String to = sf.format(calendar.getTime());
//            System.out.println("回退日期from为:" + to);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
//            calendar.set(Calendar.HOUR_OF_DAY, 0); // 24 小时制
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.MILLISECOND, 000);

            String a ="ab";
            String b = "ab";
            System.out.println(a.equals(b));



//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
//            calendar.set(Calendar.HOUR_OF_DAY, 23); // 24 小时制
//            calendar.set(Calendar.MINUTE, 59);
//            calendar.set(Calendar.SECOND, 59);
//            calendar.set(Calendar.MILLISECOND, 999);

//            System.out.println(sdf.format(calendar.getTime()));
        }catch (Exception e){
            e.printStackTrace();

        }


    }
}
