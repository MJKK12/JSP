package jdbc.day03.board;

import java.text.SimpleDateFormat;
import java.util.*;

import jdbc.util.MyUtil;

public class TotalController {

	// field, attribute, property, 속성
	InterMemberDAO mdao = new MemberDAO();		// 로그인 처리 해주는 메소드가 있는 곳(InterMemberDAO)
	InterBoardDAO bdao = new BoardDAO();
	
	// operation, method, 기능
	
	// ** 시작메뉴 메소드 생성하기 ** //
	public void menu_Start(Scanner sc) {		// main 에서 만들어온 menu_Start를 멤버ctrl 클래스에서 가져온다.
	
		MemberDTO member = null;
		String s_Choice = "";
		
		
		do {
			String loginName = "";			// 로그인이 안되어있으면 loginName 이 "" 로 돌아감. (로그아웃 때문에 do~while 안쪽에 넣어야함.)
			String login_logout = "로그인";	// 기본값을 로그인으로 주고, 로그인이 되면 "로그아웃"으로 바껴야 한다.
			
			if(member != null) {
				loginName = "["+member.getName()+"님(point "+(member.getPoint())+") 로그인중..]";
				login_logout = "로그아웃";
			}
		
			System.out.println("\n >>> ------- 시작메뉴 "+ loginName +"------- <<< \n"
					 + "1. 회원가입   2."+login_logout+"   3.프로그램종료 \n"			// 2. 뒤에 로그인 or 로그아웃 (그때마다 바뀌기 때문에 변수처리를 해준다.)
					 + "--------------------------------\n");
			
			System.out.print("▷메뉴번호선택 : ");
			s_Choice = sc.nextLine();
			
			switch (s_Choice) {
				case "1":	// 회원가입
					member = memberRegister(sc);		// memberRegister 을 호출했음.
					break;								// 메소드의 return 타입을 null 로 받아야

				case "2":	
					if("로그인".equals(login_logout)) {
						// 로그인 처리하기
						member = login(sc);	// login 메소드를 호출하자. 
						
						if(member != null) {			// (로그인이 성공한 경우) member 가 null 이 아닐때만 게시판 메뉴로 가자.
							menu_Board(member, sc);		// 게시판 메뉴에 들어간다. (member 와 sc 를 받아온다.)
						}
						
					}
					
					else {
						// 로그아웃
						member = null;
						System.out.println(">> 로그아웃 되었습니다. << \n");

					}

					break;
					
				case "3":	
						// 프로그램 종료
					
					break;
					
				default:
					System.out.println(">>> 메뉴에 없는 번호입니다. 다시 선택하세요.!! <<<");
					break;
			}// end of switch (s_Choice)--------------------------------------------
			
		} while(!("3".equals(s_Choice)));



	}// end of public void menu_Start(Scanner sc)------------------------------------

	
	// case ① 회원가입을 처리해주는 메소드 생성하기
	
	private MemberDTO memberRegister(Scanner sc) {
		
		System.out.println("\n >>> --- 회원가입 --- <<< ");
		String userid = "";
		do {
			System.out.print("1. 아이디 : ");
			userid = sc.nextLine();
			
			boolean isUse = mdao.isUse_userid(userid);	// DB 에서 쓸 수 없는지 묻기
			
			if( !isUse ) {		// 입력받은 id 를 넣어준다. → 이 자체가 true / false (true 면 이미 중복이기 때문에 쓸 수 없다.) // !false == true
				System.out.println("\n >>> "+userid+"가 이미 사용중이므로 다른 아이디 값을 입력하세요. <<< \n");			
			}
			else {
				System.out.println("\n >>> "+userid+" 아이디로 사용가능 합니다.!! <<< \n");			
				break;
			}
			
		} while(true);
		
		System.out.print("2. 비밀번호 : ");
		String passwd = sc.nextLine();
		
		System.out.print("3. 회원명 : ");
		String name = sc.nextLine();
		
		System.out.print("4. 연락처(휴대폰) : ");
		String mobile = sc.nextLine();

		// 위에서 입력한 것을 DB에 넣어준다. ▶ DTO ( where 는 map 에 담는다!)
		MemberDTO member = new MemberDTO();
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);

		int n = mdao.memberRegister(member);		// DAO 에 보내주어야 한다. (mdao 호출 → 그러기 위해 인터페이스에서 회원가입하는 메소드를 만들어야 한다.)
		
		if(n==1) {		
			System.out.println("\n >>> 회원가입 성공!! <<<");
			menu_Board(member, sc);		// 게시판 메뉴에 들어간다. ▶ 회원가입 성공시 게시판 메뉴로 들어가라.
			return member;
		}
		
