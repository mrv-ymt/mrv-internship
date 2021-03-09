package com.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.entity.BrandEntity;
import com.training.service.IBrandService;

@Controller
@RequestMapping(value="/brand")
public class BrandController {

	@Autowired
	IBrandService brandService;

	@GetMapping(value="/sample")
	public String initBrandPageSample(Model model) {

		List<BrandEntity> brandsList = brandService.findAllSample();
		model.addAttribute("brandsList", brandsList);
		return "brand-index";
	}
	
	@GetMapping
	public String initBrandPage(Model model) {

		List<BrandEntity> brandsList = brandService.findAll();
		model.addAttribute("brandsList", brandsList);
		return "brand-index";
	}
}
