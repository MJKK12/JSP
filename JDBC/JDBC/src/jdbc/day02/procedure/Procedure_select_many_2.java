/*
>>>> Stored Procedure 란? <<<<<
Query 문을 하나의 파일형태로 만들거나 데이터베이스에 저장해 놓고 함수처럼 호출해서 사용하는 것임.
Stored Procedure 를 사용하면 연속되는(똑같은) query 문에 대해서 매우 빠른 성능을 보이며, 
코드의 독립성과 함께 보안적인 장점도 가지게 된다.

    create or replace procedure pcd_student_select_many         -- 복수개 가져오는 것.
    (p_addr_search  IN  VARCHAR2                                -- 주소 입력했을 때 (IN) 복수개의 결과(OUT) 출력하기 (ex. '마포'로 입력했을때 마포가 포함된 결과물 복수개 출력하기)
    ,o_data         OUT SYS_REFCURSOR                           -- 복수개는 '커서'인 SYS_REFCURSOR 를 사용한다. select 된 행들을 SYS_REFCURSOR 에 담아준다.
    )
    is    
    begin
        OPEN o_data FOR             -- OPEN 하고 o_data 에 담아주겠다.
        select S.stno, S.name, S.tel, S.addr, S.registerdate
             , C.classname, C.teachername
        from ( select stno, name, tel, addr 
                    , to_char(registerdate, 'yyyy-mm-dd hh24:mi:ss') AS registerdate
                    , fk_classno
               from jdbc_tbl_student 
               where addr like '%'||p_addr_search||'%' ) S JOIN jdbc_tbl_class C
        ON S.fk_classno = C.classno;
    end pcd_student_select_many;
    -- Procedure PCD_STUDENT_SELECT_MANY이(가) 컴파일되었습니다.

*/

package jdbc.day02.procedure;

import java.sql.*;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;

public class Procedure_select_many_2 {

	public static void main(String[] args) {

		Connection conn = null;
	 // Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		CallableStatement cstmt = null;
		// CallableStatement cstmt 은 Connection conn(연결한 오라클 서버)에 존재하는 Procedure 를 호출할 객체(우편배달부)이다.
		
		ResultSet rs = null;
	 // ResultSet rs 은 select 된 결과물이 저장되는 곳이다. (rs 에 다 들어온다.)
		
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
			cstmt = conn.prepareCall("{call pcd_student_select_many(?,?)}");	// 프로시져를 호출해주는 우편배달부이다.(오라클로부터) // 파라미터가 String 타입이므로 "" 안에 기입.
			
			/*
            오라클 서버에 생성한 프로시저  pcd_student_select_many 의 
            매개변수 갯수가 2개 이므로 ? 를 2개 준다.
                    
            다음으로 오라클의 프로시저를 수행( executeUpdate() ) 하기에 앞서서  
            반드시 해야할 일은 IN mode 로 되어진 파라미터에 값을 넣어주고,
           	OUT mode 로 설정된 곳에 그 결과값을 받아오도록 아래와 같이 설정해야 한다.    
                   
            프로시저의 IN mode 로 되어진 파라미터에 값을 넣어줄때는 
           	cstmt.setXXX() 메소드를 사용한다.
            
            프로시저의 OUT mode 로 되어진 파라미터에 저장되어진 값을 자바에서 꺼내 오려면 
           	cstmt.registerOutParameter() 메소드를 사용한다.
                   
           ※ registerOutParameter() 메소드는? ★OUT 모드★
            ==> public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException 
                : 프로시저를 실행하여 받아온 값을 JDBC타입(자바에서 인식하는 타입)으로 등록시켜주는 메소드이다.
             
             자바에서는 오라클의 OUT mode 변수에 오라클 데이터타입으로 저장되어 있는 값들을 읽어와서
           	 ★JDBC타입(자바에서 인식하는 타입)으로 변경하는 과정을 거쳐야만 한다.★
             대표적인  sqlType을 알아보면 NULL, FLOAT(실수), INTEGER(정수), VARCHAR(문자열), DATE(날짜), CLOB, BLOB 등이 있다.
         */
			
			Scanner sc = new Scanner(System.in);
			System.out.print("▷ 주소 : ");			// 검색할 주소 입력(IN MODE)
			String addr_search = sc.nextLine();

			// ▼▼▼ 오라클의 OUT 모드 값을 JDBC타입(자바에서 인식하는 타입)으로 변경하는 과정
			cstmt.setString(1, addr_search);		// 숫자 1은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ?를 말한다. ==> IN 모드인 첫번째에 주소를 넣어주겠다.
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);	// 숫자 2는 프로시저 파라미터중 두번째 파라미터인 OUT 모드의 ?를 말한다. ==> 복수개는 CURSOR 를 쓴다.
			
