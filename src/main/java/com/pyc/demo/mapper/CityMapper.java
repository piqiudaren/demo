package com.pyc.demo.mapper;

import com.pyc.demo.model.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CityMapper {
    void insertBatch(@Param("list")List<City> list);
}
