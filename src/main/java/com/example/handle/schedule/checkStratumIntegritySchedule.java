package com.example.handle.schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
// import com.example.handle.service.HandleService;
import com.example.handle.service.StratumService;

@Configuration
@EnableScheduling
public class checkStratumIntegritySchedule {
    // 启用定时任务
    @Autowired
    private StratumService stratumService;

    // 每隔 1 小时执行一次检查（可以根据实际需求修改）
    @Scheduled(cron = "0 0/1 * * * ?")  // 这个是 cron 表达式，表示每小时执行一次
    public void checkStratumIntegrity() {
        stratumService.checkStratumIntegrityGlobally();
    }
}







