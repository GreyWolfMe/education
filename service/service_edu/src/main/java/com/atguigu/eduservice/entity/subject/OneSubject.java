package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//一级分类
@Data
public class OneSubject {
    private String id;
    private String title;

    //一个一级分类有n个二级分类,n=1,2,3...
    private List<TwoSubject> children = new ArrayList<>();
}
