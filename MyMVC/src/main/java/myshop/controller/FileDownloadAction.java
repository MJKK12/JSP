package myshop.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import myshop.model.*;

public class FileDownloadAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String pnum = request.getParameter("pnum");
		
		// 다운로드 할 파일의 경로를 알아오고 File 객체를 생성한다.
		// 제품등록Action 단에 가서 경로를 알아온다.
		HttpSession session = request.getSession();
		
		
		try {
			ServletContext svlCtx = session.getServletContext();
			String uploadFileDir = svlCtx.getRealPath("/images");	// RealPath 는 실제로 톰캣 WAS가 작동해서 움직이는 곳이다 (.metadata) / 일반 workspace 파일-MyMVC 폴더는 '개발용'이다.
			//	System.out.println("=== 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> " + uploadFileDir);
			// === 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> C:\NCS\workspace(jsp)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MyMVC\images		
			
			
			// **** 시스템에 업로드 된 파일설명서 첨부파일명 및 오리지널 파일명 알아오기 **** // (DB에 업로드 된 것을 알아오자.)
			// ※getFilesystemName : 업로드용(중복 파일명 방지) / getOriginalFileName : 첨부파일 다운받는용 (사용자가 올린 그대로 파일명 받기)
			InterProductDAO pdao = new ProductDAO();
			Map<String, String> map = pdao.getPrdmanualFileName(pnum);		// 파라미터에 pnum 을 넘겨주자. (해당 file number 에 따른 첨부된 파일)
			
			// DB 에서 다운받아와야할 파일명들을 받아온다.
			// File 객체 생성하기
			// 경로명을 우선 알아오자. (어느경로에 있는 어떤 파일명인지), \\ 는 window 일 때 쓰인다. mac linux unix 는 / 로 나간다. 
			String filePath = uploadFileDir + File.separator + map.get("prdmanual_systemFileName");	// 경로명 + 파일이 실제 올라간 map  	
			/* File.separator : 사용중인 운영체제가 windows 라면 "\" File.separator 의 값은 "\" 이고,
			  					사용중인 운영체제가 매킨토시, Linux, Unix 라면 File.separator 의 값은 "/" 이다.
			 					(window 뿐만 아니라 다른 운영체제도 호환되도록 하기 위함.)	
			*/
			
			// file 은 String 타입이며, 다운을 받아와야 한다. (파일 객체를 생성)
			File file = new File(filePath);	// 파일명(filePath)이 와야 한다. (실제 .metadata 경로에 가는 것이다.)
			
			// MIME TYPE 설정하기 (다운받을 파일의 종류가 무엇인지를 알아야 한다.)
	        // (구글에서 검색어로 MIME TYPE 을 해보면 MIME TYPE에 따른 문서종류가 쭉 나온다)
	        String mimeType = svlCtx.getMimeType(filePath);
	        
		   //   System.out.println("~~~~ 확인용 mimeType => " + mimeType);
		   //   ~~~~ 확인용 mimeType => application/pdf  .pdf 파일임
		   //   ~~~~ 확인용 mimeType => image/jpeg       .jpg 파일임
		   //   ~~~~ 확인용 mimeType => application/vnd.openxmlformats-officedocument.spreadsheetml.sheet 엑셀파일임.
	        
	        if(mimeType == null) {	// 처음보는 mimeType 이 왔을 경우.     	
	        	mimeType = "application/octet-stream";
	        	// "application/octet-stream" 은 일반적으로 잘 알려지지 않은 모든 종류의 이진 데이터를 뜻하는 것임. 
	        	// 다운받을 파일이 무엇인지 응답(response) 해주는 것이다.        		
	        }
	        response.setContentType(mimeType);	// 내용물이 무슨 타입인지 정해주어야 한다. (setContentType)
			
	        // 다운로드 될 파일명 알아와서 설정하기	(DAO 에서 put 한 값을 가져온다.)
	        // map.get("prdmanual_orginFileName")이 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명임.	
	        String prdmanual_orginFileName = map.get("prdmanual_orginFileName");
	        
	        // 파일명에 한글이 포함되어 있을 때 깨지는 경우가 있다. (내용물은 깨지지 않지만 파일명이 깨지는 경우.)
	        /*
	         파일명을 깨지지 않도록 하겠다.
	         prdmanual_orginFileName (다운로드 될 파일명) 에 한글이 포함될 경우 
	         한글이 깨지지 않도록 웹브라우저 별로 encoding 하기 및 다운로드 파일명 설정해주기 
	        */
	        // 아래의 코드는 참고해온 것임. (공식같은 것), 한글이 깨지지 않게끔 다 바꿔준 것이다.        
	        String downloadFileName = "";
	        String header = request.getHeader("User-Agent");
	        
	        if (header.contains("Edge")){
	            downloadFileName = URLEncoder.encode(prdmanual_orginFileName, "UTF-8").replaceAll("\\+", "%20");
	             response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	          } else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터는 Trident로 변경됨.
	             downloadFileName = URLEncoder.encode(prdmanual_orginFileName, "UTF-8").replaceAll("\\+", "%20");
	             response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	         } else if (header.contains("Chrome")) {
	            downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	             response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	         } else if (header.contains("Opera")) {
	            downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	             response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	         } else if (header.contains("Firefox")) {
	            downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	             response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	         }
	        
	        // *** 다운로드할 요청 파일을 읽어서 클라이언트로 파일을 전송하기 *** // (WAS 서버에서 --> 사용자 PC로 보내주기)
	        FileInputStream finStream = new FileInputStream(file);		// 파일을 읽어와야 한다.
	        // 1byte 기반 파일 입력 노트스트림 생성
	        
	        // 웹이기 때문에 
	        ServletOutputStream srvOutStream = response.getOutputStream();
	        // 1byte 기반 파일 입력 노트스트림 생성
	        // ServletOutputStream 은 바이너리 데이터를 웹 브라우저로 전송할 때 사용하는 것이다.
	        
	        byte arrb[] = new byte[4096]; // 4kb씩 하겠다.	( IO 입출력 부분 참고 )
	        int data = 0;	//※ Returns : the total number of bytes read into the buffer, or -1 if there is no more data because the end ofthe file has been reached.
	        while( (data = finStream.read(arrb, 0, arrb.length)) != -1 ) {	// arrb, data, data( byte배열(저장소),시작배열주소,읽어들이는크기)
	        	// 4KB 씩 읽어들여서 	arrb[] 에 저장하겠다. (더이상 읽어들일 게 없으면 -1 이 return 된다.)
	        	srvOutStream.write(arrb, 0, data);	// arrb[] 에 저장해둠. / data 가 실제로 읽어들인 크기만큼 보낸다.(write)        	
	        }// end of while-----------------------------------------
	 
	        // write가 나오면 항상 flush(); 전송을 해줘야 한다.
	        srvOutStream.flush();
	        
	        // 자원을 닫아준다 (입출력 노드스트림 close)
	        srvOutStream.close();	// 출력 노드스트림
	        finStream.close();		// 입력 노드스트림		
        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
