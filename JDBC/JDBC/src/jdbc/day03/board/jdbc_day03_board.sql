    set hidden param parseThreshold = 150000;   
    
    show user;
    -- USER이(가) "HR"입니다.
    
    
    
        ---- *** 회원 테이블 생성하기 *** ----
    select *
    from user_tables
    where table_name = 'JDBC_MEMBER';
    
    create table jdbc_member
    (userseq       number        not null    -- 회원번호
    ,userid        varchar2(30)  not null    -- 회원아이디   // 회원탈퇴를 해도 게시물이 자동으로 삭제가 되지 않는 경우 / 되는 경우
    ,passwd        varchar2(30)  not null    -- 회원암호
    ,name          varchar2(20)  not null    -- 회원명
    ,mobile        varchar2(20)              -- 연락처
    ,point         number(10) default 0      -- 포인트     // 활동시 주겠다. ▶ update 가 필요.
    ,registerday   date default sysdate      -- 가입일자 
    ,status        number(1) default 1       -- status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴 
    ,constraint PK_jdbc_member primary key(userseq)
    ,constraint UQ_jdbc_member unique(userid)
    ,constraint CK_jdbc_member check( status in(0,1) )
    );
    -- Table JDBC_MEMBER이(가) 생성되었습니다.

    create sequence userseq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    -- Sequence USERSEQ이(가) 생성되었습니다.
    
    select *
    from jdbc_member
    order by userseq asc;
    
    update jdbc_member set status = 1;
    
    commit;
    
    ---- *** 게시판 테이블 생성하기 *** ----
    create table jdbc_board     
    (boardno       number        not null          -- 글번호
    ,fk_userid     varchar2(30)  not null          -- 작성자아이디
    ,subject       varchar2(100) not null          -- 글제목
    ,contents      varchar2(200) not null          -- 글내용
    ,writeday      date default sysdate not null   -- 작성일자
    ,viewcount     number default 0 not null       -- 조회수 
    ,boardpasswd   varchar2(20) not null           -- 글암호 (게시물에 비밀번호 설정) 
    ,constraint PK_jdbc_board primary key(boardno)
    ,constraint FK_jdbc_board foreign key(fk_userid) references jdbc_member(userid) 
    );
    -- 게시판 테이블의 글쓰기 성공(insert) 시 멤버 테이블의 point 도 update 되어야 한다.
    -- 즉, ★insert(게시판 테이블) 와 update(멤버 테이블 포인트) 둘 다 성공해야 commit★ 이다.!!! 
    -- 글쓰기(insert) 만 성공했다고 commit 을 하면 안된다. 글만쓰고 포인트를 안주면 안됨..
    -- insert 성공 한 후, update 성공되면 그때 commit 을 한다. 
    -- 한 세트 트랜잭션!!! (글쓰기와 업데이트 모두 성공한 후에 commit을 한다.)
    create sequence board_seq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    desc jdbc_board;
    
    select *
    from jdbc_board
    order by boardno desc;
    
    select *
    from jdbc_member
    order by userseq asc;
    
    commit;
    
    update jdbc_member set status = 1
    where userid = 'leess';
    
    commit; -- 반드시 commit 해야한다.
    
    --------------------------------------------------------------------------------
    insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd)
    values(board_seq.nextval, 'leehr', '안녕하세요', '처음뵙겠습니다', '1234');

    insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd)
    values(board_seq.nextval, 'eomjh', '반갑습니다', '잘부탁드립니다', '1234');

    insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd)
    values(board_seq.nextval, 'leess', '배고파요', '점심시간이다~~', '1234');

    insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd)
    values(board_seq.nextval, 'eomjh', '뭐먹을까', '맛있는거먹어요', '1234');
    
    
    select B.boardno, B.subject, M.name
         , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss'), B.viewcount
    from jdbc_board B JOIN jdbc_member M
    ON B.fk_userid = M.userid
    order by boardno desc;
    
     
    select *
    from jdbc_board
    where boardno = 10; 
    
    select *
    from jdbc_board
    where boardno = 11 and fk_userid = 'leess';
    -- 글번호가 11 이면서 아이디 leess 가 쓴 글.
    
   select *
    from jdbc_board
    where boardno = 9 and fk_userid = 'leess';
    -- 글번호 9 는 존재하지만 결과가 뜨지 않는 경우는, leess 가 쓴 글이 아닌 것이다.
    
    select *
    from jdbc_board
    where boardno = 'ㄹㄴㅇㄹㄴㅇㄹㄴ'; 
    -- ORA-01722: invalid number
    
    select *
    from jdbc_board
    where boardno = 35434       -- 존재하지 않는 글번호
    
    select *
    from jdbc_board
    where boardno = 9 
    
    
    select * 
    from jdbc_member
    
    update jdbc_member set passwd = '5555'
    where userid = 'leess';
    -- commit or rollback 을 하지 않았을 때, cmd 상에서 dml 을 해주면 해당 행만 lock 이 걸린다. (수동커밋이므로)
    -- 오라클은 수동커밋이므로, 아예 처음부터 오토커밋을 함으로써 혼란을 없앤다.
    
    rollback;
    -- lock 이 풀리게됨. (cmd 상에서 1 row updated.)
    
    
    -- BOARD_SEQ 시퀀스를 사용했을 때 다음에 들어올 값을 알고자 할 경우. --
    select * 
    from user_sequences
    where sequence_name = 'BOARD_SEQ';
    -- 시퀀스 그 다음에 들어올 값이 얼만지 보기 위해서 LAST_NUMBER 를 참고한다. (지금은 13)
    
    
    
    --------------------------------------------------------------------
    
    ---- *** 댓글 테이블 생성하기 *** ----
    create table jdbc_comment 
    (commentno   number        not null    -- 댓글번호 
    ,fk_boardno  number        not null    -- 원글의 글번호 
    ,fk_userid   varchar2(30)  not null    -- 사용자ID
    ,contents    varchar2(200) not null    -- 댓글내용 
    ,writeday    date default sysdate      -- 작성일자
    ,constraint  PK_jdbc_comment  primary key(commentno) 
    ,constraint  FK_jdbc_comment_fk_boardno foreign key(fk_boardno) 
                 references jdbc_board(boardno) on delete cascade   -- 원글 삭제하면 댓글도 사라지므로 on delete cascadde 이다. 
    ,constraint  FK_jdbc_comment_fk_userid  foreign key(fk_userid) 
                 references jdbc_member(userid) 
    );
    -- Table JDBC_COMMENT이(가) 생성되었습니다.

    
    create sequence seq_comment
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    -- Sequence SEQ_COMMENT이(가) 생성되었습니다.

    
    select *
    from jdbc_comment
    where fk_Boardno = 10;
    
    
    insert into jdbc_comment(commentno, fk_boardno, fk_userid, contents)
    values(1, '2', 'leess', '연습');
    -- jdbc_board 테이블의 원글이 존재하지 않으면 comment 가 insert 가 되지 않는다.    
    
    /*
    오류 보고 -
    ORA-02291: integrity constraint (HR.FK_JDBC_COMMENT_FK_BOARDNO) violated - parent key not found
    */
      
    select *
    from jdbc_board
    
    delete from jdbc_board
    where boardno = 13;
    
    commit;
    
    select contents, M.name, C.writeday
    from
    (select contents, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday, fk_userid
     from jdbc_comment
     where fk_boardno = 9 ) C JOIN jdbc_member M
    ON C.fk_userid = M.userid;
    
