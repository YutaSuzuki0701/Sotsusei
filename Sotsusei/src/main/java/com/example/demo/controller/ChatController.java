package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/chat/{item_number}")
	public String openChatRoom(@PathVariable("item_number") String itemNumber, Model model) {
		model.addAttribute("item_number", itemNumber);
		return "chat";
	}

	@PostMapping("/chat/{item_number}/send")
	@ResponseBody
	public String sendMessage(@PathVariable("item_number") int itemNumber,
			@RequestParam String message,
			HttpSession session) {
		saveMessageToDatabase(itemNumber, message, session);
		return "メッセージは正常に送信されました!";
	}

	private void saveMessageToDatabase(int itemNumber, String message, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		String sql = "INSERT INTO chat (message, item_number, user_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, message, itemNumber, userId);
	}
}
