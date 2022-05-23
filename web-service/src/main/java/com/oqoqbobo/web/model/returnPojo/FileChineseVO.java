package com.oqoqbobo.web.model.returnPojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileChineseVO {
    @ApiModelProperty("结果是否成功")
    private Boolean result;
    @ApiModelProperty("执行的结果")
    private String[] contents;
}
