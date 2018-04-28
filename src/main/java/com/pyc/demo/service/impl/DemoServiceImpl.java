package com.pyc.demo.service.impl;

import com.pyc.demo.mapper.CityMapper;
import com.pyc.demo.mapper.CountryMapper;
import com.pyc.demo.mapper.ProvinceMapper;
import com.pyc.demo.mapper.TownMapper;
import com.pyc.demo.mapper.VillageMapper;
import com.pyc.demo.model.City;
import com.pyc.demo.model.County;
import com.pyc.demo.model.Province;
import com.pyc.demo.model.Town;
import com.pyc.demo.model.Village;
import com.pyc.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yansou
 */
@Service("demoService")
@Transactional
public class DemoServiceImpl implements DemoService{

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private TownMapper townMapper;

    @Autowired
    private VillageMapper villageMapper;

    @Override
    public List<Province> saveProvince(List<Province> provinces){
        provinceMapper.insertBatch(provinces);
        return provinces;
    }

    @Override
    public List<City> saveCity(List<City> cityList){
         cityMapper.insertBatch(cityList);
        return cityList;
    }

    @Override
    public List<County>  saveCountry(List<County> countyList){
        countryMapper.insertBatch(countyList);
        return countyList;
    }

    @Override
    public List<Town> saveTown(List<Town> towns){
        townMapper.insertBatch(towns);
        return towns;
    }

    @Override
    public List<Village> saveVillage(List<Village> villages){
        villageMapper.insertBatch(villages);
        return villages;
    }
}
