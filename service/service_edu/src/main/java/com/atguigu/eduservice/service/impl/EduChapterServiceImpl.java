package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-21
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    //根据课程id查询并返回某一门课程下的所有章节、小节
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1.根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2.根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //3.创建list集合,用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //4.遍历查询出来的章节的list集合,进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            //①得到eduChapterList中的每个EduChapter对象
            EduChapter eduChapter = eduChapterList.get(i);
            //②把eduChapter中我们需要的值获取出来,放到chapterVo中
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            //③把chapterVo放到最终list集合
            finalList.add(chapterVo);

            //④封装某一章节下的所有小节
            //创建list集合封装每个章节下的所有小节
            List<VideoVo> videoList = new ArrayList<>();
            //遍历查询出来的小节的list集合,进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                //④.1:得到eduVideoList中的每个EduVideo对象
                EduVideo eduVideo = eduVideoList.get(m);

                //若eduVideo的chapter_id和章节的id相等,那么就进行封装
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    //④.2:把eduVideo中我们需要的值获取出来,放到videoVo中
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);

                    //④.3:将每个videoVo放到videoList中
                    videoList.add(videoVo);
                }
                //④.4把某一章节下所有小节放到章节里面
                chapterVo.setChildren(videoList);
            }
        }
        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据章节id(chapterid)查询小节表(edu_video),若能查到数据,就不删除该章节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = videoService.count(wrapper); //返回符合条件的数据的个数
        //判断
        if (count > 0) { //该章节下有小节,不删除该章节
            throw new GuliException(20001, "不删除");
        } else { //该章节下没有小节,删除该章节
            int result = baseMapper.deleteById(chapterId);
            return  result > 0;
        }
    }

    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
