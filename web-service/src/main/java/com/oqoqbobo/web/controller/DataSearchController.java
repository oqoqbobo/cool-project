package com.oqoqbobo.web.controller;

import cn.jy.operationLog.core.OperationLog;
import cn.jy.operationLog.core.OperationLogContext;
import com.oqoqbobo.web.data.PageResultList;
import com.oqoqbobo.web.data.ReturnObj;
import com.oqoqbobo.web.data.ReturnValue;
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

    @PostMapping(value = "/update")
    @OperationLog //在此加上注解
    @ApiOperation(value="更新一条记录", notes="测试")
    public ReturnValue update(Long id){
        OperationLogContext.follow(() -> secDictTypeService.queryById(id));
        try {
            secDictTypeService.update(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnValue.success("修改数据库失败，请联系管理员");
        }

        return ReturnValue.success("成功修改数据库，好开心");
    }
}
