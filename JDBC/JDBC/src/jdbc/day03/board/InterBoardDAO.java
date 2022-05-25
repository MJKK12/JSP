package jdbc.day03.board;

import java.util.*;

public interface InterBoardDAO {
	
	// 자원반납 메소드 
	void close(); // 자원반납하기.

	
	// *** 글목록보기 메소드 생성하기 *** //
	List<BoardDTO> boardList();

	// *** 글내용보기 *** //
	BoardDTO viewContents(Map<String, String> paraMap);

	// *** 글쓰기 *** //
	int write(BoardDTO board);

	// *** 댓글쓰기 *** //
	int write_comment(BoardCommentDTO comment);

	// *** 원게시글 번호에 딸린 댓글내용 보여주기 *** //
	List<BoardCommentDTO> commentList(String boardno);

	// *** 최근 1주일간 일자별 게시글 작성건수를 select 해서 나오는 결과물 *** //
	Map<String, Integer> statisticsByWeek();

	// *** 최근 2개월간 일자별 게시글 작성건수 *** // (저번달 및 이번달)
	List<Map<String, String>> statisticsByRecent_TwoMonths();

	// *** 글삭제하기 *** //
	int deletePost(Map<String, String> paraMap);

	// *** 글수정하기 *** //
	int updatePost(Map<String, String> paraMap);	

	
	
}
