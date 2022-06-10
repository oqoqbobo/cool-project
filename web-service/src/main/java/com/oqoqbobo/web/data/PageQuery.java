package com.oqoqbobo.web.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQuery {
    @ApiModelProperty(value = "当前查询页数",required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "查询数量",required = true)
    private Integer pageSize;

    @ApiModelProperty(value="排序",hidden = true)
    private String pageSort;

    @ApiModelProperty(value = "总数量",required = false,hidden = true)
    private Long total;
}
