package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录
    @Override
    public String login(UcenterMember member) {
        //获取手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "手机号、密码为空");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否正确
        if (mobileMember == null) { //数据表中没有这个手机号
            throw new GuliException(20001, "没有这个手机号数据");
        }

        //判断密码
        //把用户输入的密码进行MD5加密,然后和数据库中的密码进行比较
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new GuliException(20001, "密码错误");
        }

        //判断用户是否禁用
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "用户已禁用");
        }

        //登录成功,生成token字符串
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String nickname = registerVo.getNickname(); //昵称
        String mobile = registerVo.getMobile(); //手机号
        String password = registerVo.getPassword(); //密码
        String code = registerVo.getCode(); //验证码

        //非空判断
        if (StringUtils.isEmpty(nickname) ||
            StringUtils.isEmpty(mobile) ||
            StringUtils.isEmpty(password) ||
            StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "注册失败");
        }

        //判断验证码是否正确
        //先从redis中取得验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败");
        }

        //判断手机号是否重复
        //如果表中存在相同的手机号,那就不允许进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) { //数据表中已经有了这个手机号
            throw new GuliException(20001, "注册失败");
        }

        //将数据添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false); //用户未禁用
        member.setAvatar("https://edu-mxy.oss-cn-hangzhou.aliyuncs.com/2022/08" +
                "/28/e97af8298b4c481695cc7723c01c614a1243.jpg"); //用户默认头像
        baseMapper.insert(member);
    }

    //根据openid判断数据表中是否已有这个用户
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //查询某天注册人数(该方法供远程调用使用)
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
