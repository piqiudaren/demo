package com.pyc.demo.service;

import com.pyc.demo.model.City;
import com.pyc.demo.model.County;
import com.pyc.demo.model.Province;
import com.pyc.demo.model.Town;
import com.pyc.demo.model.Village;

import java.util.List;

public interface DemoService {
    List<Province> saveProvince(List<Province> provinces);

    List<City> saveCity(List<City> cityList);

    List<County> saveCountry(List<County> countyList);

    List<Town> saveTown(List<Town> towns);

    List<Village> saveVillage(List<Village> villages);
}
