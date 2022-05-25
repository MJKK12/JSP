package jdbc.day03.board;
// 회원 테이블
public class MemberDTO extends Object  { // MemberDTO 가 오라클의 jdbc_member(부모테이블) 테이블의 한 행을 의미하는 것이다.
		
	// attribute, field, property, 속성
	private int userseq;			// 회원번호 (기본값 0)
	private String userid;			// 회원아이디(String 타입은 기본값 null)
	private String passwd;			// 회원암호
	private String name;			// 회원명(이름은 null 이 아님, 이름은 넣어주었을테니까)
	private String mobile;			// 연락처
	private int point;				// 포인트
	private String registerday;		// 가입일자
	private int status;				// status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴 
	
	// 캡슐화 ▼
	public int getUserseq() {
		return userseq;
	}
	
	public void setUserseq(int userseq) {
		this.userseq = userseq;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public int getPoint() {
		return point;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	
	public String getRegisterday() {
		return registerday;
	}
	
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
	
		return "◇ 성명 : "+name+" \n"
			 + "◇ 연락처 : "+mobile+" \n"
			 + "◇ 포인트 : "+point+" \n"
			 + "◇ 가입일자 : "+registerday+" \n";
	}
		
		
}
