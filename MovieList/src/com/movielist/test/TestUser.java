package com.movielist.test;

import com.movielist.biz.GlobalConstants;
import com.movielist.biz.UserUtils;
import com.movielist.hibernate.User;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestUser {
	
	/**
	 * Test Case: create a user
	 * 	specify email, password and username
	 * 	verify user is created with non null id and specified email, password and username
	 */
	@org.junit.Test
	public void testCreateUser() {
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		String userName = "full name";
		
		JSONObject newUser = userUtils.createUser(userEmail, "password", userName);
		verifyUserCreatedCorrectly(newUser, userEmail, userName);
	}
	
	/**
	 * 	Test Case: Verify cannot create user with duplicate email
	 * 	Verify error message is correct
	 */
	@org.junit.Test
	public void testDuplicateEmail() {
		UserUtils userUtils = new UserUtils();
		//	create our first valid user and verify they are created correctly
		Calendar cal = Calendar.getInstance();
		String newUserEmail = "test@test"+cal.getTimeInMillis()+".com";

		JSONObject newUser = userUtils.createUser(newUserEmail, "password", "full name");
		verifyUserCreatedCorrectly(newUser, newUserEmail, "full name");
		
		//	now create that user using the same email.  Should not be able to create another user
		JSONObject duplicateUser = userUtils.createUser(newUserEmail, "password", "full name");
		assertTrue(duplicateUser.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)duplicateUser.get(GlobalConstants.MESSAGE_KEY), UserUtils.USER_EXISTS));
	}
		
	/**
	 * 	Test Case: Verify can retrieve user given correct username and password 
	 * 	1.  Create a user given an email and password
	 * 	2.  retrieve them using username and password
	 * 	3.  make sure the user ids are the same 
	 */
	@org.junit.Test
	public void testRetrieveUserWithCorrectCredentials(){
		//	first create our user and make sure they are created correctly
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		String userName = "full name";
		
		JSONObject newUser = userUtils.createUser(userEmail, "password", userName);
		verifyUserCreatedCorrectly(newUser, userEmail, userName);
		
		//	now retrieve them with the unencrypted password
		JSONObject retrievedUser = userUtils.authenticateUser(userEmail, "password");
		assertEquals(newUser.get(GlobalConstants.DATA_KEY).toString(), retrievedUser.get(GlobalConstants.DATA_KEY).toString());
	}
	
	/**
	 * 	Test Case: Verify get correct error message when trying to retrieve user 
	 * 		with incorrect password 
	 * 	1.  Create a user given an email and password
	 * 	2.  retrieve them using username and incorrect password
	 * 	3.  verify get correct error message 
	 */
	@org.junit.Test
	public void testRetrieveUserWithIncorrectCredentials(){
		//	first create our user and make sure they are created correctly
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		String userName = "full name";
		
		JSONObject newUser = userUtils.createUser(userEmail, "password", userName);
		verifyUserCreatedCorrectly(newUser, userEmail, userName);
		
		//	now retrieve them with the incorrect password
		JSONObject retrievedUser = userUtils.authenticateUser(userEmail, "badpassword");
		assertEquals(retrievedUser.get(GlobalConstants.STATUS_KEY), GlobalConstants.FAIL_KEY);
		assertTrue(verifyCorrectErrorMessage((JSONArray)retrievedUser.get(GlobalConstants.MESSAGE_KEY), UserUtils.USER_FAILED_AUTHENTICATION));
	}
	
	/**
	 * Test Case: create a user
	 * 	specify email, password and username
	 * 	verify user is created with non null id and specified email, password and username
	 */
	@org.junit.Test
	public void testRetrieveUserById() {
		//	first create the user
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		String userName = "full name";
		
		JSONObject newUser = userUtils.createUser(userEmail, "password", userName);
		verifyUserCreatedCorrectly(newUser, userEmail, userName);
		
		//	now retrieve them
		User createdUserObject = (User)newUser.get(GlobalConstants.DATA_KEY);
		JSONObject retrievedUser = userUtils.retrieveUserById(createdUserObject.getUserid());
		User retrievedUserObject = (User)retrievedUser.get(GlobalConstants.DATA_KEY);
		
		//	make sure they are equal
		assertTrue(createdUserObject.equals(retrievedUserObject));
	}
	
	/**
	 * Test to make sure we can create a user logging in through facebooks.
	 * Create the user with facebook id and name and assign them a user id
	 */
	@org.junit.Test
	public void testCreateFBUser(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String facebookid = String.valueOf(cal.getTimeInMillis());
		String facebookName = "full name";
			
		JSONObject newUser = userUtils.createFacebookUser(facebookid, facebookName);
		verifyFacebookUserCreatedCorrectly(newUser, facebookName, facebookid);
	}
	
	/**
	 * 	Test Case: Verify can retrieve user given correct username and password 
	 * 	1.  Create a user given an email and password
	 * 	2.  retrieve them using username and password
	 * 	3.  make sure the user ids are the same 
	 */
	@org.junit.Test
	public void testRetrieveFaceBookUserWithCorrectCredentials(){
		//	first create our user and make sure they are created correctly
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String facebookid = String.valueOf(cal.getTimeInMillis());
		String facebookName = "full name";
			
		JSONObject newUser = userUtils.createFacebookUser(facebookid, facebookName);
		verifyFacebookUserCreatedCorrectly(newUser, facebookName, facebookid);
		
		//	now retrieve them by thier facebookId
		JSONObject retrievedUser = userUtils.retrieveFacebookUser(facebookid);
		assertEquals(newUser.get(GlobalConstants.DATA_KEY).toString(), retrievedUser.get(GlobalConstants.DATA_KEY).toString());
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with email that is null, empty or invalid (according to Apache Commons Validator)
	 * 	we get the correct error message
	 */
	@org.junit.Test
	public void testValidEmailCreate(){
		UserUtils userUtils = new UserUtils();
		
		JSONObject newUserNullEmail = userUtils.createUser(null, "password", "full name");
		assertTrue(newUserNullEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
						
		JSONObject newUserEmptyEmail = userUtils.createUser("", "password", "full name");
		assertTrue(newUserEmptyEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
				
		//	just test two cases for now, there are so many more- maybe a lib for all cases?
		JSONObject newUserinvalidEmail1 = userUtils.createUser("foo", "password", "full name");
		assertTrue(newUserinvalidEmail1.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserinvalidEmail1.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
		
		JSONObject newUserinvalidEmail2 = userUtils.createUser("foo@foo", "password", "full name");
		assertTrue(newUserinvalidEmail2.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserinvalidEmail2.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to login with an email is null, empty or invalid (according to Apache Commons Validator)
	 * 	we get the correct error message
	 */
	@org.junit.Test	
	public void testValidEmailLogin(){
		UserUtils userUtils = new UserUtils();
		
		JSONObject newUserNullEmail = userUtils.authenticateUser(null, "password");
		assertTrue(newUserNullEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
		
		JSONObject newUserEmptyEmail = userUtils.authenticateUser("", "password");
		assertTrue(newUserEmptyEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
		
		//	just test two cases for now, there are so many more- maybe a lib for all cases?
		JSONObject newUserinvalidEmail1 = userUtils.authenticateUser("foo", "password");
		assertTrue(newUserinvalidEmail1.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserinvalidEmail1.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
		
		JSONObject newUserinvalidEmail2 = userUtils.authenticateUser("foo@foo", "password");
		assertTrue(newUserinvalidEmail2.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserinvalidEmail2.get(GlobalConstants.MESSAGE_KEY), UserUtils.EMAIL_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with a password that is null or empty we get the correct error message
	 */
	@org.junit.Test
	public void testValidPasswordCreate(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		
		JSONObject newUserNullPassword = userUtils.createUser(userEmail, null, "full name");
		assertTrue(newUserNullPassword.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullPassword.get(GlobalConstants.MESSAGE_KEY), UserUtils.PASSWORD_INVALID));
		
		JSONObject newUserEmptyPassword = userUtils.createUser(userEmail, "", "full name");
		assertTrue(newUserEmptyPassword.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyPassword.get(GlobalConstants.MESSAGE_KEY), UserUtils.PASSWORD_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to login with a password that is null or empty we get the correct error message
	 */
	@org.junit.Test	
	public void testValidPasswordLogin(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		
		JSONObject newUserNullEmail = userUtils.authenticateUser(userEmail, null);
		assertTrue(newUserNullEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.PASSWORD_INVALID));
		
		JSONObject newUserEmptyEmail = userUtils.authenticateUser(userEmail, "");
		assertTrue(newUserEmptyEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyEmail.get(GlobalConstants.MESSAGE_KEY), UserUtils.PASSWORD_INVALID));		
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with a name that is null or empty we get the correct error message
	 */
	@org.junit.Test
	public void testValidNameCreate(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String userEmail = "test@test"+cal.getTimeInMillis()+".com";
		
		JSONObject newUserNullName = userUtils.createUser(userEmail, "password", null);
		assertTrue(newUserNullName.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullName.get(GlobalConstants.MESSAGE_KEY), UserUtils.NAME_INVALID));
		
		JSONObject newUserEmptyName = userUtils.createUser(userEmail, "password", "");
		assertTrue(newUserEmptyName.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyName.get(GlobalConstants.MESSAGE_KEY), UserUtils.NAME_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with a null or empty email, password and name
	 * 	we get multiple error messages
	 */
	@org.junit.Test
	public void testValidEmailPasswordNameCreate(){
		UserUtils userUtils = new UserUtils();
		
		//	test null parameters
		JSONObject newUserNullNamePasswordEmail = userUtils.createUser(null, null, null);
		assertTrue(newUserNullNamePasswordEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		String[] expectedNullErrors = new String[]{UserUtils.EMAIL_INVALID, UserUtils.PASSWORD_INVALID, UserUtils.NAME_INVALID};
		JSONArray actualNullErrors = (JSONArray)newUserNullNamePasswordEmail.get(GlobalConstants.MESSAGE_KEY);
		
		assertTrue(actualNullErrors.length() == expectedNullErrors.length);
		for (int i=0; i<expectedNullErrors.length; i++){
			assertTrue(expectedNullErrors[i].equals(actualNullErrors.get(i)));
		}
		
		//	test empty strings parameters
		JSONObject newUserEmptyNamePasswordEmail = userUtils.createUser("", "", "");
		assertTrue(newUserEmptyNamePasswordEmail.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
	
		String[] expectedEmptyStringErrors = new String[]{UserUtils.EMAIL_INVALID, UserUtils.PASSWORD_INVALID, UserUtils.NAME_INVALID};
		JSONArray actualEmptyStringErrors = (JSONArray)newUserNullNamePasswordEmail.get(GlobalConstants.MESSAGE_KEY);
		
		assertTrue(actualEmptyStringErrors.length() == expectedEmptyStringErrors.length);
		for (int i=0; i<expectedEmptyStringErrors.length; i++){
			assertTrue(expectedEmptyStringErrors[i].equals(actualEmptyStringErrors.get(i)));
		}		
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with a facebook id that is null or empty we get the correct error message
	 */
	@org.junit.Test
	public void testValidFacebookidCreate(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String facebookName = String.valueOf(cal.getTimeInMillis());
		
		JSONObject newUserNullFacebookid = userUtils.createFacebookUser(null, facebookName);
		assertTrue(newUserNullFacebookid.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullFacebookid.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));
		
		JSONObject newUserEmptyFacebookid = userUtils.createFacebookUser("", facebookName);
		assertTrue(newUserEmptyFacebookid.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyFacebookid.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to create a user with a facebook name that is null or empty we get the correct error message
	 */
	@org.junit.Test
	public void testValidFacebookNameCreate(){
		UserUtils userUtils = new UserUtils();
		Calendar cal = Calendar.getInstance();
		String facebookid = String.valueOf(cal.getTimeInMillis());
		
		JSONObject newUserNullFacebookName = userUtils.createFacebookUser(facebookid, null);
		assertTrue(newUserNullFacebookName.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullFacebookName.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));
		
		JSONObject newUserEmptyFacebookName = userUtils.createFacebookUser(facebookid, "");
		assertTrue(newUserEmptyFacebookName.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyFacebookName.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));
	}
	
	/**
	 * Test Case: Make sure if we try to create a facebook user with a null or empty name or id
	 * 	we get multiple error messages
	 */
	@org.junit.Test
	public void testValidFacebookNameIdCreate(){
		UserUtils userUtils = new UserUtils();
		
		//	test null parameters
		JSONObject newUserNullNameId = userUtils.createFacebookUser(null, null);
		assertTrue(newUserNullNameId.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		String[] expectedNullErrors = new String[]{UserUtils.FACEBOOK_PARAMETERS_INVALID, UserUtils.FACEBOOK_PARAMETERS_INVALID};
		JSONArray actualNullErrors = (JSONArray)newUserNullNameId.get(GlobalConstants.MESSAGE_KEY);
		
		assertTrue(actualNullErrors.length() == expectedNullErrors.length);
		for (int i=0; i<expectedNullErrors.length; i++){
			assertTrue(expectedNullErrors[i].equals(actualNullErrors.get(i)));
		}
		
		//	test empty strings parameters
		JSONObject newUserEmptyNameId = userUtils.createFacebookUser("", "");
		assertTrue(newUserEmptyNameId.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		String[] expectedEmptyErrors = new String[]{UserUtils.FACEBOOK_PARAMETERS_INVALID, UserUtils.FACEBOOK_PARAMETERS_INVALID};
		JSONArray actualEmptyErrors = (JSONArray)newUserEmptyNameId.get(GlobalConstants.MESSAGE_KEY);
				
		assertTrue(actualEmptyErrors.length() == expectedEmptyErrors.length);
		for (int i=0; i<expectedEmptyErrors.length; i++){
			assertTrue(expectedEmptyErrors[i].equals(actualEmptyErrors.get(i)));
		}		
	}

	/**
	 * Test Case: Make sure if we try to login with a facebookid that is null or empty we get the correct error message
	 */
	@org.junit.Test	
	public void testValidFacebookidLogin(){
		UserUtils userUtils = new UserUtils();
		
		JSONObject newUserNullFacebookid = userUtils.retrieveFacebookUser(null);
		assertTrue(newUserNullFacebookid.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserNullFacebookid.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));
		
		JSONObject newUserEmptyFacebookid = userUtils.retrieveFacebookUser("");
		assertTrue(newUserEmptyFacebookid.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY));
		assertTrue(verifyCorrectErrorMessage((JSONArray)newUserEmptyFacebookid.get(GlobalConstants.MESSAGE_KEY), 
				UserUtils.FACEBOOK_PARAMETERS_INVALID));		
	}
		
	private void verifyUserCreatedCorrectly(JSONObject userObject, String userEmail, String userName){
		assertTrue(userObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY));
		User createdUser = (User)userObject.get(GlobalConstants.DATA_KEY);
		assertNotNull(createdUser.getUserid());
		assertTrue(createdUser.getEmail().equals(userEmail));
		assertTrue(createdUser.getName().equals(userName));
	}
	
	private void verifyFacebookUserCreatedCorrectly(JSONObject userObject, String facebookName, String facebookid){
		assertTrue(userObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.SUCCESS_KEY));
		User createdUser = (User)userObject.get(GlobalConstants.DATA_KEY);
		assertNotNull(createdUser.getUserid());
		assertTrue(createdUser.getName().equals(facebookName));
		assertTrue(createdUser.getFacebookid().equals(facebookid));
	}
	
	private boolean verifyCorrectErrorMessage(JSONArray errorMessages, String message){
		if (errorMessages.length() == 1 && errorMessages.get(0).equals(message)){
			return true;
		} else {
			return false;
		}
	}
}
