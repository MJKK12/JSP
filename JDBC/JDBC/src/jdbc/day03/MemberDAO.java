package jdbc.day03;

import java.sql.*;
import java.util.Map;

//DAO(Database Access Object) ==> 데이터베이스에 연결하여 SQL구문을 실행시켜주는 객체

public class MemberDAO implements InterMemberDAO {		// 재정의 해온다.

	// attribute, field, property, 속성
	Connection conn;
	PreparedStatement pstmt;	// 우편배달부!!
	ResultSet rs;
	
	
	// operation, method, 기능
	
	// === 자원반납을 해주는 메소드 === //
	private void close() {
		try {		// 밑에서부터 순서대로 닫는다.
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
			if(conn!=null) conn.close();
		} catch (SQLException e) {
				e.printStackTrace();
			}

	}// end of private void close()---------------------------

	
	// === 회원가입(insert) 메소드 구현하기 === //
	@Override		// 재정의 한다.
	public int memberRegister(MemberDTO member) {

		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// ① 오라클 드라이버 로딩
		
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");		// ② 어떤 오라클 서버에 붙을래?
			
			String sql = " insert into jdbc_member(userseq, userid, passwd, name, mobile)"
					   + " values(userseq.nextval, ?, ?, ?, ?) ";		// default 는 안넣어도 된다.	// ③ sql 문 작성
			
			pstmt = conn.prepareStatement(sql);		// ④ 편지전달할 객체(배달부) 생성
			pstmt.setString(1, member.getUserid());	// memberDTO 안에 있는 member 에서 끄집어 와서 넣어주겠다.
			pstmt.setString(2, member.getPasswd());	// memberDTO 안에 있는 member 에서 끄집어 와서 넣어주겠다.
			pstmt.setString(3, member.getName());	// memberDTO 안에 있는 member 에서 끄집어 와서 넣어주겠다.
			pstmt.setString(4, member.getMobile());	// memberDTO 안에 있는 member 에서 끄집어 와서 넣어주겠다.
			
			result = pstmt.executeUpdate();		// 다 넣어준 다음에 실행(execute) 해라!
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.			
		} catch (SQLIntegrityConstraintViolationException e) {
		//  System.out.println("에러코드번호 : " + e.getErrorCode());
		//  System.out.println("에러메시지 : " + e.getMessage());
			
			if(e.getErrorCode() == 1) {
				System.out.println(">> 아이디가 중복되었습니다. 새로운 아이디를 입력하세요.!! <<");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();	// 위에서 만든 자원반납 메소드를 넣음
		}
		
		return result;	// return 디폴트값 0 반납. 
	}// end of public int memberRegister(MemberDTO member)--------------------------

	
	
	// === 로그인처리(select) 메소드 구현하기 === (interMemberDAO 에 있는 login 메소드)
	@Override
	public MemberDTO login(Map<String, String> paraMap) {

		MemberDTO member = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
		
			String sql = " select userseq, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') AS registerday "+		// DB 에서 name 및 내 정보까지 보여주기 위함
						 " from jdbc_member "+																	// 오라클에서 alias 에 "" 넣어주면 ""안에 넣은 그대로 적어줘야함 (대/소문자까지) (왠만하면 alias 에 ""를 잘 붙이지 않는다.)
						 " where status = 1 and userid = ? and passwd = ? ";
			
			pstmt = conn.prepareStatement(sql);			// sql 문을 실행해야할 객체(우편배달부) 를 만든다.
			pstmt.setString(1, paraMap.get("userid"));	// paraMap 의 key인 "userid" 는 MemberCtrl 클래스의 99번 line 에서 정의해둔 것이다.
			pstmt.setString(2, paraMap.get("passwd"));	// paraMap 의 key인 "passwd" 는 MemberCtrl 클래스의 100번 line 에서 정의해둔 것이다.
			
			rs = pstmt.executeQuery();			// Result Set 으로 받는다.
			
			if(rs.next()) {	// select 된 행이 있으면 true, 없으면 false (return 값이 boolean)
				member = new MemberDTO();				// memberDTO 를 만들어서 거기에 넘겨준다.
			//	member.setName( rs.getString(1) ); 		// select 된 것들을 rs에 담았으므로, 내가 얻어오고자 하는 select name(varchar2) 이기 때문에 getString 을 사용한다. name 은 첫번째 컬럼이므로(1) 을 쓴다. // 이름을 넘겨준다.
			//	또는
			//  set 하면서 member 변수에 넣어준다. ▼
				member.setUserseq( rs.getInt("USERSEQ"));	// 오라클의 기본이 대문자이기 때문에, 관습적으로 대문자를 사용한다..// db의 name, NAME, MOBILE, POINT, REGISTERDAY 를 불러온다.
				member.setName( rs.getString("NAME") );		// 오라클의 기본이 대문자이기 때문에, 관습적으로 대문자를 사용한다..// db의 name, NAME, MOBILE, POINT, REGISTERDAY 를 불러온다.
				member.setMobile( rs.getString("MOBILE") ); 	
				member.setPoint( rs.getInt("POINT") );	
				member.setRegisterday( rs.getString("REGISTERDAY") );
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 자원반납 할 것 (위에서 만든 자원반납 메소드를 불러온다.)
			close();
		}
		
		
		return member;		// 결과물이 null 이라면 로그인 실패.!!! 로그인 시 member 를 넘겨준다.
		
	}// end of public MemberDTO login(Map<String, String> paraMap)---------------------------------------


	// === 회원탈퇴(Update) 메소드 구현하기 === // (DML 문 ▶ UPDATE)
	@Override
	public int memberDelete(int userseq) {
		
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// ① 오라클 드라이버 로딩
		
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");		// ② 어떤 오라클 서버에 붙을래?
			
			String sql = " update jdbc_member set status = 0 "
					   + " where userseq  = ? ";		// default 는 안넣어도 된다.	// ③ sql 문 작성
			
			pstmt = conn.prepareStatement(sql);		// ④ 편지전달할 객체(배달부) 생성
			pstmt.setInt(1, userseq);				// 첫번째 ? 에 입력받은 userseq 가 들어간다.
			
			result = pstmt.executeUpdate();			// 다 넣어준 다음에 실행(execute) 해라!
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();	// 위에서 만든 자원반납 메소드를 넣음
		}
		
		return result;	// return 디폴트값 0 반납. 		

	}// end of public int memberDelete(int userseq)------------------------------------------
	
}
