package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map; // Correct import statement

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EditController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/edit/{itemNumber}")
	public String openEditPage(@PathVariable("itemNumber") String itemNumber, Model model) {
		// Retrieve the product information including the photograph based on the item number
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(
				"SELECT product_name, category, price, product_condition, description, photograph " +
						"FROM merchandise WHERE item_number = ?",
				itemNumber);

		List<String> categories = Arrays.asList("レディース", "メンズ", "ベビー・キッズ", "インテリア・住まい・小物", "本・音楽・ゲーム", "おもちゃ・ホビー・グッズ",
				"コスメ・香水・美容", "家電・スマホ・カメラ", "スポーツ・レジャー", "ハンドメイド", "チケット", "自動車・オートバイ", "その他");

		List<String> productConditions = Arrays.asList("新品、未使用", "未使用に近い", "目立った傷や汚れなし", "やや傷や汚れあり", "傷や汚れあり",
				"全体的に状態が悪い");

		if (!resultList.isEmpty()) {
			Map<String, Object> product = resultList.get(0);
			model.addAttribute("product", product);
		}

		model.addAttribute("item_number", itemNumber);
		model.addAttribute("categories", categories);
		model.addAttribute("productConditions", productConditions);

		return "edit";
	}

	@PostMapping("/update/{itemNumber}")
	public String updateProduct(@PathVariable("itemNumber") String itemNumber,
			@RequestParam("productName") String productName,
			@RequestParam("category") String category,
			@RequestParam("price") double price,
			@RequestParam("productCondition") String productCondition,
			@RequestParam("description") String description,
			@RequestParam(value = "photograph", required = false) MultipartFile photograph) {

		String sql = "UPDATE merchandise SET product_name = ?, category = ?, price = ?, " +
				"product_condition = ?, description = ?, photograph = ? WHERE item_number = ?";


		jdbcTemplate.update(sql, productName, category, price, productCondition, description, photograph,
				itemNumber);

		return "redirect:/update_completed";
	}

	@GetMapping("/update_completed")
	public String updateCompleted(Model model, HttpSession session) {
		// Retrieve itemNumber from the session (assuming it's set in a previous request)
		String itemNumber = (String) session.getAttribute("itemNumber");

		// Fetch updated data from the database based on itemNumber
		List<Map<String, Object>> updatedResults = jdbcTemplate.queryForList(
				"SELECT * FROM merchandise WHERE item_number = ? AND date_purchased IS NULL",
				itemNumber);

		// Add the updated data to the model
		model.addAttribute("selectResult", updatedResults);

		return "update_completed";
	}

}
