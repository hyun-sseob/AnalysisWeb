package kr.co.shs.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import kr.co.shs.service.FileServiceInterface;
import kr.co.shs.util.HttpdUtil;
import net.sf.json.JSONArray;

@Controller
public class BoardController {

	@Resource(name = "sqlSession")
	SqlSession session;
	
	@Autowired
	FileServiceInterface fsi;
	
	@RequestMapping("/userList")
	public  ModelAndView userList() {
		
		List<HashMap<String, Object>> list = session.selectList("user.userList"); 
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/boardList")
	public ModelAndView boardList(HttpServletRequest req) {
		
		String boardNo = req.getParameter("boardNo");
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> filesAndboard = session.selectList("board.filesAndboard"); //boardNo를 넘겨주자 쿼리문에 맞게
		
		resultMap.put("filesAndboard", filesAndboard);
		resultMap.put("boardNo", boardNo);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/aboutList")
	public ModelAndView aboutList() {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		HashMap<String, Object> profile = session.selectOne("board.profileList");
		
		List<HashMap<String, Object>> resumeI = session.selectList("board.resumeI");  //selecList() 메소드의 타입은 List이다.
		List<HashMap<String, Object>> resumeR = session.selectList("board.resumeR");
		List<HashMap<String, Object>> resumeS = session.selectList("board.resumeS");
		
		resultMap.put("resumeI", resumeI);
		resultMap.put("resumeR", resumeR);
		resultMap.put("resumeS", resumeS);
		resultMap.put("profile", profile);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/msgList")
	public ModelAndView msgList() {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> msgList = session.selectList("msg.msgList");
		resultMap.put("msgList", msgList);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/FileUpload/{dir}")
	public void fileUpload(@RequestParam("file") MultipartFile[] files, @PathVariable("dir") String dir, HttpServletRequest req, HttpServletResponse res) {
//		HttpdUtil.makeJsonWriter(res, fsi.fileUpload(files, dir, req));
	}
	
	@RequestMapping("/boardOne")
	public ModelAndView boardOne(HttpServletRequest req) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		String boardNo = req.getParameter("boardNo");
		param.put("boardNo", boardNo);
		
		HashMap<String, Object> boardOne = session.selectOne("board.boardOne",param);
		resultMap.put("boardOne", boardOne);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/boardUpdate")
	public ModelAndView boardUpdate(HttpServletRequest req) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String boardNo = req.getParameter("boardNo");
		String boardTitle = req.getParameter("boardTitle");
		String boardContents = req.getParameter("boardContents");
		String category = req.getParameter("category");
		map.put("boardNo", boardNo);
		map.put("boardTitle",boardTitle );
		map.put("boardContents", boardContents);
		map.put("category", category);
		
		int status = session.update("board.boardUpdate",map);
		resultMap.put("status", status);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/boardDelete")
	public ModelAndView boardDelete(HttpServletRequest req) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		String boardNo = req.getParameter("boardNo");
		param.put("boardNo", boardNo);
		
		int status = session.update("board.boardDelete", param);
		
		resultMap.put("status", status);
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/userInsert")
	public ModelAndView userInsret(HttpServletRequest req) {
			
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String id = req.getParameter("id");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		
		map.put("id", id);
		map.put("userName", userName);
		map.put("password", password);
		
		int status = session.insert("user.userInsert", map);
		
		resultMap.put("status", status);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/userLogin")
	public ModelAndView userLogin(HttpServletRequest req, HttpSession sessionC) {
		
		HashMap<String, Object> param = HttpdUtil.getParamMap(req);
		
		System.out.println(param);
		
		HashMap<String, Object> resultMap = session.selectOne("user.userLogin",param);
		
		if(resultMap == null) {  
			resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			System.out.println("실패");
		}else {
			resultMap.put("status", 1);
			System.out.println("성공");
		}
		sessionC.setAttribute("user", resultMap); 
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/sessionCheck")
    public void sessionCheck(HttpSession sessionC, HttpServletResponse res) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    HashMap<String, Object> user = (HashMap<String, Object>) sessionC.getAttribute("user");

	//    result.put("data", user);
	    if(user == null) {
	       result.put("result", 0);
	       HttpdUtil.makeJsonWriter(res, result);             
	    }
	    else if(user.get("status").toString().equals("1")) {
	       result.put("result", 1);
	       HttpdUtil.makeJsonWriter(res, result); 
	    }
	    else {
	       result.put("result", 0);
	       HttpdUtil.makeJsonWriter(res, result);          
	    }
	}
	
	@RequestMapping("/logout")
	public String Logout(HttpSession sessionC) {
		sessionC.invalidate();
		
		return "redirect:/";
	}
		
	@RequestMapping("/userSession")
	public ModelAndView userSession(HttpSession sessionCheck) {
		
		HashMap<String, Object> userMap = (HashMap<String, Object>) sessionCheck.getAttribute("user");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		int userNo = (Integer) userMap.get("userNo");
		param.put("userNo", userNo);
		
		HashMap<String, Object> userSelectOne = session.selectOne("user.userSelectOne", userNo);
		resultMap.put("userSelectOne", userSelectOne);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/userLevel")
	public ModelAndView userLevel(HttpSession sessionCheck) {
		System.out.println("오냐?");
		HashMap<String, Object> userMap = (HashMap<String, Object>) sessionCheck.getAttribute("user");
		HashMap<String, Object> status = new HashMap<String, Object>();
		
		int level = (Integer) userMap.get("level");
		int userNo = (Integer) userMap.get("userNo");
		
//		param.put("level", level);
		System.out.println(userMap);
		System.out.println(level);
		
		if(level == 1) {
			status.put("status", "1");
			status.put("userNo", userNo);
		}else {
			status.put("status", "2");
		}
		
		return HttpdUtil.makeJsonView(status);
	}
	@RequestMapping("/passwordUpdate")
	public ModelAndView passwordUpdate(HttpServletRequest req,HttpSession sessonCheck) {
		
		HashMap<String, Object> userMap = (HashMap<String, Object>) sessonCheck.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		int userNo = (Integer) userMap.get("userNo");
		String password = req.getParameter("password");
		
		param.put("userNo", userNo);
		param.put("password", password);
		
		int status = session.update("user.passwordUpdate", param);
		resultMap.put("status", status);
		
		System.out.println(resultMap);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/userDelete")
	public ModelAndView userDelete(HttpSession sessionCheck) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> userMap = (HashMap<String, Object>) sessionCheck.getAttribute("user");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		int userNo = (Integer) userMap.get("userNo");
		param.put("userNo", userNo);
		
		int status = session.update("user.userDelete",param);
		
		resultMap.put("status", status);
		
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/sendMessage")
	public ModelAndView sendMessage(HttpServletRequest req) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		String msgName = req.getParameter("msgName");
		String msgEmail = req.getParameter("msgEmail");
		String msgContents = req.getParameter("msgContents");
		
		param.put("msgName", msgName);
		param.put("msgEmail", msgEmail);
		param.put("msgContents", msgContents);
		
		int status = session.insert("msg.msgInsert", param);
		
		resultMap.put("status", status);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/msgDetail")
	public ModelAndView msgDetail(HttpServletRequest req) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		String msgNo = req.getParameter("msgNo");
		param.put("msgNo", msgNo);
		
		HashMap<String, Object> msgDetail = session.selectOne("msg.msgSelectOne",param);
		
		resultMap.put("msgDetail", msgDetail);
		
		return HttpdUtil.makeJsonView(resultMap);
	}
	
//	@RequestMapping("/msgDelete")
//	public ModelAndView msgDelete(HttpServletRequest req) {
//		
//		HashMap<String, Object> param = new HashMap<String, Object>();
//		HashMap<String, Object> resultMap = new HashMap<String, Object>();
//		
//		
//		
//		return HttpdUtil.makeJsonView(resultMap);
//	}
}













