
----- ***** MyMVC 에서 작업한 것이다.***** -----

show user;
-- USER이(가) "SYS"입니다.

create user mymvc_user identified by cclass default tablespace users;
-- User MYMVC_USER이(가) 생성되었습니다.

grant connect, resource, create view, unlimited tablespace to mymvc_user;
-- Grant을(를) 성공했습니다.

grant update, insert, delete, select, drop on board_qna to mymvc_user;


show user;
-- USER이(가) "mymvc_user"입니다.

ALTER USER MYMVC_USER ACCOUNT UNLOCK;

ALTER USER MYMVC_USER IDENTIFIED BY cclass;

GRANT connect, resource, create view, create table , create sequence, unlimited tablespace to mymvc_user;

SELECT * FROM ALL_USERS;


create table tbl_main_image
(imgno           number not null
,imgfilename     varchar2(100) not null
,constraint PK_tbl_main_image primary key(imgno)
);

create sequence seq_main_image
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '미샤.png');  
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '원더플레이스.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '레노보.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '동원.png'); 

commit;

select imgno, imgfilename
from tbl_main_image 
order by imgno asc;

String sql = "select imgno, imgfilename\n"+
"from tbl_main_image \n"+
"order by imgno asc";




---------------------------------
----- **** 회원 테이블 생성 **** ------
-- drop table tbl_member purge;
alter table tbl_member
modify userid varchar2(40);

create table tbl_member
(userid             varchar2(40)   not null  -- 회원아이디
,pwd                varchar2(200)  not null  -- 비밀번호 (SHA-256 암호화 대상)
,name               varchar2(30)   not null  -- 회원명
,email              varchar2(200)  not null  -- 이메일 (AES-256 암호화/복호화 대상)
,mobile             varchar2(200)            -- 연락처 (AES-256 암호화/복호화 대상) 
,postcode           varchar2(5)              -- 우편번호
,address            varchar2(200)            -- 주소
,detailaddress      varchar2(200)            -- 상세주소
,extraaddress       varchar2(200)            -- 참고항목
,gender             varchar2(1)              -- 성별   남자:1  / 여자:2
,birthday           varchar2(10)             -- 생년월일   
,coin               number default 0         -- 코인액
,point              number default 0         -- 포인트 
,registerday        date default sysdate     -- 가입일자 
,lastpwdchangedate  date default sysdate     -- 마지막으로 암호를 변경한 날짜  
,status             number(1) default 1 not null     -- 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
,idle               number(1) default 0 not null     -- 휴면유무      0 : 활동중  /  1 : 휴면중 
,constraint PK_tbl_member_userid primary key(userid)
,constraint UQ_tbl_member_email  unique(email)
,constraint CK_tbl_member_gender check( gender in('1','2') )
,constraint CK_tbl_member_status check( status in(0,1) )
,constraint CK_tbl_member_idle check( idle in(0,1) )
);

select *
from tbl_member
order by registerday desc;

-- 엄정화 비번은 'qwer1234$' 이다.!! eom@naver.com / 010-1234-2325
-- 이순신 비번은 'qwer1234$' 이다. leess@naver.com / 010-1234-5678
-- 암호화 안된 것들은 지우자.
delete from tbl_member
where userid = 'Superman';

commit;

-- 테이블 자바 변경

delete from tbl_member
where userid = "leess";


-- 내 개인 board_qna 게시판 작업 목록 -- (semi_my_board) 에서 작성한 것임.
create table board_qna(
    board_num number,
    board_writer varchar2(20),
    board_subject varchar2(50),
    board_content varchar2(4000),
    board_file varchar2(50),
    board_re_ref number,
    board_re_lev number,
    board_re_seq number,
    board_readcount number,
    board_date date
)

commit;

select *
from board_qna;

create SEQUENCE board_num;

-- 제약조건 추가
alter table board_qna
add constraint pk_board_num primary key(board_num);



drop table BOARD_QNA;


commit;


-- 유튜브 따라하면서 만든것
create table board_qna (
    num number primary key,
    title varchar2(50) not null,
    writer varchar2(50) not null,
    content varchar2(1000),
    regdate date,
    cnt number default 0
);

SELECT * FROM USER_SEQUENCES

drop sequence board_seq;

create sequence board_seq
start with 1
increment by 1
maxvalue 99999
nocache
nocycle
noorder;

commit;

select *
from board_qna;

select *
from tbl_member
order by registerday desc;

