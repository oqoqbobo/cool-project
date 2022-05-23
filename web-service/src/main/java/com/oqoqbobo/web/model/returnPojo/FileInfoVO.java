package com.oqoqbobo.web.model.returnPojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FileInfoVO {
    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("是否是文件夹")
    private Boolean isFile;

    @ApiModelProperty("子文件集合")
    List<FileInfoVO> myFileList;
}
