package common.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import member.model.*;
import my.util.MyUtil;
import myshop.model.*;

public abstract class AbstractController implements InterCommand {
	// AbstractController 클래스는 추상(미완성) 클래스로써 부모클래스로 사용된다.
	// 부모클래스인데 인터페이스에 올려놓은 것은 재정의하지 않겠다 (abstract)
	/*
	    === 다음에 나오는 것은 우리끼리한 약속이다. ===
	
      ※ view 단 페이지(.jsp)로 이동시 forward 방법(dispatcher)으로 이동시키고자 한다라면 
        자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
	     
	    super.setRedirect(false); 
	    super.setViewPage("/WEB-INF/index.jsp");	--> 이 페이지로 보내주겠다.
	    
	    
      ※ URL 주소를 변경하여 페이지 이동시키고자 한다라면
	    즉, sendRedirect 를 하고자 한다라면    
	    자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
	          
	    super.setRedirect(true);
	    super.setViewPage("registerMember.up");     --> 이 url 로 보내주겠다.         
	*/
	
	private boolean isRedirect = false;
	// isRedirect 변수의 값이 false 라면 view단 페이지(.jsp)로 forward 방법(dispatcher)으로 이동시키겠다. 
	// isRedirect 변수의 값이 true 라면 sendRedirect 로 페이지이동을 시키겠다.
	
	private String viewPage;
	// viewPage 는 isRedirect 값이 false 이라면 view단 페이지(.jsp)의 경로명 이고,
	// isRedirect 값이 true 이라면 이동해야할 페이지 URL 주소 이다.
	
	public boolean isRedirect() {	// boolean 타입은 get 이 아니라 is가 온다.
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// 로그인 유무를 검사해서 로그인 했으면 true 를 리턴해주고,
	// 로그인 하지 않았으면 false 를 리턴한다.
	
	public boolean checkLogin(HttpServletRequest request) {		
		// 세션에 올라와 있는 것은 모든 클래스에서 읽을 수 있다.
		// 모든 클래스에서 공통으로 사용하는 checkLogin 을 '부모클래스'에서 사용한다.
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");	// 여기서 loginuser 는 MemberVo 이다.
		
		if(loginuser != null) {
			//로그인 한 경우
			return true;
		}
		else {
			// 로그인 하지 않은 경우
			return false;
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// *** 제품목록(Category)을 보여줄 메소드 생성하기 *** //
	// VO 를 사용하지 않고 MAP 으로 처리해본다. (VO 도 사용 가능하다.)
	public void getCategoryList(HttpServletRequest request) throws SQLException {
	// DB 에 저장되어 있는 카테고리 리스트가 필요하다.
	// 상품과 관련된 DAO 가 필요하다.
	// 자식클래스인 MallHome1 과 MallHome2 에서 호출하기 위해 부모클래스에 만들었다.
		
		InterProductDAO pdao = new ProductDAO();
		
		List<HashMap<String, String>> categoryList = pdao.getCategoryList();	// where 절은 필요없이 모두 select 해올 것이다. 그러므로 파라미터는 필요가 없다.
		// 복수개가 select 되므로 list 로 한다.
		// DB 에서 가져온 것들을 아래의 request 영역에 "categoryList" 의 key 값으로 넣어둔 것이다.
		
		request.setAttribute("categoryList", categoryList);
		// view 단으로 보내준다.
	}
	
	/////////////////////////////////////////////////////////////////////
	// 매번 쓰는 기능이므로 부모클래스에 만들어서 불러온다. (MyUtil 에는 getCurrnetURL(현재 URL 주소 알려주는 메소드) 가 있다.)
	// *** 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 본 그 페이지에 그대로 머무를 수(갈 수) 있도록 한다.
	public void goBackURL(HttpServletRequest request) {
		// request 를 넘겨주면 session (누구나 다 접근, 호출 가능)
		HttpSession session =  request.getSession();				// session 에 넘겨주면 누구나 다 쓸 수 있다.
		session.setAttribute("goBackURL", MyUtil.getCurrnetURL(request));	// request 를 넣었을 때 현재 URL 을 알려준다.
		// session 에 현재 보고자 하는 URL 이 들어오는 것이다.
	}
	
	
	
}
