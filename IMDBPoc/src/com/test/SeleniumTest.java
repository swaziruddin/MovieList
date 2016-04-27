package com.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTest {
	
	public static final String MOVIELISTMAINURL = "http://localhost:8080/IMDBPoc/";

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		WebDriver webDriver = new FirefoxDriver();
		webDriver.get(MOVIELISTMAINURL);
		System.out.println("Succesfully opened localhost");
		if (webDriver.findElement(By.id("searchTermText")).getText().equalsIgnoreCase("Search Term:")){
			System.out.println("Found search box text");
		} else {
			System.out.println("oops! Should have found search box text");
		}
		Thread.sleep(5);
		webDriver.quit();

	}

}
