package myshop.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class CartListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// super.~~ 은 모두 부모클래스에서 가져온 것이다.
		// 카테고리 목록을 조회해오기
		super.getCategoryList(request);
		
		// 장바구니 보기는 반드시 해당 사용자가 로그인을 해야만 볼 수 있다.
		boolean isLogin = super.checkLogin(request);
		
		if(!isLogin) {
		 //  로그인을 하지 않았다면
			 request.setAttribute("message", "장바구니를 보려면 먼저 로그인 부터 하세요!!");
	         request.setAttribute("loc", "javascript:history.back()"); 
	         
	      // super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         return;			
		}
		else {
		 //  로그인을 한 상태이다. (장바구니 리스트는 사용자인 경우에 사용자의 리스트만 보여주고, 관리자는 모든 회원의 장바구니 목록을 본다.)
			 HttpSession session = request.getSession();
			 MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			 
			 // DB 에서 자기것만 보여주자.
			 InterProductDAO pdao = new ProductDAO();			 
			 // 제품 카트를 보는 장바구니를 만든다. (where 절에 login 된 사람들의 것만 본다.)
			 List<CartVO> cartList = pdao.selectProductCart(loginuser.getUserid());
			 
			 // DB 에서 읽어온 후 넘겨주자. (장바구니 총액 & 총포인트)
			 // where 절에 필요한 것 ==> 로그인된 userid 
			 // 금액, 수량을 알아오기 위해 map 을 쓴다
			 // 로그인한 사용자의 장바구니에 담긴 주문 총액 합계 및 총 포인트 합계 알아오기
			 Map<String, String> resultMap = pdao.selectCartSumPricePoint(loginuser.getUserid());

			 request.setAttribute("cartList", cartList);	// view 단 페이지에 보여주기 위해 보낸다.
			 request.setAttribute("resultMap", resultMap);	// view 단 페이지에 보여주기 위해 보낸다.
			 
			 super.setRedirect(false);
			 super.setViewPage("/WEB-INF/myshop/cartList.jsp");	// cartList 를 보여주는 view 단으로 넘긴다.
			
		}
		
	}

}
