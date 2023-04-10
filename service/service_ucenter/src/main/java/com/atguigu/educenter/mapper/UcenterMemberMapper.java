package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-09-11
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某天注册人数(该方法供远程调用使用)
    Integer countRegisterDay(String day);
}
