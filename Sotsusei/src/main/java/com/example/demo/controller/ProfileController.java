package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(path = "profile", method = RequestMethod.GET)
	public String profile() {
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
