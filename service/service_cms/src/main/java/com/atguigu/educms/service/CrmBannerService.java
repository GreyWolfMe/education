package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-07
 */
public interface CrmBannerService extends IService<CrmBanner> {
    //根据banner的id进行降序排列,显示排列之后的前两条记录
    List<CrmBanner> selectAllBanner();
}
