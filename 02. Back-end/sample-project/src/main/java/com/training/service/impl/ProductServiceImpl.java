package com.training.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.training.common.constant.Constants;
import com.training.common.util.FileHelper;
import com.training.dao.IProductDao;
import com.training.dao.jpaspec.ProductJpaSpecification;
import com.training.entity.ProductEntity;
import com.training.model.PagerModel;
import com.training.model.ResponseDataModel;
import com.training.service.IProductService;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${parent.folder.images.product}")
	private String productLogoFolderPath;
	
	@Autowired
	IProductDao productDao;

	@Override
	public ResponseDataModel searchWithConditions(Map<String, Object> searchConditions, int pageNumber) {

		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		Map<String, Object> responseMap = new HashMap<>();
		try {

			Sort sortInfo = Sort.by(Sort.Direction.DESC, "productId");
			Pageable pageable = PageRequest.of(pageNumber - 1, Constants.PAGE_SIZE, sortInfo);
			Page<ProductEntity> productEntitiesPage = productDao.findAll(ProductJpaSpecification.getSearchCriteria(searchConditions), pageable);
			responseMap.put("productList", productEntitiesPage.getContent());
			responseMap.put("paginationInfo", new PagerModel(pageNumber, productEntitiesPage.getTotalPages()));
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = e.getMessage();
			LOGGER.error("Error when get all brand: ", e);
		}
		return new ResponseDataModel(responseCode, responseMsg, responseMap);
	}

	@Override
	public ResponseDataModel findAllWithPagerApi(int pageNumber) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		Map<String, Object> responseMap = new HashMap<>();
		try {
			Sort sortInfo = Sort.by(Sort.Direction.DESC, "productId");
			Pageable pageable = PageRequest.of(pageNumber - 1, Constants.PAGE_SIZE, sortInfo);
			Page<ProductEntity> productEntityPages = productDao.findAll(pageable);
			responseMap.put("productList", productEntityPages.getContent());
			responseMap.put("paginationInfo", new PagerModel(pageNumber, productEntityPages.getTotalPages()));
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = e.getMessage();
			LOGGER.error("Error when get all product: ", e);
		}
		return new ResponseDataModel(responseCode, responseMsg, responseMap);
	}

	@Override
	public ResponseDataModel addProductApi(ProductEntity productEntity) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		try {
			if(findByProductName(productEntity.getProductName()) != null ) {
				responseMsg = "Product Name is duplicated";
				responseCode = Constants.RESULT_CD_DUPL;
			}else {
				MultipartFile[] imageFiles = productEntity.getImageFiles();
				if (imageFiles != null && imageFiles[0].getSize() > 0) {
					String imagePath = FileHelper.addNewFile(productLogoFolderPath, imageFiles);
					productEntity.setImage(imagePath);
				}
				productDao.saveAndFlush(productEntity);
				responseMsg = "Product is added successfully";
				responseCode = Constants.RESULT_CD_SUCCESS;
			}
		} catch (Exception e) {
			responseMsg = "Error when adding product";
			LOGGER.error("Error when adding product: ", e);		}
		return new ResponseDataModel(responseCode, responseMsg);
	}

	@Override
	public ProductEntity findByProductName(String productName) {
		return productDao.findByProductName(productName);
	}

	@Override
	public ResponseDataModel findByProductId(Long productId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		ProductEntity productEntity = null ;
		try {
			productEntity = productDao.findByProductId(productId);
			if(productEntity != null) {
				responseCode = Constants.RESULT_CD_SUCCESS;
			}
		} catch (Exception e) {
			responseMsg = "Error when finding product by ID";
			LOGGER.error("Error when finding product by ID: ", e);
		}
		return new ResponseDataModel(responseCode, responseMsg, productEntity);
	}

	@Override
	public ResponseDataModel updateProductApi(ProductEntity productEntity) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		try {
			ProductEntity duplicatedProduct = findByProductNameAndProductIdNot(productEntity.getProductName(), productEntity.getProductId());
			if(duplicatedProduct != null ) {
				responseMsg = "Product Name is duplicated";
				responseCode = Constants.RESULT_CD_DUPL;
			}else {
				MultipartFile[] imageFiles = productEntity.getImageFiles();
				if (imageFiles != null && imageFiles[0].getSize() > 0) {
					String imagePath = FileHelper.addNewFile(productLogoFolderPath, imageFiles);
					productEntity.setImage(imagePath);
				}
				productDao.saveAndFlush(productEntity);
				responseMsg = "Product is updating successfully";
				responseCode = Constants.RESULT_CD_SUCCESS;
			}
		} catch (Exception e) {
			responseMsg = "Error when updating product";
			LOGGER.error("Error when updating product: ", e);		
			}
		return new ResponseDataModel(responseCode, responseMsg);
	}

	@Override
	public ProductEntity findByProductNameAndProductIdNot(String productName, Long productId) {
		return productDao.findByProductNameAndProductIdNot(productName, productId);
	}

	@Override
	public ResponseDataModel deleteById(Long id) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = StringUtils.EMPTY;
		ProductEntity productEntity = productDao.findByProductId(id);
		try {
			if(productEntity != null) {
				productDao.deleteById(id);
				productDao.flush();
				FileHelper.deleteFile(productEntity.getImage());
				responseCode = Constants.RESULT_CD_SUCCESS;
				responseMsg = "Product is deleted successfully";
			}
			
		} catch (Exception e) {
			responseMsg = "Error when deleting product";
			LOGGER.error("Error when deleting product: ", e);
		}
		
		return new ResponseDataModel(responseCode, responseMsg);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}