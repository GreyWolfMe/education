package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    //上传视频到阿里云
    String uploadAlyVideo(MultipartFile file);

    //根据多个视频id删除阿里云中的视频
    void removeMoreAlyVideo(List videoIdList);
}
