package com.data.mapper;

import com.data.model.SecDictType;
import com.data.model.SecDictTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecDictTypeMapper {
    long countByExample(SecDictTypeExample example);

    int deleteByExample(SecDictTypeExample example);

    int deleteByPrimaryKey(Long typeId);

    int insert(SecDictType record);

    int insertSelective(SecDictType record);

    List<SecDictType> selectByExample(SecDictTypeExample example);

    SecDictType selectByPrimaryKey(Long typeId);

    int updateByExampleSelective(@Param("record") SecDictType record, @Param("example") SecDictTypeExample example);

    int updateByExample(@Param("record") SecDictType record, @Param("example") SecDictTypeExample example);

    int updateByPrimaryKeySelective(SecDictType record);

    int updateByPrimaryKey(SecDictType record);
}