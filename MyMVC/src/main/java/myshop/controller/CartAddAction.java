package myshop.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class CartAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 장바구니는 항상 로그인을 했는지 검사 해야한다. (로그인 한 유저만 장바구니에 담을 수 있다.)
		// *** 로그인 유무 검사하기 *** //
		boolean isLogin = super.checkLogin(request);
		
		if(!isLogin) { // 로그인 하지 않은 상태라면
			
			/*
			  사용자가 로그인을 하지 않은 상태에서 특정제품을 장바구니에 담고자 하는 경우
			  사용자라 로그인을 하면 장바구니에 담고자 했던 해당 특정제품 페이지로 이동하도록 해야 한다.
			  이와 같이 하기 위해 ProdViewAction 클래스에서 super.goBackURL(request); 을 해두었다.
			*/
			
			request.setAttribute("message", "장바구니에 담기 위해서는 로그인이 필요합니다.");
			request.setAttribute("loc", "javascript:history.back()");	
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {	// 로그인 한 상태라면
			// 경우의 수를 생각해보자. 장바구니에 물건이 있는데 같은 물건을 또 담을 수가 있다. 이때 해당 제품을 장바구니 테이블에 넣을 것인데,
			// 장바구니 테이블(tbl_cart) 에 해당 제품을 담아야 한다.
			// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고,
			// 장바구니 테이블에 해당 제품이 이미 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다면 tbl_cart 테이블에 update 를 해야한다.
			
			String method = request.getMethod();
			
			if("post".equalsIgnoreCase(method)) {
				// POST 방식이라면
				String pnum = request.getParameter("pnum");	// 제품 번호
				String oqty = request.getParameter("oqty"); // 주문 개수
				
				// 로그인 한 유저의 id (위에서 로그인 한 사람만 장바구니 쓸 수 있도록 세팅해놓음)
				HttpSession session = request.getSession();
				MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
				
				// view 단에서 form 태그 정보를 받아왔으니 DB 로 보내주자
				InterProductDAO pdao = new ProductDAO();

				// 받아온 3개의 정보를 map 에 담아서 DB로 보내자. (pnum, oqty, loginuser)
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("pnum", pnum);
				paraMap.put("oqty", oqty);
				paraMap.put("userid", loginuser.getUserid());	// 로그인 한 사람의 id
				
				String message = "";
				String loc = "";
				
				try {
					int n = pdao.addCart(paraMap);		// insert 또는 update	
					// insert 되든 update 가 되든 return 값은 1이 나온다.
					
					if(n==1) {	// 정상적으로 insert 또는 update 가 되었다면
						message = "장바구니 담기에 성공했습니다.";
						loc = "cartList.up";	// 상대경로를 써도 된다. (request.getContextPat() 를 써도 된다.) 장바구니 목록을 보여주는 view 페이지로 이동한다.					
					}
					else {
						message = "장바구니 담기에 실패했습니다.";
						loc = "javascript:history.back();";	// 상대경로를 써도 된다. 장바구니 목록을 보여주는 view 페이지로 이동한다.											
					}
					
				} catch (SQLException e) {
					e.printStackTrace();	// sql 문이 틀렸을 때 					
					message = "장애 발생으로 인해 장바구니 담기에 실패했습니다.";
					loc = "javascript:history.back();";	// 상대경로를 써도 된다. 장바구니 목록을 보여주는 view 페이지로 이동한다.											
				}
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
			//	super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			else {
				// GET방식이라면
				  String message = "비정상적인 경로로 들어왔습니다";
				  String loc = "javascript:history.back()";
				   
				  request.setAttribute("message", message);
				  request.setAttribute("loc", loc);
				      
				// super.setRedirect(false);   
				   super.setViewPage("/WEB-INF/msg.jsp");				
			}
			
		}
		
	}
	
}
