package myshop.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberVO;
import myshop.model.*;

public class OrderAddAction extends AbstractController {

	// 전표(주문코드) 를 생성해주는 메소드 생성하기 //
	private String getOdrCode() throws SQLException {
		
		// 전표(주문코드) 형식 : s+날짜+sequence ==>  s20220411-1
		
		// 날짜 생성
		Date now = new Date();
		SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd"); 
		String today = smdatefm.format(now);
		
		// 시퀀스 얻어오기
		InterProductDAO pdao = new ProductDAO();
		
		int seq = pdao.getSeq_tbl_order();
		// pdao.getSeq_tbl_order(); 는 시퀀스 seq_tbl_order 값을 채번해오는 것이다.
		
		return "s"+today+"-"+seq;		
		
	}// end of String getOdrCode()-------------------------------------------
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// $ajax 로 넘겨받은 data들을 가져온다.
		// 두개의 테이블에 insert 해줘야 한다.
		String pnumjoin = request.getParameter("pnumjoin");				// 제품 번호가 한개가 아니라 여러개로 이어져 있다. (join)
		String oqtyjoin = request.getParameter("oqtyjoin");	
		String cartnojoin = request.getParameter("cartnojoin");			// 장바구니 번호는 삭제(delete)해야 할 때 필요함 (주문하면 해당 제품을 장바구니에서 비워야함)	
		String totalPricejoin = request.getParameter("totalPricejoin");	

		String sumtotalPrice = request.getParameter("sumtotalPrice");	
		String sumtotalPoint = request.getParameter("sumtotalPoint");
	
	/*	
		System.out.println("~~~~~~~~~~~~ 확인용 pnumjoin : " + pnumjoin); 5,3,60
		System.out.println("~~~~~~~~~~~~ 확인용 oqtyjoin : " + oqtyjoin); 2,4,2
		System.out.println("~~~~~~~~~~~~ 확인용 cartnojoin : " + cartnojoin); 6,3,1
		System.out.println("~~~~~~~~~~~~ 확인용 totalPricejoin : " + totalPricejoin); 66000,40000,20000
		System.out.println("~~~~~~~~~~~~ 확인용 sumtotalPrice : " + sumtotalPrice); 126000
		System.out.println("~~~~~~~~~~~~ 확인용 sumtotalPoint : " + sumtotalPoint); 260
	*/
	
	  // ===== Transaction 처리하기 ===== // 
	  // 1. 주문 테이블에 입력되어야할 주문전표를 채번(select)하기 
	  // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
	  // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
	  // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
	    
	  // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete OR update)하기(수동커밋처리)
	  // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 
	    
	  // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
	  // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
	  // 8. **** SQL 장애 발생시 rollback 하기(rollback) **** 
	   
	  // === Transaction 처리가 성공시 세션에 저장되어져 있는 loginuser 정보를 새로이 갱신하기 ===
	  // === 주문이 완료되었을시 주문이 완료되었다라는 email 보내주기  === // 	
		
		InterProductDAO pdao = new ProductDAO();
		
		// vo 대신 map 을 쓰자. (map 에 담아서 DB 에 보내자.)
		Map<String, Object> paraMap = new HashMap<>(); 	// String 타입이건 String 타입의 '배열' 이건 다 받는 것은 부모클래스.(다형성) 그러므로 Object 를 쓴다. 객체이면 다 받기 때문
		// map 에 아래의 정보들을 put 할 때, 배열이건 아니건 다 받는다는 의미로 String, String 이 아니라 String, Object 로 받는다.
		
		// == 주문 테이블에 insert == // erd 보면서 코드 짜기, map 에 넣어서 DB 로 보내자
		
		// 1. 전표(주문코드)를 가져오기 --> 위에서 만들어 놓은 메소드를 가져오자.
		String odrcode = getOdrCode();		// return 타입이 String		
		paraMap.put("odrcode", odrcode);	// DB 에 보내기 위해서 map 에 담자.
		
		// 2. 회원아이디 (어차피 장바구니는 로그인 된 사람만 가능)
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");		
		paraMap.put("userid", loginuser.getUserid());
		
