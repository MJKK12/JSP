package myshop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class CommentUpdateAction extends AbstractController {

	// 특정제품 구매후기 리뷰 수정하기	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
	      
	      if("POST".equalsIgnoreCase(method)) {
	         // **** POST 방식으로 넘어온 것이라면 **** //
	         
	         String review_seq = request.getParameter("review_seq");
	         String contents = request.getParameter("contents");

	         // !!!! 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기 !!!! //
	         contents = contents.replaceAll("<", "&lt;");
	         contents = contents.replaceAll(">", "&gt;");
	         
	         // 입력한 내용에서 엔터는 <br>로 변환시키기 
	         contents = contents.replaceAll("\r\n", "<br>");
	         
	         InterProductDAO pdao = new ProductDAO();
	         
	         Map<String,String> paraMap = new HashMap<>();
	         paraMap.put("review_seq", review_seq);
	         paraMap.put("contents", contents);

	         int n = pdao.reviewUpdate(paraMap);
	         
	         JSONObject jsobj = new JSONObject();  
	         jsobj.put("n", n);
	         
	         String json = jsobj.toString();  // 문자열 형태로 변환해줌.
	         
	         request.setAttribute("json", json);
	         
	      //   super.setRedirect(false);
	         super.setViewPage("/WEB-INF/jsonview.jsp");
	         
	      }
	      else {
	         // **** POST 방식으로 넘어온 것이 아니라면 **** //
	         
	         String message = "비정상적인 경로를 통해 들어왔습니다.!!";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
	      }      		
		
	}

}
