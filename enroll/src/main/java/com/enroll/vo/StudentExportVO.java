package com.enroll.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentExportVO {

    @ExcelProperty("学生姓名")
    private String name;

    @ExcelProperty("身份证号")
    private String idCard;

    @ExcelProperty("家长手机号")
    private String parentPhone;

    @ExcelProperty("房产地址")
    private String address;

    @ExcelProperty("提交时间")
    private String createTime;
}
