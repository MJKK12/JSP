package myshop.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.*;

public class CommentRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 구매후기 쓰기 (jsp 에서 보낸 구매후기 form 태그 모든 것) // 구매후기를 쓴 유저가 누구이고, 어떤 제품이고, 어떤 내용인지
		String fk_userid = request.getParameter("fk_userid");
		String fk_pnum = request.getParameter("fk_pnum");
		String contents = request.getParameter("contents");

		// !!!! 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기 !!!!! //
		contents =  contents.replaceAll("<","&lt;");
		contents =  contents.replaceAll(">","&gt;");
		// 입력한 내용에서 엔터는 <br> 로 변환시키기
		contents =  contents.replaceAll("\r\n", "<br>");	// 엔터 인식
		
		PurchaseReviewsVO reviewsVO = new PurchaseReviewsVO();
		// VO 에 담아주자.
		reviewsVO.setFk_userid(fk_userid);
		reviewsVO.setFk_pnum(Integer.parseInt(fk_pnum));
		reviewsVO.setContents(contents);
		
		// form 태그로부터 넘어온 것들을 insert 해주자.
		InterProductDAO pdao = new ProductDAO();		
		
		int n = 0;		
		// 구매후기는 1번만 쓸 수 있도록 한다. (Unique Key 로 막아두었다.)
		// 제약조건에 위배된 경우 (동일한 제품에 대하여 동일한 회원이 제품후기를 2번 쓴 경우 unique 제약에 위배됨)
		try {
			n = pdao.addComment(reviewsVO);		// sql 문 실행시 정상이면 0, 제약조건 위배 시 -1
			
		} catch (SQLIntegrityConstraintViolationException e) {
		//	e.printStackTrace();
			n = -1;	// 정상이면 0을주고, 제약조건 위배시 -1을 준다.
			
		} catch (Exception e) {
		//	e.printStackTrace();
			
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		String json = jsonObj.toString();	// {"n":1} 또는 {"n":-1}
		
		request.setAttribute("json", json);

	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
