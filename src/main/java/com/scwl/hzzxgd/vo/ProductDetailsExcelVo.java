package com.scwl.hzzxgd.vo;

import com.scwl.hzzxgd.excel.ExcelExport;
import com.scwl.hzzxgd.excel.ExcelImport;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductDetailsExcelVo {
    /**
     * 产品编码
     */
    @ExcelExport(value = "产品编码")
    @ExcelImport(value = "产品编码",required = true,unique = true)
    private String code;
    /**
     * 产品名称
     */
    @ExcelExport(value = "产品名称")
    @ExcelImport(value = "产品名称")
    private String name;
    /**
     * 产品规格
     */
    @ExcelExport(value = "产品规格")
    @ExcelImport(value = "产品规格")
    private String spec;
    /**
     * 产品单位名称
     */
    @ExcelExport(value = "产品单位名称")
    @ExcelImport(value = "产品单位名称")
    private String unitName;
    /**
     * 尺寸
     */
    @ExcelExport(value = "尺寸")
    @ExcelImport(value = "尺寸")
    private String size;
    /**
     * 颜色
     */
    @ExcelExport(value = "颜色")
    @ExcelImport(value = "颜色")
    private String color;
    /**
     * 附件
     */
    @ExcelExport(value = "附件")
    @ExcelImport(value = "附件")
     private String attachment;
     /**
     * 照片
     */
    @ExcelExport(value = "照片")
    @ExcelImport(value = "照片")
    private String picture;
    /**
     * 备注说明
     */
    @ExcelExport(value = "备注说明")
    @ExcelImport(value = "备注说明")
    private String remark;
    /**
     * 原始数据
     */
    private String rowData;
    /**
     * 错误提示
     */
    private String rowTips;
    /**
     * 行号
     */
    private Integer rowNum;
}
