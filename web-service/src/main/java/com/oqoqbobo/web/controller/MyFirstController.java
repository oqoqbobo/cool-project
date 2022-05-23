package com.oqoqbobo.web.controller;

import com.data.mapper.SecDictTypeMapper;
import com.data.model.SecDictType;
import com.data.model.SecDictTypeExample;
import com.oqoqbobo.web.data.ReturnObj;
import com.oqoqbobo.web.data.ReturnValue;
import com.oqoqbobo.web.service.fileHandle.FileHandleService;
import com.oqoqbobo.web.service.fileHandle.impl.FileHandleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "测试swagger接口")
@RestController
@RequestMapping("/web/")
public class MyFirstController {

    @Autowired
    private FileHandleService fileHandleService;

    @Autowired
    private SecDictTypeMapper dictTypeMapper;

    @GetMapping(value = "/test")
    @ApiOperation(value="测试", notes="测试")
    public ReturnObj test(){
        SecDictTypeExample example = new SecDictTypeExample();
        example.createCriteria().andTypeCodeEqualTo("securityType");
        List<SecDictType> list = dictTypeMapper.selectByExample(example);
        return ReturnObj.success("成功连接数据库，好开心",list);
    }

    @GetMapping(value = "/handleContent")
    @ApiOperation(value="处理文件内容", notes="通过文件名称和文件路径进行定位，处理国际化")
    public ReturnValue handleContent(String filePath,String setContent,Boolean isAppened){
        try {

            fileHandleService.handleContent(filePath,setContent,isAppened);
            return ReturnValue.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnValue.fail(e.getMessage());
        }
    }

    @GetMapping(value = "/handleFile")
    @ApiOperation(value="处理文件夹内容", notes="通过文件夹路劲，处理内部所有文件的国际化")
    public ReturnValue handleFile(String filePath,String setContent,Boolean isAppened){
        try {
            fileHandleService.handleFile(filePath,setContent,isAppened);
            return ReturnValue.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnValue.fail(e.getMessage());
        }
    }

    @GetMapping(value = "/downLoadOutText")
    @ApiOperation(value="下载输出文件", notes="下载处理后的输出文件")
    public void downLoadOutText(HttpServletResponse response){
        try {
            fileHandleService.downLoadOutText(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/resetKeyIsUsedOutTxt")
    @ApiOperation(value="删除out文件中重复或者冗余的翻译", notes="重置out的文本内容")
    public ReturnValue resetKeyIsUsedOutTxt(String filePath){
        try {
            fileHandleService.resetOutText(filePath);
            return ReturnValue.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnValue.fail("执行失败");
    }

}
