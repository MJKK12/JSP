/*
>>>> Stored Procedure 란? <<<<<
Query 문을 하나의 파일형태로 만들거나 데이터베이스에 저장해 놓고 함수처럼 호출해서 사용하는 것임.
Stored Procedure 를 사용하면 연속되는(똑같은) query 문에 대해서 매우 빠른 성능을 보이며, 
코드의 독립성과 함께 보안적인 장점도 가지게 된다.

    create or replace procedure pcd_jdbc_tbl_student_insert
    (p_stno           IN jdbc_tbl_student.stno%type   -- 학번
    ,p_name           IN jdbc_tbl_student.name%type   -- 학생명
    ,p_tel            IN jdbc_tbl_student.tel%type   -- 연락처
    ,p_addr           IN jdbc_tbl_student.addr%type    -- 주소
    ,p_fk_classno     IN jdbc_tbl_student.fk_classno%type
    )
    is  -- 변수선언
        v_day         varchar2(1);
        v_hour        varchar2(2);   
        error_dayTime exception;
    begin
        -- 오늘의 요일명 알아오도록 한다.
        v_day := to_char(sysdate, 'd');  -- '1','2','3','4','5','6','7'  (여기서 '1' 은 일요일이다.)
                                         --  일, 월, 화, 수, 목, 금, 토
        v_hour := to_char(sysdate, 'hh24');
        
        if ( v_day in('1','7') OR                       -- error 띄우는 if 절
             v_hour < '09' or v_hour >'13' ) then 
            raise error_dayTime;
        else                                            -- 정상일 때 띄우는 else 절
            insert into jdbc_tbl_student(stno, name, tel, addr, fk_classno) 
            values(p_stno, p_name, p_tel, p_addr, p_fk_classno);
        end if;
         
        exception
            when error_dayTime then
                 raise_application_error(-20005,'>> 영업시간(월~금 09시~14시이전)마감 이므로 insert 를 할 수 없습니다. <<');       -- 사용자가 정의하는 error 범위 (-20001~20999)
                                       
    end pcd_jdbc_tbl_student_insert;
    -- Procedure PCD_JDBC_TBL_STUDENT_INSERT이(가) 컴파일되었습니다.

*/

package jdbc.day02.procedure;

import java.sql.*;
import java.util.Scanner;

public class Procedure_insert_exception_03 {

	public static void main(String[] args) {

		Connection conn = null;
	 // Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		CallableStatement cstmt = null;
		// CallableStatement cstmt 은 Connection conn(연결한 오라클 서버)에 존재하는 Procedure 를 호출할 객체(우편배달부)이다.
		
		String fk_classno = "";
		String stno = "";
		
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
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
				
			// >>> 3. Connection conn 객체를 사용하여 prepareCall() 메소드를 호출함으로써  
			//		  CallableStatement cstmt 객체를 생성한다.
			//		  즉, CallableStatement cstmt 객체가 우편배달부(택배기사) 쯤에 해당하는 것이다. <<< //
			cstmt = conn.prepareCall("{call pcd_jdbc_tbl_student_insert(?,?,?,?,?)}");	// 프로시져를 호출해주는 우편배달부이다.(오라클로부터) // 파라미터가 String 타입이므로 "" 안에 기입.
			
			/*
	            오라클 서버에 생성한 프로시저  pcd_jdbc_tbl_student_insert 의 
	            매개변수 갯수가 5개 이므로 ? 를 5개 준다.
	                    
	            다음으로 오라클의 프로시저를 수행( executeUpdate() ) 하기에 앞서서  
	            반드시 해야할 일은 IN mode 로 되어진 파라미터에 값을 넣어준다.
			*/
			
			Scanner sc = new Scanner(System.in);
			System.out.print("▷ 학번 : ");			// 검색할 주소 입력(IN MODE)
			stno = sc.nextLine();
			
			System.out.print("▷ 성명 : ");	
			String name = sc.nextLine();
			
			System.out.print("▷ 연락처 : ");	
			String tel = sc.nextLine();
			
			System.out.print("▷ 주소 : ");	
			String addr = sc.nextLine();

			System.out.print("▷ 학급번호 : ");	
			fk_classno = sc.nextLine();
			
			cstmt.setString(1, stno);		// 숫자 1은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			cstmt.setString(2, name);		// 숫자 2은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			cstmt.setString(3, tel);		// 숫자 3은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			cstmt.setString(4, addr);		// 숫자 4은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			cstmt.setString(5, fk_classno);	// 숫자 5은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			
			// >>> 4. CallableStatement cstmt 객체를 사용하여 오라클의 프로시저 실행하기 <<< //
			int n =cstmt.executeUpdate();		// 오라클 서버에게 해당 프로시저를 실행하라는 것이다. 프로시저는 무조건 executeUpdate 이다. 
			// 프로시저의 실행은 cstmt.executeUpdate(); 또는 cstmt.execute() 이다.

			if(n == 1) {
				System.out.println(">> 데이터 입력 성공 !! <<");
			}
			
			sc.close();
			
		 } catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.
	 	 } catch (SQLException e) {
			// e.printStackTrace();
	 		int errorCode = e.getErrorCode();
	 		// 빨간 errorCode 를 뜨게 하지 않고 사용자가 직접 문구를 띄우기
	 		if(errorCode == 20005) {
	 			System.out.println(e.getMessage());
	 			/*
		 		  ORA-20005: >> 영업시간(월~금 09시~17시이전)마감 이므로 insert 를 할 수 없습니다. <<
				  ORA-06512: at "HR.PCD_JDBC_TBL_STUDENT_INSERT", line 28
				  ORA-06512: at line 1
		 		*/
	 		}
	 		else if (errorCode == 2291) {
				System.out.println(">> 학급번호 "+fk_classno+" 는 존재하지 않는 학급번호 입니다.학급번호를 올바르게 입력하세요. <<");	 			
	 		}
	 		else if (errorCode == 1) {
				System.out.println(">> 학번 "+stno+" 는 이미 사용중이므로 다른 학번을 입력하세요. <<");	 				 			
	 		}
	 		else {
	 			e.printStackTrace();

	 		}
	 		
		 } finally {
			// >>> 6. 사용했던 자원을 반납하기 <<< //
			// 반납의 순서는 생성순서의 역순으로 한다.
			// pstmt 부터 순차적으로 닫는다.
			 
			try {
				if(cstmt != null)		// 이렇게 if 절을 써주어야 nullPoniterException 이 뜨지 않는다.
					cstmt.close();		// 성공하든 실패하든 항상 닫는다 (finally)
	
				if(conn != null)
					conn.close();
			} catch (SQLException e) {

			}	
			
		}	 

		System.out.println("\n~~~ 프로그램 종료 ~~~");
		
	}// end of main()------------------------------------------------

}
