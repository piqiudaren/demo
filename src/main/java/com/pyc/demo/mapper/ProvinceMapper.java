package com.pyc.demo.mapper;

import com.pyc.demo.model.Province;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yansou
 */
public interface ProvinceMapper {

    void insertBatch(@Param("list")List<Province> list);
}
