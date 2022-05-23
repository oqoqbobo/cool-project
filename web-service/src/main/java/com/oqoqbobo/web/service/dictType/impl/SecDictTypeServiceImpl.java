package com.oqoqbobo.web.service.dictType.impl;


import com.data.mapper.SecDictTypeMapper;
import com.oqoqbobo.web.mapper.SecDictTypeExtendMapper;
import com.oqoqbobo.web.model.dictType.DictTypePO;
import com.oqoqbobo.web.model.dictType.DictTypeVO;
import com.oqoqbobo.web.service.dictType.SecDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecDictTypeServiceImpl implements SecDictTypeService {
    @Autowired
    private SecDictTypeMapper secDictTypeMapper;
    @Autowired
    private SecDictTypeExtendMapper secDictTypeExtendMapper;

    @Override
    public List<DictTypeVO> queryList(DictTypePO po) {
//        PageHelper.startPage(po.getPageNo(), po.getPageSize());
        po.setPageSort("create_time desc,type_id");
        List<DictTypeVO> list = secDictTypeExtendMapper.queryList(po);
//        PageInfo<SecDictType> info = new PageInfo<>(list);
//        po.setTotal(info.getTotal());
        return list;
    }
}
