<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<title>Product Management</title>
<jsp:include page="../../common/head.jsp" />
<link rel="stylesheet" href="<c:url value='/css/brand.css'/>">

<link rel="stylesheet" href="<c:url value='/css/product.css'/>">


</head>
<body>
	<jsp:include page="../../common/header.jsp" />
	<div class="container">
		<div class="sub-header">
			<div class="float-left sub-title">Product Management</div>
		</div>
		<!-- Search -->
		<div class = "search-component"> 
		<form  id = "searchForm" enctype="multipart/form-data" role = "form" method = "POST" > 
			<input type="text" placeholder="Product Name , Brand Name" style="width: 250px;" name = "keyword">
			<div class="search-item">
				<label>Price From</label> 
				<input type="text" name = "priceFrom" id = "priceFrom" class="priceKey" 
					 onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 13' pattern= "[0-9]*" >
			</div>
			<div class="search-item">
				<label>Price To</label>
				 <input type="text" name = "priceTo" id = "priceTo" class ="priceKey"  
				 onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 13 ' pattern= "[0-9]*" >
			</div>
			<div class="float-right">
				<button class="btn btn-primary btn-block" type = "submit"  >Search</button>
			</div>
		</form>

		</div>
		<table class="table table-bordered" id="productInfoTable">
			<thead>
				<tr class="text-center">
					<th scope="col">#</th>
					<th scope="col">Product</th>
					<th scope="col">Quantity</th>
					<th scope="col">Price</th>
					<th scope="col">Brand Name</th>
					<th scope="col">Open For Sale</th>
					<th scope="col">Image</th>
					<th scope="col">Description</th>
					<th scope="col">Option</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<div class="float-left">
			<a class="btn btn-success add-btn" id="addProductInfoModal"> <i
				class="fas fa-plus-square"> </i> Add Product
			</a>
		</div>

		<div class="d-flex justify-content-center float-right">
			<ul class="pagination">
			</ul>
		</div>
	</div>
	
	<!--  Show add product and edit product   -->  
	
	<div class="modal fade" id="productInfoModal">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<form id="productInfoForm" role="form" enctype="multipart/form-data">
					<div class="modal-header">
						<h5 class="modal-title">Add Product</h5> 
						<!--Btn Close Form  -->
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group d-none">
							<label>Product ID </label>
							<input type="text" class="form-control" name="productId" id="productId" placeholder="Product ID" readonly>
						</div>
						<div class="form-group">
							<label for="productName">Product Name <span class="required-mask">(*)</span></label>
							<input type="text" class="form-control" id="productName" name="productName" placeholder="Product Name">
						</div>
						<div class="form-group">
							<label for="productName">Brand Name <span class="required-mask">(*)</span></label>
							<select	id = "brand-list" name = "brandEntity.brandId"> 
							</select>

						</div>
						<div class="form-group">
							<label for="quantity">Quantity <span class="required-mask">(*)</span></label>
							<input type="number" class="form-control" id="quantity" name="quantity"  min ="1">
						</div>
						<div class="form-group">
							<label for="price">Price <span class="required-mask">(*)</span></label>
							<input type="text" class="form-control" id="price" name="price" 
				 				onkeypress='return event.charCode >= 48 && event.charCode <= 57 ' pattern= "[0-9]*" >
						</div>
						<div class="form-group">
							<label for="saleDate">Open For Sale  <span class="required-mask">(*)</span></label>
							<input type="date" class="form-control" id="saleDate" name="saleDate" placeholder="Open For SaleDate">
						</div>
						
						
						<div class="form-group" id="image" >
							<label for="image">Product Image <span class="required-mask">(*)</span></label>
							<div class="preview-image-upload" id="logoImg">
								<img src="<c:url value='/images/image-demo.png'/>" alt="image">
							</div>
							<input type="file" class="form-control upload-image" name="imageFiles" accept="image/*" />
							<input type="hidden" class="old-img" id="image" name="image">
						</div>
						<div class="form-group">
							<label for="description">Description</label>
							<textarea name="description" id="description" cols="30" rows="3" class="form-control" placeholder="Description"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-primary" id="saveProductBtn">Save</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- Modal Confirm Deleting Brand -->
	<div class="modal fade" id="confirmDeleteModal" >
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Delete Brand</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>Do you want to delete <b id="deletedProductName"></b>?</p>
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary" id="deleteSubmitBtn">Save</button>
				</div>
			</div>
		</div>
	</div>
	

	<jsp:include page="../../common/footer.jsp" />
	<script src="<c:url value='/js/product.js'/>"></script>
</body>
</html>