		else {
			return null;		// db에서 잘못되면 null 을 넘겨주어야 한다.
		}
		
	}// end of private void memberRegister(Scanner sc)------------------------
	
	// case ② 로그인 처리 메소드 생성하기


	private MemberDTO login(Scanner sc) {	// return 타입이 memberDTO 인 login 메소드

		MemberDTO member = null;
		
		System.out.println("\n >>> ---- 로그인 ---- <<<");
		
		System.out.print("▷ 아이디 : ");
		String userid = sc.nextLine();			// 테이블에 있는 컬럼명 대로 넣자. (userid)
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();			// 테이블에 있는 컬럼명 대로 넣자. (passwd)
		
		Map<String, String> paraMap = new HashMap<>();	// where 는 map 이다.!!! 기억
		paraMap.put("userid", userid);	// paraMap 에 넣자. (put)
		paraMap.put("passwd", passwd);	// paraMap 에 넣자. (put)
		
		member = mdao.login(paraMap);
		
		if(member != null) {
			System.out.println("\n >>> 로그인 성공!! <<< \n");
		}
		else {
			System.out.println("\n >>> 로그인 실패!! <<< \n");
		}
		
		return member;
	}// end of private MemberDTO login(Scanner sc)------------------------
	
	
	
	// case ③ 게시판 메뉴 메소드 생성하기
	
	private void menu_Board(MemberDTO member, Scanner sc) {
		// menu_Board 를 호출한 곳에 가서 sc (받은것) 을 넘겨주면서 여기서도 sc를 쓸 수 있도록 한다.

		String menuNo = "";
		
		String adminOnly_menu = ("admin".equals(member.getUserid()))?"10. 모든회원정보보기":"";		// 삼항연산자, 10.모든회원정보를 기본값으로 주고, 변수처리해서 그때마다 바뀌게함. do~while 문 밖에 둬야하는 이유 : 로그아웃 때문에.
		// userid 가 admin 이라면 adminOnly_menu 변수에 넣어주겠다.
		
		do {
			System.out.println("============ 게시판 메뉴 ["+member.getName() +"님(point "+(member.getPoint())+") 로그인중..] ============\n"
							 + " 1.글목록보기   2.글내용보기     3.글쓰기   4.댓글쓰기 \n"
							 + " 5.글수정하기   6.글삭제하기     7.최근 1주일간 일자별 게시글 작성건수 \n"
							 + " 8.최근 2개월간 일자별 게시글 작성건수   9.나가기   "+ adminOnly_menu +"\n"
							 + " -------------------------------------------------------");

			System.out.print("▷ 메뉴번호 선택 : ");
			menuNo = sc.nextLine();
			
			switch (menuNo) {
				case "1":  // 글목록보기 (contents 컬럼 제외) ▶ DB 가서 조회해와야함.
					boardList();	// where 절이 없기 때문에 파라미터를 쓰지 않는다. (특정글보기가 아니기 때문에)
					break;

				case "2": // 글내용보기 (contents 컬럼 포함) 	// 조회수 업데이트 조건 (올린사람id != 읽은사람 id)
					viewcontents(sc, member.getUserid());	// member.getUserid() : 로그인한 ID
					// 보여만 주면 되므로 return 타입은 void (sysout)
					// 댓글은 그 클릭 했을때 글내용보기 밑에 댓글이 나타난다.
					break;
				
				case "3": // 글쓰기(jdbc_board 테이블에 insert 및 jdbc_member 테이블의 point 컬럼 update) 댓글은 +10점 // (로그인이 된 userid 가 들어가야 하므로 파라미터에 넣는다.)
					write(sc, member);
					break;
				
				case "4": // 댓글쓰기(jdbc_comment 테이블에 insert 및 jdbc_member 테이블의 point 컬럼 update) 댓글은 +5점
					write_comment(sc, member);	// 누가(member) 무엇을 쓰는지(sc)
					break;
					
				case "5": // 글수정하기				
					int n = updatePost(sc, member);		// 삭제해야할 글 번호 알아와야 함. (sc), 내가 쓴글만 지워야 한다.(로그인된 사람이 누구인지 알아야함.)
					
					if (n==0) {	// 수정할 글 번호가 Int 가 아닌 경우.
						System.out.println(">> [경고] 할 글번호 입력값은 숫자만 가능합니다. <<\n");
					}
					else if(n==1) {
						// n ==> 1 이라면 존재하지 않는 글번호(boardno)를 가지고 글을 수정하려는 경우
						System.out.println(">> [경고] 수정할 글번호는 존재하지 않는 글번호 입니다. <<\n");
					}
					else if(n==2) { 
						System.out.println(">> [경고] 다른 사용자의 글은 수정불가 합니다. <<\n");						
					}
					else if(n==3) { 
						System.out.println(">> [경고] 입력하신 글암호가 글작성시 입력한 암호와 일치하지 않습니다. <<\n");						
					}
					else if(n==4) {
						System.out.println(">> 글수정이 성공되었습니다.!! <<\n");						
					}
					else if(n==5) {
						System.out.println(">> [장애발생] DB에 장애가 발생한 관계로 글수정이 불가합니다.!! <<\n");						
					}
					else if(n==6) {
						System.out.println(">> 글수정을 취소하였습니다.!! <<\n");						
					}					
					break;
					
				case "6": // 글삭제하기
					n = deletePost(sc, member);		// 삭제해야할 글 번호 알아와야 함. (sc), 내가 쓴글만 지워야 한다.(로그인된 사람이 누구인지 알아야함.)
					
					if (n==0) {	// 삭제할 글 번호가 Int 가 아닌 경우.
						System.out.println(">> [경고] 삭제할 글번호 입력값은 숫자만 가능합니다. <<\n");
					}
					else if(n==1) {
						// n ==> 1 이라면 존재하지 않는 글번호(boardno)를 가지고 글을 삭제하려는 경우
						System.out.println(">> [경고] 삭제할 글번호는 존재하지 않는 글번호 입니다. <<\n");
					}
					else if(n==2) { 
						System.out.println(">> [경고] 다른 사용자의 글은 삭제불가 합니다. <<\n");						
					}
					else if(n==3) { 
						System.out.println(">> [경고] 입력하신 글암호가 글작성시 입력한 암호와 일치하지 않습니다. <<\n");						
					}
					else if(n==4) {
						System.out.println(">> 글삭제가 성공되었습니다.!! <<\n");						
					}
					else if(n==5) {
						System.out.println(">> [장애발생] DB에 장애가 발생한 관계로 글삭제가 불가합니다.!! <<\n");						
					}
					else if(n==6) {
						System.out.println(">> 글삭제를 취소하였습니다.!! <<\n");						
					}
					
					break;
					
				case "7": // 최근 1주일간 일자별 게시글 작성건수
					statisticsByWeek();		// DB 의 where 절 (where 절은 고정되어 있음 (7일 內로)) , 보여주기만 하면 끝남.(return 타입은 void)
					break;

				case "8": // 최근 2개월간 일자별 게시글 작성건수
					statisticsByRecent_TwoMonths();		
					break;
					
				case "9": // 나가기
					
					break;
					
				case "10": // 모든회원정보보기(관리자(admin)전용)
					
					if("admin".equals(member.getUserid())) {
						
						System.out.println("[1:회원명의 오름차순 / 2: 회원명의 내림차순 / \n "
										 + "3:가입일자 오름차순 / 4: 가입일자 내림차순]");
						System.out.print("▷ 정렬선택 : ");
						String sortChoice = sc.nextLine();
						
						if( !("1".equals(sortChoice) || "2".equals(sortChoice) || 
						      "3".equals(sortChoice) || "4".equals(sortChoice)) ) {													
							sortChoice = "1";			// 1 또는 2 또는 3 또는 4 를 제외한 나머지가 sortChoice 에 입력되면 1로 보겠다.
						}
						
						selectAllMember(sortChoice);	
						// 관리자를 제외한 모든 회원들을 선택한 정렬기준으로 보여주는 메소드 호출 // sortChoice 를 selectAllMember 에 넣고 원하는 정렬대로 회원정보 결과를 출력한다.		
						// 보여만 주면 끝나는 것이므로 return type 은 void 이다.
					}
					
					else {
						System.out.println(">> 메뉴에 없는 번호입니다.!! << \n");
					}
					
					break;
					
				default:
						System.out.println(">> 메뉴에 없는 번호입니다.!! << \n");
					break;

			}
			
		} while( !("9".equals(menuNo)) );	//menuNo 가 9라면 반복을 멈춰라. 
		
		// menuNo 인 9번 나가기를 입력할때까지 계~~속 반복해야함. (그렇다면 위의 것이 Set)
		
	}// end of private void menu_Board()--------------------------------------------


	// *** 글목록보기 메소드 생성하기 *** //
	private void boardList() {

		List<BoardDTO> boardList = bdao.boardList();		 // ① DB 에 간다. (bdao) // 컬럼컬럼 만들어가면서 자동적으로 DTO 를 만들어간다.
	
		if( boardList.size() > 0 ) {		// 넘어온 boardList의 size 가 
			// 게시글이 존재하는 경우
		
			System.out.println("\n------------------------ [게시글 목록] ------------------------");
			System.out.println("글번호\t글제목\t작성자\t작성일자\t\t\t조회수");
			System.out.println("------------------------------------------------------------");
			// 글자수가 8글자 이상일때 그 뒷부분을 ".." 로 대체한다.
			StringBuilder sb = new StringBuilder();
			
			for(BoardDTO board : boardList) {
				String subject = board.getSubject();
				
				if(subject.length() > 8) {
					subject = subject.substring(0, 8)+"..";		
				}
				else {
					int cnt = 10 - subject.length();		// ex 8 - "안녕하세요" : 3글자 남은만큼 공백을 붙여주겠다.
															// 10이 와야 위의 ".." 만큼 더 늘어나므로 8에서 10으로 바꾼다.
					String blank = "";
					for(int i=0; i<cnt; i++) {
						blank += " ";		// " " 만큼 더한다..
					}// end of for----------------------------------------------------
				
					subject += blank;
				
				}
				
				String commentcnt = (board.getCommentcnt() > 0)?"["+board.getCommentcnt()+"]":"";
			// 해당 원글에 딸린 댓글의 개수
			// ["+board.getCommentcnt()+"];
			// 댓글수가 0 이상이면 아무것도 나타내지 않고, 댓글수가 있으면 딸린 댓글(board.getCommentcnt()) 대로 표시해라. 	
		
				sb.append( board.getBoardno()+"\t"+ 
						   subject+commentcnt+"\t"+ 
						   board.getMember().getName()+"\t"+ 
						   board.getWriteday()+"\t"+ 
						   board.getViewcount()+"\n"
						  );
			}// end of for---------------------------------------------
		
			System.out.println(sb.toString());
			
		}
		else {
			// 게시글이 1개도 존재하지 않을 경우
			System.out.println(">> 글목록이 없습니다. \n <<");
		}
		
	}// end of private void boardList() ---------------------------------

	
	
	// *** 글내용보기 (contents 컬럼 포함) 메소드 생성하기 *** //
	private void viewcontents(Scanner sc, String login_userid) {
		
		System.out.println("\n>>> 글내용 보기 <<<");
		
		System.out.print("▷ 글번호 : ");
		String boardno = sc.nextLine();		// 컬럼네임과 맞추자 : boardno // 글번호 & 현재 로그인된애가 누군지 보내야한다. (where 는 MAP !!, 복수개)
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("boardno", boardno);			// key 값에 "boardno" 를 주겠다.
		paraMap.put("fk_userid", login_userid);		// 컬럼명인 fk_userid 로 key 값을 준다.
		
		BoardDTO board = bdao.viewContents(paraMap);					// MemberDAO 에 보내기 위해 paraMap 을 파라미터에 넣는다.
		// 있으면 객체 없으면 null, return 타입을 BoardDTO (있으면 BoardDTO 로 받아서) 
		
		if(board != null) {
			// 존재하는 글번호를 입력한 경우. // null 이 아니라면 화면에 결과물을 보이자.!!
				System.out.println("\n>> [글내용 보기] <<");
				System.out.println("▷ 글번호 : " + board.getBoardno() + "\n" 
								 +("▷ 작성자 : " + board.getMember().getName() + "\n" 
								 + "▷ 글제목 : " + board.getSubject() + "\n" 
								 + "▷ 글내용 : " + board.getContents() + "\n"
								 + "▷ 작성일자 : " + board.getWriteday() + "\n"
								 + "▷ 조회수 : " + board.getViewcount() + "\n" )) ;
			
			
			//////////////////////////////////////////////////////////////////////////////
			
			System.out.println(">> [댓글내용보기] <<");
			System.out.println("------------------------------------------------------------");
			
			List<BoardCommentDTO> commentList = bdao.commentList(boardno);		
			// 원글의 번호를 넣는다. // 댓글이 복수개로 여러개가 나올 수가 있다. & SQL 문이 나와야한다.
			// 리턴타입은 BoardCommentDTO.
			if( commentList.size() > 0 ) {
				// 딸린 댓글이 존재하는 경우

				System.out.println("댓글내용\t\t작성자\t작성일자");
				System.out.println("------------------------------------------------------------");

				StringBuilder sb = new StringBuilder();
				for(BoardCommentDTO comment : commentList) {
					sb.append( comment.getContents() + "\t" + comment.getMember().getName() +"\t" + comment.getWriteday() + "\n" );
				}// end of for------------------------------------------------------------------
				
				System.out.println(sb.toString());
				
			}
			else {
				// 딸린 댓글이 없음.
				System.out.println(" == 댓글내용 없음 ==");
				System.out.println("------------------------------------------------------------\n");
			}
			
		}
		
	}// end of private void viewcontents(Scanner sc, String string)---------------------------------

	
	
	
	// *** 글쓰기 메소드 생성하기 *** //
	private void write(Scanner sc, MemberDTO member) {

		System.out.println("\n >>> 글쓰기 <<<");
		
		System.out.println("1. 작성자명 : " + member.getName());	// 여기서 이름을 불러오기 위해 다 들어가있는 member 를 불러온다.
		
		System.out.print("2. 글제목 : ");
		String subject = sc.nextLine();

		System.out.print("3. 글내용 : ");
		String contents = sc.nextLine();
		
		System.out.print("4. 글암호 : ");
		String boardpasswd = sc.nextLine();
		
		
		String yn = "";
		do {
			System.out.print("\n>> 정말로 글쓰기를 하시겠습니까? [Y/N] << ");
			yn = sc.nextLine();
			
			if("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)) {
				break;		// 빠져나오겠다.
			}
			else {	
				System.out.println(">> [경고] Y 또는 N 만 입력하세요 !! << \n");
			}			
		} while(true);		// while 했을때만 insert 를 보내야 한다.
			
		if( "n".equalsIgnoreCase(yn) ) {	// N = 글쓰기 취소
			System.out.println(">> 글쓰기를 취소하셨습니다. << \n");
		}
		else {
			// 사용자가 "Y" 를 입력한 경우 ▶ 게시판DTO(boardDTO) 에 보내준다.(insert)
			BoardDTO board = new BoardDTO();
			board.setFk_userid(member.getUserid());		// boardno 는 시퀀스 이므로 넣을 필요가 없다.
			board.setSubject(subject);					// 위에 있는 것 끌어온다.		
			board.setContents(contents);
			board.setBoardpasswd(boardpasswd);
			
			int n = bdao.write(board);		// board 를 boardDAO 에 보낸다. (글쓰기 성공시 n == 1, 실패시 n == 0 이 나온다.)
											// DB 에서 insert 해주면 return 값은 한 행이 들어갔을 거니까 1이 나온다. 1이 아니면 꼬인 것임.
			if(n==1) {						// DML 문이므로 int 로 return 값을 받아오겠다. (select 가 아니다.)
				System.out.println(">> 글쓰기가 성공적으로 완료되었습니다. << \n");		// 게시판 테이블의 글쓰기 성공(insert) 시 멤버 테이블의 point 도 update 되어야 한다. 그때 commit 해야한다.
				member.setPoint( member.getPoint() + 10 );// ★★★★★ 반드시 기억!!
				// ▲ 글쓰기에 성공하면 바로 보여주는 포인트가 10 증가되도록 적용해야 한다. (로그인을 다시 해야만 업데이트 돼야하는게 X) --> 게시판메뉴, 시작메뉴에 포인트 update				
				// () 안에 현재포인트를 읽고 온 후 +10 증가.
			}
			else {
				System.out.println(">> 장애가 발생되어 글쓰기에 실패했습니다. << \n");
			}
		}									
	
	}// end of private void write(Scanner sc, String string)------------------------------------------------

	
	

	// *** 댓글쓰기 메소드 생성하기 *** // 
	private void write_comment(Scanner sc, MemberDTO member) {

		System.out.println("\n>>> 댓글쓰기 <<<");
		
		System.out.println("1. 작성자명 : " + member.getName());
		
		String fk_boardno = "";
		do {
			System.out.print("2. 원글의 글번호 : " );
			fk_boardno = sc.nextLine();			// ① 존재하는 글번호를 입력하는 경우 (원글의 boardno 가 얼마인지. ) ▶ 저장된 DB 에 가서 봐야한다. (DB 가서 확인한 후에 막을 수 있다.)
														// ② 존재하지 않는 글번호를 입력하는 경우 "23425346657" ▶ 저장된 DB 에 가서 봐야한다. (DB 가서 확인한 후에 막을 수 있다.)
														// ③ ㄴㅇㄹㄴㅇㄹ dfdsfgdsg 와 같이 엉뚱한 것을 입력했을 경우. (int 만 들어오게끔.)
			
			try {
				Integer.parseInt(fk_boardno);	// String 타입이지만 숫자형태로 된 String 타입
				break;	// 올바르게 숫자를 입력했다면 빠져나가자.
			} catch (NumberFormatException e) {			// 숫자외의 것을 입력하면 NumberFormatException	
				System.out.println(">>[경고] 원글의 글번호는 정수로만 입력하세요!! <<");
			}
		} while(true);
		
		String contents = "";
		do {
			System.out.print("3. 댓글내용 : " );			// ① "안녕하세요" 와 같이 잘 입력한 경우. (contents 는 not null 타입)
														// ② "    " 와 같이 공백만 입력한 경우. ▶ null 로 본다.
														// ③ 그냥 엔터만 입력한 경우. ▶ null 로 본다.
			contents = sc.nextLine();
			
			if(contents.trim().isEmpty()) {				// 공백을 제거했을 때 내용물이 하나도 없는가?
				// ② "    " 와 같이 공백만 입력한 경우. ▶ null 로 본다.
				// ③ 그냥 엔터만 입력한 경우. ▶ null 로 본다.
				System.out.println(">>[경고] 댓글내용은 엔터 또는 공백만으로는 입력할 수 없습니다.!! <<");
			}
			else {
				// 공백 말고 무언가 들어왔을 때
				break;	// 빠져나간다.
			}
			
		}while(true);
		// insert 일 것이므로 DTO 에 보낸다.
		
		BoardCommentDTO comment = new BoardCommentDTO(); // BoardCommentDTO 에 넣어주겠다.
		comment.setFk_boardno(fk_boardno);			// 원글의 글번호	
		comment.setFk_userid(member.getUserid());	// 댓글을 작성하고자 하는 사용자(현재 로그인한 사용자)의 userid. (id가 누구인가?)
		comment.setContents(contents);				// 댓글내용
		// writeday 는 defalut 값 이므로 적지 않아도 된다.

		int n = bdao.write_comment(comment);		// 0(회사잘못) or 1(알맞게넣음) or -1(유저잘못) 의 경우의수
		
		if(n == 1) {
			// 댓글쓰기 성공한 경우
			System.out.println(">> 댓글이 성공적으로 등록되었습니다.!! <<");
			member.setPoint( member.getPoint() + 5 );		// ★★★★★ 반드시 기억!!
			// ▲ 댓글쓰기에 성공하면 바로 포인트가 +5 증가 적용이 되어야 한다. (로그인을 다시 해야만 업데이트 돼야하는게 X) --> 게시판메뉴, 시작메뉴에 포인트 update
			// () 안에 현재포인트를 읽고 온 후 +5 증가.
		}
		else if(n == -1) {
			// 사용자가 댓글을 쓸 때, 존재하지 않는 원글번호를 입력한 경우.
			System.out.println(">> 올바른 원글번호를 입력하세요.!! <<");		
		}
		else {	// n == 0
			// 기타 장애가 발생한 경우 (개발 시 에러가 발생했을 때)
			System.out.println(">> [장애발생] DB장애 발생으로 인해 댓글 등록에 실패했습니다.!! <<");		
		}
		
	}// end of private void write_comment(Scanner sc, MemberDTO member)------------------------

	// *** 글수정하기 메소드 생성하기 *** //
	private int updatePost(Scanner sc, MemberDTO member) {

		int n = 0;
		
		System.out.println("\n>>> 글 수정하기 <<<");
		
		System.out.print("▷ 수정할 글번호 : ");
		String boardno =  sc.nextLine();
		
		try {
			Integer.parseInt(boardno);	// int 로 바뀐다.
			
			System.out.print("▷ 수정할 글제목 : ");
			String subject = sc.nextLine();
			
			System.out.print("▷ 수정할 글내용 : ");
			String contents = sc.nextLine();
			
			System.out.print("▷ 수정할 글의 글암호 : ");
			String boardpasswd = sc.nextLine();
			
			do {
				// DB에 넘어가기 전에 글 지울건지 물어봄.
				System.out.print(">> 정말로 글을 수정하시겠습니다?[Y/N] : ");
				String yn = sc.nextLine();
				
					if("n".equalsIgnoreCase(yn)) {	// 대,소문자 구분없이 n 입니까?
						n = 6;	
						// n== > 6 이라면 사용자가 글수정을 취소한 경우 (NO)
					
						break;
					}
					else if("y".equalsIgnoreCase(yn)) {	// 대,소문자 구분없이 n 입니까?
						// 사용자가 글을 수정하는 경우 (YES)
						
						Map<String, String> paraMap = new HashMap<>();			// where 는 Map
						paraMap.put("boardno", boardno);
						paraMap.put("boardpasswd", boardpasswd);
						paraMap.put("userid", member.getUserid());		// DB 로 가야한다.
						
						paraMap.put("subject", subject);		// DB 로 가야한다.
						paraMap.put("contents", contents);		// DB 로 가야한다.
						
						n = bdao.updatePost(paraMap);	
					
					/*
					 	n ==> 1 이라면 존재하지 않는 글번호(boardno)를 가지고 글을 수정하려는 경우		 	
						n ==> 2 이라면 다른 사용자의 글을 수정하려고 한 경우
						n== > 3 이라면 수정하려는 글암호가 글수정시 입력받은 글암호와 일치하지 않는 경우
						n== > 4 이라면 글수정이 성공한 경우
						n== > 5 이라면 DB(SQL문)에 장애가 발생한 경우
					*/
						break;
					}
					else {
						System.out.println(">> [경고] Y 또는 N 만 입력하세요.!! << \n");
					}
			}while(true);
		
		} catch (NumberFormatException e) {
			// str_boardno 값이 숫자로 변환 될 수 없는 경우 ▶ DB 로 아예 보내지 않는다.
			n = 0;
		}
		
		return n;
	}// end of private int updatePost(Scanner sc, MemberDTO member)------------------------------------

	
	
	

	// *** 글삭제하기 메소드 생성하기 *** //
	private int deletePost(Scanner sc, MemberDTO member) {

		int n = 0;
		
		System.out.println("\n>>> 글 삭제하기 <<<");
		
		System.out.print("▷ 삭제할 글번호 : ");
		String boardno =  sc.nextLine();
		
		try {
			Integer.parseInt(boardno);	// int 로 바뀐다.
			
			System.out.print("▷ 삭제할 글암호 : ");
			String boardpasswd =  sc.nextLine();
			
			do {
				// DB에 넘어가기 전에 글 지울건지 물어봄.
				System.out.print(">> 정말로 글을 삭제하시겠습니다?[Y/N] : ");
				String yn = sc.nextLine();
				
					if("n".equalsIgnoreCase(yn)) {	// 대,소문자 구분없이 n 입니까?
						n = 6;	
						// n== > 6 이라면 사용자가 글삭제를 취소한 경우 (NO)
					
						break;
					}
					else if("y".equalsIgnoreCase(yn)) {	// 대,소문자 구분없이 n 입니까?
						// 사용자가 글을 삭제하는 경우 (YES)
						
						Map<String, String> paraMap = new HashMap<>();			// where 는 Map
						paraMap.put("boardno", boardno);
						paraMap.put("boardpasswd", boardpasswd);
						paraMap.put("userid", member.getUserid());		// DB 로 가야한다.
						
						n = bdao.deletePost(paraMap);	
					
					/*
					 	n ==> 1 이라면 존재하지 않는 글번호(boardno)를 가지고 글을 삭제하려는 경우		 	
						n ==> 2 이라면 다른 사용자의 글을 삭제하려고 한 경우
						n== > 3 이라면 삭제하려는 글암호가 글삭제시 입력받은 글암호와 일치하지 않는 경우
						n== > 4 이라면 글삭제가 성공한 경우
						n== > 5 이라면 DB(SQL문)에 장애가 발생한 경우
					*/
						break;
					}
					else { 
						System.out.println(">> [경고] Y 또는 N 만 입력하세요.!! << \n");
					}
			}while(true);
		
		} catch (NumberFormatException e) {
			// str_boardno 값이 숫자로 변환 될 수 없는 경우 ▶ DB 로 아예 보내지 않는다.
			n = 0;
		}
		
		return n;
	}// end of private int deletePost(Scanner sc, MemberDTO member)------------------------------------

	
	

	// *** 최근 1주일간 일자별 게시글 작성건수 메소드 생성하기 *** //
	private void statisticsByWeek() {
		
		System.out.println("\n------------------ [최근 1주일간 일자별 게시글 작성건수] ------------------");
		
		String title = "전체\t";	// title 의 초기치에는 전체\t 를 준다.
		
		// 만약 오늘이 2022-02-03 이라면
		// 전체  2022-01-28  2022-01-29  2022-01-30  2022-01-31  2022-02-01  2022-02-02  2022-02-03
		// 와 같이 제목을 나타내고자 한다.
		for(int i=0; i<7; i++) {
			title += MyUtil.addDay(i-6) + "   ";	// title 에 계속 붙여나가는 식으로 반복 (6일전(-6), 5일전, 4일전, 3일전, 2일전, 1일전, 오늘(0))
		}// end of for------------------------------------------------
		
		System.out.println(title);
		// 전체  2022-01-28  2022-01-29  2022-01-30  2022-01-31  2022-02-01  2022-02-02  2022-02-03

		System.out.println("----------------------------------------------------------------------");
		
		Map<String, Integer> resultMap = bdao.statisticsByWeek();	// 최근 1주일간 일자별 게시글 작성건수를 select 해서 나오는 결과물 // 메소드 이름과 똑같이 만든다.							
																	// DTO 는 일반적으로 INSERT 및 SELECT 와 쓰일 수 있음. 그러나 현재는 오로지 SELECT 된 결과물만 가져오기 때문에,
																	// ▶ return 타입을 map 으로 한다.
		
		String result = resultMap.get("TOTAL") + "\t"  + 			// DAO 에서 put 해온것을 get 해오자.
						resultMap.get("PREVIOUS6") + "\t\t"  +
						resultMap.get("PREVIOUS5") + "\t\t"  +
						resultMap.get("PREVIOUS4") + "\t\t"  +
						resultMap.get("PREVIOUS3") + "\t"  +
						resultMap.get("PREVIOUS2") + "\t\t"  +
						resultMap.get("PREVIOUS1") + "\t"  +
						resultMap.get("TODAY");
	
		System.out.println(result);
		System.out.println("");	// 한 줄 띄우기.
		
	}// end of private void statisticsByWeek()------------------------
	
	
	
	
	// *** 최근 2개월간 일자별 게시글 작성건수 *** // 
	private void statisticsByRecent_TwoMonths() {
		
		Calendar currentDate = Calendar.getInstance(); 
		// 현재날짜와 시간을 얻어온다.
		
		currentDate.add(Calendar.MONTH, -1); 	// currentDate 가 1달 전으로 변경된다. 

		SimpleDateFormat sdatefmt = new SimpleDateFormat("yyyy년 MM월");		
		String previous_1_month = sdatefmt.format(currentDate.getTime());	// 1달 전 (2022년 01월)
		
		currentDate = Calendar.getInstance(); 
		// 현재날짜와 시간을 얻어온다.

		String current_month = sdatefmt.format(currentDate.getTime());	// 현재 (2022년 02월)

		
		System.out.println("\n>>>> [ "+previous_1_month+", "+current_month+" 일자별 게시글 작성건수] <<<<\n");
	//	System.out.println("\n>>>> [ 2022년 01월, 2022년 02월 일자별 게시글 작성건수] <<<<");

		System.out.println("------------------------------");
		System.out.println("작성일자\t     작성건수\t ");
		System.out.println("------------------------------");
		 
		List<Map<String, String>> mapList = bdao.statisticsByRecent_TwoMonths();		// 한 행이 map 이다. map 이 복수개이므로 List.
		
		if(mapList.size()>0) {		// size 의 알맹이가 있습니까? (ex. 작성한 글이 있는가?)
		
			StringBuilder sb = new StringBuilder();
			
			for(Map<String, String> map : mapList) {
				sb.append( map.get("WRITEDAY") + "\t" + map.get("CNT") + "\n" );	// key 값.
			}// end of for------------------------------
			System.out.println(sb.toString());		// sb 까지만 써도 됨. StringBuilder 를 String 타입으로 바꿔준 것.
		}
		else {		// 작성한 글이 없으면?
			System.out.println(" 작성된 게시글이 없습니다. ");
		}
	}// end of private void statisticsByRecent_TwoMonths()----------------------------------------------

	
	
	
	// *** 관리자를 제외한 모든 회원들을 선택한 정렬기준으로 보여주는 메소드 생성하기 *** // 
	private void selectAllMember(String sortChoice) {		
	
		System.out.println("\n>>> ========== 모든 회원정보 ========== <<<");
		System.out.println("--------------------------------------------------------");
		System.out.println("회원번호   아이디   성명   연락처       포인트      가입일자          탈퇴유무 ");
		System.out.println("--------------------------------------------------------");

		List<MemberDTO> memberList = mdao.selectAllMember(sortChoice);
		// return 타입이 MemberDTO 인 DB 에서 회원정보를 불러와야함. (DAO 로 간다.) // field 에서 해온 mdao에 간다. 
		// 정렬때문에 파라미터가 필요하다.(그래야 order by 를 할 수 있다.), return 타입은 DTO(한 행은 DTO), DTO는 복수개이므로 배열이 아니라 collection 를 쓴다. (가입자 수를 예측할 수 없다.)
		// Collection : List / Map 을 기억하자. (map 은 key 값을 다 알아야 하지만, List 는 key 를 알 필요없이 0부터 끝까지 이다. ▶ List 를 쓰자.) 
		// return 타입은 List 인데, 구성원의 한 행 한 행은 MemberDTO .
		// memberDAO 는 interface 로 받았기 때문에 interface 에 selectAllMember의 메소드를 만들어 주겠다.
		
		// ** admin 빼고 가입한 회원이 없는 경우의 수  (rs.next() == false 임.) ** 
		// List<MemberDTO> memberList = new ArrayList<>(); 에서 add 한 적이 없기 때문에 빈껍데기만 넘어감.(size 는 0..)
		
		int memberCount = memberList.size();	// "admin" 을 제외한 회원수 ▶ 회원수(알맹이) 있는지 없는지 체크
		
		if(memberCount > 0) {	// admin 을 제외하고 회원수가 0 초과인 상태. (회원수가 있으면, 끄집어 낸다.)
		
			StringBuilder sb = new StringBuilder();
			
			for(MemberDTO member : memberList) {				// 확장 for문 ▶ 배열 사용
				

				String str_status = (member.getStatus() == 1)?"가입중":"탈퇴함";			// 회원탈퇴여부가 0,1 로 나오는 것 대신 가입/탈퇴로 보여주면서 변수처리.				
		
				
				// MemberDAO 에서 set 한 것을 get 해온다. ▼
				sb.append( member.getUserseq() + "   " + 		// 차곡차곡 쌓아준다.
						   member.getUserid() + "   " + 
						   member.getName() + "   " + 
					       member.getMobile() + "   " + 
					       member.getPoint() + "   " + 
						   member.getRegisterday() + "   " +
						   str_status + "\n" );
			}// end of for---------------------------
		
			System.out.println(sb.toString());
			
		}
		else {	// admin 을 제외하고 회원수가 0 명.
			System.out.println("가입된 회원이 없습니다.");
		}
	}// end of private void selectAllMember(String sortChoice)------------------------------------

}
