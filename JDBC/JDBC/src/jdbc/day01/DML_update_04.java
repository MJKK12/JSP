package jdbc.day01;

import java.sql.*;
import java.util.Scanner;

public class DML_update_04 {

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
			// UPDATE 는 DML 이므로 수동 commit 으로 바꿔준다 ▼
			conn.setAutoCommit(false); // 수동 commit 으로 전환
			

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
	
			 System.out.print("▷ 수정할 글번호 : ");
			 String no = sc.nextLine();	
			 
			 sql = " select name, msg " 
				 + " from jdbc_tbl_memo "
			 	 + " where no = ? ";
			 
			 pstmt.close();
			 pstmt = conn.prepareStatement(sql);	// sql 문을 실행시킬 우편배달부가 필요하다.
			 pstmt.setString(1, no);				// setInt 후 no 앞에 parseint 를 붙여도 되지만 setString 으로 쓰는것이나 똑같다. (모두 호환된다.)
			 
			 rs.close();
			 rs = pstmt.executeQuery();				// return 타입이 ResultSet.
			 
			 if(rs.next()) {						//select 된 것이 있느냐.
				 
				 String name = rs.getString(1);		// 첫번째 컬럼
				 String msg = rs.getString(2);		// 두번째 컬럼
				 
				 System.out.println("\n=== 수정하기 전 내용 ===");
				 System.out.println("\n□ 글쓴이 : " + name);
				 System.out.println("□ 글내용 : " + msg);
				 
				 System.out.println("\n=== 글 수정하기 ===");
				 System.out.print("▷ 글쓴이 : ");
				 name = sc.nextLine();
				 
				 System.out.print("▷ 글내용 : ");
				 msg = sc.nextLine();

				 sql = " update jdbc_tbl_memo set name = ? "
					 + " 						 ,msg = ? "
					 + " where no = ? ";
				 
				 pstmt = conn.prepareStatement(sql);
				 pstmt.setString(1, name);
				 pstmt.setString(2, msg);
				 pstmt.setString(3, no);
				 
				 int n = pstmt.executeUpdate();
				 
				 if(n==1) {
					 // n==1 라는 뜻은 update 구문이 성공했다는 말이다.
					 String yn = "";
					 do {
						 System.out.print("▷ 정말로 수정하시겠습니까?[Y/N] : ");
						 yn = sc.nextLine();
						 
						 if("y".equalsIgnoreCase(yn)) {
							 conn.commit(); // 커밋
							 System.out.println(">> 데이터 수정 성공!! <<");
							 
							 System.out.println("\n=== 수정한 후 내용 ===");

							 sql = " select name, msg "
							 	 + " from jdbc_tbl_memo "
							 	 + " where no = ? ";
							 
							 pstmt = conn.prepareStatement(sql);	// 변경된 sql 문
							 pstmt.setString(1, no);
							 
							 rs = pstmt.executeQuery();				// select 된 것은 resultSet 이다.
							 
							 rs.next();								// 이 line 꼭 들어가야만 커서가 아래로 내려간다.!!
							 
							 name = rs.getString(1);				// 컬럼 보이기.
							 msg = rs.getString(2);

							 System.out.println("□ 글쓴이 : " + name);
							 System.out.println("□ 글내용 : " + msg);							 
						 }
						 
						 else if("n".equalsIgnoreCase(yn)){
							 conn.rollback(); // 롤백
							 System.out.println(">> 데이터 수정 취소!! <<");		// 롤백했으므로 우리가 스스로 데이터를 수정 취소한 것임. (실패가 X)
							 
						 }
						 
						 else {
							 System.out.println(">> Y 또는 N 만 입력하세요!! <<\n");
						 }
						 
					 } while(!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn))); // y 나 n 이외의 것을 입력했으면 계속 반복
				 }
				 
			 }// end of if(rs.next())-------------------------------------
			 
			 else {
				 System.out.println(">>> 글번호 "+no+" 는 존재하지 않습니다. <<<\n");
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
		
        sc.close();
        System.out.println("~~~ 프로그램 종료 ~~~");		// 닫은 후(close) 종료.
        
	}// end of main() -----------------------------------

}
