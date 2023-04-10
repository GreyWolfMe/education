package com.atguigu.demo.mxycivil;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EntityRead {
    //设置excel第一列对应的属性
    @ExcelProperty(index = 0)
    private Integer unit;

    //设置excel第二列对应的属性
    @ExcelProperty(index = 1)
    private String positionName;

    @ExcelProperty(index = 2)
    private String positionCode;

    @ExcelProperty(index = 3)
    private String positionNum;

    @ExcelProperty(index = 4)
    private String thresholdAge;

    @ExcelProperty(index = 5)
    private String thresholdDiploma;

    @ExcelProperty(index = 6)
    private String thresholdExperience;

    @ExcelProperty(index = 7)
    private String thresholdOther;

    @ExcelProperty(index = 8)
    private String thresholdMajor;

    @ExcelProperty(index = 9)
    private String bodyCheck;

    @ExcelProperty(index = 10)
    private String physicalFitnessTest;

    @ExcelProperty(index = 11)
    private String remark;
}
