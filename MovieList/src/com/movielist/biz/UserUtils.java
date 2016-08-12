package com.movielist.biz;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.json.JSONObject;

import com.movielist.hibernate.HibernateUtil;
import com.movielist.hibernate.User;

/**
 * Business layer implementation regarding the user
 * Allows creation of new user and authentication of existing users
 * 
 * Error checking
 * 
 * @author swaziruddin
 *
 */
//	TODO:make this is singleton?
public class UserUtils {
	public static final String USER_EXISTS = "Email is taken. Cannot create new user.";
	public static final String USER_FAILED_AUTHENTICATION = "Authentication failed.  Try again";
	public static final String USER_DOESNT_EXIST = "Email not found.  Try again or sign up";
	public static final String EMAIL_INVALID = "Email is invalid.  Please enter a valid email";
	public static final String PASSWORD_INVALID = "Password is invalid.  Please enter a valid password";
	public static final String NAME_INVALID = "Name is invalid.  Please enter a valid name";
	public static final String USERID_INVALID = "User Id is invalid.  Please supply a valid user id.";
	//	facebook credentials error messages- these should only occur if facebook is down
	public static final String FACEBOOK_PARAMETERS_INVALID = "Facebook login service is not available.  Please try again.";
	
	/**
	 * Create a new user given the parameters
	 * @param email
	 * @param password
	 * @param name
	 * @return
	 * 	a JSON object that is jsend compliant
	 * 	If the user is created successfully, return the user as part of the JSON object
	 * 	Otherwise, return the error message(s)
	 */
	public JSONObject createUser(String email, String password, String name) {
		JSONObject returnObject = new JSONObject();
		if (!validateEmail(email)){
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, EMAIL_INVALID);
		}
		if (!validatePassword(password)){
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, PASSWORD_INVALID);
		}
		if (name == null || name.isEmpty()){
			//	add error messages...
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, NAME_INVALID);
		}
		//	if we found errors, return!
		if (returnObject.has(GlobalConstants.STATUS_KEY) && 
				returnObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY)){
			return returnObject;
		}		
		
		User user;		
		//	set password and salt encryption here...
		PasswordEncryptionService pes = new PasswordEncryptionService();
		byte[] salt;
		byte[] encryptedPassword;
		try {
			salt = pes.generateSalt();
			encryptedPassword = pes.getEncryptedPassword(password, salt);
			
			user = new User(email, encryptedPassword, salt, name);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e){
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		}
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.getTransaction();
			transaction.begin();
			session.save(user);
			//	flush so the data in the session is synchronized with what's in the db
			session.flush();
			transaction.commit();
			
			//	once we've successfully created our user, wrap it in a JSON object and return it
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.SUCCESS_KEY);
			returnObject.put(GlobalConstants.DATA_KEY, user);
			return returnObject;
		} catch (ConstraintViolationException cve){
			//	Constraint violation: email already exists
			if (transaction != null){
				transaction.rollback();
			}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, USER_EXISTS);
			return returnObject;
		} catch(Exception e){
			if (transaction != null){
				transaction.rollback();
			}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		} finally{
			session.close();
		}
	}
	
	/**
	 * Create a new user given the parameters
	 * @param facebookid
	 * @param name
	 * 	Parameters should be sent from facebook once user logs in using facebook credentials
	 * @return
	 * 	a JSON object that is jsend compliant
	 * 	If the user is created successfully, return the user as part of the JSON object
	 * 	Otherwise, return the error message(s)
	 */
	public JSONObject createFacebookUser(String facebookid, String facebookName) {
		JSONObject returnObject = new JSONObject();
		if (facebookid == null || facebookid.isEmpty()){
			//	add error messages...
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, FACEBOOK_PARAMETERS_INVALID);
		}
		if (facebookName == null || facebookName.isEmpty()){
			//	add error messages...
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, FACEBOOK_PARAMETERS_INVALID);
		}
		//	if we found errors, return!
		if (returnObject.has(GlobalConstants.STATUS_KEY) && 
				returnObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY)){
			return returnObject;
		}		
		
		User user = new User(facebookName, facebookid);
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.getTransaction();
			transaction.begin();
			session.save(user);
			//	flush so the data in the session is synchronized with what's in the db
			session.flush();
			transaction.commit();
			
			//	once we've successfully created our user, wrap it in a JSON object and return it
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.SUCCESS_KEY);
			returnObject.put(GlobalConstants.DATA_KEY, user);
			return returnObject;
		} catch (ConstraintViolationException cve){
			//	TODO: shouldn't this never happen- think about it?  We should do something useful...
			//	Constraint violation: facebookid already exists
			if (transaction != null){
				transaction.rollback();
			}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, USER_EXISTS);
			return returnObject;
		} catch(Exception e){
			if (transaction != null){
				transaction.rollback();
			}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		} finally{
			session.close();
		}
	}

	
	/**
	 * Authenticate user given email and password
	 * 
	 * @param email
	 * @param password
	 * @return
	 * 	a JSON object that is jsend compliant
	 * 	If the user authenticated successfully, return the authenticated user as part of the JSON object
	 * 	Otherwise, return the error message(s)
	 * 	
	 */
	public JSONObject authenticateUser(String email, String password){
		JSONObject returnObject = new JSONObject();
		
		//	first do some basic validation
		if (!validateEmail(email)){
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, EMAIL_INVALID);
		}
		if (!validatePassword(password)){
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, PASSWORD_INVALID);
		}
		//	if we found errors, return!
		if (returnObject.has(GlobalConstants.STATUS_KEY) && 
				returnObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY)){
			return returnObject;
		}
		
		Session session = null;
		Transaction transaction = null;
		User user;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.getTransaction();
	        transaction.begin();
			//	first retrieve the user using their email
	        String hql = "FROM User U WHERE U.email = :user_email";
	        @SuppressWarnings("rawtypes")
			Query query = session.createQuery(hql);
	        query.setParameter("user_email", email);
	        @SuppressWarnings("unchecked")
			List<User> results = (List<User>)query.getResultList();
	        if (results == null || results.isEmpty()){
	        	//oops can't find the user!
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_DOESNT_EXIST);
        		return returnObject;
	        }
	        user = results.get(0);
	        if (user == null){
	        	//	oops can't find the user!
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_DOESNT_EXIST);
        		return returnObject;
	        }
			//	now verify that the password is correct
			PasswordEncryptionService pes = new PasswordEncryptionService();
	        try {
	        	if (pes.authenticate(password, user.getPassword(), user.getSalt())){
	        		returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.SUCCESS_KEY);
	        		returnObject.put(GlobalConstants.DATA_KEY, user);
	        		return returnObject;
	        	} else{
	        		//	user didn't authenticate
	        		returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
	        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_FAILED_AUTHENTICATION);
	        		return returnObject;
	        	}	        	
	        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
	        	if (transaction != null){
	        		transaction.rollback();
	        	}
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
				returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
				return returnObject;
	        }
		} catch(Exception e){
			if (transaction != null){
        		transaction.rollback();
        	}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		} finally{
			session.close();
		}
	}
	
	/**
	 * Authenticate user given email and password
	 * 
	 * @param email
	 * @param password
	 * @return
	 * 	a JSON object that is jsend compliant
	 * 	If the user authenticated successfully, return the authenticated user as part of the JSON object
	 * 	Otherwise, return the error message(s)
	 * 	
	 */
	public JSONObject retrieveFacebookUser(String facebookid){
		JSONObject returnObject = new JSONObject();
		
		//	make sure facebookid isn't null or invalid
		if (facebookid == null || facebookid.isEmpty()){
			//	add error messages...
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, FACEBOOK_PARAMETERS_INVALID);
		}
		//	if we found errors, return!
		if (returnObject.has(GlobalConstants.STATUS_KEY) && 
				returnObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY)){
			return returnObject;
		}		
				
		Session session = null;
		Transaction transaction = null;
		User user;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.getTransaction();
	        transaction.begin();
			//	first retrieve the user using their facebookid
	        String hql = "FROM User U WHERE U.facebookid = :facebookid";
	        @SuppressWarnings("rawtypes")
			Query query = session.createQuery(hql);
	        query.setParameter("facebookid", facebookid);
	        @SuppressWarnings("unchecked")
			List<User> results = (List<User>)query.getResultList();
	        if (results == null || results.isEmpty()){
	        	//oops can't find the user!
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_DOESNT_EXIST);
        		return returnObject;
	        }
	        user = results.get(0);
	        if (user == null){
	        	//	oops can't find the user!
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_DOESNT_EXIST);
        		return returnObject;
	        }
	        //	we found our user- return them
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.SUCCESS_KEY);
    		returnObject.put(GlobalConstants.DATA_KEY, user);
    		return returnObject;
		} catch(Exception e){
			if (transaction != null){
        		transaction.rollback();
        	}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		} finally{
			session.close();
		}
	}
	
	 /**
	 * Given a userId, return the user wrapped in a JSON Object
	 * @param userid
	 * @return
	 * a JSON object that is jsend compliant
	 * 	If the user is created successfully, return the user as part of the JSON object
	 * 	Otherwise, return the error message(s)
	 */
	public JSONObject retrieveUserById(Long userId){
		JSONObject returnObject = new JSONObject();
		
		if (userId == null){
			//	add error messages...
			if (!returnObject.has(GlobalConstants.STATUS_KEY)){
				returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
			}
			returnObject.append(GlobalConstants.MESSAGE_KEY, USERID_INVALID);
		}
		//	if we found errors, return!
		if (returnObject.has(GlobalConstants.STATUS_KEY) && 
				returnObject.get(GlobalConstants.STATUS_KEY).equals(GlobalConstants.FAIL_KEY)){
			return returnObject;
		}		
				
		Session session = null;
		Transaction transaction = null;
		User user;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.getTransaction();
	        transaction.begin();
			//	first retrieve the user using their id
	        user = (User)session.get(User.class, userId);
	        if (user == null){
	        	//	oops can't find the user!
	        	returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.FAIL_KEY);
        		returnObject.append(GlobalConstants.MESSAGE_KEY, USER_DOESNT_EXIST);
        		return returnObject;
	        }
	        //	we found our user- return them
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.SUCCESS_KEY);
    		returnObject.put(GlobalConstants.DATA_KEY, user);
    		return returnObject;
		} catch(Exception e){
			if (transaction != null){
        		transaction.rollback();
        	}
			returnObject.put(GlobalConstants.STATUS_KEY, GlobalConstants.ERROR_KEY);
			returnObject.append(GlobalConstants.MESSAGE_KEY, GlobalConstants.SERVER_ERROR);
			return returnObject;
		} finally{
			session.close();
		}
	 }
	
	/**
	 * Verify email is not null, not empty and passes basic Apache Commons Validator validation
	 * 	no need to make sure email is unique as it is specified in the database
	 * @param email
	 * @return
	 * 	boolean true or false
	 */
	private boolean validateEmail(String email){
		if (email == null || email.isEmpty()){
			return false;
		} else if (!EmailValidator.getInstance().isValid(email)){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Verify password isn't null or empty
	 * This method can be expanded as password requirements are added
	 * 
	 * @param password
	 * @return
	 * 	boolean true or false
	 */
	private boolean validatePassword(String password){
		if (password == null || password.isEmpty()){
			return false;
		} else {
			return true;
		}
	}
}