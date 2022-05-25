package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class CommentListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// '특정제품' 에 대한 제품후기 리뷰 들을 보여준다.
		String fk_pnum = request.getParameter("fk_pnum");
		
		InterProductDAO pdao = new ProductDAO();	
		List<PurchaseReviewsVO> commentList = pdao.commentList(fk_pnum);	// 어떤 제품(fk_pnum) 에 대한 리뷰인지.
		
		JSONArray jsArr = new JSONArray();
		
		if(commentList.size() > 0) {	// DAO 에서 new ArrayList() 이기 때문에 조회결과가 없어도 null 이 아니다.
			
			for(PurchaseReviewsVO reviewsVO : commentList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("review_seq", reviewsVO.getReview_seq());
				jsObj.put("userid", reviewsVO.getFk_userid());
				jsObj.put("name", reviewsVO.getMvo().getName());
				jsObj.put("contents",reviewsVO.getContents());
				jsObj.put("writeDate", reviewsVO.getWriteDate());
				
				jsArr.put(jsObj);
			}// end of for-------------------------------------
			
		}
		
		String json = jsArr.toString();	
		// [] 또는
		// [{"contents":"제가 좋아하는 옷입니다~~","review_seq":3,"name":"엄정화","writeDate":"2022-04-13 14:34:11","userid":"eomjh"},{"contents":"시원하고 좋습니다.","review_seq":1,"name":"김민정","writeDate":"2022-04-13 14:31:06","userid":"kimmj"}]
		
		request.setAttribute("json", json);

	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");		
	}
		

}