		// 3. 주문총액 및 주문총포인트
		paraMap.put("sumtotalPrice", sumtotalPrice);
		paraMap.put("sumtotalPoint", sumtotalPoint);
		
		// 4. 주문일자 (DB 에 sysdate로 들어가므로 쓸 필요가 없다.)
		
		
		
		// ## 주문상세 테이블에 insert ## // erd 보면서 코드 짜기
		
		// 1. 주문상세 일련번호 (내부적으로 시퀀스 쓰면 된다, PK 이므로)
		
		// 2. 전표(주문코드) 가져오기 (주문상세 테이블에서 주문코드는 '주문' 테이블 주문코드의 FK) ==> 이미 위의 주문테이블 insert 할 때 주문코드 map 에 넣어놨다.
		
		// 3. 제품번호 (Split 사용을 통해 join 된 pnum 을 다시 나누어준다. 1개 제품으로만 이루어진 것이 아니기 때문이다. "," 를 중심으로 나눈다.)
		String[] pnumArr = pnumjoin.split(",");		// , 외에는 \\ 를 써줘야 한다. 콤마는 생략 가능. String 타입의 배열이 나온다.
													// ex) 장바구니에서 여러개 제품을 주문한 경우 					 			(ex "5,3,60" ==> split ==> ["5","3","60"] )				
													// ex) 장바구니에서 제품 1개만 주문한 경우 (장바구니 번호O)   					(ex "5" ==> split ==> ["5"] )				
													// ex) 특정제품을 장바구니에 담지않고 '바로주문하기' 로 주문한 경우(장바구니 번호X) (ex "5" ==> split ==> ["5"] )			
		// 4. 주문량
		String[] oqtyArr = oqtyjoin.split(",");	
		
		// 5. 주문가격
		String[] totalPriceArr = totalPricejoin.split(",");	
		
		paraMap.put("pnumArr", pnumArr);	// String 타입의 배열
		paraMap.put("oqtyArr", oqtyArr);
		paraMap.put("totalPriceArr", totalPriceArr);

		
		
		// @@ 제품 테이블 update 하기 @@ // (장바구니 주문 시, 해당 제품의 잔고량을 주문갯수만큼 줄여야 한다.)
		// 1. 제품 테이블의 잔고량 컬럼의 값을 주문량 만큼 감해야 한다.(-) ==> 위에서 주문량은 이미 구해두었다.
		
		
		// ** 장바구니 테이블에서 delete 하기 **//
		// 1. 장바구니 번호를 알아오기 (바로주문하기의 경우 장바구니 번호가 없으므로 cartnojoin 이 null 값이 나올 수 있기 때문에 이를 방지하기 위해 if 문을 사용한다.)
		paraMap.put("cartnojoin", cartnojoin);			// ex) 장바구니에서 여러개 제품을 주문한 경우 (ex. "5,3,60" ==> split ==> ["5","3","60"] )			
														// ex) 장바구니에서 제품 1개만 주문한 경우 (장바구니 번호O)   (ex. "5" ==> split ==> ["5"] )
														// ex) 특정제품을 장바구니에 담지않고 '바로주문하기' 로 주문한 경우 (장바구니 번호X) (ex. null )	
		// 특정제품을 장바구니에 담지않고 '바로주문하기' 로 주문한 경우 cartnojoin 의 값은 NULL 이다. (form 태그에서 보낼때부터 아예 cartnojoin 값이 없음)
		
		// ===== 회원 테이블에서 로그인한 사용자의 coin 금액과 point 를 update 하기 ===== //	
		// 1. 로그인 한 사용자 (map 에 이미 넣어두었다.)
		
		// 2. coin 금액 & point 금액을 update 하기 위한 것은 이미 위에서 sumtotalPrice 와 sumtotalPoint 을 map 에 넣어둠
		
		
		// ===== Transction 처리를 해주는 메소드 호출하기 ===== //
		int isSuccess = pdao.orderAdd(paraMap);	// paraMap 에 담은 것을 db 에 보내준다.
		
		JSONObject jsobj = new JSONObject();
		jsobj.put("isSuccess", isSuccess);		// DAO 에서 메소드 성공시 1 return;
		
