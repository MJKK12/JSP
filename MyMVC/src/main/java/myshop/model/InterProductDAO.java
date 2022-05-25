package myshop.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import member.model.MemberVO;

public interface InterProductDAO {

	// 메인페이지에 보여주는 상품 이미지 파일명을 모두 조회(select)하는 메소드
	// return 타입 필요 
	
	// DTO(Data Transfer Object) == VO(Value Object)
	List<ImageVO> imageSelectAll() throws SQLException;
	
	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	// VO 를 사용하지 않고 Map 으로 처리해보겠습니다.
	List<HashMap<String, String>> getCategoryList() throws SQLException;

	// Ajax(JSON)를 사용하여 상품목록을 "더보기" 방식으로 페이징처리 해주기 위해 스펙별로 제품의 전체개수 알아오기 //	
	int totalPspecCount(String string) throws SQLException;

	// SpecName 에 따라서 갯수를 알아온다.
	// Ajax(JSON)를 사용하여 상품목록을 "더보기" 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기 //	
	List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException;

    // 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select)해오기 
	List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException;

    //  페이지바를 만들기 위해서 특정카테고리의 제품개수에 대한 총페이지수 알아오기(select)  
	int getTotalPage(String cnum) throws SQLException;
	
	// spec 목록을 보여주고자 한다.
	List<SpecVO> selectSpecList() throws SQLException;

	// 제품번호 채번 해오기
	int getPnumOfProduct() throws SQLException;

	// tbl_product 테이블에 제품정보 insert 하기 (추가 제품 이미지가 3개라면 3번 insert. 즉 n개 일때 n번 insert)	
	int productInsert(ProductVO pvo) throws SQLException;

	// 이번에는 product_imagefile 테이블에 insert 하기( VO 대신에 HashMap 을 사용하여 insert 하겠다.)
	int product_imagefile_Insert(Map<String, String> paraMap) throws SQLException;

	// 제품번호(pnum) 을 가지고서 해당 제품의 정보를 조회하기 
	ProductVO selectOneProductByPnum(String pnum) throws SQLException;

	// 제품번호(pnum)을 가지고서 해당 제품의 추가된 이미지 정보를 조회하기 (1개 대표이미지 --> 에 딸린 나머지 n개의 이미지가 있다.)
	List<String> getImagesByPnum(String pnum) throws SQLException;
	
	// 제품번호(pnum)를 가지고서 해당 제품의 제품설명서 첨부파일의 서버에 업로드 된 첨부파일명 및 오리지널 파일명 조회해오기 (DB에 업로드 된 것을 알아오자.)
	Map<String, String> getPrdmanualFileName(String pnum) throws SQLException;

	// === 장바구니 담기 === //
	// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고,
	// 장바구니 테이블에 해당 제품이 이미 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다면 tbl_cart 테이블에 update 를 해야한다.
	int addCart(Map<String, String> paraMap) throws SQLException;

	// 로그인한 사용자의 장바구니 목록을 조회하기
	List<CartVO> selectProductCart(String userid) throws SQLException;

	// 장바구니 테이블에서 특정 제품을 제거하기
	int delCart(String cartno) throws SQLException;

	// 장바구니 테이블에서 특정 제품의 주문량을 변경하기
	int updateCart(Map<String, String> paraMap) throws SQLException;

	// 로그인한 사용자의 장바구니에 담긴 주문 총액 합계 및 총 포인트 합계 알아오기
	Map<String, String> selectCartSumPricePoint(String userid) throws SQLException;

	// 주문번호(시퀀스 seq_tbl_order 값)을 채번해오는 것
	int getSeq_tbl_order() throws SQLException;

	
	// ===== Transaction 처리하기 ===== // 
    // 1. 주문 테이블에 입력되어야할 주문전표를 채번(select)하기 
    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
    
    // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete OR update)하기(수동커밋처리) 
    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 

    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
    // 8. **** SQL 장애 발생시 rollback 하기(rollback) ****  
	int orderAdd(Map<String, Object> paraMap) throws SQLException;
	
	// 주문한 제품에 대해 email 보내기 시, email 내용에 넣을 주문한 제품번호들에 대한 제품정보를 얻어오는 것.
	List<ProductVO> getJumunProductList(String pnums) throws SQLException;
    
    // *** 주문내역에 대한 페이징 처리를 위해 주문 갯수를 알아오기 위한 것으로
    //     관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 갯수만 알아오고,
    //     관리자로 로그인을 했을 경우에는 모든 사용자들이 주문한 갯수를 알아온다.
	int getTotalCountOrder(String userid) throws SQLException;

    // *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고,
    //     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.	
	List<Map<String, String>> getOrderList(String userid, int currentShowPageNo, int sizePerPage) throws SQLException;

	// Ajax 를 이용한 제품후기를 작성하기전 해당 제품을 사용자가 실제 구매했는지 여부를 알아오는 것임. 구매했다라면 true, 구매하지 않았다면 false 를 리턴함.
	boolean isOrder(Map<String, String> paraMap) throws SQLException;

	// 특정 회원이 특정 제품에 대해 좋아요에 투표하기(insert) 
	int likeAdd(Map<String, String> paraMap) throws SQLException;

	// 특정 회원이 특정 제품에 대해 싫어요에 투표하기(insert) 
	int dislikeAdd(Map<String, String> paraMap) throws SQLException;

	// 특정 제품에 대한 좋아요,싫어요의 투표결과(select)	
	Map<String, Integer> getLikeDislikeCnt(String pnum) throws SQLException;

	// Ajax 를 이용한 특정 제품의 상품후기를 입력(insert)하기 
	int addComment(PurchaseReviewsVO reviewsVO) throws SQLException;

	// Ajax 를 이용한 특정 제품의 상품후기를 조회(select)하기	
	List<PurchaseReviewsVO> commentList(String fk_pnum) throws SQLException;

	// Ajax 를 이용한 특정 제품의 상품후기를 삭제(delete)하기	
	int reviewDel(String review_seq) throws SQLException;

	// Ajax 를 이용한 특정 제품의 상품후기를 수정(update)하기	
	int reviewUpdate(Map<String, String> paraMap) throws SQLException;

	// 영수증전표(odrcode)소유주에 대한 사용자 정보를 조회해오는 것.
	MemberVO odrcodeOwnerMemberInfo(String odrcode) throws SQLException;
	
	// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 2(배송시작)로 변경하기
	int updateDeliverStart(String odrcodePnum) throws SQLException;

	// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 3(배송완료)로 변경하기
	int updateDeliverEnd(String odrcodePnum) throws SQLException;

	// tbl_map(위,경도) 테이블에 있는 정보 가져오기 (select)
	List<Map<String, String>> selectStoreMap() throws SQLException;

	
}
