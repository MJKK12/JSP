package chap02;

import java.io.IOException;
import java.io.PrintWriter;

import javax.print.attribute.standard.MediaSize.NA;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMethod_01 extends HttpServlet {

	private static final long serialVersionUID = 1L;	// GetMethod 노란줄 방지용..
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		System.out.println("~~~ 확인용 : doGet 메소드가 호출됨");
		
		// HttpServletRequest request 객체는 전송된 데이터를 처리해주는 용도로 쓰인다.
		String name = request.getParameter("name");
		String school = request.getParameter("school");
		String color = request.getParameter("color");
		
		String[] arrfood = request.getParameterValues("food");	// checkbox 는 복수개 이므로 .getParameterValues 를 쓴다. --> 배열[] 에 담아져 나온다. (food 여러개 선택)
		String food = "";
		
		if(arrfood != null) {
			food = String.join(",", arrfood);	// 음식을 최소 1개 이상 선택하면 null 이 아니다.
		}
		else {	// null 값. (선택하지 않았음 (좋아하는 음식이 1개도 없을 경우.
			food = "좋아하는 음식이 없습니다.";
		}
		
		// 콘솔에 출력하기 시작 //
		System.out.println("name => "+ name);	
		System.out.println("school => "+ school);	
		System.out.println("color => "+ color);	
		System.out.println("food => "+ food);	
		
	/*	
	   // === 8. String.join("구분자", 문자열배열명) ===	★잘 기억할 것!★
	   //        문자열배열을 "구분자"로 합쳐서 String 타입으로 돌려주는 것이다.
	   String[] nameArr = {"한석규","두석규","세석규","네석규","오석규"};
	   String names = String.join("-", nameArr);   // "-"로 이어주는 것임. return 타입은 String 타입이 나온다.
	   System.out.println(names);
	   // "한석규-두석규-세석규-네석규-오석규"
	*/	

		// 콘솔에 출력하기 끝 //

		
		// 웹브라우저에 출력하기 시작 //
		// HttpServletResponse response 객체는 넘어온 데이터를 조작해서 결과물을 나타내고자 할 때 쓰인다.
		response.setContentType("text/html; charset=UTF-8"); // content 타입을 셋팅한다.
		
		PrintWriter out = response.getWriter();
		// out 은 웹브라우저에 기술하는 대상체라고 생각하자.
		
		out.println("<html>");
		out.println("<head><title>개인 성향 테스트 결과화면</title></head>");
		out.println("<body>");
		out.println("<h2>개인 성향 테스트 결과(GET)</h2>");
		out.printf("<span style='color:orange; font-weight:bold;'>%s</span> 님의 개인 성향은<br/><br/>", name);
		
		if(color != null) {
			out.printf("학력은 %s이며, %s색을 좋아합니다.<br/><br/>", school, color);
		}
		else {
			out.printf("학력은 %s이며, 좋아하는 색이 없습니다.", school);
		}
		/*
			 %d ==> decimal 정수
			 %f ==> float 실수
			 %s ==> string 문자열
		*/
		
		out.println(" 좋아하는 음식은 ?" + food);
		
		out.println("</body>");
		out.println("</html>");
		
		// 웹브라우저에 출력하기 끝 //
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("*** 확인용 : doPost 메소드가 호출됨");

	}

}
