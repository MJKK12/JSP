package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.*;

public class MallHome1Action extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)
		// 로그인을 하지 않은 상태에서 특정제품을 조회한 후 "장바구니 담기"나 "바로주문하기" 할때와 "제품후기쓰기" 를 할때 
		// 로그인 하라는 메시지를 받은 후 로그인 하면 시작페이지로 가는 것이 아니라 방금 조회한 특정제품 페이지로 돌아가기 위한 것임.
		// 제품 상세보기 클릭하자마자 일단 url 세션이 들어오는 것임. (로그인에도 이 goBackURL(request)) 를 해뒀기 때문에 session 이 null 이 아니라면 로그인 한 후 에도 그 페이지 그대모 머물러 있다.
		
		// 카테고리 목록을 가져오기 (부모클래스인 AbstractController 에서 호출해온다.)
		super.getCategoryList(request);
	
		// === Ajax(JSON) 을 사용하여 HIT 상품목록을 "더보기" 방식으로 페이징 처리해서 보여주겠다. === //
		// DAO 에서 저장된 data 들을 불러온다.		
		InterProductDAO pdao = new ProductDAO();
		
		// HIT 상품의 전체개수를 알아온다. (DAO 에 가서 알아온다.) --> 페이징 처리를 위해 전체 개수를 알아와야 한다.
		int totalHITCount = pdao.totalPspecCount("1");	// where 절 snum 이 ==> "1"이다 (HIT의 경우)
		
	//	System.out.println("확인용 totalHITCount :" + totalHITCount);
		// 확인용 totalHITCount : 36
		
		request.setAttribute("totalHITCount", totalHITCount);
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallHome1.jsp");
	
	}

}
