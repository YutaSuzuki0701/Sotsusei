package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ListingController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(path = "listing", method = RequestMethod.GET)
	public String listing() {
		return "listing";
	}

	@RequestMapping(path = "/determinePrice", method = RequestMethod.GET)
	@ResponseBody
	public String determinePrice(@RequestParam int enteredPrice) {
		// TODO: Call AI service or logic to determine price based on enteredPrice
		// For demonstration purposes, generate a random multiplier between 1 and 3
		double randomMultiplier =  new Random().nextDouble() * 0.9;

		int determinedPrice = (int) (enteredPrice * randomMultiplier);

		return String.valueOf(determinedPrice);
	}

	//出品本機能↓↓↓↓↓↓↓↓
	@RequestMapping(path = "/listing", method = RequestMethod.POST)
	public String listing(String productName, String category, int price, String condition, String description,
			@RequestParam("image") MultipartFile image, Model model, HttpSession session) throws IOException {
		if (!image.isEmpty()) {
			// 画像が選択されている場合のみ処理を行う

			// 画像データをバイト配列に変換
			byte[] imageData = image.getBytes();

			double condition_level;
			switch (condition) {
			case "新品未使用":
				condition_level = 0.9;
				break;
			case "未使用に近い":
				condition_level = 0.8;
				break;
			case "目立った傷や汚れなし":
				condition_level = 0.7;
				break;
			case "やや傷や汚れあり":
				condition_level = 0.6;
				break;
			case "傷や汚れあり":
				condition_level = 0.5;
				break;
			default:
				condition_level = 0.4;
			}
			// Base64エンコード
			String encodedImage = Base64.getEncoder().encodeToString(imageData);

			// 現在日時情報で初期化されたインスタンスの生成
			Date dateObj = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// 日時情報を指定フォーマットの文字列で取得
			String display = format.format(dateObj);

			// モデルにエンコードされた画像データを追加
			model.addAttribute("encodedImage", encodedImage);

			int userId = (int) session.getAttribute("id");

			// データベースに画像を保存
			jdbcTemplate.update(
					"INSERT INTO merchandise (product_name,category,price,product_condition,product_condition_level,description,user_id,date_listed,photograph) VALUES (?,?,?,?,?,?,?,?,?)",
					productName, category, price, condition, condition_level, description, userId, display,
					encodedImage);

		}

		return "redirect:/home";
	}

}
