/*
 * BaseController.java
 * 
 * created: 12-10-2016
 * modified: 12-10-2016
 * 
 * base class for shared functionality of LoginController and RegisterController
 */

package com.cjimgarten.controllers.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

public class BaseController {
	
	protected Connection conn;
	protected String username;
	protected String password;
	
	/**
	 * create an instance
	 */
	public BaseController(Connection conn, String username, String password) {
		this.conn = conn;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * returns a hashed password
	 */
	protected String hashPassword(String password) {
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] passwordDigest = md.digest();
	
			// create a hex string of the password hash
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < passwordDigest.length; i++) {
				buffer.append(Integer.toHexString(0xff & passwordDigest[i]));
			}
			hash = buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}
