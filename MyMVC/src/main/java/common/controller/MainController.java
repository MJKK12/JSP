package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainController extends AbstractController {
	
	@Override
	public String toString() {
		return "@@@ 확인용 MainController 클래스의 인스턴스 메소드인 toString() 을 호출함 ***";
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	/*	
		super.setRedirect(true);	// 부모클래스에 있는 setRedirect 에 boolean(true,false) 값을 주면 isRedirect 값이 바뀌는 것.
		this.setRedirect(true);		// this 는 생략 가능하다.
		setRedirect(true);			// 그러나 부모클래스에서 이미 isRedirect 기본값은 false 였기 때문에 쓰지 않아도 된다.		
	*/
		
		// 내가 이해한 것 : 즉, 바로 이어질 .jsp 파일이 없고, index.up 과 연결된 indexController 클래스로 가서, 그 클래스에 연결된 .jsp 페이지로 setViewPage 를 한다.
		super.setRedirect(true);		// true 라면 sendRedirect 이기 때문에 .up 이다.
		super.setViewPage("index.up");	// view 단 페이지가 어디인지 알려준다.

	}

}
