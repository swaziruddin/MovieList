package com.movielist.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

/**
 * Class representing a user
 * 
 * @author swaziruddin
 *
 */
@Entity
@Table (name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USERID") 
	private long userid;
	
	@Column(name="EMAIL") 
	private String email;
	
	@Column(name="PASSWORD") 
	private byte[] password;
	
	@Column(name="SALT")
	private byte[] salt;
	
	@Column(name="NAME") 
	private String name;
	
	@Column(name="NUMBERFAILEDLOGIN") 
	private String numberfailedlogin;
	
	@Column(name="FACEBOOKID") 
	private String facebookid;
	
	public User(){}
	
	public User(long userid, String email, byte[] password, byte[] salt, String name, String numberfailedlogin, String facebookid) {
		super();
		this.userid = userid;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.name = name;
		this.numberfailedlogin = numberfailedlogin;
		this.facebookid = facebookid;
	}

	public User(String email, byte[] password, byte[] salt, String name) {
		super();
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.name = name;
	}
	
	public User(String name, String facebookid) {
		super();
		this.name = name;
		this.facebookid = facebookid;
	}
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumberfailedlogin() {
		return numberfailedlogin;
	}
	public void setNumberfailedlogin(String numberfailedlogin) {
		this.numberfailedlogin = numberfailedlogin;
	}
	public String getFacebookid() {
		return facebookid;
	}
	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	/**
	 * @return String representation of User
	 * 	Do not include encrypted password or salt because why?
	 */
	public String toString(){
		return "UserId: "+getUserid()
			+ " Email: "+getEmail()
			+ " Name: "+getName()
			+ " Number of Failed Logins: "+getNumberfailedlogin()
			+ " Facebook ID: "+getFacebookid();
	}
	
	/**
	 * @return JSON representation of User
	 * Do not include encrypted password or salt because why?
	 */
	public JSONObject toJSONObject(){
		JSONObject returnJSONObject = new JSONObject();
		returnJSONObject.put("UserId", getUserid());
		returnJSONObject.put("Email", getEmail());
		returnJSONObject.put("Name", getName());
		returnJSONObject.put("Number of Failed Logins", getNumberfailedlogin());
		returnJSONObject.put("Facebook ID", getFacebookid());
		
		return returnJSONObject;
	}
	
	/**
	 * return true if the following members are equal: 
	 * 	userid, email, facebookid, name and number of failed logins
	 * @param user
	 * @return
	 */
	public boolean equals (User user){
		return (getUserid() == user.getUserid() 
				&& ((getEmail() == null && user.getEmail() == null) ||
				(getEmail() != null && user.getEmail() != null && 
					getEmail().equals(user.getEmail())))
				&& ((getFacebookid() == null && user.getFacebookid() == null) ||
					(getFacebookid() != null && user.getFacebookid() != null &&
					getFacebookid().equals(user.getFacebookid())))
				&& getName().equals(user.getName())
				&& ((getNumberfailedlogin() == null && user.getNumberfailedlogin() == null) ||
					(getNumberfailedlogin() != null && user.getNumberfailedlogin() != null &&
						getNumberfailedlogin().equals(user.getNumberfailedlogin()))));
	}
}
