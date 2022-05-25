package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.*;

public class ProdViewAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)
		// 로그인을 하지 않은 상태에서 특정제품을 조회한 후 "장바구니 담기"나 "바로주문하기" 할때와 "제품후기쓰기" 를 할때 
		// 로그인 하라는 메시지를 받은 후 로그인 하면 시작페이지로 가는 것이 아니라 방금 조회한 특정제품 페이지로 돌아가기 위한 것임.
		// 제품 상세보기 클릭하자마자 일단 url 세션이 들어오는 것임. (로그인에도 이 goBackURL(request)) 를 해뒀기 때문에 session 이 null 이 아니라면 로그인 한 후 에도 그 페이지 그대모 머물러 있다.
		
		// 카테고리 목록을 조회해오기	(VO 에 만들어뒀다.)
		super.getCategoryList(request);
		
		String pnum = request.getParameter("pnum");	// 제품번호 (mallHome1.jsp 에서 href 에 pnum 을 넘겨줘야 하므로 가져온다.)
	
		// DB 에서 pnum 을 select 해오자
		InterProductDAO pdao = new ProductDAO();

		// select 된 정보 1개를 가져오자.
		// 제품번호(pnum) 을 가지고서 해당 제품의 정보를 조회하기  (pvo -DB 에서 읽어온것)
		ProductVO pvo = pdao.selectOneProductByPnum(pnum);	// 제품번호(파라미터 pnum)를 가지고, 제품 1개의 정보를 조회할 수 있다.
				
		// 제품번호(pnum)을 가지고서 해당 제품의 추가된 이미지 정보를 조회하기 (1개 대표이미지 --> 에 딸린 나머지 n개의 이미지가 있다.)
		List<String> imgList = pdao.getImagesByPnum(pnum);	// 추가된 이미지만 보여주면 된다. (return이 복수개)
		
		// get 방식이기 때문에 유저가 장난칠 가능성이 있음.
		// 존재하지 않는 제품번호(pnum) 을 써서 장난치는 경우 (pnum=123235436...)
		if(pvo == null) {
			// GET 방식이므로 사용자가 웹브라우저 주소창에서 장난쳐서 존재하지 않는 제품번호를 입력한 경우
	         String message = "검색하신 제품은 존재하지 않습니다.";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	        // super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         
	         return;			
		}
		else {
			// 제품이 존재하는 경우 pvo 와 imaList 를 view 단에 넘겨주겠다.
			request.setAttribute("pvo", pvo);			// 제품 클릭시 보여지는 제품의 정보
			request.setAttribute("imgList", imgList);	// 해당 제품에 추가된 이미지 

			super.setViewPage("/WEB-INF/myshop/prodView.jsp");		
		}
		
	}

}
