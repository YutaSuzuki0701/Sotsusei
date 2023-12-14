package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	//最初のページ
	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public String home(Model model) {

		List<Map<String, Object>> resultList;

		resultList = jdbcTemplate.queryForList("select * from merchandise");

		model.addAttribute("selectResult", resultList);
		return "home";
	}

	@RequestMapping(path = "/profile", method = RequestMethod.GET)
	public String profile() {
		return "profile";
	}

	@RequestMapping(path = "/kounyu", method = RequestMethod.GET)
	public String kounyu() {
		return "kounyu";
	}

	@RequestMapping(path = "/osusume", method = RequestMethod.GET)
	public String osusume() {
		return "osusume";
	}

	@RequestMapping(path = "/ninki", method = RequestMethod.GET)
	public String ninki() {
		return "ninki";
	}

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public String all() {
		return "all";
	}

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

}