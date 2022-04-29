package com.scwl.hzzxgd.vo;

import com.scwl.hzzxgd.excel.ExcelExport;
import com.scwl.hzzxgd.excel.ExcelImport;
import lombok.Data;

@Data
public class ProcessCenterExcelVo {
    @ExcelExport(value = "工序名称")
    @ExcelImport(value = "工序名称",required = true,unique = true)
    private String centerName;
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
