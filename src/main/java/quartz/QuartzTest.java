package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.MonthlyCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class QuartzTest {
    public static void main(String[] args) {
        try{
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            Date runtime = DateBuilder.evenMinuteDate(new Date());

            MonthlyCalendar monthlyCalendar = new MonthlyCalendar();
            monthlyCalendar.setDayExcluded(29,true);
//            Calendar excludeDay = new GregorianCalendar();
//            excludeDay.setTime(new Date());
//            annualCalendar.setDayExcluded(excludeDay,true);
            scheduler.addCalendar("myDay",monthlyCalendar,false,false);

            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("job1","group1")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
//                    .startAt(runtime);
//                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ?"))
                    .modifiedByCalendar("myDay")
                    //forjob这个是用来干啥的
//                    .forJob("job1","group1")
                    .build();
            scheduler.scheduleJob(job,trigger);
            scheduler.start();
            Thread.sleep(65L * 1000L);
            scheduler.shutdown(true);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
