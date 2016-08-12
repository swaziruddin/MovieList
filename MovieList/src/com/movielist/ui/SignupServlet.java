package com.movielist.ui;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.movielist.biz.GlobalConstants;
import com.movielist.biz.UserUtils;
import com.movielist.hibernate.User;

/**
 * Servlet that handles ui logic for signing up as a first time user
 * 
 * @author swaziruddin
 *
 */
@SuppressWarnings("serial")
public class SignupServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/signup.jsp");
	    rd.forward(request, response);	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		RequestDispatcher rd;
		//	login button was clicked, take them to the login page
		if (request.getParameter("login") != null) {		
			rd = getServletContext().getRequestDispatcher("/login.jsp");
		}
		//	they want to sign up
		else if (request.getParameter("signup") != null){
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserUtils userUtils = new UserUtils();
			JSONObject newUser = userUtils.createUser(email, password, name);
			//	verify we could create a user
			if (newUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY)){
				request.setAttribute("user", newUser.get(GlobalConstants.DATA_KEY));
				//	create a cookie that expires in 60 minutes and set the userid
				Cookie movielistCookie = new Cookie("userId", 
						String.valueOf(((User)newUser.get(GlobalConstants.DATA_KEY)).getUserid()));
	            movielistCookie.setMaxAge(60 * 60);
	            response.addCookie(movielistCookie);
	            //	set the username in the request so we can display it
	            request.setAttribute("userName", ((User)newUser.get(GlobalConstants.DATA_KEY)).getName());
				rd = getServletContext().getRequestDispatcher("/welcome.jsp");
			} else{
				//	if there are errors, put them in an arraylist so it's easier for the ui to 
				//	iterate through
				//	TODO: centralize this code
				int errorLength = ((JSONArray)newUser.get(GlobalConstants.MESSAGE_KEY)).length();
				String[] errors = new String[errorLength];
				for (int i=0; i<errorLength; i++){
					errors[i] = ((JSONArray)newUser.get(GlobalConstants.MESSAGE_KEY)).getString(i);
				}
				request.setAttribute("errors", errors);
				rd = getServletContext().getRequestDispatcher("/signup.jsp");
			}
		} else{
			//	default keep them on the sign up page
			rd = getServletContext().getRequestDispatcher("/signup.jsp");
		}
		
	    rd.forward(request, response);	
	}
}