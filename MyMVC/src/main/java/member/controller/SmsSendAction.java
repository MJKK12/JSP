package member.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import net.nurigo.java_sdk.api.Message;

public class SmsSendAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// *** SMS 발송 (WEB 발신) *** // 
		// HashMap 에 받는 사람 번호, 보내는 사람 번호, 문자 내용 등을 저장한 뒤 Message 클래스 객체의 send 메소드를 이용해 문자를 보낸다.
		
		//	String api_key = "발급받은 본인의 API Key";  // 발급받은 본인 API Key
		String api_key = "NCS0XLT83BP6BZNR";
		
		// String api_secret = "발급받은 본인의 API Secret";  // 발급받은 본인 API Secret
		String api_secret =	"DWXXDOJKYNMXH6LH3XPJYLXONR4JFPO9";	
		
		// coolsms 사이트 참고할 것 (도움말)
		Message coolsms = new Message(api_key, api_secret);
		// net.nurigo.java_sdk.api.Message 임. 
	    // 먼저 다운 받은  javaSDK-2.2.jar 를 /MyMVC/src/main/webapp/WEB-INF/lib 안에 넣어서 build 시켜야 함.   
		   
		// 받는 사람의 전화번호와 내용이 들어와야 한다.
		// memberOneDetail.jsp js에서 getParameter 해오자.
		String mobile = request.getParameter("mobile");			// 받는 사람의 번호
		String smsContent = request.getParameter("smsContent");	// 발송 내용
		String datetime = request.getParameter("datetime");   	// 발송 시간
		   
		// datetime 의 경우, 설정을 할 수도 있고 하지 않고 바로 발송하는 경우의 수가 있다.
		// 따라서 2가지 경우의 수를 생각한다.
		
		// == 4개 파라미터(to, from, type, text)는 필수사항이다. ==
		HashMap<String, String> paraMap = new HashMap<>();
		// 여기서 K,V 값은 coolsms 에서 정해준 대로 써야한다. --> map 에 넣어주자
		paraMap.put("to", mobile);			// 수신번호
		paraMap.put("from", "01093304522");	// 발신번호에 등록된 번호만 넣을 수 있다.
		// 2020년 10월 16일 이후로 발신번호 사전등록제로 인해 등록된 발신번호로만 문자를 보낼 수 있다.
		paraMap.put("type", "SMS"); // Message type ( SMS(단문), LMS(장문), MMS, ATA )
		paraMap.put("text", smsContent);	// 문자 내용
		
		// 예약전송을 위해 if 문을 사용한다.
		if(datetime != null) {
			// 발송 시간이 null 이 아니라면, (즉 예약전송일을 지정한 것임) map 에 datetime 까지 넣어준다.
			// 예약전송시 날짜 설정 반드시 YYYYMMDDHHMISS 형식대로 해야함. (날짜 사이에 '-' 가 들어가면 안됨.)
		//	paraMap.put("datetime", "202204071418");	// 예약전송시 날짜 설정 반드시 YYYYMMDDHHMISS 형식대로 해야함.
			paraMap.put("datetime", datetime);			// 예약전송시 날짜 설정 반드시 YYYYMMDDHHMISS 형식대로 해야함.
		}
	
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
		
		// 문자를 발송한다.(send())	
		JSONObject jsonObj = (JSONObject)coolsms.send(paraMap);		// parameter 에 map 이 무조건 들어와야 한다. return 타입이 jsonObject 이므로, 형변환 해준다.
		// 주의 ! import 할 떄 import org.json.simple.JSONObject; 로 할 것!!
		/*
		    org.json.JSONObject 이 아니라 
		    org.json.simple.JSONObject 이어야 한다.  
		*/
		
		String json = jsonObj.toString();
		
	//	System.out.println("발송 확인용 json : " + json);
		// 발송 확인용 json : {"group_id":"R2GlpTxiYT9TvhdQ","success_count":1,"error_count":0}
		
		request.setAttribute("json", json);		// jsonview.jsp 에서 key 값을 json 으로 설정해두었다.
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
