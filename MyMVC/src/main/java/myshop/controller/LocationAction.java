package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class LocationAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)

	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/location.jsp");		
	}

}
