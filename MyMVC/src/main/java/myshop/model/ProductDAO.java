package myshop.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.model.MemberVO;
import util.security.AES256;
import util.security.SecretMyKey;

public class ProductDAO implements InterProductDAO {	
	
	// DB 에 가서 읽어와야 하기 때문에 Connection 을 한다. (DBCP 를 쓴다.)
	private DataSource ds;		// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private AES256 aes;
		
	// 생성자에서 코딩을 해주어야 한다.
	public ProductDAO() {
	    
		try {		
			Context initContext = new InitialContext();	// 1. 커넥션 풀에 접근하려면 JNDI 서비스를 사용
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");  
		    ds = (DataSource)envContext.lookup("jdbc/mymvc_oracle"); // 2. .lookup( )은 리소스를 찾은 후 리소스를 사용할 수 있도록 객체를 반환해주는 메소드
		    // "jdbc/myoracle" 는 web.xml 에 있는 <res-ref-name> 이다.
		    // 이는 context.xml에 있는 name에 해당한다. (오라클 DB와 연결)
		    
		    aes = new AES256(SecretMyKey.KEY);	// 객체 생성.
		    // SecretMyKey.KEY는 우리가 만든 비밀키이다.
		    
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	}
		
	// 자원 반납해주는 메소드
	private void close() {
		// 사용된 것을 닫아야(close) 한다. null이 아니라면, 자원반납
		try {
			if(rs != null) 		{ rs.close(); 		rs = null;	}
			if(pstmt != null) 	{ pstmt.close(); 	pstmt = null;	}
			if(conn != null) 	{ conn.close(); 	conn = null;	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}// end of private void close()-----------------------
	
	// 시작(메인)페이지에 보여주는 상품이미지파일명을 모두 조회(select) 하는 메소드
	@Override
	public List<ImageVO> imageSelectAll() throws SQLException {

		List<ImageVO> imgList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select imgno, imgfilename "+
						 " from tbl_main_image "+
						 " order by imgno asc ";
			
			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			while(rs.next()) {
				
				ImageVO imgvo = new ImageVO();
				imgvo.setImgno(rs.getInt(1));	// imgvo 에 넣는다.
				imgvo.setImgfilename(rs.getString(2));	// imgvo 에 넣는다.
				
				imgList.add(imgvo);		// 위에서 set 한 것을 imgList 에 add 한다.				
			}// end of while---------------------------------------
			
		} finally {
			close();
		}
		
		return imgList;
	}// public List<ImageVO> imageSelectAll()------------------

	
	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	// VO 를 사용하지 않고 Map 으로 처리해보겠습니다.
	@Override
	public List<HashMap<String, String>> getCategoryList() throws SQLException {

		List<HashMap<String, String>> categoryList = new ArrayList<>();
		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select cnum, code, cname "+
						 " from tbl_category "+
						 " order by cnum asc ";
			
			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			while(rs.next()) {				
				HashMap<String, String> map = new HashMap<>();				
				map.put("cnum", rs.getString(1));
				map.put("code", rs.getString(2));
				map.put("cname", rs.getString(3));
				
				categoryList.add(map);
				
			}// end of while---------------------------------------
			
		} finally {
			close();
		}
		
		return categoryList;
	}

	// Ajax(JSON)를 사용하여 상품목록을 "더보기" 방식으로 페이징처리 해주기 위해 스펙별로 제품의 전체개수 알아오기 //	
	@Override
	public int totalPspecCount(String fk_snum) throws SQLException {

		int totalCount = 0;
		
		try {
			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select count(*) "+
						 " from tbl_product "+
						 " where fk_snum = ? ";
			
			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.
			pstmt.setString(1, fk_snum);
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)

			rs.next();
			
			totalCount = rs.getInt(1);
			
		} finally {
			close();
			
		}
		
		return totalCount;
	}

