package com.atguigu.demo.mxycivil;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EntityWrite {
    //设置excel表头名称
    @ExcelProperty("机关（单位）名称")
    private Integer unit;

    @ExcelProperty("职位名称")
    private String positionName;

    @ExcelProperty("职位代码")
    private String positionCode;

    @ExcelProperty("招录人数")
    private String positionNum;

    @ExcelProperty("年龄要求")
    private String thresholdAge;

    @ExcelProperty("学历（学位）要求")
    private String thresholdDiploma;

    @ExcelProperty("工作经历")
    private String thresholdExperience;

    @ExcelProperty("其他要求")
    private String thresholdOther;

    @ExcelProperty("专业")
    private String thresholdMajor;

    @ExcelProperty("体检标准")
    private String bodyCheck;

    @ExcelProperty("是否进行体能测评")
    private String physicalFitnessTest;

    @ExcelProperty("备注")
    private String remark;
}
