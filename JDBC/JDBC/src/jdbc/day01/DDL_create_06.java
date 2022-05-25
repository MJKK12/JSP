package jdbc.day01;

import java.sql.*;

public class DDL_create_06 {

	public static void main(String[] args) {

		Connection conn = null;		
		PreparedStatement pstmt = null;		
		ResultSet rs = null;

        try {		
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");		// return 타입은 connection, 내 IP에 붙는 것
			// DDL 은 autocommit
			
			
			String sql_1 = " select * "
				         + " from user_tables "
				         + " where table_name = 'JDBC_TBL_EXAMTEST' "; 	// ';' 붙이지 않는다.
						
			String sql_2 = " drop table JDBC_TBL_EXAMTEST cascade constraints purge ";	// Constraint : 부모-자식 테이블 간 관계를 끊는 것
					
			String sql_3 = " create table jdbc_tbl_examtest "
						 + " (no   number(4) "
						 + " ,name varchar2(40) "
						 + " ,msg  varchar2(200) "
						 + " )";
			
			String sql_4 = " select * "
					     + " from user_sequences "
						 + " where sequence_name = 'JDBC_SEQ_EXAMTEST' ";	
			
			String sql_5 = "drop sequence JDBC_SEQ_EXAMTEST ";
			
			String sql_6 = " create sequence JDBC_SEQ_EXAMTEST"
			 			 + " start with 1 "
			 			 + " increment by 1 "
			 			 + " nomaxvalue "
						 + " nominvalue "
						 + " nocycle "
						 + " nocache ";
			
			String sql_7 = " insert into jdbc_tbl_examtest(no, name, msg) "
						 + " values(jdbc_seq_examtest.nextval, '이순신', '안녕하세요? 이순신입니다.') ";
			
			String sql_8 = " select * "
						 + " from jdbc_tbl_examtest "
						 + " order by no asc ";
					
			pstmt = conn.prepareStatement(sql_1); 
			// "JDBC_TBL_EXAMTEST" 테이블이 존재하는지 알아본다.
			
			rs = pstmt.executeQuery();			// 위 select 문을 execute 해라.

			int n = 0;
			if(rs.next()) {						// 커서로 한행한행 체크하는데 값이 없다면 false!!
				// "JDBC_TBL_EXAMTEST" 테이블이 존재하는 경우
				// 먼저 JDBC_TBL_EXAMTEST 을 drop 해야 한다.
				pstmt.close();				
				pstmt = conn.prepareStatement(sql_2);
				
				n = pstmt.executeUpdate();			// DDL 문
				/*  pstmt.executeUpdate()은  
                	DML(insert, update, delete, merge)문 및 DDL(create table, create view, drop table 등)문을 수행해주는 메소드로서 
                    결과값은 int 형태로서 DML문 이라면 적용된 행의 갯수가 나오고 DDL문 이라면 0이 나온다.
				*/
				System.out.println("drop table : " + n);
				// drop table : 0
			}
			
			// "JDBC_TBL_EXAMTEST" 테이블을 생성(create) 한다.
			pstmt = conn.prepareStatement(sql_3);
			n = pstmt.executeUpdate();			// DDL 문			
			/* pstmt.executeUpdate()은  
	        	DML(insert, update, delete, merge)문 및 DDL(create table, create view, drop table 등)문을 수행해주는 메소드로서 
	            결과값은 int 형태로서 DML문 이라면 적용된 행의 갯수가 나오고 DDL문 이라면 0이 나온다.
			 */
			System.out.println("create table : " + n);
			// create table : 0
			// table 이 있을 경우에 drop → create 순으로 나온다.
			
			//////////////////////////////////////////////////////////////////////////////
			
			pstmt = conn.prepareStatement(sql_4); 
			// "JDBC_TBL_EXAMTEST" 시퀀스가 존재하는지 알아본다.
			
			rs.close();
			rs = pstmt.executeQuery();			// DDL 문			

			if(rs.next()) {
				// "JDBC_SEQ_EXAMTEST" 시퀀스가 존재하는 경우
				
				// 먼저 "JDBC_SEQ_EXAMTEST" 시퀀스를 drop 한다.
				conn.prepareStatement(sql_5);
				n = pstmt.executeUpdate();			// DDL 문

				System.out.println("drop Sequence : " + n);
				// drop Sequence : 0
			}
			
			// "JDBC_SEQ_EXAMTEST" 시퀀스를 생성(create) 한다.
			pstmt = conn.prepareStatement(sql_6); 
			n = pstmt.executeUpdate();			// DDL 문			

			System.out.println("create Sequence : " + n);
			// create Sequence : 0
				
			pstmt = conn.prepareStatement(sql_7); 	// 이 편지(7번 sql 편지)를 전달할 우편배달부
			n = pstmt.executeUpdate();
			System.out.println("insert 를 한 DML문의 n : " + n);
			// insert 를 한 DML문의 n : 1
			
			pstmt = conn.prepareStatement(sql_8); 	// 이 편지(8번 sql 편지)를 전달할 우편배달부
			rs = pstmt.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			int cnt = 0;
			
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					System.out.println("------------------------------------------");
					System.out.println("일련번호\t성명\t글내용");
					System.out.println("------------------------------------------");
				}

				sb.append(rs.getInt("NO") + "\t" + rs.getString("NAME") + "\t" + rs.getString("MSG") + "\n" );	// 첫번째 no은 sequence 니까 getInt // StringBuilder에 차곡차곡 쌓는다.
			}// end of while(rs.next())----------------------------------------
		
			if(cnt > 0) {
				System.out.println(sb.toString());
			}
			else {
				System.out.println(">> 입력된 데이터가 없습니다. <<");
			}
				
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

        System.out.println("~~~ 프로그램 종료 ~~~");		// 닫은 후(close) 종료.
        
	}// end of main() -----------------------------------

}
