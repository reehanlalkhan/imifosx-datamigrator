package org.ideoholic.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ideoholic.dao.LoginDao;

public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 2233093329064205559L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //invalidate the session if exists
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.html");
	}
}
