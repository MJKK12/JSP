package jdbc.day03.board;

import java.util.List;
import java.util.Map;

public interface InterMemberDAO {
	
	// 자원반납 메소드 
	void close();		// return 타입이 없는 close();, 자원반납

	// 회원가입시 사용가능한 아이디 인지 중복된 아이디라서 사용 불가한지 알려주는 메소드 
	boolean isUse_userid(String userid);
	
	// ① 회원가입(insert) 메소드
	int memberRegister(MemberDTO member);	// DTO 를 담아간다. return 타입은 int.
	
	// ② 로그인 처리(select) 메소드		▶ MemberDAO 에 가서 overriding 처리.
	MemberDTO login(Map<String, String> paraMap);

	// 관리자를 제외한 모든 회원들을 선택한 정렬기준으로 보여주는(select) 메소드  
	List<MemberDTO> selectAllMember(String sortChoice);


}
