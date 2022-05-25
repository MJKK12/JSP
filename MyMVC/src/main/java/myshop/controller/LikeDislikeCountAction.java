package myshop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.*;

public class LikeDislikeCountAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String pnum = request.getParameter("pnum");
		
		InterProductDAO pdao = new ProductDAO();
		
		Map<String, Integer> map = pdao.getLikeDislikeCnt(pnum);	// 좋아요, 싫어요 컬럼 2개 이므로 Map 을 쓴다. (복수개 행이 아니므로 list 를 쓰지 않는다.)
		
		JSONObject jsonObj = new JSONObject();	// {} 객체타입
		
		jsonObj.put("likecnt", map.get("likecnt"));
		jsonObj.put("dislikecnt", map.get("dislikecnt"));
		
		String json = jsonObj.toString();	// "{"likecnt:2", "dislikecnt:0"dislikecnt}" 문자열 타입
	
		request.setAttribute("json", json);
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");		
		
	}

}
