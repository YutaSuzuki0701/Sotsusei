package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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

	@RequestMapping(path = "/purchase", method = RequestMethod.POST)
	public String purchase(Model model, int item_number) {
		Date dateObj = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String display = format.format(dateObj);
		jdbcTemplate.update("UPDATE merchandise SET date_purchased = ? WHERE item_number = ?", display, item_number);

		return "redirect:/procedure_completed";
	}

	@RequestMapping(path = "/procedure_completed", method = RequestMethod.POST)
	public String kadaiRetry() {
		return "redirect:/home";
	}
}
