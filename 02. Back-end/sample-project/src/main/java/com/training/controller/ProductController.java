package com.training.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.training.common.constant.Constants;
import com.training.entity.ProductEntity;
import com.training.model.ResponseDataModel;
import com.training.service.IProductService;

@Controller
@RequestMapping(value = { "/product" })
public class ProductController {

	@Autowired
	IProductService productService;
	
	@GetMapping
	public String indexProduct() {
		return "product/product-index";
	}
	
	
	@GetMapping("/api/{pageNumber}")
	@ResponseBody
	public ResponseDataModel findAllWithPagerApi(@PathVariable("pageNumber") int pageNumber) {
		return productService.findAllWithPagerApi(pageNumber);
	}
	
	@GetMapping("/api")
	@ResponseBody
	public ResponseDataModel findProductById(@RequestParam("id") Long id) {
		return productService.findByProductId(id);
	}
	
	@PostMapping("/api/")
	@ResponseBody
	public ResponseDataModel addProductApi(@ModelAttribute ProductEntity productEntity) {
		return productService.addProductApi(productEntity);
	}
	
	@PostMapping("/api/{productId}")
	@ResponseBody
	public ResponseDataModel updateProductApi(	@ModelAttribute ProductEntity productEntity,
												@PathVariable(required = false) Long productId) {
		if(productId != null ) {
			return productService.updateProductApi(productEntity);
		}else {
			return new ResponseDataModel(Constants.RESULT_CD_FAIL , "Unknown Product");
		}
		
	}
	
	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseDataModel deleteByIdApi(@PathVariable("id") Long id) {
		return productService.deleteById(id);
	}
	
	@PostMapping(value = {"/api/searchApi/{pageNumber}"})
	@ResponseBody
	public ResponseDataModel searchApi(@RequestParam Map<String, Object> searchConditions, @PathVariable("pageNumber") int pageNumber) {
		return productService.searchWithConditions(searchConditions, pageNumber);
	}
	
	
	
}
