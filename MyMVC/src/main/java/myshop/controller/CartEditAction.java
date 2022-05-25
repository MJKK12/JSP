package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class CartEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
		
		// POST 방식으로만 접근가능
		if(!"POST".equalsIgnoreCase(method)) {// get 방식일때
			String message = "비정상적인 경로로 들어왔습니다";
			String loc = "javascript:history.back()";
			 
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			 
			super.setViewPage("/WEB-INF/msg.jsp");
			return;					
		}
		
		else if("POST".equalsIgnoreCase(method) && super.checkLogin(request)) {
			// POST 방식이고 로그인을 했을 때, 로그인한 사용자 소유의 장바구니에 담았다면 장바구니 수량을 변경한다.
			// POST 방식 일지라도 타인의 장바구니를 변경해서는 안됨.
			// 장바구니에서 해당 제품을 변경한다.
			// super.checkLogin(request) : 로그인 시 true 값 반환 (로그인 된 상태라는 것)
			
			String cartno = request.getParameter("cartno");
			String oqty = request.getParameter("oqty");		// 주문량
			String userid = request.getParameter("userid");	// 로그인한 사용자의 것이어야 한다. (장바구니 소유주 아이디)
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser"); // 키값은 loginuser
			
			if( loginuser.getUserid().equals(userid) ) {
				// 장바구니를 비운다. (getParatmeter 해온 값과 loginuser 가 같을 때)
				// 로그인한 유저의 id = 장바구니 소유주
				
				// DB 에서 읽어온 후 장바구니를 주문량을변경. (where 절에 cartno 와 oqty를 넘긴다.)
				// map 을 사용하도록 한다.
				InterProductDAO pdao = new ProductDAO();
				
				// where 절에 올 cartno, oqty 를 map에 넣어주자.
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("cartno", cartno);
				paraMap.put("oqty", oqty);
				
				// 장바구니 테이블에서 특정 제품의 주문량을 변경하기
				int n = pdao.updateCart(paraMap);
				
				JSONObject jsobj = new JSONObject();	// JSONObject 타입으로 결과 반환
				jsobj.put("n", n);
				
				String json = jsobj.toString();	// 결과물인 jsobj 를 웹페이지에 찍어준다. (웹페이지에 결과물 찍기 위해 string 변환)
				
				request.setAttribute("json", json);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/jsonview.jsp");
				
			}
			else {	// 다른사용자의 장바구니에 들어왔을 때
				String message = "다른 사용자의 장바구니는 제거할 수 없습니다.";
				String loc = "javascript:history.back()";
				 
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				 
				super.setViewPage("/WEB-INF/msg.jsp");
				return;		
			}
			
			
		}
		
	}

}