			// >>> 4. CallableStatement cstmt 객체를 사용하여 오라클의 프로시저 실행하기 <<< //
			cstmt.executeUpdate();		// 오라클 서버에게 해당 프로시저를 실행하라는 것이다. 프로시저는 무조건 executeUpdate 이다. 
			// 프로시저의 실행은 cstmt.executeUpdate(); 또는 cstmt.execute() 이다.

			// 지금의 프로시저 실행된 결과는 프로시저의 2번째 파라미터(OUT MODE) 인 곳에 저장된다.
			// 그래서 2번째 파라미터에 저장된 정보를 꺼내오도록 한다.
			rs = (ResultSet) cstmt.getObject(2);			// 두번째 ?의 파라미터에 해당하는 결과물에 이름,전화번호,주소 등 **숫자와 문자가 혼합**되어 있음 (int, String타입이 혼합됨)
			// 여기서 숫자 2는 프로시저의 파라미터 순서를 말한다.
            // 즉, 2번째 파라미터에 저장되어진 정보를 꺼내오는데 리턴타입은 Object 이다.	(Object 는 객체들의 최상위 타입!!)
            // 여기서 2번째 파라미터는 CURSOR로 되어진 OUT 모드이며 select 되어진 결과물이다.
            // 그러므로 Object 타입으로 리턴된 것을 ResultSet 타입으로 casting(강제형변환)시켜야 한다. (select 된 것은 ResultSet 에 넣어주어야 한다.)
			
			StringBuilder sb = new StringBuilder();
			
			int cnt = 0;
			
			while(rs.next()) {		// select 된 것이 없으면 수행하지 X
				cnt++;
				
				if(cnt == 1) {
					System.out.println("------------------------------------------------------------------------------------");
					System.out.println("학번  \t성명  \t연락처  \t\t주소  \t\t입학일자  \t\t과정명  \t\t교사명");
					System.out.println("------------------------------------------------------------------------------------");
				}
				
				int stno = rs.getInt("STNO");
				String name = rs.getString("NAME");
				String tel = rs.getString("TEL");
				String addr = rs.getString("ADDR");
				String registerdate = rs.getString("REGISTERDATE");
				String classname = rs.getString("CLASSNAME");
				String teachername = rs.getString("TEACHERNAME");
				
				// StringBuilder 에 쌓아서 출력해준다.
				sb.append(stno+" \t"+name+" \t"+tel+" \t"+addr+" \t"+ registerdate+" \t"+ classname +" \t"+teachername+"\n");
				
			}// end of while(rs.next())------------------------------
			
			
			
			if(cnt == 0) {
				System.out.println(">> 검색하신 "+addr_search+"에 거주하는 학생은 없습니다. << ");
			}
			else {
				System.out.println(sb.toString());	// sb 에 쌓아둔 것을 출력해라.
			}
			
			sc.close();
			
		 } catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.
	 	 } catch (SQLException e) {
			e.printStackTrace();
		 } finally {
			// >>> 6. 사용했던 자원을 반납하기 <<< //
			// 반납의 순서는 생성순서의 역순으로 한다.
			// pstmt 부터 순차적으로 닫는다.
			 
			try {
				if(rs != null)		// 이렇게 if 절을 써주어야 nullPoniterException 이 뜨지 않는다.
					rs.close();		// 성공하든 실패하든 항상 닫는다 (finally)
	
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
