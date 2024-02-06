package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ListingController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private  PythonController pythonController;

	public Map<String, Integer> determinePrice(@RequestBody Map<String, String> requestData) {
//	    int enteredPrice = Integer.parseInt(requestData.get("enteredPrice"));
//	    String enteredCondition = requestData.get("enteredCondition");
//	    double conditionLevel = determineConditionLevel(enteredCondition);
//
//	    // DjangoのAPIエンドポイント
//	    String djangoApiUrl = "http://localhost:8000/api/AI_Determine";
//
//	    // 新しいMapにenteredPriceを追加
//	    Map<String, Object> newRequestData = new HashMap<>(requestData);
//	    newRequestData.put("enteredPrice", enteredPrice);
//	    newRequestData.put("conditionLevel", conditionLevel);
//
//	    // DjangoのAPIにPOSTリクエストを送信
//	    ResponseEntity<Map> responseEntity = restTemplate.postForEntity(djangoApiUrl, newRequestData, Map.class);

	    // レスポンスの取得
	    Map<String, Integer> responseData = pythonController.callDjangoApi(requestData);

	    // AIモデルの予測結果を取得
	    int aiDeterminedPrice = responseData.get("ai_determined_price");

	    Map<String, Integer> finalResponseData = new HashMap<>();
	    finalResponseData.put("ai_determined_price", aiDeterminedPrice);

	    return finalResponseData;
	}




	@RequestMapping(path = "listing", method = RequestMethod.GET)
	public String listing() {
		return "listing";
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
            condition_level = determineConditionLevel(condition);
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
    // 商品状態判定メソッド
    private double determineConditionLevel(String condition) {
        double conditionLevel = 0.4; // デフォルト値

        switch (condition) {
            case "新品未使用":
                conditionLevel = 0.9;
                break;
            case "未使用に近い":
                conditionLevel = 0.8;
                break;
            case "目立った傷や汚れなし":
                conditionLevel = 0.7;
                break;
            case "やや傷や汚れあり":
                conditionLevel = 0.6;
                break;
            case "傷や汚れあり":
                conditionLevel = 0.5;
                break;
            // 他の条件にも対応する場合は追加
        }

        return conditionLevel;
    }

}