	// Ajax(JSON)를 사용하여 상품목록을 "더보기" 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기 //		
	@Override
	public List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException {

		List<ProductVO> prodList = new ArrayList<>();
		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select pnum, pname, code, pcompany, pimage1, pimage2, pqty, price, saleprice, sname, pcontent, point, pinputdate "
					   + " from "
					   + " ( "
					   + "    select row_number() over(order by pnum desc) as RNO "
					   + "         , P.pnum, P.pname, C.code, P.pcompany, P.pimage1, P.pimage2, P.pqty, P.price, P.saleprice, S.sname, P.pcontent, P.point "
					   + "         , to_char(P.pinputdate, 'yyyy-mm-dd') as pinputdate "
					   + "    from tbl_product P "
					   + "    JOIN tbl_category C "
					   + "    ON P.fk_cnum = C.cnum "
					   + "    JOIN tbl_spec S "
					   + "    ON P.fk_snum = S.snum "
					   + "    where S.sname = ? "
					   + " ) V "
					   + " where V.RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.

			// 위치홀더에 값을 넣어준다.
			pstmt.setString(1, paraMap.get("sname"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			while(rs.next()) {				
				// select 되어진 것이 있다면 값을 담는다.
				 ProductVO pvo = new ProductVO();
				
				 pvo.setPnum(rs.getInt(1));     // 제품번호
				 pvo.setPname(rs.getString(2)); // 제품명
				 
				 CategoryVO categvo = new CategoryVO(); 	// 읽어온 code 는 categoryVO 에 있다.
				 categvo.setCode(rs.getString(3)); 
				 
				 pvo.setCategvo(categvo);           // 카테고리코드 (읽어온 것을 다시 pvo 에 넣은 것이다. 원래 productVO 에 없음. JOIN 때문에 넣은 것이다.)
				 pvo.setPcompany(rs.getString(4));  // 제조회사명
				 pvo.setPimage1(rs.getString(5));   // 제품이미지1   이미지파일명
				 pvo.setPimage2(rs.getString(6));   // 제품이미지2   이미지파일명
				 pvo.setPqty(rs.getInt(7));         // 제품 재고량
				 pvo.setPrice(rs.getInt(8));        // 제품 정가
				 pvo.setSaleprice(rs.getInt(9));    // 제품 판매가(할인해서 팔 것이므로)
				   
				 SpecVO spvo = new SpecVO(); 
				 spvo.setSname(rs.getString(10)); 
				 
				 pvo.setSpvo(spvo); // 스펙 (읽어온 것을 다시 pvo 에 넣은 것이다. 원래 productVO 에 없음. JOIN 때문에 넣은 것이다.)
				   
				 pvo.setPcontent(rs.getString(11));   // 제품설명 
				 pvo.setPoint(rs.getInt(12));         // 포인트 점수        
				 pvo.setPinputdate(rs.getString(13)); // 제품입고일자
				 
				 prodList.add(pvo);	// List 에 담아주자.				
			}// end of while---------------------------------------
			
		} finally {
			close();
		}
		
		return prodList;
	}


    // 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select)해오기 
	@Override
	public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException {

		List<ProductVO> prodList = new ArrayList<>();
	      
	      try {
	          conn = ds.getConnection();
	          
	          String sql = "select cname, sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+
	                "from "+
	                "( "+
	                "    select rownum AS RNO, cname, sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+ 
	                "    from "+
	                "    ( "+
	                "        select C.cname, S.sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+
	                "        from "+
	                "            (select pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point "+
	                "                  , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate, fk_cnum, fk_snum  "+
	                "             from tbl_product  "+
	                "             where fk_cnum = ? "+
	                "             order by pnum desc "+
	                "        ) P "+
	                "        JOIN tbl_category C "+
	                "        ON P.fk_cnum = C.cnum "+
	                "        JOIN tbl_spec S "+
	                "        ON P.fk_snum = S.snum "+
	                "    ) V "+
	                ") T "+
	                "where T.RNO between ? and ? ";
	          
	          pstmt = conn.prepareStatement(sql);
	          
	          int currentShowPageNo = Integer.parseInt( paraMap.get("currentShowPageNo") );
	          int sizePerPage = 10; // 한 페이지당 화면상에 보여줄 제품의 개수는 10 으로 한다.
	          
	          pstmt.setString(1, paraMap.get("cnum"));
	          pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식
	          pstmt.setInt(3, (currentShowPageNo * sizePerPage)); // 공식 
	          
	          rs = pstmt.executeQuery();
	          
	          while( rs.next() ) {
	             
	             ProductVO pvo = new ProductVO();
	             
	             pvo.setPnum(rs.getInt("pnum"));      // 제품번호
	             pvo.setPname(rs.getString("pname")); // 제품명
	             
	             CategoryVO categvo = new CategoryVO(); 
	             categvo.setCname(rs.getString("cname"));  // 카테고리명  
	             
	             pvo.setCategvo(categvo);                   // 카테고리 
	             pvo.setPcompany(rs.getString("pcompany")); // 제조회사명
	             pvo.setPimage1(rs.getString("pimage1"));   // 제품이미지1   이미지파일명
	             pvo.setPimage2(rs.getString("pimage2"));   // 제품이미지2   이미지파일명
	             pvo.setPqty(rs.getInt("pqty"));            // 제품 재고량
	             pvo.setPrice(rs.getInt("price"));          // 제품 정가
	             pvo.setSaleprice(rs.getInt("saleprice"));  // 제품 판매가(할인해서 팔 것이므로)
	               
	             SpecVO spvo = new SpecVO(); 
	             spvo.setSname(rs.getString("sname")); // 스펙이름 
	             
	             pvo.setSpvo(spvo); // 스펙 
	               
	             pvo.setPcontent(rs.getString("pcontent"));       // 제품설명 
	             pvo.setPoint(rs.getInt("point"));              // 포인트 점수        
	             pvo.setPinputdate(rs.getString("pinputdate")); // 제품입고일자                                             
	             
	             prodList.add(pvo);
	          }// end of while-----------------------------------------
	          
	      } finally {
	         close();
	      }      
	      
	      return prodList;
	}

	 
	//  페이지바를 만들기 위해서 특정카테고리의 제품개수에 대한 총페이지수 알아오기(select)  
	@Override
	public int getTotalPage(String cnum) throws SQLException {

		int totalPage = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select ceil( count(*)/10 ) "  // 10 이 sizePerPage 이다. (count(*) 는 총 제품 갯수)
	                  + " from tbl_product "
	                  + " where fk_cnum = ? "; 
	         
	         pstmt = conn.prepareStatement(sql);
	         
	         pstmt.setString(1, cnum);
	               
	         rs = pstmt.executeQuery();
	         
	         rs.next();
	         
	         totalPage = rs.getInt(1);
	         
	      } finally {
	         close();
	      }      
	      
	      return totalPage;

	}
	
	
	// spec 목록을 보여주고자 한다.	
	@Override
	public List<SpecVO> selectSpecList() throws SQLException  {

		List<SpecVO> specList = new ArrayList<>();
		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select snum, sname "+
						 " from tbl_spec "+
						 " order by snum asc ";

			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			while(rs.next()) {	// 복수개
				SpecVO spvo = new SpecVO();				
				spvo.setSnum(rs.getInt(1));
				spvo.setSname(rs.getString(2));
				
				specList.add(spvo);
				
			}// end of while---------------------------------------
			
		} finally {
			close();
		}
		
