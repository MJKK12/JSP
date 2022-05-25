package jdbc.day03.board;
// 게시글을 관리하는 DAO

import java.sql.*;
import java.util.*;

public class BoardDAO implements InterBoardDAO {

	// attribute, field, property, 속성
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	
	// operation, method, 기능
	
	// *** 자원반납 메소드 구현하기 *** //
	@Override
	public void close() {
		
		try {
			if(rs != null) 		rs.close();
			if(pstmt != null) 	pstmt.close();
			if(conn != null) 	conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}// end of public void close()----------------------
		
	
	// *** 글목록보기 메소드 구현하기 *** //
	@Override
	public List<BoardDTO> boardList() {

		List<BoardDTO> boardList = new ArrayList<>();	// 게시글 목록
		// return 에는 자식테이블 & 부모테이블 까지 넘겨주어야 한다는 뜻이다. (그러므로 boardDTO 속에는 memberDTO가 들어와야 한다는 뜻이다.)
		// BoardDTO 속에 JOIN jdbc_member M 된 것도 들어와야 한다. 
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			/*
			String sql = " select B.boardno, B.subject, M.name "
					   + " 		, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss'), B.viewcount "
					   + " from jdbc_board B JOIN jdbc_member M "
					   + " ON B.fk_userid = M.userid "
					   + " order by boardno ";	
			*/
			
			  String sql =  " select B.boardno, B.subject, M.name "+
					  		"        , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss'), B.viewcount "+
					  		"        , nvl(C.COMMENTCNT, 0) "+
					  		"    from jdbc_board B JOIN jdbc_member M "+
					  		"    ON B.fk_userid = M.userid "+
					  		"    LEFT JOIN ( select fk_boardno, count(*) AS COMMENTCNT "+
					  		"                from jdbc_comment "+
					  		"                group by fk_boardno) C "+
					  		"    ON B.boardno = C.fk_boardno "+
					  		"    order by 1 desc";

			pstmt = conn.prepareStatement(sql);

			
			rs = pstmt.executeQuery();		// sql 문이 select 문이므로 executeQuery

			while(rs.next()) {				// select 된 모든 회원들을 조회해야 하므로 복수개행 ▶ if 에서 while 문으로 변경
											// 만약 admin 빼고 가입된 사람이 없으면 rs.next() 는 false 임.
				BoardDTO board = new BoardDTO();	// 매번 새로운 BoardDTO 
				
				board.setBoardno(rs.getInt(1));
				board.setSubject(rs.getString(2));			// 앞의 totalController 의 관리자 부분 모든회원정보보기 추가를 위해 getUserid를 추가함으로써 setUserid가 필요해서 추가함.	
				
				//★★★★★테이블 JOIN 시 추가★★★★★//
				MemberDTO member = new MemberDTO();			// member 테이블에 있는 name 을 가져오기 위함 (JOIN 을 했기 때문)			
				member.setName(rs.getString(3));			// ★★★★ JOIN 을 해옴으로써 BoardDTO 에 없던 부모테이블의 name 컬럼을 가져옴. (엄정화(name) 은 게시판 테이블에 글 여러번 쓰기 가능.)
				board.setMember(member);					// name 은 board 테이블이 아니라 member 테이블에 있다. ▶ BoardDTO 에 할 수 없다.
				//member.setMobile(rs.getString(4));
				//▲JOIN 시★★★★★// 							// member 테이블의 mobile 도 넣는다고 한다면, member.setMobile(rs.getString(4)); 를 넣을 수도 있다.(JOIN 한것은 저런식으로 넣는다.)
				
				board.setWriteday(rs.getString(4));			
				board.setViewcount(rs.getInt(5));
				board.setCommentcnt(rs.getInt(6));			// 댓글의 개수
				
				boardList.add(board);			// boardList 에 담아라.
			}// end of while(rs.next()) --------------------------------
			
		
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return boardList;		// return 타입 중요(nullPointerException)
	}// end of public List<BoardDTO> boardList()--------------------------------

	
	
	// *** 글내용보기 메소드를 구현하기*** //
	@Override
	public BoardDTO viewContents(Map<String, String> paraMap) {

		BoardDTO board = null;

		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select * "
					   + " from jdbc_board "
					   + " where boardno = ? ";	
				         
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));		// 유저가 입력한 boardno
			
			rs = pstmt.executeQuery();		// sql 문이 select 문이므로 executeQuery
			