SELECT * FROM USER_TABLES;

-- 삽입
insert into board_qna (num,title,writer,content,regdate,cnt)
values(board_seq.nextval, '제목1','작성자1','내용1', sysdate ,0);

alter table board_qna CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;

-- 삽입 자바버전으로 바꾸기
String sql = "insert into board_qna (num,title,writer,content,regdate,cnt)\n"+
"values(board_seq.nextval, '제목1','작성자1','내용1', sysdate ,0);";

-- 조회 (selectAll)
select num,title,writer,content,regdate,cnt
from board_qna
order by num desc;


String sql = "select num,title,writer,content,regdate,cnt\n"+
"from board_qna";

-- 조회 시, '하나의 게시글'만 조회해야 할 때도 있으므로, 쿼리문을 또 만들어야 한다. (selectOne)
select "num","title","writer","content","regdate","cnt"
from board_qna
where "num"=?;

-- 수정
update board_qna set title='제목수정',content='내용수정' 
where num = 4;

String sql = "update board_qna set title='제목수정',content='내용수정' \n"+
"where num = 4;";

-- 삭제
delete from board_qna 
where num = 1;

String sql = "delete from board_qna \n"+
"where num = 1;";

-- 게시글 등록해보기
insert into board_qna (num,title,writer,content,regdate,cnt)
values(board_seq.nextval, '제목2','작성자2','내용2', sysdate ,0);

commit;



-- 로그인 기록 테이블 _0322 수업 (erd 모델링을 바탕으로 생성한다.)
create table tbl_loginhistory
(fk_userid   varchar2(40) not null 
,logindate   date default sysdate not null
,clientip    varchar2(20) not null
,constraint FK_tbl_loginhistory foreign key(fk_userid) references tbl_member(userid)  
);
-- Table TBL_LOGINHISTORY이(가) 생성되었습니다.

select *
from tbl_loginhistory;



--- 로그인을 처리하기 위한 SQL 문 작성하기 0323 --- 내 정보 수정까지 가능하게 하기 위함. ( JOIN 테이블 )
describe tbl_member;

SELECT userid, name, email, mobile, postcode, address, detailaddress, extraaddress
     , gender, birthyyyy, birthmm, birthdd, coin, point, registerday, pwdchangegap
     , nvl(lastlogingap, trunc( months_between(sysdate, registerday) ) ) as lastlogingap
FROM 
(
select userid, name, email, mobile, postcode, address, detailaddress, extraaddress
     , gender, substr(birthday,1,4) as birthyyyy, substr(birthday,6,2) as birthmm, substr(birthday,9,2) as birthdd
     , coin, point, to_char(registerday, 'yyyy-mm-dd') as registerday
     , trunc( months_between(sysdate, lastpwdchangedate) ) as pwdchangegap -- 3개월미만일 시 (2.9) 이상일 시(3.00) -- pwd 를 언제 바꿨는지(마지막으로 바꾼 날짜가 3개월전후인지의 간격)
from tbl_member
where status = 1 and userid = 'leess' and pwd = '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382'
) M
CROSS JOIN
(
select trunc ( months_between(sysdate, max(logindate)) ) as lastlogingap       -- 제일 큰 값 max( 마지막으로 들어온 날 , 예를들어 '마지막으로' 로그인 한지 1년이 지났는지 아닌지 체크)
from tbl_loginhistory
where fk_userid = 'leess'
) H;

--------------------- 
-- ** 마지막으로 로그인 한지 12개월이 초과되었을 경우 테스트하려고 하는 것이다.! ** --
update tbl_member set registerday = add_months(registerday, -13)
where userid = 'leess';
-- 가입한 날짜에 (-13 개월)을 넣어주겠다.

update tbl_member set lastpwdchangedate = add_months(lastpwdchangedate, -13)
where userid = 'leess';
-- 마지막으로 암호를 바꾼지 (-13 개월)을 넣어주겠다.

update tbl_loginhistory set logindate = add_months(logindate, -13)
where fk_userid = 'leess';
-- 로그인한 기록을 13개월 전으로 돌리겠다.

select fk_userid, to_char(logindate, 'yyyy-mm-dd hh24:mi:ss') as logindate , clientip 
from tbl_loginhistory;

commit;
-- 커밋 완료


-- 원상복구하기 *** (위에거에서 다시 +13개월 해서 변경했다.)
update tbl_member set registerday = add_months(registerday, -13)
                    , idle = 0
