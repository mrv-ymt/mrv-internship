$(document).ready(function() {
	findAllProducts(1);


	/*Pagination*/
	$('.pagination').on('click', '.page-link', function() {
		var pagerNumber = $(this).attr("data-index");
		var formData = new FormData($searchForm[0]);
		if (formData == null) {
			findAllProducts(pagerNumber);
		} else {
			searchApi(formData, pagerNumber);
		}
	})

	$('.search-component button').on('click', function() {
		event.defaultPrevented;
		var formData = new FormData($searchForm[0]);
		formData.set("priceFrom" ,formData.get("priceFrom").replaceAll("." ,""));
		formData.set("priceTo" ,formData.get("priceTo").replaceAll(".",""));
		var pagerNumber = 1;
		searchApi(formData, pagerNumber);
		return false;
	});
	
	

	var $productInfoForm = $('#productInfoForm');
	var $productInfoModal = $('#productInfoModal');
	var $searchForm = $('#searchForm');
	/*	Show add product info (product Form)*/
	$('#addProductInfoModal').on('click', function() {
		resetFormModal($productInfoForm);
		showModalWithCustomizedTitle($productInfoModal, "Add Product");
		$('#productId').closest(".form-group").addClass("d-none");
		$('#logoImg img').attr('src', '/images/image-demo.png');
		showBrandNameInForm();
	});

	/*Show Edit Form Info*/
	$('#productInfoTable').on('click', '.edit-btn', function() {
		/*d-none */
		$("#image .required-mask").addClass("d-none");

		$.ajax({
			url: "/product/api?id=" + $(this).data("id"),
			type: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success: function(responseData) {
				if (responseData.responseCode == 100) {
					var productInfo = responseData.data;
					resetFormModal($productInfoForm);
					showModalWithCustomizedTitle($productInfoModal, "Edit Product");
					$('#productId').val(productInfo.productId);
					$('#productName').val(productInfo.productName);
					showBrandNameInForm(productInfo.brandEntity.brandId);
					$('#quantity').val(productInfo.quantity);
					$('#price').val(convertMoney(productInfo.price));
					$('#saleDate').val(productInfo.saleDate);
					var productImage = productInfo.image;
					if (productImage == null || productImage == "") {
						brandLogo = "/images/image-demo.png";
					}
					$("#logoImg img").attr("src", productImage);
					$('input.old-img').val(productImage);
					$('#productId').closest(".form-group").removeClass("d-none");
					$('#description').val(productInfo.description);
				}
			}

		});


	});



	$('#saveProductBtn').on('click', function(event) {
		event.preventDefault();
		if(document.getElementById("price").value == ""){
			$('#price').val("");
		}else {
			$('#price').val(cvStringToInt(document.getElementById("price").value));
		}
		var formData = new FormData($productInfoForm[0]);
		console.log(typeof  formData.get("price"));
		var productId = formData.get("productId");
		var isAddAction = productId == undefined || productId == "";

		$.validator.addMethod("invalidDate", function(input) {
			if (!/([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/.test(input))
				return false;
			else {
				return true;
			}
		}, "Please Input Correct Format Date");

		$productInfoForm.validate({
			ignore: [],
			rules: {
				productName: {
					required: true,
					maxlength: 100,
				},
				quantity: {
					required: true,
					digits: true,
					min: 1,
					max: 10000,
				},
				price: {
					number: true,
					required: true,
					min: 1000,
				},
				saleDate: {
					required: true,
					invalidDate: true
				},
				imageFiles: {
					required: isAddAction,
				},
			},
			messages: {
				productName: {
					required: "Please input Product Name",
					maxlength: "The Brand Name must be less than 100 characters",
				},
				quantity: {
					required: "Please input Product Quantity",
					digits: "Please input Product Quantity number",
					min: "Min Quantity is 1",
					max: "Max Quantity is 10000",
				},
				price: {
					required: "Please input Product Price",
					min: "Min Price is 1000",
				},
				saleDate: {
					required: "Please input Sale Date",
					date: "Please Input Correc Format dd/mm/yyyy",
				},
				imageFiles: {
					required: "Please upload Product Image",
				},
			},
			errorElement: "div",
			errorClass: "error-message-invalid"
		});
		if ($productInfoForm.valid()) {
			// POST data to server-side by AJAX
			$.ajax({
				url: "/product/api/" + (isAddAction ? "" : productId),
				type: 'POST',
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				cache: false,
				timeout: 10000,
				data: formData,
				success: function(responseData) {

					// Hide modal and show success message when save successfully
					// Else show error message in modal
					if (responseData.responseCode == 100) {
						$productInfoModal.modal('hide');
						findAllProducts(1);
						showNotification(true, responseData.responseMsg);
					} else {
						showMsgOnField($productInfoForm.find("#productName"), responseData.responseMsg);
					}
				}
			});
		}
	});
	// Show delete product confirmation modal
	$("#productInfoTable").on('click', '.delete-btn', function() {
		$("#deletedProductName").text($(this).data("name"));
		$("#deleteSubmitBtn").attr("data-id", $(this).data("id"));
		$('#confirmDeleteModal').modal('show');
	});

	// Submit delete brand
	$("#deleteSubmitBtn").on('click', function() {
		$.ajax({
			url: "/product/api/" + $(this).attr("data-id"),
			type: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success: function(responseData) {
				$('#confirmDeleteModal').modal('hide');
				showNotification(responseData.responseCode == 100, responseData.responseMsg);
				findAllProducts(1);
			}
		});
	});

});
/*Show showBrandNameInForm*/

function searchApi(formData, pagerNumber) {
	$.ajax({
		url: "/product/api/searchApi/" + pagerNumber,
		type: 'POST',
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		cache: false,
		timeout: 10000,
		data: formData,
		success: function(responseData) {
			renderBrandsTable(responseData.data.productList);
			renderPagination(responseData.data.paginationInfo);
		}
	});
}


function showBrandNameInForm(brandId) {
	$.ajax({
		url: "/brand/api/findAllBrandApi",
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(responseData) {
			if (responseData.responseCode == 100) {
				var listBrand = responseData.data;
				showBrandName(listBrand, brandId);
			}
		}
	})
}





function showBrandName(listBrand, brandId) {
	var optionHtml = "";
	$("#brand-list").empty();
	$.each(listBrand, function(i, v) {
		if (brandId == v.brandId) {
			optionHtml = "<option value = " + v.brandId + " selected> " + v.brandName + "</option> ";
		} else {
			optionHtml = "<option value = " + v.brandId + "> " + v.brandName + "</option> ";
		}
		$("#brand-list").append(optionHtml);
	});
}



function findAllProducts(pagerNumber) {
	$.ajax({
		url: "/product/api/" + pagerNumber,
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(responseData) {
			if (responseData.responseCode == 100) {
				renderBrandsTable(responseData.data.productList);
				renderPagination(responseData.data.paginationInfo);
			}
		}
	});
}

function renderBrandsTable(productList) {
	var rowHtml = "";
	$("#productInfoTable tbody").empty();
	if(Object.keys(productList).length === 0) {
		showNotification(false, "Cannot finding with this key !!!");
	}else{
	$.each(productList, function(key, value) {
		rowHtml = "<tr>"
			+ "<td>" + value.productId + "</td>"
			+ "<td>" + value.productName + "</td>"
			+ "<td>" + value.quantity + "</td>"
			+ "<td>" + convertMoney(value.price) + " VNĐ</td>"
			+ "<td>" + value.brandEntity.brandName + "</td>"
			+ "<td>" + value.saleDate + "</td>"
			+ "<td class='text-center'><a href='" + value.image + "' data-toggle='lightbox' data-max-width='1000'><img class='img-fluid' src='" + value.image + "'></td>"
			+ "<td>" + value.description + "</td>"
			+ "<td class='action-btns'>"
			+ "<a class='edit-btn' data-id='" + value.productId + "'><i class='fas fa-edit'></i></a> | <a class='delete-btn' data-name='" + value.productName + "' data-id='" + value.productId + "'><i class='fas fa-trash-alt'></i></a>"
			+ "</td>"
			+ "</tr>";
		$("#productInfoTable tbody").append(rowHtml);
	});
	}
}

function renderPagination(paginationInfo) {

	var paginationInnerHtml = "";
	if (paginationInfo.pageNumberList.length > 0) {
		$("ul.pagination").empty();
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.firstPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationInfo.firstPage + '">First</a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.previousPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationInfo.previousPage + '"> < </a></li>'
		$.each(paginationInfo.pageNumberList, function(key, value) {
			paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (value == paginationInfo.currentPage ? 'active' : '') + '" href="javascript:void(0)" data-index="' + value + '">' + value + '</a></li>';
		});
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.nextPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationInfo.nextPage + '"> > </a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.lastPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationInfo.lastPage + '">Last</a></li>'
		$("ul.pagination").append(paginationInnerHtml);
	}
}