<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="header.jsp" />	

     	<!-- 상단 컨텐츠 시작 -->
		<div class="row custom-topcontents">
			<div class="col-md-3">	<!-- 1/4 (12분할 기준) -->
			<h2>회원가입</h2>
	         <p>회원 가입 화면입니다. 회원 가입하시면 회원만의 특화된 서비스를 이용하실 수 있습니다.</p>
			</div>
			
			<div class="col-md-9">	<!-- 3/4 (12분할 기준) -->
   				<img src="./images/Lighthouse.jpg" class="img-fluid" alt="반응형 이미지">
			</div>
		</div>
      	<!-- 상단 컨텐츠 끝 -->


      	<!-- 중앙 컨텐츠 시작 -->
		<div class="row" style=" margin: 20px 0 50px 0;">
			<div class="col-md-3">
				<ul class="nav flex-column">
		          <li class="nav-item">
		            <a class="nav-link" href="#">로그인</a>
		          </li>
		          <li class="nav-item">
		            <a class="nav-link" href="#">아이디 찾기</a>
		          </li>
		          <li class="nav-item">
		            <a class="nav-link" href="#">비밀번호 찾기</a>
		          </li>
		        </ul>
			</div>
			<div class="col-md-9">
			
			<!-- 회원가입 폼 시작 -->
			<form action="register.do" method="post">
	            <fieldset>
				<legend>회원 가입</legend>
                  <div class="form-group">
                     <div class="row">
                        <div class="col-md-3">
                           <label for="email">아이디</label>
                        </div>
                        <div class="col-md-9">
                           <input class="form-control" type="email" id="email"   name="email" placeholder="이메일을 입력하세요" autocomplete="off" required /> 
                        </div>
                     </div>
                  </div>   
                  <div class="form-group">
                     <div class="row">   
                        <div class="col-md-3">
                           <label for="pwd">비밀번호</label>
                        </div>
                        <div class="col-md-9">
                           <input class="form-control" type="password" id="pwd" name="pwd" placeholder="6~12자 사이로 입력" required />
                        </div>
                     </div>   
                  </div>
                  <div class="form-group">
                     <div class="row">
                        <div class="col-md-3">
                           <label for="name">이름</label>
                        </div>
                        <div class="col-md-9">
                           <input class="form-control" type="text" id="name" name="name" autocomplete="off" required />
                        </div>
                     </div>   
                  </div>
                  <div class="form-group">
                      <div class="row">
                        <div class="col-md-3">
                           <label for="tel">휴대폰</label>
                        </div>
                        <div class="col-md-9">
                           <input class="form-control" type="tel" id="tel" name="tel" />
                        </div>
                     </div>   
                  </div>
                  <div class="form-group custom-submit">
                     <input class="btn btn-success" type="submit" value="전송">
                  </div>		
	            </fieldset>
	         </form>
			<!-- 회원가입 폼 끝 -->
			
			</div>
		</div>		
      	<!-- 중앙 컨텐츠 끝 -->

		<hr>
		
<jsp:include page="footer.jsp" />
