package chap03;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/registerPerson.do")
public class RegisterPerson extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// jQuery로 전송할 때 유효성 검사를 다 했기 때문에 색,음식은 null 값이 나올 수 없다. 
		// 색과 음식은 선택해야만 전송이 된다. 그러므로 if 를 쓸 필요가 없다.
		String method = request.getMethod();
		
	//	System.out.println("확인용 method =>" + method);
		
		if("POST".equalsIgnoreCase(method)) {
			// POST 방식으로 들어온 경우
			
			String name = request.getParameter("name");
			String school = request.getParameter("school");
			String color = request.getParameter("color");
			String[] arrfood = request.getParameterValues("food");		// 음식은 다중선택 이므로, getParameterValues 이다.(리턴타입은 배열)
			
			String foods = String.join(",", arrfood);
		/*
			System.out.println("확인용 name =>" + name);
			System.out.println("확인용 school =>" + school);
			System.out.println("확인용 color =>" + color);
			System.out.println("확인용 foods =>" + foods);
		 */		
			Map<String, String> paraMap = new HashMap<>();	// Map 에 담아둔다.
			paraMap.put("name", name);
			paraMap.put("school", school);
			paraMap.put("color", color);
			paraMap.put("foods", foods);
			
			request.setAttribute("paraMap", paraMap);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/chap03_StandardAction/04_forwardForm_view_02.jsp");	// 경로를 적어준다.	(해당 jsp에 넘겨주겠다.)
			// java 파일은 /webapp 까지는 쓰면 안되기 때문에 지운다.
			// 리턴타입은 RequestDispatcher
			dispatcher.forward(request, response);
		}
		else {
			// GET 방식으로 들어온 경우 (주소창에 임의로 GET 방식처럼 입력했을때 들어오는 것을 방지)
			RequestDispatcher dispatcher = request.getRequestDispatcher("/chap03_StandardAction/04_forwardForm_view_error_03.jsp");	// 경로를 적어준다.	(해당 jsp에 넘겨주겠다.)
			dispatcher.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);	// doGet 시 이렇게 호출 하겠다.
	}

}
