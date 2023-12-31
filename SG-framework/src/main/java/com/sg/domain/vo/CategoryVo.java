package com.sg.domain.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //用法含义：为null的字段不序列化
public class CategoryVo {
    @ExcelProperty("分类Id")
    private Long id;
    //标题
    @ExcelProperty("分类名")
    private String name;

    @ExcelProperty("描述")
    private String description;
    //状态0:正常,1禁用

    @ExcelProperty("状态0:正常,1禁用")
    private String status;
}
