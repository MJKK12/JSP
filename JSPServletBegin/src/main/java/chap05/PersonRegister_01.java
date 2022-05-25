package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 회원가입
@WebServlet("/personRegister.do")
public class PersonRegister_01 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private interPersonDAO_03 dao = new PersonDAO_04();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getMethod();	// "GET" 또는 "POST" 인지 알려주는 것
		System.out.println("확인용 method : " + method);	// 콘솔창에서 확인
	
		if("GET".equals(method)) {	
			// 넘어온 것이 GET 방식이면,	view 단 페이지를 보여준다.
			// 넘어온 것이 GET 방식이면,	http://localhost:9090/JSPServletBegin/personRegister.do 를 하면 "개인성향 데이터를 DB로 전송하기" 페이지(form 태그 페이지)가 나오도록 한다.
			// 서블릿에서 jsp 페이지로 이동할 때 처리하는 방법
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/chap05_ok/personRegister.jsp");	// jsp 파일
			dispatcher.forward(request, response);
		}
		else {
			// 넘어온 것이 POST 방식이면
			// http://localhost:9090/JSPServletBegin/personRegister.do 를 하면 submit 된 데이터를 받아서 DB로 보내야 한다.
			String name = request.getParameter("name");
			String school = request.getParameter("school");
			String color = request.getParameter("color");
			String[] foodArr = request.getParameterValues("food");			
			
			// 위 data 들을 DTO 에 전송한다. (DTO 에 넣는다.)
			personDTO_02 psdto = new personDTO_02();
			psdto.setName(name);
			psdto.setSchool(school);
			psdto.setColor(color);
			psdto.setFood(foodArr);		// 배열
			
			// 이것들을 실제로 DB 에 보내야 한다. (DAO 가 필요하다.)
			// DAO 는 인터페이스로 만든다.
			
			String pathName = "";	// view 페이지의 경로
			
			try {
				int n = dao.personRegister(psdto);
				// int n 에는 form 태그에 입력한 이름, 학력, 색상, 음식이 설정되어 있다. 
				if(n==1) {	// 정상
					pathName = "/WEB-INF/chap05_ok/personRegister_success.jsp";	// 성공 시 보여줄 view 페이지
				}
				
			} catch (SQLException e) {	// 에러 발생
				e.printStackTrace();
				pathName = "/WEB-INF/chap05_ok/personRegister_fail.jsp";		// 실패 시 보여줄 view 페이지
			}	
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(pathName);	// dao 메소드 결과물에 따라서 보여줄 내용	
			dispatcher.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