where userid = 'leess';

update tbl_member set lastpwdchangedate = add_months(lastpwdchangedate, 13)
where userid = 'leess';

update tbl_loginhistory set logindate = add_months(logindate, 13)
where fk_userid = 'leess';

select fk_userid, to_char(logindate, 'yyyy-mm-dd hh24:mi:ss') as logindate , clientip 
from tbl_loginhistory;

commit;
-- 커밋 완료

-- 원복끝 **********

select *
from tbl_member
order by registerday desc;

select fk_userid, to_char(logindate, 'yyyy-mm-dd hh24:mi:ss') as logindate , clientip 
from tbl_loginhistory;

-- 여기서 로그인 한 적이 없으면 null 이 나오게 되는데, 이때 nvl 을 사용해서 null 을 숫자로 바꿔준다. (null 값이 들어오면 안되기 때문에 nvl 을 사용해야 한다.)
--> nvl(lastlogingap, trunc( months_between(sysdate, registerday) ) ) as lastlogingap
-- months_between(sysdate, registerday) 현재 날짜와 회원으로 가입한 날짜.


-- ** 마지막으로 비밀번호를 변경한지 3개월이 초과되었을 경우 테스트하려고 하는 것이다.! ** --
update tbl_member set lastpwdchangedate = add_months(lastpwdchangedate, -5)
where userid = 'leess';

commit;
-- 커밋 완료.

-- 원복 시작 (비밀번호 변경한지 -5개월 이던것을 다시 +5개월 해서 원래대로 한다.)
update tbl_member set lastpwdchangedate = add_months(lastpwdchangedate, 5)
where userid = 'leess';
-- 원복 끝

commit;
-- 커밋 완료.







-- [개인용] --

------- 글쓰기 게시판 따라하기 MVC 패턴 2 (220321) --------
create table Qna_Board
( boardQna_num NUMBER NOT NULL,
  boardQna_writer VARCHAR2(50),
  boardQna_subject VARCHAR2(100),
  boardQna_content VARCHAR2(2000),
  boardQna_file VARCHAR2(100),
  BoardQna_re_ref NUMBER,
  BoardQna_re_lev NUMBER,
  BoardQna_re_seq NUMBER,
  BoardQna_readcount NUMBER,
  BoardQna_date DATE,
  CONSTRAINT PK_Qna_Board PRIMARY KEY(boardQna_num)
);
-- Table QNA_BOARD이(가) 생성되었습니다.

create sequence boardQna_num;
-- Sequence BOARDQNA_NUM이(가) 생성되었습니다.

drop table Qna_Board;

drop sequence qna_seq;

commit;



---- 0322 최종 플젝용 테이블

create table qna_board
( qnaNum        NUMBER          PRIMARY KEY,    -- 글번호
  qnaWriter     VARCHAR2(50),                   -- 작성자
  qnaSubject    VARCHAR2(100),                  -- 글제목
  qnaContent    VARCHAR2(2000),                 -- 글내용
  qnaReadCount  NUMBER          default 0,      -- 조회수
  qnaRegDate    DATE            default sysdate -- 작성일
);
-- Table QNA_BOARD이(가) 생성되었습니다.

create sequence board_seq
start with 1
increment by 1
maxvalue 99999
nocache
nocycle
noorder;
-- Sequence BOARDQNA_NUM이(가) 생성되었습니다.

--- 전체글 목록 조회 sql 문 ①
select qnaNum
      , case when length(qnaSubject) > 15 then substr(qnaSubject, 0, 15) || '...' else qnaSubject end AS qnaSubject 
      , qnaWriter 
      , to_char(qnaRegDate, 'yyyy-mm-dd') as qnaRegDate
      , qnaReadCount
from qna_board
order by qnaNum desc ;



select * 
from qna_board

insert into qna_board(qnaNum, qnaWriter, qnaSubject) values(board_seq.nextval, '작성자1', '안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요')
insert into qna_board(qnaNum, qnaWriter, qnaSubject) values(board_seq.nextval, '작성자2', '배송 문의 드립니다!!!')
insert into qna_board(qnaNum, qnaWriter, qnaSubject) values(board_seq.nextval, '작성자3', '환불 하고 싶습니다. 답변부탁드립니다.')
insert into qna_board(qnaNum, qnaWriter, qnaSubject) values(board_seq.nextval, '작성자4', '제 물건은 언제 배송 오나요???')

commit;
