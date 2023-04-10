package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService staService;

    //在每天的凌晨一点查询前一天的统计数据并插入到统计表
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期(格式是yyyy-MM-dd)
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.registerCount(day);
    }
}
