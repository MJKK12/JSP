package common.controller;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
		description = "사용자가 웹에서 *.up 을 했을 경우 이 서블릿이 응답을 해준다.", 
		urlPatterns = { "*.up" }, 
		initParams = { 	// 초기화 파라미터 (아래의 value 경로에 초기화 파일이 있다.)
				@WebInitParam(name = "propertyConfig", value = "C:/NCS/workspace(jsp)/MyMVC/src/main/webapp/WEB-INF/Command.properties", description = "*.up에 대한 클래스의 매핑파일")
		})
public class FrontController extends HttpServlet {
	// *.up 을 했을 경우 이 FrontController 가 응답한다.
	private static final long serialVersionUID = 1L;

	Map<String,Object> cmdMap = new HashMap<>() ;	// 다형성으로 모두 받아야 한다. <K,V> , cmdMap 에 모두 담는다.

	public void init(ServletConfig config) throws ServletException {

	/*
	    웹브라우저 주소창에서  *.up 을 하면 FrontController 서블릿이 응대를 해오는데 
	    맨 처음에 자동적으로 실행되어지는 메소드가 init(ServletConfig config) 이다.
	    여기서 중요한 것은 init(ServletConfig config) 메소드는 WAS(톰캣)가 구동되어진 후
	    딱 1번만 init(ServletConfig config) 메소드가 실행되어지고, 그 이후에는 실행이 되지 않는다. 
	    그러므로 init(ServletConfig config) 메소드에는 FrontController 서블릿이 동작해야할 환경설정을 잡아주는데 사용된다.
	*/
		// *** 확인용 *** //
	//	System.out.println("확인용 입니다. => 서블릿 FrontController 의 init(ServletConfig config) 메소드가 실행됨. ");
	
		FileInputStream fis = null;
		// 특정 파일에 있는 내용을 읽어오기 위한 용도로 쓰이는 객체 (여기서는 Common.properties 를 읽어온다.)
		
		String props = config.getInitParameter("propertyConfig");
	//	System.out.println("~~~~ 확인용 props => "+ props );
		
		try {	// 해당 파일이 존재할 때
			fis = new FileInputStream(props);
			// fis 는 C:/NCS/workspace(jsp)/MyMVC/WebContent/WEB-INF/Command.properties 파일의 내용을 읽어오기 위한 용도로 쓰이는 객체이다.
			
			Properties pr = new Properties();
			// Properties 는 Collection 중 HashMap 계열중의  하나로써
			// "key","value"으로 이루어져 있는것이다.
			// 그런데 중요한 것은 Properties 는 key도 String 타입이고, value도 String 타입만 가능하다는 것이다.
	        // key는 중복을 허락하지 않는다. (덮어씌운다.) value 값을 얻어오기 위해서는 key값만 알면 된다.
				
			pr.load(fis);	// InputStream 은 인터페이스이다.
			// FileInputStream 을 pr(properties)에 올린다는 말이다.
			/*
		         pr.load(fis); 은  fis 객체를 사용하여 C:/NCS/workspace(jsp)/MyMVC/WebContent/WEB-INF/Command.properties 파일의 내용을 읽어다가 
		         Properties 클래스의 객체인 pr 에 로드(load)시킨다.
		         그러면 pr 은 읽어온 파일(Command.properties)의 내용에서 = 을 기준으로 왼쪽은 key로 보고, 오른쪽은 value 로 인식한다.
			 */
			
			Enumeration<Object> en = pr.keys();	
			/*
	          pr.keys(); 은
	          C:/NCS/workspace(jsp)/MyMVC/WebContent/WEB-INF/Command.properties 파일의 내용물에서 
	          = 을 기준으로 왼쪽에 있는 모든 key 들만 가져오는 것이다.    
	       */
			
			while(en.hasMoreElements()) {	
				// 그 값이 있는가? 그 값이 있다면, 그 key 를 가져오는 것이다.
				// hasMoreElements() 메서드는 Enumeration 의 메서드로 현재 커서가 가리키는 다음 위치에도 요소가 있으면 true를 반환

				String key = (String)en.nextElement();	// properties 는 k,y 모두 String 타입이므로 형변환한다.
			/*	
				System.out.println("~~~~ 확인용 key 값 => "+key);
				~~~~ 확인용 key 값 => /main.up
				~~~~ 확인용 key 값 => /index.up
			*/
			/*	
				System.out.println("확인용 value =>"+ pr.getProperty(key));	// value 값을 불러온다.
				확인용 value =>common.controller.MainController
				확인용 value =>common.controller.IndexController
			*/	
				String className = pr.getProperty(key);	// Command.properties 파일에서 key 값
				
				if(className != null) {
					// key 값이 null 이 아닐 때
					className = className.trim();	// 좌우 공백을 없앤 것을 다시 classname 에 넣는다.
					
					Class<?> cls = Class.forName(className);		// class 로 만든다.
					// <?> 은 generic 인데 어떤 클래스 타입인지는 모르지만 어쨌든 클래스 타입이 들어온다는 뜻이다. (ex.BoardController, ProductController...)
					// String 타입으로 된 className 을 클래스화 시켜주는 것이다.
					// 주의할 점은 실제로 String 으로 되어져 있는 문자열이 클래스로 존재해야만 한다는 것이다.
					
					Constructor<?> constrt = cls.getDeclaredConstructor();	// 기본생성자
					// <?> : 최상위, 어떤 것이든지 (다형성)
					// 생성자 만들기
					
					Object obj = constrt.newInstance();
					// 생성자로부터 실제 객체(인스턴스)를 생성해주는 것이다.
					
				//	System.out.println("[확인용] : " + obj.toString()); 	// 객체가 되어야 인스턴스메소드(toString)을 돌릴 수 있는 것이다.
				/*
				 	[확인용] : @@@ 확인용 MainController 클래스의 인스턴스 메소드인 toString() 을 호출함 ***
					[확인용] : *** 확인용 IndexController 클래스의 인스턴스 메소드인 toString() 을 호출함 ***
				*/
					
					cmdMap.put(key, obj);	// cmdMap 에 넣어준다.
					// cmdMap 에서 키값으로 Command.properties 파일에 저장되어진 url(/*.up) 을 주면 
					// cmdMap 에서 해당 클래스에 대한 객체(인스턴스)를 얻어오도록 만든 것이다. 즉, key 만 알면 value 값을 가져온다는 개념이다.
				}// end of if(classname != null)---------------------------
				
			}// end of while(en.hasMoreElements())----------------------------

			
			
		} catch (FileNotFoundException e) {	// 해당 파일이 없을 때
			// 파일경로, 파일명을 잘못 적었을 때 그런 파일이 없다는 것을 알리는 것.
			e.printStackTrace();
			System.out.println("C:/NCS/workspace(jsp)/MyMVC/src/main/webapp/WEB-INF/Command.properties 파일은 존재하지 않는 파일입니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	// 그러한 class 가 존재하지 않을 때. (즉, Command.properties 에 존재하는데 그런 class 가 만들어지지 않음)
			e.printStackTrace();
			System.out.println("문자열로 명명된 클래스가 존재하지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 웹브라우저 주소 입력창에서
		// http://localhost:9090/MyMVC/member/idDuplicateCheck.up?userid=leess 와 같이 입력되었다면
		// /member/idDuplicateCheck.up 만큼만 추출을 해와야 한다. (Command.Properties 와 연관)
		
	//	String url = request.getRequestURL().toString();	// return 타입이 StringBuffer 인 것을 toString() 을 사용해서 String 타입으로 바꾼다.
	//	System.out.println("확인용 url =>" + url);
		// 확인용 url =>http://localhost:9090/MyMVC/member/idDuplicateCheck.up
		// 여기서 data 인 ?userid=leess 는 필요 없이 그 앞에것까지만 읽어오는 것이다.
		// 이때 우리는 여기서 /member/idDuplicateCheck.up 만 필요하다.
		
		// *** URL 대신에 URI 를 사용한다.
		// 웹브라우저 주소 입력창에서
		// http://localhost:9090/MyMVC/member/idDuplicateCheck.up?userid=leess 와 같이 입력되었다면
		// /member/idDuplicateCheck.up 만큼만 추출을 해와야 한다. (Command.Properties 와 연관)
		
		String uri = request.getRequestURI();	
	//	System.out.println("확인용 uri =>" + uri);
		// 확인용 uri =>/MyMVC/member/idDuplicateCheck.up
		
	//	System.out.println("확인용 => " + request.getContextPath());
	//	확인용 => /MyMVC
		
		String ctxPath = request.getContextPath();	// /member/idDuplicateCheck.up 만 필요
		
		String key = uri.substring(ctxPath.length());	// 여기서 /MyMVC 의 길이(length)는 6 
		
	//	System.out.println("확인용 key => " + key);
		// key 는 ctxPath 다음에 나온 것이다.
		// 확인용 key => /member/idDuplicateCheck.up
		// 확인용 key => /index.up
		// 확인용 key => /main.up
		
		AbstractController action = (AbstractController) cmdMap.get(key);	// 클래스 모두 부모클래스가 같기 때문에 부모클래스로 받는다.
		
		if(action == null) {	// 존재하지 않는 key 값이라면.
	//		System.out.println(""+key+" 는 URI 패턴에 매핑된 클래스가 없습니다.");
			// /member/idDuplicateCheck.up 는 URI 패턴에 매핑된 클래스가 없습니다.
		}
		else {	// 존재하는 key 값이라면.
			try {
				action.execute(request, response);	// 메소드(execute) 를 돌린 결과물을 알아와야 한다.
				
				Boolean bool = action.isRedirect();		// false 또는 true 
				String viewPage = action.getViewPage();	// *.jsp 또는 *.up // Controller 에서 setViewPage 한것을 get(읽어온다.)
														// false 라면 ~~.jsp 이고, true ~~.up 이다.
				if(!bool) {	// false 라면 (forward)				
					// viewPage 에 명기된 view단 페이지로 forward(dispatcher)를 하겠다는 말이다.
					// forward 되어지면 웹브라우저의 URL주소 변경되지 않고 그대로 이면서 화면에 보여지는 내용은 forward 되어지는 jsp 파일이다.
					// 또한 forward 방식은 forward 되어지는 페이지로 데이터를 전달할 수 있다는 것이다.

					// 주소창에 잘못 적었을 경우를 방지한다. ↓

					if(viewPage != null) {
						RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
						dispatcher.forward(request, response);
					}
				
				}
				else {	// true 라면 (sendReDirect)
				   // viewPage 에 명기된 주소로 sendRedirect(웹브라우저의 URL주소 변경됨)를 하겠다는 말이다.
	               // 즉, 단순히 페이지이동을 하겠다는 말이다. 
	               // 암기할 내용은 sendRedirect 방식은 sendRedirect 되어지는 페이지로 데이터를 전달할 수가 없다는 것이다.
					if(viewPage != null) {	// ~~.up 페이지로 간다.
						response.sendRedirect(viewPage);
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
