package com.oqoqbobo.web.mapper;

import com.oqoqbobo.web.model.dictType.DictTypePO;
import com.oqoqbobo.web.model.dictType.DictTypeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecDictTypeExtendMapper {

    List<DictTypeVO> queryList(DictTypePO page);


}
