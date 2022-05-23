package com.oqoqbobo.web.model.dictType;

import com.data.model.SecDictType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictTypeVO extends SecDictType {
    @ApiModelProperty("创建时间")
    private String createTimeStr;

    @ApiModelProperty("更新时间")
    private String updateTimeStr;
}
