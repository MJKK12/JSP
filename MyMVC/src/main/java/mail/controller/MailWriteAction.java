package mail.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import common.controller.AbstractController;

public class MailWriteAction extends AbstractController {


public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {


		super.setViewPage("/WEB-INF/mail/mailWrite.jsp");
	
	}

}
	