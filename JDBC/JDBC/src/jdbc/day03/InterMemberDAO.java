package jdbc.day03;

import java.util.Map;

public interface InterMemberDAO {

	// 회원가입(insert) 메소드 (DB 에서 insert 해줄 것이다.)
	int memberRegister(MemberDTO member);		// 파라미터 MemberDTO 라는 클래스에 담으세요. ▶ DB 에 넣는다.
	
	// 로그인처리(select) 메소드
	MemberDTO login(Map<String, String> paraMap);
	
	// 회원탈퇴(Update) 메소드
	int memberDelete(int userseq);				// 로그인은 회원가입해야만 가능한 것이고, 로그인을 해야 회원탈퇴가 가능한것이다. ▶ status 를 1에서 0으로 update 하자. 
												// id 가 아니라 회원번호인 userseq 를 파라미터로 넣는다.
}
