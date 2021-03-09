package com.training.service;

import java.util.Map;

import com.training.model.ResponseDataModel;

public interface IProductService {

	ResponseDataModel searchWithConditions(Map<String, Object> searchConditions, int pageNumber);

}
