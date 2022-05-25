package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test1Controller extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("name", "이순신");	// 이순신 값을 아래의 setViewPage에 넘겨준다.
	//	super.setRedirect(isRedirect());	// 부모클래스에서 상속받는데, isRedirect 가 false 기본값 이므로, 굳이 쓰지 않아도 된다.
		super.setViewPage("/WEB-INF/test/test1.jsp");	// 이에 맞는 jsp 파일을 만든후 웹에서 실행해본다.
	}

}
