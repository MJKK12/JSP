package chap03;

public class PersonDTO {

	private String name;
	private String school;
	private String color;
	private String[] food;
	
	// (기본생성자는 생략이 되어있는 상태이다.)
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
	
	////////////////////////////////////////////////////
	
	// 음식을 보여주기 위해서 새로운 메소드를 만든다. (배열, 복수개 values)
	public String getStrFood() {
		if(food != null) {
			return String.join(",", food);	// food 에 다 들어오는 것
		}
		else {
			return "";	// null 이라면 없는 값으로 "".
		}
	}
	
	
	
}
