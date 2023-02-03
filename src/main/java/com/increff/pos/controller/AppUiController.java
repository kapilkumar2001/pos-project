package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/brands")
	public ModelAndView brandcategory() {
		return mav("brands.html");
	}

	@RequestMapping(value = "/ui/products")
	public ModelAndView product() {
		return mav("products.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("orders.html");
	}

	@RequestMapping(value = "/ui/reports")
	public ModelAndView Reports() {
		return mav("reports.html");
	}
}
