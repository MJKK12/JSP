<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="header.jsp" />

     	<!-- 상단 컨텐츠 시작 -->
		<div class="row custom-topcontents">
			<div class="col-md-3">	<!-- 1/4 (12분할 기준) -->
			<h2>로그인</h2>
	         <p>회원 로그인 화면입니다. 회원 아이디와 비밀번호를 입력하고 로그인하세요.</p>
			</div>
			
			<div class="col-md-9">	<!-- 3/4 (12분할 기준) -->
   				<img src="./images/Koala.jpg" class="img-fluid" alt="반응형 이미지">
			</div>
		</div>
      	<!-- 상단 컨텐츠 끝 -->


      	<!-- 중앙 컨텐츠 시작 -->
		<div class="row" style=" margin: 20px 0 50px 0;">
			<div class="col-md-3">
				<ul class="nav flex-column">
		          <li class="nav-item">
		            <a class="nav-link" href="#">회원가입</a>
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
			<!-- 경고 메시지 시작 -->
			<div class="alert alert-warning alert-dismissible fade show" role="alert">
			  공용으로 사용하는 PC에서는 &quot;비밀번호 기억하기&quot;를 체크하면 개인정보 유출의 위험이있습니다.
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
			    <span aria-hidden="true">&times;</span>
			  </button>
			</div>
			<!-- 경고 메시지 끝  -->
		
			
			<!-- 로그인 폼 시작 -->
			<form action="login.do" method="post">
	            <fieldset>
	                <legend>회원 로그인</legend>
	                <div class="row">
	                  <div class="col-md-9">
	                     <div class="form-group row">
	                        <div class="col-md-3">
	                           <label for="email">아이디</label>
	                        </div>
	                        <div class="col-md-9">
	                           <input class="form-control" type="email" id="email" name="email" placeholder="아이디" autocomplete="off" required /> 
	                        </div>
	                     </div>
	                     <div class="form-group row">
	                        <div class="col-md-3">
	                           <label for="pwd">비밀번호</label>
	                        </div>
	                        <div class="col-md-9">
	                           <input class="form-control" type="password" id="pwd" name="pwd" placeholder="비밀번호" required />
	                        </div>
	                     </div>
	                     <div class="form-group">
	                        <label for="user_remember_me">비밀번호 기억하기</label>
	                        <input type="checkbox" id="user_remember_me" name="remember" value="1" />
	                     </div>
	                  </div>
	                  <div class="col-md-3">
	                     <input class="btn btn-success" type="submit" value="로그인" style="height: 85px;" />
	                  </div>
	               </div>
	            </fieldset>
	         </form>
			<!-- 로그인 폼 끝 -->
			
			</div>
		</div>		
      	<!-- 중앙 컨텐츠 끝 -->

		<hr>
		
<jsp:include page="footer.jsp" />