package org.ideoholic.datamigrator.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ideoholic.datamigrator.dao.LoginDao;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 2233093329064205559L;

	public LoginServlet() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("uname");
		String password = request.getParameter("psw");
		response.setContentType("text/html");
		
		if(LoginDao.validate(name, password)) {
			createSessionOnLogin(request);
			RequestDispatcher dispatcher = request.getRequestDispatcher("displayServlet");
			dispatcher.forward(request, response);
		}else {
			PrintWriter responseWriter = response.getWriter();
			responseWriter.println("<font color=red><em>Username or password provided is invalid.");
			responseWriter.println("Please provide valid credentials.</em></font>");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
			dispatcher.include(request, response);
		}

	}
	
	private void createSessionOnLogin(HttpServletRequest request) {
		HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        //generate a new session
        HttpSession newSession = request.getSession(true);

        //setting session to expiry in 5 mins
        newSession.setMaxInactiveInterval(5*60);
	}

}
