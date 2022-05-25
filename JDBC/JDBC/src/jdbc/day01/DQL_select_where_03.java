package jdbc.day01;

import java.sql.*;
import java.util.Scanner;

public class DQL_select_where_03 {

	public static void main(String[] args) {

		Connection conn = null;		
		PreparedStatement pstmt = null;		
		ResultSet rs = null;
		
		Scanner sc = new Scanner(System.in);
		
		
        try {		

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.print("▷ 연결할 오라클 서버의 IP 주소 : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "HR", "cclass");		// return 타입은 connection, 내 IP에 붙는 것


			String sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday "
				       + " from jdbc_tbl_memo "
				       + " order by no desc "; 

			
			pstmt = conn.prepareStatement(sql); 
						
			rs = pstmt.executeQuery();

		 System.out.println("------------------------------------------------------------------------");	
		 System.out.println("글번호\t글쓴이\t글내용\t작성일자");	
		 System.out.println("------------------------------------------------------------------------");	

		 StringBuilder sb = new StringBuilder();
		 
		 while(rs.next()) {	

			int no = rs.getInt(1);					
			String name = rs.getString(2); 						
			String msg = rs.getString(3); 					
			String writeday = rs.getString(4); 								
			
			sb.append(no);
			sb.append("\t"+name);
			sb.append("\t"+msg);
			sb.append("\t"+writeday+"\n");
			
		 }// end of while(rs.next() ------------------------------------
		 
		 System.out.println(sb.toString());		// return 타입이 String 타입으로.
		 
		 //////////////////////////////////////////////////////////////////////////////////////////
		 
		 // === StringBuilder sb 를 초기화 하기 === //
		 sb = new StringBuilder();				// StringBuilder 에 append 된 것을 싹다 날리겠다.
		 // 또는
		 //sb.setLength(0);
		 // 위에서 초기화 한 후 다시 StringBuilder 에 새롭게 append 해온다.
		 
		 sb.append("--------- >>> 조회할 대상 <<< ---------\n");
         sb.append("1.글번호   2.글쓴이   3.글내용   4.종료\n");
         sb.append("------------------------------------\n");
		 
         String menu = sb.toString();
         
 		 String str_menuNo = "";
         do {         
	         System.out.println(menu);
	         System.out.print("▷ 번호선택 : ");
	         str_menuNo = sc.nextLine();
	         
	         String colName = "";	// where 절에 들어올 컬럼명
	         
		    switch (str_menuNo) {
				case "1":	// 글번호로 검색
					colName ="no";	
					break;
	
				case "2":	// 글쓴이로 검색
					colName ="name";					
					break;	
	
				case "3":	// 글내용으로 검색
					colName ="msg";				
					break;
	
				case "4":	// 종료
	
					break;
	
				default:	// 1~4 외에는 메뉴에 없는 번호이다.
					System.out.println(">> 메뉴에 없는 번호입니다. \n<<");
					break;
			}// end of switch (str_menuNo)------------------------------------------
	         
		    // ★오로지 1,2,3 번만 입력했을때 아래와 같이 검색어가 나와야 함. (4번 종료를 했는데 이게 뜨면 안됨 ▶ if 문으로 걸어주자.)		  
		    if("1".equals(str_menuNo) || "2".equals(str_menuNo) || "3".equals(str_menuNo)) {
	         
		     System.out.print("▷ 검색어 : ");
	         String search = sc.nextLine();
	         
	         sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday "
			     + " from jdbc_tbl_memo ";
	         
	         if(!"3".equals(str_menuNo)) { // 글번호 또는 글쓴이로 검색시	         
	        	 sql += " where " + colName + " = ? ";		// colName 대신에 위치홀더를 써도 될까? (X) ▶ 데이터값만 위치홀더이지 컬럼네임이나 테이블네임은 위치홀더 쓰면 안된다.
	        	// ★★★ !!! 컬럼명 또는 테이블명 은 위치홀더인 ? 를 쓰면 안되고 변수로 처리 해야한다. !!! 
                // ★★★ !!! 데이터값만 위치홀더인 ? 를 써야 한다. !!!							
	         
	         }	        
	         else { // 글내용으로 검색시
	        	 sql += " where " + colName + " like '%'|| ? ||'%' ";
	         }
			
	         sql += " order by no desc "; 
		   
	         pstmt = conn.prepareStatement(sql);		// 우편배달부!
	         pstmt.setString(1, search);
	         
	         rs = pstmt.executeQuery();
	         
			 // === StringBuilder sb 를 초기화 하기 === //
			 sb = new StringBuilder();				// StringBuilder 에 append 된 것을 싹다 날리겠다.
			 // 또는
			 //sb.setLength(0);
			 
			 	int cnt = 0;
			 	while(rs.next()) {
			 		 cnt++;
			 		
			 		 if(cnt == 1) {
						 System.out.println("------------------------------------------------------------------------");	
						 System.out.println("글번호\t글쓴이\t글내용\t작성일자");	
						 System.out.println("------------------------------------------------------------------------");	
			 		 }
					 
						int no = rs.getInt(1);					
						String name = rs.getString(2); 						
						String msg = rs.getString(3); 					
						String writeday = rs.getString(4); 								
						
						sb.append(no);
						sb.append("\t"+name);
						sb.append("\t"+msg);
						sb.append("\t"+writeday+"\n");
			 		 
			 	}// end of while(rs.next())------------------------------------
			 
			 	if(cnt > 0) {	// 검색한 대상이 존재하는 경우
			 		System.out.println(sb.toString());
			 	}
			 	else {	// 검색한 대상이 없는 경우
			 		String searchType = "";
			 		
			 		switch (str_menuNo) {
						case "1":
							searchType = "글번호";
							break;
						case "2":
							searchType = "글쓴이중";							
							break;
						case "3":
							searchType = "글내용";
							break;
					}// end of switch(str_menuNo)-----------------------------------
			 		
			 		System.out.println(">>> "+searchType+"에는 "+search+"에 해당하는 데이터가 없습니다. <<< \n");
			 	}
			 	
		    }// end of if--------------------------------------------------
		  
		  
         } while( !("4".equals(str_menuNo)) );			// 4번 넣을때까지 do~while 문 반복한다.. == 4를 넣으면 종료. // 5번을 누르면 번호선택이 다시 나와야 한다.
         // end of do~while----------------------------------------
        	 
        	 
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {		// 자원 반납.
			
			try {
				if(rs != null)			
					pstmt.close();		
	
				if(pstmt != null)		
					pstmt.close();		
	
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
			}	
			
		}	 
		
        sc.close();
        System.out.println("~~~ 프로그램 종료 ~~~");		// 닫은 후(close) 종료.
        
	}// end of main() -----------------------------------

}
