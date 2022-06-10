package com.oqoqbobo.web.model.dictType;

import com.oqoqbobo.web.data.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictTypePO extends PageQuery {
    @ApiModelProperty("Id")
    private Long id;
}
