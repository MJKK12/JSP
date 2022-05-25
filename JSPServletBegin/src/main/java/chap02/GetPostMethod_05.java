package chap02;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* === 중요 ===
	확장자가 .xml 또는 .java 인 파일에서 URL경로를 나타낼때 맨 앞에 / 가 오면
	그 앞에는  http://ip주소:포트번호/컨텍스트패스명 이 자동으로 붙게 된다.
	우리의 컨텍스트 패스명은  /JSPServletBegin 이다.      
	즉, 우리는  http://localhost:9090/JSPServletBegin/05_getMethod.do 으로 된다. 
*/
@WebServlet("/05_getPostMethod.do")
public class GetPostMethod_05 extends HttpServlet {

	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/*
        post 방식으로 넘어온 데이터중 영어는 글자가 안깨지지만,
        한글은 글자모양이 깨져나온다.
        그래서  post 방식에서 넘어온 한글 데이터가 글자가 안깨지게 하려면 
        아래처럼 request.setCharacterEncoding("UTF-8"); 을 해야 한다.
        주의할 것은 request.getParameter("변수명"); 보다 먼저 기술을 해주어야 한다는 것이다.      
		*/
		
		request.setCharacterEncoding("UTF-8");	// post 방식에서 넘어온 데이터는 한글로 바꿀 수 있다.
		
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
		
		// *** 클라이언트(form 태그가 있는 .jsp 파일)에서 넘어온 method 방식이 GET 인지 POST 인지 알아오기.
		String method = request.getMethod();	//	GET 또는 POST
		
		// HttpServletResponse response 객체는 넘어온 데이터를 조작해서 결과물을 나타내고자 할 때 쓰인다.
		response.setContentType("text/html; charset=UTF-8"); // content 타입을 셋팅한다.
		
		PrintWriter out = response.getWriter();
		// out 은 웹브라우저에 기술하는 대상체라고 생각하자.
		
		out.println("<html>");
		out.println("<head><title>개인 성향 테스트 결과화면</title></head>");
		out.println("<body>");
		out.println("<h2>개인 성향 테스트 결과("+method+")</h2>");
		out.printf("<span style='color:pink; font-weight:bold;'>%s</span> 님의 개인 성향은<br/><br/>", name);
		
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
		
		out.println(" 좋아하는 음식은 ? " + food);
		
		out.println("</body>");
		out.println("</html>");
		
		// 웹브라우저에 출력하기 끝 //	

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
