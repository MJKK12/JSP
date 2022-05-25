package chap05;

import java.sql.SQLException;
import java.util.List;

public interface interPersonDAO_03 {

	// 개인성향을 입력(insert)해주는 추상메소드(미완성 메소드)
	int personRegister(personDTO_02 psdto) throws SQLException;
	// insert 이므로 return 타입은 int (1개 행)
	// 오류 발생시 메소드를 호출한 쪽에서 exception 을 처리하라는 뜻이다.

	// tbl_person_interest 테이블에 저장된 행(데이터)들을 select 해주는 추상메소드(미완성메소드)
	List<personDTO_02> selectAll() throws SQLException;

	// tbl_person_interest 테이블에 저장된 특정 1개 행만 select 해주는 추상메소드(미완성메소드)
	personDTO_02 selectOne(String seq) throws SQLException;

	// tbl_person_interest 테이블에 저장된 특정 1개 행만 delete 해주는 추상메소드(미완성메소드)
	int deletePerson(String seq) throws SQLException;

	// tbl_person_interest 테이블에 저장된 특정 1개 행만 update 해주는 추상메소드(미완성메소드)
	int updatePerson(personDTO_02 psdto) throws SQLException;
	

}
