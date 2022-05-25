package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chap03.PersonDTO;


@WebServlet("/personUpdateEnd.do")
public class PersonUpdateEnd_09 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private interPersonDAO_03 dao = new PersonDAO_04();	// field 에 dao 를 만들고 기본생성자를 준 것이다. → 기본생성자가 움직인다.

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getMethod();	// "GET" 또는 "POST"
		
		String path ="";
		
		if("POST".equalsIgnoreCase(method)) {	// POST 방식 일 때만

			String seq = request.getParameter("seq");	// form 태그에서 변경된 name 을 받아온다.
			String name = request.getParameter("name");	// form 태그에서 변경된 name 을 받아온다.
			String school = request.getParameter("school");	// form 태그에서 변경된 school 을 받아온다.
			String color = request.getParameter("color");	// form 태그에서 변경된 color 을 받아온다.
			String[] foodArr = request.getParameterValues("food");	// form 태그에서 변경된 food 을 받아온다. (food 의 경우 null 값이 나올 수 있음. 선택 X 인 경우)
			
			personDTO_02 psdto = new personDTO_02();
			psdto.setSeq(Integer.parseInt(seq));	// get파라미터에 seq를 읽어와야 한다. form 태그에 seq가 없기 때문에 넣어줘야 한다.
			psdto.setName(name);					// 넘어온 seq 를 DTO인 psdto 에 담는다. // PK 인 seq 가 들어와야 한다. (Update 하려면 PK 인 seq 가 필요하다. 동명이인의 이름이 있을 수 있다. )
			psdto.setSchool(school);
			psdto.setColor(color);
			psdto.setFood(foodArr);
			
			try {
				int n = dao.updatePerson(psdto);	// dao 로 보내준다. (personDTO에 담아서 보내달라.) , DML 문이기 때문에 리턴타입은 int (한개행이 변경되었습니다.)
	
				if(n==1) {	// 정상적으로 update 됐을 때 (페이지가 이동하는 것이지 view 단을 보여주는 것이 아니다.)
					// 특정 개인에 대한 개인성향 결과를 보여주는 페이지로 이동시킨다.
					// 즉, URL 경로로 페이지의 이동을 시켜줘야 한다.
					response.sendRedirect("personDetail.do?seq="+seq);	// sendRedirect : 페이지의 이동이다. 상대경로 이므로 / 를 붙이지 않는다.
					return;
				}
				else {
					// 즉, URL 경로로 페이지의 이동을 시켜줘야 한다.
					response.sendRedirect("personSelectAll.do");	// sendRedirect : 페이지의 이동이다. 상대경로 이므로 / 를 붙이지 않는다.
					return;					
				}
				
			} catch (SQLException e) {	// 잘못됐기 때문에 에러페이지를 보여준다.
				e.printStackTrace();	// method 가 post 방식일 때만 update를 해줘야한다.
				path = "/WEB-INF/chap05_ok/personRegister_fail.jsp";	// SQL 문 에러 발생 시!				
			}	
		}
		
		else {
			// GET 방식으로 넘어왔다면 (사용자가 post 방식이 아니라 get 방식으로 넘어온 것임)
			path = "/WEB-INF/chap05_ok/personDetail_funStop.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
