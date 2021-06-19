package com.training.service;

import java.util.Map;

import com.training.entity.ProductEntity;
import com.training.model.ResponseDataModel;

public interface IProductService {

	ProductEntity findByProductName(String productName);
	ProductEntity findByProductNameAndProductIdNot(String productName, Long productId);
	
	ResponseDataModel findByProductId(Long id);
	ResponseDataModel searchWithConditions(Map<String, Object> searchConditions, int pageNumber);
	ResponseDataModel findAllWithPagerApi(int pageNumber);
	ResponseDataModel addProductApi(ProductEntity productEntity);
	ResponseDataModel updateProductApi(ProductEntity productEntity);
	ResponseDataModel deleteById(Long id);
}
