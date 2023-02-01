package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	// @RequestMapping(value = "/ui/brands")
	// public ModelAndView home() {
	// 	return mav("home.html");
	// }

//	@RequestMapping(value = "/ui/employee")
//	public ModelAndView employee() {
//		return mav("employee.html");
//	}

//	@RequestMapping(value = "/ui/admin")
//	public ModelAndView admin() {
//		return mav("user.html");
//	}

	@RequestMapping(value = "/ui/brands")
	public ModelAndView brandcategory() {
		return mav("brandcategory.html");
	}

	@RequestMapping(value = "/ui/products")
	public ModelAndView product() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/inventory-report")
	public ModelAndView inventoryReport() {
		return mav("inventory_report.html");
	}

	@RequestMapping(value = "/ui/brands-report")
	public ModelAndView brandReport() {
		return mav("brand_report.html");
	}

	@RequestMapping(value = "/ui/sales-report")
	public ModelAndView salesReport() {
		return mav("sales_report.html");
	}

	@RequestMapping(value = "/ui/posdaysales-report")
	public ModelAndView posDaySalesReport() {
		return mav("posdaysales_report.html");
	}

	@RequestMapping(value = "/ui/reports")
	public ModelAndView Reports() {
		return mav("reports.html");
	}
}
