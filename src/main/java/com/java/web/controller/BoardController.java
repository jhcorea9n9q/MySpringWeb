package com.java.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.util.HttpUtil;
import com.java.util.PathUtil;

import net.sf.json.JSONArray;

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

		if (status == 1) {
			System.out.println("성공적인 회원가입.");
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/userSelect")
	public String userSelect(HttpServletRequest req, RedirectAttributes attr) {
		String userEmail    = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);
		
		HashMap<String, Object> resultMap = sess.selectOne("user.userSelect", param);
		System.out.println(resultMap);
		
		if (resultMap == null) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("status", PathUtil.NO);
		} else {
			resultMap.put("status", PathUtil.OK);
		}
		
		attr.addFlashAttribute("JSTLdata", resultMap);
		
		return "redirect:/";
	}
	
	@RequestMapping("/bInsert")
	public String binsert() {
		System.out.println(PathUtil.FILE_DNS);
		return "board/insert";
	}
	
	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// return에서 사용할 용도
		
		String boardTitle = req.getParameter("boardTitle");
		String boardContents = req.getParameter("boardContents");
		String data = req.getParameter("data");
		// ajax쪽에서 String 형태로 배열이 담겨왔다.
		
		System.out.println("1. 넘어온 게시글 제목과 내용 : " + boardTitle + "," + boardContents);
		System.out.println("2. 넘어온 파일 데이터 : " + data);
		System.out.println("3. 파일 데이터 속성 : " + data.getClass().getName());
		
		// 우선 board에 insert하기
		/******************************************************************/
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContents);
		params.put("userNo", 1);
		
		int status = sess.insert("board.boardinsert", params);
		/******************************************************************/
		
		// insert가 잘 되었을 때만 file업로드가 되게 해야한다.(status 활용)
		if(status==1) {
			int boardNo = sess.selectOne("board.getBoardNo");
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
				
				Map<String,Object> fileMap = new HashMap<String, Object>();
				fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
				fileMap.put("userNo", 1);
				
				status = sess.insert("board.fileinsert", fileMap);
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
		
		return HttpUtil.makeJsonView(map);
	}
	
	@RequestMapping("/bSelect")
	public String bselect() {
		return "board/detail";
	}
	
	@RequestMapping("/bld")
	public ModelAndView bld(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("boardData", sess.selectOne("board.boardOne", param));
		resultMap.put("filesData", sess.selectList("board.filesList", param));
		
		return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/bList")
	public String bList() {
		return "board/list";
	}
	
	@RequestMapping("/bbld")
	public ModelAndView bbld() {
		List list = sess.selectList("board.boardList");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/bUpdate")
	public String bU() {
		return "board/update";
	}
	
	@RequestMapping("/bud")
	public ModelAndView bud(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int boardNo =  Integer.parseInt(req.getParameter("boardNo"));
		String boardTitle = req.getParameter("boardTitle");
		String boardContents = req.getParameter("boardContents");
		String data = req.getParameter("data");
		String delData = req.getParameter("delData");
		
		System.out.println(boardNo + "," + boardTitle + "," + boardContents);
		System.out.println(delData);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("boardNo", boardNo);
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContents);
		
		int status = sess.update("board.boardupdate", params);

		if(status==1) {
			List<Map<String,Object>> delList = JSONArray.fromObject(delData);
			for(int i = 0; i < delList.size(); i++) {
				sess.update("board.filesDel", delList.get(i));
			}
			
			List<Map<String,Object>> datalist = JSONArray.fromObject(data);
			for(int i = 0; i < datalist.size(); i++) {
				String fileName = datalist.get(i).get("fileName").toString();
				String fileURL = datalist.get(i).get("fileURL").toString();
				
				Map<String,Object> fileMap = new HashMap<String, Object>();
				fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
				fileMap.put("userNo", 1);
				
				status = sess.insert("board.fileinsert", fileMap);
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
		
		return HttpUtil.makeJsonView(map);
	}
}
