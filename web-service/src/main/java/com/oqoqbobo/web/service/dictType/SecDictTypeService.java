package com.oqoqbobo.web.service.dictType;

import com.oqoqbobo.web.model.dictType.DictTypePO;
import com.oqoqbobo.web.model.dictType.DictTypeVO;

import java.util.List;

public interface SecDictTypeService {
    List<DictTypeVO> queryList(DictTypePO po);
}
