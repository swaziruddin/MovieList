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
 * Servlet that handles logic for logging in
 * 
 * @author swaziruddin
 *
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
	    rd.forward(request, response);	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		RequestDispatcher rd;
		//	the signup button was clicked, take them to the signup page
		if (request.getParameter("signup") != null){
			rd = getServletContext().getRequestDispatcher("/signup.jsp");
		} else if(request.getParameter("login") != null){
			//	the login button was clicked, check the credentials
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserUtils userUtils = new UserUtils();
			JSONObject retrievedUser = userUtils.authenticateUser(email, password);
			//	verify it's a success
			if (retrievedUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY)){
				setUpForRedirect(request, response, String.valueOf(((User)retrievedUser.get(GlobalConstants.DATA_KEY)).getUserid()), 
						((User)retrievedUser.get(GlobalConstants.DATA_KEY)).getName());
				rd = getServletContext().getRequestDispatcher("/welcome.jsp");
			} else {
				request.setAttribute("errors", putErrorsInArray(retrievedUser));
				rd = getServletContext().getRequestDispatcher("/login.jsp");
			}
		} else if (request.getParameter("facebookid") != null && request.getParameter("facebookname") != null){
			//	we are getting a facebooklogin
			String facebookid = request.getParameter("facebookid");
			String facebookName = request.getParameter("facebookname");
			
			UserUtils userUtils = new UserUtils();
			JSONObject retrievedFacebookUser = userUtils.retrieveFacebookUser(facebookid);
			if (retrievedFacebookUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY)){
				setUpForRedirect(request, response, String.valueOf(((User)retrievedFacebookUser.get(GlobalConstants.DATA_KEY)).getUserid()), 
						((User)retrievedFacebookUser.get(GlobalConstants.DATA_KEY)).getName());
				rd = getServletContext().getRequestDispatcher("/welcome.jsp");
			} else if (retrievedFacebookUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY) &&
					((JSONArray)retrievedFacebookUser.get(GlobalConstants.MESSAGE_KEY)).get(0).equals(UserUtils.USER_DOESNT_EXIST)){
				//	we need to create a user in our app
				JSONObject createdUser = userUtils.createFacebookUser(facebookid, facebookName);
				if (createdUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY)){
					setUpForRedirect(request, response, String.valueOf(((User)createdUser.get(GlobalConstants.DATA_KEY)).getUserid()), 
							((User)createdUser.get(GlobalConstants.DATA_KEY)).getName());
					rd = getServletContext().getRequestDispatcher("/welcome");
				} else{
					//	there were errors creating the user
					request.setAttribute("errors", putErrorsInArray(createdUser));
					rd = getServletContext().getRequestDispatcher("/login.jsp");
				}
			} else {
				request.setAttribute("errors", putErrorsInArray(retrievedFacebookUser));
				rd = getServletContext().getRequestDispatcher("/login.jsp");
			}
		} else {
			//	default keep them on login page
			rd = getServletContext().getRequestDispatcher("/login.jsp");
		}		
	    rd.forward(request, response);	
	}
	
	//	TODO:  pull out into a util
	private String[] putErrorsInArray(JSONObject userObject){
		//	if there are errors, put them in an arraylist so it's easier for the ui to 
		//	iterate through
		int errorLength = ((JSONArray)userObject.get(GlobalConstants.MESSAGE_KEY)).length();
		String[] errors = new String[errorLength];
		for (int i=0; i<errorLength; i++){
			errors[i] = ((JSONArray)userObject.get(GlobalConstants.MESSAGE_KEY)).getString(i);
		}
		return errors;
	}
	
	/**
	 * Set up the session and request for redirect.
	 * So far, this method sets a cookie with the userId in the session and adds the userName to the request 
	 */
	private void setUpForRedirect(HttpServletRequest request, HttpServletResponse response, 
			String userId, String userName){
		//	create a cookie that expires in 60 minutes and set the userid
		//	TODO: should this be something like movieListUserId
		Cookie movielistCookie = new Cookie("userId", userId);
        movielistCookie.setMaxAge(60 * 60);
        response.addCookie(movielistCookie);
        //	set the username in the request so we can display it
        request.setAttribute("userName", userName);
	}
}
