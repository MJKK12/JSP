package common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myshop.model.ImageVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class IndexController extends AbstractController {
	// 시작페이지
	@Override
	public String toString() {
		return "*** 확인용 IndexController 클래스의 인스턴스 메소드인 toString() 을 호출함 ***";
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)

		InterProductDAO pdao = new ProductDAO();		// DAO 에서 가져온다.
		List<ImageVO> imgList = pdao.imageSelectAll();	// db 에서 select 해온 결과물을 저장소인 request 영역에 저장해준다.
		
		request.setAttribute("imgList", imgList);	// 이 이미지를 view 단 페이지에 넣는다. "imgList" 파일만 열어볼 수 있는 것이다.
		
		
	/*	
		super.setRedirect(false);	// 부모클래스에 있는 setRedirect 에 boolean(true,false) 값을 주면 isRedirect 값이 바뀌는 것.
		this.setRedirect(false);	// this 는 생략 가능하다.
		setRedirect(false);			// 그러나 부모클래스에서 이미 isRedirect 기본값은 false 였기 때문에 쓰지 않아도 된다.		
	*/
		
	// 부모클래스에 접근은 못하지만, isRedirect 메소드를 통해서 쓸 뿐이지 직접 접근은 하지 못한다.
	// isRedirect 를 직접 건들지는 못하지만, 메소드를 통해서 건든다는 뜻이다.
	// 부모클래스에서 이미 isRedirect 기본값은 false 였기 때문에 super.setRedirect(false); 를 쓰지 않아도 된다.		
		super.setViewPage("/WEB-INF/index.jsp");	// view 단 페이지가 어디인지 알려준다.
		
	}

}
