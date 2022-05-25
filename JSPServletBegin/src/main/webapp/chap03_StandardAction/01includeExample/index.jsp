<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="header.jsp" />

     	<!-- 상단 컨텐츠 시작 -->
		<div class="row custom-topcontents">
			<div class="col-md-3">	<!-- 1/4 (12분할 기준) -->
			<h2>Getting started</h2>
	         <p>An overview of Bootstrap, how to download and use, basic
	            templates and examples, and more.</p>
	         <p>
	            <a class="btn btn-primary btn-lg">Learn more &raquo;</a>
	         </p>
			</div>
			
			<div class="col-md-9">	<!-- 3/4 (12분할 기준) -->
			<!-- Carousel 시작 -->
				<div id="carouselExampleIndicators" class="carousel slide carousel-fade" data-ride="carousel">
				  <ol class="carousel-indicators">
				    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
				    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
				    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
				  </ol>
				  <div class="carousel-inner">
				    <div class="carousel-item active">
				      <img src="./images/Koala.jpg" class="d-block w-100" alt="..."> <!-- d-block 은 display: block; 이고  w-100 은 width 의 크기는 <div class="carousel-item active">의 width 100% 로 잡으라는 것이다. -->
				      <div class="carousel-caption d-none d-md-block"> <!-- d-none 은 display : none; 이므로 화면에 보이지 않다가, d-md-block 이므로 d-md-block 은 width 가 768px이상인 것에서만 display: block; 으로 보여라는 말이다.  --> 
					    <h5>Koala</h5>
					    <p>Koala Content</p>
					  </div>
				    </div>
				    <div class="carousel-item">
				      <img src="./images/Lighthouse.jpg" class="d-block w-100" alt="...">
				      <div class="carousel-caption d-none d-md-block">
					    <h5>Lighthouse</h5>
					    <p>Lighthouse Content</p>
					  </div>		      
				    </div>
				    <div class="carousel-item">
				      <img src="./images/Penguins.jpg" class="d-block w-100" alt="...">
				      <div class="carousel-caption d-none d-md-block">
					    <h5>Penguins</h5>
					    <p>Penguins Content</p>
					  </div>		      
				    </div>
				  </div>
				  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
				    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
				    <span class="sr-only">Previous</span>
				  </a>
				  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
				    <span class="carousel-control-next-icon" aria-hidden="true"></span>
				    <span class="sr-only">Next</span>
				  </a>
				</div>
		     <!-- Carousel 끝 -->		   
			</div>
		</div>
      	<!-- 상단 컨텐츠 끝 -->


      	<!-- 중앙 컨텐츠 시작 -->
		<div class="row" style="margin-top: 20px;">
			<div class="col-lg-8">
				<div class="row">
					<div class="col-sm-6">	<!-- 6/12(1/2 분할) -->
						<h2>Heading</h2>
						<p>Donec id elit non mi porta gravida at eget metus. Fusce
						   dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh,
						   ut fermentum massa justo sit amet risus. Etiam porta sem malesuada
						   magna mollis euismod. Donec sed odio dui.</p>
						<p>
						   <a class="btn btn-success" href="#" role="button">View details &raquo;</a>
						</p>
					</div>
					
					<div class="col-sm-6">
						<h2>Heading</h2>
						<p>Donec id elit non mi porta gravida at eget metus. Fusce
						   dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh,
						   ut fermentum massa justo sit amet risus. Etiam porta sem malesuada
						   magna mollis euismod. Donec sed odio dui.</p>
						<p>
						   <a class="btn btn-success" href="#" role="button">View details &raquo;</a>
						</p>
					</div>
				</div>
			</div>	
			<div class="col-lg-4">
				<ul class="list-group" style="padding-top: 5px;">
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A first list item
				    <span class="badge badge-primary badge-pill">14</span>
				  </li>
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A second list item
				    <span class="badge badge-primary badge-pill">10</span>
				  </li>
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A third list item
				    <span class="badge badge-primary badge-pill">7</span>
				  </li>
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A fourth list item
				    <span class="badge badge-primary badge-pill">5</span>
				  </li>
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A fifth list item
				    <span class="badge badge-primary badge-pill">15</span>
				  </li>
				  <li class="list-group-item d-flex justify-content-between align-items-center">
				    A sixth list item
				    <span class="badge badge-primary badge-pill">1</span>
				  </li>
				</ul>
			</div>
						
		</div>		
      	<!-- 중앙 컨텐츠 끝 -->

		<!-- 썸네일 시작(이미지에 보더가 생기는 것이다.) -->
		<div class="row" style="margin: 15px 0 30px 0;">
			<div class="col-sm-6 col-lg-3 mb-3">  <!-- small 일 경우 반반, large 일 경우 1/4 -->
				<div>
		            <img src="images/Koala.jpg" alt="thumbnail" class="img-thumbnail" width="260">
		            <div>
		               <h3>Thumbnail label</h3>
		               <p>Cras justo odio, dapibus ac facilisis in, egestas eget
		                  quam. Donec id elit non mi porta gravida at eget metus. Nullam id
		                  dolor id nibh ultricies vehicula ut id elit.
		               </p>
		            </div>
	         	</div>
			</div>
			<div class="col-sm-6 col-lg-3 mb-3">  <!-- small 일 경우 반반, large 일 경우 1/4 -->
				<div>
		            <img src="images/Koala.jpg" alt="thumbnail" class="img-thumbnail" width="260">
		            <div>
		               <h3>Thumbnail label</h3>
		               <p>Cras justo odio, dapibus ac facilisis in, egestas eget
		                  quam. Donec id elit non mi porta gravida at eget metus. Nullam id
		                  dolor id nibh ultricies vehicula ut id elit.
		               </p>
		            </div>
	         	</div>
			</div>
			<div class="col-sm-6 col-lg-3 mb-3">  <!-- small 일 경우 반반, large 일 경우 1/4 -->
				<div>
		            <img src="images/Koala.jpg" alt="thumbnail" class="img-thumbnail" width="260">
		            <div>
		               <h3>Thumbnail label</h3>
		               <p>Cras justo odio, dapibus ac facilisis in, egestas eget
		                  quam. Donec id elit non mi porta gravida at eget metus. Nullam id
		                  dolor id nibh ultricies vehicula ut id elit.
		               </p>
		            </div>
	         	</div>
			</div>
			<div class="col-sm-6 col-lg-3 mb-3">  <!-- small 일 경우 반반, large 일 경우 1/4 -->
				<div>
		            <img src="images/Koala.jpg" alt="thumbnail" class="img-thumbnail" width="260">
		            <div>
		               <h3>Thumbnail label</h3>
		               <p>Cras justo odio, dapibus ac facilisis in, egestas eget
		                  quam. Donec id elit non mi porta gravida at eget metus. Nullam id
		                  dolor id nibh ultricies vehicula ut id elit.
		               </p>
		            </div>
	         	</div>
			</div>						
		</div>
		<!-- 썸네일 끝(이미지에 보더가 생기는 것이다.) -->

		<hr>
		
<jsp:include page="footer.jsp" />
