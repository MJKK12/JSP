package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// === 로그인 유무 검사하기 === //
	      boolean isLogIn = super.checkLogin(request);
	      
	      if(!isLogIn) {	// 로그인 하지 않았을 때
	         request.setAttribute("message", "주문내역을 조회하려면 먼저 로그인 부터 하세요!!");
	         request.setAttribute("loc", "javascript:history.back()"); 
	         
	      //   super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         return;
	      }
	      else { // 로그인 했을 때
	    	  // *** 페이징 처리한 주문 목록 보여주기 *** //
	    	  InterProductDAO pdao = new ProductDAO();
	    	  
	    	  // 1. 페이징 처리를 위해 1페이지당 보여줄 장바구니에 들어간 제품의 갯수를 몇개로 할 것인가를 정한다.
              //    한 페이지당 보여줄 제품의 갯수는 10개로 한다.	    	  
	    	  int sizePerPage = 10;	
	    	  
	    	  // 2. 페이징 처리한 데이터 조회 결과물 가져오기
	    	  int totalPage = 0;
	    	  
	    	  //  -- 주문내역에 들어있는 제품의 총갯수 구하기
	          // *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 조회를 해오고, 
	          //     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 조회해온다.
	    	  HttpSession session = request.getSession();
	          MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
	          String userid = loginuser.getUserid(); 	// 고유한 userid (로그인된 사용자가 누구인지 알아오자)
	          
	          // *** 주문내역에 대한 페이징 처리를 위해 주문 갯수를 알아오기 위한 것으로
	          //     관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 갯수만 알아오고,
	          //     관리자로 로그인을 했을 경우에는 모든 사용자들이 주문한 갯수를 알아온다.
	          int totalCountOrder = pdao.getTotalCountOrder(userid);

	          totalPage = (int) Math.ceil( (double)totalCountOrder/sizePerPage );
              //                       17.0/10 = 1.7(double)  ==> 2.0(ceil)  ==> 2(int, 소수부절삭)
              //                       20.0/10 = 2.0 ==> 2.0  ==> 2 
     
	          // System.out.println("~~~ 확인용 totalPage : " + totalPage);
	          
	          //   -- 조회하고자 하는 페이지번호를 받아와야 한다.
	          String str_currentShowPageNo = request.getParameter("currentShowPageNo"); 
	                   
	          int currentShowPageNo = 0;
	                   
	          try {
	               if(str_currentShowPageNo == null) {
	                 currentShowPageNo = 1; 
	               }
	               else {
	                 currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
	                  
	                 if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
	                	 // 현재 선택한 페이지가 1 미만의 페이지 이던지, 전체 페이지 갯수 이상의 페이지 넘버로 이동했을 때 (사용자가 장난침)
	                    currentShowPageNo = 1;
	                 }
	               }
	          } catch (NumberFormatException e) {
	        	  // 사용자가 현재 페이지를 숫자 이외의 것을 입력했을 때 1페이지로 보여준다. (ex. 문자열 입력)
	                  currentShowPageNo = 1;
	          }
	          // *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고,
	          //     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.	
	          // 굳이 VO 만들 필요 없이 map 으로 쓴다.
	          List<Map<String, String>> orderList = pdao.getOrderList(userid, currentShowPageNo, sizePerPage);	// where 절에 누구인지를 알아야 한다. (userid, 로그인된 유저 --> admin 또는 일반회원)

	          
	          
	          // *********** 페이지바 시작 *********** //
	          
	          //  -- 페이지바 만들기 
	          String url = "orderList.up";	// 상대경로
	          int blockSize = 10;
	          //  blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 갯수이다.
	                   
		       //        1 2 3 4 5 6 7 8 9 10 [다음][마지막]                -- 1개 블럭
		       //  [처음][이전] 11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개 블럭
		       //  [처음][이전] 21 22                                      -- 1개 블럭 
	          
	          int loop = 1;
	          // loop 는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 갯수(위의 설명상 지금은 10(==blockSize)까지만 증가하는 용도이다.
	          
	          int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1; 
	          // *** !! 공식이다. !! *** // 
	          /*
	              1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은 1 이다. 
	              11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.
	              21 22                          -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다. 
	          
	              currentShowPageNo    pageNo
	              ------------------------------------------------
	                 1           ((1 - 1)/10) * 10 + 1 ==> 1
	                 2           ((2 - 1)/10) * 10 + 1 ==> 1
	                 3           ((3 - 1)/10) * 10 + 1 ==> 1 
	                 4           ((4 - 1)/10) * 10 + 1 ==> 1
	                 5           ((5 - 1)/10) * 10 + 1 ==> 1
	                 6           ((6 - 1)/10) * 10 + 1 ==> 1 
	                 7           ((7 - 1)/10) * 10 + 1 ==> 1 
	                 8           ((8 - 1)/10) * 10 + 1 ==> 1
	                 9           ((9 - 1)/10) * 10 + 1 ==> 1
	                 10          ((10 - 1)/10) * 10 + 1 ==> 1
	                 
	                 11          ((11 - 1)/10) * 10 + 1 ==> 11  
	                 12          ((12 - 1)/10) * 10 + 1 ==> 11
	                 13          ((13 - 1)/10) * 10 + 1 ==> 11
	                 14          ((14 - 1)/10) * 10 + 1 ==> 11
	                 15          ((15 - 1)/10) * 10 + 1 ==> 11
	                 16          ((16 - 1)/10) * 10 + 1 ==> 11
	                 17          ((17 - 1)/10) * 10 + 1 ==> 11
	                 18          ((18 - 1)/10) * 10 + 1 ==> 11
	                 19          ((19 - 1)/10) * 10 + 1 ==> 11 
	                 20          ((20 - 1)/10) * 10 + 1 ==> 11
	                 
	                 21          ((21 - 1)/10) * 10 + 1 ==> 21
	                 22          ((22 - 1)/10) * 10 + 1 ==> 21
	           */
	          String pageBar = "";
	          
	          // *** [맨처음][이전] 만들기 *** // 
	          if(pageNo != 1) {
	             pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?currentShowPageNo=1'>[맨처음]</a></li>";
	             pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
	          }
	          
	          while( !(loop > blockSize || pageNo > totalPage) ) {
	             
	             if(pageNo == currentShowPageNo) {
	                pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";  
	             }
	             else {
	                pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
	             }
	             
	             loop++;
	             pageNo++;
	          }// end of while-------------------------------------------------
	          
	          // *** [다음][마지막] 만들기 *** // 
	          if( pageNo <= totalPage ) {
	             pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?currentShowPageNo="+pageNo+"'>[다음]</a></li>";
	             pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
	          }
	          
	          // *********** 페이지바 끝 *********** //
	          
	          
	          request.setAttribute("orderList", orderList);	          
	          request.setAttribute("pageBar", pageBar);

	       // super.setRedirect(false);
	          super.setViewPage("/WEB-INF/myshop/orderList.jsp");
		}
	
	}

}
