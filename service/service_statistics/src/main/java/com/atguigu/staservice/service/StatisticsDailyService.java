package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-18
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //统计某天的注册人数(远程调用)并加到统计表
    void registerCount(String day);

    //图表显示,返回两部分数据:日期的json数组(横坐标)和数量的json数组(纵坐标)
    Map<String, Object> getShowData(String type, String begin, String end);
}
