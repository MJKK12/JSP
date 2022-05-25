package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 회원 한 사람을 선택했을 때 보여주는 결과 (개인정보를 보여주는 부분)
@WebServlet("/personDetail.do")
public class PersonDetail_06 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private interPersonDAO_03 dao = new PersonDAO_04();	// field 에 dao 를 만들고 기본생성자를 준 것이다. → 기본생성자가 움직인다.
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// DB 에서 select 해 간다. 누구인지를 먼저 알아야 한다.
		String seq = request.getParameter("seq");	// 회원번호를 가져왔다.
		// DB 에서 select 한다.
	
		String path = "";
		
		try {
			personDTO_02 psdto = dao.selectOne(seq);	// 한개만 읽어오는 메소드 (한행만 읽어온다.)		
			request.setAttribute("psdto", psdto);		// 정보를 request 영역에 psdto를 넘겨준다(담아준다.)
			
			if(psdto != null) {
				path = "/WEB-INF/chap05_ok/personDetail.jsp";
			}
			else {
				path = "/WEB-INF/chap05_ok/personDetail_funStop.jsp";	// 사용자 장난 금지하도록!
			}
		} catch (SQLException e) {
			e.printStackTrace();
			path = "/WEB-INF/chap05_ok/personRegister_fail.jsp";	// 원래는 personDetail 에 대한 fail.jsp 파일을 만들어야 하지만 여기서는 임시로 Register_fail.jsp 를 사용한다.
		} 	
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);	// 넘기자.
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
