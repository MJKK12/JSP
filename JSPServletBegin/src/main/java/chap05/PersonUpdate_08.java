package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personUpdate.do")
public class PersonUpdate_08 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private interPersonDAO_03 dao = new PersonDAO_04();	// field 에 dao 를 만들고 기본생성자를 준 것이다. → 기본생성자가 움직인다.
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getMethod();	//"GET" 또는 "POST"
		
		String path = "";		// 경로 입력
		
		if("POST".equalsIgnoreCase(method)) {
			// 정상적인 POST 방식으로 들어오면 (삭제는 method=post 를 써야한다.)
			
			String seq = request.getParameter("seq");	// DB의 where 절로 간다.
			
		//	System.out.println("확인용 제거할 seq =>" + seq);
			
			try {	// POST 방식일때.
				personDTO_02 psdto = dao.selectOne(seq);	// ① 그 사람에 대한 정보를 먼저 보여준다. (db에서 psdto를 읽어와서, 아래의 view 단 페이지로 넘겨준것이다.)
				request.setAttribute("psdto", psdto);		// 자신의 정보를 보여줘야 한다.
				path = "/WEB-INF/chap05_ok/personUpdate.jsp";
									
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
