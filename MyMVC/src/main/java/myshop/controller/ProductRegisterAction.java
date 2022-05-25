package myshop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)

		// == 관리자(admin)로 로그인 했을 때에만 조회가 가능하도록 해야한다. == //
		// 이중으로 막아주도록 하자 (메뉴바 & 주소창 url 접속)
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");	// 세션에가서 key 값이 있는지 없는지 읽어온다. 리턴타입은 object 이기 때문에 MemberVO 로 바꾼다.
			
		if(loginuser == null || !"admin".equals(loginuser.getUserid())) {	// 로그인을 하지 않았거나 or 로그인된 유저의 아이디가 admin 이 아님
		//	로그인을 하지 않았거나 || 로그인을 했지만, 로그인한 유저의 id 가 admin 인 아닌 경우.	(올바르지 않은 접속)		
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
		
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");	// url 입력 시 맨앞에 '/' 입력 잊지말기
		}
		
		else {
			// 관리자(admin)로 로그인 했을 때 (올바른 접속)
			// 관리자로 접속 시, form 페이지를 보여주자.
			// text 는 웹브라우저에 쓸 수 있지만 mp4 같은 동영상 파일은 글자가 아니기 때문에 웹브라우저 주소창에 올 수 없다.
			// 파일 첨부 시, 무조건 POST 방식이다.
			
			String method = request.getMethod();
			
			if(!"post".equalsIgnoreCase(method)) {
				// GET 방식 이라면 --> form 페이지를 띄운다.

			// 카테고리 목록 조회해오기
				super.getCategoryList(request);	// 부모클래스에서 만들어놓은 메소드
				
			// spec 목록을 보여주고자 한다.
				InterProductDAO pdao = new ProductDAO();
			// spec 목록을 DB 에서 가져와서 보여주자. (복수개가 나온다. 이미 vo 를 만들어놨음 / HashMap 도 사용가능하다.)
				List<SpecVO> specList = pdao.selectSpecList();				
				request.setAttribute("specList", specList);
				
			//	super.setRedirect(false);
				super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");	// 관리자 전용(admin) 제품등록 폴더

			}
			
			else {
				// POST 방식 이라면, 제품등록.jsp 에서 보낸 form 태그를 받아야 한다.
				// 그러나 이때, input 태그에 file이 있으면 받지 못한다. --> 외부라이브러리를 불러온다.
				/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					파일을 첨부해서 보내는 폼태그가 
					enctype="multipart/form-data" 으로 되어었다라면
					HttpServletRequest request 을 사용해서는 데이터값을 받아올 수 없다.
					이때는 cos.jar 라이브러리를 다운받아 사용하도록 한 후  (com.oreilly.servlet == cos)
					아래의 객체를 사용해서 데이터 값 및 첨부되어진 파일까지 받아올 수 있다.
				  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    
				 */
				MultipartRequest mtrequest = null;
				/*
		             MultipartRequest mtrequest 은 
		             HttpServletRequest request 가 하던일을 그대로 승계받아서 일처리를 해주고 
		             동시에 파일을 받아서 업로드, 다운로드까지 해주는 기능이 있다.      
				*/
				
				// 1. 첨부된 파일을 디스크의 어느경로에 업로드 할 것인지 그 경로를 설정해야 한다.
				ServletContext svlCtx = session.getServletContext();
				String uploadFileDir = svlCtx.getRealPath("/images");	// RealPath 는 실제로 톰캣 WAS가 작동해서 움직이는 곳이다 (.metadata) / 일반 workspace 파일-MyMVC 폴더는 '개발용'이다.
				//	System.out.println("=== 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> " + uploadFileDir);
				// === 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> C:\NCS\workspace(jsp)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MyMVC\images		
					
				/*
	             MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
	                   
	             MultipartRequest(HttpServletRequest request,
	                              String saveDirectory, -- 파일이 저장될 경로
	                              int maxPostSize,      -- 업로드할 파일 1개의 최대 크기(byte)
	                              String encoding,
	                              FileRenamePolicy policy) -- 중복된 파일명이 올라갈 경우 파일명다음에 자동으로 숫자가 붙어서 올라간다.   
	                  
	             파일을 저장할 디렉토리를 지정할 수 있으며, 업로드제한 용량을 설정할 수 있다.(바이트단위). 
	             이때 업로드 제한 용량을 넘어서 업로드를 시도하면 IOException 발생된다. 
	             또한 국제화 지원을 위한 인코딩 방식을 지정할 수 있으며, 중복 파일 처리 인터페이스를사용할 수 있다.
	                        
	             이때 업로드 파일 크기의 최대크기를 초과하는 경우이라면 
	             IOException 이 발생된다.
	             그러므로 Exception 처리를 해주어야 한다.                
				*/				
				
				// 파일을 업로드 하지 않는 mtrequest = null; 상태에서, 업로드 해준다.
				// 파일을 업로드 해준다.
				try {
					mtrequest = new MultipartRequest(request, uploadFileDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());			
				} catch (IOException e) {
					  e.printStackTrace();
						
					  request.setAttribute("message", "업로드 되어질 경로가 잘못되었거나 또는 최대용량 10MB를 초과했으므로 파일업로드 실패함!!");
					  request.setAttribute("loc", request.getContextPath()+"/shop/admin/productRegister.up"); 
					  
					  super.setViewPage("/WEB-INF/msg.jsp");
					  return; // 종료
				}
				// 파일을 업로드 해준다. 끝 //
				// MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
	           
				// === 첨부 이미지 파일을 올렸으니 그 다음으로 제품정보를 (제품명, 정가, 제품수량,...) DB의 tbl_product 테이블에 insert 를 해주어야 한다.  ===
				
				// 새로운 제품 등록시 form 태그에서 입력한 값들을 얻어오기.
				String fk_cnum =  mtrequest.getParameter("fk_cnum");	// request 의 역할을 하기 때문에 getParameter 도 가능. (원래 request 가 하던 기능들도 할 수 있다.)
				String pname =  mtrequest.getParameter("pname");	
				String pcompany =  mtrequest.getParameter("pcompany");	
			  
				// ※getFilesystemName : 업로드용(중복 파일명 방지) / getOriginalFileName : 첨부파일 다운받는용 (사용자가 올린 그대로 파일명 받기)
				// 업로드되어진 시스템의 첨부파일 이름(파일서버에 업로드 되어진 실제파일명)을 얻어 올때는 
	            // cos.jar 라이브러리에서 제공하는 MultipartRequest 객체의 getFilesystemName("form에서의 첨부파일 name명") 메소드를 사용 한다. 
	            // 이때 업로드 된 파일이 없는 경우에는 null을 반환한다.	// getFilesystemName : 실제 탐색기에 올라간 파일명
				String pimage1 = mtrequest.getFilesystemName("pimage1");		// file 은 getParameter 가 아니다.
				String pimage2 = mtrequest.getFilesystemName("pimage2");		// file 은 getParameter 가 아니다.
		/*
				System.out.println("~~~ 확인용 pimage1 : " + pimage1);
				System.out.println("~~~ 확인용 pimage2 : " + pimage2);
				~~~ 확인용 : pimage1 쉐보레전면1.jpg
				~~~ 확인용 : pimage2 쉐보레후면1.jpg
		*/		
				// getOriginalFileName 는 실제 다운받는 파일명 (다운받을때 사용자가 올린 파일명으로 그대로 다운로드 받아야 한다. 즉, 쉐보레전면123.jpg 이 아니라 쉐보레전면.jpg로 다운받는 것)
				String pimage1_originFileName = mtrequest.getOriginalFileName("pimage1");		// getOriginalFileName : 실제 사용자가 올린 file 이름
				String pimage2_originFileName = mtrequest.getOriginalFileName("pimage2");		// 실제 사용자가 올린 file 이름
		/*
				System.out.println("*** 확인용 pimage1_originFileName : " + pimage1_originFileName);
				System.out.println("*** 확인용 pimage2_originFileName : " + pimage2_originFileName);
				*** 확인용 : pimage1_originFileName 쉐보레전면.jpg
				*** 확인용 : pimage2_originFileName 쉐보레후면.jpg		
		*/
				String prdmanual_systemFileName = mtrequest.getFilesystemName("prdmanualFile");
				// 제품설명서 파일명(파일서버에 업로드 되어진 실제파일명)  
	            // 제품설명서 파일명 입력은 선택사항이므로 NULL 이 될 수 있다.
		//		System.out.println("확인용 prdmanualFile_systemFilename ==> " + prdmanualFile_systemFilename);
				// 확인용 prdmanualFile_systemFilename ==> null (첨부파일에 첨부 안했을 때)
				
				String prdmanual_originFileName = mtrequest.getOriginalFileName("prdmanualFile");		// file 은 getParameter 가 아니다.
				// 필수 첨부파일이 아닌 경우에는 null 값이 들어와도 된다. (not null 이 아님)
				// 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명
	            // 제품설명서 파일명 입력은 선택사항이므로 NULL 이 될 수 있다.
	            // 첨부파일들 중 이것만 파일다운로드를 해주기 때문에 getOriginalFileName(String name) 메소드를 사용한다.

			/*
	            <<참고>> 
	            ※ MultipartRequest 메소드

	              --------------------------------------------------
	              반환타입                         설명
	            --------------------------------------------------
	             Enumeration       getFileNames()
	            
	                               업로드 된 파일들에 대한 이름을 Enumeration객체에 String형태로 담아 반환한다. 
	                               이때의 파일 이름이란 클라이언트 사용자에 의해서 선택된 파일의 이름이 아니라, 
	                               개발자가 form의 file타입에 name속성으로 설정한 이름을 말한다. 
	                               만약 업로드 된 파일이 없는 경우엔 비어있는 Enumeration객체를 반환한다.
	            
	             
	             String            getContentType(String name)
	            
                                   업로드 된 파일의 컨텐트 타입을 얻어올 수 있다. 
                                   이 정보는 브라우저로부터 제공받는 정보이다. 
                                   이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	             File              getFile(String name)
	            
                                   업로드 된 파일의 File객체를 얻는다. 
                                   우리는 이 객체로부터 파일사이즈 등의 정보를 얻어낼 수 있다. 
                                   이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	             String            getFilesystemName(String name)
	            
                                   시스템에 업로드되어진 파일의 이름을 반환한다.
                                   시스템에 "쉐보레전면.jpg" 가 올라가 있는데 또 사용자가 웹에서 "쉐보레전면.jpg" 파일을 올릴경우 
	                               FileRenamePolicy 에 의해 시스템에 업로드되어지는 파일명은 "쉐보레전면1.jpg" 가 되며
	                               "쉐보레전면1.jpg" 파일명을 리턴시켜주는 것이  getFilesystemName(String name) 이다.                       
                                   만약에, 이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	             String            getOriginalFileName(String name)
	            
                                   중복 파일 처리 인터페이스에 의해 변환되기 이전의 파일 이름을 반환한다. 
                                   이때업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	             String            getParameter(String name)
	            
                                   지정한 파라미터의 값을 반환한다. 
                                   이때 전송된 값이 없을 경우에는 null을 반환한다.
	            
	            
	             Enumeration       getParameternames()
	            
                                   폼을 통해 전송된 파라미터들의 이름을 Enumeration객체에 String 형태로 담아 반환한다. 
                                   전송된 파라미터가 없을 경우엔 비어있는 Enumeration객체를 반환한다
	            
	            
	             String[]          getparameterValues(String name)
	            
                                   동일한 파라미터 이름으로 전송된 값들을 String배열로 반환한다. 
                                   이때 전송된파라미터가 없을 경우엔 null을 반환하게 된다. 
                                   동일한 파라미터가 단 하나만 존재하는 경우에는 하나의 요소를 지닌 배열을 반환하게 된다.    
	         */
				
				String pqty =  mtrequest.getParameter("pqty");				// 제품 수량
				String price =  mtrequest.getParameter("price");			// 제품 정가
				String saleprice =  mtrequest.getParameter("saleprice");	// 제품 판매가
				String fk_snum =  mtrequest.getParameter("fk_snum");		// 제품스펙
				
				// !!!! 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기 !!!!! //
				String pcontent =  mtrequest.getParameter("pcontent");		// 제품설명
				pcontent =  pcontent.replaceAll("<","&lt;");
				pcontent =  pcontent.replaceAll(">","&gt;" );
				// 입력한 내용에서 엔터는 <br> 로 변환시키기
				pcontent =  pcontent.replaceAll("\r\n", "<br>");	// 엔터 인식
				
			/*
				&lt;script type="text/javascript"&gt;
				alert("안녕하세요~~ 빨강파랑 ㅋㅋㅋ");                
				var body = document.getElementsByTagName("body");
				                body[0].style.backgroundColor = "red";               
				var arrDiv = document.getElementsByTagName("div");
				for(var i=0; i<arrDiv.length; i++) {
				arrDiv[i].style.backgroundColor = "blue";
				}
				&lt;/script&gt;	
			*/
				
				String point =  mtrequest.getParameter("point");			// 제품포인트

				InterProductDAO pdao = new ProductDAO();
				// 논리모델링 부분 고려하기
				// 이미지파일 테이블의 파일num 은, 제품 테이블의 file num 를 fk로 한다.
				// 1 1004(fk_pnum) 새우깡.png (imgfileno fk_pnum imgfilename)
				// 이때, 이미지파일테이블은 제품테이블에서 pnum 의 '시퀀스'를 알아와야 한다. ==> "채번" 해온다.(일련번호)
				
				// 제품번호 채번 해오기위해 DAO 에 보내준다.
				int pnum = pdao.getPnumOfProduct();		// return 타입은 시퀀스 이므로 int
				
				// VO 를 만들자. (getParameter 해온 것을 넣어준다.)
				ProductVO pvo = new ProductVO();
				pvo.setPnum(pnum);	// DB에서 채번해 온 것
				pvo.setFk_cnum(Integer.parseInt(fk_cnum));	// 카테고리 num (VO 에서는 int 타입이므로 바꿔준다.)
				pvo.setPname(pname);
			    pvo.setPcompany(pcompany);
			    pvo.setPimage1(pimage1);
	 	 	    pvo.setPimage2(pimage2);
			    pvo.setPrdmanual_systemFileName(prdmanual_systemFileName);
			    pvo.setPrdmanual_orginFileName(prdmanual_originFileName);	// origin 으로 수정
			    pvo.setPqty(Integer.parseInt(pqty));
			    pvo.setPrice(Integer.parseInt(price));
			    pvo.setSaleprice(Integer.parseInt(saleprice));
			    pvo.setFk_snum(Integer.parseInt(fk_snum));
			    pvo.setPcontent(pcontent);
			    pvo.setPoint(Integer.parseInt(point));	
			
			    
			    String message = "";
			    String loc = "";
			    
			    try {
						// tbl_product 테이블에 제품정보 insert 하기 (추가 제품 이미지가 3개라면 3번 insert. 즉 n개 일때 n번 insert)
					    pdao.productInsert(pvo);
					    
					 // === 추가이미지파일이 있다면 tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 해주기 === //
					    
						String str_attachCount =  mtrequest.getParameter("attachCount");	// 추가이미지파일(선택)
					//	System.out.println("### 확인용 추가이미지 파일개수 str_attachCount => "+str_attachCount);
						//	###### 확인용 추가이미지 파일개수 str_attachCount => "" (제품 추가이미지가 없을 때 null 이 아님. "" 이다.)	
						//	###### 확인용 추가이미지 파일개수 str_attachCount => "0" ~ "10" (추가이미지를 최대 10개로 설정해놓음)	
						// str_attachCount 이 추가이미지 파일의 개수인데 "" "0" ~ "10" 값이 들어온다.
		
						int attachCount = 0;
						
						if(!"".equals(str_attachCount)) {
							// "" 이 아닌 숫자가 들어왔다면 (제품이미지 추가를 1개 이상 한것)
							attachCount = Integer.parseInt(str_attachCount);
						}
						
						// 첨부파일의 파일명(파일서버에 업로드 된 실제파일명 알아오기)
						for(int i=0; i<attachCount; i++) {
							String attachFileName = mtrequest.getFilesystemName("attach"+i);	// 추가이미지파일이 n개(attachCount)면 n번만큼 name을 읽어와야함. n번 반복(for)
															// 서버에 올라간 file명
							// DAO 에 메소드를 만들자.
							// 이번에는 product_imagefile 테이블에 insert 할 것인데 VO 대신에 HashMap 을 사용하여 insert 하겠다.
							Map<String, String> paraMap = new HashMap<>();
							paraMap.put("pnum", String.valueOf(pnum));		// 제품테이블에 넣어줄 제품번호 (제품테이블의 fk키) // 위에서 채번해온 pnum (=> fk_pnum, 시퀀스 따옴)
							paraMap.put("attachFileName", attachFileName);	// 첨부될 추가이미지 파일명 
		
							pdao.product_imagefile_Insert(paraMap);				// 제품 추가이미지파일을 insert 하는 메소드 (추가될 파일명만큼 반복)				
							
						}// end of for------------------------------------------
						
						message = "제품등록 성공 !!";
						loc = request.getContextPath()+"/shop/mallHome1.up";	// 성공 시 쇼핑몰홈[더보기] 페이지로 이동
						
			    	} catch (SQLException e) {
			    		e.printStackTrace();	
			    	
			    		message = "제품등록 실패 !!";
						loc = request.getContextPath()+ "/shop/admin/productRegister.up";
					}
					
		    		request.setAttribute("message", message);
		    		request.setAttribute("loc", loc);
		    		
		    		super.setViewPage("/WEB-INF/msg.jsp");		    
			}
				
		}
		
	}

}
