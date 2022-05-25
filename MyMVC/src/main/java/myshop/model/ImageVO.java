package myshop.model;

// DTO(Data Transfer Object) == VO(Value Object)
// DB 에서 select 해 올 것을 만들어 왔다.
public class ImageVO {
	
 	private int imgno;           
 	private String imgfilename;
 	
	public int getImgno() {
		return imgno;
	}
	public void setImgno(int imgno) {
		this.imgno = imgno;
	}
	public String getImgfilename() {
		return imgfilename;
	}
	public void setImgfilename(String imgfilename) {
		this.imgfilename = imgfilename;
	}

}
