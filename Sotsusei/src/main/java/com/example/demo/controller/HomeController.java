package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	//最初のページ
	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public String home(Model model) {

		List<Map<String, Object>> resultList;

		resultList = jdbcTemplate.queryForList("select * from merchandise WHERE date_purchased IS NULL");

		model.addAttribute("selectResult", resultList);
		return "home";
	}

	@GetMapping("/search")
	public String searchMerchandise(@RequestParam(name = "query", required = false) String query, Model model) {
		if (query == null) {
			query = "";
		}

		// データベースから検索
		String sql = "SELECT * FROM merchandise WHERE product_name LIKE ? AND date_purchased IS NULL";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, "%" + query + "%");

		// 検索結果をModelに追加
		model.addAttribute("selectResult", results);

		// セッションからユーザー名を取得
		// （仮に"username"としていますが、実際のセッション属性に合わせて変更してください）
		String username = (String) model.getAttribute("username");
		model.addAttribute("sessionName", username);

		return "home"; // ホームページに遷移
	}

	@GetMapping("/ladies")
	public String ladies(Model model) {
		// merchandiseテーブルからレディースの商品を取得
		String sql = "SELECT * FROM merchandise WHERE category = 'レディース' AND date_purchased IS NULL";
		List<Map<String, Object>> ladiesProducts = jdbcTemplate.queryForList(sql);

		// Modelにレディースの商品を追加
		model.addAttribute("selectResult", ladiesProducts);

		return "home";
	}

	@GetMapping("/mens")
	public String mens(Model model) {
		// merchandiseテーブルからメンズの商品を取得
		String sql = "SELECT * FROM merchandise WHERE category = 'メンズ' AND date_purchased IS NULL";
		List<Map<String, Object>> mensProducts = jdbcTemplate.queryForList(sql);

		// Modelにメンズの商品を追加
		model.addAttribute("selectResult", mensProducts);

		return "home";
	}

	@GetMapping("/all")
    public String showAll(Model model) {
        // merchandiseテーブルから全ての商品を取得
		String sql = "SELECT * FROM merchandise WHERE category = '本・音楽・ゲーム' AND date_purchased IS NULL";
        List<Map<String, Object>> allProducts = jdbcTemplate.queryForList(sql);

        // Modelに全ての商品を追加
        model.addAttribute("selectResult", allProducts);

        return "home";
    }
}