<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<jsp:include page="header.jsp" />

     	<!-- 상단 컨텐츠 시작 -->
		<div class="row custom-topcontents">
			<div class="col-md-3">	<!-- 1/4 (12분할 기준) -->
			<h2>Board</h2>
	         <p>회원 또는 비회원 게시물을 확인할 수 있고 질문과 답변 코너를 이용한 다양한 질문에 대한 관리자의 상세
               답변을 확인할 수 있습니다.</p>
			</div>
			
			<div class="col-md-9">	<!-- 3/4 (12분할 기준) -->
   				<img src="./images/Penguins.jpg" class="img-fluid" alt="반응형 이미지">
			</div>
		</div>
      	<!-- 상단 컨텐츠 끝 -->


      	<!-- 중앙 컨텐츠 시작 -->
		<div class="row" style=" margin: 20px 0 50px 0;">
			<div class="col-md-3">
				<ul class="nav flex-column">
		          <li class="nav-item">
		            <a class="nav-link" href="#">회원전용</a>
		          </li>
		          <li class="nav-item">
		            <a class="nav-link" href="#">비회원전용</a>
		          </li>
		          <li class="nav-item">
		            <a class="nav-link" href="#">질문과 답변</a>
		          </li>
		        </ul>
			</div>
			<div class="col-md-9">
				<div class="table-responsive">
				<!-- .table-responsive 반응형 테이블(테이블의 원래 크기를 보존해주기 위한 것으로써, 디바이스의 width가 작아지면 테이블 하단에 스크롤이 생김) -->
					  <table class="table table-striped" >
					  <thead>
					    <tr>
					      <th>#</th>
					      <th>First Name</th>
					      <th>Last Name</th>
					      <th>Username</th>    		      
					    </tr>
					  </thead>
					  <tbody>
					    <tr>
					      <th>1</th>
					      <td>Mark</td>
					      <td>Otto</td>
					      <td>@mdo</td>
					    </tr>
					    <tr>
					      <th>2</th>
					      <td>Jacob</td>
					      <td>Thornton</td>
					      <td>@fat</td>
					    </tr>
					    <tr>
					      <th>3</th>
					      <td>Larry</td>
					      <td>the Bird</td>
					      <td>@twitter</td>
					    </tr>		    
					    <tr>
					      <th>4</th>
					      <td>Mark</td>
					      <td>Otto</td>
					      <td>@mdo</td>
					    </tr>
					    <tr>
					      <th>5</th>
					      <td>Jacob</td>
					      <td>Thornton</td>
					      <td>@fat</td>
					    </tr>
					    <tr>
					      <th>6</th>
					      <td>Larry</td>
					      <td>the Bird</td>
					      <td>@twitter</td>
					    <tr>
					      <th>7</th>
					      <td>Mark</td>
					      <td>Otto</td>
					      <td>@mdo</td>
					    </tr>
					    <tr>
					      <th>8</th>
					      <td>Jacob</td>
					      <td>Thornton</td>
					      <td>@fat</td>
					    </tr>
					    <tr>
					      <th>9</th>
					      <td>Larry</td>
					      <td>the Bird</td>
					      <td>@twitter</td>
					    </tr>		    					    
					  </tbody>
					</table>
				</div>
				
				<!-- 페이지네이션 가운데 정렬 -->
				<nav>
				  <ul class="pagination justify-content-center">
				    <li class="page-item"><a class="page-link" href="#">Previous</a></li>
				    <li class="page-item"><a class="page-link" href="#">1</a></li>
				    <li class="page-item"><a class="page-link" href="#">2</a></li>
				    <li class="page-item"><a class="page-link" href="#">3</a></li>
				    <li class="page-item"><a class="page-link" href="#">Next</a>
				  </ul>
				</nav>
				
			</div>
		</div>		
      	<!-- 중앙 컨텐츠 끝 -->

		<hr>
		
<jsp:include page="footer.jsp" />		