package jdbc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyUtil {

	// *** 현재 날짜에서 n일 만큼 더해서 날짜형식을 문자열로 변경하여 리턴시켜주는 메소드 *** //
	public static String addDay(int n) {
		
	   Calendar currentDate = Calendar.getInstance(); 
	   // 현재날짜와 시간을 얻어온다.
	   
	   currentDate.add(Calendar.DATE, n);   		
	   // currentDate.add(Calendar.DATE, 1); 
	   // ==> currentDate(현재 날짜)에서 두번째 파라미터에 입력해준 숫자(그 단위는 첫번째 파라미터인 것이다. 지금은 Calendar.DATE 이므로 날짜수이다)만큼 더한다.
	   // ==> 위의 결과는 currentDate 값이 1일 더한 값으로 변한다.
	   
	   SimpleDateFormat sdatefmt = new SimpleDateFormat("yyyy-MM-dd");
	   // 문자열로 바꿔준다.(String pattern)
	   
	   return sdatefmt.format(currentDate.getTime()); // Calendar 가 date 로 바뀐다.
	   
	}// end of public static String addDay(int n)------------------------------------
	
}
