package chap05;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 회원정보 목록을 모두 보여주는 것
@WebServlet("/personSelectAll.do")
public class PersonSelectAll_05 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private interPersonDAO_03 dao = new PersonDAO_04();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// tbl_person_interest 테이블에 저장된 행(데이터)을 읽어서(select) 웹페이지에 보여주어야 한다.
		
		String pathName = "";
		
		try {
			// 오라클문이 잘못 됐을때 throws sqlException 한다. 그렇기 때문에 여기서도 try~catch 를 사용한다.
			List<personDTO_02> personList = dao.selectAll();// interface 에 selectAll(); 메소드를 만든다. DTO(행들)이 현재 복수개이므로 List<>가 리턴타입
			request.setAttribute("personList", personList);	// request 영역에 저장.
			
			pathName = "/WEB-INF/chap05_ok/personSelectAll.jsp";	// 성공시 보여줄 view
			
		} catch (SQLException e) {	// 읽어올 때 오라클에서 에러 발생하면,
			e.printStackTrace();	// 어디가 오류인지 찍어준다.
			pathName = "/WEB-INF/chap05_ok/personRegister_fail.jsp";
		}	
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(pathName);	// dao 메소드 결과물에 따라서 보여줄 내용	
		dispatcher.forward(request, response);
	}	
		

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
