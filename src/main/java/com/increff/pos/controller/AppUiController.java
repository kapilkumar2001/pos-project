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

	@RequestMapping(value = "/ui/brands-report")
	public ModelAndView BrandsReport() {
		return mav("brands-report.html");
	}

	@RequestMapping(value = "/ui/inventory-report")
	public ModelAndView InventoryReport() {
		return mav("inventory-report.html");
	}

	@RequestMapping(value = "/ui/pos-day-sales-report")
	public ModelAndView PosDaySalesReport() {
		return mav("pos-day-sales-report.html");
	}

	@RequestMapping(value = "/ui/sales-report")
	public ModelAndView SalesReport() {
		return mav("sales-report.html");
	}

}
