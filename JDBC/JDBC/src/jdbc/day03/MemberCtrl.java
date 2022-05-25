package jdbc.day03;

import java.util.*;

public class MemberCtrl {

	// attribute, field, property, 속성
	MemberDAO mdao = new MemberDAO();
	
	
	// operation, method, 기능
	
	// **** 시작메뉴를 보여주는 메소드 **** //
	public void menu_Start(Scanner sc) {		// main 에서 만들어온 menu_Start를 멤버ctrl 클래스에서 가져온다.
		
		MemberDTO member = null;		// do~while 문 밖으로 빼야 한다. (login 이 되었는데 반복문 돌면서 자꾸 null 값으로 만들면 안됨.)
		String s_Choice = "";
		
		do {
			// 로그인 후 시작메뉴에 이름&로그아웃 창을 띄우기 위함. (▼변수 loginName)
			// String loginName = (member!=null)?"["+member.getName()+"님 로그인중..]":"";		// 삼항연산자 사용 (member 가 null 아니라면 첫번째"" 를 주고, null 이라면 두번째 "" 를 준다.
			// String login_logout = (member!=null)?"로그아웃":"로그인";						// member.getName() 는 member 중에서도 name 을 뽑아온다.
			
			String loginName = "";				// 로그인을 안했으면 "" 가 들어옴.
			String login_logout = "로그인";		// 로그인을 안했을 땐 "로그인" 창을, 했을땐 "로그아웃" 창을.
			String menu_myInfo = "";
			// ▼ 위에서 썼던 삼항연산자를 if문으로 바꿈.
			if(member!=null) {
				loginName = "["+member.getName()+"님 로그인중..]";
				login_logout = "로그아웃";
				menu_myInfo = "4. 내정보보기  5. 회원탈퇴하기\n";
			}
			
			System.out.println("\n >>> ------- 시작메뉴 "+ loginName +"------- <<< \n"
							 + "1. 회원가입   2."+login_logout+"   3.프로그램종료\n"			// 2. 뒤에 로그인 or 로그아웃 (그때마다 바뀌기 때문에 변수처리를 해준다.)
							 + menu_myInfo												// 내 정보보기는 내가 로그인 했을 때만 떠야하기 때문에 변수처리 해준다.
							 + "---------------------------------\n");
			
			System.out.print("▷메뉴번호선택 : ");
			s_Choice = sc.nextLine();
			
			switch (s_Choice) {
				case "1":	// 회원가입	// case 1 밑에 회원가입을 하는 메소드를 적기에는 복잡하므로 *메소드를 따로* 만든다.
					memberRegister(sc);				
					break;
					
				case "2":	// 로그인 OR 로그아웃 // member 를 판단기준으로 삼기.(login 여부)
					if("로그인".equals(login_logout)) {		
						member = login(sc);	// 로그인 시도하기 // login 입력 시 유저가 입력해야하기 때문에 파라미터에 sc 를 넘겨주어야 한다.
					}
					else {
						member = null;	// 로그아웃 하기
						System.out.println(">>> 로그아웃 되었습니다. <<< \n");
					}
					
					break;							
					
				case "3":	// 프로그램종료
					
					break;	
	
				case "4":	// 내정보보기	▶ 로그인을 안한 상태에서 4번을 선택하면 메뉴에 없는 번호라고 떠야함. : " 메뉴에 없는 번호입니다 .!! "
					if(member!=null) {
						// 내정보보기 (로그인 된 상태, member 변수가 null 이 아닌 상태)
						// System.out.println(member.toString());		// 원래는 member 만 넣었을 때 메모리 주소(jdbc.day03.MemberDTO@588df31b)만 나오게됨. ▶ DTO class 가서 재정의(overriding)를 해준다. ▶ 내정보 출력됨.
						// 또는
						System.out.println(member);
					}
					else {					
						System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요.!! <<< \n");
					}
					
					break;
					
				case "5":	
					if(member!=null) {
						// 회원탈퇴하기
						int n = mdao.memberDelete( member.getUserseq() );		// 해당 int 는 userseq 가 와야한다. // member 에서 본인의 회원번호(userseq) 를 가져와야 한다.					
					
						if(n==1) {
							System.out.println(">>> 회원탈퇴가 성공적으로 이루어졌습니다.!! <<<");
							member = null;			// null 을 주어야만 case문 빠져나간 후 do~while 문에서 null 처리 후의 결과가 나옴.
						}
					}
					else {
						System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요.!! <<< \n");					
					}
					break;	
	
				default:
					System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요.!! <<< \n");
					break;
				
			}// end of switch------------------------------------
			
		} while(!("3".equals(s_Choice)));			// 3번 종료할때까지 계속 do~while 반복해야함.


		
	}// end of public void menu_Start(Scanner sc)----------------------------------------
	


	// ① case "1" 의 회원가입 메소드 만들기 // ★ DB에 넣을 것이기 때문에 오라클 테이블 컬럼명과 똑같이 해준다.
	private void memberRegister(Scanner sc) {	// 회원이 직접 입력해야함. (Scanner sc)
		
		System.out.println(">>> --- 회원가입 --- <<<");
		
		System.out.print("1. 아이디 : ");
		String userid = sc.nextLine();		// 입력받은 아이디

		System.out.print("2. 비밀번호 : ");
		String passwd = sc.nextLine();		// 입력받은 비밀번호
		
		System.out.print("3. 회원명 : ");
		String name = sc.nextLine();		// 입력받은 회원명
		
		System.out.print("4. 연락처(휴대폰) : ");
		String mobile = sc.nextLine();		// 입력받은 연락처

		MemberDTO member = new MemberDTO();
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		int n = mdao.memberRegister(member);
		
		if(n==1) {
			System.out.println("\n >>> 회원가입을 축하드립니다. <<<");
		}
		


	}// end of private void memberRegister(Scanner sc)------------------------------------
	
	
	// ②case "2" 의 로그인을 처리해주는 메소드 생성하기 //
	private MemberDTO login(Scanner sc) {
		
		MemberDTO member = null;
		
		System.out.println("\n >>> ---- 로그인 ---- <<< ");
		
		System.out.print("▷ 아이디 : ");
		String userid = sc.nextLine();		// "leess" "eomjh"
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap =  new HashMap<>();		// <K,V> 값은 왼/오 둘다 넣어도 무방. // Map 자주 쓰이므로 꼭 기억 ★★★ // ▶ 입력한 id, pw 를 paraMap 에 담은 후 DAO 에 보낸다.
		paraMap.put("userid", userid);		// userid(V) 에 "leess"(K)
		paraMap.put("passwd", passwd);		// passwd(V) 에 "qwer1234$"(K)
											// id가 이미 존재하는지 확인하기 위함 → memberDAO 에 보낸다.
		member = mdao.login(paraMap);		// DB 에서 가져옴.
		
		if(member != null) {				// 이 member 가 null 인지 아닌지를 보기 위함.
			System.out.println("\n >>> 로그인 성공!! <<< \n");
		}
		
		else {
			System.out.println("\n >>> 로그인 실패!! <<< \n");			
		}
		
		return member;
		

		
	}// end of private MemberDTO login(Scanner sc)------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
