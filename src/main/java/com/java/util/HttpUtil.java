package com.java.util;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class HttpUtil {
	
	public static HashMap<String, Object> getParamMap(HttpServletRequest req){
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	Enumeration<?> enums = req.getParameterNames();
    	while(enums.hasMoreElements()) {
    		String paramName = enums.nextElement().toString();
    		
    		if("".equals(req.getParameter(paramName))) {
    			result = null;
    			break;
    		}
    		    		
    		result.put(paramName, req.getParameter(paramName));
    	}
    	
    	return result;
    }
	
	public static ModelAndView makeJsonView(HashMap<String, Object> map) {
		ModelAndView mav = new ModelAndView();
		
		JSONObject j = new JSONObject();
		j = JSONObject.fromObject(JSONSerializer.toJSON(map));
		
		mav.addObject("json", j);
		mav.setViewName("json");
		
		return mav;
	}

	public static void makeJsonWriter(HttpServletResponse res, HashMap<String, Object> map) {
		
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=utf-8");
		
		JSONObject j = new JSONObject();
		j = JSONObject.fromObject(JSONSerializer.toJSON(map));
		
		try {
			res.getWriter().write(j.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 로그인을 체크하고 로그인 할때만 데이터베이스 접근하게 하기.
	public static boolean loginCheck(HttpSession session) {
		
		if(session == null) {
			System.out.println("세션 정보가 없습니다.");
			return false;
		}else {
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
			if(userMap == null) {
				System.out.println("현재 로그인 되어있지 않습니다.");
				return false;
			}else {
				System.out.println("****************************************************************");
				for (String data : userMap.keySet()) {
					System.out.println(data + ":" + userMap.get(data));
				}
				System.out.println("****************************************************************");
				return true;
			}
		}
	}

}
