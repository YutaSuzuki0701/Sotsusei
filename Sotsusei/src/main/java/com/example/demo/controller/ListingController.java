package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	//出品本機能↓↓↓↓↓↓↓↓
	@RequestMapping(path = "/listing", method = RequestMethod.POST)
	public String listing(String productName, String category, int price, String condition, String description,
			int userId, @RequestParam("image") MultipartFile image, Model model) throws IOException {
		if (!image.isEmpty()) {
			// 画像が選択されている場合のみ処理を行う

			// 画像データをバイト配列に変換
			byte[] imageData = image.getBytes();
			//double condition_level;
			//            switch
			//case"新品未使用":
			//	condition_level = 0.9;
			// Base64エンコード
			String encodedImage = Base64.getEncoder().encodeToString(imageData);

			// 現在日時情報で初期化されたインスタンスの生成
			Date dateObj = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// 日時情報を指定フォーマットの文字列で取得
			String display = format.format(dateObj);

			// モデルにエンコードされた画像データを追加
			model.addAttribute("encodedImage", encodedImage);

			// データベースに画像を保存
			jdbcTemplate.update(
					"INSERT INTO merchandise (product_name,category,price,product_condition,description,user_id,date_listed,photograph) VALUES (?,?,?,?,?,?,?,?)",
					productName, category, price, condition, description, userId, display, encodedImage);

		}

		return "redirect:/home";
	}
}
