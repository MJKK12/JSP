package myshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import common.controller.AbstractController;
import myshop.model.*;

public class MallDisplayJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sname = request.getParameter("sname");	// "HIT" "NEW" "BEST"
		String start = request.getParameter("start");	 
		String len = request.getParameter("len");		
		/*
	        맨 처음에는 sname("HIT")상품을  start("1") 부터 len("8")개를 보여준다.
	        더보기... 버튼을 클릭하면  sname("HIT")상품을  start("9") 부터 len("8")개를 보여준다.
	        또  더보기... 버튼을 클릭하면  sname("HIT")상품을  start("17") 부터 len("8")개를 보여준다.      
		*/		
		
		InterProductDAO pdao = new ProductDAO();
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("sname", sname);	// "HIT" "NEW" "BEST" 인지를 봐야 한다.
		paraMap.put("start", start);	// start "1" "9"  "17" "25" "33"
										
		String end = String.valueOf((Integer.parseInt(start) + Integer.parseInt(len) - 1));	
										// end => start + len - 1;
										// end   "8" "16" "24" "32" "40"
		// Map 에 넣어야 하므로 정수타입을 --> 다시 String 타입으로 바꿔준다.
		
		paraMap.put("end", end);
		
		// VO 를 하나 만들자. (제품테이블이 들어있는 ProductVO ) () 안에는 where 절에 들어가야 할 것들이다. --> Map에 넣자.
		// paraMap 에 넣은 것들을 DB 로 보내자.
		List<ProductVO> prodList = pdao.selectBySpecName(paraMap);	// 복수개("8"개)가 나오므로 List<> 타입으로 받는다.
		
		// SQL 문의 결과물이 JSON 타입, 복수개로 나와야 한다.
		// JSONArray		
		JSONArray jsonArr = new JSONArray();	// simple 은 핸드폰 문자전송시에만 사용하고, 여기서는 .org를 import 한다.
		
		if(prodList.size() > 0) {		// DAO 에서 select 되어 온 것이 있는가
			// 확장 for 문을 사용한다.
			for(ProductVO pvo : prodList) {
				// json 타입, 즉 JavaScript 타입의 객체를 만든다.
				JSONObject jsonObj = new JSONObject();	// {} {} {} {} {} {} {} {}
														// {} {} {} {}
				// DAO 에서 select 된 값을 put 해온 것을 읽어오자.
				jsonObj.put("pnum", pvo.getPnum());
				jsonObj.put("pname", pvo.getPname());
				jsonObj.put("code", pvo.getCategvo().getCode());	// JOIN 해온 것
				jsonObj.put("pcompany", pvo.getPcompany());
	            jsonObj.put("pimage1", pvo.getPimage1());
	            jsonObj.put("pimage2", pvo.getPimage2());
	            jsonObj.put("pqty", pvo.getPqty());
	            jsonObj.put("price", pvo.getPrice());
	            jsonObj.put("saleprice", pvo.getSaleprice());
	            jsonObj.put("sname", pvo.getSpvo().getSname());	            
	            jsonObj.put("pcontent", pvo.getPcontent());
	            jsonObj.put("point", pvo.getPoint());
	            jsonObj.put("pinputdate", pvo.getPinputdate());
	            jsonObj.put("discountPercent", pvo.getDiscountPercent());     // product 에는 할인율이 얼마인지 알려주는 메소드가 필요하다. (이미지 참고) // JSTL 사용 고려 / 항상 VO 에서는 getXXX 를 한다.
	            
	            // jsonObj ==> {"pnum":1, "pname":"스마트TV", "code":"100000", "pcompany":"삼성",....... "pinputdate":"2021-04-23", "discoutPercent":15} 
	            // jsonObj ==> {"pnum":2, "pname":"노트북", "code":"100000", "pcompany":"엘지",....... "pinputdate":"2021-04-23", "discoutPercent":10}
	            
	            // jsonArray 에 담자.       
	            jsonArr.put(jsonObj);
	            /*
	               [ {"pnum":1, "pname":"스마트TV", "code":"100000", "pcompany":"삼성",....... "pinputdate":"2021-04-23", "discoutPercent":15} 
	                ,{"pnum":2, "pname":"노트북", "code":"100000", "pcompany":"엘지",....... "pinputdate":"2021-04-23", "discoutPercent":10} 
	                ,{....}
	                ,{....}
	                , .....
	                ,{....} 
	               ] 
	            */	            
	            
			}// end of for---------------------------------
		
			// jsonArr를 문자열로 변환한다.
			String json = jsonArr.toString();
			
		//	System.out.println("확인용 json => " + json);			
			/* 확인용 json => 
			  
				[{"pnum":36,"code":"100000","discountPercent":17,"pname":"노트북30","pcompany":"삼성전자","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"59.jpg","pqty":100,"pimage2":"60.jpg","pcontent":"30번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":35,"code":"100000","discountPercent":17,"pname":"노트북29","pcompany":"레노버","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"57.jpg","pqty":100,"pimage2":"58.jpg","pcontent":"29번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":34,"code":"100000","discountPercent":17,"pname":"노트북28","pcompany":"아수스","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"55.jpg","pqty":100,"pimage2":"56.jpg","pcontent":"28번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":33,"code":"100000","discountPercent":17,"pname":"노트북27","pcompany":"애플","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"53.jpg","pqty":100,"pimage2":"54.jpg","pcontent":"27번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":32,"code":"100000","discountPercent":17,"pname":"노트북26","pcompany":"MSI","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"51.jpg","pqty":100,"pimage2":"52.jpg","pcontent":"26번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":31,"code":"100000","discountPercent":17,"pname":"노트북25","pcompany":"삼성전자","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"49.jpg","pqty":100,"pimage2":"50.jpg","pcontent":"25번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":30,"code":"100000","discountPercent":17,"pname":"노트북24","pcompany":"한성컴퓨터","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"47.jpg","pqty":100,"pimage2":"48.jpg","pcontent":"24번 노트북","price":1200000,"sname":"HIT"}
				,{"pnum":29,"code":"100000","discountPercent":17,"pname":"노트북23","pcompany":"DELL","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"45.jpg","pqty":100,"pimage2":"46.jpg","pcontent":"23번 노트북","price":1200000,"sname":"HIT"}]

			*/
			
			// 위의 json 을 웹페이지에 찍어주자. (jsp 로 넘긴다.)
			request.setAttribute("json", json);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");			
		}
		
		else {
			// DB 에서 조회된 것이 없다면
			String json = jsonArr.toString();	// 문자열로 변환
		
		// *** 만약 select 된 정보가 없다면 []로 나오므로 null 이 아닌 요소가 없는 빈 배열이다. *** //	
		//	System.out.println("*** 확인용 json => " + json);		
		//  우리는 HIT 값만을 불러오려고 했는데, 검색창에서 BEST 를 입력했을 경우 아래와 같이 담긴다.
		//	*** 확인용 json => []	

		// 위의 json 을 웹페이지에 찍어주자. (jsp 로 넘긴다.)
			request.setAttribute("json", json);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");			
		}		
		
	}

}
