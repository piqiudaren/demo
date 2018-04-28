package com.pyc.demo.mapper;

import com.pyc.demo.model.City;
import com.pyc.demo.model.Town;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TownMapper {
    void insertBatch(@Param("list")List<Town> list);
}
