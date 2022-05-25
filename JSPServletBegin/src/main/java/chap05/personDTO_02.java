package chap05;

import java.util.Iterator;

public class personDTO_02 {
	// 오라클에서 테이블을 만들어온다.
	
	private int seq;          
	private String name;         
	private String school;      
	private String color;        
	private String[] food;      
	private String registerday;
	
	public int getSeq() {
		return seq;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSchool() {
		return school;
	}
	
	public void setSchool(String school) {
		this.school = school;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String[] getFood() {
		return food;
	}
	
	public void setFood(String[] food) {
		this.food = food;
	}
	
	public String getRegisterday() {
		return registerday;
	}
	
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
	////////////////////////////////////////////////////
	// personSelectAll.jsp 파일에서 food 부분에 null 값 들어갔을때를 대비해서 만든 메소드
	
	public String getStrFood() {
		
		if(food != null) {
			return String.join(",", food);
		}
		else {	// food 가 null 이라면..
			return "없음";
		}
		
	}// end of public String getStrFood()---------------------------
	
	public String getStrFoodImgFileName() {
		
		String result = null;

		if(food != null) {

			StringBuilder sb = new StringBuilder();
			
			for(int i=0; i<food.length; i++) {
				
				switch (food[i]) {	// String 타입의 food 배열, i번째(0,1,2,3,4)
				case "짜장면":
					sb.append("jjm.png");	// StringBuilder 에 넣어주겠다.
					break;

				case "짬뽕":
					sb.append("jjbong.png");						
					break;

				case "팔보채":
					sb.append("palbc.png");					
					break;
	
				case "탕수육":
					sb.append("tangsy.png");					
					break;
	
				case "양장피":
					sb.append("yang.png");					
					break;
				}// end of switch------------------------
				
				if(i < food.length-1 ) {
					sb.append(",");		// switch 문 빠져나온 후, 구분자를 준다.
				}
			}// end of for--------------------------------
			
			result = sb.toString();	// sb에 쌓인 것을 String 타입으로 바꾸자. (toString 은 생략가능하다.)			
		
		}
		
		return result;		// food == null 이면 null 값이 넘어간다. (그렇지 않으면 String 타입의 값을 return 한다.)

	}// end of public String getStrFoodImgFileName()----------
}
