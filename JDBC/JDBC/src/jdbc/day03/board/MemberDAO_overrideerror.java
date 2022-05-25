package jdbc.day03.board;

import java.sql.*;
import java.util.*;

import jdbc.day03.MemberDTO;

// 회원만 관리하는 DAO
public class MemberDAO_overrideerror {

	// field, attribute, property, 속성
	Connection conn;	// DAO 이므로 connection 이 필요함 (DB 서버가 필요하다.)
	PreparedStatement pstmt;
	ResultSet rs;				// → 이거 하고 자원반납 해야한다.
	
	// operation, method, 기능
	
	// === 자원반납을 해주는 메소드 구현하기 === //
	@Override
	public void close() {

			try {
				if(rs != null) 		rs.close();
				if(pstmt != null) 	pstmt.close();
				if(conn != null) 	conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
	} // end of public void close()----------------------------
	
	
	// 회원가입시 사용가능한 아이디 인지 중복된 아이디라서 사용 불가한지 알려주는 메소드 
	@Override
	public boolean isUse_userid(String userid) {
		
		boolean isUse = false;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// ① 오라클 드라이버 로딩
		
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");		// ② 어떤 오라클 서버에 붙을래?
			
			String sql = " select * "
					   + " from jdbc_member "
					   + " where userid = ? ";		// default 는 안넣어도 된다.	// ③ sql 문 작성
			
			pstmt = conn.prepareStatement(sql);		// ④ 편지전달할 객체(배달부) 생성
			pstmt.setString(1, userid);	// memberDTO 안에 있는 member 에서 끄집어 와서 위치홀더 "?"에 넣어주겠다.

			rs = pstmt.executeQuery();			// 다 넣어준 다음에 실행(execute) 해라!, return 타입이 int.
			
			if(rs.next()) {
				// userid 가 이미 존재하는 아이디인 경우이다. // rs.next 가 true 면 이미 존재하는 아이디기 때문에 쓸 수 없음 . 그러나 false 면 존재 하지 x 기 때문에 사용 가능.				
				isUse = false;
			}
			else {
				// userid 가 이미 존재하지 않는 아이디인 경우이다.
				isUse = true;
			}
			
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}	
		
		return isUse;
	}// end of public boolean isUse_userid(String userid)----------------------------
	
	
	// === 회원가입(insert) 메소드 구현하기 === //
	@Override
	public int memberRegister(MemberDTO member) {

		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// ① 오라클 드라이버 로딩
		
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");		// ② 어떤 오라클 서버에 붙을래?
			
			String sql = " insert into jdbc_member(userseq, userid, passwd, name, mobile)"
					   + " values(userseq.nextval, ?, ?, ?, ?) ";		// default 는 안넣어도 된다.	// ③ sql 문 작성
			
			pstmt = conn.prepareStatement(sql);		// ④ 편지전달할 객체(배달부) 생성
			pstmt.setString(1, member.getUserid());	// memberDTO 안에 있는 member 에서 끄집어 와서 위치홀더 "?"에 넣어주겠다.
			pstmt.setString(2, member.getPasswd());	// memberDTO 안에 있는 member 에서 끄집어 와서 위치홀더 "?"에 넣어주겠다.
			pstmt.setString(3, member.getName());	// memberDTO 안에 있는 member 에서 끄집어 와서 위치홀더 "?"에 넣어주겠다.
			pstmt.setString(4, member.getMobile());	// memberDTO 안에 있는 member 에서 끄집어 와서 위치홀더 "?"에 넣어주겠다.
			
			result = pstmt.executeUpdate();			// 다 넣어준 다음에 실행(execute) 해라!, return 타입이 int.
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");	// 내가 이 파일을 올리지 않았다는 뜻.			
		} catch (SQLException e) {	// userid 가 중복일 경우 SQLException 오류가 떨어짐.
		//	e.printStackTrace();
		} finally {
			close();	// 위에서 만든 자원반납 메소드를 넣음
		}
		
		return result;	// return 디폴트값 0 반납. (위에서 선언한 result 기본값 0)
	}// end of public int memberRegister(MemberDTO member)--------------------------

	
	// *** 로그인 처리(select) 메소드 구현하기 *** //
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		
		MemberDTO member = null; // 이 member 를 넘겨주어야 한다. (맨 아래 return에 )
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select userseq, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') AS registerday " // 이 뒤에 공백 주는 것 잊지 않기!
					+ " from jdbc_member "
					+ " where status = 1 and userid = ? and passwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));		// TotalController 의 hashMap 에서 put 해준 것.
			pstmt.setString(2, paraMap.get("passwd"));
			
			rs = pstmt.executeQuery();

			if(rs.next()) {		// 아래 컬럼에서 select 된 행이 있는가? (rs.next)
				member = new MemberDTO();
			
				member.setUserseq(rs.getInt(1));		// 1번째 컬럼.
				member.setName(rs.getString(2));		// 2번째 컬럼.
				member.setMobile(rs.getString(3));		// 3번째 컬럼.
				member.setPoint(rs.getInt(4));			// 4번째 컬럼.
				member.setRegisterday(rs.getString(5));	// 5번째 컬럼. (컬럼명을 쓸 것이라면 alias 가 반드시 들어가야 하지만, 여기선 그렇지 않기 때문에 위의 sql문에서 alias 를 쓸 필요가 없다.
				
			
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");				
		} catch(SQLException e) {			
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return member;
		
	}// end of public MemberDTO login(Map<String, String> paraMap)------------------






}
