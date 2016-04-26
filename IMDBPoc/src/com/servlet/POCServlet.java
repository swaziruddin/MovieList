package com.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(name="POCServlet", urlPatterns={"/callIMDB"} )
public class POCServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
	    rd.forward(request, response);	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		URL url = new URL("http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=jeniffer+garner");
		DataInputStream dataInputStream = new DataInputStream(url.openStream());
		dataInputStream.toString();
		        
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
	    rd.forward(request, response);
	}
	
}
