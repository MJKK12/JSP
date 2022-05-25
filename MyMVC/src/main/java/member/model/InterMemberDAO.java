package member.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface InterMemberDAO {

	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true를 리턴해주고, userid 가 존재하지 않으면 false를 리턴한다)
	boolean idDuplicateCheck(String userid) throws SQLException;

	// Email 중복검사 (tbl_member 테이블에서 Email 가 존재하면 true를 리턴해주고, userid 가 존재하지 않으면 false를 리턴한다)
	boolean emailDuplicateCheck(String email) throws SQLException;
	
	// 회원가입 해주는 메소드 (tbl_member 테이블에 insert)
	int registerMember(MemberVO member) throws SQLException;

	// 입력받은 Map을 가지고 한명의 회원 정보를 return 시켜주는 메소드 (로그인처리)
	MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException;

	// 입력받은 성명, 이메일 Map 을 가지고 id 를 return 시켜주는 메소드 (아이디 찾기)
	// 아이디 찾기(성명, 이메일을 입력받아서 해당 사용자의 아이디를 알려준다.)
	String findUserid(Map<String, String> paraMap) throws SQLException;

	// 입력받은 아이디, 이메일 Map 을 가지고 비밀번호 를 return 시켜주는 메소드 (비밀번호 찾기)
	// 비밀번호 찾기(아이디, 이메일을 입력받아서 해당 사용자가 존재하는지 유무를 알려준다.)
	boolean isUserExist(Map<String, String> paraMap) throws SQLException;

	// 암호 변경하기.
	int pwdUpdate(Map<String, String> paraMap) throws SQLException;

	// 회원의 코인 및 포인트 증가하기 (DB)
	int coinUpdate(Map<String, String> paraMap) throws SQLException;

	// 회원의 개인정보 변경하기	(return 타입 int)
	int updateMember(MemberVO member) throws SQLException;
	
	// 페이징 처리가 된 모든 회원 또는 검색한 회원 목록 보여주기
	List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체 회원에 대한 총 페이지 알아오기. 
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 유저아이디 값을 입력받아서 회원 1명에 대한 상세정보를 알아오기
	MemberVO memberOneDetail(String userid) throws SQLException;
	
	
}