			if(rs.next()) {
				// 입력한 글번호에 해당하는 글이 존재하는 경우
			
				// 로그인한 사용자가 쓴 글인지 (즉, 자신이 쓴 글을 자신이 보고자 하는 경우)
				// 로그인한 사용자가 쓴 글이 아닌 다른 사용자가 쓴 글인지 구분한다.			
				sql = " select * "
					+ " from jdbc_board "
					+ " where boardno = ? and fk_userid = ? ";	// 내가쓴 글인지 아닌지를 구분하는 것. (fk_userid 는 로그인된 사람 id)
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("boardno"));			// 유저가 입력한 boardno
				pstmt.setString(2, paraMap.get("fk_userid"));		// 유저가 입력한 boardno
									// ▶ 컨트롤러에서 put 한 것.	
				rs = pstmt.executeQuery();
				
				if(!rs.next()) {
					// 로그인한 사용자가 쓴 글이 아닌 다른 사용자가 쓴 글 이라면 (내가 쓴 글 XXX)
					// 조회수를 올려주자! (내가쓴 글을 내가 조회수를 올릴 수 없으니, 다른사람 글을 읽었을째 조회수 상승)
					sql = " update jdbc_board set viewcount = viewcount + 1 "
					    + " where boardno = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("boardno"));
					
					pstmt.executeUpdate();						
				}
				
