package com.movielist.biz;

public class GlobalConstants {
	
	//	jsend compliant JSON keys
	//	https://labs.omniti.com/labs/jsend
	public static final String STATUS_KEY = "status";
	
	//	three different statuses
	public static final String SUCCESS_KEY = "success";
	//	Fail key: When an API call is rejected due to invalid data or call conditions
	public static final String FAIL_KEY = "fail";
	//	Error key: When an API call fails due to an error on the server
	public static final String ERROR_KEY = "error";
	
	//	Data key: Acts as the wrapper for any data returned by the API call. 
	public static final String DATA_KEY = "data";
	//	Message key: A meaningful, end-user-readable (or at the least log-worthy) message, explaining what went wrong.
	public static final String MESSAGE_KEY = "message";
	
	//	GLOBAL ERROR MESSAGES
	public static final String SERVER_ERROR = "Server Error. Please try again";
	
}
