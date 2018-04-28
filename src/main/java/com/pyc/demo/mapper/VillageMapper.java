package com.pyc.demo.mapper;

import com.pyc.demo.model.City;
import com.pyc.demo.model.Village;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VillageMapper {
    void insertBatch(@Param("list")List<Village> list);
}
