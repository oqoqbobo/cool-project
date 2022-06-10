package com.oqoqbobo.web.service.dictType;

import com.oqoqbobo.web.model.dictType.DictTypePO;
import com.oqoqbobo.web.model.dictType.DictTypeVO;

import java.util.List;

public interface SecDictTypeService {
    List<DictTypeVO> queryList(DictTypePO po);
    DictTypeVO queryById(Long id);

    void update(Long id) throws Exception;
}
