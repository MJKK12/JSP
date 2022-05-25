package myshop.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class DeliverStartAction extends AbstractController {

	// 관리자가 주문목록에 있는 제품들에 대해 배송 처리를 할 때. (배송하기 버튼 클릭)
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
	      
	      if(!"POST".equalsIgnoreCase(method)) {
	         String message = "비정상적인 경로로 들어왔습니다.";
	         String loc = "javascript:history.back();";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         
	         return; // 종료 
	      }				
	      
	      // === 로그인 유무 검사하기 === //
	      boolean isLogIn = super.checkLogin(request);
	      
	      if(!isLogIn) {
	         request.setAttribute("message", "배송하기를 처리하려면 먼저 로그인 부터 하세요!!");
	         request.setAttribute("loc", "javascript:history.back()"); 
	         
	      //   super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         return;
	      }	
	      else {
	          HttpSession session = request.getSession();
	          
	          MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
	          String userid = loginuser.getUserid();
	          
	          if(!"admin".equals(userid) ) {
	             String message = "접근불가!! 관리자가 아닙니다.";
	             String loc = "javascript:history.back()";
	             
	             request.setAttribute("message", message);
	             request.setAttribute("loc", loc);
	             
	             super.setRedirect(false);
	             super.setViewPage("/WEB-INF/msg.jsp");
	             
	             return;
	          }
	          
	          else {
					// "admin" 으로 로그인 한 경우(관리자)라면 --> 올바른 접근
	        	  	// 체크박스에 체크된 것이 복수개 이므로 getParameterValues (전표가 복수개 이므로 배열[])
	        	  String[] odrcodeArr =	request.getParameterValues("odrcode");	// name 값을 가져온다.
	        	  String[] pnumArr = request.getParameterValues("pnum");	// name 값을 가져온다.
	        	  
	        	  StringBuilder sb = new StringBuilder();
	        	  /*
	        	    's20220414-11/59','s20220412-9/6','s20220412-9/4','s20220412-6/5','s20220412-6/4'
	        	    's20220414-11 는 주문코드(전표)' 이고 / 뒤에 붙은 59 는 제품번호이다.
	        	    이것은 오라클에서 주문코드(전표)컬럼 ||'/'|| 제품번호컬럼으로 사용하기위한 데이터 값을 만드려고 한다.
	        	   
	        	   */
	        	  for(int i=0; i<odrcodeArr.length; i++) {
	        		  
	        		  sb.append("\'"+odrcodeArr[i]+"/"+pnumArr[i]+"\',");		// in절
	        		  // sql 문의 where 절에 fk_odrcode || '/' || fk_pnum in('전표/제품번호','전표/제품번호','전표/제품번호') 을 사용하기 위한 것이다. 
	        	  }// end of for-----------------------------------------
	        	  
	        	  String odrcodePnum = sb.toString();
	        	  
	        	  // 맨 뒤의 콤마(,) 제거하기
	        	  odrcodePnum = odrcodePnum.substring(0,odrcodePnum.length()-1);
	        	  
	        	// System.out.println("확인용 odrcodePnum = > "+odrcodePnum);
	        	// 확인용 odrcodePnum = > 's20220414-11/59','s20220412-9/6','s20220412-9/4','s20220412-6/5','s20220412-6/4'
	        	// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 2(배송시작)로 변경하기
	              InterProductDAO pdao = new ProductDAO(); 
	              
	              int n = 0;	// update 가 몇개 되었는지 본다.
	              
	              try {
	            	  n = pdao.updateDeliverStart(odrcodePnum);		// 배송시작 메소드 (배송상태를 1-->2로 Update)
	            	  
	            	  if(n==odrcodeArr.length) {	// 배송상태 업데이트 된 행의 갯수 == 전표번호의 갯수
	            		  // == *** 배송을 했다는 확인문자(SMS)를 주문한 사용자들(여러명)에게 보내기 시작 *** == //
	            		  
	            		  // 동일한 전표에 서로 다른 제품들을 구매한 경우 동일 전표를 가진 사람에게는 SMS(문자)를 1번만 보내야 하므로 (제품을 10개 구매했다고 10번 보낼 순 없음.)
	                      // 중복을 허락치 않는 HashMap 을 사용하기로 한다. (Map 은 덮어씌우는 것이므로 사용.)
	            		  Map<String, String> odrcodeMap = new HashMap<>();
	            		  
	            		  for(String odrcode : odrcodeArr) {
	            			  odrcodeMap.put(odrcode, odrcode);
	            			  // odrcodeMap 에 전표를 넣기(HashMap 이므로 중복된 전표가 있으면 덮어씌우므로 고유한 값만 존재하게 된다).
	            			  // for( A : B ) for문이라 반복문, B에서 차례대로 객체를 꺼내서 A에다가 넣겠다는 것이다.
	            			  
	            		  }// end of for--------------------------------
	            		  
	            		  Set<String> odrcodeMapKeySets = odrcodeMap.keySet();
	            		  // 중복을 허락하지 않는 키값들 얻어오기
	            		  
	            		  for(String key : odrcodeMapKeySets) {	// key:전표번호
	            			  	// 중복되지 않은 key 들에 문자 보내기. 현재 key 에는 전표가 들어 있음. 우리는 핸드폰번호를 알아야 문자발송을 할 수 있다.
	            			  	// 전표번호만 알면 사용자에 대한 정보를 조회해올 수 있는 메소드.
	          		  
	            			    MemberVO mvo = pdao.odrcodeOwnerMemberInfo(key);	// 중복 문자발송을 방지하기 위해 전표번호인 key 를 넣는다. (누구인지 알아야 하기 때문에 memberVO)
	            			  	// 영수증전표(odrcode)소유주에 대한 사용자 정보를 조회해오는 것.
	            			  
	            			//    System.out.println("~~~ 확인용 key : " + key);
	            			//    System.out.println("~~~ 확인용 mvo : " + mvo);
	            			    
	            				// =================== *** SMS 발송 (WEB 발신) *** =================== // 
	            				// HashMap 에 받는 사람 번호, 보내는 사람 번호, 문자 내용 등을 저장한 뒤 Message 클래스 객체의 send 메소드를 이용해 문자를 보낸다.
	            				
	            				//	String api_key = "발급받은 본인의 API Key";  // 발급받은 본인 API Key
	            				String api_key = "NCS0XLT83BP6BZNR";
	            				
	            				// String api_secret = "발급받은 본인의 API Secret";  // 발급받은 본인 API Secret
	            				String api_secret =	"DWXXDOJKYNMXH6LH3XPJYLXONR4JFPO9";	
	            				
	            				// coolsms 사이트 참고할 것 (도움말)
	            				Message coolsms = new Message(api_key, api_secret);
	            				// net.nurigo.java_sdk.api.Message 임. 
	            			    // 먼저 다운 받은  javaSDK-2.2.jar 를 /MyMVC/src/main/webapp/WEB-INF/lib 안에 넣어서 build 시켜야 함.   

	            				// == 4개 파라미터(to, from, type, text)는 필수사항이다. ==
	            				HashMap<String, String> paraMap = new HashMap<>();
	            				// 여기서 K,V 값은 coolsms 에서 정해준 대로 써야한다. --> map 에 넣어주자
	            				paraMap.put("to", mvo.getMobile());		// 수신번호 (memberVO에서 꺼내오자.)
	            				paraMap.put("from", "01093304522");		// 발신번호에 등록된 번호만 넣을 수 있다.
	            				// 2020년 10월 16일 이후로 발신번호 사전등록제로 인해 등록된 발신번호로만 문자를 보낼 수 있다.
	            				paraMap.put("type", "SMS"); // Message type ( SMS(단문), LMS(장문), MMS, ATA )
	            				paraMap.put("text", "MyMVC 쇼핑몰에서 "+mvo.getName()+" 님께서 주문하신 전표["+key+"]를 우체국택배로 배송했습니다.");	// 문자 내용
	            				paraMap.put("app_version", "JAVA SDK v2.2"); // application name and version
	            			      
	            		      //    ==  아래의 파라미터는 필요에 따라 사용하는 선택사항이다. == 
	            		      //   paraMap.put("mode", "test"); // 'test' 모드. 실제로 발송되지 않으며 전송내역에 60 오류코드로 뜹니다. 차감된 캐쉬는 다음날 새벽에 충전 됩니다.
	            		      //   paraMap.put("image", "desert.jpg"); // image for MMS. type must be set as "MMS"
	            		      //   paraMap.put("image_encoding", "binary"); // image encoding binary(default), base64 
	            		      //   paraMap.put("delay", "10"); // 0~20사이의 값으로 전송지연 시간을 줄 수 있습니다.
	            		      //   paraMap.put("force_sms", "true"); // 푸시 및 알림톡 이용시에도 강제로 SMS로 발송되도록 할 수 있습니다.
	            		      //   paraMap.put("refname", ""); // Reference name
	            		      //   paraMap.put("country", "KR"); // Korea(KR) Japan(JP) America(USA) China(CN) Default is Korea
	            		      //   paraMap.put("sender_key", "5554025sa8e61072frrrd5d4cc2rrrr65e15bb64"); // 알림톡 사용을 위해 필요합니다. 신청방법 : http://www.coolsms.co.kr/AboutAlimTalk
	            		      //   paraMap.put("template_code", "C004"); // 알림톡 template code 입니다. 자세한 설명은 http://www.coolsms.co.kr/AboutAlimTalk을 참조해주세요. 
	            		      //   paraMap.put("datetime", "20140106153000"); // Format must be(YYYYMMDDHHMISS) 2014 01 06 15 30 00 (2014 Jan 06th 3pm 30 00)
	            		      //   paraMap.put("mid", "mymsgid01"); // set message id. Server creates automatically if empty
	            		      //   paraMap.put("gid", "mymsg_group_id01"); // set group id. Server creates automatically if empty
	            		      //   paraMap.put("subject", "Message Title"); // set msg title for LMS and MMS
	            		      //   paraMap.put("charset", "euckr"); // For Korean language, set euckr or utf-8
	            		      //   paraMap.put("app_version", "Purplebook 4.1") // 어플리케이션 버전
	            				
	            				try {
	            				// 문자를 발송한다.(send())	
	            				JSONObject jsonObj = (JSONObject)coolsms.send(paraMap);		// parameter 에 map 이 무조건 들어와야 한다. return 타입이 jsonObject 이므로, 형변환 해준다.
	            				// 주의 ! import 할 떄 import org.json.simple.JSONObject; 로 할 것!!
	            				/*
	            				    org.json.JSONObject 이 아니라 
	            				    org.json.simple.JSONObject 이어야 한다.  
	            				*/
	            				} catch (CoolsmsException e) {
	            					e.printStackTrace();		// 문자발송 시 어떤 문제가 발생했는지 찍어본다.
								}
	            		  }// end of for--------------------------------------	            		  
	            		  
	            		  // == *** 배송을 했다는 확인문자(SMS)를 주문한 사용자들(여러명)에게 보내기 끝 *** == //
	            		  
	            		  String message = "선택하신 제품들은 배송시작으로 변경되었습니다.";
	                      String loc = request.getContextPath()+"/shop/orderList.up";
	                      
	                      request.setAttribute("message", message);
	                      request.setAttribute("loc", loc);
	                      
	                      super.setRedirect(false);
	                      super.setViewPage("/WEB-INF/msg.jsp");
	            		  
	            	  }
	            	  else {
	            		  
	            		  String message = "선택하신 제품들은 배송시작으로 변경이 실패되었습니다.";
	                      String loc = "javascript:history.back();";
	                      
	                      request.setAttribute("message", message);
	                      request.setAttribute("loc", loc);
	                      
	                      super.setRedirect(false);
	                      super.setViewPage("/WEB-INF/msg.jsp");
	                      
	            	  }
	              
	              } catch (SQLException e) {
					  e.printStackTrace();
					  
					   String message = "선택하신 제품들은 배송시작으로 변경이 실패되었습니다.";
		               String loc = "javascript:history.back();";
		               
		               request.setAttribute("message", message);
		               request.setAttribute("loc", loc);
		               
		               super.setRedirect(false);
		               super.setViewPage("/WEB-INF/msg.jsp");
				  }
	              
	          }
	      
		  }
	      
	}

}