		// **** 주문이 완료되었을시 세션에 저장되어져 있는 loginuser 정보를 갱신하고
	    //      이어서 주문이 완료되었다라는 email 보내주기 시작  **** //
		if(isSuccess == 1) {
			// 주문 완료 성공 시
			// 세션에 저장된 loginuser 정보를 갱신한다.(coin, point 새롭게 갱신된 내용 반영해야함.)
			loginuser.setCoin( loginuser.getCoin() - Integer.parseInt(sumtotalPrice) );
			loginuser.setPoint( loginuser.getPoint() + Integer.parseInt(sumtotalPoint) );

			///////////// == 주문이 완료되었다는 email 보내기 시작 == /////////////
			GoogleMail mail = new GoogleMail();

			// 주문한 제품이 무엇인지 알아와야 한다.
			StringBuilder sb = new StringBuilder();
			
			// 주문 된 제품 번호의 배열
			for(int i=0; i<pnumArr.length; i++) {
				sb.append("\'"+pnumArr[i]+"\',");
			/*
               tbl_product 테이블에서 select 시
               where 절에 in() 속에 제품번호가 들어간다.
               만약에 제품번호가 문자열(ex.VARCHAR2)로 되어있어서 반드시 홑따옴표(')가 필요한 경우에는 위와같이 해주면 된다.
            */				
			}// end of for---------------------------------
			
			String pnums = sb.toString().trim();
			// "6,3,1" 에서 -->  "'6','3','1'," 로 바뀐다.
			
			// 맨 뒤에 콤마(,) 를 제거하기 위함.
			pnums = pnums.substring(0, pnums.length()-1);
			// "'6','3','1'"   (콤마 사라짐)
			
			//	System.out.println("주문한 제품번호 pnums 확인용 :" + pnums);
			// 주문한 제품번호 pnums 확인용 : '6','3','1'
			
			// 메일을 발송하자. (DB 에서 주문한 제품에 대한 내용을 읽어와야 한다.)
			// 다시 DB에 보내자.
			List<ProductVO> jumunProductList = pdao.getJumunProductList(pnums);		// DB 에서 where 에 in() 절을 쓴다.
			// 주문한 제품에 대해 email 보내기 시, email 내용에 넣을 주문한 제품번호들에 대한 제품정보를 얻어오는 것.
			
			// jumunProductList 의 내용을 이메일에 보여주자.
			// StringBuilder sb 초기화하기 (앞에서 append 한 부분), HTML 로 쌓아주자.
			sb.setLength(0);	// 또는 sb = new StringBuilder();
			
			// 본인이 원하는 디자인에 따라 div 추가하기
			sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>"+odrcode+"</span><br/><br/>");
			sb.append("<주문상품><br/>");
			
			for(int i=0; i<jumunProductList.size(); i++) {		// jumunProductList.get(i) 은 ProductVO 이다.
				sb.append(jumunProductList.get(i).getPname()+"&nbsp;"+oqtyArr[i]+"개&nbsp;&nbsp;");		// ProductVO 에서 읽어오는 것 , oqtyjoin : 주문한 갯수
				sb.append("<img src='http://127.0.0.1:9090/MyMVC/images/"+jumunProductList.get(i).getPimage1()+"' />");	// 이미지를 보여야 한다.
				sb.append("<br/>");
			}// end of for---------------------------------------
			
			sb.append("<br/>이용해 주셔서 감사합니다.");
			
			String emailContents = sb.toString();	// String 타입으로 바꿔준다.
			
			// 이 이메일은 물건을 주문한 사람에게 발송해야 한다.
			mail.sendmail_OrderFinish(loginuser.getEmail(), loginuser.getName(), emailContents);	// 물건을 주문한 사람이 누구인지?(loginuser 의 email 정보)
			
			///////////// == 주문이 완료되었다는 email 보내기 끝 == /////////////
			
		}

		// **** 주문이 완료되었을시 세션에 저장되어져 있는 loginuser 정보를 갱신하고
	    //      이어서 주문이 완료되었다라는 email 보내주기 끝  **** //		
		
		String json = jsobj.toString();
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
