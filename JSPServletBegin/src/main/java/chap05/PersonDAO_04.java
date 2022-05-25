package chap05;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class PersonDAO_04 implements interPersonDAO_03 {

	private DataSource ds;		// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 기본 생성자
	public PersonDAO_04() {
	    
		try {		
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/myoracle");
		    // "jdbc/myoracle" 는 web.xml 에 있는 <res-ref-name> 이다.
		    // 이는 context.xml에 있는 name에 해당한다. (오라클 DB와 연결)
		} catch (NamingException e) {
			e.printStackTrace();
		}
	    
	}
	
	// 자원 반납해주는 메소드
	private void close() {
		// 사용된 것을 닫아야(close) 한다. null이 아니라면, 자원반납
		try {
			if(rs != null) 		{ rs.close(); 		rs = null;	}
			if(pstmt != null) 	{ pstmt.close(); 	rs = null;	}
			if(conn != null) 	{ conn.close(); 	rs = null;	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}// end of private void close()-----------------------

	// 개인성향을 입력(insert)해주는 메소드 구현하기
	@Override
	public int personRegister(personDTO_02 psdto) throws SQLException {

		int n = 0;	

		try {
			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " insert into tbl_person_interest(seq, name, school, color, food) "
					   + " values(person_seq.nextval, ?, ?, ?, ?) ";
			// 이때 sql문 작성 실패 시 sqlException 을 throws 한다.
			pstmt = conn.prepareStatement(sql);
	
			// 위치홀더에 값을 넣어준다.
			pstmt.setString(1, psdto.getName());
			pstmt.setString(2, psdto.getSchool());
			pstmt.setString(3, psdto.getColor());
			
			if(psdto.getFood() != null)	{	// psdto.getFood() 이 null 일때/아닐때의 경우의수 대비
				pstmt.setString(4, String.join(",", psdto.getFood()));	// return 타입이 String[] 배열타입이므로 String 타입으로 바꿔준다. (, 로 묶어서 하나의 문자열로 바꿔준다.)
			}
			else {	// null 일 때, 
				pstmt.setString(4, null);	// 좋아하는 음식이 없다.
			}
			
			n = pstmt.executeUpdate();			// DML 문이므로 executeUpdate(); (DML,DDL 은 executeUpdate();)
			// executeUpdate(); 메소드는 처리한 row의 갯수(숫자) 를 반환한다. 처리항목이 없다면 0을 반환한다.
		} finally {	
			close();	// 자원을 반드시 반납한다.
		}
		
		return n;
	}// end of public int personRegister(personDTO_02 psdto)-----------

	// tbl_person_interest 테이블에 저장된 행(데이터)들을 select 해주는 메소드 구현하기
	@Override
	public List<personDTO_02> selectAll() throws SQLException {

		List<personDTO_02> personList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();	// 풀장에서 남은 튜브 한개를 가져온다(connection 을 가져온다)
			
			String sql = " select seq, name, school, color, food, to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "
					   + " from tbl_person_interest "
					   + " order by seq ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();	// select 문이므로 ? 위치홀더가 없고, DQL 문이기때문에 executeQuery()를 쓴다.
			
			while(rs.next()) { //select 된 것 만큼 반복한다. DTO(행들) 하나하나를 담아야 한다.
				
				personDTO_02 psdto = new personDTO_02();
				psdto.setSeq(rs.getInt(1));	// Int 타입, 1번째 컬럼
				psdto.setName(rs.getString(2));
				psdto.setSchool(rs.getString(3));
				psdto.setColor(rs.getString(4));
				
				// 배열일 때는, split 하면 된다. 그러나 null 값은 split 이 안되므로 nullPointerException 이 발생한다.
				// 이때, 오라클에서 food 가 null값(선택X) 인 행이 있다.
				// 따라서 읽어오는 음식이 null 이 아니면 split 를, null 이면 split 할 수가 없다.
				String food = rs.getString(5);			
				if(food != null) {
					//null 이 아니면 구분자를 기준으로 split 하겠다.
					psdto.setFood( food.split("\\,") );	// 배열이 들어온다. 구분자는 "," 로 해서 split 하겠다. (food 컬럼에)
				}
				else {
					// null 이면 split 할 수가 없다.
					psdto.setFood(null);
				}
				
				psdto.setRegisterday(rs.getString(6));
				
				personList.add(psdto);	//List 에 psdto(personDTO) 를 담자. (select 된 결과물 끝까지!)

			}// end of while------------------------------------
			
		} finally {
			close();
		}
		
		return personList;	// personList 가 리턴된다.
	}// end of public List<personDTO_02> selectAll()-----------------
	
	
	// tbl_person_interest 테이블에 저장된 특정 1개 행만 select 해주는 메소드 구현하기
	@Override
	public personDTO_02 selectOne(String seq) throws SQLException {
		
		personDTO_02 psdto = null;
		
		try {
			
			conn = ds.getConnection();	// 풀장에서 남은 튜브 한개를 가져온다(connection 을 가져온다)
			
			String sql = " select seq, name, school, color, food, to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "
					   + " from tbl_person_interest "
					   + " where seq = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq);
			
			rs = pstmt.executeQuery();	// select 문이므로 ? 위치홀더가 없고, DQL 문이기때문에 executeQuery()를 쓴다.
			
			if(rs.next()) { //select 된 것 만큼 반복한다. DTO(행들) 하나하나를 담아야 한다.
				
				psdto = new personDTO_02();
				psdto.setSeq(rs.getInt(1));	// Int 타입, 1번째 컬럼
				psdto.setName(rs.getString(2));
				psdto.setSchool(rs.getString(3));
				psdto.setColor(rs.getString(4));
				
				// 배열일 때는, split 하면 된다. 그러나 null 값은 split 이 안되므로 nullPointerException 이 발생한다.
				// 이때, 오라클에서 food 가 null값(선택X) 인 행이 있다.
				// 따라서 읽어오는 음식이 null 이 아니면 split 를, null 이면 split 할 수가 없다.
				String food = rs.getString(5);			
				if(food != null) {
					//null 이 아니면 구분자를 기준으로 split 하겠다.
					psdto.setFood( food.split("\\,") );	// 배열이 들어온다. 구분자는 "," 로 해서 split 하겠다. (food 컬럼에)
				}
				else {
					// null 이면 split 할 수가 없다.
					psdto.setFood(null);
				}
				
				psdto.setRegisterday(rs.getString(6));
				
			}// end of if------------------------------------
			
		} finally {
			close();
		}
		
		return psdto;
	}// end of public personDTO_02 selectOne(String seq)------------------

	
	// tbl_person_interest 테이블에 저장된 특정 1개 행만 delete 해주는 메소드 구현하기
	@Override
	public int deletePerson(String seq) throws SQLException {

		int n = 0;

		try {
			
			conn = ds.getConnection();	// 풀장에서 남은 튜브 한개를 가져온다(connection 을 가져온다)
			
			String sql = " delete from tbl_person_interest "
					   + " where seq = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq);
			
			n = pstmt.executeUpdate();	

		} finally {
			close();
		}

		return n;
	}// end of public int deletePerson(String seq)-------------


	// tbl_person_interest 테이블에 저장된 특정 1개 행만 update 해주는 메소드 구현하기
	@Override
	public int updatePerson(personDTO_02 psdto) throws SQLException {

		int n = 0;
		
		try {
			
			conn = ds.getConnection();	// 풀장에서 남은 튜브 한개를 가져온다(connection 을 가져온다)
			// 이름, 학력, 색깔, 음식을 다 바꾸는 것으로 한다.
			String sql = " update tbl_person_interest set name = ?, school = ?, color = ?, food = ? "
					   + " where seq = ? ";	// PK키.
			
			// 위치홀더의 값을 mapping 시키자. (dto 에서 get 해온다.)
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, psdto.getName());
			pstmt.setString(2, psdto.getSchool());
			pstmt.setString(3, psdto.getColor());
			
			String[] foodArr = psdto.getFood();
			if(foodArr != null) {	// 즉, 좋아하는 음식을 최소 1개 이상 선택 했다면.
				pstmt.setString(4, String.join(",", foodArr ));
			}
			else {	// 사용자가 음식을 1개도 선택하지 않았을 때 (null 일때)
				pstmt.setString(4, null);	// 그때 food 에 null 을 넣어주겠다.
			}
			
			// [] 에서 문자열로 바꿔야한다.(ex, 짜장면,탕수육,팔보채 처럼)
			// food 는 선택을 하지 않아도 되므로 null 값이 들어올 수도 있다. 그러므로 null 이 아닐 겨우도 넣어줘야 한다.
			
			pstmt.setInt(5, psdto.getSeq());	//dto에서 get seq
			
			n = pstmt.executeUpdate();	// update 해라. 정상이라면 1 이 나와서 return 1을 해줄 것이다.

		} finally {
			close();
		}		
		
		return n;
	}// end of public int updatePerson(personDTO_02 psdto)-------------
	


	
}
