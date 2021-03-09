package com.training.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.dao.IBrandDao;
import com.training.entity.BrandEntity;
import com.training.service.IBrandService;

@Service
public class BrandServiceImpl implements IBrandService {

	@Autowired
	IBrandDao brandDao;

	@Override
	public List<BrandEntity> findAll() {
		return brandDao.findAll();
	}

	@Override
	public List<BrandEntity> findAllSample() {
		List<BrandEntity> brandsList = new ArrayList<BrandEntity>();

		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setBrandId(1L);
		brandEntity.setBrandName("Apple");
		brandEntity.setLogo("logo");
		brandEntity.setDescription("Description");
		brandsList.add(brandEntity);
		
		BrandEntity brandEntity1 = new BrandEntity();
		brandEntity1.setBrandId(2L);
		brandEntity1.setBrandName("Samsung");
		brandEntity1.setLogo("logo");
		brandEntity1.setDescription("Description");
		brandsList.add(brandEntity1);
		
		BrandEntity brandEntity2 = new BrandEntity();
		brandEntity2.setBrandId(3L);
		brandEntity2.setBrandName("Samsung1");
		brandEntity2.setLogo("logo2");
		brandEntity2.setDescription("Description2");
		brandsList.add(brandEntity2);
		return brandsList;
	}
}