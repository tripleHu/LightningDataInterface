package cn.cqu.edu.LightningDataInterface.Tools;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SyncAdtdTask {
	// 每五秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void TaskJob() {
        //System.out.println("test second annotation style ...");
    }

}
