package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyLoginController {
	//DBへつなぐために必要
	@Autowired
	JdbcTemplate jdbcTemplate;

	//最初のページ
	@RequestMapping(path = "/mylogin", method = RequestMethod.GET)
	public String kadaiLogin() {
		return "mylogin";
	}

	@RequestMapping(path = "/kaiin", method = RequestMethod.GET)
	public String kaiin() {
		return "/kaiin";
	}

	@RequestMapping(path = "/ng", method = RequestMethod.GET)
	public String kadaiNg() {
		return "ng";
	}




	@RequestMapping(path = "/loginfailed", method = RequestMethod.POST)
	public String kadaiRetry() {
		return "redirect:/mylogin";
	}

	@RequestMapping(path = "/kaiin", method = RequestMethod.POST)
	public String kaiin(String pass, String id, String name) {

		jdbcTemplate.update("INSERT INTO member (password,id,name) VALUES(?,?,?);", pass,
				Integer.parseInt(id), name);
		return "mylogin";

	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String login( String pass,int id, String name, HttpSession session) {

		List<Map<String, Object>> resultList = jdbcTemplate
				.queryForList("SELECT * FROM member WHERE password = ? and id = ? ", pass, id);
		if (!CollectionUtils.isEmpty(resultList)) {

			name = (String) resultList.get(0).get("name");
			session.setAttribute("name", name);

			id = (int) resultList.get(0).get("id");
			session.setAttribute("id", id);

			return "redirect:/home";
		} else
			return "redirect:/ng";
	}


}