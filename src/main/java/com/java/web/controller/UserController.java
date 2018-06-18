package com.java.web.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.util.HttpUtil;
import com.java.util.PathUtil;
import com.java.web.dao.DaoInterFace;

@Controller
public class UserController {
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@Autowired
	DaoInterFace dI;
	
	@RequestMapping("/userInsert")
	public String userInsert(HttpServletRequest req) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);
		
//		getParamMap을 통해 아래 작업을 한 줄로 처리.
//		String userEmail    = req.getParameter("userEmail");
//		String userPassword = req.getParameter("userPassword");
//		String userName     = req.getParameter("userName");
//		HashMap<String, Object> param = new HashMap<String, Object>();
//		param.put("userEmail", userEmail);
//		param.put("userPassword", userPassword);
//		param.put("userName", userName);
		
		param.put("sqlType", "user.userinsert");
		param.put("sql","insert");
		
		int status = (int) dI.call(param);
//		int status = sess.insert("user.userinsert", param);

		if (status == 1) {
			System.out.println("성공적인 회원가입.");
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/userSelect")
	public String userSelect(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);
		param.put("sqlType", "user.userSelect");
		param.put("sql","selectOne");
		
		HashMap<String, Object> resultMap = (HashMap<String, Object>) dI.call(param);
		System.out.println(resultMap);
		
		if (resultMap == null) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("status", PathUtil.NO);
		} else {
			resultMap.put("status", PathUtil.OK);
		}
		
		session.setAttribute("user", resultMap);
//		attr.addFlashAttribute("JSTLdata", resultMap); 
//		session을 쓰기 시작하면서 필요없어짐.
		
		return "redirect:/";
	}
	
}
