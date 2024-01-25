package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/profile")
	public String profile(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");

		// 出品履歴
		List<Map<String, Object>> listingHistory = jdbcTemplate.queryForList(
				"SELECT product_name, price, date_listed, item_number, " +
						"CASE WHEN buyer_id IS NOT NULL THEN '売却済み' ELSE '未売却' END AS transaction_status "
						+
						"FROM merchandise WHERE user_id = ?",
				userId);

		// 購入履歴
		List<Map<String, Object>> purchaseHistory = jdbcTemplate.queryForList(
				"SELECT product_name, price, date_purchased FROM merchandise WHERE buyer_id = ?", userId);

		model.addAttribute("listingHistory", listingHistory);
		model.addAttribute("purchaseHistory", purchaseHistory);

		return "profile";
	}

	@RequestMapping(path = "/update_name", method = RequestMethod.POST)
	public String update_name(@RequestParam("newName") String newName, HttpSession session) {
		int id = (int) session.getAttribute("id");
		jdbcTemplate.update("UPDATE member SET name = ? WHERE id = ?", newName, id);

		// Manually update the session attribute with the new name
		session.setAttribute("name", newName);

		return "redirect:/home";
	}

	// ログアウト用のPOSTメソッドを処理
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		// セッションの無効化
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		// ここに認証情報のクリアなど、ログアウトのロジックを実装
		// 例: Spring Securityを使用している場合は、セキュリティコンテキストから認証情報をクリアする

		return "redirect:/mylogin"; // ログアウト後にリダイレクトする先のパス
	}

}
