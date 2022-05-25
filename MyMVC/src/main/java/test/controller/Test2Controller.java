package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test2Controller extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// true 니까 forward 가 아니라 sendRedirect 이다.
		super.setRedirect(true);
		super.setViewPage(request.getContextPath()+"/test/test1.up");
		// test2 를 검색하면 test1.up 으로 가도록 한다.
		// getContextPath() 는 MyMVC 이다.
	}

}
