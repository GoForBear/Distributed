package quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class HelloJob implements Job {

    /**
     * Job，Job需要一个公有的构造函数，否则Factory无法构建
     */
    public HelloJob(){

    }

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("hello");
    }
}
