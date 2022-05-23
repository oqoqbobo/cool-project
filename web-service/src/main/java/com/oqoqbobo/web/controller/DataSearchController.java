package com.oqoqbobo.web.controller;

import com.oqoqbobo.web.data.PageResultList;
import com.oqoqbobo.web.data.ReturnObj;
import com.oqoqbobo.web.model.dictType.DictTypePO;
import com.oqoqbobo.web.model.dictType.DictTypeVO;
import com.oqoqbobo.web.service.dictType.SecDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "查询数据库接口")
@RestController
@RequestMapping("/database/")
public class DataSearchController {
    @Autowired
    SecDictTypeService secDictTypeService;

    @PostMapping(value = "/test")
    @ApiOperation(value="测试", notes="测试")
    public ReturnObj<PageResultList<DictTypeVO>> test(DictTypePO po){
        List<DictTypeVO> typeList = secDictTypeService.queryList(po);

        return ReturnObj.success("成功连接数据库，好开心",PageResultList.result(po,typeList));
    }
}
