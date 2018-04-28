package com.pyc.demo.mapper;

import com.pyc.demo.model.City;
import com.pyc.demo.model.County;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CountryMapper {
    void insertBatch(@Param("list")List<County> list);
}
