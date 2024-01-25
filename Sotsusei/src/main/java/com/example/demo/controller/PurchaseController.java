package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PurchaseController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(path = "purchase", method = RequestMethod.GET)
	public String purchase() {
		return "purchase";
	}

	@RequestMapping(path = "/procedure_completed", method = RequestMethod.GET)
	public String kadaiNg() {
		return "procedure_completed";
	}
	@RequestMapping(path = "/procedure_failed", method = RequestMethod.GET)
	public String failed() {
		return "procedure_failed";
	}

	@RequestMapping(path = "/purchase", method = RequestMethod.POST)
	public String purchase(Model model, int item_number, HttpSession session) {
		Date dateObj = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String display = format.format(dateObj);
		int id = (int) session.getAttribute("id");

		List<Map<String, Object>> resultList;

		resultList = jdbcTemplate.queryForList("select * from merchandise WHERE item_number  = ?", item_number);

		if (id != (int) resultList.get(0).get("user_id")) {

			jdbcTemplate.update("UPDATE merchandise SET date_purchased = ? , buyer_id = ? WHERE item_number = ?",
					display, id, item_number);

			return "redirect:/procedure_completed";
		} else {
			return "redirect:/procedure_failed";
		}
	}

	@RequestMapping(path = "/back_home", method = RequestMethod.POST)
	public String backhome() {
		return "redirect:/home";
	}


}
