package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	// 新しく追加されたメソッド
	@RequestMapping(path = "/product/details/{item_number}", method = RequestMethod.GET)
	public String productDetails(Model model, @PathVariable("item_number") int item_number) {
		// 商品詳細を取得する処理を追加する（適切なメソッドを使用してデータベースから商品詳細を取得）
		// 以下はダミーコードで、実際の処理に合わせて変更が必要
		Map<String, Object> productDetails = jdbcTemplate.queryForMap("select * from merchandise where item_number = ?",
				item_number);
		model.addAttribute("productDetails", productDetails);
		return "product-details";
	}

	@RequestMapping(path = "/product/purchase/{item_number}", method = RequestMethod.GET)
	public String purchase(Model model, @PathVariable("item_number") int item_number) {

		Map<String, Object> product = jdbcTemplate.queryForMap(
				"select product_name,price from merchandise where item_number = ?",
				item_number);
		model.addAttribute("product", product);
		return "product";
	}

}
