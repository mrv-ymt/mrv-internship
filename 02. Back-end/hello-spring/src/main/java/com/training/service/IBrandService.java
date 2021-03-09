package com.training.service;

import java.util.List;

import com.training.entity.BrandEntity;

public interface IBrandService {

	List<BrandEntity> findAll();

	List<BrandEntity> findAllSample();
}
