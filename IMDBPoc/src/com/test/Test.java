package com.test;

import static org.junit.Assert.assertTrue;

public class Test{
	
	@org.junit.Test
	public void testMessage(){
		String message = "message";		
		assertTrue("Strings should be equal!", message.equals("message"));
	}

}