				sql = " select boardno, subject , contents, to_char(writeday, 'yyyy-hh-dd hh24:mi:ss'), viewcount, M.name "
						+ " from jdbc_board B JOIN jdbc_member M "
						+ " ON B.fk_userid = M.userid "
						+ " where boardno = ? ";		// 내가보고싶은 글번호 	
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("boardno"));
			
				rs = pstmt.executeQuery();
			
				rs.next();		// 위에서 돌린 결과물을 가져오자.
				
				board = new BoardDTO();
				board.setBoardno(rs.getInt(1));			// 1,2,3,4 : 셀렉트 된 결과물들				
				board.setSubject(rs.getString(2));
				board.setContents(rs.getString(3));
				board.setWriteday(rs.getString(4));
				board.setViewcount(rs.getInt(5));

				//★★★★★테이블 JOIN 시 추가★★★★★//
				MemberDTO member = new MemberDTO();			// member 테이블에 있는 name 을 가져오기 위함 (JOIN 을 했기 때문)			
				member.setName(rs.getString(6));			// ★★★★ JOIN 을 해옴으로써 BoardDTO 에 없던 부모테이블의 name 컬럼을 가져옴. (엄정화(name) 은 게시판 테이블에 글 여러번 쓰기 가능.)
				board.setMember(member);					// name 은 board 테이블이 아니라 member 테이블에 있다. ▶ BoardDTO 에 할 수 없다.
				//member.setMobile(rs.getString(7));
				//▲JOIN 시★★★★★// 							// member 테이블의 mobile 도 넣는다고 한다면, member.setMobile(rs.getString(4)); 를 넣을 수도 있다.(JOIN 한것은 저런식으로 넣는다.)				
			}
			
			else {
				// 입력한 글번호에 해당하는 글이 존재하지 않는 경우
				// ex) 2334564 번에 해당하는 글은 없음. ▶ null 을 넘긴다. (그러나 어차피 초기치가 null; 이기 때문에 굳이 BoardDTO board = null; 를 안해도 되고 메세지만 띄워도 된다.)
				// ② 존재하지 않는 글번호를 입력했을 때(그에 해당하는 rs.next()에 해당하는 행이 없음..)
				System.out.println(">> 조회하고자 하는 글번호 "+paraMap.get("boardno")+"에 해당하는 글은 없습니다. <<\n");
			}
		
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {		// 유저가 숫자외의 것을 boardno 에 넣는다면 오류.. (① 숫자외의 문자를 넣었을 때 , ② 존재하지 않는 글번호를 입력했을 때(그에 해당하는 rs.next()에 해당하는 행이 없음..)
			if( e.getErrorCode() == 1722 ) {
				System.out.println(">> 조회하고자 하는 글번호는 정수만 입력하세요. <<\n"); // ▶ ① 숫자외의 문자를 넣었을 때
			}
			else {
				e.printStackTrace();				
			}
		} finally {
			close();
		}
		
		
		return board;
	}// end of public BoardDTO viewContents(Map<String, String> paraMap)-----------


	// *** 글쓰기 메소드를 구현하기 *** // 트랜잭션 처리를 해야한다. (두 테이블에서 완성한 후 commit!)
	/*
	 	=== Transaction 처리를 해야하는 경우이다. === [DML 1 set]
	 	① jdbc_board(게시판) 테이블에 insert 를 성공하면
	 	② jdbc_member(회원) 테이블에 있는 point 컬럼의 값을 10 증가로 update 해야 한다.
	 	즉, jdbc_board(게시판) 테이블에 insert 와 jdbc_member(회원) 테이블에 update 가 
	 		둘 모두 성공해야만 commit 을 해주고, 만약에 1개라도 실패하면 모두 rollback 해야 한다.
	 */
	@Override
	public int write(BoardDTO board) {

		int result = 0;

		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			conn.setAutoCommit(false); 	// 트랜잭션 처리를 위해서 수동커밋으로 전환한다. (글쓰기→포인트 추가 한 세트, 테이블 서로 다름)
			// 글쓰기 insert sql 문
			String sql = " insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd)" 
					   + " values (board_seq.nextval, ?, ?, ?, ?) ";				
						// writeday 와 viewcount 는 default 이므로 insert() 안에 쓰지 않아도 된다.
			
			
			pstmt = conn.prepareStatement(sql);				
			pstmt.setString(1, board.getFk_userid());		// 위의 sql 문과 mapping 한다.
			pstmt.setString(2, board.getSubject());			// 앞의 컨트롤러에서 board.set~ 으로 넣어줌.
			pstmt.setString(3, board.getContents());
			pstmt.setString(4, board.getBoardpasswd());
			
			
			int n = pstmt.executeUpdate();		// sql 문이 DML 문이므로 executeUpdate (정상이라면 n 이 1로 나와야 함.)
			// 포인트 주기 update sql 문
			
			int m = 0;
			
			if(n==1) {	// n==1 : insert 가 성공했는가? (1이면 정상적으로 insert 완료.) ▶ 그 이후에 회원 테이블의 포인트 update 하자.
				sql = " update jdbc_member set point = point + 10 " 
					+ " where userid = ? ";
			
				pstmt = conn.prepareStatement(sql);				// SQL 문을 실행.
				pstmt.setString(1, board.getFk_userid());
				
				m = pstmt.executeUpdate();					// DML 문의 m! SQL 문을 실행.
				
			}

			if(m == 1) {
				// n==1 이 되어야 비로소 m==1 이 된다. (글쓰기를 해야 → point 가 쌓인다.)
				conn.commit();	// 둘다 성공했을 때 (m과 n 모두 1 일때) commit (Insert & Update)
				result = m;
			}
			
			// ★ insert 에서 성공 --> n==1, update 를 실패 시 ▶ catch 문에 가서 rollback 해줘야 한다.


		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {		// 유저가 숫자외의 것을 boardno 에 넣는다면 오류.. (① 숫자외의 문자를 넣었을 때 , ② 존재하지 않는 글번호를 입력했을 때(그에 해당하는 rs.next()에 해당하는 행이 없음..)
			try {
				conn.rollback();	// insert 는 됐으나 update 안됐으면 rollback (트랜잭션 처리)
			} catch (SQLException e1) {	} 
		} finally {
			close();
		}		
		
		return result;
	}// end of public int write(BoardDTO board)------------------------

	
	
	// *** 댓글쓰기 메소드를 구현하기 *** // 
	/*
	 	=== Transaction 처리를 해야하는 경우이다. === [DML 1 set]
	 	① jdbc_comment(댓글) 테이블에 insert 를 성공하면
	 	② jdbc_member(회원) 테이블에 있는 point 컬럼의 값을 5(점) 증가로 update 해야 한다.
	 	즉, jdbc_comment(댓글) 테이블에 insert 와 jdbc_member(회원) 테이블에 update 가 
	 		둘 모두 성공해야만 commit 을 해주고, 만약에 1개라도 실패하면 모두 rollback 해야 한다.
	*/
	@Override
	public int write_comment(BoardCommentDTO comment) {

		int result = 0;

		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			conn.setAutoCommit(false); 	// 트랜잭션 처리를 위해서 수동커밋으로 전환한다. (글쓰기→포인트 추가 한 세트, 테이블 서로 다름)
			// 글쓰기 insert sql 문
			String sql = " insert into jdbc_comment(commentno, fk_boardno, fk_userid, contents) " 
					   + " values (seq_comment.nextval, to_number(?), ?, ?) ";				// String 타입인 숫자형태. (오류 대비..to_number 를 써준다.)
						// writeday 와 viewcount 는 default 이므로 insert() 안에 쓰지 않아도 된다.
						// fk_boardno 는 
			
			pstmt = conn.prepareStatement(sql);				
			pstmt.setString(1, comment.getFk_boardno());		// 위의 sql 문과 mapping 한다.
			pstmt.setString(2, comment.getFk_userid());			// 앞의 컨트롤러에서 board.set~ 으로 넣어줌.
			pstmt.setString(3, comment.getContents());
			
			
			int n = pstmt.executeUpdate();		// sql 문이 DML 문이므로 executeUpdate (정상이라면 n 이 1로 나와야 함.)
			// 포인트 주기 update sql 문
			
			int m = 0;
			
			if(n==1) {	// n==1 : insert 가 성공했는가? (1이면 정상적으로 insert 완료.) ▶ 그 이후에 회원 테이블의 포인트 update 하자.
				sql = " update jdbc_member set point = point + 5 " 
					+ " where userid = ? ";
			
				pstmt = conn.prepareStatement(sql);				// SQL 문을 실행.
				pstmt.setString(1, comment.getFk_userid());		// 누구인지?
				
				m = pstmt.executeUpdate();					// DML 문의 m! SQL 문을 실행.
				
			}

			if(m == 1) {
				// n==1 이 되어야 비로소 m==1 이 된다. (글쓰기를 해야 → point 가 쌓인다.)
				conn.commit();	// 둘다 성공했을 때 (m과 n 모두 1 일때) commit (Insert & Update)
				result = m;
			}
			
			// ★ insert 에서 성공 --> n==1, update 를 실패 시 ▶ catch 문에 가서 rollback 해줘야 한다.


		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {		// 유저가 숫자외의 것을 boardno 에 넣는다면 오류.. (① 숫자외의 문자를 넣었을 때 , ② 존재하지 않는 글번호를 입력했을 때(그에 해당하는 rs.next()에 해당하는 행이 없음..)
			try {
				if(e.getErrorCode() == 2291) {
				    /* SQL
					    오류 보고 -
				    	ORA-02291: integrity constraint (HR.FK_JDBC_COMMENT_FK_BOARDNO) violated - parent key not found
				    */
					System.out.println(">> [오류발생] 원글 번호 "+comment.getFk_boardno()+"는 존재하지 않습니다. <<");
					result = -1;		// 유저 잘못일 땐 -1을 준다. (0 을 주면 안됨. 회사가 아니라 사용자가 잘못 넣음)	
				}
				
				conn.rollback();	// 댓글이 insert 는 됐으나 포인트가 update 안됐으면 rollback (트랜잭션 처리)
			} catch (SQLException e1) {	} 
		} finally {
			close();
		}		
		
		return result;		// (유저잘못)오류시 : 존재하지 않는 ~~~ 메세지(원글번호 존재X면) → 롤백처리 → result 값 return. (-1 리턴.) "유저잘못 관련~~~ 메세지"
							// (개발잘못)포인트에서 에러가 발생했을 때 → 롤백처리 → result 값 return. (0 리턴) : "장애발생~~~ 메세지"
	}// end of public int write_comment(BoardCommentDTO comment)---------------------------

	
	
	// *** 원게시글 번호에 딸린 댓글 보여주기 메소드 생성하기*** //
	@Override
	public List<BoardCommentDTO> commentList(String boardno) {
		
		List<BoardCommentDTO> commentList = new ArrayList<>();
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select contents, M.name, C.writeday "
					   + " from "
					   + " (select contents, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday, fk_userid "
					   + " from jdbc_comment "
				       + " where fk_boardno = ? ) C JOIN jdbc_member M "
					   + " ON C.fk_userid = M.userid" ;	
				         

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardno);
			
			rs = pstmt.executeQuery();		// select 된 결과물 sql문 실행 // sql 문이 select 문이므로 executeQuery

			while(rs.next()) {				// select 된 모든 회원들을 조회해야 하므로 복수개행 ▶ if 에서 while 문으로 변경
				
				BoardCommentDTO comment = new BoardCommentDTO();
				comment.setContents(rs.getString(1));
				// ▼ JOIN 된 것 가져오자.
				MemberDTO member = new MemberDTO();
				member.setName(rs.getString(2));
				comment.setMember(member);				
				// ▲ 
				comment.setWriteday(rs.getString(3));
				
				commentList.add(comment);
			}// end of while(rs.next()) --------------------------------
			
		
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		

		return commentList;
	}// end of public List<BoardCommentDTO> commentList(String boardno)---------------------------


	// *** 최근 1주일간 일자별 게시글 작성건수를 select 해서 나오는 메소드 생성하기*** //	
	@Override
	public Map<String, Integer> statisticsByWeek() {

		Map<String, Integer> resultMap = new HashMap<>();	// 오른쪽<> 안에는 생략가능.
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = "select count(*) AS TOTAL\n"+
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 6, 1, 0 )) AS PREVIOUS6 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 5, 1, 0 )) AS PREVIOUS5 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 4, 1, 0 )) AS PREVIOUS4 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 3, 1, 0 )) AS PREVIOUS3 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 2, 1, 0 )) AS PREVIOUS2 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 1, 1, 0 )) AS PREVIOUS1 \n" +
					 "  , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 0, 1, 0 )) AS TODAY \n" +
					 "from jdbc_board\n"+
					 "where func_midnight(sysdate) - func_midnight(writeday) < 7 ";

			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();		// select 된 결과물 sql문 실행 // sql 문이 select 문이므로 executeQuery

			rs.next();	// 한 행 밖에 출력이 안되므로 반복이 아니기때문에 if 나 while 을 쓰지 않는다.

			resultMap.put("TOTAL", rs.getInt(1));		// (Key,Value 값)
			resultMap.put("PREVIOUS6", rs.getInt(2));
			resultMap.put("PREVIOUS5", rs.getInt(3));
			resultMap.put("PREVIOUS4", rs.getInt(4));
			resultMap.put("PREVIOUS3", rs.getInt(5));
			resultMap.put("PREVIOUS2", rs.getInt(6));
			resultMap.put("PREVIOUS1", rs.getInt(7));
			resultMap.put("TODAY", rs.getInt(8));

		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
				
		return resultMap;
	}// end of public Map<String, Integer> statisticsByWeek()---------------------------

	
	
	// *** 최근 2개월간 일자별 게시글 작성건수 메소드 생성하기*** // (저번달 및 이번달)
	@Override
	public List<Map<String, String>> statisticsByRecent_TwoMonths() {

		List<Map<String, String>> mapList = new ArrayList<>();
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select decode( Grouping( to_char(writeday, 'yyyy-mm-dd') ), 0, to_char(writeday, 'yyyy-mm-dd'), '전체') AS WRITEDAY  \n" +
						 " , count(*) AS CNT\n" +
						 " from jdbc_board\n" +
						 " where to_char(writeday, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm') OR \n" +
						 " to_char(writeday, 'yyyy-mm') = to_char( add_months(sysdate, -1), 'yyyy-mm' )\n" +
						 " group by ROLLUP (to_char(writeday, 'yyyy-mm-dd')) ";

			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();		// select 된 결과물 sql문 실행 // sql 문이 select 문이므로 executeQuery

			while(rs.next()) {		// 결과물이 복수개이므로 반복이기 때문에 if 나 while 을 쓴다.
			
				Map<String, String> map = new HashMap<>();//mapList 에 담는다.
				
				map.put("WRITEDAY", rs.getString(1));			// map 에 넣자(put)!
				map.put("CNT", String.valueOf(rs.getInt(2)));	// map 에 넣자(put)!, 2번째 컬럼값. (카운트는 정수이므로 바꿔준 것)
				
				mapList.add(map);
			}// end of while(rs.next()) ------------------------------------


		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}	
		
		return mapList;
	}// end of public List<Map<String, String>> statisticsByRecent_TwoMonths()---------


	// *** 글삭제하기 메소드 생성하기*** // 
	@Override
	public int deletePost(Map<String, String> paraMap) {

		int n = 0;	
		// 해당글이 존재하지 않으면 삭제할 수가 X (우선 존재하는지 확인해봐야한다.)
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select * " 
					   + " from jdbc_board " 
					   + " where boardno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));
			
			rs = pstmt.executeQuery();		// select 된 결과물 sql문 실행 // sql 문이 select 문이므로 executeQuery

			if(!rs.next()) {	// 글번호가 잘못되었을 때
				n = 1;			// 존재하지 않는 글번호(boardno)를 가지고 글을 삭제하려는 경우
			}
			else {
				// 존재하는 글번호(boardno)를 가지고 글을 삭제하려는 경우
				sql += " and fk_userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("boardno"));	// paraMap.get("userid") 이 로그인된 사용자의 아이디 값이다.
				pstmt.setString(2, paraMap.get("userid"));	// paraMap.get("userid") 이 로그인된 사용자의 아이디 값이다.
				// 위의 String sql 문과 맞아야함.(? 갯수에 맞춰주자..)(인덱스에서 누락된 IN 또는 OUT 매개변수:: 2)
				rs = pstmt.executeQuery();
				
				if(!rs.next()) {
					// 다른 사용자의 글을 삭제하려고 한 경우. (내 게시물이 XX)
					n = 2;	// 2 를 주고 return 시켜버린다.
				}
				else {
					// 로그인한 사용자가 자신이 쓴 글을 삭제하려고 하는 경우
					sql += " and boardpasswd = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("boardno"));
					pstmt.setString(2, paraMap.get("userid"));
					pstmt.setString(3, paraMap.get("boardpasswd"));
					
					rs = pstmt.executeQuery();

					if(!rs.next()) {
						// 삭제하려는 글암호가 글삭제시 입력받은 글암호와 일치하지 않는 경우
						n = 3;
					}
					else {
						// 삭제하려는 글암호가 글삭제시 입력받은 글암호와 일치하는 경우
						// 모두 다 맞는 경우. (삭제 가능.!!)
						
						sql = " delete from jdbc_board "
							+ " where boardno = ? ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, paraMap.get("boardno"));
						
						int m = pstmt.executeUpdate();	//DML 이므로 Update.
						if(m==1) {
							n = 4;	// 모두 다 맞는 경우 4를 넣어 준다. // m이 1 이라면 return 값 4를 주겠다.
						}
							
					}
					
				}
				
			}
			
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
			n = 5;
		} finally {
			close();
		}	
		
		return n;
	}// end of public int deletePost(Map<String, String> paraMap)--------------------

	
	// *** 글수정하기 메소드 구현하기 *** //
	@Override
	public int updatePost(Map<String, String> paraMap) {
		
		int n = 0;	
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "cclass");
			
			String sql = " select * " 
					   + " from jdbc_board " 
					   + " where boardno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));
			
			rs = pstmt.executeQuery();		// select 된 결과물 sql문 실행 // sql 문이 select 문이므로 executeQuery

			if(!rs.next()) {	// 글번호가 잘못되었을 때
				n = 1;			// 존재하지 않는 글번호(boardno)를 가지고 글을 수정하려는 경우
			}
			else {
				// 존재하는 글번호(boardno)를 가지고 글을 수정하려는 경우
				sql += " and fk_userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("boardno"));	// paraMap.get("userid") 이 로그인된 사용자의 아이디 값이다.
				pstmt.setString(2, paraMap.get("userid"));	// paraMap.get("userid") 이 로그인된 사용자의 아이디 값이다.
				// 위의 String sql 문과 맞아야함.(? 갯수에 맞춰주자..)(인덱스에서 누락된 IN 또는 OUT 매개변수:: 2)
				rs = pstmt.executeQuery();
				
				if(!rs.next()) {
					// 다른 사용자의 글을 수정하려고 한 경우. (내 게시물이 XX)
					n = 2;	// 2 를 주고 return 시켜버린다.
				}
				else {
					// 로그인한 사용자가 자신이 쓴 글을 수정하려고 하는 경우
					sql += " and boardpasswd = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("boardno"));
					pstmt.setString(2, paraMap.get("userid"));
					pstmt.setString(3, paraMap.get("boardpasswd"));
					
					rs = pstmt.executeQuery();

					if(!rs.next()) {
						// 수정하려는 글암호가 글수정시 입력받은 글암호와 일치하지 않는 경우
						n = 3;
					}
					else {
						// 수정하려는 글암호가 글수정시 입력받은 글암호와 일치하는 경우
						// 모두 다 맞는 경우. (수정 가능.!!)
						
						sql = " update jdbc_board set subject =? , contents = ? "
							+ " where boardno = ? ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, paraMap.get("subject"));	//key 값 subject get해옴.
						pstmt.setString(2, paraMap.get("contents"));
						pstmt.setString(3, paraMap.get("boardno"));
						
						int m = pstmt.executeUpdate();	//DML 이므로 Update.
						if(m==1) {
							n = 4;	// 모두 다 맞는 경우 4를 넣어 준다. // m이 1 이라면 return 값 4를 주겠다.
						}
							
					}
					
				}
				
			}
			
		} catch(ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없습니다. <<");
		} catch(SQLException e) {
			e.printStackTrace();
			n = 5;
		} finally {
			close();
		}	
		
		return n;
	}// end of public int updatePost(Map<String, String> paraMap)---------------

	
	
}
