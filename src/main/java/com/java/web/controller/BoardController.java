package com.java.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BoardController {
	
	@RequestMapping("/")
	public String main() {
		return "main";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	
	@Resource(name="sqlSession")
	SqlSession sess;
	
	@RequestMapping("/userInsert")
	public String userInsert(HttpServletRequest req) {
		String userEmail    = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		String userName     = req.getParameter("userName");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);
		param.put("userName", userName);
		
		int status = sess.insert("user.userinsert", param);
		System.out.println(status);
		
		if (status == 1) {
			System.out.println("성공적인 회원가입.");
		}
		
		return "main";
	}
	
}
