package jdbc.day01;

import java.sql.*;
import java.util.Scanner;

public class DML_insert_01 {

	public static void main(String[] args) {

		Connection conn = null;
	 // Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		PreparedStatement pstmt = null;
	 // PreparedStatement pstmt 은 Connection conn(특정 오라클 서버)에 전송할 SQL문(편지)을 전달할 객체(우편배달부)이다.
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
																										// () 안을 잘못 입력 시 sql exception 이 떨어진다.
			// ==== Connection conn 기본값은 auto commit 이다. ==== //
			// ==== Connection conn 의 기본값인 auto commit 을 수동 commit 으로 전환해보겠다. ==== //
			// DML 이므로 원래 auto commit 임. (auto commit 은 rollback 불가함)
			conn.setAutoCommit(false);    // 수동 commit 으로 전환 ▶ false : auto commit 을 하지 않겠다(false)  or 하겠다 (true)
			
			
			// >>> 3. SQL문(편지)을 작성한다. <<< //
			System.out.print("▷ 글쓴이 : ");
			String name = sc.nextLine();	  // sql 컬럼명과 똑같이 맞춰준다.

			System.out.print("▷ 글내용 : ");
			String msg = sc.nextLine();	  	  // sql 컬럼명과 똑같이 맞춰준다.
		/*	
			String sql = "insert into jdbc_tbl_memo(no, name, msg)"
					   + "values(jdbc_seq_memo.nextval, '"+name+"', '"+msg+"')";		// name , msg 로 변수처리를 하면 보안상 위험하므로 권장하지 않는다.
		*/
			// ==> 이렇게 하지 마시고 아래와 같이 위치홀더를 쓰세요~~~!!!
		
			String sql = " insert into jdbc_tbl_memo(no, name, msg) "
					   + " values(jdbc_seq_memo.nextval, ?, ?) "; // SQL문 맨 뒤에 ; 를 넣으면 오류이다.!!!
			// ? 를 "위치홀더" 라고 부른다.
		    // ? 의 값을 mapping 해주어야 한다.
			// -- 위치홀더는 ''를 쓰지 않는다. (문자, 숫자 ,날짜이건 ''를 쓰지 않고 그냥 ? 이다.)// 보안을 위해 보이지 않게끔 ?를 쓴다.		
			// 위의 sql 편지를 우편배달원이 배송해주어야 한다.
			//* 테이블명이 잘못되면 : java.sql.SQLSyntaxErrorException: ORA-00942: table or view does not exist 라고 뜬다.
			
			
			
			// >>> 4. 연결한 오라클서버(conn)에 SQL문(편지)을 전달할 PreparedStatement 객체(우편배달부) 생성하기 <<< //
			pstmt = conn.prepareStatement(sql); // ▼위의 String sql 에서 첫번째 ? 에 문자열이 오면 setString, 숫자면 setInt 를 쓴다.
			pstmt.setString(1, name);			// 1 은 String sql 에서 첫번째 위치홀더(?)를 말한다. 첫번째 위치홀더(?)에 name 을 넣어준다.
			pstmt.setString(2, msg);			// 2 는 String sql 에서 두번째 위치홀더(?)를 말한다. 두번째 위치홀더(?)에 msg 을 넣어준다.
			
						
			// >>> 5. PreparedStatement pstmt 객체(우편배달부)는 작성된 SQL문(편지)을 오라클 서버에 보내서 실행이 되도록 해야 한다 <<< //
			int n = pstmt.executeUpdate();
			/*  .executeUpdate(); 은 SQL문이 DML문(insert, update, delete, merge) 이거나 
			            		    SQL문이 DDL문(create, drop, alter, truncate) 일 경우에 사용된다. 
			
			SQL문이 DML문이라면 return 되어지는 값은 적용되어진 행의 개수를 리턴시켜준다.
			예를 들어, insert into ... 하면 1 개행이 입력되므로 리턴값은 1 이 나온다. 
			 update ... 할 경우에 update 할 대상의 행의 개수가 5 이라면 리턴값은 5 가 나온다. 
			 delete ... 할 경우에 delete 되어질 대상의 행의 개수가 3 이라면 리턴값은 3 가 나온다.
			 ex) 부서번호 = 80번 delete 일 경우 20행이 삭제된다면 리턴값은 20.!! (20명에 해당하는 행의 갯수가 삭제된다.)
			 
			SQL문이 DDL문이라면 return 되어지는 값은 무조건 0 이 리턴된다.       
			
			.executeQuery(); 은 SQL문이 DQL문(select) 일 경우에 사용된다.
			
			*/
			// ▼ 여기서 N 일때 출력되는 부분 수정.
			
			if(n == 1) {
				
				String yn = "";
				
				do {
				//////////////////////////////////////////////////////////////////////
					System.out.print("▷ 정말로 입력하시겠습니까?[Y/N] : ");
					yn = sc.nextLine();
					if("y".equalsIgnoreCase(yn)) {
						conn.commit();// y 가 대/소문자 상관없이 y 라면 commit 한다 (수동 commit)
						System.out.println(">> 데이터 입력 성공!! <<");
					}
					else if("n".equalsIgnoreCase(yn)) {
						conn.rollback();// y 가 아니라면 (n이라면) 롤백.
						System.out.println(">> 데이터 입력 취소 !! <<");
					}
					else {
						System.out.println(">> Y 또는 N 만 입력하세요. << \n");	// y 또는 n 이외의 것을 입력했을 때.
					}
				//////////////////////////////////////////////////////////////////////
				} while(!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn))); // y나 n을 썼을때만 빠져나간다.
			}// end of if------------------------------
			
			
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. 사용했던 자원을 반납하기 <<< //
			// 반납의 순서는 생성순서의 역순으로 한다.
			// pstmt 부터 순차적으로 닫는다.
			try {
				if(pstmt != null)		// 이렇게 if 절을 써주어야 nullPointException 이 뜨지 않는다.
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
