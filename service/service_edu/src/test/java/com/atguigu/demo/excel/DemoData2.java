package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData2 {
    //设置excel第一列对应的属性
    @ExcelProperty(index = 0)
    private Integer sno;

    //设置excel第二列对应的属性
    @ExcelProperty(index = 1)
    private String sname;
}
