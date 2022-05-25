package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class MemberDAO implements InterMemberDAO {
	
	// DB 에 가서 읽어와야 하기 때문에 Connection 을 한다. (DBCP 를 쓴다.)
	private DataSource ds;		// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private AES256 aes;
	
	// 생성자에서 코딩을 해주어야 한다.
	public MemberDAO() {
	    
		try {		
			Context initContext = new InitialContext();	// 1. 커넥션 풀에 접근하려면 JNDI 서비스를 사용
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");  
		    ds = (DataSource)envContext.lookup("jdbc/mymvc_oracle"); // 2. .lookup( )은 리소스를 찾은 후 리소스를 사용할 수 있도록 객체를 반환해주는 메소드
		    // "jdbc/myoracle" 는 web.xml 에 있는 <res-ref-name> 이다.
		    // 이는 context.xml에 있는 name에 해당한다. (오라클 DB와 연결)
		    
		    aes = new AES256(SecretMyKey.KEY);	// 객체 생성.
		    // SecretMyKey.KEY는 우리가 만든 비밀키이다.
		    
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	}
		
	// 자원 반납해주는 메소드
	private void close() {
		// 사용된 것을 닫아야(close) 한다. null이 아니라면, 자원반납
		try {
			if(rs != null) 		{ rs.close(); 		rs = null;	}
			if(pstmt != null) 	{ pstmt.close(); 	pstmt = null;	}
			if(conn != null) 	{ conn.close(); 	conn = null;	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}// end of private void close()-----------------------
	
	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true를 리턴해주고, userid 가 존재하지 않으면 false를 리턴한다)		
	@Override
	public boolean idDuplicateCheck(String userid) throws SQLException {

		boolean isExist = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select * " 
					   + " from tbl_member "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();	// select 문이므로 Query.
			isExist = rs.next();	// 행이 있으면 (중복된 userid) true	// 이미 DB에 그러한 아이디가 있는 것임.
									// 행이 없으면 (사용 가능한 userid) false
			
		} finally {
			close();
		}
		
		return isExist;
	}

	// Email 중복검사 (tbl_member 테이블에서 Email 가 존재하면 true를 리턴해주고, userid 가 존재하지 않으면 false를 리턴한다)
	@Override
	public boolean emailDuplicateCheck(String email) throws SQLException {
		boolean isExist = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select * " 
					   + " from tbl_member "
					   + " where email = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aes.encrypt(email));	// 입력한 email을 그대로 넣는 것이 아니라, 넘어온 email을 암호화 시켜서 넣어야 한다. (DB에는 암호화 된 상태로 insert 됐기 때문이다.)
			
			rs = pstmt.executeQuery();	// select 문이므로 Query.
			isExist = rs.next();	// 행이 있으면 (중복된 userid) true	// 이미 DB에 그러한 아이디가 있는 것임.
									// 행이 없으면 (사용 가능한 userid) false
			
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();				
		} finally {
			close();
		} 
		
		return isExist;
	}

	
	// 회원가입 해주는메소드 (tbl_member 테이블에 insert)
	@Override
	public int registerMember(MemberVO member) throws SQLException {

		int result = 0;
		
		try {
		
			conn = ds.getConnection();
			
			String sql = " insert into tbl_member (userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, birthday) "
					   + " values (?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getUserid());
			pstmt.setString(2, Sha256.encrypt(member.getPwd()));		// 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.
			pstmt.setString(3, member.getName());
			pstmt.setString(4, aes.encrypt(member.getEmail()));						// 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(5, aes.encrypt(member.getMobile()));						// 휴대폰번호를 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(6, member.getPostcode());
			pstmt.setString(7, member.getAddress());
			pstmt.setString(8, member.getDetailaddress());	
			pstmt.setString(9, member.getExtraaddress());
			pstmt.setString(10, member.getGender());
			pstmt.setString(11, member.getBirthday());
			
			result = pstmt.executeUpdate();
	
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();				
		} finally {
			close();
		}
		
		return result;
	}

	// 입력받은 Map을 가지고 한명의 회원 정보를 return 시켜주는 메소드 (로그인처리)
	@Override
	public MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException {

		MemberVO member = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT userid, name, email, mobile, postcode, address, detailaddress, extraaddress "+
					"     , gender, birthyyyy, birthmm, birthdd, coin, point, registerday, pwdchangegap "+
					"     , nvl(lastlogingap, trunc( months_between(sysdate, registerday) ) ) as lastlogingap "+
					" FROM "+
					" ( "+
					" select userid, name, email, mobile, postcode, address, detailaddress, extraaddress "+
					"     , gender, substr(birthday,1,4) as birthyyyy, substr(birthday,6,2) as birthmm, substr(birthday,9,2) as birthdd "+
					"     , coin, point, to_char(registerday, 'yyyy-mm-dd') as registerday "+
					"     , trunc( months_between(sysdate, lastpwdchangedate) ) as pwdchangegap "+
					" from tbl_member\n"+
					" where status = 1 and userid = ? and pwd = ? "+
					" ) M "+
					" CROSS JOIN "+
					" ( "+
					" select trunc ( months_between(sysdate, max(logindate)) ) as lastlogingap "+
					" from tbl_loginhistory "+
					" where fk_userid = ? "+
					" ) H ";			
			
			pstmt = conn.prepareStatement(sql);
			
			// LoginAction 에서 key 값을 가져오자.
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("pwd")));		// qwer1234$
			pstmt.setString(3, paraMap.get("userid"));
			
			rs = pstmt.executeQuery();	// select 이므로 결과값이 필요해서 rs를 사용한다.
			
			if(rs.next()) {	// select 된 값이 있는지?	(select 된 값이 없으면 가입x 이거나 아이디/비번을 잘못 입력한 것이다.)
				member = new MemberVO();	// select 된 것이 있을 때에만 넣어 주겠다.		
				
				member.setUserid(rs.getString(1));
				member.setName(rs.getString(2));				
				member.setEmail(aes.decrypt(rs.getString(3)));		// 이메일을 다시 복호화해준다.
				member.setMobile(aes.decrypt(rs.getString(4)));		// 연락처를 다시 복호화해준다.
				member.setPostcode(rs.getString(5));
	            member.setAddress(rs.getString(6));
	            member.setDetailaddress(rs.getString(7));
	            member.setExtraaddress(rs.getString(8));
	            member.setGender(rs.getString(9));
	            member.setBirthday(rs.getString(10) + rs.getString(11) + rs.getString(12));	// MemberVO 에서도 birthday 하나로 묶었다.
	            member.setCoin(rs.getInt(13));
	            member.setPoint(rs.getInt(14));
	            member.setRegisterday(rs.getString(15));	
	            
				if(rs.getInt(16) >= 3) {
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false
					member.setRequirePwdChange(true);	// 로그인시 암호를 변경하라는 alert 를 띄울 때 사용된다.
				}

				if(rs.getInt(17) >= 12) {
					// 마지막으로 로그인한 날짜가 현재 시간으로부터 1년이 지났으면 휴면으로 지정한다.
					// 휴면처리를 하려면 DB 에서 UPDATE 를 해줘야 한다. (활동중인 0 상태에서 휴면중인 1로 만든다.)
					member.setIdle(1);	// 0 --> 1로 변경한다.
					
					// tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기 //
					sql = " update tbl_member set idle = 1 "
						+ " where userid = ? ";

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("userid"));						
					
					pstmt.executeUpdate();			
				}
				
				// tbl_loginhistory(로그인기록) 테이블에 insert 하기 //
				if(member.getIdle() != 1) {
					// getIdle 이 1이 아니라면, 휴면처리가 아니기 떄문에 update 해줘야 한다. (즉 0이라면 활동중이기 떄문에 로그인 기록을 남긴다(update 한다.).)
					sql = " insert into tbl_loginhistory(fk_userid, clientip)"
						+ " values(?, ?) ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("userid"));						
					pstmt.setString(2, paraMap.get("clientip"));						
					
					pstmt.executeUpdate();			
					
				}
				
			}
			
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();				
		} finally {
			close();				
		}
		
		return member;
	}

	// 아이디 찾기(성명, 이메일을 입력받아서 해당 사용자의 아이디를 알려준다.)
	// 즉, 성명과 email 이 맞으면 id 값을 돌려준다. (return)
	@Override
	public String findUserid(Map<String, String> paraMap) throws SQLException {

		String userid = null;
		
		try {
			
			conn = ds.getConnection();
			
			// status 가 1이면 탈퇴한 회원이다..
			String sql = " select userid "
					   + " from tbl_member "
					   + " where status =1 and name = ? and email = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더 값 매핑
			pstmt.setString(1, paraMap.get("name"));
			pstmt.setString(2, aes.encrypt(paraMap.get("email")));	// email 은 암호화가 되어있어야 한다.
		
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userid = rs.getString(1);	// select 된 결과물(userid)의 첫번째
			}
			
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();	
		} finally {
			close();
		}
		
		return userid;	// userid 를 알려줘야 함. (return) 
	}

	// 입력받은 아이디, 이메일 Map 을 가지고 비밀번호 를 return 시켜주는 메소드 (비밀번호 찾기)
	// 비밀번호 찾기(아이디, 이메일을 입력받아서 해당 사용자가 존재하는지 유무를 알려준다.)
	@Override
	public boolean isUserExist(Map<String, String> paraMap) throws SQLException {

		boolean isUserExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			// status 가 1이면 탈퇴한 회원이다..
			String sql = " select userid "
					   + " from tbl_member "
					   + " where status = 1 and userid = ? and email = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더 값 매핑		
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, aes.encrypt(paraMap.get("email")));	// email 은 암호화가 되어있어야 한다.
		
			rs = pstmt.executeQuery();

			isUserExist = rs.next();

		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();	
		} finally {
			close();
		}
			
		return isUserExist;
	}

	// 암호 변경하기.
	@Override
	public int pwdUpdate(Map<String, String> paraMap) throws SQLException {		
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_member set pwd = ? "
					   + " 					   , lastpwdchangedate = sysdate "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, Sha256.encrypt(paraMap.get("pwd")) );		// 암호화(SHA256, 단방향 암호화)가 되어져야 한다.
			pstmt.setString(2, paraMap.get("userid") );

			result = pstmt.executeUpdate();	// 성공이라면 1 값이 나옴. (userid 는 고유하므로!!)
			
		} finally {
			close();
		}
		
		return result;	// 0 이 아니라 result 가 return 값이다.
	}
	
	
	// 회원의 코인 및 포인트 증가하기 (DB)
	@Override
	public int coinUpdate(Map<String, String> paraMap) throws SQLException {

		int result = 0;	
		
		try {
			
			conn = ds.getConnection();
			// 현재코인(coin) 에 +? 만큼 증가 / 현재 포인트(point) 에 +? 만큼 증가. 
			String sql = " update tbl_member set coin = coin + ?, point = point + ? "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			// coinUpdateLoginAction 에서 userid 와 coinmoney 를 paraMap 에 put 함.
			pstmt.setString(1, paraMap.get("coinmoney"));
			pstmt.setInt(2, (int)(Integer.parseInt(paraMap.get("coinmoney"))*0.01));	// point 는 코인머니의 1%이므로 0.01을 곱한다.
			pstmt.setString(3, paraMap.get("userid"));
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;	// result 가 넘어가도록 한다. update 가 되면 1이 return 될 것이다.
	}

	// 회원의 개인정보 변경하기
	@Override
	public int updateMember(MemberVO member) {

		int result = 0;	
		
		try {
			
			conn = ds.getConnection();
			// 새로 비밀번호를 변경했을 때 lastpwdchangedate 도 바뀌어야 한다. ( sysdate 로 설정 ) 
			String sql = " update tbl_member set name = ? "
					   + " , pwd = ?, email = ? , mobile = ? , postcode = ? , address = ? , detailaddress = ? , extraaddress = ?, lastpwdchangedate = sysdate  "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			// coinUpdateLoginAction 에서 userid 와 coinmoney 를 paraMap 에 put 함.

			pstmt.setString(1, member.getName());
			pstmt.setString(2, Sha256.encrypt(member.getPwd()));		// 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.
			pstmt.setString(3, aes.encrypt(member.getEmail()));						// 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(4, aes.encrypt(member.getMobile()));						// 휴대폰번호를 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(5, member.getPostcode());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getDetailaddress());	
			pstmt.setString(8, member.getExtraaddress());
			pstmt.setString(9, member.getUserid());
			
			result = pstmt.executeUpdate();	// 1개 행이 업데이트
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;	// result 가 넘어가도록 한다. update 가 되면 1이 return 될 것이다.
		
	}

	// 페이징 처리가 된 모든 회원 또는 검색한 회원 목록 보여주기
	// return 타입이 list 이다.
	@Override
	public List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException {
		
		List<MemberVO> memberList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			// 검색이 들어오면 다 쪼개야 한다.
			// 화면상에는 rno 가 없기 때문에 select 에서 아래 네개 컬럼만 보여준다.
			String sql = " select userid, name, email, gender "
					+ " from "
					+ " ( "
					+ "    select rownum as rno, userid, name, email, gender "
					+ "    from "
					+ "    ( "
					+ "        select userid, name, email, gender "
					+ "        from tbl_member "
					+ "        where userid != 'admin' ";


			// map 에서 꺼내오자. (put 해온 검색타입, 검색 대상들)
			String colname = paraMap.get("searchType");	
			String searchword = paraMap.get("searchword");
			
	//		System.out.println("확인용 colname : "+colname);
	//		System.out.println("확인용 searchword : "+searchword);
			
			// where 절에 검색어를 넣어준다.
			
			// 이때 colname (컬럼네임) 은 절대 위치홀더 '?'를 쓰면 안된다.
			// 위치홀더 '?' 에 들어오는 값은 데이터값만 들어올 수 있고, 
			// 위치홀더 '?' 에는 컬럼명이나 테이블명이 들어오면 오류가 발생한다. (여기서는 option 태그로 된 검색대상, 회원명, 아이디 ,이메일 을 colname 으로 해둠.)
			// 그러므로 컬럼명이나 테이블명을 변수로 사용할 때는 위치홀더 '?' 가 아닌 변수로 처리해야 한다.
			// 컬럼네일을 굳이 감출 필요가 없다.
			// 여기서 데이터값인 searchword 는 위치홀더 '?' 를 써준다.
			
			// ** 기존 sql 문에서 검색어가 있다고 하면, sql += 함으로써 추가해준다. 
			// 아래 if 절 안의 sql 문은 where 절 다음에 나와야 한다.
			if(colname != null && !"".equals(colname) && searchword != null && !"".equals(searchword)) {
				sql += " and "+colname+" like '%'|| ? ||'%' ";
			}
			
			sql += "        order by registerday desc "
				+ "    ) V "
				+ " ) T "
				+ " where rno between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);

			// 공식에 입각해서 넣어주자. (Action 에서 put 해온 key 값을 가져온다.)
			// int 로 해줬기 때문에 숫자관련 exception 처리 할것이 없다.
			
		/*
		 	>>> where rno between A and B : A ~ B 를 구하는 공식 <<<
	        
	        - currentShowPageNo : 보고자하는 페이지 번호이다. 즉, 1페이지, 2페이지, 3페이지... 를 말한다.
	        - sizePerPage : 한 페이지당 보여줄 행의 갯수를 말한다. 즉 3개, 5개, 10개를 보여줄 때의 갯수를 말한다.
	        
		    A 는 (currentShowPageNo * sizePerPage) - (sizePerPage - 1) 이고, 
		    B 는 (currentShowPageNo * sizePerPage)이다.    
		 */	
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));	// String 타입에서 int 타입으로 변경했다.
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(colname != null && !"".equals(colname) && searchword != null && !"".equals(searchword)) {
				// 검색이 있을 때

				if("email".equals(colname)) {
					// 사용자가 조회하고자 하는 컬럼네임이 email 이라면,
					// DB 에 암호화로 저장되어 있으니, 암호화 시켜주도록 한다.
					pstmt.setString(1, aes.encrypt(searchword));	// 첫번째 컬럼에는 검색어가 들어와야 한다.				
							
				}
				else {
					// 컬럼타입을 email 로 하지 않았다면! 암호화 시키지 않는다.
					pstmt.setString(1, searchword);	// 첫번째 컬럼에는 검색어가 들어와야 한다.					
				}
				
				pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1));
				pstmt.setInt(3, (currentShowPageNo * sizePerPage));				
			}
			else {
				// 검색이 없을 때
				// 위치홀더 between ~ and ~. --> 이때, 문자열 or Int 타입으로 해도 괜찮다. (setString이나 setInt 둘 다 괜찮다.)
				pstmt.setInt(1, (currentShowPageNo * sizePerPage) - (sizePerPage - 1));
				pstmt.setInt(2, (currentShowPageNo * sizePerPage));
			}
					
			rs = pstmt.executeQuery();	
			
			// ※ DB 에서 암호화된 것을, web 상에서 뿌릴 때는 복호화 해야 한다. (이메일)
			while(rs.next()) { // select 된 갯수만큼 나와야 한다. (userid, name, email, gender)
				// vo 에 넣어준다.
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(1));	// 첫번째 컬럼
				mvo.setName(rs.getString(2));	
				mvo.setEmail(aes.decrypt(rs.getString(3)));	// 복호화 (웹에서 회원정보 목록을 조회할 때, 정상적인 이메일처럼 보여야 한다.)
				mvo.setGender(rs.getString(4));
				
				memberList.add(mvo);	// 멤버리스트에 넣자.				
			}// end of while--------------------------------------------
		
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();			
		} finally {
			close();
		}
		
		return memberList;
		
	}// end of public List<MemberVO> selectPagingMember(Map<String, String> paraMap)-------------

	
	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체 회원에 대한 총 페이지 알아오기. 
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {

		int totalPage = 0;	// 결과물이 없을 땐 0으로 나올 것이다. (count = 갯수가 0.)
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select ceil( count(*)/? ) " 
					   + " from tbl_member "
					   + " where userid != 'admin' ";

			// map 에서 꺼내오자. (put 해온 검색타입, 검색 대상들)
			String colname = paraMap.get("searchType");	
			String searchword = paraMap.get("searchword");
			
	//		System.out.println("확인용 colname : "+colname);
	//		System.out.println("확인용 searchword : "+searchword);
			
			// 이때 colname (컬럼네임) 은 절대 위치홀더 '?'를 쓰면 안된다.
			// 위치홀더 '?' 에 들어오는 값은 데이터값만 들어올 수 있고, 
			// 위치홀더 '?' 에는 컬럼명이나 테이블명이 들어오면 오류가 발생한다. (여기서는 option 태그로 된 검색대상, 회원명, 아이디 ,이메일 을 colname 으로 해둠.)
			// 그러므로 컬럼명이나 테이블명을 변수로 사용할 때는 위치홀더 '?' 가 아닌 변수로 처리해야 한다.
			// 컬럼네일을 굳이 감출 필요가 없다.
			// 여기서 데이터값인 searchword 는 위치홀더 '?' 를 써준다.
			
			// colname, searchword 은 null 도 아니면서, "" 도 아니다. ("" 는 검색이 없는 것이다.)
			if(colname != null && !"".equals(colname) && searchword != null && !"".equals(searchword)) {	// Action 단에서 searchword 가 null 일떄 "" 가 되도록 설정함.
				sql += " and "+colname+" like '%'|| ? ||'%' ";
			}
			
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, paraMap.get("sizePerPage"));	// Map 에 담겨져 있는 것 ( 현재 paraMap 속에 currentShowPageNo, sizePerPage(3,5,10page) 가 있다. )
			
			if(colname != null && !"".equals(colname) && searchword != null && !"".equals(searchword)) {
				
				if("email".equals(colname)) {
					// 사용자가 조회하고자 하는 컬럼네임이 email 이라면,
					// DB 에 암호화로 저장되어 있으니, 암호화 시켜주도록 한다.
					pstmt.setString(2, aes.encrypt(paraMap.get("searchword")));					
							
				}
				else {
					// 컬럼타입을 email 로 하지 않았다면! 암호화 시키지 않는다.
					pstmt.setString(2, paraMap.get("searchword"));					
				}
			
				
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();	// 없더라도 0은 꼭 나오게 되어있다.
			
			// select 된 결과물 출력
			totalPage = rs.getInt(1);
			
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();				
		} finally {
			close();
		}
		
		return totalPage;	// totalPage 를 넘겨준다.
	}
	

	// 유저아이디 값을 입력받아서 회원 1명에 대한 상세정보를 알아오기	
	@Override
	public MemberVO memberOneDetail(String userid) throws SQLException {

		MemberVO mvo = null;	// select 는 여러명을 넘겨주는 것과 비슷하다.
		
		try {
			
			conn = ds.getConnection();
			
			// status 가 1이면 탈퇴한 회원이다..
			String sql = " select userid, name, email, mobile, postcode, address, detailaddress, extraaddress " +
					 	 "	 , gender, substr(birthday,1,4) as birthyyyy, substr(birthday,6,2) as birthmm, substr(birthday,9,2) as birthdd " +
						 "	 , coin, point, to_char(registerday, 'yyyy-mm-dd') as registerday " +
						 " from tbl_member " +
						 " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);			
			// 위치홀더 값 매핑		
			pstmt.setString(1, userid);
		
			rs = pstmt.executeQuery();

			if(rs.next()) {	// 유저아이디가 존재하는지 확인해야 한다. 1개만 나오기 때문에 while 문을 쓰지 않고 if를 쓴다. (껼과물이 있는지)
					mvo = new MemberVO();	// select 된 것이 있을 때에만 넣어 주겠다.		
					
					mvo.setUserid(rs.getString(1));
					mvo.setName(rs.getString(2));				
					mvo.setEmail(aes.decrypt(rs.getString(3)));		// 이메일을 다시 복호화해준다.
					mvo.setMobile(aes.decrypt(rs.getString(4)));		// 연락처를 다시 복호화해준다.
					mvo.setPostcode(rs.getString(5));
					mvo.setAddress(rs.getString(6));
					mvo.setDetailaddress(rs.getString(7));
					mvo.setExtraaddress(rs.getString(8));
					mvo.setGender(rs.getString(9));
		            
					mvo.setBirthday(rs.getString(10) + rs.getString(11) + rs.getString(12));	// MemberVO 에서도 birthday 하나로 묶었다.
					mvo.setCoin(rs.getInt(13));
					mvo.setPoint(rs.getInt(14));
					mvo.setRegisterday(rs.getString(15));				
			}

		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();	
		} finally {
			close();
		}
		
		return mvo;
	}
	
}	

