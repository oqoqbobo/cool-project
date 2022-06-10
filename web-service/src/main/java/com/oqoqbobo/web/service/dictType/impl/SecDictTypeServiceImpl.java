package com.oqoqbobo.web.service.dictType.impl;


import com.data.mapper.SecDictTypeMapper;
import com.data.model.SecDictType;
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
        po.setPageSort("create_time desc,typeId desc");
        List<DictTypeVO> list = secDictTypeExtendMapper.queryList(po);
//        PageInfo<SecDictType> info = new PageInfo<>(list);
//        po.setTotal(info.getTotal());
        return list;
    }

    @Override
    public void update(Long id){
        SecDictType secDictType = new SecDictType();
        secDictType.setTypeId(id);
        secDictType.setRemarks("我是蔡镜波，哈哈哈哈");
        secDictTypeMapper.updateByPrimaryKeySelective(secDictType);
    }

    @Override
    public DictTypeVO queryById(Long id) {
//        PageHelper.startPage(po.getPageNo(), po.getPageSize());
//        po.setPageSort("create_time desc");
        DictTypePO po = new DictTypePO();
        po.setPageNo(-1);
        po.setPageSize(1);
        po.setId(id);

        List<DictTypeVO> list = secDictTypeExtendMapper.queryList(po);
//        PageInfo<SecDictType> info = new PageInfo<>(list);
//        po.setTotal(info.getTotal());
        return list.size() > 0 ? list.get(0) : null;
    }
}
