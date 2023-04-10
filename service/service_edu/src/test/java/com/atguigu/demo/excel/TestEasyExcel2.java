package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

public class TestEasyExcel2 {
    public static void main(String[] args) {
        //实现excel的写操作

        //1.设置读取的excel文件的路径和名称
        String filename = "C:\\Users\\mxy\\Desktop\\mxy01.xlsx";

        //2.调用easyexcel的方法实现读操作
        EasyExcel
                .read(
                    filename,
                    DemoData2.class,
                    new ExcelListener())
                .sheet()
                .doRead();
    }
}
