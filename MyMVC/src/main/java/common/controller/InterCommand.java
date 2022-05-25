package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InterCommand {
	// 그냥 실행만 시킨다. void
		void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;		// 웹이 돌아가는 method.
		// Exception 은 최상위
		
}