		return specList;
	}

	
	// 제품번호 채번 해오기
	@Override
	public int getPnumOfProduct() throws SQLException {

		int pnum = 0;
		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			String sql = " select seq_tbl_product_pnum.nextval"
					   + " from dual ";

			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.
			
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			rs.next();
			pnum = rs.getInt(1);	// select(읽어온 것) 결과를 pnum에 넣어준다.
			
		} finally {
			close();
		}
		
		return pnum;
	}


	// tbl_product 테이블에 제품정보 insert 하기 (추가 제품 이미지가 3개라면 3번 insert. 즉 n개 일때 n번 insert)	
	@Override
	public int productInsert(ProductVO pvo) throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " insert into tbl_product(pnum, pname, fk_cnum, pcompany, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName, pqty, price, saleprice, fk_snum, pcontent, point) " 
					   + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, pvo.getPnum());		// 제품번호 채번해오기.
			pstmt.setString(2, pvo.getPname());
	        pstmt.setInt(3, pvo.getFk_cnum());    
	        pstmt.setString(4, pvo.getPcompany()); 
	        pstmt.setString(5, pvo.getPimage1());    
	        pstmt.setString(6, pvo.getPimage2()); 
	        pstmt.setString(7, pvo.getPrdmanual_systemFileName());
	        pstmt.setString(8, pvo.getPrdmanual_orginFileName());
	        pstmt.setInt(9, pvo.getPqty()); 
	        pstmt.setInt(10, pvo.getPrice());
	        pstmt.setInt(11, pvo.getSaleprice());
	        pstmt.setInt(12, pvo.getFk_snum());
	        pstmt.setString(13, pvo.getPcontent());
	        pstmt.setInt(14, pvo.getPoint());		
	        
	        result = pstmt.executeUpdate();
	        
		} finally {
			close();
		}
		
		return result;		
	}


	// 이번에는 product_imagefile 테이블에 insert 하기( VO 대신에 HashMap 을 사용하여 insert 하겠다.)
	@Override
	public int product_imagefile_Insert(Map<String, String> paraMap) throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " insert into tbl_product_imagefile(imgfileno, fk_pnum, imgfilename) "
					   + " values(seqImgfileno.nextval, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			// Map 에 들어와있다.
			pstmt.setInt(1, Integer.parseInt(paraMap.get("pnum")));
			pstmt.setString(2, paraMap.get("attachFileName"));
	        
	        result = pstmt.executeUpdate();
	        
		} finally {
			close();
		}
		
		return result;				
	}

	// 제품번호(pnum) 을 가지고서 해당 제품의 정보를 조회하기 
	@Override
	public ProductVO selectOneProductByPnum(String pnum) throws SQLException {

		ProductVO pvo = null;
		
		// nvl(prdmanual_orginFileName, '없음') -->  prdmanual_orginFileName 가 null 값이면 '없음'
		try {
			
			conn = ds.getConnection();
			// 한 제품에 대한 상세정보를 spec 번호와 함께 조회하기 위해 JOIN 을 사용했다			
			String sql = " select S.sname, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, nvl(prdmanual_orginFileName, '없음') AS prdmanual_orginFileName "+
						 " from "+
						 " ( "+
						 " select fk_snum, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName "+
						 " from tbl_product "+
						 " where pnum = ? "+
						 " ) P JOIN tbl_spec S "+
						 " ON P.fk_snum = S.snum ";			
						
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, pnum);	// 문자열 호환 가능.
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// select 된 것이 있다면,
				
				 String sname = rs.getString(1);     // "HIT", "NEW", "BEST" 값을 가짐 
	             int    npnum = rs.getInt(2);        // 제품번호
	             String pname = rs.getString(3);     // 제품명
	             String pcompany = rs.getString(4);  // 제조회사명
	             int    price = rs.getInt(5);        // 제품 정가
	             int    saleprice = rs.getInt(6);    // 제품 판매가
	             int    point = rs.getInt(7);        // 포인트 점수
	             int    pqty = rs.getInt(8);         // 제품 재고량
	             String pcontent = rs.getString(9);  // 제품설명
	             String pimage1 = rs.getString(10);  // 제품이미지1
	             String pimage2 = rs.getString(11);  // 제품이미지2
	             String prdmanual_systemFileName = rs.getString(12); // 파일서버에 업로드되어지는 실제 제품설명서 파일명
	             String prdmanual_orginFileName = rs.getString(13);  // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명				

	             // 조회한 것(select)을 pvo 에 넣자. (select 된 것들), default 가 null 인데, pvo 를 해온다.	             
	             pvo = new ProductVO();
	             
	             // sname 이 없기 때문에 (spec 테이블에서 가져옴(나머지는 다 product 테이블) --> ProductVO에 specVO 가 들어와 있음. --> 하나 만들자.)
	             SpecVO spvo = new SpecVO();
	             spvo.setSname(sname);	// spec vo 에서 가져온 것을 다시 pvo에 넣자. 
	             
	             pvo.setSpvo(spvo);	// spvo 에 sname이 들어있고, spvo 를 다시 pvo 에 넣어준다.
	             pvo.setPnum(npnum);
	             pvo.setPname(pname);
	             pvo.setPcompany(pcompany);
	             pvo.setPrice(price);
	             pvo.setSaleprice(saleprice);
	             pvo.setPoint(point);
	             pvo.setPqty(pqty);
	             pvo.setPcontent(pcontent);
	             pvo.setPimage1(pimage1);
	             pvo.setPimage2(pimage2);
	             pvo.setPrdmanual_systemFileName(prdmanual_systemFileName);
	             pvo.setPrdmanual_orginFileName(prdmanual_orginFileName);
	             
	             
			} // end of if (rs.next())----------------------------------------
			
		} finally {
			close();
		}
		
		return pvo;		// select 된 것이 없을 때 null 값이 넘어간다.
	}

	// 제품번호(pnum)을 가지고서 해당 제품의 추가된 이미지 정보를 조회하기 (1개 대표이미지 --> 에 딸린 나머지 n개의 이미지가 있다.)
	@Override
	public List<String> getImagesByPnum(String pnum) throws SQLException {

		List<String> imgList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select imgfilename "+
						 " from tbl_product_imagefile "+
						 " where fk_pnum = ? ";

			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, pnum);	// 문자열 호환 가능.
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				String imagefilename = rs.getString(1);	// 추가된 이미지파일명			
				imgList.add(imagefilename);	// imgList 에 담는다.
			}// end of while--------------------------------
			
			
		} finally {
			close();
		}
		
		return imgList;	// 추가이미지가 없으면 size 가 0인 값만 넘어가게 된다.
	}
	
	
	// 제품번호(pnum)를 가지고서 해당 제품의 제품설명서 첨부파일의 서버에 업로드 된 첨부파일명 및 오리지널 파일명 조회해오기 (DB에 업로드 된 것을 알아오자.)
	@Override
	public Map<String, String> getPrdmanualFileName(String pnum) throws SQLException {

		Map<String, String> map = new HashMap<>();
		
		try {
			
			conn = ds.getConnection();
			

			String sql = " select prdmanual_systemFileName, prdmanual_orginFileName "+
						 " from tbl_product "+
						 " where pnum = ? ";

			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, pnum);	// 문자열 호환 가능.
			
			rs = pstmt.executeQuery();
			
			// ※prdmanual_systemFileName : 업로드용(중복 파일명 방지) / prdmanual_orginFileName : 첨부파일 다운받는용 (사용자가 올린 그대로 파일명 받기)
			if(rs.next()) {			// pnum 은 고유한 1개 값이므로 if 문	
				map.put("prdmanual_systemFileName", rs.getString(1));	// map 에 put 하자 (select 된 결과물들의 값)
				// 파일서버에 업로드 되는 실제 제품설명서 파일명
				
				map.put("prdmanual_orginFileName", rs.getString(2));				
				// 웹클라이언트의 웹브라우저에서 파일을 업로드 할 때 올리는 제품설명서 파일명
			}// end of if--------------------------------
			
			
		} finally {
			close();
		}		
		
		return map;
	}

	
	// === 장바구니 담기 === //
	// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고,
	// 장바구니 테이블에 해당 제품이 이미 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다면 tbl_cart 테이블에 update 를 해야한다.
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {

		int n = 0;
		
		// 해당 제품이 장바구니에 존재 하는지 아닌지에 따라(select 문으로 검사) insert/update 여부를 결정한다.
		try {
			
			conn = ds.getConnection();
			
			/*
	            먼저 장바구니 테이블(tbl_cart)에 어떤 회원이 새로운 제품을 넣는 것인지,
	            아니면 또 다시 제품을 추가로 더 구매하는 것인지를 알아야 한다.
	            이것을 알기위해서 어떤 회원이 어떤 제품을  장바구니 테이블(tbl_cart) 넣을때
	            그 제품이 이미 존재하는지 select 를 통해서 알아와야 한다.
	            ex) 7번 제품 은 아래에서 2개, 6번 제품은 3개를 담음. 현재 로그인 한 사람!! 이 fk_pnum 제품이 있는지 알아봐야 한다.
	            
	            
	          -------------------------------------------
	           cartno   fk_userid     fk_pnum   oqty  
	          -------------------------------------------
	             1      kimmj          7         2     
	             2      kimmj          6         3     
	             3      kimmj2         7         5     
			*/
			
			String sql = " select cartno "
					   + " from tbl_cart"
					   + " where fk_userid = ? "		// 주문한 사람은 누구이고
					   + " and fk_pnum = ? ";			// 제품 번호는 무엇인지?
			
			// sql 문 실행을 통해 아래의 userid,pnum 이 존재하는지 조회한다(select)
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));	// Action 단에서 paraMap.put 으로 넘긴것
			pstmt.setString(2, paraMap.get("pnum"));	// select 는 호환되기 때문에 굳이 Int 로 할 필요가 없다.
			
			rs = pstmt.executeQuery();	
			
			// select 된 결과물이 있는지 알아보자.
			if(rs.next()) {
				// 어떤 제품을 추가로 장바구니에 넣고자 하는 경우 (즉, 그 어떤 제품이 이미 장바구니에 담겨있었던 것이다. --> update문)
				// 주문 수량만 바꾸는 것이다. (이미 같은 제품을 3개 담아놨다고 하면, 거기서 n 개로 바뀐다.)
				
				int cartno = rs.getInt(1);	// 첫번째 컬럼값(cartno) , map 에 담아오지 않았음. sql 에서 select로 카트번호를 읽어오자.
				
				sql = " update tbl_cart set oqty = oqty + ?"
					+ " where cartno = ? ";		// PK 가 cartno 인 것		// 그러나, 이 cartno 를 map 에 담아오지는 않았으므로, SQL 문에서 읽어온다.
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(paraMap.get("oqty")) );	// 첫번째 주문량은 이미 map 에 담아져서 왔음. (Map 의 K,V 값이 String 이므로 int 로 바꿀 것)
				pstmt.setInt(2, cartno);	// 얘는 map 에 담아온게 아니고 오라클에서 select 해온 것
				
				n = pstmt.executeUpdate();	// update 하자.
			}
			else {
				// 해당 제품이 존재하지 않는 새로운 제품을 장바구니에 넣고자 하는 경우. (insert문)
				
				sql = " insert into tbl_cart(cartno, fk_userid, fk_pnum, oqty, registerday) "	// registerday 는 안써도 default sysdate임.
					+ " values(seq_tbl_cart_cartno.nextval, ?, ?, ?, default) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("userid"));	// 누구인지 알아온다. (Action 단에서 map 에 put 해온 것.)
				pstmt.setInt(2, Integer.parseInt(paraMap.get("pnum")));
				pstmt.setInt(3, Integer.parseInt(paraMap.get("oqty")));

				n = pstmt.executeUpdate();	// insert 하자.
				
			}
			
		} finally {
			close();
		}
		
		return n;
	}

	
	// 로그인한 사용자의 장바구니 목록을 조회하기
	@Override
	public List<CartVO> selectProductCart(String userid) throws SQLException {
		
		List<CartVO> cartList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			

			String sql = " select A.cartno, A.fk_userid, A.fk_pnum, "+
                    	 "        B.pname, B.pimage1, B.price, B.saleprice, B.point, A.oqty "+
                    	 " from tbl_cart A join tbl_product B "+
                    	 " on A.fk_pnum = B.pnum "+
                    	 " where A.fk_userid = ? "+
                    	 " order by A.cartno desc ";

			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, userid);	// 문자열 호환 가능.
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// 장바구니 목록에 담긴게 있다면 보여주도록 하자. (select)
				int cartno = rs.getInt("cartno");
	            String fk_userid = rs.getString("fk_userid");
	            int fk_pnum = rs.getInt("fk_pnum");
	            String pname = rs.getString("pname");
	            String pimage1 = rs.getString("pimage1");
	            int price = rs.getInt("price");
	            int saleprice = rs.getInt("saleprice");
	            int point = rs.getInt("point");
	            int oqty = rs.getInt("oqty");  // 주문량 				

            // List 로 넘겨주되, CartVO 에 담아서 넘긴다. 그러나 CartVO에는 컬럼이 4개 밖에 없다. 장바구니 목록에는 제품정보도 들어와야 하기 때문에
    		// ProductVO 에 담아줘야 한다.
	            ProductVO prodvo = new ProductVO();	// pvo 에 담는다.
	            prodvo.setPnum(fk_pnum);
	            prodvo.setPname(pname);
	            prodvo.setPimage1(pimage1);
	            prodvo.setPrice(price);
	            prodvo.setSaleprice(saleprice);
	            prodvo.setPoint(point);
	            
	            // *** 중요함 *** //
	            prodvo.setTotalPriceTotalPoint(oqty);	// 주문량에 따른 전체 가격, 포인트	            
	            // *** 중요함 *** //
	            
	            CartVO cvo = new CartVO();	// cvo 에 담는다. (cartVO 에 productVO 를 담는다.)
	            cvo.setCartno(cartno);
	            cvo.setUserid(fk_userid);
	            cvo.setPnum(fk_pnum);
	            cvo.setOqty(oqty);
	            cvo.setProd(prodvo);

	            cartList.add(cvo);	            
			}// end of while-------------------------------
			
		} finally {
			close();
		}		
		
		return cartList;
	}

	// 장바구니 테이블에서 특정 제품을 제거하기
	@Override
	public int delCart(String cartno) throws SQLException {

		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " delete from tbl_cart"
					   + " where cartno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cartno);
			
			n = pstmt.executeUpdate();
			
		} finally {

		}
		
		return n;
	}

	// 장바구니 테이블에서 특정 제품의 주문량을 변경하기
	@Override
	public int updateCart(Map<String, String> paraMap) throws SQLException {

		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " update tbl_cart set oqty = ? "
					   + " where cartno = ? ";
			
			// map 에서 가져온다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("oqty"));
			pstmt.setString(2, paraMap.get("cartno"));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
	}

	// 로그인한 사용자의 장바구니에 담긴 주문 총액 합계 및 총 포인트 합계 알아오기
	@Override
	public Map<String, String> selectCartSumPricePoint(String userid) throws SQLException {

		Map<String, String> resultMap = new HashMap<>();
		
		try {
			
			conn = ds.getConnection();

			String sql = " select NVL( sum(B.saleprice * A.oqty), 0 ) as SUMTOTALPRICE "+
						 "      , NVL( sum(B.point * A.oqty), 0 ) as SUMTOTALPOINT "+
						 " from tbl_cart A join tbl_product B "+
						 " on A.fk_pnum = B.pnum "+
						 " where A.fk_userid = ? ";

			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, userid);	// 문자열 호환 가능.
			
			rs = pstmt.executeQuery();
			rs.next();
			
			resultMap.put("SUMTOTALPRICE", rs.getString(1));
			resultMap.put("SUMTOTALPOINT", rs.getString(2));
			
			
		} finally {
			close();
		}		
		
		return resultMap;
	}

	// 주문번호(시퀀스 seq_tbl_order 값)을 채번해오는 것
	@Override
	public int getSeq_tbl_order() throws SQLException {

		int seq = 0;

		try {
			
			conn = ds.getConnection();

			String sql = " select seq_tbl_order.nextval "
					   + " from dual ";

			pstmt = conn.prepareStatement(sql);			
			
			rs = pstmt.executeQuery();
			rs.next();
			
			seq = rs.getInt(1);		// 첫번째 컬럼
			
		} finally {
			close();
		}		
		
		return seq;
	}

	
	// ===== Transaction 처리하기 ===== // ** 로직 : 장바구니 --> 주문 테이블 --> 주문상세 테이블
    // >> 앞에서 미리 했으므로 안함. 1. 주문 테이블에 입력되어야할 주문전표를 채번(select)하기 
    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
    
    // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete OR update)하기(수동커밋처리) 
    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 

    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
    // 8. **** SQL 장애 발생시 rollback 하기(rollback) ****  	
	@Override
	public int orderAdd(Map<String, Object> paraMap) throws SQLException {

		int isSuccess = 0;
		int n1 = 0, n2 = 0 , n3 = 0, n4 = 0, n5 = 0;	// n1~n5까지 일련의 순서가 성공했을 때 다음 n1->n2->n3->n4->n5 순으로 실행되도록 한다.
		
		try {
			
			conn = ds.getConnection();
			
			
			conn.setAutoCommit(false);	// 롤백해야 하므로 수동 commit 처리 해준다.
			
		    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
			String sql = " insert into tbl_order(odrcode, fk_userid, odrtotalPrice, odrtotalPoint, odrdate)"
					   + " values(?, ?, ?, ?, default) ";
			
			pstmt = conn.prepareStatement(sql);						// Action 단에서 배열로 결과가 나오는 것 때문에 Map<String,Object> 로 해뒀다.
			pstmt.setString(1, (String)paraMap.get("odrcode"));		// map<String,Object> 타입으로 되어있음. 그러므로 다시 String 으로 바꿔준다.
			pstmt.setString(2, (String)paraMap.get("userid"));
			pstmt.setInt(3, Integer.parseInt((String)paraMap.get("sumtotalPrice")));
			pstmt.setInt(4, Integer.parseInt((String)paraMap.get("sumtotalPoint")));
			
			n1 = pstmt.executeUpdate();
			System.out.println("*** 확인용 n1 : "+ n1);

		    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
			// 2번이 먼저 주문테이블에 정상적으로 insert 되어야 함. (transaction 의 과정이다.)
			if(n1 == 1) {	
				// 여러개 주문 하니까 배열타입으로 map 에 put 한 것을 map 에서 get 해오는 것이다.
				String[] pnumArr = (String[])paraMap.get("pnumArr");	// 제품번호 배열을 가져온다.	// String[] 타입(배열) --> 그냥 get 해오면 Object 타입이므로 String[] casting 한다.
				String[] oqtyArr = (String[])paraMap.get("oqtyArr");	// 주문량 배열 
				String[] totalPriceArr = (String[])paraMap.get("totalPriceArr");	// 총 주문가격 배열
			
				int cnt = 0;
				for(int i=0; i<pnumArr.length; i++) {	// 주문 상세 테이블에 위의 pnumArr 배열을 넣어주자.
					sql = " insert into tbl_orderdetail(odrseqnum, fk_odrcode, fk_pnum, oqty, odrprice, deliverStatus) "
						+ " values(seq_tbl_orderdetail.nextval, ?, to_number(?), to_number(?), to_number(?), default) ";
					
					// select 는 문자가 숫자로 호환 되지만, insert 할 때는 number 타입이라면, number 타입만 들어오는 것이 원칙이다.
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, (String)paraMap.get("odrcode"));	// Map 에서 넘어온 것은 전부 String, String[] 타입
					pstmt.setString(2, pnumArr[i]);			// value 값이 Object 타입, odrcode 는 String 타입이기 떄문에 casting 한다.
					pstmt.setString(3, oqtyArr[i]);			// DB 에서는 숫자타입이므로, sql문에 to_number 해주기 (자바에서 integer.parseInt 도 가능) OR 오라클에서 to_number(?) 가능
					pstmt.setString(4, totalPriceArr[i]);	
					
					pstmt.executeUpdate();
					cnt ++;
				}// end of for------------------------------------------
				
				if(cnt == pnumArr.length) {	//n1=1 일때, n2 실행
					n2 = 1;
				}
				System.out.println("*** 확인용 n2 : "+ n2);	// insert 성공 시
			}// end of if(n1 == 1)---------------------------

		    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) (재고량 감하기)
			if(n2 == 1) {	// 장바구니에서 넘어온 것이 n개 라면 n개 만큼 감해주기 (insert 대신 update)
				String[] pnumArr = (String[])paraMap.get("pnumArr");	// 제품번호 배열을 가져온다.	// String[] 타입(배열) --> 그냥 get 해오면 Object 타입이므로 String[] casting 한다.
				String[] oqtyArr = (String[])paraMap.get("oqtyArr");	// 주문량 배열 				

				int cnt = 0;
				for(int i=0; i<pnumArr.length; i++) {	// 배열의 갯수만큼 for 문, pnum 에 해당하는 제품의 주문만큼 잔고량 감해주기
					sql = " update tbl_product set pqty = pqty - ?"
						+ " where pnum = ? ";
					
					// select 는 문자가 숫자로 호환 되지만, insert 할 때는 number 타입이라면, number 타입만 들어오는 것이 원칙이다.
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(oqtyArr[i]));	// select 와 where 절은 호환 가능, (UPDATE 시) DB 에서는 숫자타입, 자바에서는 String 이므로. 자바에서 Integer.parseInt 를 해준다.
					pstmt.setString(2, pnumArr[i]);	// where 는 숫자-문자 호환 가능

					pstmt.executeUpdate();
					cnt ++;		// 실행 할때마다 몇번 실행했는지 count (배열 갯수만큼 count 되어야 한다.)
				}// end of for------------------------------------------				

				if(cnt == pnumArr.length) {	//n2=1 일때, n3 실행
					n3 = 1;
				}
				System.out.println("*** 확인용 n3 : "+ n3);	// update (n2) 성공 시				
			}// end of if(n2 == 1)-----------------------------------
			
		    // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete OR update)하기(수동커밋처리) 
		    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. (장바구니 테이블에서 삭제할 행이 0) << 
			// 즉, 장바구니에 들어왔을 때에만 해당 행들을 삭제해준다.
			if( paraMap.get("cartnojoin") != null && n3 == 1 ) {
				
				String cartnojoin = (String)paraMap.get("cartnojoin");	// String 타입의 배열.(Action 단 참고) Object 타입을 String 타입으로 바꾼다.
				
				// 장바구니에 물건이 담긴 상태로 주문 했을 때 && n3 까지의 과정이 모두 성공했을 때
				// 복수개 일 때 = 대신 in()절을 쓸 수 있다.
				sql= " delete from tbl_cart "
				   + " where cartno in ("+cartnojoin+") ";	
				//  !!! in 절은 위와 같이 직접 변수로 처리해야 함. !!! (위치홀더 자리에 바로 값이 들어온 것)
		        //  in 절에 사용되는 것들은 컬럼의 타입을 웬만하면 number 로 사용하는 것이 좋다. 
		        //  왜냐하면 varchar2 타입으로 되어지면 데이터 값에 앞뒤로 홑따옴표 ' 를 붙여주어야 하는데 이것을 만들수는 있지만 귀찮기 때문이다.    
		            
		        /*   
		            sql = " delete from tbl_cart "
		               + " where cartno in (?) ";
		            // !!! 위와 같이 위치홀더 ? 를 사용하면 하면 안됨. !!!       
		        */
				
			 	pstmt = conn.prepareStatement(sql);			 
			 	n4 = pstmt.executeUpdate();
				System.out.println("*** 확인용 n4 : "+ n4);	// update (n3) 성공 시			
				// 장바구니 비우기를 할 행이 3개라면, n4 는 3이 나오게 된다.(삭제된 행의 갯수가 나온다.)
				
			}// end of if( paraMap.get("cartnojoin") != null && n3 == 1 )-----------------------------------------
			
			if( paraMap.get("cartnojoin") == null && n3 == 1 ) {
				// "제품 상세 정보페이지"에서 "바로 주문하기" 를 한 경우 (장바구니에 담는 절차 없이 바로 주문)
				// 장바구니 번호인 paraMap.get("cartnojoin") 이 없는 것이다.
				
				n4 = 1;	// 바로주문하기 일때, n4 = 1; 을 기본값으로 준다.

				System.out.println("*** 바로 주문하기 인 경우 n4 : "+ n4);	// update (n3) 성공 시			
				// *** 바로 주문하기 인 경우 n4 : 1
				
			}// end of if( paraMap.get("cartnojoin") != null && n3 == 1 )----------------------
			
		    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
			// 회원 테이블을 Update 한다.
		    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. (장바구니 테이블에서 삭제할 행이 0) << 
			if(n4 > 0) {
				// DB 에서 숫자가 들어와야 - 가 가능하기 때문에 setInt 시 integer.parseInt 를 추가한다.
				sql = " update tbl_member set coin = coin - ?"
					+ " 					, point = point + ?"
					+ " where userid = ? ";

				pstmt = conn.prepareStatement(sql);			 
				
				pstmt.setInt(1, Integer.parseInt((String)paraMap.get("sumtotalPrice")));			// coin - ?
				pstmt.setInt(2, Integer.parseInt((String)paraMap.get("sumtotalPoint")));			// point + ?
				pstmt.setString(3, (String)paraMap.get("userid"));		// userid = ?				// Object 타입으로 넘어왔으니 String 으로 casting 해준다.
				
				n5 = pstmt.executeUpdate();	// userid 는 고유한 값이기 때문에 결과값은 1 이 나온다.
				System.out.println("*** 확인용 n5 : "+ n5);		
				
			}// end of if(n4 > 0)----------------------------------------------

			
		    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
			// 무조건 커밋하는 것이 아니라 앞선 과정들이 하나하나 다 성공해야 함.
			if(n1*n2*n3*n4*n5 > 0) { // 각각의 과정들이 성공시, 다 1이상의 값을 반환함
				// 수동커밋을 자동커밋으로 바꿨다.
				conn.commit();
				conn.setAutoCommit(false);	// 자동 커밋으로 전환
			
				System.out.println("확인용 n1*n2*n3*n4*n5 = " + (n1*n2*n3*n4*n5));
			
				isSuccess = 1;	// 커밋 후 1 값을 준다.
			}
			
		} catch (SQLException e) {	
			e.printStackTrace();
			
		    // 8. **** SQL 장애 발생시 rollback 하기(rollback) ****  	
			conn.rollback();			// 잘못됐을 때 rollback() 을 해준다.
			conn.setAutoCommit(false);	// 자동 커밋으로 전환			
			
			isSuccess = 0;				// 굳이 하지 않아도 되지만, 안전하게 넣어주도록 한다!
			
		} finally {
			close();
		}
		
		return isSuccess;	// 모든게 성공하면 1, 실패 시 0 return;
	}

	// 주문한 제품에 대해 email 보내기 시, email 내용에 넣을 주문한 제품번호들에 대한 제품정보를 얻어오는 것.
	@Override
	public List<ProductVO> getJumunProductList(String pnums) throws SQLException {

		List<ProductVO> jumunProductList = new ArrayList<>();
		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			// in() 절은 위치홀더(?) 를 쓸 수 없다.
			String sql = " select pnum, pname, fk_cnum, pcompany, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName, pqty, price, saleprice, fk_snum, pcontent, point "+
	                     "      , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
	                     " from tbl_product "
	                     + " where pnum in("+pnums+") ";
			
			pstmt = conn.prepareStatement(sql);		// sql 을 전달한 우편배달부.

			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			
			while(rs.next()) {				
				// select 되어진 것이 있다면 값을 담는다.
				 int pnum = rs.getInt("pnum");
	             String pname = rs.getString("pname");
	             int fk_cnum = rs.getInt("fk_cnum");
	             String pcompany = rs.getString("pcompany");
	             String pimage1 = rs.getString("pimage1");
	             String pimage2 = rs.getString("pimage2");
	             String prdmanual_systemFileName = rs.getString("prdmanual_systemFileName");
	             String prdmanual_orginFileName = rs.getString("prdmanual_orginFileName");
	             int pqty = rs.getInt("pqty");
	             int price = rs.getInt("price");
	             int saleprice = rs.getInt("saleprice");
	             int fk_snum = rs.getInt("fk_snum");
	             String pcontent = rs.getString("pcontent");
	             int point = rs.getInt("point");
	             String pinputdate = rs.getString("pinputdate");
	             
	             ProductVO productvo = new ProductVO(pnum, pname, fk_cnum, pcompany, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName, pqty, price, saleprice, fk_snum, pcontent, point, pinputdate); 
	             
	             jumunProductList.add(productvo);				
			}// end of while---------------------------------------
			
		} finally {
			close();
		}		
		
		return jumunProductList;
	}
    
	
    // *** 주문내역에 대한 페이징 처리를 위해 주문 갯수를 알아오기 위한 것으로
    //     관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 갯수만 알아오고,
    //     관리자로 로그인을 했을 경우에는 모든 사용자들이 주문한 갯수를 알아온다.
	@Override
	public int getTotalCountOrder(String userid) throws SQLException {

		int totalCountOrder = 0;
		
		// 총 주문갯수가 몇개인지 알아오기		
		try {

			conn = ds.getConnection();		
			// Datasource(== DBCP) 에서 존재하는 Connection을 하나 가지고 온다. 
			// 20개 중 1개의 커넥션을 사용하겠다. (return 타입이 Connection)
			
			// in() 절은 위치홀더(?) 를 쓸 수 없다.
			// admin 일 때는 모든 주문목록을 보여주고, 일반 회원일때는 자신의 주문 목록만 보여준다.
			String sql = " select count(*) "
					   + " from tbl_order A join tbl_orderdetail B "
					   + " on A.odrcode = B.fk_odrcode ";
			
			if("admin".equalsIgnoreCase(userid)) {
				// 관리자로 로그인했을 경우 모든 회원의 주문목록을 보여준다.
				pstmt = conn.prepareStatement(sql);
			}
			else {
				// 관리자가 아닌 일반사용자로 로그인한 경우 , 해당 회원을 알아와야 한다.
				sql += " where A.fk_userid = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
			}
			
			// sql 문을 실행한다.
			rs = pstmt.executeQuery();	// 리턴타입은 rs 이다. (DQL 문이므로 executeQuery)
			rs.next();
			
			totalCountOrder = rs.getInt(1);	// totalCountOrder : 주문목록의 개수가 몇개인지?
			
		} finally {
			close();
		}				
		
		return totalCountOrder;
	}


    // *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고,
    //     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.	
	@Override
	public List<Map<String, String>> getOrderList(String userid, int currentShowPageNo, int sizePerPage) throws SQLException {

		List<Map<String, String>> orderList = new ArrayList<>();

		try {
			
			conn = ds.getConnection();
			
			// 관리자용 회원들의 주문 목록
			String sql = " select odrcode, fk_userid, odrdate, odrseqnum, fk_pnum, oqty, odrprice, deliverstatus, "
					+ "       pname, pimage1, price, saleprice, point "
					+ " from "
					+ " ( "
					+ " select row_number() over (order by B.fk_odrcode desc, B.odrseqnum desc) AS RNO "
					+ "      , A.odrcode, A.fk_userid "
					+ "      , to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') AS odrdate "
					+ "      , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice "
					+ "      , case B.deliverstatus "
					+ "        when 1 then '주문완료' "
					+ "        when 2 then '배송중' "
					+ "        when 3 then '배송완료' "
					+ "        end AS deliverstatus "
					+ "    , C.pname, C.pimage1, C.price, C.saleprice, C.point "
					+ " from tbl_order A join tbl_orderdetail B "
					+ " on A.odrcode = B.fk_odrcode "
					+ " join tbl_product C "
					+ " on B.fk_pnum = C.pnum ";			
			
			if(!"admin".equals(userid)) {
				// 관리자가 아닌 일반 회원이 자신의 주문목록만 보기			
				sql += " where A.fk_userid = ? ";
			}
			
			sql += " ) V "
				+ " where RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			if(!"admin".equals(userid)) {
				// 관리자가 아닌 '일반 회원'이 자신의 주문목록만 보기			
				pstmt.setString(1, userid);
				pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1) ); // 공식
                pstmt.setInt(3, currentShowPageNo*sizePerPage ); // 공식	
            }
			
			else {
				// 관리자로 접속
				pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage-1) ); // 공식
                pstmt.setInt(2, currentShowPageNo*sizePerPage ); // 공식				
			}
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
			   // 컬럼 네임
			   String odrcode = rs.getString("odrcode");
			   String fk_userid = rs.getString("fk_userid");
			   String odrdate = rs.getString("odrdate");
			   String odrseqnum = rs.getString("odrseqnum");
			   String fk_pnum = rs.getString("fk_pnum");
			   String oqty = rs.getString("oqty");
			   String odrprice = rs.getString("odrprice");
			   String deliverstatus = rs.getString("deliverstatus");
			   String pname = rs.getString("pname");
			   String pimage1 = rs.getString("pimage1");
			   String price = rs.getString("price");
			   String saleprice = rs.getString("saleprice");
			   String point = rs.getString("point");
			
			   // select 된 결과물을 map 에 담는다.
			   Map<String, String> odrmap = new HashMap<>();
			   // KEY , VALUE
			   odrmap.put("ODRCODE", odrcode);
               odrmap.put("FK_USERID", fk_userid);
               odrmap.put("ODRDATE", odrdate);
               odrmap.put("ODRSEQNUM", odrseqnum);
               odrmap.put("FK_PNUM", fk_pnum);
               odrmap.put("OQTY", oqty);
               odrmap.put("ODRPRICE", odrprice);
               odrmap.put("DELIVERSTATUS", deliverstatus);
               odrmap.put("PNAME", pname);
               odrmap.put("PIMAGE1", pimage1);
               odrmap.put("PRICE", price);
               odrmap.put("SALEPRICE", saleprice);
               odrmap.put("POINT", point);		
               
               orderList.add(odrmap);               // List 에 odrmap 을 담자.
			}// end of while (rs.next())--------------------------------
			
		} finally {
			close();
		}
		
		return orderList;	// return 타입은 map 으로 된 List
	}

	
	// Ajax 를 이용한 제품후기를 작성하기전 해당 제품을 사용자가 실제 구매했는지 여부를 알아오는 것임. 구매했다라면 true, 구매하지 않았다면 false 를 리턴함.
	@Override
	public boolean isOrder(Map<String, String> paraMap) throws SQLException {

		// JOIN 으로 select 해오자.
		boolean bool = false;
		
		// '사용자'가 '구매한 제품'이 무엇인지
		try {
			
			conn = ds.getConnection();
			
			String sql = " select O.odrcode "
					   + " from tbl_order O join tbl_orderdetail D "
					   + " on O.odrcode = D.fk_odrcode "
					   + " where O.fk_userid = ? and D.fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("fk_userid"));
			pstmt.setString(2, paraMap.get("fk_pnum"));
			
			rs = pstmt.executeQuery();
			
			// select 된 행이 있는지 본다.
			bool = rs.next();	// 행이 있으면 true, 없으면 false (return 값 true/false)
			
			
		} finally {
			close();
		}
		
		return bool;
	}


	// 특정 회원이 특정 제품에 대해 좋아요에 투표하기(insert) *** 중복 insert (중복 투표) 금지 ***	
	@Override
	public int likeAdd(Map<String, String> paraMap) throws SQLException {
		// 0개 행 삭제 시 좋아요 또는 싫어요를 누른 적이 없는 것
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			// ** fk_userid, fk_pnum 두개가 1set 가 되도록 한다 (트랜잭션 처리 --> 수동커밋 필요)
			conn.setAutoCommit(false);	// 수동커밋으로 전환
			
			// 기존에 싫어요 테이블에 있었던 것을 delete 하고
			String sql = " delete from tbl_product_dislike "
					   + " where fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("pnum"));
			
			pstmt.executeUpdate();
			
			// 새롭게 좋아요 테이블에 insert 한다.
			sql = " insert into tbl_product_like(fk_userid, fk_pnum) "
				+ " values(?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("pnum"));
			
			n = pstmt.executeUpdate();		
			
			if(n==1) {
				// 정상적으로 delete 및 insert 두개가 실행됐을 때. "1 행이 insert" / 반대의 경우 0. (insert 가 제대로 되지 않음)
				conn.commit();
			}
			
			
		} catch(SQLIntegrityConstraintViolationException e) {
			// 중복투표 방지 (중복투표시 (같은 데이터 중복 입력 시 제약조건 오류 발생)
		//	e.printStackTrace();	// 오류 시 ORA-00001: unique constraint (MYMVC_USER.PK_TBL_PRODUCT_LIKE) violated
			conn.rollback();
		} finally {
			close();
		}
		
		return n;
	}

	
	// 특정 회원이 특정 제품에 대해 싫어요에 투표하기(insert)  *** 중복 insert (중복 투표) 금지 ***	
	@Override
	public int dislikeAdd(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			// ** fk_userid, fk_pnum 두개가 1set 가 되도록 한다 (트랜잭션 처리 --> 수동커밋 필요)
			conn.setAutoCommit(false);	// 수동커밋으로 전환
			
			// 기존에 좋아요 테이블에 있었던 것을 delete 하고
			String sql = " delete from tbl_product_like "
					   + " where fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("pnum"));
			
			pstmt.executeUpdate();
			
			// 새롭게 싫어요 테이블에 insert 한다.
			sql = " insert into tbl_product_dislike(fk_userid, fk_pnum) "
				+ " values(?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("pnum"));
			
			n = pstmt.executeUpdate();		
			
			if(n==1) {
				// 정상적으로 delete 및 insert 두개가 실행됐을 때. "1 행이 insert" / 반대의 경우 0.
				conn.commit();
			}			
			
		} catch(SQLIntegrityConstraintViolationException e) {
			// 중복투표 방지 (중복투표시 (같은 데이터 중복 입력 시 제약조건 오류 발생)
		//	e.printStackTrace();	// 오류 시 ORA-00001: unique constraint (MYMVC_USER.PK_TBL_PRODUCT_LIKE) violated
			conn.rollback();
		} finally {
			close();
		}
		
		return n;
	}

	
	// 특정 제품에 대한 좋아요,싫어요의 투표결과(select)	
	@Override
	public Map<String, Integer> getLikeDislikeCnt(String pnum) throws SQLException {

		Map<String, Integer> map = new HashMap<>();

		try {
			
			conn = ds.getConnection();
			
			String sql = " select "
					   + " 	(select count(*) "
					   + " 	from tbl_product_like "
					   + " 	where fk_pnum = ? ) AS LIKECNT "
					   + " , "
					   + " 	(select count(*) "
					   + " 	from tbl_product_dislike "
					   + " 	where fk_pnum = ? ) AS DISLIKECNT "
					   + " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, (pnum));
			pstmt.setString(2, (pnum));
			
			rs = pstmt.executeQuery();
			
			// select 된 행이 있는지 본다.
			// count(*) 은 무조건 값이 나오기 때문에 if 절을 쓸 필요가 없다.
			rs.next();
			
			map.put("likecnt", rs.getInt(1));
			map.put("dislikecnt", rs.getInt(2));
						
		} finally {
			close();
		}		
		
		return map;
	}

	
	// Ajax 를 이용한 특정 제품의 상품후기를 입력(insert)하기 
	@Override
	public int addComment(PurchaseReviewsVO reviewsVO) throws SQLException {

		int n = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " insert into tbl_purchase_reviews(review_seq, fk_userid, fk_pnum, contents, writeDate) "
	                    + " values(seq_purchase_reviews.nextval, ?, ?, ?, default) ";
	                  
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, reviewsVO.getFk_userid());
	         pstmt.setInt(2, reviewsVO.getFk_pnum());
	         pstmt.setString(3, reviewsVO.getContents());
	         
	         n = pstmt.executeUpdate();
	         
	      } finally {
	         close();
	      }
	      
	      return n;

	}


	// Ajax 를 이용한 특정 제품의 상품후기를 조회(select)하기	
	@Override
	public List<PurchaseReviewsVO> commentList(String fk_pnum) throws SQLException {
		// JOIN 해온다. (댓글-제품테이블)
		
		List<PurchaseReviewsVO> commentList = new ArrayList<>();
	      
	      try {
	         conn = ds.getConnection();
	         // 내가 화면에 보여주고 싶은 것이 무엇인지?
	         String sql = " select review_seq, fk_userid, name, fk_pnum, contents, to_char(writeDate, 'yyyy-mm-dd hh24:mi:ss') AS writeDate "+
	                      " from tbl_purchase_reviews R join tbl_member M "+
	                      " on R.fk_userid = M.userid  "+
	                      " where R.fk_pnum = ? "+
	                      " order by review_seq desc ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, fk_pnum);
	         
	         rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	            String contents = rs.getString("contents");
	            String name = rs.getString("name");
	            String writeDate = rs.getString("writeDate");
	            String fk_userid = rs.getString("fk_userid");
	            int review_seq = rs.getInt("review_seq");
	                                    
	            PurchaseReviewsVO reviewvo = new PurchaseReviewsVO();
	            reviewvo.setContents(contents);
	            
	            MemberVO mvo = new MemberVO();
	            mvo.setName(name);
	            
	            reviewvo.setMvo(mvo);
	            reviewvo.setWriteDate(writeDate);
	            reviewvo.setFk_userid(fk_userid);
	            reviewvo.setReview_seq(review_seq);
	            
	            commentList.add(reviewvo);
	         }         
	         
	      } finally {
	         close();
	      }
	      
	      return commentList;

	}

	
	// Ajax 를 이용한 특정 제품의 상품후기를 삭제(delete)하기	
	@Override
	public int reviewDel(String review_seq) throws SQLException {

		  int n = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " delete from tbl_purchase_reviews "
	         		    + " where review_seq = ? ";
	                  
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, review_seq);
	         
	         n = pstmt.executeUpdate();
	         
	      } finally {
	         close();
	      }
	      
	      return n;
	}

	// Ajax 를 이용한 특정 제품의 상품후기를 수정(update)하기	
	@Override
	public int reviewUpdate(Map<String, String> paraMap) throws SQLException {

	      int n = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " update tbl_purchase_reviews set contents = ? , writedate = sysdate "
	                    + " where review_seq = ? ";
	                  
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, paraMap.get("contents"));
	         pstmt.setString(2, paraMap.get("review_seq"));
	         
	         n = pstmt.executeUpdate();
	         
	      } finally {
	         close();
	      }
	      
	      return n;

	}

	
	// 영수증전표(odrcode)소유주에 대한 사용자 정보를 조회해오는 것.
	@Override
	public MemberVO odrcodeOwnerMemberInfo(String odrcode) throws SQLException {

		MemberVO mvo = null;	// select 는 여러명을 넘겨주는 것과 비슷하다.
		
		try {
			
			conn = ds.getConnection();
			
			// status 가 1이면 탈퇴한 회원이다..
			String sql = " select userid, name, email, mobile, postcode, address, detailaddress, extraaddress " +
					 	 "	 , gender, substr(birthday,1,4) as birthyyyy, substr(birthday,6,2) as birthmm, substr(birthday,9,2) as birthdd " +
						 "	 , coin, point, to_char(registerday, 'yyyy-mm-dd') as registerday " +
						 " from tbl_member " +
						 " where userid = ( select fk_userid " + 
						 " 					from   tbl_order " + 
						 "					where odrcode = ? ) ";
			
			pstmt = conn.prepareStatement(sql);			
			// 위치홀더 값 매핑		
			pstmt.setString(1, odrcode);	// odrcode (전표번호만 알면 사용자가 누구인지 조회 가능)
		
			rs = pstmt.executeQuery();

			if(rs.next()) {	// 유저아이디가 존재하는지 확인해야 한다. 1개만 나오기 때문에 while 문을 쓰지 않고 if를 쓴다. (껼과물이 있는지)
					mvo = new MemberVO();	// select 된 것이 있을 때에만 넣어 주겠다.		
					
					mvo.setUserid(rs.getString(1));
					mvo.setName(rs.getString(2));				
					mvo.setEmail(aes.decrypt(rs.getString(3)));		// 이메일을 다시 복호화해준다.
					mvo.setMobile(aes.decrypt(rs.getString(4)));		// 연락처를 다시 복호화해준다.
					mvo.setPostcode(rs.getString(5));
					mvo.setAddress(rs.getString(6));
					mvo.setDetailaddress(rs.getString(7));
					mvo.setExtraaddress(rs.getString(8));
					mvo.setGender(rs.getString(9));
		            
					mvo.setBirthday(rs.getString(10) + rs.getString(11) + rs.getString(12));	// MemberVO 에서도 birthday 하나로 묶었다.
					mvo.setCoin(rs.getInt(13));
					mvo.setPoint(rs.getInt(14));
					mvo.setRegisterday(rs.getString(15));				
			}

		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();	
		} finally {
			close();
		}
		
		return mvo;
	}


	// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 2(배송시작)로 변경하기
	@Override
	public int updateDeliverStart(String odrcodePnum) throws SQLException {
	
		int n = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         // in() 은 위치홀더 '?'가 안되고, 변수처리 해야한다.
	         String sql = " update tbl_orderdetail set deliverstatus = 2 "
	                    + " where fk_odrcode || '/' || fk_pnum in("+odrcodePnum+") ";
	                  
	         pstmt = conn.prepareStatement(sql);
	         n = pstmt.executeUpdate();	// update 된 행의 갯수가 몇개인지? (배송상태가 1-->2 로 Update)
	         
	      } finally {
	         close();
	      }
	      
	      return n;
	}

	// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 3(배송완료)로 변경하기
	@Override
	public int updateDeliverEnd(String odrcodePnum) throws SQLException {

		int n = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         // in() 은 위치홀더 '?'가 안되고, 변수처리 해야한다.
	         String sql = " update tbl_orderdetail set deliverstatus = 3 "
	                    + " where fk_odrcode || '/' || fk_pnum in("+odrcodePnum+") ";
	                  
	         pstmt = conn.prepareStatement(sql);
	         n = pstmt.executeUpdate();	// update 된 행의 갯수가 몇개인지? (배송상태가 1-->2 로 Update)
	         
	      } finally {
	         close();
	      }
	      
	      return n;
	      
	}

	
	// tbl_map(위,경도) 테이블에 있는 정보 가져오기 (select)
	@Override
	public List<Map<String, String>> selectStoreMap() throws SQLException {

		List<Map<String, String>> storeMapList = new ArrayList<>();	

		try {
			
			conn = ds.getConnection();
			
			String sql = " select storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex "
					   + " from tbl_map "
					   + " order by zindex asc ";
			
			pstmt = conn.prepareStatement(sql);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
	            HashMap<String, String> map = new HashMap<>();
	            map.put("STOREID", rs.getString("STOREID"));
	            map.put("STORENAME", rs.getString("STORENAME"));
	            map.put("STOREURL", rs.getString("STOREURL"));
	            map.put("STOREIMG", rs.getString("STOREIMG"));
	            map.put("STOREADDRESS", rs.getString("STOREADDRESS"));
	            map.put("LAT", rs.getString("LAT"));
	            map.put("LNG", rs.getString("LNG"));
	            map.put("ZINDEX", rs.getString("ZINDEX"));
	                        
	            storeMapList.add(map); 
	         }
						
		} finally {
			close();
		}		
		
		return storeMapList;
		
	}
	
}
