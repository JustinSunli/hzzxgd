package com.scwl.hzzxgd.vo;

import com.scwl.hzzxgd.excel.ExcelExport;
import com.scwl.hzzxgd.excel.ExcelImport;
import lombok.Data;

@Data
public class ProductUnitExcelVo {
    @ExcelExport(value = "产品单位")
    @ExcelImport(value = "产品单位",required = true,unique = true)
    private String unitName;
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
