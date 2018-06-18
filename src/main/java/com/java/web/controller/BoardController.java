package com.java.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.util.HttpUtil;
import com.java.util.PathUtil;
import com.java.web.dao.DaoInterFace;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class BoardController {
	
	@Resource(name="sqlSession")
	SqlSession sess;
	
	@Autowired
	DaoInterFace dI;
	
	@RequestMapping("/bInsert")
	public String binsert(HttpSession session) {
		if(HttpUtil.loginCheck(session)) {
			return "board/insert";
		}else {
			return "redirect:/login";
		}
		
	}
	
	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// return에서 사용할 용도
		if (HttpUtil.loginCheck(session)) {
		
			String boardTitle = req.getParameter("boardTitle");
			String boardContents = req.getParameter("boardContents");
			String data = req.getParameter("data");
			// ajax쪽에서 String 형태로 배열이 담겨왔다.
			
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
			
			System.out.println("1. 넘어온 게시글 제목과 내용 : " + boardTitle + "," + boardContents);
			System.out.println("2. 넘어온 파일 데이터 : " + data);
			System.out.println("3. 파일 데이터 속성 : " + data.getClass().getName());
			
			// board에 insert하기
			/******************************************************************/
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("boardTitle", boardTitle);
			params.put("boardContents", boardContents);
			params.put("userNo", userMap.get("userNo"));
			params.put("sqlType", "board.boardinsert");
			params.put("sql", "insert");
			
			int status = (int) dI.call(params);
			
	//		int status = sess.insert("board.boardinsert", params);
			/******************************************************************/
			
			// insert가 잘 되었을 때만 file업로드가 되게 해야한다.(status 활용)
			if(status==1) {
				params = new HashMap<String, Object>();
				params.put("sqlType", "board.getBoardNo");
				params.put("sql", "selectOne");
				int boardNo = (int) dI.call(params);
				System.out.println("4. 새로 생성된 BoardNo(게시글번호) : " + boardNo);
				
				List<Map<String,Object>> dataList = JSONArray.fromObject(data);
				// 받아온 값을 JSONArray로 배열형태로 바꾸고 변수화.
				System.out.println("5. Array형태로 변환시킨 파일 데이터 dataList :" + dataList);
				System.out.println("6. dataList의 속성 :" + dataList.getClass().getName());
				System.out.println("7. 파일(데이터)인 dataList의 수 : " + dataList.size());
				
				for(int i = 0; i < dataList.size(); i++) {
					String fileName = dataList.get(i).get("fileName").toString();
					String fileURL = dataList.get(i).get("fileURL").toString();
									// List안에 Map이 담겨있기에 이런 짓이 가능하다.
						            // Object가 value값이므로 toString 해주어야한다.
						            // (만일 value쪽을 String으로 선언했으면 안해도됨)
					System.out.println("8-" + (i+1) + ". String으로 뽑아온 파일정보 : " +fileName + "," + fileURL);
					
					HashMap<String,Object> fileMap = new HashMap<String, Object>();
					fileMap.put("boardNo", boardNo);
					fileMap.put("fileName", fileName);
					fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
					fileMap.put("userNo", userMap.get("userNo"));
					fileMap.put("sqlType", "board.fileinsert");
					fileMap.put("sql", "insert");
					status = (int) dI.call(fileMap);
				}
				if (status == 1) {
					map.put("msg", "글 작성 완료");
					// 파일첨부가 필수가 아니기에 예외처리를 하지 않았음.
					map.put("status", PathUtil.OK);
					map.put("boardNo", boardNo); // view를 위해 보드넘버를 보낸다.
				} else {
					map.put("msg", "파일 첨부에 오류 발생.");
				}
				
			} else {
				map.put("msg", "글 작성에 오류 발생.");
			}
		
		} else {
			map.put("msg", "현재 로그인 되어있지 않습니다.");
		}
		
		return HttpUtil.makeJsonView(map);
	}
	
	@RequestMapping("/bSelect")
	public String bselect() {
		return "board/detail";
	}
	
	@RequestMapping("/bld")
	public ModelAndView bld(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		
		param.put("sqlType", "board.boardOne");
		param.put("sql", "selectOne");
		resultMap.put("boardData", dI.call(param));
		
		param.put("sqlType", "board.filesList");
		param.put("sql", "selectList");
		resultMap.put("filesData", dI.call(param));
		
		return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/bList")
	public String bList() {
		return "board/list";
	}
	
	@RequestMapping("/bbld")
	public ModelAndView bbld() {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("sqlType", "board.boardList");
		param.put("sql", "selectList");
		
		List list = (List) dI.call(param);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		
		return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/bUpdate")
	public String bU(HttpSession session, HttpServletRequest req, RedirectAttributes attr) {
		if(HttpUtil.loginCheck(session)) {
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
			HashMap<String, Object> paramMap = HttpUtil.getParamMap(req);
			String userNo1 = userMap.get("userNo").toString();
			
			paramMap.put("sqlType", "board.boardOne");
			paramMap.put("sql", "selectOne");
			
			HashMap<String, Object> resultMap = (HashMap<String, Object>) dI.call(paramMap);
			String userNo2 = resultMap.get("userNo").toString();
			
			if(userNo1.equals(userNo2)) {
				return "board/update";
			}else {
				attr.addFlashAttribute("userNo", userNo2);
				return "redirect:/bSelect";
			}
		}else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping("/bud")
	public ModelAndView bud(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (HttpUtil.loginCheck(session)) {
			
			int boardNo =  Integer.parseInt(req.getParameter("boardNo"));
			String boardTitle = req.getParameter("boardTitle");
			String boardContents = req.getParameter("boardContents");
			String data = req.getParameter("data");
			String delData = req.getParameter("delData");
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
			
			System.out.println(boardNo + "," + boardTitle + "," + boardContents);
			System.out.println(delData);
			
			/*******************************************************************/ 
			//update 하는 부분
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("boardNo", boardNo);
			params.put("boardTitle", boardTitle);
			params.put("boardContents", boardContents);
			
			params.put("sqlType", "board.boardupdate");
			params.put("sql", "update");
			
			int status = (int) dI.call(params);
			/*******************************************************************/ 
	
			if(status==1) {
				List<JSONObject> delList = JSONArray.fromObject(delData);
				System.out.println("delList : " + delList);
				for(int i = 0; i < delList.size(); i++) {
					JSONObject j = delList.get(i);
					params = new HashMap<String, Object>();
					
					params.put("fileNo", j.get("fileNo"));
					params.put("sqlType", "board.filesDel");
					params.put("sql", "update");
					dI.call(params);
				}
				
				List<Map<String,Object>> datalist = JSONArray.fromObject(data);
				for(int i = 0; i < datalist.size(); i++) {
					String fileName = datalist.get(i).get("fileName").toString();
					String fileURL = datalist.get(i).get("fileURL").toString();
					
					HashMap<String,Object> fileMap = new HashMap<String, Object>();
					fileMap.put("boardNo", boardNo);
					fileMap.put("fileName", fileName);
					fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
					fileMap.put("userNo", userMap.get("userNo"));
					
					fileMap.put("sqlType","board.fileinsert");
					fileMap.put("sql","insert");
					
					status = (int) dI.call(fileMap);
				}
				
				if (status == 1) {
					map.put("msg", "글 작성 완료");
					map.put("status", PathUtil.OK);
					map.put("boardNo", boardNo);
				} else {
					map.put("msg", "파일 첨부에 오류 발생.");
				}
				
			} else {
				map.put("msg", "글 작성에 오류 발생.");
			}
		
	   } else {
		   map.put("msg", "현재 로그인이 되어있지 않습니다.");
	   }
		
		return HttpUtil.makeJsonView(map);
	}
	
	@RequestMapping("/bDel")
	public String bDel(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		
		param.put("sqlType", "board.boardDel");
		param.put("sql", "update");
		int status1 = (int) dI.call(param);
		System.out.println(status1);
		
		param.put("sqlType", "board.filesBoardDel");
		param.put("sql", "update");
		int status2 = (int) dI.call(param);
		System.out.println(status2);
		
		return "redirect:/bList";
	}
	
	// 끝
}
