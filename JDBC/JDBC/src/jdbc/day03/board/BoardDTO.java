package jdbc.day03.board;
// 게시글 테이블
public class BoardDTO {		// BoardDTO 는 오라클의 jdbc_board(자식테이블) 테이블의 한 행을 의미한다.

//  ---------------------------------------------	
//  오라클의 jdbc_board 테이블에 insert 및 select 하는 용도
	
	private int boardno;		// 글번호
	private String fk_userid;	// 작성자아이디
	private String subject;		// 글제목
	private String contents;	// 글내용
	private String writeday;	// 작성일자
	private int viewcount;		// 조회수
	private String boardpasswd;	// 글암호 (게시물에 비밀번호 설정)
//  ---------------------------------------------

	private MemberDTO member; 	// 자식테이블의 부모
	// 오라클의 jdbc_board 테이블과 jdbc_member 테이블을 JOIN 하여 select 하는 용도 
	// ( ①여기서는 게시판목록에서 name 을 나타내기 위해 JOIN 을 사용 )
	// default 값 null 대신에 member 하나 만들어서 M.name 테이블에 넣어준다.
	
	private int commentcnt;		// select 용도
	// 오라클의 jdbc_board 테이블과 jdbc_comment 테이블을 JOIN 하여 select 하는 용도 
	
	
	// ▼ 캡슐화

	public int getBoardno() {
		return boardno;
	}

	public void setBoardno(int boardno) {
		this.boardno = boardno;
	}
	
	public String getFk_userid() {
		return fk_userid;
	}
	
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getWriteday() {
		return writeday;
	}
	
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	
	public int getViewcount() {
		return viewcount;
	}
	
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	
	public String getBoardpasswd() {
		return boardpasswd;
	}
	
	public void setBoardpasswd(String boardpasswd) {
		this.boardpasswd = boardpasswd;
	}
	
	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public int getCommentcnt() {
		return commentcnt;
	}

	public void setCommentcnt(int commentcnt) {
		this.commentcnt = commentcnt;
	}

	
	
}
