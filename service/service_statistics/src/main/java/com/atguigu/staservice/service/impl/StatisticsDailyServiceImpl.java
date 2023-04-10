package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-18
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;

    //统计某天的注册人数(远程调用)并加到统计表
    @Override
    public void registerCount(String day) {
        //插入新数据之前,先删掉表中与即将插入的数据日期相同的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        //远程调用,得到某天的注册人数
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer)registerR.getData().get("countRegister");

        //把获取到的数据添加到统计表
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister); //注册人数
        sta.setDateCalculated(day); //统计的哪天的数据
        //统计表中的其它数据我们这里模拟生成随机数,后期自行完善
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setCourseNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(sta);
    }

    //图表显示,返回两部分数据:日期的json数组(横坐标)和数量的json数组(纵坐标)
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //1.根据条件查询对应数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //2.封装数据(我们需要返回两部分数据:统计日期 和 该日期对应的数量)
        //2.1创建两个list集合
        List<String> date_calculated = new ArrayList<>(); //统计日期
        List<Integer> numDataList = new ArrayList<>(); //数量
        //2.2遍历staList进行数据的封装
        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //2.2.1封装日期list集合
            date_calculated.add(daily.getDateCalculated());
            //2.2.2封装对应数量
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        //3.将封装后的两个list集合放到map集合中并返回
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculated", date_calculated);
        map.put("numDataList", numDataList);
        return map;
    }
}