----------------------------------------------------------------------------------------
---- 글제목 다음에 딸린 댓글의 개수를 보여주고자 한다.

    select B.boardno, B.subject, M.name 
        , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss'), B.viewcount 
        from jdbc_board B JOIN jdbc_member M 
        ON B.fk_userid = M.userid 
        order by boardno desc;
    
    select fk_boardno, count(*) AS COMMENTCNT
    from jdbc_comment
    group by fk_boardno;         -- 원글 번호를 끼리끼리 그룹지었을 때


    select B.boardno, B.subject, M.name 
           , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss'), B.viewcount 
           , nvl(C.COMMENTCNT, 0)   -- null 이면 0 넣어라.
    from jdbc_board B JOIN jdbc_member M 
    ON B.fk_userid = M.userid 
    -- 왼쪽테이블 내용 다 보여줘야 하기 때문에 LEFT 조인
    LEFT JOIN ( select fk_boardno, count(*) AS COMMENTCNT   -- count(*) 딸린댓글갯수
                from jdbc_comment
                group by fk_boardno) C
    ON B.boardno = C.fk_boardno
    order by 1 desc;
    
    
    
    -- *** 최근1주일간 일자별 게시글 작성건수 *** --      
    select boardno, fk_userid, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss')
         , sysdate - writeday
         , to_date( to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(writeday, 'yyyy-mm-dd'), 'yyyy-mm-dd')  -- 시,분,초 없으면 항상 0시 0분 0초.(자정) // 날짜-날짜(문자-문자는 안되므로 바꿈)
    from jdbc_board
    order by boardno desc;            
    -- 어젯밤 11시와 아침 7시는 다르지만 똑같은 어제이므로 0시0분0초로 맞춰준다.

    select * 
    from jdbc_board
    where to_date( to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(writeday, 'yyyy-mm-dd'), 'yyyy-mm-dd') < 7   -- 7일 이내의 게시물만 보여라.!!



    --- *** 특정날짜를 입력받아서 그 날짜의 자정(0시 0분 0초)의 값을 반환시켜주는 함수를 생성해봅니다. *** ---
    create or replace function func_midnight
    (p_date IN date)
    return date
    is
    begin
         return to_date(to_char(p_date, 'yyyy-mm-dd'), 'yyyy-mm-dd');       -- 입력받은 p_date를 to_char 에 쓰면 시,분,초를 뺀다는 것(0시0분0초). 그것을 다시한번 문자에서 날짜타입으로 바꿔준다.
    end func_midnight;
    -- Function FUNC_MIDNIGHT이(가) 컴파일되었습니다.

    
    --- *** 생성된 함수의 원본소스를 조회해본다. *** ---
    select text
    from user_source
    where type = 'FUNCTION' and name = 'FUNC_MIDNIGHT';
    
    /*
    ----------------------------------------------------------------------------------------
    TOTAL   PREVIOUS6   PREVIOUS5   PREVIOUS4   PREVIOUS3   PREVIOUS2   PREVIOUS1   TODAY
    ----------------------------------------------------------------------------------------
      3         3         0             0           0           0           0         0   
   
    */
        
    select * 
    from jdbc_board
    where func_midnight(sysdate) - func_midnight(writeday) < 7;   -- 7일 이내의 게시물만 보여라.!!
           
    select writeday 
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 6, 1, 0 ) -- 첫번째 파라미터값이 6 이면 1을, 아니면 0 을 주겠다. (2.3 - 1/28 기준)
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 5, 1, 0 )
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 4, 1, 0 )
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 3, 1, 0 )
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 2, 1, 0 )
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 1, 1, 0 )
         , decode( func_midnight(sysdate) - func_midnight(writeday) , 0, 1, 0 ) -- 0은 TODAY (오늘-오늘)
    from jdbc_board
    where func_midnight(sysdate) - func_midnight(writeday) < 7;    
    

    select count(*) AS TOTAL
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 6, 1, 0 )) AS PREVIOUS6  -- 첫번째 파라미터값이 6 이면 1을 , 아니면 0을 주겠다. (6일전 값) // Alias 의 첫번째가 숫자면 반드시 ""를 붙여야 한다.
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 5, 1, 0 )) AS PREVIOUS5 
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 4, 1, 0 )) AS PREVIOUS4 
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 3, 1, 0 )) AS PREVIOUS3 
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 2, 1, 0 )) AS PREVIOUS2 
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 1, 1, 0 )) AS PREVIOUS1 
         , sum(decode( func_midnight(sysdate) - func_midnight(writeday) , 0, 1, 0 )) AS TODAY  -- 0은 TODAY (오늘-오늘)
    from jdbc_board
    where func_midnight(sysdate) - func_midnight(writeday) < 7;                    -- 그룹함수의 null 은 다 뺀다.
    
    
    -- *** 최근 2개월간 일자별 게시글 작성건수 *** --
    select *
    from jdbc_board;
    
    update jdbc_board set writeday = add_months(writeday, -1)   -- -1개월을 더해준다.
    where boardno = 9; 
    -- 1 행 이(가) 업데이트되었습니다.

    commit;    
    
    select * 
    from jdbc_board
    where to_char(writeday, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm') OR                  -- 일수는 필요 없고, 이번'달'(연,월) 것만 보면 된다.
          to_char(writeday, 'yyyy-mm') = to_char( add_months(sysdate, -1), 'yyyy-mm' );  -- 현재로부터 -1개월 을 더해준다. (2022 년 1월 3일이 될 것).


    select decode( Grouping( to_char(writeday, 'yyyy-mm-dd') ), 0, to_char(writeday, 'yyyy-mm-dd'), '전체') AS WRITEDAY   -- 0이면 날짜를 보이고, 아니라면 '전체'를 쓰겠다. ( -- grouping 시 0은 실제 데이터, 1은 데이터가 아니다.(= 그룹을 안지었다.)
         , count(*) AS CNT
    from jdbc_board
    where to_char(writeday, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm') OR                  
          to_char(writeday, 'yyyy-mm') = to_char( add_months(sysdate, -1), 'yyyy-mm' )
    group by ROLLUP (to_char(writeday, 'yyyy-mm-dd'))    -- group 은 일수까지 포함해서 묶어줘야 한다.  
    -- null 대신에 '전체'라는 단어를 쓰겠다.
  
  
    -- 글 삭제하기 
    select *
    from jdbc_board;

    -- 경우의수 ①
    delete from jdbc_board       
    where boardno = 131234;
    -- 0개 행 이(가) 삭제되었습니다. ( 존재하지 않는 글번호를 삭제하려는 경우 )

    -- 경우의수 ②
    delete from jdbc_board       
    where boardno = 'ㄴㄹㄶㄴㅀㄹ';
    -- ORA-01722: invalid number (오류 : 삭제하려는 글 번호에 숫자를 넣어야 하는데 문자를 입력한 경우)

    -- 경우의수 ③
    delete from jdbc_board       
    where boardno = 9 and fk_userid = 'leess';
    -- 0 행 이(가) 삭제되었습니다. // 글쓴이와 글번호가 둘다 충족되어야 한다. (즉, 자기가 쓴 글만 삭제가능하다.)
    -- 존재하는 글번호지만 다른 사용자가 작성한 글을 삭제하려는 경우. (이혜리가 쓴 글을 이순신이 삭제할 수 없다.)

    -- 경우의수 ④
    delete from jdbc_board       
    where boardno = 9 and fk_userid = 'leehr' and boardpasswd = '6578';
    -- 0 행 이(가) 삭제되었습니다. 
    -- 존재하는 글번호이면서 자신이 작성한 글인데 글암호가 틀린 경우에 삭제하려고 할 때.

    -- 경우의 수 ⑤ ★ 모든 조건 충족 ★
    delete from jdbc_board       
    where boardno = 9 and fk_userid = 'leehr' and boardpasswd = '1234';
    -- 1 행 이(가) 삭제되었습니다. 
    -- 존재하는 글번호 & 자신이 작성한 글 & 글암호가 올바른 경우에 삭제하려고 할 때.
    -- ▶ 모든 조건 만족.!! 이 때 1이 결과값으로 나온다.
    
    rollback;
    -- 롤백 완료.

    select *
    from jdbc_comment;



    