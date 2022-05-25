package jdbc.day01;

import java.sql.*;
import java.util.Scanner;

public class DQL_select_02 {

	public static void main(String[] args) {

		Connection conn = null;
	 // Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		PreparedStatement pstmt = null;
		// PreparedStatement pstmt 은 Connection conn(특정 오라클 서버)에 전송할 SQL문(편지)을 전달할 객체(우편배달부)이다.
		
		ResultSet rs = null;
	 // ResultSet rs 은 select 된 결과물이 저장되는 곳이다. (rs 에 다 들어온다.)
		
		Scanner sc = new Scanner(System.in);
		
        try {		
			// >>> 1. 오라클 드라이버 로딩 <<<  //
			/*
			   === OracleDriver(오라클 드라이버)의 역할 ===
			   1). OracleDriver 를 메모리에 로딩시켜준다.
			   2). OracleDriver 객체를 생성해준다.
			   3). OracleDriver 객체를 DriverManager에 등록시켜준다.
			       --> DriverManager 는 여러 드라이버들을 Vector 에 저장하여 관리해주는 클래스이다.
			*/ 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클 서버와 연결을 할래? <<< //
			System.out.print("▷ 연결할 오라클 서버의 IP 주소 : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "HR", "cclass");		// return 타입은 connection, 내 IP에 붙는 것

			
			// >>> 3. SQL문(편지)을 작성한다. <<< //

			String sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday "
				       + " from jdbc_tbl_memo "
				       + " order by no desc "; // SQL문 맨 뒤에 ; 를 넣으면 오류이다.!!!

			
			// >>> 4. 연결한 오라클서버(conn)에 SQL문(편지)을 전달할 PreparedStatement 객체(우편배달부) 생성하기 <<< //
			pstmt = conn.prepareStatement(sql); 
						
			// >>> 5. PreparedStatement pstmt 객체(우편배달부)는 작성된 SQL문(편지)을 오라클 서버에 보내서 실행이 되도록 해야 한다 <<< //
			rs = pstmt.executeQuery();
			/*  
			    SQL문이 DQL(select) 이므로 executeQuery(); 이다.
				pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 그 타입은 *ResultSet* 으로 가져온다.
			*/
			
			 // ▼▼▼▼▼▼▼ 여기서 N 일때 출력되는 부분 수정.
			
			/*
            === 인터페이스 ResultSet 의 주요한 메소드 ===
            1. next()  : select 되어진 결과물에서 커서를 다음으로 옮겨주는 것             리턴타입은 boolean 
            2. first() : select 되어진 결과물에서 커서를 가장 처음으로 옮겨주는 것       	리턴타입은 boolean
            3. last()  : select 되어진 결과물에서 커서를 가장 마지막으로 옮겨주는 것    	리턴타입은 boolean
            
            == 커서가 위치한 행에서 컬럼의 값을 읽어들이는 메소드 ==
            getInt(숫자) : 컬럼의 타입이 숫자이면서 정수로 읽어들일때
                           파라미터 숫자는 컬럼의 위치값 
                          
            getInt(문자) : 컬럼의 타입이 숫자이면서 정수로 읽어들일때
                           파라미터 문자는 *컬럼명 또는 alias명* 
                           
            getLong(숫자) : 컬럼의 타입이 숫자이면서 정수로 읽어들일때
                              파라미터 숫자는 컬럼의 *위치값* 
                          
            getLong(문자) : 컬럼의 타입이 숫자이면서 정수로 읽어들일때
                              파라미터 문자는 *컬럼명 또는 alias명*                
            
            getFloat(숫자) : 컬럼의 타입이 숫자이면서 실수로 읽어들일때
                               파라미터 숫자는 컬럼의 *위치값* 
                          
            getFloat(문자) : 컬럼의 타입이 숫자이면서 실수로 읽어들일때
                               파라미터 문자는 *컬럼명 또는 alias명* 
                               
            getDouble(숫자) : 컬럼의 타입이 숫자이면서 실수로 읽어들일때
                                 파라미터 숫자는 *컬럼의 위치값* 
                          
            getDouble(문자) : 컬럼의 타입이 숫자이면서 실수로 읽어들일때
                                 파라미터 문자는 *컬럼명 또는 alias명*    
                                 
            getString(숫자) : 컬럼의 타입이 문자열로 읽어들일때
                                 파라미터 숫자는 *컬럼의 위치값* 
                          
            getString(문자) : 컬럼의 타입이 문자열로 읽어들일때
                                 파라미터 문자는 *컬럼명 또는 alias명*                                                        
         */
			
		 System.out.println("------------------------------------------------------------------------");	
		 System.out.println("글번호\t글쓴이\t글내용\t작성일자");	
		 System.out.println("------------------------------------------------------------------------");	

		 StringBuilder sb = new StringBuilder();
		 
		 while(rs.next()) {	// select 된 행이 없을때까지 계속 반복한다.
		  /*
               rs.next() 는 select 된 결과물에서 위치(행의 위치)를 다음으로 옮긴 후 
               행이 존재하면 true 를 리턴해주고, 행이 없으면 false 를 리턴해주는 메소드이다.
          */
		/*
			int no = rs.getInt("NO");					// "NO" 는 select 해온 컬럼명이다. (SQL 의 NO 컬럼을 읽어온다는 말이다.) // 컬럼명을 넣어준다. 관습상 대문자. (SQL의 컬럼명은 대/소문자 구분 X) get 읽어온다, set 넣어준다.
			String name = rs.getString("NAME"); 		// "NAME" 는 select 해온 컬럼명이다. (SQL 의 NAME 컬럼을 읽어온다는 말이다.)				
			String msg = rs.getString("MSG"); 			// "MSG" 는 select 해온 컬럼명이다. (SQL 의 MSG 컬럼을 읽어온다는 말이다.)			
			String writeday = rs.getString("WRITEDAY"); // "WRITEDAY" 는 select 해온 컬럼명이다. (SQL 의 WRITEDAY 컬럼을 읽어온다는 말이다.)						
		*/
		 // 또는	 
			int no = rs.getInt(1);					// 1 은 select 해온 첫번째 컬럼명이다. (SQL 의 NO 컬럼을 읽어온다는 말이다.) 
			String name = rs.getString(2); 			// 2 는 select 해온 두번째 컬럼명이다. (SQL 의 NAME 컬럼을 읽어온다는 말이다.)				
			String msg = rs.getString(3); 			// 3 은 select 해온 세번째 컬럼명이다. (SQL 의 MSG 컬럼을 읽어온다는 말이다.)			
			String writeday = rs.getString(4); 		// 4 는 select 해온 네번째 컬럼명이다. (SQL 의 WRITEDAY 컬럼을 읽어온다는 말이다.)						
			
			sb.append(no);
			sb.append("\t"+name);
			sb.append("\t"+msg);
			sb.append("\t"+writeday+"\n");
			
		 }// end of while(rs.next() ------------------------------------
		 
		 System.out.println(sb.toString());		// return 타입이 String 타입으로.
		 
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. 사용했던 자원을 반납하기 <<< //
			// 반납의 순서는 생성순서의 역순으로 한다.
			// pstmt 부터 순차적으로 닫는다.
			try {
				if(rs != null)			// 이렇게 if 절을 써주어야 nullPoniterException 이 뜨지 않는다.
					pstmt.close();		// 성공하든 실패하든 항상 닫는다 (finally)
	
				if(pstmt != null)		// 이렇게 if 절을 써주어야 nullPoniterException 이 뜨지 않는다.
					pstmt.close();		// 성공하든 실패하든 항상 닫는다 (finally)
	
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
			}	
			
		}	 
		
        sc.close();
        System.out.println("~~~ 프로그램 종료 ~~~");
        
	}// end of main() -----------------------------------

}
