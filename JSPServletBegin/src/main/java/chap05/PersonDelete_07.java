package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personDelete.do")
public class PersonDelete_07 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private interPersonDAO_03 dao = new PersonDAO_04();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getMethod();	//"GET" 또는 "POST"
		
		String path = "";		// 경로 입력
		
		if("POST".equalsIgnoreCase(method)) {
			// 정상적인 POST 방식으로 들어오면 (삭제는 method=post 를 써야한다.)
			
			String seq = request.getParameter("seq");	// DB의 where 절로 간다.
			String name = request.getParameter("name");	// DB의 where 절로 간다.
			
		//	System.out.println("확인용 제거할 seq =>" + seq);
		//	System.out.println("확인용 제거할 name =>" + name);
			
			try {
				int n = dao.deletePerson(seq);	// DML 문이므로 int 가 리턴타입 (1개행이 삭제)
								
				if(n==1) {	// 정상이라면	(삭제 성공)				
					String delInfo = "회원번호" +seq+" 번 "+name+" 님을 삭제했습니다.";
					request.setAttribute("delInfo", delInfo);
					path = "/WEB-INF/chap05_ok/personDelete_success.jsp";
				}
				
			} catch (SQLException e) {	// 삭제 실패 시
				e.printStackTrace();
				path = "/WEB-INF/chap05_ok/personRegister_fail.jsp";
			}	
			
		}
		else {	
			// 비정상적인 GET 방식으로 들어오면
			path = "/WEB-INF/chap05_ok/personDetail_funStop.jsp";	// 해당 jsp 파일을 원래 새로 또 만들어줘야 한다.
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
