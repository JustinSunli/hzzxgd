package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductUnitEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.service.ProductUnitService;
import com.scwl.hzzxgd.service.impl.ProductUnitServiceImpl;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins ="*")
public class ExcelPortController {
    @Resource
    private ProductUnitServiceImpl productUnitService;

    @RequestMapping("/excelPortUnit")
    public void excelPortUnit(HttpServletResponse response){
        List<ProductUnitEntity> list = productUnitService.list();
        ExcelUtils.export(response,"产品单位",list,ProductUnitEntity.class);
    }







}
